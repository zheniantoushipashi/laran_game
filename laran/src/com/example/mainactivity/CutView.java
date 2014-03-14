package com.example.mainactivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CutView extends View
{
	public Object m_obj;
	
	public Bitmap m_bmp;
	public int m_bmpW;
	public int m_bmpH;
	public int m_r = 0;
	public int m_x;
	public int m_y;
	public Matrix m_matrix;
	private float m_sx = 1f;
	private float m_sy = 1f;
	
	public CutRect m_rect;
	private final float RADIO = 20;
	public float m_scale = FREESCALE;
	private boolean isDisplay = true;
	
	private final int NONE_EVENT = 0;
	private final int LEFT_TOP_DOWN = 1;
	private final int RIGHT_TOP_DOWN = 2;
	private final int LEFT_BOTTOM_DOWN = 3;
	private final int RIGHT_BOTTOM_DOWN = 4;
	private int m_event = NONE_EVENT;

	public CutView(Context context)
	{
		super(context);
	}

	public CutView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CutView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void SetImage(Object obj)
	{
		m_bmp = null;
		m_matrix = null;
		m_r = 0;
		m_sx = 1;
		m_sy = 1;
		isDisplay = true;
		
		m_obj = obj;
		m_rect = new CutRect();
		invalidate();
	}

	private Paint temp_paint = new Paint();
	private float[] temp_src = new float[4];
	private float[] temp_dst = new float[4];
	private Canvas m_canvas;

	@Override
	protected void onDraw(Canvas canvas)
	{
		m_canvas = canvas;
		
		if(m_bmp == null && getWidth() > 0 && getHeight() > 0)
		{
			if(m_obj instanceof String)
			{
				//m_bmp = BitmapFactory.decodeFile((String)m_obj);
				m_bmp = BitmapFactory.decodeFile((String)m_obj);
			}
			else if(m_obj instanceof Integer)
			{
				m_bmp = BitmapFactory.decodeResource(getResources(), (Integer)m_obj);
				
			}
			else
			{
				m_bmp = null;
			}

			if(m_bmp != null)
			{
				m_bmp = MakeBmp.CreateBitmap(m_bmp, getWidth(), getHeight(), -1, 0, Config.ARGB_8888);
				m_bmpW = m_bmp.getWidth();
				m_bmpH = m_bmp.getHeight();
				m_x = (getWidth() - m_bmp.getWidth())/2;
				m_y = (getHeight() - m_bmp.getHeight())/2;
				
				temp_paint.setStrokeJoin(Join.BEVEL);
				temp_paint.setStrokeCap(Cap.SQUARE);
				temp_paint.setAntiAlias(true); //反锯齿
				temp_paint.setFilterBitmap(true);
			}
		}
		
		if(m_bmp != null)
		{
			if(m_matrix == null)
			{
				Log.i("m_x", ""+m_x);
				Log.i("m_y", ""+m_y);
				m_matrix = new Matrix();
				m_matrix.postTranslate(m_x, m_y);
				m_matrix.postScale(m_sx, m_sy, m_x + m_bmp.getWidth()/2, m_y + m_bmp.getHeight()/2);  //缩放、倒置
				m_matrix.postRotate(m_r, m_x + m_bmp.getWidth()/2, m_y + m_bmp.getHeight()/2);
			}
			canvas.drawBitmap(m_bmp, m_matrix, null);

			temp_src[0] = m_bmpW * m_rect.m_x;
			temp_src[1] = m_bmpH * m_rect.m_y;
			temp_src[2] = temp_src[0] + m_bmpW * m_rect.m_w;
			temp_src[3] = temp_src[1] + m_bmpH * m_rect.m_h;
			m_matrix.mapPoints(temp_dst, temp_src);  //映射出看到的点

			temp_dst2 = FixPoints(temp_dst);
			
			if(isDisplay)
			{
			//阴影
			temp_paint.setColor(0x88000000);
			canvas.drawRect(0, 0, getWidth(), (int)temp_dst2[1], temp_paint);  //上
			canvas.drawRect(0, (int)temp_dst2[1], (int)temp_dst2[0], (int)temp_dst2[3], temp_paint);  //左
			canvas.drawRect((int)temp_dst2[2], (int)temp_dst2[1], getWidth(), (int)temp_dst2[3], temp_paint);  //右
			canvas.drawRect(0, (int)temp_dst2[3], getWidth(), getHeight(), temp_paint); //下
			
			//矩形
			temp_paint.setColor(0xFFFFFFFF);
			temp_paint.setStrokeWidth(3);
			canvas.drawLine(temp_dst2[0], temp_dst2[1], temp_dst2[2], temp_dst2[1], temp_paint);
			canvas.drawLine(temp_dst2[2], temp_dst2[1], temp_dst2[2], temp_dst2[3], temp_paint);
			canvas.drawLine(temp_dst2[2], temp_dst2[3], temp_dst2[0], temp_dst2[3], temp_paint);
			canvas.drawLine(temp_dst2[0], temp_dst2[3], temp_dst2[0], temp_dst2[1], temp_paint);
			
			//四个圆点
			switch(m_event)
			{
			case NONE_EVENT :
				canvas.drawCircle(temp_dst[0], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[2], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[0], temp_dst[3], RADIO, temp_paint); 
				canvas.drawCircle(temp_dst[2], temp_dst[3], RADIO, temp_paint); 
				break;
			case LEFT_TOP_DOWN:
				temp_paint.setColor(Color.GREEN);
				canvas.drawCircle(temp_dst[0], temp_dst[1], RADIO, temp_paint);
				temp_paint.setColor(0xFFFFFFFF);
				canvas.drawCircle(temp_dst[2], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[0], temp_dst[3], RADIO, temp_paint); 
				canvas.drawCircle(temp_dst[2], temp_dst[3], RADIO, temp_paint);
				break;
			case RIGHT_TOP_DOWN :
				canvas.drawCircle(temp_dst[0], temp_dst[1], RADIO, temp_paint);
				temp_paint.setColor(Color.GREEN);
				canvas.drawCircle(temp_dst[2], temp_dst[1], RADIO, temp_paint);
				temp_paint.setColor(0xFFFFFFFF);
				canvas.drawCircle(temp_dst[0], temp_dst[3], RADIO, temp_paint); 
				canvas.drawCircle(temp_dst[2], temp_dst[3], RADIO, temp_paint);
				break;
			case LEFT_BOTTOM_DOWN:
				canvas.drawCircle(temp_dst[0], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[2], temp_dst[1], RADIO, temp_paint);
				temp_paint.setColor(Color.GREEN);
				canvas.drawCircle(temp_dst[0], temp_dst[3], RADIO, temp_paint);
				temp_paint.setColor(0xFFFFFFFF);
				canvas.drawCircle(temp_dst[2], temp_dst[3], RADIO, temp_paint); 
				break;
			case RIGHT_BOTTOM_DOWN:
				canvas.drawCircle(temp_dst[0], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[2], temp_dst[1], RADIO, temp_paint);
				canvas.drawCircle(temp_dst[0], temp_dst[3], RADIO, temp_paint); 
				temp_paint.setColor(Color.GREEN);
				canvas.drawCircle(temp_dst[2], temp_dst[3], RADIO, temp_paint); 
				temp_paint.setColor(0xFFFFFFFF);
				break;
			}
			
			//中间线
			temp_paint.setStrokeWidth(1);
			canvas.drawLine((int)(temp_dst2[0]+(temp_dst2[2]-temp_dst2[0])/3), (int)(temp_dst2[1]), (int)(temp_dst2[0]+(temp_dst2[2]-temp_dst2[0])/3), (int)(temp_dst2[3]), temp_paint);
			canvas.drawLine((int)(temp_dst2[0]+(temp_dst2[2]-temp_dst2[0])*2/3), (int)(temp_dst2[1]), (int)(temp_dst2[0]+(temp_dst2[2]-temp_dst2[0])*2/3), (int)(temp_dst2[3]), temp_paint);
			canvas.drawLine((int)(temp_dst2[0]), (int)(temp_dst2[1]+(temp_dst2[3]-temp_dst2[1])/3), (int)(temp_dst2[2]), (int)(temp_dst2[1]+(temp_dst2[3]-temp_dst2[1])/3), temp_paint);
			canvas.drawLine((int)(temp_dst2[0]), (int)(temp_dst2[1]+(temp_dst2[3]-temp_dst2[1])*2/3), (int)(temp_dst2[2]), (int)(temp_dst2[1]+(temp_dst2[3]-temp_dst2[1])*2/3), temp_paint);
			
			temp_paint.setTextAlign(Align.CENTER);
			temp_paint.setTextSize(21);
			canvas.drawText((int)(temp_dst2[2]-temp_dst2[0])+"*"+(int)(temp_dst2[3]-temp_dst2[1]), (int)((temp_dst2[2]-temp_dst2[0])/2+temp_dst2[0]), (int)((temp_dst2[3]-temp_dst2[1])/2+temp_dst2[1]), temp_paint);
		
			}
		}
	}
	
	private float[] temp_dst2 ;
	float[] FixPoints(float[] src)  //旋转后重新排序
	{
		float[] dst = new float[4];
		dst[0] = (src[0] > src[2]) ? src[2] : src[0];
		dst[1] = (src[1] > src[3]) ? src[3] : src[1];
		dst[2] = (src[0] < src[2]) ? src[2] : src[0];
		dst[3] = (src[1] < src[3]) ? src[3] : src[1];
		return dst;
	}

	private PointF downPoint = new PointF();
	private PointF tempPoint = new PointF();
	private float dist_x;
	private float dist_y;
	private final int NONE = 0;
	private final int MOVE = 1;
	private final int LEFT_TOP_ZOOM = 2;
	private final int RIGHT_TOP_ZOOM = 3;
	private final int LEFT_BOTTOM_ZOOM = 4;
	private final int RIGHT_BOTTOM_ZOOM = 5;
	private int mode = NONE;
	private Matrix temp_matrix = new Matrix(); // 逆矩阵
	private float x;
	private float y;
	private float w;
	private float h;
	private final float MIN_LEN = 0.18f;
	private float[] temp_src4 = new float[8];
	private float[] temp_dst4 = new float[8];
	private float[] temp_src_pointer = new float[4];
	private float[] temp_dst_pointer = new float[4];
	private float start_dist;
	private float move_dist;
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(m_bmp == null || m_rect == null)
		{
			return false;
		}
		
		switch(event.getPointerCount())
		{
			case 1: //单指
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					temp_src4[0] = m_rect.m_x*m_bmp.getWidth();  //记录四个点
					temp_src4[1] = m_rect.m_y*m_bmp.getHeight();
					temp_src4[2] = (m_rect.m_x + m_rect.m_w)*m_bmp.getWidth();
					temp_src4[3] = (m_rect.m_y)*m_bmp.getHeight();
					temp_src4[4] = (m_rect.m_x)*m_bmp.getWidth();
					temp_src4[5] = (m_rect.m_y + m_rect.m_h)*m_bmp.getHeight();
					temp_src4[6] = (m_rect.m_x + m_rect.m_w)*m_bmp.getWidth();
					temp_src4[7] = (m_rect.m_y + m_rect.m_h)*m_bmp.getHeight();
					m_matrix.mapPoints(temp_dst4, temp_src4); //映射出看到的四个点
					
					temp_matrix.reset();
					m_matrix.invert(temp_matrix); //求逆矩阵
					temp_src[0] = event.getX();
					temp_src[1] = event.getY();
					temp_matrix.mapPoints(temp_dst,temp_src);  //映射到图片上的坐标
					temp_dst[0] = temp_dst[0] / m_bmp.getWidth();
					temp_dst[1] = temp_dst[1] / m_bmp.getHeight();
					downPoint.set(temp_dst[0], temp_dst[1]);
					x = m_rect.m_x;
					y = m_rect.m_y;
					w = m_rect.m_w;
					h = m_rect.m_h;
					if(temp_dst[0] >= m_rect.m_x && temp_dst[1] >= m_rect.m_y && temp_dst[0] <= m_rect.m_x+m_rect.m_w && temp_dst[1] <= m_rect.m_y + m_rect.m_h)
					{
						Log.i("down", "mode : MOVE");
						mode = MOVE;
					}
					if(isDwon(temp_dst4[0], temp_dst4[1], temp_src[0], temp_src[1]))
					{
						Log.i("down", "mode : LEFT_TOP_ZOOM");
						mode = LEFT_TOP_ZOOM;
						m_event = LEFT_TOP_DOWN;
						invalidate();
					}
					if(isDwon(temp_dst4[2], temp_dst4[3], temp_src[0], temp_src[1]))
					{
						Log.i("down", "mode : RIGHT_TOP_ZOOM");
						mode = RIGHT_TOP_ZOOM;
						if(m_r % 180 == 0)
						{
							m_event = RIGHT_TOP_DOWN;
						}
						else
						{
							m_event = LEFT_BOTTOM_DOWN;
						}
						invalidate();
					}
					if(isDwon(temp_dst4[4], temp_dst4[5], temp_src[0], temp_src[1]))
					{
						Log.i("down", "mode : LEFT_BOTTOM_ZOOM");
						mode = LEFT_BOTTOM_ZOOM;
						if(m_r % 180 == 0)
						{
							m_event = LEFT_BOTTOM_DOWN;
						}
						else
						{
							m_event = RIGHT_TOP_DOWN;
						}
						invalidate();
					}
					if(isDwon(temp_dst4[6], temp_dst4[7], temp_src[0], temp_src[1]))
					{
						Log.i("down", "mode : RIGHT_BOTTOM_ZOOM");
						mode = RIGHT_BOTTOM_ZOOM;
						m_event = RIGHT_BOTTOM_DOWN;
						invalidate();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					temp_matrix.reset();
					m_matrix.invert(temp_matrix);
					temp_src[0] = event.getX();
					temp_src[1] = event.getY();
					temp_matrix.mapPoints(temp_dst,temp_src);
					temp_dst[0] = temp_dst[0] / m_bmp.getWidth();
					temp_dst[1] = temp_dst[1] / m_bmp.getHeight();
					tempPoint.set(temp_dst[0], temp_dst[1]);
					
					dist_x = tempPoint.x - downPoint.x;
					dist_y = tempPoint.y - downPoint.y;
					
					if(mode == MOVE)
					{
//						Log.i("move", "dist_x = "+dist_x);
						m_rect.m_x = dist_x + x;
						m_rect.m_y = dist_y + y;
						if(m_rect.m_x <= 0) m_rect.m_x = 0;
						if(m_rect.m_y <= 0) m_rect.m_y = 0;
						if(m_rect.m_x + m_rect.m_w >= 1f) m_rect.m_x = 1f - m_rect.m_w;
						if(m_rect.m_y + m_rect.m_h >= 1f) m_rect.m_y = 1f - m_rect.m_h;
					}
					else if(mode == LEFT_TOP_ZOOM)
					{
						if(m_scale == FREESCALE)
						{
							m_rect.m_x = dist_x + x;
							m_rect.m_y = dist_y + y;
							m_rect.m_w = w - dist_x;
							m_rect.m_h = h - dist_y;
							if(m_rect.m_h <= MIN_LEN)
							{
								m_rect.m_h = MIN_LEN;
								m_rect.m_y = y + h - MIN_LEN;
							}
						}
						else
						{
							setLtScale(m_scale);
						}
						
						if(m_rect.m_x <= 0) 
						{
							m_rect.m_x = 0;
							m_rect.m_w = x + w;
						}
						if(m_rect.m_y <= 0)
						{
							m_rect.m_y = 0;
							m_rect.m_h = y + h;
						}
						if(m_rect.m_w <= MIN_LEN)
						{
							m_rect.m_w = MIN_LEN;
							m_rect.m_x = x + w - MIN_LEN;
						}
					}
					else if(mode == RIGHT_TOP_ZOOM)
					{
						if(m_scale == FREESCALE)
						{
							m_rect.m_y = dist_y + y;
							m_rect.m_w = w + dist_x;
							m_rect.m_h = h - dist_y;
							if(m_rect.m_h <= MIN_LEN)
							{
								m_rect.m_h = MIN_LEN;
								m_rect.m_y = y + h - MIN_LEN;
							}
						}
						else
						{
							setRtScale(m_scale);
						}
						
						if(m_rect.m_w <= MIN_LEN) m_rect.m_w = MIN_LEN;
						
						if(m_rect.m_y <= 0) 
						{
							m_rect.m_y = 0;
							m_rect.m_h = y + h;
						}
						if(m_rect.m_x + m_rect.m_w >= 1f) m_rect.m_w = 1f - m_rect.m_x;
					}
					else if(mode == LEFT_BOTTOM_ZOOM)
					{
						if(m_scale == FREESCALE)
						{
							m_rect.m_x = dist_x + x;
							m_rect.m_w = w - dist_x;
							m_rect.m_h = h + dist_y;
							if(m_rect.m_x <= 0) 
							{
								m_rect.m_x = 0;
								m_rect.m_w = x + w;
							}
							if(m_rect.m_y + m_rect.m_h >= 1f) m_rect.m_h = 1f - m_rect.m_y;
							if(m_rect.m_w <= MIN_LEN)
							{
								m_rect.m_w = MIN_LEN;
								m_rect.m_x = x + w - MIN_LEN;
							}
							if(m_rect.m_h <= MIN_LEN) m_rect.m_h = MIN_LEN;
						}
						else
						{
							setLbScale(m_scale);
						}
					}
					else if(mode == RIGHT_BOTTOM_ZOOM)
					{
						if(m_scale == FREESCALE)
						{
							m_rect.m_w = w + dist_x;
							m_rect.m_h = h + dist_y;
							if(m_rect.m_w <= MIN_LEN) m_rect.m_w = MIN_LEN;
							if(m_rect.m_h <= MIN_LEN) m_rect.m_h = MIN_LEN;
							if(m_rect.m_x + m_rect.m_w >= 1f) m_rect.m_w = 1f - m_rect.m_x;
							if(m_rect.m_y + m_rect.m_h >= 1f) m_rect.m_h = 1f - m_rect.m_y;
						}
						else
						{
							setRbScale(m_scale);
						}
					}
					
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					mode = NONE;
					m_event = NONE_EVENT;
					invalidate();
					break;
				}
				break;
			case 2: //双指
				switch(event.getAction())
				{
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					break;
				}
				break;
		}
		return true;
	}
	
	private float spacing(float[] f)
	{
		float x = f[0] - f[2];
		float y = f[1] - f[3];
		return FloatMath.sqrt(x*x+y*y);
	}
	
	public static final float FREESCALE = 100;
	public void setFree()
	{
		m_scale = FREESCALE;
		m_matrix = null;
		invalidate();
	}
	
	private float temp_h ;
	public void setScale(float s) //设置矩形的宽高比
	{
		if(m_r % 180 !=0)
		{
			m_scale = 1.0f/s;
			m_rect.m_w = 0.25f;
		}
		else
		{
			m_scale = s;
			m_rect.m_w = 0.4f;
		}
		isDisplay = true;
		if(m_scale != FREESCALE)
		{
			m_rect.m_h = ((m_rect.m_w * m_bmp.getWidth()) / m_scale) / m_bmp.getHeight();
			temp_h = (MIN_LEN * m_bmp.getWidth() / m_scale) / m_bmp.getHeight();
		}
		m_matrix = null;
		invalidate();
	}
	
	private void setLtScale(float s)
	{
		m_rect.m_x = dist_x + x;
		dist_y = (dist_x * m_bmp.getWidth() / s) / m_bmp.getHeight();
		m_rect.m_y = dist_y + y;
		m_rect.m_w = w - dist_x;
		m_rect.m_h = h - dist_y;
		
		if(m_rect.m_h <= temp_h)
		{
			m_rect.m_h = temp_h;
			m_rect.m_y = y + h - temp_h;
		}
	}
	
	private void setRtScale(float s)
	{
		m_rect.m_w = w + dist_x;
		dist_y = -(dist_x * m_bmp.getWidth() / s) / m_bmp.getHeight();
		m_rect.m_y = dist_y + y;
		m_rect.m_h = h - dist_y;
		
		if(m_rect.m_h <= temp_h)
		{
			m_rect.m_h = temp_h;
			m_rect.m_y = y + h - temp_h;
		}
	}
	
	private void setLbScale(float s)
	{
		m_rect.m_x = dist_x + x;
		m_rect.m_w = w - dist_x;
		dist_y = -(dist_x * m_bmp.getWidth() / s) / m_bmp.getHeight();
		m_rect.m_h = h + dist_y;
		if(m_rect.m_x <= 0) 
		{
			m_rect.m_x = 0;
			m_rect.m_w = x + w;
			m_rect.m_h = h + x;
		}
		if(m_rect.m_y + m_rect.m_h >= 1f) 
		{
			m_rect.m_h = 1f - m_rect.m_y;
			m_rect.m_w = w + (1f - y - h) * s;
			m_rect.m_x = x - (1f - y - h) * s;
		}
		if(m_rect.m_w <= MIN_LEN)
		{
			m_rect.m_w = MIN_LEN;
			m_rect.m_x = x + w - MIN_LEN;
		}
		
		if(m_rect.m_h <= temp_h) m_rect.m_h = temp_h;
	}
	
	private void setRbScale(float s)
	{
		m_rect.m_w = w + dist_x;
		dist_y = (dist_x * m_bmp.getWidth() / s) / m_bmp.getHeight();
		m_rect.m_h = h + dist_y;
		if(m_rect.m_w <= MIN_LEN) m_rect.m_w = MIN_LEN;
		if(m_rect.m_h <= temp_h) m_rect.m_h = temp_h;
		if(m_rect.m_x + m_rect.m_w >= 1f) m_rect.m_w = 1f - m_rect.m_x;
		if(m_rect.m_y + m_rect.m_h >= 1f) m_rect.m_h = 1f - m_rect.m_y;
	}
	
	//旋转
	private float scale = 1f;
	public void setRotate(int r)
	{
		m_r = r;
		m_matrix = null;
		isDisplay = false;
		m_rect.rectReset();
		if(m_r % 180 != 0)
		{
			if(m_bmpW > m_bmpH)
			{
				scale = (float)getHeight()/(float)m_bmpW;
			}
			else{
				scale = (float)getWidth()/(float)m_bmpH;
			}
		}
		else
		{
			if(m_bmpW > m_bmpH)
			{
				scale = (float)getWidth()/(float)m_bmpW;
			}
			else
			{
				scale = (float)getHeight()/(float)m_bmpH;
			}
		}
//		m_sx = scale;
//		m_sy = scale;
		invalidate();
	}
	public void setRotate(int r, float sx, float sy)
	{
		m_sx = sx;
		m_sy = sy;
		m_r = r;
		m_matrix = null;
		isDisplay = false;
		m_rect.rectReset();
		invalidate();
	}
	
	//左右倒置
	public void setXInvert()
	{
		if(m_r % 180 == 0)
		{
			m_sx = (-1.0f) * m_sx;
		}
		else
		{
			m_sy = (-1.0f) * m_sy;
		}
		m_matrix = null;
		isDisplay = false;
		invalidate();
	}
	
	//上下倒置
	public void setYInvert()
	{
		if(m_r % 180 == 0)
		{
			m_sy = (-1.0f) * m_sy;
		}
		else
		{
			m_sx = (-1.0f) * m_sx;
		}
		m_matrix = null;
		isDisplay = false;
		invalidate();
	}
	
	public void showRect(boolean b)
	{
		isDisplay = b;
		m_matrix = null;
		invalidate();
	}
	
	public void reset(boolean b)
	{
		m_r = 0;
		m_sx = 1;
		m_sy = 1;
		isDisplay = b;
		m_scale = FREESCALE;
		m_event = NONE_EVENT;
		m_rect.rectReset();
		m_matrix = null;
		invalidate();
	}
	
	//是否在四个点
	private boolean isDwon(float x, float y, float dx, float dy)
	{
		if(dx >= x - RADIO && dx<=x+RADIO && dy>=y-RADIO && dy<=y+RADIO)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public class CutRect
	{
		//默认位置为图片中心
		public float m_x = 0.25f;
		public float m_y = 0.25f;
		public float m_w = 0.5f;
		public float m_h = 0.5f;
		
		public void rectReset()
		{
			m_x = 0.25f;
			m_y = 0.25f;
			m_w = 0.5f;
			m_h = 0.5f;
		}
	}

	//获取裁剪后的图片
	public Bitmap GetCutImage()
	{
		if(m_bmp != null){
			Bitmap out;
			
			if(m_obj instanceof String){
				out = BitmapFactory.decodeFile((String)m_obj);
			}
			else if(m_obj instanceof Integer){
				out = BitmapFactory.decodeResource(getResources(), (Integer)m_obj);
			}
			else{
				out = null;
			}

			int x = (int)(out.getWidth() * m_rect.m_x);
			int y = (int)(out.getHeight() * m_rect.m_y);
			int w = (int)(out.getWidth() * m_rect.m_w);
			int h = (int)(out.getHeight() * m_rect.m_h);

			Bitmap re;
			if(m_r % 180 != 0){
				re = Bitmap.createBitmap(h, w, Config.ARGB_8888);
			}
			else
			{
				re = Bitmap.createBitmap(w, h, Config.ARGB_8888);
			}
			Matrix m = new Matrix();
			m.postTranslate(-x, -y);   //移动到原点
			m.postRotate(m_r, w/2f, h/2f);  //绕中心旋转
			
			if(m_sx < 0 || m_sy < 0)
			{
				m.postScale(m_sx, m_sy, w/2f, h/2f);  //倒置
				if(m_r % 180 != 0)
				{
					m.postRotate(180, w/2f, h/2f);
					if(m_sx < 0 && m_sy  <0)
					{
						m.postRotate(180, w/2f, h/2f);
					}
				}
			}
			
			if(m_r % 180 != 0)
			{
				m.postTranslate(h/2f - w/2f, w/2f - h/2f); //如果不是旋转180度，旋转后移回原点
			}
			
			Canvas ca = new Canvas(re);
			ca.drawBitmap(out, m, null);
			
			return re;
		}

		return null;
	}
}
