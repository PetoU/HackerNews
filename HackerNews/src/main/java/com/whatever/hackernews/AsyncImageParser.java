package com.whatever.hackernews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.whatever.hackernews.model.ImageRounder;

import java.io.InputStream;

/**
 * Created by PetoU on 11/06/14.
 */
public class AsyncImageParser extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public AsyncImageParser(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(Consts.IMAGE_PARSE + url).openStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.i("Image parse error", e.getMessage());
        }

        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
