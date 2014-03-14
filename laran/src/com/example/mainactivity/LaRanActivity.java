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
      //������¼��ǰ����һ����ɫ
     private int whichColor = 0;
     private int whichSize=0;
     private Canvas cacheCanvas;
 	private Bitmap cachebBitmap;
 	private Path path;
     /** Called when the activity is first created. */
 	  public void saveToFile(String filename) throws FileNotFoundException {
     	 File f = new File(filename);
  		if(f.exists())
  			throw new RuntimeException("�ļ���" + filename + " �Ѵ��ڣ�");
  			
  		FileOutputStream fos = new FileOutputStream(new File(filename));
  		//�� bitmap ѹ����������ʽ��ͼƬ����
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
     	// ����һ����Ļ��С��λͼ����Ϊ����
 		cachebBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
 	
 		cacheCanvas = new Canvas(cachebBitmap);
         findViewById(R.id.jihebutton).setOnClickListener(this);
         mPaint = new Paint();
         //���ʵ���ɫ
        mPaint.setColor(Color.RED);
      
        //���ʵĴ�ϸ
        jiheButton=(ImageButton)this.findViewById(R.id.jihebutton);
        jiheButton.setImageResource(R.drawable.jihe);
        jiheButton.setVisibility(View.VISIBLE);
        jiheButton.setOnClickListener(new View.OnClickListener() 
        {                  public void onClick(View v) {
       	  
       	   Intent intent = new Intent();
              //ָ��intentҪ��������
              intent.setClass(LaRanActivity.this, JiHeActivity.class);
              //����һ���µ�Activity
              startActivity(intent);
              //�رյ�ǰ��Activity
              LaRanActivity.this.finish();
           			 
           	  }
        });    
        cleanButton = (ImageButton)this.findViewById(R.id.flushbutton);
         cleanButton.setImageResource(R.drawable.clean);
         cleanButton.setVisibility(View.VISIBLE);
         //��ť����
         cleanButton.setOnClickListener(new View.OnClickListener() 
         {
            
            @Override
             public void onClick(View v) 
             {
                 // TODO Auto-generated method stub
                //��������SurfaceView
                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                 mCanvas.drawColor(Color.BLACK);
                  //������ɣ��ύ�޸�
                  mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                //������һ��
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
                .setTitle("��Ƥ��С")
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
                                 //���ʵ���ɫ
                                 mPaint.setStrokeWidth(0.2f);
                                 whichSize = 0;
                                 break;
                            }
                            case 1:
                           {
                               //���ʵ���ɫ
                                 mPaint.setStrokeWidth(0.6f);
                                 whichSize = 1;
                                break;
                             }
                            case 2:
                             {
                                 //���ʵ���ɫ
                                 mPaint.setStrokeWidth(1.0f);
                                 whichSize = 2;
                                  break;
                             }
                            case 3:
                            {
                                //���ʵ���ɫ
                                mPaint.setStrokeWidth(1.4f);
                                whichSize = 3;
                                 break;
                            }
                            case 4:
                            {
                                //���ʵ���ɫ
                                mPaint.setStrokeWidth(1.8f);
                                whichSize = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
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
                .setTitle("���ʴ�С")
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
                                 //���ʵ���ɫ
                                 mPaint.setStrokeWidth(0.2f);
                                 whichSize = 0;
                                 break;
                            }
                            case 1:
                           {
                               //���ʵ���ɫ
                                 mPaint.setStrokeWidth(0.6f);
                                 whichSize = 1;
                                break;
                             }
                            case 2:
                             {
                                 //���ʵ���ɫ
                                 mPaint.setStrokeWidth(1.0f);
                                 whichSize = 2;
                                  break;
                             }
                            case 3:
                            {
                                //���ʵ���ɫ
                                mPaint.setStrokeWidth(1.4f);
                                whichSize = 3;
                                 break;
                            }
                            case 4:
                            {
                                //���ʵ���ɫ
                                mPaint.setStrokeWidth(1.8f);
                                whichSize = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
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
               //ָ��intentҪ��������
               intent.setClass(LaRanActivity.this, HuaDong.class);
               //����һ���µ�Activity
               startActivity(intent);
               //�رյ�ǰ��Activity
               LaRanActivity.this.finish();
            			 
            	  }
         });    
       
         colorButton = (ImageButton)this.findViewById(R.id.colorbutton);
         colorButton.setImageResource(R.drawable.ys);
         colorButton.setVisibility(View.VISIBLE);
         //��ť����
         colorButton.setOnClickListener(new View.OnClickListener() 
         {
             
        
             public void onClick(View v) 
             {
                // TODO Auto-generated method stub
                Dialog mDialog = new AlertDialog.Builder(LaRanActivity.this)
                .setTitle("��ɫ����")
                 .setSingleChoiceItems(new String[]{"��ɫ","��ɫ","��ɫ","��ɫ","��ɫ"}, whichColor, new DialogInterface.OnClickListener() 
                 {
                     
                    @Override
                     public void onClick(DialogInterface dialog, int which) 
                    {
                        // TODO Auto-generated method stub
                       switch(which)
                         {
                             case 0:
                            {
                                 //���ʵ���ɫ
                                 mPaint.setColor(Color.RED);
                               whichColor = 0;
                                 break;
                            }
                            case 1:
                           {
                               //���ʵ���ɫ
                                 mPaint.setColor(Color.GREEN);
                                  whichColor = 1;
                                break;
                             }
                            case 2:
                             {
                                 //���ʵ���ɫ
                                 mPaint.setColor(Color.BLUE);
                                 whichColor = 2;
                                  break;
                             }
                            case 3:
                            {
                                //���ʵ���ɫ
                                mPaint.setColor(Color.YELLOW);
                                whichColor = 3;
                                 break;
                            }
                            case 4:
                            {
                                //���ʵ���ɫ
                                mPaint.setColor(Color.WHITE);
                                whichColor = 4;
                                 break;
                            }
                        }
                     }
              })
                 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
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
          //��ȡx����
          float x = event.getX();
         //��ȡy���꣨��֪��ΪʲôҪ��ȥһ��ƫ��ֵ�ŶԵ�׼��Ļ��
         float y = event.getY()-50;
        
         //��һ�ν����Ȳ���
         if(canDraw)
         {     
              //��ȡ�����¼�
             switch(event.getAction())
              {
                 //������϶��¼�
                 case MotionEvent.ACTION_MOVE:
                {               
                      //��������SurfaceView
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
                      //������һ��
                      mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                      mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                     
                      break;
                  }
              }
          
          }
      
          //����Ŀǰ��x����ֵ
          oldX = x;
         //����Ŀǰ��y����ֵ
         oldY = y;
       
          canDraw = true;
         
          return true;
        
      }

      Handler handler = new Handler();

  	@Override
  	protected void onResume()
  	{
  		super.onResume();
  		// ��Ҫ��ʾ��popupwindow���뵽��Ϣ�������ģ�����activity����������Ȼ����popupwindow�ӳ�1������ʾ�������̣߳�
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
