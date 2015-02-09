package com.zillowapp;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.zillowapp.HistoricalZestimatesFragment;
import com.zillowapp.BasicInfoFragment;
public class FragmentAdapter extends FragmentPagerAdapter {
	JSONObject json = null; 
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    public FragmentAdapter(FragmentManager fm, JSONObject json){
    	super(fm);
    	this.json = json;
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new BasicInfoFragment(json);
        case 1:
            // Games fragment activity
            return new HistoricalZestimatesFragment(json);
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}
