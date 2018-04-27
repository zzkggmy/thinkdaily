package com.byren.kai.thinkdaily.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore

object pickPic {
    fun getPhotoSelectItem(uri: Uri) : Intent{
        val takeIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takeIntent.addCategory(Intent.CATEGORY_DEFAULT)
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
        return takeIntent
    }
    fun cropPhoto(inputUri: Uri,outputUri: Uri) : Intent{
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(inputUri,"image/*")
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 200);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        return intent
    }
}