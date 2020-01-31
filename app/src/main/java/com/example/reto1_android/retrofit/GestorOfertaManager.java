package com.example.reto1_android.retrofit;

import com.example.reto1_android.model.OfertaBean;
import com.example.reto1_android.model.OfertaBeans;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GestorOfertaManager {
    @POST("oferta")
    public Call<Void> createOferta(@Body OfertaBean oferta);
    @GET("oferta/{idOferta}")
    public OfertaBean findOfertaById(@Path("idOferta") Integer idOferta);
    @DELETE("oferta/{idOferta}")
    public Call<Void> deleteOferta(@Path("idOferta") Integer idOferta);
    @GET("oferta")
    public Call<OfertaBeans> findAllOfertas();
    @PUT("oferta/actualizar")
    public Call<Void> updateOferta(OfertaBean oferta);
    @PUT("insertarPack/{idPack}")
    public void insertarPack(OfertaBean oferta,@Path("idPack") Integer idPack);
    @PUT("eliminarPack/{idPack}")
    public void eliminarPack(OfertaBean oferta,@Path("idPack") Integer idPack);
}

