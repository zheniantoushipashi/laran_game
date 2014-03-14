package com.example.mainactivity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PathMenuActivity extends Activity  implements OnClickListener {
	/** Called when the activity is first created. */
	LinearLayout LinearLayout;
	RelativeLayout r1;
	MyView myView;
	ShiJian sj;
 ImageButton xkbutton;
 ImageButton showall;
	private boolean isEraser = false;
  ImageButton back;
	ImageButton drawtoolButton;// 选择形状
	ImageButton eraserButton;// 橡皮
	ImageButton cleanAllButton;// 清空
	ImageButton savePButton;// 保存图片
	ImageButton showButton;// 查看图片
ImageButton texiao_btn;  //特效
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainjihe);
		MyAnimations.initOffset(PathMenuActivity.this);
		myView = new MyView(getApplicationContext());
		composerButtonsWrapper.addView(myView);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);

		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!areButtonsShowing) {
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(0,
									-270, 300));
				} else {
					MyAnimations
							.startAnimationsOut(composerButtonsWrapper, 300);
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(
									-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
	

				
		composerButtonsShowHideButton.startAnimation(MyAnimations
				.getRotateAnimation(0, 360, 200));

	}
	/*
	 * 选择形状
	 */
	public void selectShape() {
		final String[] mItems = { "直线", "矩形", "圆形", "三角形", "立方体", "圆柱体", "涂鸦" };

		AlertDialog.Builder builder = new AlertDialog.Builder(PathMenuActivity.this);
		builder.setTitle("选择形状");

		builder.setItems(mItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				//调用方法setDrawTool（）进行相应的实例化
				myView.setDrawTool(which);
				Toast.makeText(getApplicationContext(),
						"选择了: " + mItems[which], Toast.LENGTH_SHORT).show();

				// 如果选择了图形，则将按钮eraserButton设置显示为“橡皮”
				
				isEraser = false;
			}

		}).setIcon(R.drawable.ic_launcher);

		builder.create().show();
	}
	public void eraser() {
		if (isEraser) {
			// 当前显示为“画笔”
			// 调用view类（drawTool）中的方法setDrawTool()，传递参数
			myView.setDrawTool(6);
			// 则点击后设置按钮显示为“橡皮”
			
			isEraser = false;
		} else {
			// 当前显示为“橡皮”
			// 调用view类（drawTool）中的方法setDrawTool()，传递参数
			myView.setDrawTool(10);
			// 点击后设置按钮显示为“画笔”
			
			isEraser = true;
		}
	
	for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
		composerButtonsWrapper.getChildAt(i).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
	}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
					switch (v.getId()) {

					case R.id.drawtool_btn:// 画图形状的选择
						selectShape();
						break;

					case R.id.eraser_btn:// 橡皮
						eraser();
						break;

					case R.id.cleanAll_btn:// 清空
						LinearLayout.removeAllViews();
						myView = new MyView(getApplicationContext());
						LinearLayout.addView(myView);
						break;

					case R.id.saveP_btn:// 保存图片
						myView.savePicture(sj.getName(), 0);
						showButton.setVisibility(View.VISIBLE);
						break;

					case R.id.showP_btn:// 查看图片
						 File f=new File("mnt/sdcard/JiHeImg");
					
					      String[] images = f.list(new ImageFilter());  
					    
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
						myView.editPicture(max+".jpg");
					      
					    	 
					    
						break;

					default:
						break;
					}
		
	}

}