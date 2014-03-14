package com.example.mainactivity;

import java.io.File;

import com.example.game.FirstGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class JiHeActivity extends Activity implements OnClickListener {

	LinearLayout LinearLayout;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jihe);

		myView = new MyView(getApplicationContext());
		
      sj=new ShiJian(null);
		// ��view���뵽������
		LinearLayout = (LinearLayout) findViewById(R.id.linearlayout01);
		LinearLayout.removeAllViews();
		LinearLayout.addView(myView);
    
		initBottomButton();
	/*	texiao_btn.setOnClickListener(new View.OnClickListener() 
	        {                  public void onClick(View v) {
	       	  
	       	   Intent intent = new Intent();
	              //ָ��intentҪ��������
	              intent.setClass(JiHeActivity.this, ChooseImageActivity.class);
	              //����һ���µ�Activity
	              startActivity(intent);
	              //�رյ�ǰ��Activity
	              JiHeActivity.this.finish();
	           			 
	           	  }
	        }); 
	        */ 
		back.setOnClickListener(new View.OnClickListener() 
	    {                  public void onClick(View v) {
	   	  
	   	   Intent intent = new Intent();
	          //ָ��intentҪ��������
	          intent.setClass(JiHeActivity.this, LaRanActivity.class);
	          //����һ���µ�Activity
	          startActivity(intent);
	          //�رյ�ǰ��Activity
	          JiHeActivity.this.finish();
	       			 
	       	  }
	    });   
		showall.setOnClickListener(new View.OnClickListener() 
	    {                  public void onClick(View v) {
	    	showall.setVisibility(View.GONE);
	    	drawtoolButton.setVisibility(View.VISIBLE);
	    	eraserButton.setVisibility(View.VISIBLE);
	    	cleanAllButton.setVisibility(View.VISIBLE);	 
	    	savePButton.setVisibility(View.VISIBLE);
	    	back.setVisibility(View.VISIBLE);
	    	xkbutton.setVisibility(View.VISIBLE);
	       	  }
	    }); 
		  xkbutton=(ImageButton)this.findViewById(R.id.xkbutton);
		  xkbutton.setImageResource(R.drawable.gfhxu);
	        xkbutton.setVisibility(View.GONE);
	        xkbutton.setOnClickListener(new View.OnClickListener() 
	        {                  public void onClick(View v) {
	       	  
	       	   Intent intent = new Intent();
	              //ָ��intentҪ��������
	              intent.setClass(JiHeActivity.this, ImageFilterMain.class);
	              //����һ���µ�Activity
	              startActivity(intent);
	              //�رյ�ǰ��Activity
	              JiHeActivity.this.finish();
	           			 
	           	  }
	        });    
		
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do something
        	  Intent intent = new Intent();
              //ָ��intentҪ��������
              intent.setClass(JiHeActivity.this, FirstMenu.class);
              //����һ���µ�Activity
              startActivity(intent);
              //�رյ�ǰ��Activity
              JiHeActivity.this.finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
	// �󶨰�ť�������ü����¼�
	private void initBottomButton() {

		drawtoolButton = (ImageButton) findViewById(R.id.drawtool_btn);
		drawtoolButton.setImageResource(R.drawable.jh1);
		drawtoolButton.setVisibility(View.GONE);
		
		eraserButton = (ImageButton) findViewById(R.id.eraser_btn);
		eraserButton.setImageResource(R.drawable.xp);
		eraserButton.setVisibility(View.GONE);
		
		showall=(ImageButton) findViewById(R.id.showall);
		showall.setImageResource(R.drawable.showall);
		showall.setVisibility(View.VISIBLE);
		
		cleanAllButton = (ImageButton) findViewById(R.id.cleanAll_btn);
		
		cleanAllButton.setImageResource(R.drawable.clean);
		cleanAllButton.setVisibility(View.GONE);
		
		savePButton = (ImageButton) findViewById(R.id.saveP_btn);
		savePButton.setImageResource(R.drawable.showp);
		savePButton.setVisibility(View.GONE);
		
		showButton = (ImageButton) findViewById(R.id.showP_btn);
		showButton.setImageResource(R.drawable.search);
		showButton.setVisibility(View.GONE);
		
		//texiao_btn= (ImageButton) findViewById(R.id.texiao_btn);
		//texiao_btn.setImageResource(R.drawable.ps);
		//texiao_btn.setVisibility(View.VISIBLE);
		
		back= (ImageButton) findViewById(R.id.back_btn);
		back.setImageResource(R.drawable.back);
		back.setVisibility(View.GONE);
		// ����onClick(View v)����
		drawtoolButton.setOnClickListener(this);
		eraserButton.setOnClickListener(this);
		cleanAllButton.setOnClickListener(this);
		savePButton.setOnClickListener(this);
		showButton.setOnClickListener(this);
	}

	

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

	/*
	 * ѡ����״
	 */
	public void selectShape() {
		final String[] mItems = { "ֱ��", "����", "Բ��", "������", "������", "Բ����", "Ϳѻ" };

		AlertDialog.Builder builder = new AlertDialog.Builder(JiHeActivity.this);
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

	/*
	 * ��Ƥ ��ť������ʾ����Ƥ���롰���ʡ�;Ĭ����ʾ����Ƥ��
	 */
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
	}

}
