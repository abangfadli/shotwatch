package com.abangfadli.shotwatchapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.abangfadli.shotwatch.Listener;
import com.abangfadli.shotwatch.ScreenshotData;
import com.abangfadli.shotwatch.ShotWatch;


public class MainActivity extends AppCompatActivity {

    private ShotWatch mShotWatch;

    private TextView mText;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.text);
        mImage = (ImageView) findViewById(R.id.image);

        mShotWatch = new ShotWatch(getContentResolver(), new Listener() {
            @Override
            public void onScreenShotTaken(ScreenshotData screenshotData) {
                mText.setText(screenshotData.getFileName());
                Uri uri = Uri.parse(screenshotData.getPath());
                mImage.setImageURI(uri);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShotWatch.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShotWatch.unregister();
    }
}
