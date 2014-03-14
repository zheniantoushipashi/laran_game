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
	// ��ͣ��ǣ�trueΪ��ͣ��falseΪ����ͣ
	private boolean pauseFlag = false;
	// MediaPlayer������
	MediaPlayer mp;
	// AudioManager������
	AudioManager am;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yinyue);

	// ��ťʵ����
bPlay =(Button) findViewById(R.id.buttonplay);
bPause = (Button) findViewById(R.id.buttonpause);
bStop = (Button) findViewById(R.id.buttonstop);
bAdd = (Button) findViewById(R.id.buttonvadd);
bReduce=(Button) findViewById(R.id.buttonvreduce);

mp=new MediaPlayer();
am=(AudioManager)this.getSystemService(this.AUDIO_SERVICE);

// ���Ű�ť
	bPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				try {
					 mp.reset();
					// ������Ƶ�ļ��Ľ���Initialized״̬
					mp.setDataSource(PATH);
					// ����prepared״̬
					mp.prepare();
					// ��������
					mp.start();
					Toast.makeText(YinYue.this, "��������", 5000).show();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
// ��ͣ��ť
bPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (mp.isPlaying()) {
					mp.pause();
					pauseFlag = true;
				} else if (pauseFlag) {
					mp.start();
					pauseFlag = false;

				}
				Toast.makeText(YinYue.this, "��ͣ����", 5000).show();
			}
		});
// ֹͣ��ť
bStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ֹͣ����
				mp.stop();
				// ���õ�uninitalized״̬
				mp.reset();
				try {
					// ������Ƶ����initalized״̬
					mp.setDataSource(PATH);
					// ����prepare״̬
					mp.prepare();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(YinYue.this, "ֹͣ����", 5000).show();
			}
		});

// ��������
bAdd.setOnClickListener(new OnClickListener() {

	public void onClick(View v) {
				// ��������
	am.adjustVolume(AudioManager.ADJUST_RAISE, 0);
				Toast.makeText(YinYue.this, "��������", 5000).show();
			}
		});
// ��������
		bReduce.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ��������
				am.adjustVolume(AudioManager.ADJUST_LOWER, 0);
				Toast.makeText(YinYue.this, "��������", 5000).show();
			}
		});
	}
}