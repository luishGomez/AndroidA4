package com.example.reto1_android.retrofit;

import com.example.reto1_android.model.Oferta;
import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.PackBean;
import com.example.reto1_android.model.PacksBeans;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PackManager {
    @POST("pack")
    Call<Void> create(@Body PackBean pack);

    @PUT("pack")
    Call<Void> edit(@Body PackBean pack);

    @DELETE("pack/{id}")
    Call<Void> remove(@Path("id") Integer id);

    @GET("pack/{id}")
    Call<PackBean> find(@Path("id") Integer id);

    @GET("pack")
    Call<PacksBeans> findAll();

    @PUT("pack/insertarApunte/{id}")
    Call<Void> insertarApunte(PackBean pack, @Path("id") Integer id);

    @PUT("pack/eliminarApunte/{id}")
    Call<Void> eliminarApunte(PackBean pack, @Path("id") Integer id);

    @GET("oferta/{id}")
    Call<OfertaBean> getOferta(@Path("id") Integer id);
}
