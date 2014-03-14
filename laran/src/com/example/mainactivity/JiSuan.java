package com.example.mainactivity;

import com.ant.liao.GifView;
import com.example.total.Total;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JiSuan extends Activity{
	private GifView jisuan;
	Button next;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jisuan);
		
		jisuan=(GifView)findViewById(R.id.jisuan);
		jisuan.setGifImage(R.drawable.xxx); 
		//next=(Button)findViewById(R.id.next);
		//next.setVisibility(View.VISIBLE);
		/*next.setOnClickListener(new View.OnClickListener() 
	    {                  public void onClick(View v) {
	   	  
	   	   Intent intent = new Intent();
	          //指定intent要启动的类
	          intent.setClass(JiSuan.this, Stage2.class);
	          //启动一个新的Activity
	          startActivity(intent);
	          //关闭当前的Activity
	          JiSuan.this.finish();
	       			 
	       	  }
	    });   */
	}
}
