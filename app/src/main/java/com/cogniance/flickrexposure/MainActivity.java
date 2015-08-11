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
            "http://farm8.staticflickr.com/7315/9046944633_881f24c4fa.jpg",
            "http://farm3.staticflickr.com/2828/9046946983_923887b17d.jpg",
            "http://farm4.staticflickr.com/3810/9046947167_3a51fffa0b.jpg",
            "http://farm4.staticflickr.com/3773/9049175264_b0ea30fa75_s.jpg",
            "http://farm4.staticflickr.com/3781/9046945893_f27db35c7e_s.jpg",
            "http://farm6.staticflickr.com/5344/9049177018_4621cb63db_s.jpg",
            "http://farm8.staticflickr.com/7307/9046947621_67e0394f7b_s.jpg",
            "http://farm6.staticflickr.com/5457/9046948185_3be564ac10_s.jpg",
            "http://farm4.staticflickr.com/3752/9046946459_a41fbfe614_s.jpg",
            "http://farm8.staticflickr.com/7403/9046946715_85f13b91e5_s.jpg",
            "http://farm4.staticflickr.com/3777/9049174610_bf51be8a07_s.jpg",
            "http://farm8.staticflickr.com/7324/9046946887_d96a28376c_s.jpg"
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
