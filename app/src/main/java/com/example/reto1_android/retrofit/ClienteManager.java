package com.example.reto1_android.retrofit;

import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.ClienteBeans;
import com.example.reto1_android.model.ClientesBeans;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClienteManager {
    @POST("cliente")
    public Call<Void> create(@Body ClienteBean cliente);

    @PUT("cliente")
    public Call<Void> edit(@Body ClienteBean cliente);

    @DELETE
    public void remove(@Path("id") Integer id);

    @GET("cliente/{id}")
    public Call<ClienteBean> find(@Path("id") Integer id);

    @GET("cliente")
    public Call<Collection<ClienteBean>> findAll();

    @GET("cliente/votantes/{id}")
    public Call<ClienteBeans> getVotantesId(@Path("id") Integer id);

    @PUT("cliente/password")
    public Call<Void> actualizarContrasenia(@Body ClienteBean cliente);

    @POST("cliente/comprar/{idApunte}")
    public Call<Void> comprarApunte(@Body ClienteBean cliente, @Path("idApunte") Integer idApunte);

    @GET("cliente/passwordForgot/{login}")
    public Call<Boolean> passwordForgot(@Path("login") String login);

    @GET("cliente/iniciarSesionAndroid/{login}/{contrasenia}")
    public Call<ClienteBean> iniciarSesion(@Path("login") String login, @Path("contrasenia") String contrasenia);

    @GET("cliente/publicKey")
    public Call<String> getPublicKey();
}
