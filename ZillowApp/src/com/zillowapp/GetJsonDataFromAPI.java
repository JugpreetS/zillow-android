package com.zillowapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.util.EntityUtils;
import android.util.Log;

public class GetJsonDataFromAPI {
	static JSONObject jsonObject = null;
	static String json = "";
	
	public JSONObject getJSONFromApi(String url){		
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet); 
			HttpEntity httpEntity = httpResponse.getEntity();
			json = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
        } catch (ClientProtocolException e) {
        	Log.e("Buffer Error", "Error converting result " + e.toString());
        } catch (IOException e) {
        	Log.e("Buffer Error", "Error converting result " + e.toString());
        }
		//parse to JSON object
		try{
			jsonObject = new JSONObject(json);
		} catch(JSONException e){
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		return jsonObject;
	}
}
