package com.keju.baby;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.keju.baby.util.StringUtil;
/**
 * 异步加载图片
 * @author Zhoujun
 */
public class AsyncImageLoader {
	public static final String IMAGE_TYPE_OF_PNG = "png";
	public static final String IMAGE_TYPE_OF_JPEG = "jpg";
	
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	
	private static ExecutorService executorService; // 固定五个线程来执行任务
	private final Handler handler = new Handler();
	private static AsyncImageLoader instance;
	public static AsyncImageLoader getInstance() {
		if (instance == null) {
			instance = new AsyncImageLoader();
			executorService = Executors.newFixedThreadPool(5);
		}
		return instance;
	}
	
	public void clear(){
		for (Entry<String, SoftReference<Drawable>> entry : imageCache.entrySet()) {       
			Drawable drawable = entry.getValue().get();
			if(drawable!=null)
				drawable.setCallback(null);
		} 
		imageCache.clear();
		System.gc();
	}
	
	private List<String> imageUrlLoadTask = new ArrayList<String>();
	public boolean containsUrl(String url){
		return imageUrlLoadTask.contains(url);
	}
	
	public Drawable loadAsynLocalDrawable(final String imageUrl,final ImageCallback callback) {
		
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawable(imageUrl); 
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}
	/**
	 * 异步加载图片
	 * @param imageUrl
	 * @param callback
	 * @return
	 */
	public Drawable loadAsynSoftRefeDrawable(final Map<String, SoftReference<Drawable>> imageCache,int maxSize,final String imageUrl,final ImageCallback callback) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		if(imageCache.size() > maxSize){//每超过15张照片清空
			clear();
		}
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawable(imageUrl); 
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}
	
	public Drawable loadAsynDrawable(final Map<String, Drawable> imageCache,int maxSize,final String imageUrl,final ImageCallback callback) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			return imageCache.get(imageUrl);
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		if(imageCache.size() > maxSize){//每超过15张照片清空
			clear();
		}
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawable(imageUrl); 
					if(drawable != null){
						imageCache.put(imageUrl, drawable);
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}

	/**
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中

		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawable(imageUrl); 
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);

				}
			}
		});

		return null;
	}
	
	
	/**
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public Drawable loadDrawableAppointType(final String imageUrl,
			final ImageCallback callback, final String imgType) {
		// 如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		if(containsUrl(imageUrl))
			return null;
		imageUrlLoadTask.add(imageUrl);
		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中

		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = fetchDrawableAppointType(imageUrl, imgType); 
					if(drawable != null){
						imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					}
					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable,imageUrl);
							imageUrlLoadTask.remove(imageUrl);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);

				}
			}
		});

		return null;
	}

	//存放去远程图片的任务
	private static Map<String,String> fetchTaskMap = new HashMap<String, String>();
	public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()+ "/"+Constants.APP_DIR_NAME+"/");
	File file = new File(PHOTO_DIR, ".nomedia");
	public Drawable fetchDrawable(String imageUrl) {

		InputStream is = null;
		File localFile = null;
		try {
			Drawable drawable;
			if(!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();
			localFile  = new File(PHOTO_DIR,StringUtil.createImageName(imageUrl));
			if(!localFile.exists() || localFile.length() <= 0){
				if(!fetchTaskMap.containsKey(imageUrl)){
					fetchTaskMap.put(imageUrl, imageUrl);
					is = fetch(imageUrl);
					if(is==null)
						 throw new RuntimeException("stream is null");  
//					Bitmap bm = null;
//					byte[] data = readStream(is);  
//			        if(data!=null){  
//			        	bm = BitmapFactory.decodeByteArray(data, 0, data.length);  
//			        } 
			        Bitmap bm = BitmapFactory.decodeStream(is);
			        if(bm!=null){
			        	OutputStream outStream = new FileOutputStream(localFile,false);  
			            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);  
			            outStream.flush();  
			            outStream.close();
			        }
					is.close();
					if(bm!=null && !bm.isRecycled())
						bm.recycle();
					fetchTaskMap.remove(imageUrl);
				}
			}
//				localFile  = new File(recordBean.getPhotoUrl());
			if(localFile.length() <= 0){
				return null;
			}
			drawable = fetchLocal(localFile.getPath());
			return drawable;
		} catch (Exception e) {
			//出现异常则删除原始文件
			if(localFile!=null && localFile.exists()){
				localFile.delete();
			}
			Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		} finally{//关闭流
			try {
				//删除fetch任务
				if(fetchTaskMap.containsKey(imageUrl)){
					  fetchTaskMap.remove(imageUrl);
				}
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Drawable fetchDrawableAppointType(String imageUrl, String imgType) {

		InputStream is = null;
		File localFile = null;
		try {
			Drawable drawable;
			if(!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();	
			if(!file.exists())
				file.createNewFile();
			localFile  = new File(PHOTO_DIR,StringUtil.createImageName(imageUrl));
			if(!localFile.exists() || localFile.length() <= 0){
				if(!fetchTaskMap.containsKey(imageUrl)){
					fetchTaskMap.put(imageUrl, imageUrl);
					is = fetch(imageUrl);
					if(is==null)
						 throw new RuntimeException("stream is null");  
					Bitmap bm = null;
					byte[] data = readStream(is);  
			        if(data!=null){  
			        	bm = BitmapFactory.decodeByteArray(data, 0, data.length);  
			        } 
//			        bm = BitmapFactory.decodeStream(is);
			        if(bm!=null){
			        	OutputStream outStream = new FileOutputStream(localFile,false);  
						if(IMAGE_TYPE_OF_PNG.equals(imgType)){
							bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);  
						}
						if(IMAGE_TYPE_OF_JPEG.equals(imgType)){
							bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);  
						}
			            outStream.flush();  
			            outStream.close();
			        }
					is.close();
					if(bm!=null && !bm.isRecycled())
						bm.recycle();
					fetchTaskMap.remove(imageUrl);
				}
			}
//				localFile  = new File(recordBean.getPhotoUrl());
			if(localFile.length() <= 0){
				return null;
			}
			drawable = fetchLocal(localFile.getPath());
			return drawable;
		} catch (Exception e) {
			//出现异常则删除原始文件
			if(localFile!=null && localFile.exists()){
				localFile.delete();
			}
			Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
		} finally{//关闭流
			try {
				//删除fetch任务
				if(fetchTaskMap.containsKey(imageUrl)){
					  fetchTaskMap.remove(imageUrl);
				}
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 得到图片字节流 数组大小
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception{        
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();        
        byte[] buffer = new byte[1024];        
        int len = 0;        
        while( (len=inStream.read(buffer,0,1024)) != -1){        
            outStream.write(buffer, 0, len);        
        }    
        outStream.flush();
        outStream.close();        
        inStream.close();        
        return outStream.toByteArray();        
    }  
	/**
	 * 从网络获取图片
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();
	}
	/**
	 * 从本地获取图片
	 * @param urlString
	 * @return
	 */
	public Drawable fetchLocal(String urlString){
		try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(urlString, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, 512*512);
			//这里一定要将其设置回false，因为之前我们将其设置成了true     
			opts.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(urlString,opts);
			return new BitmapDrawable(bitmap); 
		}catch(Exception e){
			Log.e(this.getClass().getSimpleName(), "fetchLocal failed", e);
			return null;
		}
	}

	// 对外界开放的回调接口
	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Drawable imageDrawable,String imageUrl);
	}
	
	/**
	 * 计算缩放比例
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);

		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
				.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
