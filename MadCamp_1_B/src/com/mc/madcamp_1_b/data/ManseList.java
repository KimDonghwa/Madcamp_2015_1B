package com.mc.madcamp_1_b.data;

import java.util.ArrayList;

public class ManseList {
	
	public ManseList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<ManseData> getManse(){
		ArrayList<ManseData> manse = new ArrayList<ManseData>();
		
		for (int i = 0; i < 25; i++){
			if(i < 10)
				manse.add(new ManseData("manse0" + i , false));
			else
				manse.add(new ManseData("manse" + i, false));
		}
		
		return manse;
	}
}
