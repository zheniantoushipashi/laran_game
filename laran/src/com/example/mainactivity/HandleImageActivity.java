package com.example.mainactivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;


public class HandleImageActivity extends Activity{

	private ImageView mGetImage = null;
	private RelativeLayout mSaveStep = null;
	private LinearLayout mEffertButton = null;
	private LinearLayout mSeekBar = null;
	private LinearLayout mEditButton = null;
	private LinearLayout mEffectButton = null;
	private LinearLayout mFrameButton = null;
	private SeekBar mSaturationBar = null;
	private SeekBar mBrightnessBar = null;
	private SeekBar mContrastBar = null;
	
	private File mFile;
	private String mNewPath;
	private int mImgHeight = 0;
	private int mImgWidth = 0;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private static final int CAMERA_WITH_DATA = 1;
	private static final int PHOTO_PICKED_WITH_DATA = 2;
	private static final int SELECT_PICTURE = 3;
	
	private Bitmap mBitmap = null;
	private Bitmap mDstBmp = null;//��ʱ����
//	private Bitmap mSaturationBmp = null;
//	private Bitmap mBrightnessBmp = null;
//	private Bitmap mContrastBmp = null;
	private ColorMatrix mAllColorMatrix;
	private ColorMatrix mSaturationMatrix;
	private ColorMatrix mBrightnessMatrix;
	private ColorMatrix mContrastMatrix;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);				
		
		// ȫ����ʾ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//��ȡ���ĳߴ�
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;	
		Log.i("info", "HandleImageActivity-->mScreenWidth:" + mScreenWidth + "-->mScreenHeight:" + mScreenHeight);
		
		setContentView(R.layout.image_handle);
		
		mGetImage = (ImageView) findViewById(R.id.choose_image);
		mSaveStep = (RelativeLayout) findViewById(R.id.save_all);
		mEffertButton = (LinearLayout) findViewById(R.id.effert_button);
		mSeekBar = (LinearLayout) findViewById(R.id.seek_bar);
		mEditButton = (LinearLayout) findViewById(R.id.edit_button);
		mEffectButton = (LinearLayout) findViewById(R.id.effect_button);
		mFrameButton = (LinearLayout) findViewById(R.id.frame_button);
		mSaturationBar = (SeekBar) findViewById(R.id.saturation_bar);
		mBrightnessBar = (SeekBar) findViewById(R.id.brightness_bar);
		mContrastBar = (SeekBar) findViewById(R.id.contrast_bar);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		Log.i("info", "HandleImageActivity-->path:" + path);
		mFile = new File(path);
		if(path == null){
			Toast.makeText(this, R.string.load_failure, Toast.LENGTH_LONG).show();
			finish();
		}
		
		mBitmap = BitmapFactory.decodeFile(path);
		mImgWidth = mBitmap.getWidth();
		mImgHeight = mBitmap.getHeight();
		mGetImage.setImageBitmap(mBitmap);
		
		Log.i("info", "HandleImageActivity before-->mImgHeight:" + mImgHeight + "-->mImgWidth:" + mImgWidth);
		while(mImgWidth > mScreenWidth || mImgHeight > mScreenHeight){
			mImgWidth = mImgWidth / 2;
			mImgHeight = mImgHeight / 2;
		}
		Log.i("info", "HandleImageActivity-->mImgHeight:" + mImgHeight + "-->mImgWidth:" + mImgWidth);
		
		mDstBmp = Bitmap.createBitmap(mImgWidth, mImgHeight, Config.ARGB_8888);
		mDstBmp = mBitmap;
	}
	
	public void onClick(View view) throws IOException{
		
		int viewId = view.getId();
		Log.i("info", "onCLick-->viewId:" + viewId);
		
		switch(viewId){
			case R.id.edit:
				editHandle();
				break;
			case R.id.crop_button:
				doCropPhoto(mBitmap);
//				startCropIntent();
				break;
			case R.id.rotation_button:
				rotateEffect();
				break;
			case R.id.reversion_button:
				reversionEffect();
				break;
			case R.id.zoom_button:
				zoomEffect();
				break;
			case R.id.tone:
				toneHandle();
				break;
			case R.id.frame:
				addFrame();
				break;
			case R.id.frame_button_one:
				addFrameByID(R.id.frame_button_one);
				break;
			case R.id.frame_button_two:
				addFrameByID(R.id.frame_button_two);
				break;
			case R.id.frame_button_three:
				addFrameByID(R.id.frame_button_three);
				break;
			case R.id.frame_button_fore:
				addFrameByID(R.id.frame_button_fore);
				break;
			case R.id.effect:
				effect();
				break;
			case R.id.old_remeber_button:
				Bitmap oldBmp = oldRemeber(mBitmap);//����Ч��
				mGetImage.setImageBitmap(oldBmp);
				break;
			case R.id.sharpen_button:
				Bitmap sharpenBmp = sharpenImageAmeliorate(mBitmap);//ͼƬ��
				//Bitmap sharpenBmp = sharpenImage(mBitmap, 3);//ͼƬ��
				mGetImage.setImageBitmap(sharpenBmp);
				break;
			case R.id.blur_button:
				Bitmap blurBmp = blurImage(mBitmap);//ģ��Ч��
				mGetImage.setImageBitmap(blurBmp);
				break;
			case R.id.overlay_button:
				Bitmap overlayBmp = overlayAmeliorate(mBitmap);//ͼƬЧ������
				//Bitmap overlayBmp = overlay(mBitmap);//ͼƬЧ������
				mGetImage.setImageBitmap(overlayBmp);
				break;
			case R.id.emboss_button:
				Bitmap embossBmp = embossAmeliorate(mBitmap);//����Ч��
				//Bitmap embossBmp = emboss(mBitmap);//����Ч��
				mGetImage.setImageBitmap(embossBmp);
				break;
			case R.id.film_button:
				Bitmap filmBmp = filmAmeliorate(mBitmap);//��ƬЧ��
				//Bitmap filmBmp = film(mBitmap);//��ƬЧ��
				mGetImage.setImageBitmap(filmBmp);
				break;
			case R.id.sunshine_button:
				Bitmap sunshineBmp = sunshineAmeliorate(mBitmap);//����Ч��
				//Bitmap sunshineBmp = sunshine(mBitmap);//����Ч��
				mGetImage.setImageBitmap(sunshineBmp);
				break;
			case R.id.neon_button:
				Bitmap neonBmp = neon(mBitmap);//�޺�Ч��
				mGetImage.setImageBitmap(neonBmp);
				break;
			case R.id.halo_button:
				int width = mBitmap.getWidth() / 2;
				int height = mBitmap.getHeight() / 2;
				int radius = Math.min(width, height);
				Bitmap haloBmp = halo(mBitmap, width, 
						height, radius);//����Ч��
				mGetImage.setImageBitmap(haloBmp);
				break;
			case R.id.save:
				createSDCardDir();
				saveMyBitmap(100);
				break;
			case R.id.cancel:
				cancelSave();
				break;
			default:
				break;
		}
	}
	
	private void toneHandle(){
		mSaveStep.setVisibility(View.VISIBLE);
		mEffertButton.setVisibility(View.GONE);
		mSeekBar.setVisibility(View.VISIBLE);
		if (null == mAllColorMatrix){
			mAllColorMatrix = new ColorMatrix();
		}
		
		if(null == mSaturationMatrix){
			mSaturationMatrix = new ColorMatrix();
		}
		
		if(null == mBrightnessMatrix){
			mBrightnessMatrix = new ColorMatrix();
		}
		
		if(null == mContrastMatrix){
			mContrastMatrix = new ColorMatrix();
		}
		
		mSaturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

			// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
			Bitmap saturationBmp = Bitmap.createBitmap(mDstBmp.getWidth(), 
					mDstBmp.getHeight(), Config.ARGB_8888);
			// �õ����ʶ���
			Canvas canvas = new Canvas(saturationBmp); 
			// �½�paint
			Paint paint = new Paint(); 
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
				paint.setAntiAlias(true); 								
				// ��ΪĬ��ֵ
				mSaturationMatrix.reset();
				//���ñ��Ͷ�
				mSaturationMatrix.setSaturation((float) (progress / 100.0));					
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
				
				mAllColorMatrix.reset();
				// Ч������
				mAllColorMatrix.postConcat(mSaturationMatrix);
				mAllColorMatrix.postConcat(mBrightnessMatrix); 
				mAllColorMatrix.postConcat(mContrastMatrix); 
				
				paint.setColorFilter(new ColorMatrixColorFilter(mAllColorMatrix));
				canvas.drawBitmap(mBitmap, 0, 0, paint);
				mGetImage.setImageBitmap(saturationBmp);
			}			
		});
		
		mBrightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

			// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
			Bitmap brightnessBmp = Bitmap.createBitmap(mDstBmp.getWidth(), 
					mDstBmp.getHeight(), Config.ARGB_8888);
			// �õ����ʶ���
			Canvas canvas = new Canvas(brightnessBmp); 
			// �½�paint
			Paint paint = new Paint(); 
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub		
				int brightNess = progress - 127;				
				// ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
				paint.setAntiAlias(true); 			
				//���ó�Ĭ��ֵ
				mBrightnessMatrix.reset();
				mBrightnessMatrix.set(new float[]{
						1, 0, 0, 0, brightNess,
						0, 1, 0, 0, brightNess,
						0, 0, 1, 0, brightNess,
						0, 0, 0, 1, 0
				});						
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub	
				
				mAllColorMatrix.reset();
				// Ч������
				mAllColorMatrix.postConcat(mSaturationMatrix);
				mAllColorMatrix.postConcat(mBrightnessMatrix); 
				mAllColorMatrix.postConcat(mContrastMatrix); 
				
				paint.setColorFilter(new ColorMatrixColorFilter(mAllColorMatrix));
				canvas.drawBitmap(mBitmap, 0, 0, paint);
				mGetImage.setImageBitmap(brightnessBmp);
			}			
		});
		
		mContrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
			Bitmap contrastBmp = Bitmap.createBitmap(mDstBmp.getWidth(), 
					mDstBmp.getHeight(), Config.ARGB_8888);
			// �õ����ʶ���
			Canvas canvas = new Canvas(contrastBmp); 
			// �½�paint
			Paint paint = new Paint(); 
						
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub	
				float contrast = (float)((progress + 64) / 128.0);
				
				// ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
				paint.setAntiAlias(true); 
				//���ó�Ĭ��ֵ
				mContrastMatrix.reset();
				mContrastMatrix.set(new float[]{
						contrast, 0, 0, 0, 0,
						0, contrast, 0, 0, 0,
						0, 0, contrast, 0, 0,
						0, 0, 0, 1, 0
				});							
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
				mAllColorMatrix.reset();
				// Ч������
				mAllColorMatrix.postConcat(mSaturationMatrix);
				mAllColorMatrix.postConcat(mBrightnessMatrix); 
				mAllColorMatrix.postConcat(mContrastMatrix); 
				paint.setColorFilter(new ColorMatrixColorFilter(mAllColorMatrix));
				canvas.drawBitmap(mBitmap, 0, 0, paint);
				mGetImage.setImageBitmap(contrastBmp);
			}
		});
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("info", "onActivityResult-->requestCode:" + requestCode);
		
		if (resultCode == RESULT_OK && null != data){
			switch (requestCode) {
				case CAMERA_WITH_DATA:
					Bitmap cropBmp = data.getParcelableExtra("data");
					if(cropBmp != null){
						doCropPhoto(cropBmp);
					}
					
					break;
				case PHOTO_PICKED_WITH_DATA:
					Bitmap bmp = data.getParcelableExtra("data");
	
					if(bmp != null){		
						mGetImage.setImageBitmap(bmp);		
					}		
					break;
				case SELECT_PICTURE:
					mGetImage.setImageDrawable(Drawable.createFromPath(mFile.getAbsolutePath()));
					break;
				default:
					break;
			}
		}
	}

	private void editHandle(){
		mSaveStep.setVisibility(View.VISIBLE);
		mEffertButton.setVisibility(View.GONE);
		mEditButton.setVisibility(View.VISIBLE);
	}
	
	public static Intent getCropImageIntent(Bitmap data){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
	    intent.putExtra("data", data);
	    intent.putExtra("crop", "true");
	    intent.putExtra("aspectX", 1);
	    intent.putExtra("aspectY", 1);
	    intent.putExtra("outputX", 128);
	    intent.putExtra("outputY", 128);
//	    intent.putExtra("output", data);
        intent.putExtra("outputFormat", "JPEG");//���ظ�ʽ
	    intent.putExtra("return-data", true);

	    return intent;
	}  
	
	private void doCropPhoto(Bitmap data){

		Intent intent = getCropImageIntent(data);
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}
	
	private void startCropIntent(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");// crop=true �������ܳ������Ĳü�ҳ��.
        intent.putExtra("aspectX", 1);// ������Ϊ�ü���ı���.
        intent.putExtra("aspectY", 2);// x:y=1:2
        intent.putExtra("output", Uri.fromFile(mFile));
        intent.putExtra("outputFormat", "JPEG");//���ظ�ʽ

        startActivityForResult(Intent.createChooser(intent, "ѡ��ͼƬ"), SELECT_PICTURE);
	}
	
	/**
	 * ͼƬ��ת
	 * @param degree
	 */
	public Bitmap rotate(Bitmap bmp, float degree){
    	Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		
		return bm;
    }
	
	private void rotateEffect(){
		Bitmap bitMap = rotate(mBitmap, 90);//
		mGetImage.setImageBitmap(bitMap);
	}
	
	/**
	 * ͼƬ����
	 * @param bm
	 * @param w
	 * @param h
	 * @return
	 */
	public Bitmap zoom(Bitmap bm, float scale){
		Log.i("info", "zoom-->bm.getWidth():" + bm.getWidth());
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		
		return resizedBitmap;
	}
	
	private void zoomEffect(){
		Bitmap bitMap = zoom(mBitmap, (float) (1.0 / 2));
		Log.i("info", "zoomEffect-->bitMap.getWidth():" + bitMap.getWidth());
		mGetImage.setImageBitmap(bitMap);		
	}
	
	/**
	 * ͼƬ��ת
	 * @param bm
	 * @param flag
	 * @return
	 */
	public Bitmap reversion(Bitmap bmp, int flag){
		float[] floats = null;
		
		switch (flag){
		case 0: // ˮƽ��ת
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		case 1: // ��ֱ��ת
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}
		
		if (floats != null){
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			
			return bm;
		}
		
		return null;
	}
	
	private void reversionEffect(){
		Bitmap bitMap;
		bitMap = reversion(mBitmap, 0);
		mGetImage.setImageBitmap(bitMap);		
	}
	
	/**
	 * ��ӱ߿�
	 * 
	 * @param bmp
	 * @return
	 */
	private Bitmap addFrame(Bitmap bmp, int id){  
		int width = bmp.getWidth();  
		int height = bmp.getHeight();  
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
		// ��ȡ�߿�ͼƬ   
		Bitmap framePicture = null; //= BitmapFactory.decodeResource(this.getResources(), R.drawable.black);
		switch(id){
			case R.id.frame_button_one:
				framePicture = BitmapFactory.decodeResource(this.getResources(), R.drawable.frame_one);
				break;
			case R.id.frame_button_two:
				framePicture = BitmapFactory.decodeResource(this.getResources(), R.drawable.frame_two);
				break;
			case R.id.frame_button_three:
				framePicture = BitmapFactory.decodeResource(this.getResources(), R.drawable.frame_three);
				break;
			case R.id.frame_button_fore:
				framePicture = BitmapFactory.decodeResource(this.getResources(), R.drawable.frame_four);
				break;
			default:
				break;
		}
		
		int frameWidth = framePicture.getWidth();  
		int frameHeight = framePicture.getHeight();  
		float scaleX = width * 1F / frameWidth;  
		float scaleY = height * 1F / frameHeight; 
		
		Matrix matrix = new Matrix(); 		
		matrix.postScale(scaleX, scaleY);  		
		Bitmap frameCopy = Bitmap.createBitmap(framePicture, 0, 0, frameWidth, frameHeight, matrix, true); 
		
		int pixColor = 0;  
		int layColor = 0;  
		int newColor = 0;  
		int pixR = 0;  
		int pixG = 0;  
		int pixB = 0;  
		int pixA = 0;  
		int newR = 0;  
		int newG = 0;  
		int newB = 0;  
		int newA = 0;  
		int layR = 0;  
		int layG = 0;  
		int layB = 0;  
		int layA = 0;  
		float alpha = 0.3F;  
		float alphaR = 0F;  
		float alphaG = 0F;  
		float alphaB = 0F;  
		
		for (int i = 0; i < width; i++){  
			for (int k = 0; k < height; k++)  {  
				pixColor = bmp.getPixel(i, k);  
				layColor = frameCopy.getPixel(i, k);  
				// ��ȡԭͼƬ��RGBAֵ   
				pixR = Color.red(pixColor);  
				pixG = Color.green(pixColor);  
				pixB = Color.blue(pixColor);  
				pixA = Color.alpha(pixColor);  
				// ��ȡ�߿�ͼƬ��RGBAֵ   
				layR = Color.red(layColor);  
				layG = Color.green(layColor);  
				layB = Color.blue(layColor);  
				layA = Color.alpha(layColor);  
				// ��ɫ�봿��ɫ����ĵ�   
				if (layR < 20 && layG < 20 && layB < 20){  
					alpha = 1F;  
				}else{  
					alpha = 0.3F;  
				}  
				alphaR = alpha;  
				alphaG = alpha;  
				alphaB = alpha;  
				// ������ɫ����   
				newR = (int) (pixR * alphaR + layR * (1 - alphaR));  
				newG = (int) (pixG * alphaG + layG * (1 - alphaG));  
				newB = (int) (pixB * alphaB + layB * (1 - alphaB));  
				layA = (int) (pixA * alpha + layA * (1 - alpha));  
				// ֵ��0~255֮��   
				newR = Math.min(255, Math.max(0, newR));  
				newG = Math.min(255, Math.max(0, newG));  
				newB = Math.min(255, Math.max(0, newB));  
				newA = Math.min(255, Math.max(0, layA));  
				newColor = Color.argb(newA, newR, newG, newB);  
				bitmap.setPixel(i, k, newColor);  
			}  
		}  
		
		return bitmap;  
	}  
	
	private void addFrame(){
		mSaveStep.setVisibility(View.VISIBLE);
		mEffertButton.setVisibility(View.GONE);
		mFrameButton.setVisibility(View.VISIBLE);
	}
	
	private void addFrameByID(int id){
		Bitmap bitmap = addFrame(mBitmap, id);
		mGetImage.setImageBitmap(bitmap);
	}
	
	private void effect(){
		mSaveStep.setVisibility(View.VISIBLE);
		mEffertButton.setVisibility(View.GONE);
		mEffectButton.setVisibility(View.VISIBLE);
	}	
	
	/**
	 * ����Ч��(���֮ǰ�����Ż���һ��)
	 * @param bmp
	 * @return
	 */
	private Bitmap oldRemeber(Bitmap bmp)
	{
		// �ٶȲ���
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++){
			for (int k = 0; k < width; k++){
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ģ��Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap blurImage(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int newColor = 0;
		
		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				for (int m = 0; m < 9; m++)
				{
					int s = 0;
					int p = 0;
					switch(m)
					{
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}
				
				for (int m = 0; m < 9; m++)
				{
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}
				
				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		return bitmap;
	}
	
	/**
	 * �ữЧ��(��˹ģ��)(�Ż�������������)
	 * @param bmp
	 * @return
	 */
	private Bitmap blurImageAmeliorate(Bitmap bmp){
		long start = System.currentTimeMillis();
		// ��˹����
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int delta = 16; // ֵԽСͼƬ��Խ����Խ����Խ��
		
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}
				
				newR /= delta;
				newG /= delta;
				newB /= delta;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ��Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap sharpenImage(Bitmap bmp, float sharpen){
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		float alpha = 0.9F;
		int newColor = 0;
		
		int[][] colors = new int[8][3];
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				for (int m = 0; m < 8; m++)
				{
					int s = 0;
					int p = 0;
					switch(m)
					{
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}
				
				pixColor = bmp.getPixel(i, k);
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				for (int m = 0; m < 8; m++)
				{
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}
				
				newR = (int) ((pixR - newR / 8F) * alpha) + pixR;
				newG = (int) ((pixG - newG / 8F) * alpha) + pixG;
				newB = (int) ((pixB - newB / 8F) * alpha) + pixB;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		return bitmap;
	}
	
	/**
	 * ͼƬ�񻯣�������˹�任��
	 * @param bmp
	 * @return
	 */
	private Bitmap sharpenImageAmeliorate(Bitmap bmp){
		long start = System.currentTimeMillis();
		// ������˹����
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ͼƬЧ������
	 * @param bmp
	 * @return
	 */
	private Bitmap overlay(Bitmap bmp){
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		Bitmap overlay = BitmapFactory.decodeResource(this.getResources(), R.drawable.rainbow_overlay);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		int newColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		float alpha = 0.5F;
		for (int i = 0; i < width; i++)
		{
			for (int k = 0; k < height; k++)
			{
				pixColor = bmp.getPixel(i, k);
				layColor = overlayCopy.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				newColor = Color.argb(newA, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
			}
		}
		
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ͼƬЧ������
	 * @param bmp �����˳ߴ��С��Bitmap
	 * @return
	 */
	private Bitmap overlayAmeliorate(Bitmap bmp){
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		// �Ա߿�ͼƬ��������
		Bitmap overlay = BitmapFactory.decodeResource(this.getResources(), R.drawable.rainbow_overlay);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		final float alpha = 0.5F;
		
		int[] srcPixels = new int[width * height];
		int[] layPixels = new int[width * height];
		bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
		overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);
		
		int pos = 0;
		for (int i = 0; i < height; i++)
		{
			for (int k = 0; k < width; k++)
			{
				pos = i * width + k;
				pixColor = srcPixels[pos];
				layColor = layPixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				srcPixels[pos] = Color.argb(newA, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "overlayAmeliorate used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ����Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap emboss(Bitmap bmp){
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;		
		int pixColor = 0;		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				pixColor = bmp.getPixel(i, k);
				newR = Color.red(pixColor);
				newG = Color.green(pixColor);
				newB = Color.blue(pixColor);
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(200, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * ����Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap embossAmeliorate(Bitmap bmp){
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Log.i("info", "embossAmeliorate-->width:" + width + "-->height:" + height);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565 );		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;		
		int pixColor = 0;		
		int newR = 0;
		int newG = 0;
		int newB = 0;	
		int newA=0;
		int[] pixels = new int[width * height];
		
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len;k++)
			{
				pos = (i)* width+k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor)+20;
				pixG = Color.green(pixColor)+20;
				pixB = Color.blue(pixColor)+20;
				
				pixColor = pixels[pos + 1];
				newR =Color.red(pixColor)/2-pixR/2+127 ;
				newG =Color.green(pixColor)-newR+20 ;
				newB =Color.blue(pixColor)-pixB+200;
				
				newR = Math.min(255, Math.max(60, newR));
				newG = Math.min(255, Math.max(60, newG));
				newB = Math.min(255, Math.max(60, newB));
				
				pixels[pos] = Color.argb(200, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * ��ƬЧ��
	 * @param bmp
	 * @return
	 */
	private Bitmap film(Bitmap bmp)
	{
		// 255 - x
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = 255 - pixR+20;
				newG = 255 - pixG;
				newB = 255 - pixB+10;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(100, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * ��ƬЧ��
	 * @param bmp
	 * @return
	 */
	private Bitmap filmAmeliorate(Bitmap bmp)
	{
		// RGBA�����ֵ
		final int MAX_VALUE = 255;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				pos = i * width + k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = MAX_VALUE - pixR;
				newG = MAX_VALUE - pixG;
				newB = MAX_VALUE - pixB;
				
				newR = Math.min(MAX_VALUE, Math.max(0, newR));
				newG = Math.min(MAX_VALUE, Math.max(0, newG));
				newB = Math.min(MAX_VALUE, Math.max(0, newB));
				
				pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * ����Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap sunshine(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(centerX, centerY);
		
		float strength = 150F; // ����ǿ�� 100~150
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = pixR;
				newG = pixG;
				newB = pixB;
				
				int distance = (int) (Math.pow((centerX - i), 2) + Math.pow(centerY - k, 2));
				if (distance < radius * radius)
				{
					int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * ����Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap sunshineAmeliorate(Bitmap bmp)
	{
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(centerX, centerY);
		
		final float strength = 150F; // ����ǿ�� 100~150
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				pos = i * width + k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = pixR;
				newG = pixG;
				newB = pixB;
				
				// ���㵱ǰ�㵽�������ĵľ��룬ƽ������ϵ��������֮��ľ���
				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
				if (distance < radius * radius)
				{
					// ���վ����С�������ӵĹ���ֵ
					int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * �޺�Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap neon(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		int pixColor1 = 0;
		int pixColor2 = 0;
		
		int pixR1 = 0;
		int pixG1 = 0;
		int pixB1 = 0;
		int pixR2 = 0;
		int pixG2 = 0;
		int pixB2 = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int delta = 2;
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				pixColor1 = bmp.getPixel(i+1, k);
				pixColor2 = bmp.getPixel(i, k+1);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				pixR1 = Color.red(pixColor1);
				pixG1 = Color.green(pixColor1);
				pixB1 = Color.blue(pixColor1);
				
				pixR2 = Color.red(pixColor2);
				pixG2 = Color.green(pixColor2);
				pixB2 = Color.blue(pixColor2);
				
				newR = (int) (Math.sqrt(Math.pow(pixR - pixR1, 2) + Math.pow(pixR - pixR2, 2)) * delta);
				newG = (int) (Math.sqrt(Math.pow(pixG - pixG1, 2) + Math.pow(pixG - pixG2, 2)) * delta);
				newB = (int) (Math.sqrt(Math.pow(pixB - pixB1, 2) + Math.pow(pixB - pixB2, 2)) * delta);
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * ���ӱ߿�ͼƬ���ò���
	 * @param bmp
	 * @return
	 */
	private Bitmap alphaLayer(Bitmap bmp){
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		// �߿�ͼƬ
		Bitmap overlay = BitmapFactory.decodeResource(this.getResources(), R.drawable.black);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		int newColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		float alpha = 0.3F;
		float alphaR = 0F;
		float alphaG = 0F;
		float alphaB = 0F;
		for (int i = 0; i < width; i++)
		{
			for (int k = 0; k < height; k++)
			{
				pixColor = bmp.getPixel(i, k);
				layColor = overlayCopy.getPixel(i, k);
				
				// ��ȡԭͼƬ��RGBAֵ
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				// ��ȡ�߿�ͼƬ��RGBAֵ
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				// ��ɫ�봿��ɫ����ĵ�
				if (layR < 20 && layG < 20 && layB < 20)
				{
					alpha = 1F;
				}
				else
				{
					alpha = 0.3F;
				}
				
				alphaR = alpha;
				alphaG = alpha;
				alphaB = alpha;
				
				// ������ɫ����
				newR = (int) (pixR * alphaR + layR * (1 - alphaR));
				newG = (int) (pixG * alphaG + layG * (1 - alphaG));
				newB = (int) (pixB * alphaB + layB * (1 - alphaB));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				// ֵ��0~255֮��
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				newColor = Color.argb(newA, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
			}
		}
		
		
		return bitmap;
	}
	
	/**
	 * ����Ч��
	 * @param bmp
	 * @param x �������ĵ���bmp�е�x����
	 * @param y �������ĵ���bmp�е�y����
	 * @param r ���εİ뾶
	 * @return
	 */
	private Bitmap halo(Bitmap bmp, int x, int y, float r){  
//		long start = System.currentTimeMillis();  
		// ��˹����   
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };  
		int width = bmp.getWidth();  
		int height = bmp.getHeight(); 
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); 
		
		int pixR = 0;  
		int pixG = 0;  
		int pixB = 0;  
		int pixColor = 0;  
		int newR = 0;  
		int newG = 0;  
		int newB = 0;  
		
		int delta = 18; // ֵԽСͼƬ��Խ����Խ����Խ��   
		int idx = 0;  
		int[] pixels = new int[width * height];  
		bmp.getPixels(pixels, 0, width, 0, 0, width, height); 
		
		for (int i = 1, length = height - 1; i < length; i++){  
			for (int k = 1, len = width - 1; k < len; k++){  
				idx = 0;  
				int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));  
				// ������������ĵ���ģ������   
				if (distance > r * r){  
					for (int m = -1; m <= 1; m++){  
						for (int n = -1; n <= 1; n++){  
							pixColor = pixels[(i + m) * width + k + n];  
							pixR = Color.red(pixColor);  
							pixG = Color.green(pixColor);  
							pixB = Color.blue(pixColor);  
							newR = newR + (int) (pixR * gauss[idx]);  
							newG = newG + (int) (pixG * gauss[idx]);  
							newB = newB + (int) (pixB * gauss[idx]);  
							idx++;  
						}  
					}  
                      
					newR /= delta;  
					newG /= delta;  
					newB /= delta;  
					newR = Math.min(255, Math.max(0, newR));  
					newG = Math.min(255, Math.max(0, newG));  
					newB = Math.min(255, Math.max(0, newB));  
					pixels[i * width + k] = Color.argb(255, newR, newG, newB);  
					newR = 0;  
					newG = 0;  
					newB = 0;  
				}  
			}  
		}  
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
//		long end = System.currentTimeMillis();  
//		Log.d("may", "used time="+(end - start)); 
		
		return bitmap;  
	}
	
	/**
	 * ��ɫ����Ч��
	 * @param bmp
	 * @return
	 */
	private Bitmap stria(Bitmap bmp){
		long start = System.currentTimeMillis();
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // ����һ����ͬ��С��ͼƬ
		
		// �������ص��RGBֵ
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int[] pixels = new int[width * height]; // ����ͼƬ�����ص���Ϣ
		bmp.getPixels(pixels, 0, width, 0, 0, width, height); // ������ͼƬ���浽һά�����У�ÿwidth������Ϊһ��
		
		final int delta = 40; // ÿ40�����صĸ߶���Ϊһ����λ
		final int blackHeight = 10; // ��ɫ����߶�
		final int BLACK = 0;
		
		for (int i = 0; i < height; i++)
		{
			// ��ͼƬ�Ľ���������
			// ÿ��30�����صĸ߶Ⱦͻ����һ���߶�Ϊ10�����صĺ�ɫ���
			// ÿ40������Ϊһ����λ��ǰ���10���ؾͻᱻ����ɺ�ɫ
			if (i % delta <= blackHeight)
			{
				for (int k = 0; k < width; k++)
				{
					// �Ե�ǰ���ص㸳�µ�RGBֵ
					newR = BLACK;
					newG = BLACK;
					newB = BLACK;
					
					// Color.argb()���ǽ��ĸ�0~255��ֵ���һ�����ص㣬Ҳ����RGBAֵ��A��alpha����͸����
					pixels[i * width + k] = Color.argb(255, newR, newG, newB); // �޸����ص�
				}
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height); // �򴴽�����ͬ��С����ͼƬ�����ص�ֵ
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * ���ƹ�����������
	 * @param width ���̵Ŀ��
	 * @return
	 */
	private Bitmap chessbord(int width){
		long start = System.currentTimeMillis();
		Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
		int[] pixels = new int[width * width];
		
		final int delta = width / 8; // ������ͼƬ�ֳ�8 X 8������delta��ʾÿ������Ŀ�Ȼ�߶�
		final int blackPix = Color.BLACK; // ��ɫ��
		final int whitePix = Color.WHITE; // ��ɫ��
		
		int pixColor = whitePix;
		boolean isWhite = false; // ��ɫ�ı�ʶ
		for (int i = 0; i < width; i++) // ����
		{
			isWhite = !(i / delta % 2 == 0); // ��һ������ʼΪ��ɫ�������������λ���ǰ�ɫ
			for (int k = 0; k < width; k++) // ����
			{
				if (k / delta % 2 != 0) // �����ϵĺڰ���䣬ż���������Ǻ�ɫ
				{
					pixColor = isWhite ? blackPix : whitePix; // ���ǰ����ǰ�ɫ��Ҫ��ɺ�ɫ
				}
				else
				{
					pixColor = isWhite ? whitePix : blackPix; // ���������Ǻڰ����
				}
				
//				pixColor = k / delta % 2 != 0 ? (isWhite ? blackPix : whitePix) : (isWhite ? whitePix : blackPix);
				pixels[i * width + k] = pixColor;
			}
			
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	public static String savePicToSdcard(Bitmap bitmap, String path, String fileName) {   
		String filePath = "";   
		if (bitmap == null) {   
			return filePath;   
		} else {     
			filePath = path+ fileName;   
			File destFile = new File(filePath);   
			OutputStream os = null; 
			
			try {   
				os = new FileOutputStream(destFile);   
				bitmap.compress(CompressFormat.JPEG, 100, os); 
				
				os.flush();   
				os.close();   
			} catch (IOException e) {   
				filePath = "";   
			}   
		}   
			return filePath;   
	}  
	
	public String saveToLocal(Bitmap bm){
    	String path = "/sdcard/Pictures/";
    	
    	try{
			FileOutputStream fos = new FileOutputStream(path);
			bm.compress(CompressFormat.JPEG, 75, fos);
		
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return null;
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
		
		return path;
    }
	
	//�ж�SD���Ƿ����-->
	public String getSDPath(){
		File SDdir = null;
	    boolean sdCardExist = Environment.getExternalStorageState()
	    		.equals(android.os.Environment.MEDIA_MOUNTED);
	    if(sdCardExist){
	    	SDdir = Environment.getExternalStorageDirectory();
	    }
	    if(SDdir != null){
	    	return SDdir.toString();
	    }
	    else{
	    	return null;
	    }
	}

	//�����ļ���
	public void createSDCardDir(){
		if(getSDPath()==null){
			Toast.makeText(this, R.string.no_sd_card, Toast.LENGTH_LONG).show();
		}else{
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
				File sdcardDir =Environment.getExternalStorageDirectory();
				//�õ�һ��·����������sdcard���ļ���·��������
				mNewPath = sdcardDir.getPath()+"/laranImages/";//newPath�ڳ�����Ҫ����
				Log.i("info", "mNewPath:" + mNewPath);
				File path = new File(mNewPath);
				if (!path.exists()) {
					//�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
					path.mkdirs();
				}
			}
			else{
				Log.i("info", "createSDCardDir false");
			}
		}
	}
	
	//����ͼƬ
	public void saveMyBitmap(int percent) throws IOException {
	        
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmssS");   
	    String date = dateformat.format(new Date());
	    String bitName = date;
	    Log.i("info", "saveMyBitmap-->bitName:" + bitName);
		File f = new File(mNewPath + bitName + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		
		try {
			fOut = new FileOutputStream(f);        
		} catch (FileNotFoundException e) {        
			e.printStackTrace();
		}
		//ͼƬѹ��
		mBitmap.compress(Bitmap.CompressFormat.JPEG, percent, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finish();
	}

	private static Bitmap drawable2Bitmap(Drawable d){
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();
        Bitmap.Config config = d.getOpacity()!= PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width,height,config);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, width, height);
        d.draw(canvas);
        
        return bitmap;
	}
	
	private void cancelSave(){
		if(mSaveStep.getVisibility() == View.VISIBLE){
			mSaveStep.setVisibility(View.GONE);
		}
		
		if(mEditButton.getVisibility() == View.VISIBLE){
			mEditButton.setVisibility(View.GONE);
		}
		
		if(mSeekBar.getVisibility() == View.VISIBLE){
			mSeekBar.setVisibility(View.GONE);
		}
		
		if(mEffectButton.getVisibility() == View.VISIBLE){
			mEffectButton.setVisibility(View.GONE);
		}
		
		if(mFrameButton.getVisibility() == View.VISIBLE){
			mFrameButton.setVisibility(View.GONE);
		}
		
		if(mEffertButton.getVisibility() == View.GONE){
			mEffertButton.setVisibility(View.VISIBLE);			
		}
		
		mGetImage.setImageBitmap(mBitmap);
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mSaveStep.getVisibility() == View.VISIBLE){
				mSaveStep.setVisibility(View.GONE);
			}
			
			if(mEditButton.getVisibility() == View.VISIBLE){
				mEditButton.setVisibility(View.GONE);
			}
			
			if(mSeekBar.getVisibility() == View.VISIBLE){
				mSeekBar.setVisibility(View.GONE);
			}
			
			if(mEffectButton.getVisibility() == View.VISIBLE){
				mEffectButton.setVisibility(View.GONE);
			}
			
			if(mFrameButton.getVisibility() == View.VISIBLE){
				mFrameButton.setVisibility(View.GONE);
			}
			
			if(mEffertButton.getVisibility() == View.GONE){
				mEffertButton.setVisibility(View.VISIBLE);
				
				return true;
			}
		}
		
		return super.onKeyUp(keyCode, event);
	}
		
}