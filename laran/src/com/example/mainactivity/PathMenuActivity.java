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
	ImageButton drawtoolButton;// ѡ����״
	ImageButton eraserButton;// ��Ƥ
	ImageButton cleanAllButton;// ���
	ImageButton savePButton;// ����ͼƬ
	ImageButton showButton;// �鿴ͼƬ
ImageButton texiao_btn;  //��Ч
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
	 * ѡ����״
	 */
	public void selectShape() {
		final String[] mItems = { "ֱ��", "����", "Բ��", "������", "������", "Բ����", "Ϳѻ" };

		AlertDialog.Builder builder = new AlertDialog.Builder(PathMenuActivity.this);
		builder.setTitle("ѡ����״");

		builder.setItems(mItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				//���÷���setDrawTool����������Ӧ��ʵ����
				myView.setDrawTool(which);
				Toast.makeText(getApplicationContext(),
						"ѡ����: " + mItems[which], Toast.LENGTH_SHORT).show();

				// ���ѡ����ͼ�Σ��򽫰�ťeraserButton������ʾΪ����Ƥ��
				
				isEraser = false;
			}

		}).setIcon(R.drawable.ic_launcher);

		builder.create().show();
	}
	public void eraser() {
		if (isEraser) {
			// ��ǰ��ʾΪ�����ʡ�
			// ����view�ࣨdrawTool���еķ���setDrawTool()�����ݲ���
			myView.setDrawTool(6);
			// ���������ð�ť��ʾΪ����Ƥ��
			
			isEraser = false;
		} else {
			// ��ǰ��ʾΪ����Ƥ��
			// ����view�ࣨdrawTool���еķ���setDrawTool()�����ݲ���
			myView.setDrawTool(10);
			// ��������ð�ť��ʾΪ�����ʡ�
			
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

					case R.id.drawtool_btn:// ��ͼ��״��ѡ��
						selectShape();
						break;

					case R.id.eraser_btn:// ��Ƥ
						eraser();
						break;

					case R.id.cleanAll_btn:// ���
						LinearLayout.removeAllViews();
						myView = new MyView(getApplicationContext());
						LinearLayout.addView(myView);
						break;

					case R.id.saveP_btn:// ����ͼƬ
						myView.savePicture(sj.getName(), 0);
						showButton.setVisibility(View.VISIBLE);
						break;

					case R.id.showP_btn:// �鿴ͼƬ
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