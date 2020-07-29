# Android Activity Results

Activity Result APIs were introduced in AndroidX `Activity 1.2.0-alpha02` and `Fragment 1.3.0-alpha02` and they provide components for registering for a result, launching the result, and handling the result once it is dispatched by the system.


### Getting Started

Add the following dependencies to the app's `build.gradle` file. (At time of writing these were the latest versions of `activity-ktx` and `fragment-ktx`. Feel free to use any version later than this.)

```
dependencies {
    ...
    implementation 'androidx.activity:activity-ktx:1.2.0-alpha07'
    implementation 'androidx.fragment:fragment-ktx:1.3.0-alpha07'
    ...
}
```

### Resources

- [Android Developer Docs - Activity Result API](https://developer.android.com/training/basics/intents/result)
- [*Deep Dive into Activity Results API — No More onActivityResult()* by Wajahat Karim
](https://wajahatkarim.com/2020/05/activity-results-api-onactivityresult/)