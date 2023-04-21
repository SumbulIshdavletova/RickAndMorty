package ru.sumbul.rickandmorty.characterDetails.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CharacterDetailClient {

//    companion object {
//        private const val BASE_URL = "https://rickandmortyapi.com/api/"
//    }
//    private var instance: CharacterDetailClient? = characterDetailClient()
//    private var characterDetailsService: CharacterDetailsService? = null
//    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//        .create()
//
//
//    private fun characterDetailClient() : CharacterDetailsService() {
//
//        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//        return characterDetailsService = retrofit.create(CharacterDetailsService::class.java)
//    }
//
//    fun getInstance(): GitHubClient? {
//        if (instance == null) {
//            instance = GitHubClient()
//        }
//        return instance
//    }
//
//
//    public Observable<List<GitHubRepo>> getStarredRepos(@NonNull String userName) {
//        return gitHubService.getStarredRepositories(userName);
//    }
//}
}