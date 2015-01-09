package com.mc.madcamp_1_b;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View.MeasureSpec;

public class CameraSurfaceView extends SurfaceView {
	
	public CameraSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(
				MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}
}
