package com.mc.madcamp_1_b.data;

public class ManseData {
	private String name;
	private boolean check;
	
	public ManseData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ManseData(String name, boolean check) {
		super();
		this.name = name;
		this.check = check;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
}
