package com.example.mainactivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.example.mainactivity.Eraser;



import android.app.Activity;
   import android.app.AlertDialog;
   import android.app.Dialog;
import android.content.Context;
   import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
   import android.graphics.Canvas;
   import android.graphics.Color;
   import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
  import android.graphics.Rect;
  import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
  import android.view.MotionEvent;
  import android.view.SurfaceHolder;
  import android.view.SurfaceView;
  import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.graphics.PorterDuff;  
import android.graphics.Path;
  public class LaRanActivity extends Activity implements OnClickListener
  {
	  private Eraser eraser=null;
	  private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/";
      private SurfaceView mSurfaceView = null;
      private SurfaceHolder mSurfaceHolder = null;
    private ImageButton cleanButton = null;
     private ImageButton colorButton = null;
     private ImageButton bcButton = null;
     private ImageButton xpButton=null;
     private ImageButton lrButton=null;
     private ImageButton hbButton=null;
     private ImageButton jiheButton=null;
     private Button bctpbutton=null;
     private Button xkbutton=null;
     private float oldX = 0f;
     private float oldY = 0f;
     Context mContext = null;
    private boolean canDraw = false;
      private Paint mPaint = null;
      private Paint nPaint = null;
      //用来记录当前是哪一种颜色
     private int whichColor = 0;
     private int whichSize=0;
     private Canvas cacheCanvas;
 	private Bitmap cachebBitmap;
 	private Path path;
     /** Called when the activity is first created. */
 	  public void saveToFile(String filename) throws FileNotFoundException {
     	 File f = new File(filename);
  		if(f.exists())
  			throw new RuntimeException("文件：" + filename + " 已存在！");
  			
  		FileOutputStream fos = new FileOutputStream(new File(filename));
  		//将 bitmap 压缩成其他格式的图片数据
  		cachebBitmap.compress(CompressFormat.PNG, 50, fos);
  		try {
  			fos.close();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	}
     public void onCreate(Bundle savedInstanceState) 
     {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_abc);
     	path = new Path();
	
	
         mSurfaceView = (SurfaceView)this.findViewById(R.id.surfaceview);
        
         mSurfaceHolder = mSurfaceView.getHolder();
     	// 创建一张屏幕大小的位图，作为缓冲
 		cachebBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
 	
 		cacheCanvas = new Canvas(cachebBitmap);
         findViewById(R.id.jihebutton).setOnClickListener(this);
         mPaint = new Paint();
         //画笔的颜色
        mPaint.setColor(Color.RED);
      
        //画笔的粗细
        jiheButton=(ImageButton)this.findViewById(R.id.jihebutton);
        jiheButton.setImageResource(R.drawable.jihe);
        jiheButton.setVisibility(View.VISIBLE);
        jiheButton.setOnClickListener(new View.OnClickListener() 
        {                  public void onClick(View v) {
       	  
       	   Intent intent = new Intent();
              //指定intent要启动的类
              intent.setClass(LaRanActivity.this, JiHeActivity.class);
              //启动一个新的Activity
              startActivity(intent);
              //关闭当前的Activity
              LaRanActivity.this.finish();
           			 
           	  }
        });    
        cleanButton = (ImageButton)this.findViewById(R.id.flushbutton);
         cleanButton.setImageResource(R.drawable.clean);
         cleanButton.setVisibility(View.VISIBLE);
         //按钮监听
         cleanButton.setOnClickListener(new View.OnClickListener() 
         {
            
            @Override
             public void onClick(View v) 
             {
                 // TODO Auto-generated method stub
                //锁定整个SurfaceView
                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                 mCanvas.drawColor(Color.BLACK);
                  //绘制完成，提交修改
                  mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                //重新锁一次
                 mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                 mSurfaceHolder.unlockCanvasAndPost(mCanvas);
             }
         });    
         xpButton=(ImageButton)this.findViewById(R.id.xpbutton);
         xpButton.setImageResource(R.drawable.xp);
         xpButton.setVisibility(View.VISIBLE);
         xpButton.setOnClickListener(new View.OnClickListener() 
         {
             
        
             public void onClick(View v) 
             {
            	 mPaint.setColor(Color.BLACK);
				// TODO Auto-generated method stub
                Dialog mDialog = new AlertDialog.Builder(LaRanActivity.this)
                .setTitle("橡皮大小")
                 .setSingleChoiceItems(new String[]{"0.2","0.6","1.0","1.4","1.8"}, whichSize, new DialogInterface.OnClickListener() 
                 {
                     
                    @Override
                     public void onClick(DialogInterface dialog, int which) 
                    {
                        
					// TODO Auto-generated method stub
                       switch(which)
                         {
                             case 0:
                            {
                                 //画笔的颜色
                                 mPaint.setStrokeWidth(0.2f);
                                 whichSize = 0;
                                 break;
                            }
                            case 1:
                           {
                               //画笔的颜色
                                 mPaint.setStrokeWidth(0.6f);
                                 whichSize = 1;
                                break;
                             }
                            case 2:
                             {
                                 //画笔的颜色
                                 mPaint.setStrokeWidth(1.0f);
                                 whichSize = 2;
                                  break;
                             }
                            case 3:
                            {
                                //画笔的颜色
                                mPaint.setStrokeWidth(1.4f);
                                whichSize = 3;
                                 break;
                            }
                            case 4:
                            {
                                //画笔的颜色
                                mPaint.setStrokeWidth(1.8f);
                                whichSize = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() 
                {
                      
                  
                    public void onClick(DialogInterface dialog, int which) 
                    {
                        // TODO Auto-generated method stub
                         dialog.dismiss();
                     }
                })
                .create();
                 mDialog.show();
              }
          });
         hbButton=(ImageButton)this.findViewById(R.id.hbbutton);
         hbButton.setImageResource(R.drawable.hb);
         hbButton.setVisibility(View.VISIBLE);
         hbButton.setOnClickListener(new View.OnClickListener() 
         {
             
        
             public void onClick(View v) 
             {
            	
                Dialog mDialog = new AlertDialog.Builder(LaRanActivity.this)
                .setTitle("画笔大小")
                 .setSingleChoiceItems(new String[]{"0.2","0.6","1.0","1.4","1.8"}, whichSize, new DialogInterface.OnClickListener() 
                 {
                     
                    @Override
                     public void onClick(DialogInterface dialog, int which) 
                    {
                        
					// TODO Auto-generated method stub
                       switch(which)
                         {
                             case 0:
                            {
                                 //画笔的颜色
                                 mPaint.setStrokeWidth(0.2f);
                                 whichSize = 0;
                                 break;
                            }
                            case 1:
                           {
                               //画笔的颜色
                                 mPaint.setStrokeWidth(0.6f);
                                 whichSize = 1;
                                break;
                             }
                            case 2:
                             {
                                 //画笔的颜色
                                 mPaint.setStrokeWidth(1.0f);
                                 whichSize = 2;
                                  break;
                             }
                            case 3:
                            {
                                //画笔的颜色
                                mPaint.setStrokeWidth(1.4f);
                                whichSize = 3;
                                 break;
                            }
                            case 4:
                            {
                                //画笔的颜色
                                mPaint.setStrokeWidth(1.8f);
                                whichSize = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() 
                {
                      
                  
                    public void onClick(DialogInterface dialog, int which) 
                    {
                        // TODO Auto-generated method stub
                         dialog.dismiss();
                     }
                })
                .create();
                 mDialog.show();
              }
          });
         lrButton=(ImageButton)this.findViewById(R.id.lrbutton);
         lrButton.setImageResource(R.drawable.lr);
         lrButton.setVisibility(View.VISIBLE);
         lrButton.setOnClickListener(new View.OnClickListener() 
         {                  @Override     
            	  public void onClick(View v) {
        	  
        	   Intent intent = new Intent();
               //指定intent要启动的类
               intent.setClass(LaRanActivity.this, HuaDong.class);
               //启动一个新的Activity
               startActivity(intent);
               //关闭当前的Activity
               LaRanActivity.this.finish();
            			 
            	  }
         });    
       
         colorButton = (ImageButton)this.findViewById(R.id.colorbutton);
         colorButton.setImageResource(R.drawable.ys);
         colorButton.setVisibility(View.VISIBLE);
         //按钮监听
         colorButton.setOnClickListener(new View.OnClickListener() 
         {
             
        
             public void onClick(View v) 
             {
                // TODO Auto-generated method stub
                Dialog mDialog = new AlertDialog.Builder(LaRanActivity.this)
                .setTitle("颜色设置")
                 .setSingleChoiceItems(new String[]{"红色","绿色","蓝色","黄色","白色"}, whichColor, new DialogInterface.OnClickListener() 
                 {
                     
                    @Override
                     public void onClick(DialogInterface dialog, int which) 
                    {
                        // TODO Auto-generated method stub
                       switch(which)
                         {
                             case 0:
                            {
                                 //画笔的颜色
                                 mPaint.setColor(Color.RED);
                               whichColor = 0;
                                 break;
                            }
                            case 1:
                           {
                               //画笔的颜色
                                 mPaint.setColor(Color.GREEN);
                                  whichColor = 1;
                                break;
                             }
                            case 2:
                             {
                                 //画笔的颜色
                                 mPaint.setColor(Color.BLUE);
                                 whichColor = 2;
                                  break;
                             }
                            case 3:
                            {
                                //画笔的颜色
                                mPaint.setColor(Color.YELLOW);
                                whichColor = 3;
                                 break;
                            }
                            case 4:
                            {
                                //画笔的颜色
                                mPaint.setColor(Color.WHITE);
                                whichColor = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() 
                {
                      
                  
                    public void onClick(DialogInterface dialog, int which) 
                    {
                        // TODO Auto-generated method stub
                         dialog.dismiss();
                     }
                })
                .create();
                 mDialog.show();
              }
          });
                    
      }
          
      public boolean onTouchEvent(MotionEvent event)
     {       
          //获取x坐标
          float x = event.getX();
         //获取y坐标（不知道为什么要减去一个偏移值才对得准屏幕）
         float y = event.getY()-50;
        
         //第一次进来先不管
         if(canDraw)
         {     
              //获取触屏事件
             switch(event.getAction())
              {
                 //如果是拖动事件
                 case MotionEvent.ACTION_MOVE:
                {               
                      //锁定整个SurfaceView
                     Canvas mCanvas = mSurfaceHolder.lockCanvas(); 
                     mCanvas.drawLine(x, y, oldX, oldY, mPaint);
                     mCanvas.drawLine(x, y, oldX-1, oldY+1, mPaint);
                    
                     mCanvas.drawLine(x, y, oldX-2, oldY+2, mPaint);
                     
                    
                     mCanvas.drawLine(x, y, oldX, oldY+2, mPaint);
                     mCanvas.drawLine(x, y, oldX, oldY+3, mPaint);
                     mCanvas.drawLine(x, y, oldX, oldY+1, mPaint);
                     mCanvas.drawLine(x, y, oldX-2, oldY+1, mPaint);
                     mCanvas.drawLine(x, y, oldX+1, oldY+1, mPaint);
                     
                     mCanvas.drawLine(x, y, oldX+2, oldY, mPaint);
                     mCanvas.drawLine(x+1, y+1, oldX+2, oldY, mPaint);
                     mCanvas.drawLine(x+2, y+2, oldX+2, oldY, mPaint);
                   
                     mCanvas.drawLine(x, y, oldX+1, oldY+1, mPaint);
                     mCanvas.drawLine(x, y, oldX+2, oldY+2, mPaint);
                     mCanvas.drawLine(x, y, oldX+3, oldY, mPaint);
                     mCanvas.drawLine(x, y, oldX, oldY+5, mPaint);
                     mCanvas.drawLine(x, y, oldX+4, oldY, mPaint);
                     mCanvas.drawLine(x, y, oldX+5, oldY, mPaint);
                    
                     mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                      //重新锁一次
                      mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                      mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                     
                      break;
                  }
              }
          
          }
      
          //保存目前的x坐标值
          oldX = x;
         //保存目前的y坐标值
         oldY = y;
       
          canDraw = true;
         
          return true;
        
      }

      Handler handler = new Handler();

  	@Override
  	protected void onResume()
  	{
  		super.onResume();
  		// 将要显示的popupwindow加入到消息处理中心，先让activity运行起来，然后让popupwindow延迟1秒中显示（不算线程）
  		handler.postDelayed(new Runnable()
  		{
  			public void run()
  			{
  				
  			}
  		},1000);
  	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.xpbutton:
			onClickButtonEraser();
		}
	}



	private void onClickButtonEraser() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Eraser.class);
		startActivity(intent);
	}
      
  }
