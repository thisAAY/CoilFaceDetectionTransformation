
# Coil face detection transformation

[![](https://jitpack.io/v/thisAAY/CoilFaceDetectionTransformation.svg)](https://jitpack.io/#thisAAY/CoilFaceDetectionTransformation)

### An Android image transformation library providing cropping above Face Detection for [Coil](https://github.com/coil-kt/coil/)

### How to use it?

STEP 1:
- Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
- Add the dependency
```
dependencies {
  implementation 'com.github.thisAAY:CoilFaceDetectionTransformation:Tag'
}
```
STEP 2:

Initialize the detector (May be in `onCreate()` method)

```kotlin
// Will use lifecyle to release resources
CoilFaceDetector.init(requireContext(), lifecycle)
```
Or
```kotlin
// Without the lifecyle you have to release it by your self using .release()
CoilFaceDetector.init(requireContext())
```

STEP 3:

Set Coil transform

```kotlin
imageView.load(photo){
  ...
  transformations(FaceCenterCrop())
}
```

**Note:** If no face is detected, it will fallback to CENTER CROP.

Library dependencies:
------
```
implementation("io.coil-kt:coil:1.1.0")
implementation 'com.google.mlkit:face-detection:16.0.3'
```


**If you liked it, please Star it.**

Huge thanks to  [GlideFaceDetectionTransformation](https://github.com/abhiint16/GlideFaceDetectionTransformation)
