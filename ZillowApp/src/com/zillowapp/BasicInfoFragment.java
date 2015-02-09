package com.zillowapp;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
public class BasicInfoFragment extends Fragment {
	JSONObject searchData = null;
	JSONObject result = null;
	String homeDetails, street, city, state, zipcode, address;
	String propertyType, lastSoldPrice, yearBuilt, lastSoldDate;
	String lotSizeSqFt, estimateLastUpdate, estimateAmount, finishedSqFt;
	String estimateValueChangeSign, estimateValueChange;
	String bathrooms, estimateValuationRangeLow, estimateValuationRangeHigh;
	String bedrooms, restimateLastUpdate, restimateAmount, taxAssessmentYear;
	String restimateValueChangeSign, restimateValueChange, taxAssessment;
	String restimateValuationRangeLow, restimateValuationRangeHigh;
	String nameFB, linkFB, lastSoldPriceFB, overallChangeFB;
	ImageView fb_share;
	String pictureFB;
	final Context context = getActivity();
	private UiLifecycleHelper uiHelper;
	AlertDialog alert; 
	
	
	//FB
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	public static final String TAG_RESULT = "result";
	public static final String TAG_CHART = "chart";
	public BasicInfoFragment(JSONObject json) {
		searchData = json;
	}
//	public static HistoricalZestimatesFragment newInstance(JSONObject json){
//		//HistoricalZestimatesFragment obj = new HistoricalZestimatesFragment();
//		Bundle args = new Bundle();
//		args.putString("searchData", json.toString());
//		//obj.setArguments(args);
//		//return obj;
//	}
	
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        
    	
    	
    	//setting footer info
		View rootView = inflater.inflate(R.layout.basic_info, container, false);
//        TextView zestimate2 = (TextView)rootView.findViewById(R.id.zestimate2);
//        TextView zestimate3 = (TextView)rootView.findViewById(R.id.zestimate3);
//        zestimate2.setText(Html.fromHtml("Use is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\">Terms Of Use</a>"));
//        zestimate2.setMovementMethod(LinkMovementMethod.getInstance());
//        zestimate3.setText(Html.fromHtml("<a href=\"http://www.zillow.com/zestimate/\">What\'s a Zestimate?</a>"));
//        zestimate3.setMovementMethod(LinkMovementMethod.getInstance());
		
		
        buildVariables(searchData);
        assignValues(rootView);
        //attach the fb_share to a handler
        fb_share = (ImageView)rootView.findViewById(R.id.fb_share);
        fb_share.setOnClickListener(fbShareClickListener);
        
