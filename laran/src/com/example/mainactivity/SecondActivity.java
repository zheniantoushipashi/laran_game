package com.example.mainactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.total.Total;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SecondActivity extends Activity
{
	public static  Bitmap m_bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.second);

		((ImageView)this.findViewById(R.id.img)).setImageBitmap(m_bmp);
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do something
        	  Intent intent = new Intent();
              //指定intent要启动的类
              intent.setClass(SecondActivity.this, ImageFilterMain.class);
              //启动一个新的Activity
              startActivity(intent);
              //关闭当前的Activity
              SecondActivity.this.finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
	@Override
	protected void onDestroy()
	{
		((ImageView)this.findViewById(R.id.img)).setImageBitmap(null);
		if(m_bmp != null)
		{
			m_bmp.recycle();
			m_bmp = null;
		}
		super.onDestroy();
	}

	public void OnClick(View v)
	{
		if(m_bmp != null)
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			m_bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
			 File f=new File("mnt/sdcard/laranPicture/LaRan");
		      String[] images = f.list(new ImageFilter());  
		      System.out.println("size="+images.length);  
		      long[] a;
		      a=new long[images.length];
		      for (int i = 0; i < images.length; i++){   
		          		        
		        	  String imgfile=images[i];   
		        	  if(images[i].length()==18){
		        	  String s1=imgfile.substring(0,14 );
			          long result=Long.parseLong(s1);      
			         a[i]=result;    
		        	  }
		                
				 }     
		         long max=a[0];
		         for(int k=0;k<a.length;k++) { 
		       	  if(max<a[k]) { 
		       	  max = a[k]; 
		       	  } 
		       	  }                
			if(SaveFileToSDCard(null, "/laranPicture/LaRan/"+max+".jpg", output.toByteArray()) == null)
			{
				Toast.makeText(getApplicationContext(), "SD卡不可写入！", Toast.LENGTH_LONG).show();
				
				return;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_LONG).show();
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED), Environment.getExternalStorageDirectory().getAbsolutePath());
				if(Total.compareImage("mnt/sdcard/laranPicture/LaRan/"+max+".jpg","mnt/sdcard/laranPicture/LaRan/"+"stage1.jpg")>=20){
					Intent intent = new Intent();
			          //指定intent要启动的类
			          intent.setClass(SecondActivity.this, JiSuan.class);
			          //启动一个新的Activity
			          startActivity(intent);
			          //关闭当前的Activity
			          SecondActivity.this.finish();
				}else{
					Intent intent = new Intent();
			          //指定intent要启动的类
			          intent.setClass(SecondActivity.this, Fail.class);
			          //启动一个新的Activity
			          startActivity(intent);
			          //关闭当前的Activity
			          SecondActivity.this.finish();
				}
				

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
}
