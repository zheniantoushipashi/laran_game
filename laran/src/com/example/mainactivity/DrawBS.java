package com.example.mainactivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

/*
 * 主view类。
 * 该类类似于一个接口，子类都继承实现了主类的这些方法
 */
public class DrawBS {
	
	public int downState;
	public int moveState;
	public Point downPoint = new Point();
	public Point movePoint = new Point();
	
	public Point eventPoint = new Point();
	public Paint paint;//声明画笔
	
	
	public DrawBS() {
		// 设置画笔
		paint = new Paint();
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth(5);// 笔宽5像素
		paint.setColor(Color.RED);// 设置为红笔
		paint.setAntiAlias(true);// 锯齿不显示
		
	}
	
	
	public void onTouchDown(Point point) {
		
	}
	 
	public void onTouchMove(Point point) {
		
	}
	
	public void onTouchUp(Point point) {
		
	}
	
	
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
	};

}
