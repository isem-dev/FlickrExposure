package com.cogniance.flickrexposure;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends Activity {

    private final String LOG_TAG = this.getClass().getName();

    private Toolbar toolbar;

    private ProgressBar progressBar;
    private TextView progressText;

    private List<String> imagesArray;
    private List<String> titlesArray;

    private Parcelable listInstanceState;
    private ListView listView;
    private ImageAdapter imageAdapter;

    private DownloadContentTask downloadContentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        initToolbar();

//        if (savedInstanceState != null) {
////            imagesArray = savedInstanceState.getStringArrayList("imagesArrayState");
////            titlesArray = savedInstanceState.getStringArrayList("titlesArrayState");
//
//            listInstanceState = savedInstanceState.getParcelable("listViewState");
//            listView.onRestoreInstanceState(listInstanceState); //java.lang.NullPointerException
//
//        } else {
            initProgressBar();
            downloadContentTask = (DownloadContentTask) new DownloadContentTask(this, progressBar, progressText).execute(FlickrUser.getFlickrURL());//Separated thread for connection establishment and content downloading
//        }

        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putStringArrayList("imagesArrayState", (ArrayList<String>) imagesArray);
//        outState.putStringArrayList("titlesArrayState", (ArrayList<String>) titlesArray);

        outState.putParcelable("listViewState", listView.onSaveInstanceState());

        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        imagesArray = savedInstanceState.getStringArrayList("imagesArrayState");
//        titlesArray = savedInstanceState.getStringArrayList("titlesArrayState");

        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (downloadContentTask.getStatus() == AsyncTask.Status.FINISHED) {
            downloadContentTask.cancel(true);
        }

        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (downloadContentTask.getStatus() == AsyncTask.Status.RUNNING) {
            downloadContentTask.cancel(true);
        }

        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    Toast.makeText(getApplicationContext(), R.string.settings_clicked, Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.download) {
                    Toast.makeText(getApplicationContext(), R.string.download_clicked, Toast.LENGTH_SHORT).show();

                    initListView();
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);

        Log.d(LOG_TAG, "initToolbar");
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressText.setText(R.string.connection);
    }

    protected void initListView() {
        listView = (ListView) findViewById(R.id.listViewMain);
        imagesArray = downloadContentTask.getImageURLArray();
        titlesArray = downloadContentTask.getImageTitlesArray();
        imageAdapter = new ImageAdapter(getApplicationContext(), R.layout.imageitem, imagesArray, titlesArray);
        listView.setAdapter(imageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "item is selected: position = " + position + ", id = " + id);
            }
        });

        Log.d(LOG_TAG, "initListView");
    }

}
