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
	 * �������Ʋ������ͼƬ��ͼƬ��������
	 * 
	 * @param bmp ԭʼbitmap
	 * @param outW �������������ȣ�-1Ϊ������
	 * @param outH ������������߶ȣ�-1Ϊ������ 
	 * @param scale_w_h ���ͼƬ�Ŀ�߱�����-1Ϊ��ͼ����
	 * @param rotate ԭʼbitmap��Ҫ��ת�ĽǶ� 0,90,180,270...
	 * @param config ��������
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
	 * ���ط��ϱ�����ͼƬ�ü�����
	 * 
	 * @param w ͼƬ�Ŀ� 
	 * @param h ͼƬ�ĸ� 
	 * @param scale_w_h ��߱���
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
	 * ���������ţ������С��Ҫ���һ�£��Զ��ü��������֣�
	 * 
	 * @param bmp ԭʼbitmap
	 * @param outW �����ͼƬ��
	 * @param outH �����ͼƬ��
	 * @param posType POS_START(��ʼ��ȡ) POS_CENTER(�м��ȡ) POS_END(β����ȡ)
	 * @param rotate ԭʼbitmap��Ҫ��ת�ĽǶ�0,90,180,270...
	 * @param config ��������
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
	 * ����ͼƬ
	 * 
	 * @param bmp ԭʼbitmap
	 * @param outW �����ͼƬ��
	 * @param outH �����ͼƬ��
	 * @param rotate ԭʼbitmap��Ҫ��ת�ĽǶ�0,90,180,270...
	 * @param config ��������
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
