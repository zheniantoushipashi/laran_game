package com.example.mainactivity;

import com.example.game.FirstGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class JinDuTiao extends Activity {
	ProgressBar pb;
	// 消息，用来接受消息和发送消息
	Handler hand;
	int bu = 0;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jindutiao);
		// 创建一个线程
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setMax(100);

		tv = (TextView) findViewById(R.id.texitview2);
		// 定义消息对象
		hand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				tv.setText("正在加载:" + bu + "%");
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
		Thread start = new Thread() {
			public void run() {

				try {
					// 让当前线程睡眠20ms

					while (bu < 100) {
						pb.setProgress(bu);
						pb.setSecondaryProgress(bu + 5);
						// 发送消息 Message就是要发送的信息
						// 一般被放到线程中去用来更新UI组件
						Message msg = new Message();
						hand.sendMessage(msg);
						bu++;
						Thread.sleep(40);
					}
					Intent i = new Intent(JinDuTiao.this, FirstGame.class);
					// 调用一个新的Activity
					startActivity(i);
					// 关闭原本的Activity
					JinDuTiao.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		// 启动线程
		start.start();
		bu = 0;

	}
}