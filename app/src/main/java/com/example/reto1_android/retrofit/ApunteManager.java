package com.example.reto1_android.retrofit;

import com.example.reto1_android.model.ApunteAndroid;
import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntesBeans;

import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApunteManager {
    @POST("apunte")
    public Call<Void> create(@Body ApunteBean apunte) ;

    @PUT("apunte")
    public Call<ResponseBody> edit(@Body ApunteBean apunte) ;

    @DELETE("apunte/{id}")
    public Call<Void> remove(@Path("id") Integer id) ;

    @GET("apunte/{id}")
    public Call<ApunteBean> find(@Path("id") Integer id) ;

    @GET("apunte/")
    public Call<ApuntesBeans> findAll() ;
    @GET("apunte/creador/{id}")
    public Call<ApuntesBeans> getApuntesByCreador(@Path("id") Integer id) ;

    @GET("apunte/cliente/{id}")
    public Call<ApuntesBeans> getApuntesByComprador(@Path("id") Integer id) ;

    @PUT("apunte/votar/{idCliente}/{like}")
    public Call<Void> votacion(@Path("idCliente") Integer idCliente,@Path("like") Integer like,@Body ApunteBean apunte) ;

    @GET("apunte/cuantasCompras/{id}")
    public Call<Integer> cuantasCompras(@Path("id") Integer id) ;

    @GET("apunte/archivo/{id}")
    public Call<String> getArchivoDelApunte(@Path("id") Integer id);

    @POST("apunte/createApunteAndroid")
    public Call<Void> createApunteAndroid(@Body ApunteAndroid apunte);

}
