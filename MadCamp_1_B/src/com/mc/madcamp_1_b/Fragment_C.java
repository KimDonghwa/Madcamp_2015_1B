package com.mc.madcamp_1_b;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_C extends Fragment implements SurfaceHolder.Callback {
	private final long	FINSH_INTERVAL_TIME	= 2000;
	private long		backPressedTime		= 0;
	private FrameLayout mainImage;
	private ImageButton preRes, nextRes;
	private RelativeLayout preview;
	private Button saveImage;
	private ImageView preImage, resource, preResult;
	private CameraSurfaceView camSurface;
	private SurfaceHolder holder;
	Camera camera;
	byte[] tempdata;
	boolean cameraRun, image, focus;
	Bitmap result, bm;
	int imageNum = 1;
	
	
	public Fragment_C() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_c, container, false);
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        result = null;
        
        image = false;
        
        focus = false;
        
        mainImage = (FrameLayout)view.findViewById(R.id.mainImage);
        preRes = (ImageButton)view.findViewById(R.id.preRes);
        nextRes = (ImageButton)view.findViewById(R.id.nextRes);
        preview = (RelativeLayout)view.findViewById(R.id.preview);
        saveImage = (Button)view.findViewById(R.id.saveImage);
        preImage = (ImageView)view.findViewById(R.id.preImage);
        resource = (ImageView)view.findViewById(R.id.resource);
        preResult = (ImageView)view.findViewById(R.id.preResult);
        camSurface = (CameraSurfaceView)view.findViewById(R.id.camera);
        holder = camSurface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        String resName = "@drawable/clo" + imageNum;
        String packName = getActivity().getBaseContext().getPackageName();
        int resId = getActivity().getBaseContext().getResources().getIdentifier(resName, "drawable", packName);
        
        resource.setImageResource(resId);
        
        preRes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				switch(imageNum){
					case 1:
						imageNum = 3;
						break;
					case 2:
					case 3:
						imageNum -= 1;
						break;
				}
				
				String resName = "@drawable/clo" + imageNum;
				String packName = getActivity().getBaseContext().getPackageName();
				int resId = getActivity().getBaseContext().getResources().getIdentifier(resName, "drawable", packName);
				
				resource.setImageResource(resId);
				
			}
        	
        });
        
        nextRes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				switch(imageNum){
					case 1:
					case 2:
						imageNum += 1;
						break;
					case 3:
						imageNum = 1;
				}
				
				String resName = "@drawable/clo" + imageNum;
				String packName = getActivity().getBaseContext().getPackageName();
				int resId = getActivity().getBaseContext().getResources().getIdentifier(resName, "drawable", packName);
				
				resource.setImageResource(resId);
			}
        	
        });
        
        mainImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				if(focus)
					camera.takePicture(sCallback, pCallback, jpeg);
				else
					Toast.makeText(getActivity(), "원하는 곳을 길게 클릭하여 포커스를 잡아주세요!", Toast.LENGTH_SHORT).show();
				
			}
        	
        });
        
        mainImage.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				
				focus = false;
				preResult.setEnabled(focus);
				
				camera.autoFocus(autoFocus);
				
				return true;
			}
        	
        });
        
        saveImage.setOnClickListener(new OnClickListener(){
        	
        	public void onClick(View v){
        		
        		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        		try {
        			String url = "storage/emulated/0/media/"+dateFormat.format(new Date()) + ".png";
        			FileOutputStream out = new FileOutputStream(url);
        			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        			File file = new File(url);
        			Intent save = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        			save.setData(Uri.fromFile(file));
        			getActivity().sendBroadcast(save);
        		} catch (FileNotFoundException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		preview.setVisibility(View.GONE); 
        		
        		Toast.makeText(getActivity(), "사진이 저장 되었습니다.", Toast.LENGTH_SHORT).show();
        		
        		mainImage.setEnabled(true);
        		image = false;
        		
        		camera.startPreview();
        		
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
					if (image) {
						preview.setVisibility(View.GONE);
						mainImage.setEnabled(true);
						image = false;
						camera.startPreview();
					} else {
						if( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime){
							getActivity().moveTaskToBack(true);
							getActivity().finish();
							android.os.Process.killProcess(android.os.Process.myPid());
						} else {
							backPressedTime = tempTime;
							Toast.makeText(getActivity().getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
						}
					}
				}
				return false;
			}
        });
		
		return view;
	}
	
	AutoFocusCallback autoFocus = new AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			focus = success;
			preResult.setEnabled(focus);
		}
    	
    };
    
    ShutterCallback sCallback = new ShutterCallback(){

		@Override
		public void onShutter() {}
    	
    };
    
    PictureCallback pCallback = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
		}
    
    };
    
    PictureCallback jpeg = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			if(data != null){
				tempdata = data;
				done();
			}
			
		}
    	
    };
    
    void done(){
    	//camera.stopPreview();
    	
    	bm = BitmapFactory.decodeByteArray(tempdata, 0, tempdata.length);
    	bm = rotate(bm, 90);
    	
    	String resName = "@drawable/clo" + imageNum;
		String packName = getActivity().getBaseContext().getPackageName();
		int resId = getActivity().getBaseContext().getResources().getIdentifier(resName, "drawable", packName);
    	
    	BitmapDrawable resD = (BitmapDrawable)getResources().getDrawable(resId);
    	
    	Bitmap res = resD.getBitmap().copy(Config.ARGB_8888, true);
    	
    	
    	
    	float bmWidth, bmHeight, rWidth, rHeight;
    	
    	bmWidth = bm.getWidth();
    	bmHeight = bm.getHeight();
    	rWidth = res.getWidth();
    	rHeight = res.getHeight();

    	float percente = (float) (bmHeight / 100);
		float scale = (float) (camSurface.getHeight() / percente);
		bmWidth *= (scale / 100);
		bmHeight *= (scale / 100);
    	//}
    	
    	bm = Bitmap.createScaledBitmap(bm, (int)bmWidth, (int)bmHeight, true);
    	
    	percente = (float) (rWidth / 100);
		scale = (float) (camSurface.getWidth() / percente);
		rWidth *= (scale / 100);
		rHeight *= (scale / 100);
    	
    	
    	res = Bitmap.createScaledBitmap(res, (int)rWidth, (int)rHeight, true);
//		layers[0] = new BitmapDrawable(bm);
//		layers[1] = r.getDrawable(R.drawable.clo2);
//
//		layD = new LayerDrawable(layers);
		
		bm = overlay(bm, res);
		
		mainImage.setEnabled(false);

		preImage.setImageBitmap(bm);
		preImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		preview.setVisibility(View.VISIBLE);
		
		image = true;
		
		res.recycle();
    }
    
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2){
    	Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp1.getHeight(), bmp2.getConfig());
    	Canvas canvas = new Canvas(bmOverlay);
    	canvas.drawBitmap(bmp1, (bmp2.getWidth() - bmp1.getWidth())/2, 0 , null);
    	canvas.drawBitmap(bmp2, 0, bmp1.getHeight() - bmp2.getHeight(), null);
    	return bmOverlay;
    }
    
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    
    public static Bitmap rotate(Bitmap b, int degrees) {
        if ( degrees != 0 && b != null ) {
            Matrix m = new Matrix();
            m.setRotate( degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2 );
            try {
                Bitmap b2 = Bitmap.createBitmap( b, 0, 0, b.getWidth(), b.getHeight(), m, true );
                if (b != b2) {
                    //b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }
    

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
		try {
			if(cameraRun){
				camera.stopPreview();
				cameraRun = false;
			}
			
			Camera.Parameters params = camera.getParameters();

			List<Size> arpSize = params.getSupportedPictureSizes();
			if (arpSize == null){
				params.setPictureSize(width, height);
			} else {
				int diff = 10000;
				Size opti = null;
				for (Size s : arpSize){
					if(Math.abs(s.height - height) < diff){
						diff = Math.abs(s.height - height);
						opti = s;
					}
				}
				params.setPictureSize(opti.width, opti.height);
			}
			
			List<Size> arSize = params.getSupportedPreviewSizes();
			if (arSize == null){
				params.setPreviewSize(width, height);
			} else {
				int diff = 10000;
				Size opti = null;
				for (Size s : arSize){
					if(Math.abs(s.height - height) < diff){
						diff = Math.abs(s.height - height);
						opti = s;
					}
				}
				params.setPreviewSize(opti.width, opti.height);
			}
			
			camera.setParameters(params);
			
			camera.setPreviewDisplay(this.holder);

			camera.setDisplayOrientation(90);

			camera.startPreview();
			cameraRun = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
	
	public static Bitmap viewToBitmap(View view) {
	    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    if (view instanceof SurfaceView) {
	        SurfaceView surfaceView = (SurfaceView) view;
	        surfaceView.setZOrderOnTop(true);
	        surfaceView.draw(canvas);
	        surfaceView.setZOrderOnTop(false);
	        return bitmap;
	    } else {
	        
	//For ViewGroup & View
	        view.draw(canvas);
	        return bitmap;
	    }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		cameraRun = false;
		camera.release();
		camera = null;
	}
	
	
}
