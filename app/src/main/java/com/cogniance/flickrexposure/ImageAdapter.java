package com.cogniance.flickrexposure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private LayoutInflater layoutInflater;
    private List<String> imageURLArray;
    private List<String> imageTitlesArray;

    public ImageAdapter(Context context, int viewResourceId, List<String> imagesArray, List<String> titlesArray) {
        super(context, viewResourceId, titlesArray);

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageURLArray = imagesArray;
        imageTitlesArray = titlesArray;
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
        new DownloadAsyncTask().execute(viewHolder);

        return convertView;
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        // Decode image in background
        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            ViewHolder viewHolder = params[0];
            URL imageURL;
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
