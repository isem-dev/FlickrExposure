package com.cogniance.flickrexposure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private final String LOG_TAG = this.getClass().getName();

    private LayoutInflater layoutInflater;
    private List<String> imageURLArray;
    private List<String> imageTitlesArray;

    private LruCache<String, Bitmap> bitmapLruCache;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024); //kilobytes
    private final int cashSize = maxMemory / 18; // Use 1/8th of the available memory

    public ImageAdapter(Context context, int viewResourceId, List<String> imagesArray, List<String> titlesArray) {
        super(context, viewResourceId, titlesArray);

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageURLArray = imagesArray;
        imageTitlesArray = titlesArray;

        //Initialize memory cache
        bitmapLruCache = new LruCache<String, Bitmap>(cashSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size is measured in kilobytes
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static class ViewHolder {
        String imageURL;
        Bitmap bitmap;
        ImageView imageView;
        TextView titleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.imageitem, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.itemTexView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        // Image and title loading using AsyncTask via URL
        viewHolder.imageURL = imageURLArray.get(position);
        viewHolder.titleTextView.setText(imageTitlesArray.get(position));
        new DownloadAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, viewHolder);

        return convertView;
    }

    protected class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        // Decode image in background
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder viewHolder = params[0];
            viewHolder.bitmap = getBitmapFromMemCache(viewHolder.imageURL);

            if (viewHolder.bitmap == null) {
                URL imageURL;
                try {
                    imageURL = new URL(viewHolder.imageURL);
                    viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
                    addBitmapToMemoryCache(viewHolder.imageURL, viewHolder.bitmap);
                } catch (java.io.IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder viewHolder) {
            if (viewHolder.bitmap == null) {
                viewHolder.imageView.setImageResource(R.drawable.flickr_button_logo_vector);
            } else {
                viewHolder.imageView.setImageBitmap(viewHolder.bitmap);
            }
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return bitmapLruCache.get(key);
    }

}
