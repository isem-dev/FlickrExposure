package com.cogniance.flickrexposure;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadContentTask extends AsyncTask<String, Void, List<String>> {

    private final String LOG_TAG = this.getClass().getName();

    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private TextView progressText;

    private List<String> imageURLArray;
    private List<String> imageTitlesArray;

    public DownloadContentTask(MainActivity mainActivity, ProgressBar progressBar, TextView progressText) {
        this.mainActivity = mainActivity;
        this.progressBar = progressBar;
        this.progressText = progressText;
    }

    public List<String> getImageURLArray() {
        return imageURLArray;
    }

    public List<String> getImageTitlesArray() {
        return imageTitlesArray;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String content = null;

        try {
            content = getContent(params[0]);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        if (content != null) {
            publishProgress();
        }

        FlickrXmlPullParser flickrXmlPullParser = new FlickrXmlPullParser();
        imageURLArray = flickrXmlPullParser.parse(content);
        imageTitlesArray = flickrXmlPullParser.getImageTitlesArray();

        return imageURLArray;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        progressText.setText(R.string.connected);
    }

    @Override
    protected void onPostExecute(List<String> imageURLArray) {
        if (imageURLArray != null) {
            progressText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            mainActivity.initListView();
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
