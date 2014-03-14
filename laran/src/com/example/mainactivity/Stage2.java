package com.example.mainactivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.game.FirstGame;
import com.example.mainactivity.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.*;
import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
public class Stage2 extends Activity {
	   GifView  gv;
   ImageButton sure1;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   
		setContentView(R.layout.stage2);
		gv = (GifView)findViewById(R.id.dh);  
		gv.setGifImage(R.drawable.dh);  
		sure1=(ImageButton)findViewById(R.id.sure1);
		sure1.setVisibility(View.VISIBLE);
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.stage2);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		 File f=new File("mnt/sdcard/laranPicture/LaRan/game");
		 if(!f.exists())
			{
				f.mkdirs();
			}
		 if(SaveFileToSDCard(null, "/laranPicture/LaRan/"+"stage2.jpg", output.toByteArray()) == null)
			{
				Toast.makeText(getApplicationContext(), "SD卡不可写入！", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_LONG).show();
//				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED), Environment.getExternalStorageDirectory().getAbsolutePath());
			}
		sure1.setOnClickListener(new View.OnClickListener() 
	    {                  public void onClick(View v) {
	   	  
	   	   Intent intent = new Intent();
	          //指定intent要启动的类
	          intent.setClass(Stage2.this, JinDuTiao.class);
	          //启动一个新的Activity
	          startActivity(intent);
	          //关闭当前的Activity
	          Stage2.this.finish();
	       			 
	       	  }
	    });   
	}
	 

}

