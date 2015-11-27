package com.cogniance.flickrexposure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.net.URL;


public class DisplayImageActivity extends Activity {

    private final String LOG_TAG = this.getClass().getName();

    private ImageView imageView;
    private Intent intent;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayimagescreen);

        imageView = (ImageView) findViewById(R.id.imageScreenView);
        intent = getIntent();

        ImageLoadThread imageLoadThread = new ImageLoadThread();
        imageLoadThread.start();
    }

    private class ImageLoadThread extends Thread {

        @Override
        public void run() {
            try {
                String urlString = intent.getStringExtra(MainActivity.IMAGE_URL);
                urlString = urlString.replace("_q.jpg", "_c.jpg"); //150x150 => 800 on longest side

                Log.d(LOG_TAG, urlString);

                URL imageURL = new URL(urlString);
                bitmap = BitmapFactory.decodeStream(imageURL.openStream());

                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
    }
}
