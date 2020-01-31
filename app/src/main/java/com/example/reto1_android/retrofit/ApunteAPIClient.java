package com.example.reto1_android.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApunteAPIClient {
    private static String API_BASE_URL = "http://10.0.2.2:8080/ServerApuntes4/webresources/";

    public static ApunteManager getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //SimpleXmlConverterFactory.create()
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        ApunteManager ApunteManager = retrofit.create(ApunteManager.class);
        return ApunteManager;
    }
    public static ApunteManager getClientText(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //SimpleXmlConverterFactory.create()
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        ApunteManager apunteManager = retrofit.create(ApunteManager.class);
        return apunteManager;
    }
}
