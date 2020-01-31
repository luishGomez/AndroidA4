package com.example.reto1_android.retrofit;



import com.example.reto1_android.exceptions.WrongPasswordException;
import com.example.reto1_android.model.UserBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserManager {
    @POST
    public void createUser(UserBean usuario);
    @PUT
    public void updateUser(UserBean usuario);
    @GET("user/iniciarSesionAndroid/{login}/{contrasenia}")
    public Call<UserBean> iniciarSesion(@Path("login") String login, @Path("contrasenia") String contrasenia) throws WrongPasswordException;
}

