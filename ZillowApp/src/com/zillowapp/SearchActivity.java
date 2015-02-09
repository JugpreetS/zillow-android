package com.zillowapp;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.UserSettingsFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;


@SuppressWarnings("deprecation")
public class SearchActivity extends FragmentActivity implements ActionBar.TabListener{
	public static final String TAG_RESULT = "result";
	public static final String TAG_CHART = "chart";
	JSONObject result = null;
	JSONObject chart = null;
	JSONObject searchData = null;
	
	private ViewPager viewPager;
    private FragmentAdapter mAdapter;
    private ActionBar actionBar;
 
    private String[] tabs = { "BASIC INFO", "HISTORICAL ZESTIMATES" };
    private UiLifecycleHelper uiHelper;
    //private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        
        //FB
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		
		////FB
		 uiHelper = new UiLifecycleHelper(this, null);
		 uiHelper.onCreate(savedInstanceState);
		    
	    ///FB
		 
		 
      
		setContentView(R.layout.activity_search);
		TextView zestimate2 = (TextView)findViewById(R.id.zestimate2);
	      TextView zestimate3 = (TextView)findViewById(R.id.zestimate3);
	      zestimate2.setText(Html.fromHtml("Use is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\">Terms Of Use</a>"));
	      zestimate2.setMovementMethod(LinkMovementMethod.getInstance());
	      zestimate3.setText(Html.fromHtml("<a href=\"http://www.zillow.com/zestimate/\">What\'s a Zestimate?</a>"));
	      zestimate3.setMovementMethod(LinkMovementMethod.getInstance());
		try {
			searchData = new JSONObject(getIntent().getStringExtra("searchData"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), searchData);
 
        viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        //Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
 
    //@Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    
    }
 
    //@Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    //@Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
		/*try {
			JSONObject searchData = new JSONObject(getIntent().getStringExtra("searchData"));
			result = searchData.getJSONObject(TAG_RESULT);
			chart = searchData.getJSONObject(TAG_CHART);
			String home = result.getString("homedetails");
			TextView temp = (TextView)findViewById(R.id.temp);
			temp.setText(home);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
	
	//FB
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	
}
