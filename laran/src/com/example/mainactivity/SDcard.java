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
	//����һ���µĺ�SRC���ȿ��һ����λͼ

	Canvas cv = new Canvas( newb );

	//draw src into

	cv.drawBitmap( src, 0, 0, null );//�� 0��0���꿪ʼ����src

	//draw watermark into

	cv.drawBitmap( watermark, w - ww + 5, h - wh + 5, null );//��src�����½ǻ���ˮӡ

	//save all clip

	cv.save( Canvas.ALL_SAVE_FLAG );//����

	//store

	cv.restore();//�洢

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

	int w = src.getWidth();//Դ�ļ��Ĵ�С

	int h = src.getHeight();

	// calculate the scale - in this case = 0.4f

	float scaleWidth = ( ( float ) destWidth ) / w;//�����С����

	float scaleHeight = ( ( float ) destHeigth ) / h;//�߶���С����

	Log.d( tag, "bitmap width is :" + w );

	Log.d( tag, "bitmap height is :" + h );

	Log.d( tag, "new width is :" + destWidth );

	Log.d( tag, "new height is :" + destHeigth );

	Log.d( tag, "scale width is :" + scaleWidth );

	Log.d( tag, "scale height is :" + scaleHeight );

	Matrix m = new Matrix();//����

	m.postScale( scaleWidth, scaleHeight );//���þ������

	Bitmap resizedBitmap = Bitmap.createBitmap( src, 0, 0, w, h, m, true );
	//ֱ�Ӱ��վ���ı�����Դ�ļ��������

	return resizedBitmap;

	}


	

}
