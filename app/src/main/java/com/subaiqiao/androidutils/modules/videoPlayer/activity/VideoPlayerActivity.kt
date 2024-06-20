package com.subaiqiao.androidutils.modules.videoPlayer.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.subaiqiao.androidutils.R

class VideoPlayerActivity : ComponentActivity() {

    private lateinit var videoPlayer: StandardGSYVideoPlayer;

    private lateinit var orientationUtils: OrientationUtils;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_video_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    fun init() {
        videoPlayer = findViewById (R.id.video_player)
//        val source = "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear3/prog_index.m3u8"
        val source = "https://vip.ffzy-online.com/20221029/5952_80bc1f98/2000k/hls/mixed.m3u8"
        videoPlayer.setUp(source, true, "测试视频");
        val imageView = ImageView(this)
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        imageView.setImageResource(R.mipmap.xxx1)
        videoPlayer.thumbImageView = imageView
        //增加title
        videoPlayer.titleTextView.visibility = View.VISIBLE
        //设置返回键
        videoPlayer.backButton.setVisibility(View.VISIBLE)
        val orientationUtils = OrientationUtils(this, videoPlayer)
        videoPlayer.fullscreenButton.setOnClickListener {
            orientationUtils.resolveByClick();
        }
        videoPlayer.setIsTouchWiget(true)
        videoPlayer.backButton.setOnClickListener {
            onBackPressed()
        }

        videoPlayer.isNeedOrientationUtils = false
        videoPlayer.startPlayLogic()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos();
        orientationUtils.releaseListener();
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }
}