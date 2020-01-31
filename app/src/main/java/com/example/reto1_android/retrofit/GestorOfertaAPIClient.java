package com.example.reto1_android.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class GestorOfertaAPIClient {
    private static String API_BASE_URL = "http://10.0.2.2:8080/ServerApuntes4/webresources/";

    public static GestorOfertaManager getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        GestorOfertaManager gestorOfertaManager = retrofit.create(GestorOfertaManager.class);
        return gestorOfertaManager;
    }
}
