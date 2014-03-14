package com.example.mainactivity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
public class GuanYu extends Activity  implements OnClickListener{
	  Context mContext = null;
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_gy);
	  }
		@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        // TODO Auto-generated method stub
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            //do something
	        	  Intent intent = new Intent();
	              //指定intent要启动的类
	              intent.setClass(GuanYu.this, FirstMenu.class);
	              //启动一个新的Activity
	              startActivity(intent);
	              //关闭当前的Activity
	              GuanYu.this.finish();
	            return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	}