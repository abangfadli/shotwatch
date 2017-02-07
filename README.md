# ShotWatch
Android Screenshot Watcher (Screenshot Detection) using `ContentObserver`

##Usage
```java
ShotWatch mShotWatch;


void onCreate() {
	Listener listener = new Listener() {
		public void onScreenShotTaken(ScreenShotData data) {
			textView.setText(data.getFileName());
			imageView.setImageURI(URI.parse(data.getPath());
		}
	}
	
	mShotWatch = new ShotWatch(getContentResolver(), listener);
}

// Register to begin receive event
void onResume() {
	mShotWatch.register();
}

// Don't forget to unregister when apps goes to background
void onPause() {
	mShotWatch.unregister();
}
```

## Gradle
`compile 'com.abangfadli.shotwatch:shotwatch:1.0.3'`