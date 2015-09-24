package com.cogniance.flickrexposure;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    private Toolbar toolbar;

    private ProgressBar progressBar;
    private TextView progressText;

    private FlickrXmlPullParser flickrXmlPullParser;
    private List<String> imageURLArray;
    private List<String> imageTitlesArray;

    private ListView listView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        initToolbar();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressText.setText("Connection...");

        new DownloadContentTask().execute(FlickrUser.getFlickrURL());//Separated thread for connection establishment and content downloading

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.download) {
                    Toast.makeText(MainActivity.this, "Downloading", Toast.LENGTH_SHORT).show();

                    initListView();
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listViewMain);
        imageAdapter = new ImageAdapter(MainActivity.super.getApplicationContext(), R.layout.imageitem, imageURLArray, imageTitlesArray);
        listView.setAdapter(imageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(String.valueOf(R.string.app_name), "item is selected: position = " + position + ", id = " + id);
            }
        });
    }

    private class DownloadContentTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String content = null;

            try {
                content = getContent(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flickrXmlPullParser = new FlickrXmlPullParser();
            imageURLArray = flickrXmlPullParser.parse(content);

            imageTitlesArray = flickrXmlPullParser.getImageTitlesArray();

            return imageURLArray;
        }

        @Override
        protected void onPostExecute(List<String> imageURLArray) {
            if (imageURLArray != null) {
                progressText.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Connection established", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(MainActivity.this, "Downloading", Toast.LENGTH_SHORT)
                        .show();

                initListView();
            }
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setDoInput(true);

                connection.connect(); //android.os.NetworkOnMainThreadException fixed by means of separated thread via AsyncTask

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return (buffer.toString());

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}
