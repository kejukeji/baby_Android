package com.keju.baby.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import android.content.Context;

/**
 * 文件处理工具类
 * @author Zhoujun
 * 
 */
public class FileUtil {
	/**
	 * 读取assets下的文本数据
	 * @param fileName
	 * @return
	 */
	public static String getStringFromAssets(Context context,String fileName){ 
        try { 
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
	}
	
	/**
	 * 读取文本流文件
	 * @param is
	 * @return
	 */
	public static String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			return "";
		}
	}
	
	/**
	 * 获得文件大小 
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static long getFileSize(File f) throws IOException{
		long s=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s= fis.available();
        } 
        return s;
	}
	/**
	 * 格式化文件大小
	 * @param fileS
	 * @return
	 * @throws IOException 
	 */
	public static String formatFileSize(File f) {//转换文件大小
		String fileSizeString = "";
		try{
			long fileS = getFileSize(f);
	        DecimalFormat df = new DecimalFormat("#.00");
	        if (fileS < 1024) {
	            fileSizeString = df.format((double) fileS) + "b";
	        } else if (fileS < 1048576) {
	            fileSizeString = df.format((double) fileS / 1024) + "kb";
	        } else if (fileS < 1073741824) {
	            fileSizeString = df.format((double) fileS / 1048576) + "mb";
	        } else {
	            fileSizeString = df.format((double) fileS / 1073741824) + "gb";
	        }
		}catch(Exception e){
		}
        return fileSizeString;
    }
	
	/**
	 * 拷贝资源文件到sd卡
	 * @param context
	 * @param resId
	 * @param databaseFilename  如数据库文件拷贝到sd卡中
	 */
	public static void copyResToSdcard(Context context, int resId,
			String databaseFilename) {// name为sd卡下制定的路径
		try {
			// 不存在得到数据库输入流对象
			InputStream is = context.getResources().openRawResource(resId);
			// 创建输出流
			FileOutputStream fos = new FileOutputStream(databaseFilename);
			// 将数据输出
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			// 关闭资源
			fos.close();
			is.close();
		} catch (Exception e) {
		}
	}
}
