package com.wy.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore;

public class MethodsCompat {
	
	@TargetApi(7)
    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
       	return MediaStore.Images.Thumbnails.getThumbnail(cr,origId,kind, options);
    }
      
}
