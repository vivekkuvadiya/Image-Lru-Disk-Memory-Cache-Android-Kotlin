package com.example.imagelrucache.cache.lrudisk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import com.example.imagelrucache.cache.lrudisk.model.CacheImage
import com.example.imagelrucache.utils.Constant
import com.example.imagelrucache.utils.decodeFromBase64
import com.example.imagelrucache.utils.encodeToBase64
import kotlinx.coroutines.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class LruDiskCache(private val context: Context) :
    LinkedHashMap<String, CacheImage>(Constant.LRU_CACHE_CAPACITY, 0.75f, true) {

    init {
        loadIntoMemory()
    }

    fun putImage(key: String, value: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(getCacheDirectory(), formatKey(key))
            put(key, CacheImage(isDiskCache = false,file = file, bitmap = value, time = 0L))
            putBitmapIntoCache(file, value)
        }
    }

    fun clearCache(){
        deleteDirectory(getCacheDirectory())
        clear()
    }

    suspend fun getAllImages(): List<Bitmap> {
        if (size < 1)
            return emptyList()
        return withContext(Dispatchers.Main) {
            async { getAllBitmap() }.await()
        }
    }

    private suspend fun getAllBitmap(): List<Bitmap> = withContext(Dispatchers.IO) {
        val allCacheImages = mutableListOf<Bitmap>()
        entries.reversed().forEach {
            if (it.value.file.exists()){
                allCacheImages.add(BitmapFactory.decodeFile(it.value.file.path))
            }
        }
        allCacheImages
    }

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CacheImage>?): Boolean {
        return if (size > Constant.LRU_CACHE_CAPACITY) {
            //Remove from
            eldest?.value?.file?.let {
                removeFromDisk(it)
            }
            true
        } else {
            false
        }
    }

    private fun removeFromDisk(file: File){
        deleteDirectory(file)
    }

    private suspend fun putBitmapIntoCache(file: File, bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        }
    }

    private fun formatKey(str: String?): String {
        val formatted = "${System.currentTimeMillis()}_${str}"
        return formatted.encodeToBase64()
    }

    private fun loadIntoMap(): Map<String, CacheFile> {
        val map = HashMap<String, CacheFile>()
        val cacheDirectory = getCacheDirectory()
        if (cacheDirectory.exists() && cacheDirectory.length() > 0) {
            cacheDirectory.listFiles()?.let {
                for (file in it) {
                    val decodeName = file.name.decodeFromBase64().split("_")
                    map[decodeName[1]] = CacheFile(file, decodeName[0].toLong())
                }
            }
        }
        return map.toList().sortedBy { (_, value) -> value.time }.toMap()
    }

    data class CacheFile(val file: File, val time: Long)

    private fun loadIntoMemory() {
        val loadIntoMap = loadIntoMap()
        if (loadIntoMap.isEmpty()) {
            return
        }

        loadIntoMap.forEach {
            put(
                it.key, CacheImage(
                    isDiskCache = true,
                    file = it.value.file,
                    time = it.value.time,
                )
            )
        }

    }

    private fun deleteDirectory(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (i in children.indices) {
                    val success = deleteDirectory(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    private fun getCacheDirectory(): File {
        val cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !isExternalStorageRemovable()
            ) {
                context.externalCacheDir?.path
            } else {
                context.cacheDir.path
            }
        val file = File(cachePath + File.separator + "DogImageCache")
        if (!file.exists())
            file.mkdirs()
        return file
    }
}