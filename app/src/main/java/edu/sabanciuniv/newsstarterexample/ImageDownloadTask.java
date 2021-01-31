package edu.sabanciuniv.newsstarterexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import edu.sabanciuniv.newsstarterexample.model.NewsItem;

public class ImageDownloadTask extends AsyncTask<NewsItem,Void, Bitmap> {

    ImageView imgView;
    public ImageDownloadTask (ImageView imgView)
    {
        this.imgView=imgView;
    }
    @Override
    protected Bitmap doInBackground(NewsItem... newsItems) {
        NewsItem current=newsItems[0];
        Bitmap bitmap=null;
        try {
            URL url =new URL(current.getImageId());
            InputStream is=new BufferedInputStream(url.openStream());
            bitmap= BitmapFactory.decodeStream(is);


        } catch (MalformedURLException e) {
            Log.e("DEV",e.getMessage());
        } catch (IOException e) {
            Log.e("DEV",e.getMessage());

        }
        return bitmap;

    }
    protected void onPostExecute(Bitmap bitmap)
    {
        imgView.setImageBitmap(bitmap);
    }

}
