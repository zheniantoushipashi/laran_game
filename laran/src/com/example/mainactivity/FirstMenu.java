package com.example.mainactivity ;
//Download by http://www.codefans.net

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import com.ant.liao.GifView;
import com.example.game.FirstGame;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FirstMenu extends Activity {
	  MediaPlayer mMediaPlayer;
	private Boolean flag=false;
	private ImageButton home;
	private ImageButton menu;
	private ImageButton search;
	private ImageButton exit;
	private ImageButton guanyu;
	private ImageButton tupian;
	private ImageButton laran;
	private ImageButton yinxiao;
	private ImageButton draw;
	private ImageButton jihe;
	private RelativeLayout level2;
	private RelativeLayout level3;
	private GifView gv;
	 Context mContext = null;
	private boolean isLevel2Show = true;
	private boolean isLevel3Show = true;
	
	 private void playLocalFile() {
	     mMediaPlayer = MediaPlayer.create(this, R.raw.music);
	        mMediaPlayer.setLooping(true);
	       
	 
	        try {
	            mMediaPlayer.prepare();
	        } catch (IllegalStateException e) {
	            
	        } catch (IOException e) {
	           
	        }
	 
	        mMediaPlayer.start();      
	       
	    }
	 public boolean copyFile(String from, String to) {

	        try {
	            int bytesum = 0;
	            int byteread = 0;
	            File oldfile = new File(from);
	            //TODO:add storege check.
	            if (oldfile.exists()) {
	                InputStream inStream = getResources().getAssets().open(from);
	                OutputStream fs = new BufferedOutputStream(new FileOutputStream(
	                        to));
	                byte[] buffer = new byte[8192];
	                while ((byteread = inStream.read(buffer)) != -1) {
	                    bytesum += byteread;
	                    fs.write(buffer, 0, byteread);
	                }
	                inStream.close();
	                fs.close();
	            }
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	 private void CopyAssets(String assetDir,String dir) {
         String[] files;    
          try    
          {    
              files = this.getResources().getAssets().list(assetDir);    
          }    
          catch (IOException e1)    
          {    
              return;    
          }    
          File mWorkingPath = new File(dir);
          //if this directory does not exists, make one. 
          if(!mWorkingPath.exists())    
          {    
              if(!mWorkingPath.mkdirs())    
              {    

              }    
          }    

          for(int i = 0; i < files.length; i++)    
          {    
              try    
              {    
                  String fileName = files[i]; 
                  //we make sure file name not contains '.' to be a folder. 
                  if(!fileName.contains("."))
                  {
                      if(0==assetDir.length())
                      {
                          CopyAssets(fileName,dir+fileName+"/");
                      }
                      else
                      {
                          CopyAssets(assetDir+"/"+fileName,dir+fileName+"/");
                      }
                      continue;
                  }
                  File outFile = new File(mWorkingPath, fileName);    
                  if(outFile.exists()) 
                      outFile.delete();
                  InputStream in =null;
                  if(0!=assetDir.length())
                      in = getAssets().open(assetDir+"/"+fileName);    
                  else
                      in = getAssets().open(fileName);
                  OutputStream out = new FileOutputStream(outFile);  

                  // Transfer bytes from in to out   
                  byte[] buf = new byte[1024];    
                  int len;    
                  while ((len = in.read(buf)) > 0)    
                  {    
                      out.write(buf, 0, len);    
                  }    

                  in.close();    
                  out.close();    
              }    
              catch (FileNotFoundException e)    
              {    
                  e.printStackTrace();    
              }   


              catch (IOException e)    
              {    
                  e.printStackTrace();    
              }         
          }
         }

	 public String SaveFileToSDCard(String path, String name, byte[] bytes)
		{
			// 判断SD卡是否可读写
			if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			{
				String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
				if(path != null && !path.equals(""))
				{
					dir += File.separator + path;
				}

				try
				{
					File fileDir = new File(dir);
					if(!fileDir.exists())
					{
						fileDir.mkdirs();
					}

					dir += File.separator + name;
					File saveFile = new File(dir);
					if(!saveFile.exists())
					{
						saveFile.createNewFile(); // 需要在AndroidManifest.xml里申请许可
					}
					FileOutputStream os = new FileOutputStream(saveFile);
					os.write(bytes);
					os.close();

					return dir;
				}
				catch(FileNotFoundException e)
				{
					System.out.println("SD openFileOutput - FileNotFoundException!!!");
				}
				catch(IOException e)
				{
					System.out.println("SD openFileOutput - IOException!!!");
				}
			}

			return null;
		}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caidan);
		playLocalFile();
		//AssetManager assets = getAssets();
		
	
		
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.stage1);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		 File f=new File("mnt/sdcard/laranPicture/LaRan/game");
		 if(!f.exists())
			{
				f.mkdirs();
			}
		 if(SaveFileToSDCard(null, "/laranPicture/LaRan/"+"stage1.jpg", output.toByteArray()) == null)
			{
				Toast.makeText(getApplicationContext(), "SD卡不可写入！", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				//Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_LONG).show();
//				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED), Environment.getExternalStorageDirectory().getAbsolutePath());
			}
		
		
		search=(ImageButton) findViewById(R.id.search);
		
		home = (ImageButton) findViewById(R.id.home);
		menu = (ImageButton) findViewById(R.id.menu);
		guanyu=(ImageButton) findViewById(R.id.c7);
		exit=(ImageButton) findViewById(R.id.exit);
		yinxiao=(ImageButton) findViewById(R.id.c1);
		tupian=(ImageButton) findViewById(R.id.c3);
		jihe=(ImageButton) findViewById(R.id.c6);
		draw=(ImageButton) findViewById(R.id.c2);
		laran=(ImageButton) findViewById(R.id.c4);
		level2 = (RelativeLayout) findViewById(R.id.level2);
		level3 = (RelativeLayout) findViewById(R.id.level3);
		gv = (GifView)findViewById(R.id.dh);  
		gv.setGifImage(R.drawable.c);  
		yinxiao.setOnClickListener(new OnClickListener() {		   
			    public void onClick(View arg0) {
			 	if(!flag){
			 		//yinxiao.setImageResource(R.drawable.on);
			 	//	yinxiao.setVisibility(View.VISIBLE);
			    		    mMediaPlayer.stop();
			    		    flag=true;
			    	}else{
			    		//yinxiao.setImageResource(R.drawable.off);
			    		//yinxiao.setVisibility(View.VISIBLE);	    		   
		    		    playLocalFile();
		    		   flag=false;
			    	}
			    
			    
			    }
			});
		 search.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, Stage1.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
		 jihe.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, JiHeActivity.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
		 draw.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, LaRanActivity.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
      laran.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, ImageFilterMain.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
tupian.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, HuaDong.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
		 guanyu.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	   Intent intent = new Intent();
				          //指定intent要启动的类
				          intent.setClass(FirstMenu.this, GuanYu.class);
				          //启动一个新的Activity
				          startActivity(intent);
				          //关闭当前的Activity
				          FirstMenu.this.finish();
			    }
			});
		 exit.setOnClickListener(new OnClickListener() {
			   
			    public void onClick(View arg0) {
			    	View img = findViewById(R.id.dh);
					img.startAnimation(new TVOffAnimation());
			    	 System.exit(1);
			    }
			});
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isLevel3Show){
					//隐藏3级导航菜单
					MyAnimation.startAnimationOUT(level3, 500, 0);
				}else {
					//显示3级导航菜单
					MyAnimation.startAnimationIN(level3, 500);
				}
				
				isLevel3Show = !isLevel3Show;
			}
		});
		
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isLevel2Show){
					//显示2级导航菜单
					MyAnimation.startAnimationIN(level2, 500);
				} else {
					if(isLevel3Show){
						//隐藏3级导航菜单
						MyAnimation.startAnimationOUT(level3, 500, 0);
						//隐藏2级导航菜单
						MyAnimation.startAnimationOUT(level2, 500, 500);
						isLevel3Show = !isLevel3Show;
					}
					else {
						//隐藏2级导航菜单
						MyAnimation.startAnimationOUT(level2, 500, 0);
					}
				}
				isLevel2Show = !isLevel2Show;
			}
		});

	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        System.exit(1);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

}
