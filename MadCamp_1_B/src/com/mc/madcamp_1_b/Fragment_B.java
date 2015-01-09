package com.mc.madcamp_1_b;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mc.madcamp_1_b.adapter.ManseAdapter;
import com.mc.madcamp_1_b.data.ManseData;
import com.mc.madcamp_1_b.data.ManseList;

public class Fragment_B extends Fragment {
	private final long	FINSH_INTERVAL_TIME	= 2000;
	private long		backPressedTime		= 0;
	
	final int REQ_CODE_SELECT_IMAGE = 100;
	
	private GridView manseGrid;
	private ManseAdapter adapter;
	private ManseList makeList;
	private ArrayList<ManseData> manseList;
	private ImageView manseImage;
	private Button deleteBtn, addBtn;
	private boolean image, delete;
	
	public Fragment_B() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_b, container, false);
		
		image = false;
		delete = false;
		
		manseGrid = (GridView)view.findViewById(R.id.manseList);
		manseImage = (ImageView)view.findViewById(R.id.manseImage);
		deleteBtn = (Button)view.findViewById(R.id.delete);
		addBtn = (Button)view.findViewById(R.id.add);
		
		manseList = new ArrayList<ManseData>();
		
		makeList = new ManseList();
		
		manseList = makeList.getManse();
		
		adapter = new ManseAdapter(getActivity(), getActivity().getBaseContext(), manseList);
		
		manseGrid.setAdapter(adapter);
		
		manseGrid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(delete){
					if(manseList.get(position).isCheck()){
						manseList.get(position).setCheck(false);

						LinearLayout check = (LinearLayout) view
								.findViewById(R.id.checkView);
						check.setBackgroundColor(getActivity().getBaseContext()
								.getResources().getColor(R.color.bg));
					} else {
						manseList.get(position).setCheck(true);

						LinearLayout check = (LinearLayout) view
								.findViewById(R.id.checkView);
						check.setBackgroundColor(getActivity().getBaseContext()
								.getResources().getColor(R.color.check));
					}
				}
				else{
					if (!image) {
						if(manseList.get(position).getName().contains("manse")){
							String resName = "@drawable/"
									+ manseList.get(position).getName()
									+ "_origin";
							String packName = getActivity().getBaseContext()
									.getPackageName();
							int resId = getActivity()
									.getBaseContext()
									.getResources()
									.getIdentifier(resName, "drawable",
											packName);

							manseImage.setImageResource(resId);
						} else {
							try {
								Bitmap bm = Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(manseList.get(position).getName()));
								manseImage.setImageBitmap(bm);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						manseImage.setVisibility(View.VISIBLE);
						image = true;
					} else {
						manseImage.setVisibility(View.GONE);
						image = false;
					}
				}
			}

		});
		
		manseGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				
				delete = true;
				
				manseList.get(position).setCheck(true);
				
				LinearLayout check = (LinearLayout)view.findViewById(R.id.checkView);
				check.setBackgroundColor(getActivity().getBaseContext().getResources().getColor(R.color.check));
				
				return true;
			}
			
		});
		
		deleteBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				delete = false;
				
				int size = manseList.size();
				
				for(int i = size-1; i >= 0; i--){
					if(manseList.get(i).isCheck())
						manseList.remove(i);
				}
				
				adapter.notifyDataSetChanged();
				
				manseGrid.setAdapter(adapter);
				
				//manseGrid.setAdapter(new ManseAdapter(getActivity(), getActivity().getBaseContext(), manseList));
			}
			
		});
		
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent image = new Intent(Intent.ACTION_PICK);
				image.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				image.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(image, REQ_CODE_SELECT_IMAGE);
				
			}
			  
		});
		
		view.setFocusableInTouchMode(true);
        view.requestFocus();
		
		view.setOnKeyListener(new View.OnKeyListener() {
            
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				long tempTime = System.currentTimeMillis();
		    	long intervalTime = tempTime - backPressedTime;
				
				if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
					if (!image) {
						if( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime){
			    			getActivity().moveTaskToBack(true);
			    			getActivity().finish();
			    			android.os.Process.killProcess(android.os.Process.myPid());
			    		} else {
			    			backPressedTime = tempTime;
			    			Toast.makeText(getActivity().getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
			    		}
					} else {
						manseImage.setVisibility(View.GONE);
						image = false;
					}
				}
				return false;
			}
        });
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == REQ_CODE_SELECT_IMAGE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				manseList.add(new ManseData(data.getData() + "", false));
			}
		}
		
		adapter.notifyDataSetChanged();
		
		manseGrid.setAdapter(adapter);
		
	}

}
