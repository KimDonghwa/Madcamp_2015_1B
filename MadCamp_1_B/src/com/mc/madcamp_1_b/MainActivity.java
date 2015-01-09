package com.mc.madcamp_1_b;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private final long	FINSH_INTERVAL_TIME	= 2000;
	private long		backPressedTime		= 0;
	
	private int NUM_PAGES = 3;
	
	public final static int FRAGMENT_A = 0;
	public final static int FRAGMENT_B = 1;
	public final static int FRAGMENT_C = 2;
	
	private ViewPager pager;
	private Button aBtn, bBtn, cBtn;
	
	private pagerAdapter pA;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        aBtn = (Button)findViewById(R.id.aBtn);
        bBtn = (Button)findViewById(R.id.bBtn);
        cBtn = (Button)findViewById(R.id.cBtn);
        
        pager = (ViewPager)findViewById(R.id.pager);
        
        pA = new pagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pA);
        pager.setCurrentItem(FRAGMENT_A);
        
        pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				aBtn.setSelected(false);
				bBtn.setSelected(false);
				cBtn.setSelected(false);
				
				switch(position){
				case FRAGMENT_A:
					aBtn.setSelected(true);
					break;
				case FRAGMENT_B:
					bBtn.setSelected(true);
					break;
				case FRAGMENT_C:
					cBtn.setSelected(true);
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
        
        aBtn.setSelected(true);
        
        aBtn.setOnClickListener(this);
        bBtn.setOnClickListener(this);
        cBtn.setOnClickListener(this);
        
        /*
         * aBtn.setOnClickListener(new OnClickListener(){
         * 	public void onClick(View v){
         * 		dksjkf
         *  }
         * });
         */
        
    }
    
    private class pagerAdapter extends FragmentPagerAdapter{

		public pagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
				case FRAGMENT_A:
					return new Fragment_A();
				case FRAGMENT_B:
					return new Fragment_B();
				case FRAGMENT_C:
					return new Fragment_C();
				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NUM_PAGES;
		}
		
		
    	
    }

    @Override
	public void onClick(View v) {
    	switch(v.getId()){
    		case R.id.aBtn:
    			pager.setCurrentItem(FRAGMENT_A);
    			aBtn.setSelected(true);
    			bBtn.setSelected(false);
    			cBtn.setSelected(false);
    			break;
    		case R.id.bBtn:
    			pager.setCurrentItem(FRAGMENT_B);
    			aBtn.setSelected(false);
    			bBtn.setSelected(true);
    			cBtn.setSelected(false);
    			break;
    		case R.id.cBtn:
    			pager.setCurrentItem(FRAGMENT_C);
    			aBtn.setSelected(false);
    			bBtn.setSelected(false);
    			cBtn.setSelected(true);
    			break;
    	}
	}
    
    @Override
    public void onBackPressed() {
    	long tempTime = System.currentTimeMillis();
    	long intervalTime = tempTime - backPressedTime;
    	
    	if(pager.getCurrentItem() == FRAGMENT_A){
    		if( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime){
    			moveTaskToBack(true);
    			finish();
    			android.os.Process.killProcess(android.os.Process.myPid());
    		} else {
    			backPressedTime = tempTime;
    			Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
}
