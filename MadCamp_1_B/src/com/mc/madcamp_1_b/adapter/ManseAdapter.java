package com.mc.madcamp_1_b.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mc.madcamp_1_b.R;
import com.mc.madcamp_1_b.data.ManseData;

public class ManseAdapter extends BaseAdapter{
	private Context context, baseContext;
	private ArrayList<ManseData> manseList;
	private LayoutInflater inflater;
	private ImageView manseView;
	private LinearLayout checkView;
	
	public ManseAdapter(Context context, Context baseContext, ArrayList<ManseData> manseList) {
		this.context = context;
		this.baseContext = baseContext;
		
		this.manseList = manseList;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return manseList.size();
	}

	@Override
	public ManseData getItem(int position) {
		// TODO Auto-generated method stub
		return manseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		final ManseData manse = manseList.get(position);
		
		View view = v;
		
		if(v == null){
			view = inflater.inflate(R.layout.manse_view, null);
			
		} else {
			view = v;
		}
		
		if(manse != null){
			manseView = (ImageView)view.findViewById(R.id.manseView);
			checkView = (LinearLayout)view.findViewById(R.id.checkView);
			
//			LinearLayout.LayoutParams pr = (LinearLayout.LayoutParams)checkView.getLayoutParams();
//			
//			pr.height = pr.width;
//			
//			checkView.setLayoutParams(pr);
			
			manseView.setLayoutParams(new LinearLayout.LayoutParams(getCellWidthDP(), getCellWidthDP()));
			
			if(manse.isCheck())
				checkView.setBackgroundColor(baseContext.getResources().getColor(R.color.check));
			else
				checkView.setBackgroundColor(baseContext.getResources().getColor(R.color.bg));
			
			if(manse.getName().contains("manse")){
				String resName = "@drawable/" + manse.getName();
				String packName = baseContext.getPackageName();
				int resId = baseContext.getResources().getIdentifier(resName,
						"drawable", packName);

				manseView.setImageResource(resId);
			} else {
				try {
					Bitmap bm = Images.Media.getBitmap(context.getContentResolver(), Uri.parse(manse.getName()));
					float width = bm.getWidth();
					float height = bm.getHeight();
					float percente = (float) (width / 100);
					float scale = (float) (150 / percente);
					width *= (scale / 100);
					height *= (scale / 100);
					
					bm = Bitmap.createScaledBitmap(bm, (int)width, (int)height, true);
					
					manseView.setImageBitmap(bm);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return view;
	}
	
	private int getCellWidthDP(){
		int width = context.getResources().getDisplayMetrics().widthPixels;
		width -= width/10;
		int cellWidth = width/3;
		
		return cellWidth;
	}

}
