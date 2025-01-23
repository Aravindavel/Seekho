package com.aravind.seekhoapp.interfaces



import com.aravind.seekhoapp.model.AnimeModel
import com.aravind.seekhoapp.model.PreferenceModel
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface ApiService {

    @GET("top/anime")
    suspend fun anime(@QueryMap hashMap: HashMap<String, String>): Response<AnimeModel>

    @GET("anime")
    suspend fun animeDetails(@QueryMap hashMap: HashMap<String, String>): Response<AnimeModel>
}