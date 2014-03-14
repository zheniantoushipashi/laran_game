
package com.example.mainactivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public class BaoCun extends Activity implements OnTouchListener{
	private int x;//绘画开始的横坐标
	private int y;//绘画开始的纵坐标
	private int m;//绘画结束的横坐标
	private int n;//绘画结束的纵坐标
	private int width;//绘画的宽度
	private int height;//绘画的高度
	private Bitmap bitmap;//生成的位图
	private MyView1 myView;//绘画选择区域
	private ImageView image1;
	private ImageView image2;
	ImageView imageView;
	private Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = new ImageView(this);
        setContentView(R.layout.activity_zj);
       
      
        myView = new MyView1(this);
        button = (Button) findViewById(R.id.btnscreen);
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(myView.isSign()){
					
					myView.setSeat(0, 0, 0, 0);
					myView.setSign(false);
					button.setText("停止截屏");
				}else{
					myView.setSign(true);
					button.setText("开始截屏");
					image2.setImageBitmap(null);
				}
				myView.postInvalidate();
			}
		});
        image1.setOnTouchListener(this);
        this.addContentView(myView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x = 0;
			y = 0;
			width = 0;
			height = 0;
			x = (int) event.getX();
			y = (int) event.getY();
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			m = (int) event.getX();
			n = (int) event.getY();
			myView.setSeat(x, y, m, n);
			myView.postInvalidate();
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(event.getX()>x){
				width = (int)event.getX()-x;
			}else{
				width = (int)(x-event.getX());
				x = (int) event.getX();
			}
			if(event.getY()>y){
				height = (int) event.getY()-y;
			}else{
				height = (int)(y-event.getY());
				y = (int) event.getY();
			}
			image2.setImageBitmap(getBitmap(this));
		}
		if(myView.isSign()){
			return false;
		}else{
			return true;
		}
	}
	private Bitmap getBitmap(Activity activity){
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		bitmap = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int toHeight = frame.top;
		bitmap = Bitmap.createBitmap(bitmap, x, y+2*toHeight, width, height);
		try {
			FileOutputStream fout = new FileOutputStream("mnt/sdcard/test.png");
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.setDrawingCacheEnabled(false);
		return bitmap;
	}
}