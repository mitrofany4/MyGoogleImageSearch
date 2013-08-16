package com.maksym.ABSPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		
		// Open FragmentTab1.java
		case 0:
			FragmentTab1 fragmenttab1 = new FragmentTab1();
			return fragmenttab1;

		// Open FragmentTab2.java
		case 1:
			FragmentTab2 fragmenttab2 = new FragmentTab2();
			return fragmenttab2;

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}
