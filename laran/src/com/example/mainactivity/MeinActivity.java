package com.example.mainactivity;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MeinActivity extends Activity
{
	private CutView m_view;
	private int degree = 0;
	private LinearLayout rotation_bar;
	private LinearLayout clip_bar;
	private LinearLayout free_rotateBar;
	Display dis;
	private LinearLayout bottom_bar;
	private SeekBar mSeekBar;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		dis = getWindowManager().getDefaultDisplay();
		
		setContentView(R.layout.first);
		
		bottom_bar = (LinearLayout)this.findViewById(R.id.btn_bar);

		FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
		m_view = new CutView(this);
		m_view.setLayoutParams(fl);
		((FrameLayout)this.findViewById(R.id.fr)).addView(m_view);
		//Bitmap bit=BitmapFactory.decodeFile("mnt/sdcard/123.jpg");
		  File f=new File("mnt/sdcard/laranPicture/LaRan");
	      String[] images = f.list(new ImageFilter());  
	      System.out.println("size="+images.length);  
	      long[] a;
	      a=new long[images.length];
	    
	      for (int i = 0; i < images.length; i++){   
	        	  String imgfile=images[i];  
	        	  if(images[i].length()==18){
	        	  String s1=imgfile.substring(0,14 );
	        	  long result=Long.parseLong(s1);   
	        	  a[i]=result;        
	        	  }
	                    
			 }   
	      
	         long max=a[0];
	         for(int k=0;k<a.length;k++) { 
	       	  if(max<a[k]) { 
	       	  max = a[k]; 
	       	  } 
	       	  }                
		m_view.SetImage("mnt/sdcard/laranPicture/LaRan/"+max+".jpg");
		
		free_rotateBar = (LinearLayout)this.findViewById(R.id.free_rotate_bar);
		free_rotateBar.setVisibility(View.GONE);
		
		mSeekBar = (SeekBar)findViewById(R.id.seekBar);
		mSeekBar.setProgress(50);
		mSeekBarListener = new mySeekBarListener();
		mSeekBar.setOnSeekBarChangeListener(mSeekBarListener);
		
		rotation_bar = (LinearLayout)this.findViewById(R.id.rotation_btn_bar);
		rotation_bar.setVisibility(View.GONE);
		
		clip_bar = (LinearLayout)this.findViewById(R.id.clip_bar);
	}
	
	private mySeekBarListener mSeekBarListener;
	private int temp_degree; 
	private class mySeekBarListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			temp_degree = mSeekBar.getProgress();
			float temp = 1f + Math.abs((float)temp_degree - 50f) / 50f;
			m_view.setRotate(temp_degree - 50, temp, temp);
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}
	
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		ContentResolver cr = getContentResolver();
		if(resultCode != RESULT_OK) 
			return;
		
		if(requestCode == PIC_PICK)
		{
			Uri uri = data.getData();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			String imgPath = cursor.getString(1);
			if(uri!=null)
			{
				try
				{
					m_view.SetImage(imgPath);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private final int PIC_PICK = 10;
	public void OnGetPic(View v)
	{
		degree = 0;
		if(m_view != null)
		{
			if(isPressed)
			{
				setFreeRotateBarGone();
			}
			Intent it = new Intent(Intent.ACTION_GET_CONTENT);
			it.setType("image/*");
			startActivityForResult(it, PIC_PICK);
		}
	}
	
	//响应按钮点击
	public void OnCut(View v)
	{
		if(m_view != null)
		{
			SecondActivity.m_bmp = m_view.GetCutImage();
			this.startActivity(new Intent(this, SecondActivity.class));
		}
	}
	
	private final int CLIPVIEW = 1;
	private final int ROTATIONVIEW = 2;
	private int view_flag = CLIPVIEW;
	public void OnClip(View v)
	{
		if(m_view != null)
		{
			view_flag = CLIPVIEW;
			m_view.showRect(true);
			m_view.setFree();
			setFreeRotateBarGone();
			rotation_bar.setVisibility(View.GONE);
			clip_bar.setVisibility(View.VISIBLE);
		}
	}
	
	private PopupWindow m_popup;
	public void OnShowPW(View v)
	{
		if(m_view != null)
		{
			if(m_popup == null)
			{
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.button_layout, null);
				m_popup = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			}
			m_popup.setFocusable(true);  //设置焦点
			m_popup.setOutsideTouchable(true);
			m_popup.setBackgroundDrawable(new BitmapDrawable());  //按返回键时消失
			m_popup.setTouchInterceptor(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
					{
						m_popup.dismiss();
						return true;
					}
					return false;
				}
			});
			
			m_popup.setAnimationStyle(R.style.AnimationFade);
			m_popup.showAtLocation(bottom_bar, Gravity.LEFT, 0, dis.getHeight()/2 - bottom_bar.getHeight() - ((LinearLayout)this.findViewById(R.id.btn_bar)).getHeight()/2 - clip_bar.getHeight());
		}
	}
	
	public void OnRotate(View v)
	{
		if(m_view != null)
		{
			view_flag = ROTATIONVIEW;
			clip_bar.setVisibility(View.GONE);
			rotation_bar.setVisibility(View.VISIBLE);
			m_view.showRect(false);
		}
	}
	
	public void OnRotation(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree += 90;
			if(degree >= 360) degree = 0;
			m_view.setRotate(degree);
		}
	}
	
	public void OnLeftRotation(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree += -90;
			if(degree <= -360) degree = 0;
			m_view.setRotate(degree);
		}
	}
	
	public void OnXInvert(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			m_view.setXInvert();
		}
	}
	
	public void OnYInvert(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			m_view.setYInvert();
		}
	}
	
	public void OnReset(View v)
	{
		degree = 0;
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree = 0;
			if(view_flag == CLIPVIEW)
			{
				m_view.reset(true);
			}
			else if(view_flag == ROTATIONVIEW)
			{
				m_view.reset(false);
			}
		}
	}
	
	public void OnScale_1_1(View v)
	{
		if(m_view != null)
		{
			m_view.setScale(1f);
			m_popup.dismiss();
		}
	}
	
	public void OnScale_3_2(View v)
	{
		if(m_view != null)
		{
			m_view.setScale(1.5f);
			m_popup.dismiss();
		}
	}
	
	public void OnScale_16_9(View v)
	{
		if(m_view != null)
		{
			m_view.setScale(1.78f);
			m_popup.dismiss();
		}
	}
	
	public void OnScale_free(View v)
	{
		if(m_view != null)
		{
			m_view.setFree();
			m_popup.dismiss();
		}
	}
	
	private boolean isPressed = false;
	public void OnFreeRotate(View v)
	{
		if(m_view != null)
		{
			isPressed = !isPressed;
			if(isPressed)
			{
				free_rotateBar.setVisibility(View.VISIBLE);
			}
			else
			{
				free_rotateBar.setVisibility(View.GONE);
			}
		}
	}
	private void setFreeRotateBarGone()
	{
		free_rotateBar.setVisibility(View.GONE);
		isPressed = false;
	}
	
	public void OnFreeRotateReset(View v)
	{
		if(m_view != null)
		{
			mSeekBar.setProgress(50);
		}
	}
}
