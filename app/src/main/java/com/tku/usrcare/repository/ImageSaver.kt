package com.tku.usrcare.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class ImageSaver {
    fun saveImageToInternalStorage(bitmap: Bitmap, context: Context, fileName: String) {
        try {
            // 開啟檔案輸出流
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fos ->
                // 壓縮並寫入 Bitmap 至檔案輸出流
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadImageFromInternalStorage(filename: String, context: Context): Bitmap? {
        return try {
            context.openFileInput(filename).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteImageFromInternalStorage(filename: String, context: Context) {
        context.deleteFile(filename)
    }

    fun clearAllImageFromInternalStorage(context: Context) {
        context.fileList().forEach {
            context.deleteFile(it)
        }
    }

    suspend fun loadImageFromWeb(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val newurl = URL(url)
                BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
