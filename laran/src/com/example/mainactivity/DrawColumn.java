package com.example.mainactivity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
/*
 * 画圆柱
 * 
 * android自己没有画圆柱的方法，所以需要自己想个办法实现。
 * 思路：
 * 1、先画一个椭圆，
 * 2、根据第一个椭圆画第二个椭圆。这两个椭圆上下平行
 * 3、将两个椭圆的左边和右边连接起来。这样看起来就行圆柱了
 */
public class DrawColumn extends DrawBS {

	//声明椭圆1、2的4个点坐标
	private Point ovalPoint1, ovalPoint2, ovalPoint3, ovalPoint4;
	private Point LeftPoint_oval1, RightPoint_oval1, LeftPoint_oval2,
			RightPoint_oval2;
	private RectF rectF1, rectF2;

	public DrawColumn() {
		// TODO Auto-generated constructor stub
		// 实例化
		LeftPoint_oval1 = new Point();
		RightPoint_oval1 = new Point();
		LeftPoint_oval2 = new Point();
		RightPoint_oval2 = new Point();
		ovalPoint1 = new Point();
		ovalPoint2 = new Point();
		ovalPoint3 = new Point();
		ovalPoint4 = new Point();
		rectF1 = new RectF();
		rectF2 = new RectF();
	}

	public void onTouchDown(Point point) {
		downPoint.set(point.x, point.y);
	}

	public void onTouchMove(Point point) {

		/*
		 * 画椭圆与画矩形类似
		 * 先确定画矩形1需要的两个点坐标点oval1Point1、oval1Point2
		 */
		ovalPoint1.set(downPoint.x, downPoint.y);
		ovalPoint2.set(point.x, point.y);
		
		/*
		 * 椭圆1 左边和右边的的坐标点LeftPoint_oval1、RightPoint_oval1
		 */
		//纵坐标
		int y1 = ovalPoint1.y + (ovalPoint2.y - ovalPoint1.y) / 2;
		LeftPoint_oval1 = new Point(ovalPoint1.x, y1);
		RightPoint_oval1 = new Point(ovalPoint2.x, y1);

		//计算一个距离，
		int distance = (int) Math.abs(Math.sqrt((ovalPoint2.x - ovalPoint1.x)
				* (ovalPoint2.x - ovalPoint1.x) + (ovalPoint2.y - ovalPoint1.y)
				* (ovalPoint2.y - ovalPoint1.y)));


		/*
		 * 根据椭圆1画椭圆2
		 * 椭圆1画椭圆2上下平行，横坐标不变，改变纵坐标
		 * 确定画椭圆2需要的两个点坐标oval2Point1、oval2Point2
		 */
		ovalPoint3.set(ovalPoint1.x, ovalPoint1.y + distance);
		ovalPoint4.set(ovalPoint2.x, ovalPoint2.y + distance);
		/*
		 * 椭圆2 左边和右边的的坐标点LeftPoint_oval1、RightPoint_oval1
		 */
		//纵坐标
		int y2 = ovalPoint3.y + (ovalPoint4.y - ovalPoint3.y) / 2;
		LeftPoint_oval2 = new Point(ovalPoint1.x, y2);
		RightPoint_oval2 = new Point(ovalPoint2.x, y2);

		rectF1.set(ovalPoint1.x, ovalPoint1.y, ovalPoint2.x, ovalPoint2.y);
		rectF2.set(ovalPoint3.x, ovalPoint3.y, ovalPoint4.x, ovalPoint4.y);

	}

	
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawOval(rectF1, paint);
		canvas.drawOval(rectF2, paint);
		canvas.drawLine(LeftPoint_oval1.x, LeftPoint_oval1.y,
				LeftPoint_oval2.x, LeftPoint_oval2.y, paint);
		canvas.drawLine(RightPoint_oval1.x, RightPoint_oval1.y,
				RightPoint_oval2.x, RightPoint_oval2.y, paint);
		
	}

}
