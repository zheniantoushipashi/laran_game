package com.example.mainactivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*
 * 实现方式：
 * 采用了两层bitmap的方式：
 * 底层与表层bitmap：floorBitmap, surfaceBitmap;表层bitmap为透明色，否则会覆盖掉底层bitmap的图形
 * 当前画图东都在表层bitmap：surfaceBitmap。如果改变画笔，则将当前surfaceBitmap的内容绘制到底层bitmap：floorBitmap
 * 如果选择橡皮，则需要在底层bitmap上进行绘制，
 * 查看原图片，也是讲图片绘制到底层bitmap
 */
public class MyView extends View {

	private DrawBS drawBS = null;
	private Point evevtPoint;
	private Bitmap floorBitmap, surfaceBitmap;// 底层与表层bitmap
	private Canvas floorCanvas, surfaceCanvas;// bitmap对应的canvas

	private boolean isEraser = false;
	
	Bitmap newbm;

	@SuppressLint("ParserError")
	public MyView(Context context) {
		super(context);

		// 初始化drawBS，即drawBS默认为DrawPath类
		drawBS = new DrawPath();
		evevtPoint = new Point();

		// 底层bitmap与canvas，
		floorBitmap = Bitmap.createBitmap(480, 700, Bitmap.Config.ARGB_8888);
		floorCanvas = new Canvas(floorBitmap);

		
		// 表面bitmap。置于底层bitmap之上，用于赋值绘制当前的所画的图形；需要设置为透明，否则覆盖底部bitmap
		surfaceBitmap = Bitmap.createBitmap(480, 700, Bitmap.Config.ARGB_8888);
		surfaceCanvas = new Canvas(surfaceBitmap);
		surfaceCanvas.drawColor(Color.TRANSPARENT);

	}

	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// 将底层bitmap的图形绘制到主画布
		
		canvas.drawBitmap(floorBitmap, 0, 0, null);

		// 判断选择的图形是否为橡皮
		if (isEraser) {
			// 如果是橡皮，让画笔在底层bitmap上进行操作，
			/*
			 * 传递底层Canvas参数。 调用相应的画图工具类方法,在底层bitmap上使用floorCanvas进行绘图
			 */
			drawBS.onDraw(floorCanvas);
			canvas.drawBitmap(floorBitmap, 0, 0, null);

		} else {
			// 如果不是橡皮，则让画笔在表层bitmap上进行操作，
			/*
			 * 传递表层Canvas参数。 调用相应画图工具类方法,在表层bitmap上使用surfaceCanvas进行绘图
			 */
			drawBS.onDraw(surfaceCanvas);
			canvas.drawBitmap(surfaceBitmap, 0, 0, null);
		}

	}

	// 触摸事件。调用相应的画图工具类进行操作
	@SuppressLint("WrongCall")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		evevtPoint.set((int) event.getX(), (int) event.getY());

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			drawBS.onTouchDown(evevtPoint);
			break;

		case MotionEvent.ACTION_MOVE:
			drawBS.onTouchMove(evevtPoint);
			/*
			 * 拖动过程中不停的将bitmap的颜色设置为透明（清空表层bitmap）
			 * 否则整个拖动过程的轨迹都会画出来
			 */
			surfaceBitmap.eraseColor(Color.TRANSPARENT);
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			drawBS.onTouchUp(evevtPoint);
			break;
		default:
			break;
		}
		return true;
	}

	// 选择图形，实例化相应的类
	public void setDrawTool(int i) {
		switch (i) {
		case 0:
			drawBS = new DrawLine();
			break;
		case 1:
			drawBS = new DrawRectangle();
			break;
		case 2:
			drawBS = new DrawCircle();
			break;
		case 3:
			drawBS = new DrawTriangle();
			break;
		case 4:
			drawBS = new DrawCube();
			break;
		case 5:
			drawBS = new DrawColumn();
			break;
		case 10:
			// 如果需要橡皮。则实例化重新设置画笔的构造方法
			drawBS = new DrawPath(10);// 橡皮
			break;
		default:
			drawBS = new DrawPath();
			break;
		}

		// 如果选择橡皮，isEraser = true
		if (i == 10) {
			isEraser = true;
		} else {
			isEraser = false;
		}
		// 如果重新选择了图形，则需要将表层bitmap上的图像绘制到底层bitmap上进行保存
		floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
	}

	// 将图片存入内存卡
	public void savePicture(String draw_name, int alpha) {
		FileOutputStream fos = null;
		String type = null;

		if (alpha == 0) {// 不透明
			type = ".jpg";
		} else {
			type = ".png";
		}
		try {
			String strPath = "mnt/sdcard/JiHeImg/";
			File fPath = new File(strPath);
			if (!fPath.exists()) {
				fPath.mkdir();
			}

			File f = new File(strPath + draw_name.trim() + type);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			fos = new FileOutputStream(f);
			Bitmap b = null;
			destroyDrawingCache();
			setDrawingCacheEnabled(true);

			buildDrawingCache();
			b = getDrawingCache();

			if (b != null) {
				b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				if (!b.isRecycled())
					b.recycle();
				b = null;
				System.gc();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
				}
			}
		}
	}

	// 取出图片
	public void editPicture(String draw_name) {
		// 新建一个临时的bitmap用于放置读取到的图片
		Bitmap tempBitmap = Bitmap.createBitmap(floorBitmap.getWidth(),
				floorBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		if (!tempBitmap.isRecycled()) {
			tempBitmap.recycle();
			tempBitmap = null;
			System.gc();
		}

		tempBitmap = getLoacalBitmap(draw_name);
		// 将tempBitmap中的图形绘制到floorBitmap
		floorCanvas.drawBitmap(tempBitmap, 0, 0, null);
		invalidate();
	}

	private Bitmap getLoacalBitmap(String draw_name) {
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true; // 获取资源图片
			FileInputStream fis = new FileInputStream(
					"/sdcard/JiHeImg/" + draw_name);
			return BitmapFactory.decodeStream(fis, null, opt);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
