package com.zillowapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetImages {
	List<Bitmap> images = new ArrayList<Bitmap>();
	HttpGet getRequest;
	
	public List<Bitmap> GetImage(String... urls){
		DefaultHttpClient client = new DefaultHttpClient();
		for(int i=0; i<urls.length; i++){
			{
				getRequest = new HttpGet(urls[i]);
			
				try {
					HttpResponse response = client.execute(getRequest);
					int statusCode = response.getStatusLine().getStatusCode();
			
					if (statusCode == HttpStatus.SC_OK) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							InputStream inputStream = null;
							try {
								// getting contents from the stream 
								inputStream = entity.getContent();
			
								// decoding stream data back into image Bitmap that android understands
								final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			
								images.add(bitmap);
							} finally {
								if (inputStream != null) {
									inputStream.close();
								}
								entity.consumeContent();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
		
			}
		}
		return images;
	}
}
