package com.example.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseImageActivity extends Activity{
	
//	private ImageView mImageView = null;
	private static final int FLAG_CHOOSE = 1;
	private ImageButton returnbutton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
//		mImageView = (ImageView) findViewById(R.id.image);
	}
	
	public void onClick(View view){
		
		switch(view.getId()){
		case R.id.choose_img:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, FLAG_CHOOSE);
			break;
			default:
				break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("info", "onActivityResult-->requestCode:" + requestCode);
		if (resultCode == RESULT_OK && null != data){
			switch(requestCode){
			case FLAG_CHOOSE:
				Uri uri = data.getData();
				Log.i("info", "onActivityResult-->uri:" + uri + "-->uri.getAuthority()" + uri.getAuthority());
				if (!TextUtils.isEmpty(uri.getAuthority())){

					Cursor cursor = getContentResolver().query(uri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);
					if (null == cursor){
						Toast.makeText(this, R.string.no_found, Toast.LENGTH_SHORT).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d("may", "path="+path);
					Intent intent = new Intent(this, HandleImageActivity.class);
					intent.putExtra("path", path);
					startActivity(intent);				
				}else{
					Intent intent = new Intent(this, HandleImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivity(intent);	
				}				
				break;
			}
		}
	}
	
}
