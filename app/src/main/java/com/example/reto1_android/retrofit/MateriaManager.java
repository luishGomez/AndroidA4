package com.example.reto1_android.retrofit;

import com.example.reto1_android.model.MateriaBean;
import com.example.reto1_android.model.MateriasBeans;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MateriaManager {
    @POST("materia")
    Call<Void> create(@Body MateriaBean materia);

    @PUT("materia")
    Call<Void> edit(@Body MateriaBean materia);

    @DELETE("materia/{id}")
    Call<Void> remove(@Path("id") Integer id);

    @GET("materia/{id}")
    Call<MateriaBean> find(@Path("id") Integer id);

    @GET("materia")
    Call<MateriasBeans> findAll();
}
