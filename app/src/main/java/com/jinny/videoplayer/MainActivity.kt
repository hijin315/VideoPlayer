package com.jinny.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinny.videoplayer.dto.VideoDto
import com.jinny.videoplayer.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var videoAdapter: VideoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()       // 프래그먼트 붙이기
        videoAdapter = VideoAdapter()
        findViewById<RecyclerView>(R.id.main_recyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

        getVideoList()
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder().baseUrl("https://run.mocky.io/").addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        retrofit.create(VideoService::class.java).also {
            it.listVideos().enqueue(object : Callback<VideoDto> {
                override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                    //실패일 경우
                    Log.d("MainActivity", "" + t.message)
                }

                override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                    if (response.isSuccessful.not()) {
                        Log.d("MainActivity", "response Fail")
                        return
                    }
                    response.body()?.let {
                        videoAdapter.submitList(it.videos)
                    }
                }
            })
        }
    }
}