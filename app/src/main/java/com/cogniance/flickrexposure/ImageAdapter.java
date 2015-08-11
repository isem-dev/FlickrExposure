package com.cogniance.flickrexposure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.net.URL;

public class ImageAdapter extends ArrayAdapter<String> {

    private String[] imageURLArray;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, int textViewResourceId, String[] imagesArray) {
        super(context, textViewResourceId, imagesArray);

//        layoutInflater = ((Activity)context).getLayoutInflater();
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageURLArray = imagesArray;
    }

    private static class ViewHolder {
        String imageURL;
        Bitmap bitmap;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.imageitem, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        // Image loading directly from resource
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//        Bitmap imageBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.flickr_button_logo_vector, options);
//        viewHolder.imageView.setImageBitmap(imageBitmap);

        // Image loading using an AsyncTask via URL
        viewHolder.imageURL = imageURLArray[position];
        new DownloadAsyncTask().execute(viewHolder);

        return convertView;
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        // Decode image in background
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder viewHolder = params[0];
            URL imageURL = null;
            try {
                imageURL = new URL(viewHolder.imageURL);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (java.io.IOException e) {
                e.printStackTrace();
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
}
