package com.aravind.seekhoapp.view

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.annotation.OptIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.databinding.ActivityAnimeDetailsBinding
import com.aravind.seekhoapp.helper.gone
import com.aravind.seekhoapp.helper.visible
import com.aravind.seekhoapp.model.AnimeModel
import com.aravind.seekhoapp.network.NetworkResult
import com.aravind.seekhoapp.view.adapter.GenreAdapter
import com.aravind.seekhoapp.view.adapter.MainCastAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient

class AnimeDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityAnimeDetailsBinding
    private var animeData = AnimeModel.Data()
    var ismute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentValues()
        initViews()
        initClickViews()
    }

    private fun getIntentValues() {
        if (intent.hasExtra("animeModel")){
            val gson = Gson()
            val  videos = intent.getStringExtra("animeModel").toString()
            animeData = gson.fromJson(videos,AnimeModel.Data ::class.java)
        }
    }

    private fun initClickViews() {
        binding.rltClose.setSafeOnClickListener { onBackPressed() }
    }

    private fun initViews() {
        binding.tvTitle.text = animeData.title
        binding.tvSynopsis.text = animeData.synopsis
        binding.tvEpisodes.text = animeData.episodes.toString()
        binding.tvRating.text = animeData.score.toString()
        Glide.with(binding.root.context).load(animeData.images.jpg.imageUrl).placeholder(R.drawable.ic_default)
            .error(R.drawable.ic_default).into(binding.ivBlurPostImage)
        Glide.with(binding.root.context).load(animeData.images.jpg.imageUrl).placeholder(R.drawable.ic_default)
            .error(R.drawable.ic_default).into(binding.ivPostImage)
        if (animeData.genres.size > 0)
            initGenreRecyclerView()
        else
            binding.lltGenre.gone()

        if (animeData.producers.size > 0)
            initMainCastRecyclerView()
        else
            binding.lltMainCast.gone()

        if (animeData.trailer.embedUrl.isNotEmpty())
            initVideoPlayer()
        else
            binding.lltTrailer.gone()
    }

    private fun initVideoPlayer() {
        val webSettings: WebSettings = binding.youtubeWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        binding.youtubeWebView.webViewClient = WebViewClient()

        val videoId = animeData.trailer.youtubeId
        val iframeHtml = """
            <html>
                <body style="margin:0; padding:0;">
                    <iframe 
                        width="100%"
                        height="100%" 
                        src="https://www.youtube.com/embed/$videoId?autoplay=1&rel=0" 
                        frameborder="0" 
                        allowfullscreen>
                    </iframe>
                </body>
            </html>
        """.trimIndent()

        binding.youtubeWebView.loadData(iframeHtml, "text/html", "utf-8")
    }


    private fun initGenreRecyclerView() {
        val genreAdapter = GenreAdapter(this, animeData.genres)
        binding.rvGenre.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvGenre.adapter = genreAdapter
    }

    private fun initMainCastRecyclerView() {
        val adapter = MainCastAdapter(this, animeData.producers)
        binding.rvMainCast.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvMainCast.adapter = adapter
    }

    override fun onSuccess(networkResult: NetworkResult<Any>) {
        
    }

    override fun onFailure(networkResult: NetworkResult<Any>) {
        
    }

    override fun onLoading(networkResult: NetworkResult<Any>) {
        
    }


}