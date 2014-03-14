package com.example.mainactivity;
import com.ant.liao.GifView;
import com.example.game.FirstGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class Fail extends Activity{
	private GifView jisuan;
	Button fail;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jisuan);		
		jisuan=(GifView)findViewById(R.id.jisuan);
		jisuan.setGifImage(R.drawable.fail);  
		//fail=(Button)findViewById(R.id.fail);
		//fail.setVisibility(View.VISIBLE);
		/* fail.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
				 Intent intent = new Intent();
				 intent.setClass(Fail.this,FirstGame.class);
				 startActivity(intent);
				 Fail.this.finish();
			    }
			});*/
	}
}
