package com.example.mainactivity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;

public class MakeBmp
{
	/**
	 * 根据限制参数输出图片，图片不会拉伸
	 * 
	 * @param bmp 原始bitmap
	 * @param outW 输出容器的最大宽度，-1为不限制
	 * @param outH 输出容器的最大高度，-1为不限制 
	 * @param scale_w_h 输出图片的宽高比例，-1为按图比例
	 * @param rotate 原始bitmap需要旋转的角度 0,90,180,270...
	 * @param config 像素类型
	 */
	public static Bitmap CreateBitmap(Bitmap bmp, int outW, int outH, float scale_w_h, int rotate, Config config)
	{
		Bitmap outBmp = null;
		if(bmp != null)
		{
			int inW = bmp.getWidth();
			int inH = bmp.getHeight();

			if(rotate % 180 != 0)
			{
				inW += inH;
				inH = inW - inH;
				inW -= inH;
			}

			MyRect clipRect;
			if(scale_w_h > 0)
			{
				clipRect = MakeRect(inW, inH, scale_w_h);
			}
			else
			{
				clipRect = new MyRect();
				clipRect.m_x = 0;
				clipRect.m_y = 0;
				clipRect.m_w = inW;
				clipRect.m_h = inH;
			}

			float scale = 1;
			if(outW > 0 && outH > 0 && (outW < clipRect.m_w || outH < clipRect.m_h))
			{
				float scale1 = (float)outW / clipRect.m_w;
				float scale2 = (float)outH / clipRect.m_h;
				if(scale1 <= scale2)
				{
					scale = scale1;
				}
				else
				{
					scale = scale2;
				}
			}
			else if(outW > 0)
			{
				if(outW < clipRect.m_w)
				{
					scale = (float)outW / clipRect.m_w;
				}
			}
			else if(outH > 0)
			{
				if(outH < clipRect.m_h)
				{
					scale = (float)outH / clipRect.m_h;
				}
			}

			float bmpW = clipRect.m_w * scale;
			float bmpH = clipRect.m_h * scale;
			float centerX = bmpW / 2f;
			float centerY = bmpH / 2f;
			Matrix matrix = new Matrix();
			matrix.postTranslate((bmpW - bmp.getWidth()) / 2f, (bmpH - bmp.getHeight()) / 2f);
			matrix.postRotate(rotate, centerX, centerY);
			matrix.postScale(scale, scale, centerX, centerY);

			outBmp = Bitmap.createBitmap((int)bmpW, (int)bmpH, config);
			Canvas canvas = new Canvas(outBmp);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			canvas.drawBitmap(bmp, matrix, null);
		}

		return outBmp;
	}

	/**
	 * 返回符合比例的图片裁剪矩形
	 * 
	 * @param w 图片的宽 
	 * @param h 图片的高 
	 * @param scale_w_h 宽高比例
	 */
	public static MyRect MakeRect(float w, float h, float scale_w_h)
	{
		MyRect outRect = new MyRect();
		outRect.m_w = w;
		outRect.m_h = w / scale_w_h;
		if(outRect.m_h > h)
		{
			outRect.m_h = h;
			outRect.m_w = h * scale_w_h;
		}
		outRect.m_x = (w - outRect.m_w) / 2f;
		outRect.m_y = (h - outRect.m_h) / 2f;

		return outRect;
	}

