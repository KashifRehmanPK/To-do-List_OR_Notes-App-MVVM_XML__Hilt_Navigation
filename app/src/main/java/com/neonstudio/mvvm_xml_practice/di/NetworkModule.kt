package com.neonstudio.mvvm_xml_practice.di

import com.neonstudio.mvvm_xml_practice.api.AuthInterceptor
import com.neonstudio.mvvm_xml_practice.api.NotesAPI
import com.neonstudio.mvvm_xml_practice.api.UserAPI
import com.neonstudio.mvvm_xml_practice.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }



    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }


    @Singleton
    @Provides
    fun providesIOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }


    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Builder,okHttpClient: OkHttpClient): NotesAPI{
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NotesAPI::class.java)
    }





}