package com.zillowapp;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.*;
import android.net.*;
import android.widget.EditText;
import android.os.StrictMode;

public class MainActivity extends ActionBarActivity{
	ImageView zillow_logo;
	Button search_btn;
	EditText stAddressVal;
	EditText cityVal;
	Spinner stateValObj;
	String stateVal="Choose State";
	TextView stAddressError;
	TextView cityError;
	TextView StateError;
	String url="";
	Boolean firstLoad = true;
	public static final String TAG_RESULT = "result";
	public static final String TAG_CHART = "chart";
	public static final String TAG_ERROR = "errorCode";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       
        zillow_logo = (ImageView)findViewById(R.id.zillow_logo);
        zillow_logo.setOnClickListener(zillowLogoListener);
        
        search_btn = (Button)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(searchBtnListener);
       
        stAddressVal = (EditText)findViewById(R.id.street_address_val);
        stAddressVal.addTextChangedListener(addressChangeListener);
        cityVal = (EditText)findViewById(R.id.city_val);
        cityVal.addTextChangedListener(cityChangeListener);
        
        stateValObj = (Spinner)findViewById(R.id.state_Val);
        stateValObj.setOnItemSelectedListener(stateListener);
        
        stAddressError = (TextView)findViewById(R.id.stAddError);
        cityError = (TextView)findViewById(R.id.cityError);
        StateError = (TextView)findViewById(R.id.stateError);
        
        //hide the error messages on start.
        stAddressError.setVisibility(View.INVISIBLE);
        cityError.setVisibility(View.INVISIBLE);
        StateError.setVisibility(View.INVISIBLE);
        
        TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
		errorMessage.setVisibility(View.INVISIBLE);
        
    }  
    private TextWatcher addressChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if(!isEmpty(stAddressVal)){
				stAddressError.setVisibility(View.INVISIBLE);
			}
			else{
				stAddressError.setVisibility(View.VISIBLE);
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	private TextWatcher cityChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if(!isEmpty(cityVal)){
				cityError.setVisibility(View.INVISIBLE);
			}
			else{
				cityError.setVisibility(View.VISIBLE);
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
    		
    private OnItemSelectedListener stateListener = new OnItemSelectedListener() {
    	@Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            stateVal = parent.getItemAtPosition(position).toString().trim();
            if(stateVal.contentEquals("Choose State")){
            	if(!firstLoad){
            		StateError.setVisibility(View.VISIBLE);
            	}
            }
            else{
            	StateError.setVisibility(View.INVISIBLE);
            }
            firstLoad = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };
    private OnClickListener zillowLogoListener = new OnClickListener() {
		public void onClick(View v) {
			Uri uri = Uri.parse("http://www.zillow.com");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	};
	
	private OnClickListener searchBtnListener = new OnClickListener() {
		public void onClick(View v) {
			//check if all the text fields are not empty
			
			//make a call to API to get data
			
			Boolean isAddressEmpty = isEmpty(stAddressVal);
			Boolean isCityEmpty = isEmpty(cityVal);
			Boolean isStateEmpty = stateVal.trim().contentEquals("Choose State");
			
			//show the respective errors
			if(isAddressEmpty)
				stAddressError.setVisibility(View.VISIBLE);
			if(isCityEmpty)
				cityError.setVisibility(View.VISIBLE);
			if(isStateEmpty)
				StateError.setVisibility(View.VISIBLE);
			
			Boolean inputsValid = !(isAddressEmpty||isCityEmpty||isStateEmpty);
			if(inputsValid){
				String address = getText(stAddressVal);
				String city = getText(cityVal);
				String state = stateVal;
				
				url = "http://applicationj-env.elasticbeanstalk.com/?";
				List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("streetInput", address));
				params.add(new BasicNameValuePair("cityInput", city));
				params.add(new BasicNameValuePair("stateInput", state));
				
				String parameterString = URLEncodedUtils.format(params, "utf-8");
				url+=parameterString;
				new GetData().execute(url);
			}
			else{
				//some field is empty
			}
			
			
		}
	};

	private boolean isEmpty(EditText etText) {
	    if (etText.getText().toString().trim().length() > 0) {
	        return false;
	    } else {
	        return true;
	    }
	}
    
	private String getText(EditText etText){
		return etText.getText().toString().trim();
	}
	@Override    
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetData extends AsyncTask<String, Void, JSONObject>{
    	@Override
        public JSONObject doInBackground(String... urls) {
    		GetJsonDataFromAPI obj = new GetJsonDataFromAPI();
    		JSONObject jsonObject = obj.getJSONFromApi(urls[0]);
            return jsonObject;
        }
    	
    	@Override
        public void onPostExecute(JSONObject jsonObject) {
    		TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
	        //try {
	        	//check if an errorcode is recieved
	        	if(jsonObject.has(TAG_ERROR)){
	        		errorMessage.setVisibility(View.VISIBLE);
	        	}
	        	else{
	        		errorMessage.setVisibility(View.INVISIBLE);
	        		//JSONObject result = jsonObject.getJSONObject(TAG_RESULT);
	        		//call the new activity 
	        		Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
	        		intent.putExtra("searchData", jsonObject.toString());
	        		startActivity(intent);
	        	}
	        	
	       //} catch (JSONException e) {
	       //  e.printStackTrace();
	       //}
      }
    }
}
