package com.example.reto1_android.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MateriaAPIClient {
    private static String API_BASE_URL = "http://10.0.2.2:8080/ServerApuntes4/webresources/";

    public static MateriaManager getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        MateriaManager MateriaManager = retrofit.create(MateriaManager.class);
        return MateriaManager;
    }
}
