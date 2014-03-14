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
	// ��Ϣ������������Ϣ�ͷ�����Ϣ
	Handler hand;
	int bu = 0;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jindutiao);
		// ����һ���߳�
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setMax(100);

		tv = (TextView) findViewById(R.id.texitview2);
		// ������Ϣ����
		hand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				tv.setText("���ڼ���:" + bu + "%");
			}
		};
	}

	@Override
	protected void onStart() {
		super.onStart();
		Thread start = new Thread() {
			public void run() {

				try {
					// �õ�ǰ�߳�˯��20ms

					while (bu < 100) {
						pb.setProgress(bu);
						pb.setSecondaryProgress(bu + 5);
						// ������Ϣ Message����Ҫ���͵���Ϣ
						// һ�㱻�ŵ��߳���ȥ��������UI���
						Message msg = new Message();
						hand.sendMessage(msg);
						bu++;
						Thread.sleep(40);
					}
					Intent i = new Intent(JinDuTiao.this, FirstGame.class);
					// ����һ���µ�Activity
					startActivity(i);
					// �ر�ԭ����Activity
					JinDuTiao.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		// �����߳�
		start.start();
		bu = 0;

	}
}