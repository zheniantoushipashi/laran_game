package com.example.mainactivity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
/*
 * 画线段
 * 
 * 伸长、缩短、移动其实都是重新画线
 */
public class DrawLine extends DrawBS {

	private Point cenPoint;//直线的中间点
	private Point lPoint1, lPoint2;//直线的起点与终点
	private Rect lPoint1Rect, lPoint2Rect;//以直线起点和终点为中心的矩形
	
	public DrawLine() {
		// TODO Auto-generated constructor stub
		cenPoint = new Point();
		lPoint1 = new Point();
		lPoint2 = new Point();
		lPoint1Rect = new Rect();
		lPoint2Rect = new Rect();
	}
	  
	
	
	public void onTouchDown(Point point) {
		downPoint.set(point.x, point.y);

		//如果点击起点
		if (lPoint1Rect.contains(point.x, point.y)) {
			Log.i("onTouchDown", "downState = 1");
			downState = 1;
		//如果点击终点
		} else if (lPoint2Rect.contains(point.x, point.y)) {
			Log.i("onTouchDown", "downState = 2");
			downState = 2;
		//如果在直线上
		} else if (panduan(point)) {
			Log.i("onTouchDown", "downState = 3");
			downState = 3;
		//在直线外
		} else {
			Log.i("onTouchDown", "downState = 0");
			downState = 0;
		}
	}   
	  
	
	
	  
	public void onTouchMove(Point point) {

		switch (downState) {
		//如果拖动直线起点，则直线的终点不变
		case 1:
			lPoint1.set(point.x, point.y);
			moveState = 1;
			break;
		//如果拖动直线终点，则直线的起点不变
		case 2:
			lPoint2.set(point.x, point.y);
			moveState = 2;
			break;
		//如果点住直线进行拖动，则根据移动的距离重新设定直线的起点与终点
		case 3:
			//计算直线的中间点
			cenPoint.x = (lPoint2.x + lPoint1.x) / 2;
			cenPoint.y = (lPoint2.y + lPoint1.y) / 2;
			//移动距离
			int movedX = point.x - cenPoint.x;
			int movedY = point.y - cenPoint.y;

			lPoint1.x = lPoint1.x + movedX;
			lPoint1.y = lPoint1.y + movedY;
			lPoint2.x = lPoint2.x + movedX;
			lPoint2.y = lPoint2.y + movedY;
			moveState = 3;
			break;
		//如果不在直线上，则重新画直线
		default:
			lPoint1.set(downPoint.x, downPoint.y);
			lPoint2.set(point.x, point.y);
			break;
		}
	}   
	  
	  
	
	public void onTouchUp(Point point) {
		//重新设定直线起点和终点为中心的矩形
		lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
				lPoint1.y + 25);
		lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
				lPoint2.y + 25);
	} 
	  
	  
	 /*
	  * 判断当前所点击的点是否在直线上
	  * 
	  * 根据用户所点击的点到线段两个端点的距离之和
	  * 与线段的距离进行比较 来判断
	  */
	public boolean panduan(Point point) {

		double lDis = Math.sqrt((lPoint1.x - lPoint2.x)
				* (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
				* (lPoint1.y - lPoint2.y));

		double lDis1 = Math.sqrt((point.x - lPoint1.x) * (point.x - lPoint1.x)
				+ (point.y - lPoint1.y) * (point.y - lPoint1.y));
		double lDis2 = Math.sqrt((point.x - lPoint2.x) * (point.x - lPoint2.x)
				+ (point.y - lPoint2.y) * (point.y - lPoint2.y));


		if (lDis1 + lDis2 >= lDis + 0.00f && lDis1 + lDis2 <= lDis + 5.00f) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// 画直线
		canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y, paint);
	}


}
