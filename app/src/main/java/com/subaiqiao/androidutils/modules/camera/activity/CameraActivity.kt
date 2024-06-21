package com.subaiqiao.androidutils.modules.camera.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.subaiqiao.androidutils.R
import java.io.File

class CameraActivity : ComponentActivity() {

    lateinit var imageUri: Uri
    lateinit var outPutImage: File
    lateinit var activityCameraImageView: ImageView

    //实现回调函数，当相机界面被销毁之后，图片会缓存到指定的地点，然后到指定的路径中去获取图片和显示即可
    private val launcherCallback = ActivityResultCallback<ActivityResult> { result ->
        Log.d("result", "${result.data}")
        if (result.resultCode == Activity.RESULT_OK) {
            val bitMap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            activityCameraImageView.setImageBitmap(rotateIfRequired(bitMap))
        }
    }

    val intentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        launcherCallback
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_camera_constraint_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val activityCameraBtn: Button = findViewById(R.id.activity_camera_btn)
        activityCameraImageView = findViewById(R.id.activity_camera_image_view)
        activityCameraBtn.setOnClickListener {
            //创建一个file对象，指明照片的存储路径是缓存的目录和为拍下的照片取名为output_image.jpg
            outPutImage = File(externalCacheDir, "output_image.jpg")
            if (outPutImage.exists()) {
                outPutImage.delete()
            }
            outPutImage.createNewFile()
            //如果版本低于7，就用uri将file对象转化成uri对象，否则就封装成uri对象
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //FileProvider是一种特殊的ContentProvider
                FileProvider.getUriForFile(
                    this,
                    "com.subaiqiao.androidutils.fileprovider",
                    outPutImage
                )
            } else {
                Uri.fromFile(outPutImage)
            }
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            //调用指定了图片输出的地址，当相机被调用的时候，指定了拍照之后的存储地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            //startActivityForResult(intent,1)//这个方法过期了，，需要使用下面这个方法
            intentLauncher.launch(intent)
        }
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outPutImage.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }
}