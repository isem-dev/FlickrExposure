package com.cogniance.flickrexposure;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Toolbar toolbar;

    private String[] imageURLArray = {
            //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[size_suffixe_letter].jpg
            "https://farm8.staticflickr.com/7628/17019645167_ba75287b72_q.jpg",
            "https://farm8.staticflickr.com/7613/17226551151_6ba61a0613_q.jpg",
            "https://farm8.staticflickr.com/7673/16606923443_dda3031f01_q.jpg",
            "https://farm9.staticflickr.com/8777/17019663617_9c12658bc1_q.jpg",
            "https://farm8.staticflickr.com/7701/17019727837_b2660e74f1_q.jpg",
            "https://farm8.staticflickr.com/7587/17039372438_81d67ef664_q.jpg",
            "https://farm9.staticflickr.com/8752/17039387838_3828eaa227_q.jpg",
            "https://farm9.staticflickr.com/8791/17201159096_9d707c1544_q.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        initToolbar();

        ListView listView = (ListView) findViewById(R.id.listViewMain);
        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.imageitem, imageURLArray);
        listView.setAdapter(imageAdapter);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

}
