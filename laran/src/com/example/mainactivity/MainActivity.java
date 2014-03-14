package com.example.mainactivity;
import com.example.game.FirstGame;
import com.example.mainactivity.PublicWay;
import java.io.IOException;
import java.util.HashMap;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
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
import android.widget.Button;

public class MainActivity extends Activity  implements OnClickListener  {
    Context mContext = null;
    private MediaPlayer mMediaPlayer;
    private Button button6;
    private boolean flag=false;
    
    private void playLocalFile() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.music);
        mMediaPlayer.setLooping(true);
        //播放工程res目录下的raw目录中的音乐文件in_call_alarm
 
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            
        } catch (IOException e) {
           
        }
 
        mMediaPlayer.start();      
       
    }
  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	ImageButton button0=(ImageButton)findViewById(R.id.button0);
   
    button0.setVisibility(View.VISIBLE);
    
    ImageButton button1=(ImageButton)findViewById(R.id.button1);
    button1.setImageResource(R.drawable.c);
    button1.setVisibility(View.VISIBLE);
    
    button6=(Button)findViewById(R.id.button6);
    button6.setVisibility(View.VISIBLE);
    
     final ImageButton button2=(ImageButton)findViewById(R.id.button2);
    button2.setImageResource(R.drawable.off);
    button2.setVisibility(View.VISIBLE);
     button2.setOnClickListener(this);
    ImageButton button3=(ImageButton)findViewById(R.id.button3);
    button3.setImageResource(R.drawable.d);
    button3.setVisibility(View.VISIBLE);
    ImageButton button4=(ImageButton)findViewById(R.id.button4);
    button4.setImageResource(R.drawable.e);
    button4.setVisibility(View.VISIBLE);
    
    ImageButton button5=(ImageButton)findViewById(R.id.button5);
    button5.setImageResource(R.drawable.f);
    button5.setVisibility(View.VISIBLE);
	mContext = this;
	//playLocalFile();
	
	/**进入游戏世界 - 单点触摸**/
       
	 button0.setOnClickListener(new OnClickListener() {
	   
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,LaRanActivity.class); 
		 startActivity(intent);
	    }
	});
	 button6.setOnClickListener(new OnClickListener() {
		   
		    public void onClick(View arg0) {
			 Intent intent = new Intent(mContext,FirstGame.class); 
			 startActivity(intent);
		    }
		});
	 button5.setOnClickListener(new OnClickListener() {
		   
		    public void onClick(View arg0) {
		    	
			 Intent intent = new Intent(mContext,HuaDong.class); 
			
			 startActivity(intent);
			
		    }
		});
	 
	 button2.setOnClickListener(new OnClickListener() {		   
		    public void onClick(View arg0) {
		  /*  	if(!flag){
		    		 button2.setImageResource(R.drawable.on);
		    		    button2.setVisibility(View.VISIBLE);
		    		    mMediaPlayer.stop();
		    		    flag=true;
		    	}else{
		    		button2.setImageResource(R.drawable.off);
	    		    button2.setVisibility(View.VISIBLE);	    		   
	    		    playLocalFile();
	    		   flag=false;
		    	}
		    
		    	*/
		    	 Intent intent = new Intent(mContext,ImageFilterMain.class); 
				 startActivity(intent);
		    }
		});
	
	 button3.setOnClickListener(new OnClickListener() {
		   
		    public void onClick(View arg0) {
			 Intent intent = new Intent(mContext,BangZhu.class); 
			 startActivity(intent);
		    }
		});
	 button4.setOnClickListener(new OnClickListener() {
		   
		    public void onClick(View arg0) {
			 Intent intent = new Intent(mContext,GuanYu.class); 
			 startActivity(intent);
		    }
		});
       
        PublicWay.activityList.add(this);
       
        //退出程序事件
        button1.setOnClickListener(new OnClickListener(){
    @Override
    public void onClick(View v) {
    //TODO Auto-generated method stub
    //遍历Activity集合，关闭所有集合内的Activity
    for(int i=0;i<PublicWay.activityList.size();i++){
    if (null != PublicWay.activityList.get(i)) {
    PublicWay.activityList.get(i).finish();
    System.exit(1);
    }
    }
    }
        
        });
      
    }
	@Override
	public void onClick(View v) {
		
		
	}
   
    
    
}
