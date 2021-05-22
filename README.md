Attribution: the project makes use of Icons made by [Freepik](https://www.freepik.com "Freepik") from [www.flaticon.com](https://www.flaticon.com/ "Flaticon")

This demonstrates a shared element transition bug when a `ViewGroup` is one of the source shared elements. This results in a crash on the return transition when navigating back. Might be related to the bug fix: https://android-review.googlesource.com/q/I99675eac030325415789be0762aa666355f27dd8

The reason is that number of elements in the `sharedElementsIn` array exceeds the number of elements in the `sharedElementsOut` array.

Below code throws `IndexOutOfBoundsException` while iterating

Replacing
```groovy
implementation 'androidx.appcompat:appcompat:1.3.0'
```

with
```groovy
implementation 'androidx.appcompat:appcompat:1.2.0'
```

in the `app/build.gradle` eliminates the problem

```Java
    void setNameOverridesReordered(final View sceneRoot,
            final ArrayList<View> sharedElementsOut, final ArrayList<View> sharedElementsIn,
            final ArrayList<String> inNames, final Map<String, String> nameOverrides) {
        final int numSharedElements = sharedElementsIn.size();
        final ArrayList<String> outNames = new ArrayList<>();

        for (int i = 0; i < numSharedElements; i++) {
            final View view = sharedElementsOut.get(i);
            final String name = ViewCompat.getTransitionName(view);
            outNames.add(name);
            if (name == null) {
                continue;
            }
            ViewCompat.setTransitionName(view, null);
            final String inName = nameOverrides.get(name);
            for (int j = 0; j < numSharedElements; j++) {
                if (inName.equals(inNames.get(j))) {
                    ViewCompat.setTransitionName(sharedElementsIn.get(j), name);
                    break;
                }
            }
        }

        OneShotPreDrawListener.add(sceneRoot, new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < numSharedElements; i++) {
                    ViewCompat.setTransitionName(sharedElementsIn.get(i), inNames.get(i));
                    ViewCompat.setTransitionName(sharedElementsOut.get(i), outNames.get(i));
                }
            }
        });
    }

``` 