        //FB
        Session session = Session.getActiveSession();
	        if (session == null) {
		        if (savedInstanceState != null) {
		        	session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
		        }
		        if (session == null) {
		        	session = new Session(getActivity());
		        }
		        Session.setActiveSession(session);
		        if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
		        	session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		        }
	        }
        //updateView();
        
        
        return rootView;
    }
	private void assignValues(View rootView) {
		TextView propertyLink = (TextView)rootView.findViewById(R.id.propertyLink);
		String propLink = "<a href=\""+homeDetails+"\">"+address+"</a>";
		propertyLink.setText(Html.fromHtml(propLink));
		propertyLink.setMovementMethod(LinkMovementMethod.getInstance());
		TextView propertyTypeVal = (TextView)rootView.findViewById(R.id.propertyTypeVal);
		propertyTypeVal.setText(propertyType);
		TextView yearBuiltVal = (TextView)rootView.findViewById(R.id.yearBuiltVal);
		yearBuiltVal.setText(yearBuilt);
		TextView lotSizeSqFtVal = (TextView)rootView.findViewById(R.id.lotSizeSqFtVal);
		lotSizeSqFtVal.setText(lotSizeSqFt);
		TextView finishedSqFtVal = (TextView)rootView.findViewById(R.id.finishedSqFtVal);
		finishedSqFtVal.setText(finishedSqFt);
		TextView bathroomsVal = (TextView)rootView.findViewById(R.id.bathroomsVal);
		bathroomsVal.setText(bathrooms);
		TextView bedroomsVal = (TextView)rootView.findViewById(R.id.bedroomsVal);
		bedroomsVal.setText(bedrooms);
		TextView taxAssessmentYearVal = (TextView)rootView.findViewById(R.id.taxAssessmentYearVal);
		taxAssessmentYearVal.setText(taxAssessmentYear);
		TextView taxAssessmentVal = (TextView)rootView.findViewById(R.id.taxAssessmentVal);
		taxAssessmentVal.setText(taxAssessment);
		TextView lastSoldPriceVal = (TextView)rootView.findViewById(R.id.lastSoldPriceVal);
		lastSoldPriceVal.setText(lastSoldPrice);
		TextView lastSoldDateVal = (TextView)rootView.findViewById(R.id.lastSoldDateVal);
		lastSoldDateVal.setText(lastSoldDate);
		TextView estimateAmountInfo = (TextView)rootView.findViewById(R.id.estimateAmount);
		String zestimatePropertyEstimate = getString(R.string.estimateAmount);
		zestimatePropertyEstimate+=" "+estimateLastUpdate;
		estimateAmountInfo.setText(zestimatePropertyEstimate);
		
//		ImageView overallChange = (ImageView)rootView.findViewById(R.id.estimateValueChangeSign);
//		if(estimateValueChangeSign.trim().contentEquals("-")){
//			overallChange.setImageResource(R.drawable.down);
//		}
//		else if(estimateValueChangeSign.trim().contentEquals("+")){
//			overallChange.setImageResource(R.drawable.up);
//		}
		
		TextView estimateAmountVal = (TextView)rootView.findViewById(R.id.estimateAmountVal);
		estimateAmountVal.setText(estimateAmount);
		TextView estimateValueChangeVal = (TextView)rootView.findViewById(R.id.estimateValueChangeVal);
		estimateValueChangeVal.setText(estimateValueChange);
		if(estimateValueChangeSign.contentEquals("-")){
			estimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
		}
		else if(estimateValueChangeSign.contentEquals("+")){
			estimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
		}
		
		
		TextView allTimePropertyRangeVal = (TextView)rootView.findViewById(R.id.allTimePropertyRangeVal);
		String allTimePropRange = estimateValuationRangeLow+"-"+estimateValuationRangeHigh;
		allTimePropertyRangeVal.setText(allTimePropRange);
		TextView restimateAmountInfo = (TextView)rootView.findViewById(R.id.restimateAmount);
		String restimateValuation = getString(R.string.restimateAmount);
		restimateValuation+=" "+restimateLastUpdate;
		restimateAmountInfo.setText(restimateValuation);
		
		
//		ImageView overallRentChange = (ImageView)rootView.findViewById(R.id.restimateValueChangeSign);
//		if(restimateValueChangeSign.trim().contentEquals("-")){
//			overallRentChange.setImageResource(R.drawable.down);
//		}
//		else if(restimateValueChangeSign.trim().contentEquals("+")){
//			overallRentChange.setImageResource(R.drawable.up);
//		}
		TextView restimateAmountVal = (TextView)rootView.findViewById(R.id.restimateAmountVal);
		restimateAmountVal.setText(restimateAmount);
		TextView restimateValueChangeVal = (TextView)rootView.findViewById(R.id.restimateValueChangeVal);
		restimateValueChangeVal.setText(restimateValueChange);
		if(restimateValueChangeSign.contentEquals("-")){
			restimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
		}
		else if(estimateValueChangeSign.contentEquals("+")){
			restimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
		}
		
		
		
		TextView allTimeRentChangeVal = (TextView)rootView.findViewById(R.id.allTimeRentChangeVal);
		String allTimeRentRange = restimateValuationRangeLow+"-"+restimateValuationRangeHigh;
		allTimeRentChangeVal.setText(allTimeRentRange);
	}

	public void buildVariables(JSONObject searchData){
		try{
			result = searchData.getJSONObject(TAG_RESULT);
			homeDetails = result.getString("homedetails");
			propertyType = result.getString("useCode");
			if (propertyType=="") {
                propertyType="N/A";
            }
			yearBuilt = result.getString("yearBuilt");
			if (yearBuilt.equals("")) {
                yearBuilt="N/A";
            }
			lastSoldPrice = result.getString("lastSoldPrice");
			if (lastSoldPrice.equals("")||lastSoldPrice.equals("0")||lastSoldPrice.equals("0.0")) {
                lastSoldPrice="N/A";
            }
            else{
                lastSoldPrice = "$"+lastSoldPrice;
            }
			lastSoldDate = result.getString("lastSoldDate");
			if (lastSoldDate.equals("") || lastSoldDate.equals("01-Jan-1970") || lastSoldDate.equals("31-Dec-1969")) {
                lastSoldDate="N/A";
            }
			lotSizeSqFt = result.getString("lotSizeSqFt");
			if (lotSizeSqFt.equals("")) {
                lotSizeSqFt="N/A";
            }
            else{
                lotSizeSqFt=lotSizeSqFt+" sq.ft.";
            }
			estimateLastUpdate = result.getString("estimateLastUpdate");
			if (estimateLastUpdate.equals("")|| estimateLastUpdate.equals("01-Jan-1970") || estimateLastUpdate.equals("31-Dec-1969")) {
                estimateLastUpdate="N/A";
            }
			estimateAmount = result.getString("estimateAmount");
			if (estimateAmount.equals("")|| estimateAmount.equals("0") || estimateAmount.equals("0.0")) {
                estimateAmount="N/A";
            }
            else{
                estimateAmount="$"+estimateAmount;
            }
			finishedSqFt = result.getString("finishedSqFt");
			if (finishedSqFt.equals("")) {
                finishedSqFt="N/A";
            }
            else{
                finishedSqFt = finishedSqFt+" sq.ft.";
            }
			//handle overallchangearrow.
			
			estimateValueChangeSign = result.getString("estimateValueChangeSign");
			
			estimateValueChange = result.getString("estimateValueChange");
			if (estimateValueChange.equals("") || estimateValueChange.equals("0") || estimateValueChange.equals("0.0")) {
                estimateValueChange="N/A";
            }
            else{
                estimateValueChange="$"+estimateValueChange;
            }
			bathrooms = result.getString("bathrooms");
			if (bathrooms.equals("")) {
                bathrooms = "N/A";
            }
			estimateValuationRangeLow = result.getString("estimateValuationRangeLow");
			if (estimateValuationRangeLow.equals("") || estimateValuationRangeLow.equals("0") || estimateValuationRangeLow.equals("0.0")) {
                estimateValuationRangeLow="N/A";
            }
            else{
                estimateValuationRangeLow="$"+estimateValuationRangeLow;
            }
			estimateValuationRangeHigh = result.getString("estimateValuationRangeHigh");
			if (estimateValuationRangeHigh.equals("") || estimateValuationRangeHigh.equals("0") || estimateValuationRangeHigh.equals("0.0")) {
                estimateValuationRangeHigh="N/A";
            }
            else{
                estimateValuationRangeHigh="$"+estimateValuationRangeHigh;
            }
			bedrooms = result.getString("bedrooms");
            if (bedrooms.equals("")) {
                bedrooms="N/A";
            }
            restimateLastUpdate = result.getString("restimateLastUpdate");
            if (restimateLastUpdate.equals("") || restimateLastUpdate.equals("01-Jan-1970") || restimateLastUpdate.equals("31-Dec-1969")) {
                restimateLastUpdate="N/A";
            }
            restimateAmount = result.getString("restimateAmount");
            if (restimateAmount.equals("") || restimateAmount.equals("0") || restimateAmount.equals("0.0")) {
                restimateAmount="N/A";
            }
            else{
                restimateAmount="$"+restimateAmount;
            }
            taxAssessmentYear = result.getString("taxAssessmentYear");
            if (taxAssessmentYear.equals("")) {
                taxAssessmentYear="N/A";
            }
            //handle rentchangeArrow.
            restimateValueChangeSign = result.getString("restimateValueChangeSign");
            
            restimateValueChange = result.getString("restimateValueChange");
            if (restimateValueChange.equals("") || restimateValueChange.equals("0") || restimateValueChange.equals("0.0")) {
                restimateValueChange="N/A";
            }
            else{
                restimateValueChange="$"+restimateValueChange;
            }
            taxAssessment = result.getString("taxAssessment");
            if (taxAssessment.equals("")) {
                taxAssessment="N/A";
            }
            else{
                taxAssessment = "$"+taxAssessment;
            }
            restimateValuationRangeLow = result.getString("restimateValuationRangeLow");
            if (restimateValuationRangeLow.equals("")|| restimateValuationRangeLow.equals("0") || restimateValuationRangeLow.equals("0.0")) {
            	restimateValuationRangeLow="N/A";
            }
            else{
            	restimateValuationRangeLow="$"+restimateValuationRangeLow;
            }
            
            restimateValuationRangeHigh = result.getString("restimateValuationRangeHigh");
            if (restimateValuationRangeHigh.equals("") || restimateValuationRangeHigh.equals("0") || restimateValuationRangeHigh.equals("0.0")) {
                restimateValuationRangeHigh="N/A";
            }
            else{
                restimateValuationRangeHigh="$"+restimateValuationRangeHigh;
            }
            JSONObject chart = searchData.getJSONObject(TAG_CHART);
	        String oneYear = chart.getJSONObject("1year").getString("url").toString();
	        
            street = result.getString("street");
            city = result.getString("city");
            state = result.getString("state");
            zipcode = result.getString("zipcode");
            address = street+", "+city+", "+state+"-"+zipcode;
            nameFB = address;
            linkFB = homeDetails;
            lastSoldPriceFB = lastSoldPrice;
            pictureFB = oneYear;
            if(estimateValueChangeSign.equals("-")){
            	overallChangeFB = "-"+estimateValueChange;
            }
            else{
            	overallChangeFB=estimateValueChange;
            }
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
	}

	
	private void publishFeedDialog() {
	    Bundle params = new Bundle();
	    params.putString("name", nameFB);
	    params.putString("caption", "Property information form Zillow.com");
	    params.putString("description", "<br/>Last Sold Price: "+lastSoldPriceFB+", 30 Days Overall Change: "+overallChangeFB);
	    params.putString("link", linkFB);
	    params.putString("picture", pictureFB);
	    
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(getActivity(),
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(getActivity(),
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(getActivity().getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getActivity().getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getActivity().getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }
	        })
	        .build();
	    feedDialog.show();
	}
	
	private OnClickListener fbShareClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			alert = new AlertDialog.Builder(getActivity())
			.setTitle("Post to Facebook")
		    .setPositiveButton("Post Property Details", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
//		        	@Override
//		        	    public void call(Session session, SessionState state, Exception exception) {
//		        	        if(session.isOpened()) {
		        	        	//publishFeedDialog();
//		        	        }
//		        	    }
//		        	});
		        	        	onClickLogin();
	        	
		        	
		        }
		    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
//		            Toast toast = new Toast(getActivity());
//		            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		            Toast toast = Toast.makeText(getActivity(), "Post Cancelled", Toast.LENGTH_SHORT);
		            toast.setGravity(Gravity.BOTTOM, 0, 0);
		            toast.show();
		        }
		    }).show();
			
		}
	};

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
			} else {
			Session.openActiveSession(getActivity(), true, statusCallback);
		}
	}
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if(state.isOpened()){
				publishFeedDialog();
			}
		}
	}
	
	private void updateView(){
		Session session = Session.getActiveSession();
		if(session.isOpened()){
			
		}
		else{
			onClickLogin();
		}
	}
	
	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
	 }
	 
	 @Override
	 public void onSaveInstanceState(Bundle outState) {
		 super.onSaveInstanceState(outState);
		 Session session = Session.getActiveSession();
		 Session.saveSession(session, outState);
	 }
}
