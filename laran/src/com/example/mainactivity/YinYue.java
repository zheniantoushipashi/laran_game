package com.example.mainactivity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class YinYue extends Activity{
	private Button bPlay;
	private Button bPause;
	private Button bStop;
	private Button bAdd;
	private Button bReduce;
	@SuppressLint("SdCardPath")
	private String PATH = "/data/data/com.example.mainactivity/res/raw/a.mp3";
	// 暂停标记，true为暂停，false为非暂停
	private boolean pauseFlag = false;
	// MediaPlayer的引用
	MediaPlayer mp;
	// AudioManager的引用
	AudioManager am;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yinyue);

	// 按钮实例化
bPlay =(Button) findViewById(R.id.buttonplay);
bPause = (Button) findViewById(R.id.buttonpause);
bStop = (Button) findViewById(R.id.buttonstop);
bAdd = (Button) findViewById(R.id.buttonvadd);
bReduce=(Button) findViewById(R.id.buttonvreduce);

mp=new MediaPlayer();
am=(AudioManager)this.getSystemService(this.AUDIO_SERVICE);

// 播放按钮
	bPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				try {
					 mp.reset();
					// 加载音频文件的进入Initialized状态
					mp.setDataSource(PATH);
					// 进入prepared状态
					mp.prepare();
					// 播放音乐
					mp.start();
					Toast.makeText(YinYue.this, "播放音乐", 5000).show();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
// 暂停按钮
bPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (mp.isPlaying()) {
					mp.pause();
					pauseFlag = true;
				} else if (pauseFlag) {
					mp.start();
					pauseFlag = false;

				}
				Toast.makeText(YinYue.this, "暂停播放", 5000).show();
			}
		});
// 停止按钮
bStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 停止播放
				mp.stop();
				// 重置到uninitalized状态
				mp.reset();
				try {
					// 加载音频进入initalized状态
					mp.setDataSource(PATH);
					// 进入prepare状态
					mp.prepare();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(YinYue.this, "停止播放", 5000).show();
			}
		});

// 增大音量
bAdd.setOnClickListener(new OnClickListener() {

	public void onClick(View v) {
				// 增大音量
	am.adjustVolume(AudioManager.ADJUST_RAISE, 0);
				Toast.makeText(YinYue.this, "增大音量", 5000).show();
			}
		});
// 减少音量
		bReduce.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 减少音量
				am.adjustVolume(AudioManager.ADJUST_LOWER, 0);
				Toast.makeText(YinYue.this, "减少音量", 5000).show();
			}
		});
	}
}