package com.aravind.seekhoapp.view


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.databinding.ActivityMainBinding
import com.aravind.seekhoapp.helper.Enums.REQ_ANIME
import com.aravind.seekhoapp.helper.PaginationLinearLayoutScrollListener
import com.aravind.seekhoapp.helper.convertAs
import com.aravind.seekhoapp.helper.gone
import com.aravind.seekhoapp.helper.visible
import com.aravind.seekhoapp.model.AnimeModel
import com.aravind.seekhoapp.network.NetworkResult
import com.aravind.seekhoapp.view.adapter.AnimeAdapter
import com.google.gson.Gson

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0
    var animeModel = AnimeModel()
    private var animeListModel = AnimeModel()
    lateinit var animeAdapter: AnimeAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private var isLoading = false
    private var isLastPage = false
    private var totalPages = 0
    private var currentPage = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClickView()
        callAnimesApi()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initClickView() {

    }

    private fun callAnimesApi() {
        if (currentPage == 1)
            commonMethods.showProgressDialog(this)
        commonViewModel?.apiRequest(REQ_ANIME,animeHashmap(),null)
    }

    private fun animeHashmap() = HashMap<String, String>().apply {
        put("page", currentPage.toString())
    }


    private fun initAnimeRecyclerview() {
        if (currentPage == 1) {
            animeListModel = animeModel
            linearLayoutManager = LinearLayoutManager(this).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            binding.rvAnime.layoutManager = linearLayoutManager

            animeAdapter = AnimeAdapter(this, animeListModel.data).apply {
                onAnimeClick = {
                    val gson = Gson()
                    val animeString = gson.toJson(animeListModel.data[it])
                    startActivity(Intent(this@MainActivity, AnimeDetailsActivity::class.java)
                        .putExtra("animeModel",animeString),
                        ActivityOptions.makeCustomAnimation(this@MainActivity, R.anim.slide_in, R.anim.slide_out).toBundle())
                }
            }

            binding.rvAnime.addOnScrollListener(object :
                PaginationLinearLayoutScrollListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    if (animeModel.pagination.lastVisiblePage > 1 && currentPage != totalPages) {
                        isLoading = true
                        currentPage += 1
                        animeAdapter.showLoading()
                        callAnimesApi()
                    }
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
            binding.rvAnime.adapter = animeAdapter
        } else {
            val oldSize = animeListModel.data.size
            animeAdapter.hideLoading()
            animeListModel.data.addAll(animeModel.data)
            animeAdapter.notifyItemRangeInserted(oldSize, animeModel.data.size)
        }
    }

    override fun onSuccess(networkResult: NetworkResult<Any>) {
        when(networkResult.requestCode) {
            REQ_ANIME -> {
                commonMethods.hideProgressDialog()
                animeModel = (networkResult.data!! convertAs AnimeModel::class.java)
                if (animeModel.data.size > 0) {
                    totalPages = animeModel.pagination.lastVisiblePage
                    binding.rvAnime.visible()
                    binding.lltNoData.gone()
                    initAnimeRecyclerview()
                    isLoading = false
                }else{
                    binding.rvAnime.gone()
                    binding.lltNoData.visible()
                }
            }
        }
    }


    override fun onFailure(networkResult: NetworkResult<Any>) {
        commonMethods.hideProgressDialog()
        commonMethods.showToast(this,resources.getString(R.string.please_try_again))
    }

    override fun onLoading(networkResult: NetworkResult<Any>) {

    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (backPressed >= 1) {
            finishAffinity()
            super.onBackPressed()
        } else {
            backPressed += 1
            commonMethods.showToast(this, getString(R.string.press_back_again))
        }
    }
}