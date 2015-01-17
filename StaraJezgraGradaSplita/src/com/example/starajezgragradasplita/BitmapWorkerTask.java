package com.example.starajezgragradasplita;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

class BitmapWorkerTask extends AsyncTask<String, Integer/*Void*/, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private ProgressBar progressBar;

    public BitmapWorkerTask(ImageView imageView, ProgressBar tempProgressBar) {
        // Use a WeakReference to ensure the ImageView can be garbage
        // collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        progressBar = tempProgressBar;
    }
    
/*
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    }
*/
    
    @Override   
    protected void onProgressUpdate(Integer... values) {
	    int value = values[0];
	    progressBar.setProgress(value);
	    super.onProgressUpdate(values);
    }
      

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
    	Bitmap bitmap = null;
        try {
        	URL url = new URL(params [0]);
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	               
        	connection.connect();
        	int MAX = connection.getContentLength();
        	progressBar.setMax(MAX);
        	              
        	InputStream inputStream = connection.getInputStream();
        	            
        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        	 
        	byte[] buf = new byte[1024];
        	int len = 0;
        	int processBarNum = 0;
        	while ((len = inputStream.read(buf))!= - 1) {
        	outputStream.write(buf, 0, len);
        	processBarNum += len;
        	                   
        	publishProgress(processBarNum); // notification to update processBar   
        	}
        	bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, MAX);
        	inputStream.close();
        	return bitmap;
            //return BitmapFactory.decodeStream((InputStream) new URL(data)
            //        .getContent());
            //}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
            	progressBar.setVisibility(View.GONE);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
