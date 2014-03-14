package com.example.mainactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.game.FirstGame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageFilterMain extends Activity {
	  private static final int IMAGE_SELECT = 1; // ѡ��ͼƬ  
	    private static final int IMAGE_CUT = 2; // �ü�ͼƬ  
	private CutView m_view;
	private Bitmap myBitmap,floorBitmap, surfaceBitmap;;
	Image image;
	Bitmap m_bmp;
	ShiJian sj;
	private ImageButton btnscreen;
	private DrawBS drawBS = null;
	private byte[] mContent;
	private ImageView imageView;
	ImageView imageView1;
	private TextView textView;
	private Button baocun;
	LinearLayout LinearLayout;
	MyView1 myView;
	
	private OnClickListener imgViewListener;
	public void getId(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageView1 = new ImageView(this);
		 m_view=new CutView(this);
		setContentView(R.layout.activity_zj);
		myView = new MyView1(getApplicationContext());	
		
		String name1 = null;
		sj=new ShiJian(name1);
		imageView= (ImageView) findViewById(R.id.imgfilter);
		
		textView = (TextView) findViewById(R.id.runtime);
		
		floorBitmap = Bitmap.createBitmap(480, 700, Bitmap.Config.ARGB_8888);	
		surfaceBitmap = Bitmap.createBitmap(480, 700, Bitmap.Config.ARGB_8888);
		Canvas surfaceCanvas = new Canvas(surfaceBitmap);
		btnscreen=(ImageButton)this.findViewById(R.id.btnscreen);
		  btnscreen.setImageResource(R.drawable.cut);
		btnscreen.setVisibility(View.GONE);
	
		btnscreen.setOnClickListener(new View.OnClickListener() 
        {                 
		@SuppressLint("SdCardPath")
		public void onClick(View v) {
			View vv = v.getRootView();
			//��������
			vv.setDrawingCacheEnabled(true);
			//ȡ��λͼ
			Bitmap bm = vv.getDrawingCache();
			//��imageView��ʾ�ղŽص�ͼ		
			//�洢·��
			/*
			 File file = new File("/sdcard/song/");
			if(!file.exists())
				file.mkdirs();
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.png");
					myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
					fileOutputStream.close();
					System.out.println("saveBmp is here");
				} catch (Exception e) {
							e.printStackTrace();
			}
		*/
			 File file = new File("mnt/sdcard/laranPicture/LaRan");
		
			 if(!file.exists())
				file.mkdirs();
                FileOutputStream out;
                try{
                        out = new FileOutputStream(file+"/"+sj.getName()+".jpg");
                       
                        Toast.makeText(ImageFilterMain.this, "��ѡ����Ҫ��ͼƬ����", Toast.LENGTH_SHORT).show(); 
                        if(bm.compress(Bitmap.CompressFormat.PNG, 70, out)) 
                        {
                        	Toast.makeText(ImageFilterMain.this, "����cut", Toast.LENGTH_SHORT).show(); 
                                out.flush();
                                out.close();
                        }
                        Intent intent = new Intent();
      	              //ָ��intentҪ��������
      	              intent.setClass(ImageFilterMain.this, MeinActivity.class);
      	              //����һ���µ�Activity
      	              startActivity(intent);
      	              //�رյ�ǰ��Activity
      	              ImageFilterMain.this.finish();
                } 
                catch (FileNotFoundException e) 
                {
                	Log.e("one",e.toString());
                        e.printStackTrace();
                } 
                catch (IOException e) 
                {
                	Log.e("two",e.toString());
                        e.printStackTrace(); 
                }
			
        }
        });    
	
		//ע����androidϵͳ�ϣ��ֻ�ͼƬ�ߴ羡��������480*480��Χ��,�����ڸ�˹����ʱ��������ڴ����������
		imgViewListener = new OnClickListener()
		{
			public void onClick ( View v )
			{
				final CharSequence[] items =
				{ "���", "����"};
				AlertDialog dlg = new AlertDialog.Builder(ImageFilterMain.this).setTitle("ѡ��ͼƬ").setItems(items,
						new DialogInterface.OnClickListener()
						{
							public void onClick ( DialogInterface dialog , int item )
							{
								// ����item�Ǹ���ѡ��ķ�ʽ��
								// ��items�������涨�������ַ�ʽ�����յ��±�Ϊ1���Ծ͵������շ���
								if (item == 1)
								{
									Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
									startActivityForResult(getImageByCamera, 1);
									btnscreen.setVisibility(View.VISIBLE);
								} else
								{
									Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
									getImage.addCategory(Intent.CATEGORY_OPENABLE);
									getImage.setType("image/jpeg");
									startActivityForResult(getImage, 0);
									btnscreen.setVisibility(View.VISIBLE);
								}
								if(item == 2){
									   Intent intent2 = getImageClipIntent();  
							            startActivityForResult(intent2, IMAGE_CUT);  
								}
							}
						}).create();
				dlg.show();
			}
		};
		// ��imageView�ؼ��󶨵���������
		imageView.setOnClickListener(imgViewListener);
			
	}
	
	
	public void SaveBitmap(byte[] mContent2)
	{
		
		//�洢·��
		File file = new File("/sdcard/song/");
		if(!file.exists())
			file.mkdirs();
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.png");
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
				fileOutputStream.close();
				System.out.println("saveBmp is here");
			} catch (Exception e) {
						e.printStackTrace();
		}
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
					String mNewPath = sdcardDir.getPath()+"/laranImages/";//newPath�ڳ�����Ҫ����
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
		public void setM(CutView cut){
			View view = null;
			LayoutParams params = null;
			this.addContentView(view, params);
		}
		public void SaveBitmap(Bitmap bit)
		{
			
			//�洢·��
			File file = new File("/sdcard/song/");
			if(!file.exists())
				file.mkdirs();
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.png");
					myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
					fileOutputStream.close();
					System.out.println("saveBmp is here");
				} catch (Exception e) {
							e.printStackTrace();
			}
		}
		//����ͼƬ
		@SuppressWarnings("null")
		public void saveMyBitmap(int percent) throws IOException {
			
			
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmssS");   
		    String date = dateformat.format(new Date());
		    String bitName = date;
		    Log.i("info", "saveMyBitmap-->bitName:" + bitName);
			String mNewPath = null;
			
			File file = new File("/sdcard/laranImages/");
		
		
			FileOutputStream fOut = null;
			
			try {
				fOut = new FileOutputStream(file+mNewPath + bitName + ".png");        
			} catch (FileNotFoundException e) {        
				e.printStackTrace();
			}
			Bitmap mBitmap = null;
			
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
		@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        // TODO Auto-generated method stub
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            //do something
	        	  Intent intent = new Intent();
	              //ָ��intentҪ��������
	              intent.setClass(ImageFilterMain.this, FirstGame.class);
	              //����һ���µ�Activity
	              startActivity(intent);
	              //�رյ�ǰ��Activity
	              ImageFilterMain.this.finish();
	            return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }
	@ Override
	protected void onActivityResult ( int requestCode , int resultCode , Intent data )
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		ContentResolver resolver = getContentResolver();
		/**
		 * ��Ϊ���ַ�ʽ���õ���startActivityForResult������
		 * �������ִ����󶼻�ִ��onActivityResult������ ����Ϊ�����𵽵�ѡ�����Ǹ���ʽ��ȡͼƬҪ�����жϣ�
		 * �����requestCode��startActivityForResult����ڶ���������Ӧ
		 */
		LoadImageFilter();
		if(requestCode == IMAGE_CUT){
			 LoadImageFilter();
			 // һ��Ҫ��"return-data"���صı�ǩ"data"һ��  
            Bitmap bmp = data.getParcelableExtra("data");   
           
            imageView.setImageBitmap(bmp);  
        
		}
		if (requestCode == 0)
		{
			try
			{
				// ���ͼƬ��uri
				Uri originalUri = data.getData();
				
				// ��ͼƬ���ݽ������ֽ�����
				mContent = readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
				// ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����
				myBitmap = getPicFromBytes(mContent, null);
				// //�ѵõ���ͼƬ���ڿؼ�����ʾ
				imageView.setImageBitmap(myBitmap);
				LoadImageFilter();
				
			} catch ( Exception e )
			{
				System.out.println(e.getMessage());
			}

		} else if (requestCode == 1)
		{
			try
			{
				super.onActivityResult(requestCode, resultCode, data);
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				mContent = baos.toByteArray();
			} catch ( Exception e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// �ѵõ���ͼƬ���ڿؼ�����ʾ
			imageView.setImageBitmap(myBitmap);
			LoadImageFilter();
		  
		}
	}
	  private Intent getImageClipIntent() {  
	        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
	        // ʵ�ֶ�ͼƬ�Ĳü�,����Ҫ����ͼƬ�����Ժʹ�С  
	        intent.setType("image/*"); // �������ԣ���ʾ��ȡ�������͵�ͼƬ  
	        intent.putExtra("crop", "true");// ���ÿ��Ի���ѡѡ�����������,ע���������ַ���"true"  
	        intent.putExtra("aspectX", 1);// ���ü��п�1:1������Ч��  
	        intent.putExtra("aspectY", 1);  
	        intent.putExtra("outputX", 128);  
	        intent.putExtra("outputY", 128);  
	        intent.putExtra("return-data", true);  
	        return intent;  
	    }  
	public static Bitmap getPicFromBytes ( byte[] bytes , BitmapFactory.Options opts )
	{
		opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds=true;
		Bitmap bit=	BitmapFactory.decodeByteArray(bytes,0, bytes.length, opts);
		opts.inJustDecodeBounds=false;
		int be=(int)(opts.outHeight/(float)200);
		 
		if(be<=0)
			be=1;
		 opts.inSampleSize = be; 
		if (bytes != null)
			if (opts != null)
			
				return BitmapFactory.decodeByteArray(bytes,0, bytes.length, opts);
			else
				
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream ( InputStream inStream ) throws Exception
	{
		byte[] buffer = new byte[1024*3000];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	 @SuppressWarnings("unused")
	private Bitmap getBitmapFromView(View view, int width, int height)  
	    {  
	                int widthSpec = View.MeasureSpec.makeMeasureSpec(width,  
	                    View.MeasureSpec.EXACTLY);  
	                int heightSpec = View.MeasureSpec.makeMeasureSpec(height,  
	                    View.MeasureSpec.EXACTLY);  
	                view.measure(widthSpec, heightSpec);  
	                view.layout(0, 0, width, height);  
	        Bitmap bitmap = Bitmap.createBitmap(width, height,  
	            Bitmap.Config.ARGB_8888);  
	        Canvas canvas = new Canvas(bitmap);  
	        canvas.drawColor(Color.TRANSPARENT);  
	      
	        view.draw(canvas);  
	  
	        return bitmap;  
	    }  
	/**
	 * ����ͼƬfilter
	 */
	private void LoadImageFilter() {
		Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
		final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(
				ImageFilterMain.this);
		gallery.setAdapter(new ImageFilterAdapter(ImageFilterMain.this));
		gallery.setSelection(2);
		gallery.setAnimationDuration(3000);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
				new processImageTask(ImageFilterMain.this, filter).execute();
				System.out.print(AdapterView.INVALID_POSITION);
			}
			
		});
		
	}

	
	public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
		private IImageFilter filter;
        private Activity activity = null;
		public processImageTask(Activity activity, IImageFilter imageFilter) {
			this.filter = imageFilter;
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			textView.setVisibility(View.VISIBLE);
					
		}

		public Bitmap doInBackground(Void... params) {
			Image img = null;
			Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds=true;
			Bitmap bit=	BitmapFactory.decodeByteArray(mContent, 0, 0, opts);
			opts.inJustDecodeBounds=false;
			int be=(int)(opts.outHeight/(float)200);
			if(be<=0)
				be=1;
			try
	    	{
				Bitmap bitmap = getPicFromBytes(mContent, opts);
				img = new Image(bitmap);
				
				if (filter != null) {
					img = filter.process(img);
					img.copyPixelsFromBuffer();
				}
				
				return img.getImage();
				
	    	}
			catch(Exception e){
				if (img != null && img.destImage.isRecycled()) {
					img.destImage.recycle();
					img.destImage = null;
					System.gc(); // ����ϵͳ��ʱ����
				}
			}
			finally{
				
				if (img != null && img.image.isRecycled()) {
					
					img.image.recycle();
					img.image = null;
					
					System.gc(); // ����ϵͳ��ʱ����
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null){
				super.onPostExecute(result);
				imageView.setImageBitmap(result);	
				
			}	
			textView.setVisibility(View.GONE);			
		}
		
	}

	public class ImageFilterAdapter extends BaseAdapter {
		public ImageFilterAdapter(){}
		public class FilterInfo {
			public int filterID;
			public IImageFilter filter;

			public FilterInfo(int filterID, IImageFilter filter) {
				this.filterID = filterID;
				this.filter = filter;
			}
		}

		private Context mContext;
		private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
		

		public ImageFilterAdapter(Context c) {
			mContext = c;
			
			//99��Ч��
			filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
	        //v0.4 
			filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
			filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte)1)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte)2)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter(ImageFilterMain.this, R.drawable.texture1)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter(ImageFilterMain.this, R.drawable.texture2)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter, new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));
			
			//v0.3  
			filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
			filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
			filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
			filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
			filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
			filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
			filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
		
			filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
			filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF,20,100)));
			filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
			filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
			filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
			filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));
			
			//v0.2
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));
			
			//v0.1
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
			filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
			filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
			filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
			filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
			filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
			filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
			filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
			filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.threshold_filter,	new ThresholdFilter()));
			filterArray.add(new FilterInfo(R.drawable.noisefilter,	new NoiseFilter()));
			filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
			filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
			filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
		//	filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
			filterArray.add(new FilterInfo(R.drawable.brick_filter,	new BrickFilter()));
			filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,	new GaussianBlurFilter()));
			filterArray.add(new FilterInfo(R.drawable.light_filter,	new LightFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MistFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MosaicFilter()));
			filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,	new OilPaintFilter()));
			filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,new RadialDistortionFilter()));
			filterArray.add(new FilterInfo(R.drawable.reflection1_filter,new ReflectionFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.reflection2_filter,new ReflectionFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,new SmashColorFilter()));
			filterArray.add(new FilterInfo(R.drawable.tint_filter,	new TintFilter()));
			filterArray.add(new FilterInfo(R.drawable.vignette_filter,	new VignetteFilter()));
			filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,new AutoAdjustFilter()));
			filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,	new ColorQuantizeFilter()));
			filterArray.add(new FilterInfo(R.drawable.waterwave_filter,	new WaterWaveFilter()));
			filterArray.add(new FilterInfo(R.drawable.vintage_filter,new VintageFilter()));
			filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,new OldPhotoFilter()));
			filterArray.add(new FilterInfo(R.drawable.sepia_filter,	new SepiaFilter()));
			filterArray.add(new FilterInfo(R.drawable.rainbow_filter,new RainBowFilter()));
			filterArray.add(new FilterInfo(R.drawable.feather_filter,new FeatherFilter()));
			filterArray.add(new FilterInfo(R.drawable.xradiation_filter,new XRadiationFilter()));
			filterArray.add(new FilterInfo(R.drawable.nightvision_filter,new NightVisionFilter()));

			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,null/* �˴�������ԭͼЧ�� */));
			
		}

		public int getCount() {
			return filterArray.size();
		}

		public Object getItem(int position) {
			return position < filterArray.size() ? filterArray.get(position).filter
					: null;
		}

		public long getItemId(int position) {
			return position;
		}
   
		public View getView(int position, View convertView, ViewGroup parent) {
			Bitmap bmImg = BitmapFactory
					.decodeResource(mContext.getResources(),
							filterArray.get(position).filterID);
			int width = 100;// bmImg.getWidth();
			int height = 100;// bmImg.getHeight();
			bmImg.recycle();
			ImageView imageview = new ImageView(mContext);
			imageview.setImageResource(filterArray.get(position).filterID);
			imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
			imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);// ������ʾ��������
			
			return imageview;
			
		}
	};
	

}