	public static class MyRect
	{
		public float m_x;
		public float m_y;
		public float m_w;
		public float m_h;
	}

	
	public static final int POS_START = 1;
	public static final int POS_CENTER = 2;
	public static final int POS_END = 3;
	/**
	 * 按比例缩放，输出大小与要求的一致（自动裁剪超出部分）
	 * 
	 * @param bmp 原始bitmap
	 * @param outW 输出的图片宽
	 * @param outH 输出的图片高
	 * @param posType POS_START(开始截取) POS_CENTER(中间截取) POS_END(尾部截取)
	 * @param rotate 原始bitmap需要旋转的角度0,90,180,270...
	 * @param config 像素类型
	 */
	public static Bitmap CreateFixBitmap(Bitmap bmp, int outW, int outH, int posType, int rotate, Config config)
	{
		Bitmap outBmp = null;
		if(bmp != null)
		{
			int inW = bmp.getWidth();
			int inH = bmp.getHeight();

			if(rotate % 180 != 0)
			{
				inW += inH;
				inH = inW - inH;
				inW -= inH;
			}

			float scale = (float)outW / inW;
			if(scale * inH < outH)
			{
				scale = (float)outH / inH;
			}

			Matrix matrix = new Matrix();
			switch(posType)
			{
				case POS_START:
					switch(rotate % 360)
					{
						case 0:
							matrix.postScale(scale, scale, 0, 0);
							break;
						case 90:
							matrix.postTranslate(inW, 0);
							matrix.postRotate(rotate, inW, 0);
							matrix.postScale(scale, scale, 0, 0);
							break;
						case 180:
							matrix.postTranslate(inW, inH);
							matrix.postRotate(rotate, inW, inH);
							matrix.postScale(scale, scale, 0, 0);
							break;
						case 270:
							matrix.postTranslate(0, inH);
							matrix.postRotate(rotate, 0, inH);
							matrix.postScale(scale, scale, 0, 0);
							break;
						default:
							break;
					}
					break;
				case POS_CENTER:
					matrix.postTranslate((outW - bmp.getWidth()) / 2f, (outH - bmp.getHeight()) / 2f);
					matrix.postRotate(rotate, outW / 2f, outH / 2f);
					matrix.postScale(scale, scale, outW / 2f, outH / 2f);
					break;
				case POS_END:
					switch(rotate % 360)
					{
						case 0:
							matrix.postTranslate(outW - inW, outH - inH);
							matrix.postScale(scale, scale, outW, outH);
							break;
						case 90:
							matrix.postRotate(rotate, 0, 0);
							matrix.postTranslate(outW, outH - inH);
							matrix.postScale(scale, scale, outW, outH);
							break;
						case 180:
							matrix.postRotate(rotate, 0, 0);
							matrix.postTranslate(outW, outH);
							matrix.postScale(scale, scale, outW, outH);
							break;
						case 270:
							matrix.postRotate(rotate, 0, 0);
							matrix.postTranslate(outW - inW, outH);
							matrix.postScale(scale, scale, outW, outH);
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}

			outBmp = Bitmap.createBitmap(outW, outH, config);
			Canvas canvas = new Canvas(outBmp);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			canvas.drawBitmap(bmp, matrix, null);
		}

		return outBmp;
	}
	
	/**
	 * 拉伸图片
	 * 
	 * @param bmp 原始bitmap
	 * @param outW 输出的图片宽
	 * @param outH 输出的图片高
	 * @param rotate 原始bitmap需要旋转的角度0,90,180,270...
	 * @param config 像素类型
	 */
	public static Bitmap CreateTensileBitmap(Bitmap bmp, int outW, int outH, int rotate, Config config)
	{
		Bitmap outBmp = null;
		if(bmp != null)
		{
			int inW = bmp.getWidth();
			int inH = bmp.getHeight();

			if(rotate % 180 != 0)
			{
				inW += inH;
				inH = inW - inH;
				inW -= inH;
			}
			
			Matrix matrix = new Matrix();
			matrix.postTranslate((outW - bmp.getWidth()) / 2f, (outH - bmp.getHeight()) / 2f);
			matrix.postRotate(rotate, outW / 2f, outH / 2f);
			matrix.postScale((float)outW / (float)inW, (float)outH / (float)inH, outW / 2f, outH / 2f);
			
			outBmp = Bitmap.createBitmap(outW, outH, config);
			Canvas canvas = new Canvas(outBmp);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
			canvas.drawBitmap(bmp, matrix, null);
		}
		
		return outBmp;
	}
}
