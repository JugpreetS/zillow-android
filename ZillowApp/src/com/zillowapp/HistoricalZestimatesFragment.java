package com.zillowapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View.OnClickListener;
import android.app.Notification.Style;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
 
public class HistoricalZestimatesFragment extends Fragment {
	JSONObject searchData=null;
	JSONObject result;
	JSONObject chart;
	String homeDetails, street, city, state, zipcode, address;
	String oneYear, fiveYear, tenYear;
	List<ImageView> images;
	List<Bitmap> imageBitmap;
	ImageView chartImage;
	TextView yearInfo;
	TextView addressInfo;
	String[] imageUrls;
	String[] yearInfoStrings;
	
	Button prevButton;
	Button nextButton;
	int currentImageAndText = 0;
	
	public static final String TAG_CHART = "chart";
	public static final String TAG_RESULT = "result";
	public HistoricalZestimatesFragment(JSONObject json) {
		this.searchData = json;
		images = new ArrayList<ImageView>();
		imageBitmap = new ArrayList<Bitmap>();
		imageUrls = new String[3];
		yearInfoStrings = new String[3];
	}
//	public static HistoricalZestimatesFragment newInstance(JSONObject json){
//		HistoricalZestimatesFragment obj = new HistoricalZestimatesFragment();
//		Bundle args = new Bundle();
//		args.putString("searchData", json.toString());
//		obj.setArguments(args);
//		return obj;
//	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.historical_zestimates, container, false);
        //define event handlers for button clicks.
        prevButton = (Button)rootView.findViewById(R.id.prev);
        nextButton = (Button)rootView.findViewById(R.id.next);
        
        prevButton.setOnClickListener(prevButtonClickListener);
        nextButton.setOnClickListener(nextButtonClickListener);
        
        
        
        
        
        
        
        
        //setting the footer info        
//        TextView zestimate2 = (TextView)rootView.findViewById(R.id.zestimate2);
//        TextView zestimate3 = (TextView)rootView.findViewById(R.id.zestimate3);
//        zestimate2.setText(Html.fromHtml("Use is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\">Terms Of Use</a>"));
//        zestimate2.setMovementMethod(LinkMovementMethod.getInstance());
//        zestimate3.setText(Html.fromHtml("<a href=\"http://www.zillow.com/zestimate/\">What\'s a Zestimate?</a>"));
//        zestimate3.setMovementMethod(LinkMovementMethod.getInstance());
        buildVariables(searchData);
        buildImages(oneYear, fiveYear, tenYear);
        /*textSwitcher = (TextSwitcher)rootView.findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(getActivity());
				textView.setGravity(Gravity.CENTER);
				return textView;
			}
		});*/
        addressInfo = (TextView)rootView.findViewById(R.id.zestimateHouseAddress);
        addressInfo.setText(address);
        chartImage = (ImageView)rootView.findViewById(R.id.imageChart);
        yearInfo = (TextView)rootView.findViewById(R.id.zestimateYearInfo);
        yearInfo.setText(R.string.zestimateYearOneInfoVal);
        yearInfo.setTypeface(null, Typeface.BOLD);
        
        
        return rootView;
    }
	private void buildImages(String oneYear, String fiveYear, String tenYear) {
		imageUrls[0] = oneYear;
		imageUrls[1] = fiveYear;
		imageUrls[2] = tenYear;
		
		new GetImage().execute(imageUrls);
		
		int a =2;
		int b=a;
	}
	
	private class GetImage extends AsyncTask<String, Void, List<Bitmap>>{

		@Override
		protected List<Bitmap> doInBackground(String... urls) {
			GetImages obj = new GetImages();
    		List<Bitmap> imgObject = obj.GetImage(urls);
            return imgObject;
		}

		@Override
		protected void onPostExecute(List<Bitmap> result) {
			
//			ImageView obj = new ImageView(getActivity());
//			obj.setImageBitmap(result);
//			images.add(obj);
			imageBitmap=result;
			chartImage.setImageBitmap(imageBitmap.get(0));
		}
	}

	private void buildVariables(JSONObject searchData) {
		try{
			result = searchData.getJSONObject(TAG_RESULT);
			street = result.getString("street");
	        city = result.getString("city");
	        state = result.getString("state");
	        zipcode = result.getString("zipcode");
	        address = street+", "+city+", "+state+"-"+zipcode;
	        
	        chart = searchData.getJSONObject(TAG_CHART);
	        oneYear = chart.getJSONObject("1year").getString("url");
	        fiveYear = chart.getJSONObject("5year").getString("url");
	        tenYear = chart.getJSONObject("10year").getString("url");
	        yearInfoStrings[0] = getString(R.string.zestimateYearOneInfoVal);
	        yearInfoStrings[1] = getString(R.string.zestimateYearFiveInfoVal);
	        yearInfoStrings[2] = getString(R.string.zestimateYearTenInfoVal);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
	}

	private OnClickListener prevButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(currentImageAndText>0){
				currentImageAndText--;
				chartImage.setImageBitmap(imageBitmap.get(currentImageAndText));
				yearInfo.setText(yearInfoStrings[currentImageAndText]);
			}
			else{
				currentImageAndText=2;
				chartImage.setImageBitmap(imageBitmap.get(currentImageAndText));
				yearInfo.setText(yearInfoStrings[currentImageAndText]);
			}
			
		}
	};
	
private OnClickListener nextButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(currentImageAndText<2){
				currentImageAndText++;
				chartImage.setImageBitmap(imageBitmap.get(currentImageAndText));
				yearInfo.setText(yearInfoStrings[currentImageAndText]);
			}
			else{
				currentImageAndText=0;
				chartImage.setImageBitmap(imageBitmap.get(currentImageAndText));
				yearInfo.setText(yearInfoStrings[currentImageAndText]);
			}
			
		}
	};
}
