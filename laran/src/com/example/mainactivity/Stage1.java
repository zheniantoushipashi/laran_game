package com.example.mainactivity;

import com.example.game.FirstGame;
import com.example.mainactivity.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.view.*;
import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
public class Stage1 extends Activity {
	   GifView  gv;
   ImageButton sure1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   
		setContentView(R.layout.stage1);
		gv = (GifView)findViewById(R.id.dh);  
		gv.setGifImage(R.drawable.dh);  
		sure1=(ImageButton)findViewById(R.id.sure1);
		sure1.setVisibility(View.VISIBLE);
		sure1.setOnClickListener(new View.OnClickListener() 
	    {                  public void onClick(View v) {
	   	  
	   	   Intent intent = new Intent();
	          //ָ��intentҪ��������
	          intent.setClass(Stage1.this, JinDuTiao.class);
	          //����һ���µ�Activity
	          startActivity(intent);
	          //�رյ�ǰ��Activity
	          Stage1.this.finish();
	       			 
	       	  }
	    });   
	}
	 

}
