package com.example.mainactivity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SDcard extends Activity {
	/*
	*
	* create the bitmap from a byte array
	*
	* @param src the bitmap object you want proecss
	* @param watermark the water mark above the src
	* @return return a bitmap object ,if paramter's length is 0,return null
	*/

	private Bitmap createBitmap( Bitmap src, Bitmap watermark )

	{

	String tag = "createBitmap";

	Log.d( tag, "create a new bitmap" );

	if( src == null )

	{

	return null;

	}

	int w = src.getWidth();

	int h = src.getHeight();

	int ww = watermark.getWidth();

	int wh = watermark.getHeight();

	//create the new blank bitmap

	Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
	//创建一个新的和SRC长度宽度一样的位图

	Canvas cv = new Canvas( newb );

	//draw src into

	cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src

	//draw watermark into

	cv.drawBitmap( watermark, w - ww + 5, h - wh + 5, null );//在src的右下角画入水印

	//save all clip

	cv.save( Canvas.ALL_SAVE_FLAG );//保存

	//store

	cv.restore();//存储

	return newb;

	}





	/**
	* lessen the bitmap
	*
	* @param src bitmap
	* @param destWidth the dest bitmap width
	* @param destHeigth
	* @return new bitmap if successful ,oherwise null
	*/

	private Bitmap lessenBitmap( Bitmap src, int destWidth, int destHeigth )

	{

	String tag = "lessenBitmap";

	if( src == null )

	{

	return null;

	}

	int w = src.getWidth();//源文件的大小

	int h = src.getHeight();

	// calculate the scale - in this case = 0.4f

	float scaleWidth = ( ( float ) destWidth ) / w;//宽度缩小比例

	float scaleHeight = ( ( float ) destHeigth ) / h;//高度缩小比例

	Log.d( tag, "bitmap width is :" + w );

	Log.d( tag, "bitmap height is :" + h );

	Log.d( tag, "new width is :" + destWidth );

	Log.d( tag, "new height is :" + destHeigth );

	Log.d( tag, "scale width is :" + scaleWidth );

	Log.d( tag, "scale height is :" + scaleHeight );

	Matrix m = new Matrix();//矩阵

	m.postScale( scaleWidth, scaleHeight );//设置矩阵比例

	Bitmap resizedBitmap = Bitmap.createBitmap( src, 0, 0, w, h, m, true );
	//直接按照矩阵的比例把源文件画入进行

	return resizedBitmap;

	}


	

}
