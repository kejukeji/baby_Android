package com.keju.baby.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 图片处理工具类
 * @author Zhoujun
 *
 */
public class ImageUtil {
	
	/**
	 * drawable 转换成 bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable){
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}
	
	/**
	 * 图片加上圆角效果
	 * @param drawable 需要处理的图片
	 * @param percent 圆角比例大小
	 * @return
	 */
	public static Bitmap getRoundCornerBitmapWithPic(Drawable drawable, float percent){
		Bitmap bitmap = drawableToBitmap(drawable);
		return getRoundedCornerBitmapWithPic(bitmap, percent);
	}
	
	/**
	 * 图片加上圆角效果
	 * @param bitmap 要处理的位图
	 * @param roundPx 圆角大小
	 * @return 返回处理后的位图
	 */
	public static Bitmap getRoundedCornerBitmapWithPic(Bitmap bitmap, float percent) { 
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	    		bitmap.getHeight(), Config.ARGB_8888);    
	    Canvas canvas = new Canvas(output);    
	     
	    final int color = 0xff424242;    
	    final Paint paint = new Paint();    
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());    
	    final RectF rectF = new RectF(rect);    
	     
	    paint.setAntiAlias(true);    
	    canvas.drawARGB(0, 0, 0, 0);    
	    paint.setColor(color);    
	    canvas.drawRoundRect(rectF, bitmap.getWidth()*percent, bitmap.getHeight()*percent, paint);    
	     
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));    
	    canvas.drawBitmap(bitmap, rect, rect, paint);    
	     
	    return output;    
	  }
}
