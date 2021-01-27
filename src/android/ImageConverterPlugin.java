package com.stc.ImageConverterPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Base64;
import android.util.Log;


public class ImageConverterPlugin extends CordovaPlugin {
    private CallbackContext cbCtx = null;
    private CordovaActivity act = null;

	@Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
		if(action.equals("getBase64Image")){
            cbCtx = callbackContext;
            act = (CordovaActivity) this.cordova.getActivity();

            String imagePath = (String) args.get(0);
			Context ctx = act.getApplicationContext();

            File file = new File(ctx.getExternalCacheDir(),imagePath.substring(imagePath.lastIndexOf("/")));
        			
			Log.d("File Path","File Path: " + file.getPath());

            try { 	
	            // Reading a Image file from file system
	            FileInputStream imageInFile = new FileInputStream(file);
	            byte[] imageData = new byte[(int) file.length()];
	            imageInFile.read(imageData);
	 
	            // Converting Image byte array into Base64 String
	            //String imageDataString = "data:image/png;base64," + encodeImage(scale(imageData,1,1));
                String imageDataString = "data:image/png;base64," + encodeImage(imageData);
	 
	            imageInFile.close();
	            
	            cbCtx.success(imageDataString);
	 
	            System.out.println("Image Successfully Manipulated!");
	            
	        } catch (FileNotFoundException e) {
	        	cbCtx.error("File not found!");
	        	
	            System.out.println("Image not found" + e);
	        } catch (IOException ioe) {
	        	cbCtx.error("IO Exception!");
	        	
	            System.out.println("Exception while reading the Image " + ioe);
	        }
			
			//callbackContext.success(scale(file,10,10));
			
			/*
        	ArrayList<File> fList = new ArrayList<File>();
        	
        	File[] files = file.listFiles();
        	Log.d("Total Files","Total Files: " + files.length);
        	for(File f : files){
        		if(f.isFile()){
        			fList.add(f);
        			Log.d("File","File: " + f.getPath());
        		}
        		else{
        			Log.d("Directory","Directory: " + f.getPath());
        		}
        	}
			
			*/	        
		}
		
		return false;
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String encodeImage(byte[] imageByteArray) {
        return new String(Base64.encode(imageByteArray, Base64.DEFAULT));
    }
	
	public byte[] scale(byte[] fileData, int width, int height){
		Bitmap bmp = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth,scaleHeight);
		Bitmap scaledImage = Bitmap.createBitmap(bmp,0,0,w,h,matrix,false);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	scaledImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    	byte[] scaledData = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return scaledData;
	}
	
	public byte[] scale2(byte[] fileData, int width, int height) {
    	try {
    		Bitmap bmp = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
        	Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, width, height, false);
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        	scaledBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        	byte[] scaledData = stream.toByteArray();
			stream.close();
			
	    	return scaledData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Resize Error","Resize Exception Has Occurred!!!");
	    	return fileData;
		}
    }
	
	public String scaleTheImage(File image, int width, int height){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(image.getPath(), options);
		
		Bitmap scaledBmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	scaledBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	scaledBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    	byte[] scaledData = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return encodeImage(scaledData);
	}
	
	public static String resizeBitMapImage1(String filePath, int targetWidth, int targetHeight) {
	    Bitmap bitMapImage = null;
	    try {
	        Options options = new Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);
	        double sampleSize = 0;
	        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth
	                - targetWidth);
	        if (options.outHeight * options.outWidth * 2 >= 1638) {
	            sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
	            sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
	        }
	        options.inJustDecodeBounds = false;
	        options.inTempStorage = new byte[128];
	        while (true) {
	            try {
	                options.inSampleSize = (int) sampleSize;
	                bitMapImage = BitmapFactory.decodeFile(filePath, options);
	                break;
	            } catch (Exception ex) {
	                try {
	                    sampleSize = sampleSize * 2;
	                } catch (Exception ex1) {

	                }
	            }
	        }
	    } catch (Exception ex) {

	    }
	    
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitMapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
    	byte[] scaledData = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return encodeImage(scaledData);
	}
}
