package com.example.reto1_android.servicios;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.reto1_android.model.ApunteBean;
import com.example.reto1_android.model.ApuntesBeans;
import com.example.reto1_android.retrofit.ApunteAPIClient;
import com.example.reto1_android.retrofit.ApunteManager;

import retrofit2.Call;
import retrofit2.Response;

public class BackServiceAllApuntes extends IntentService {
    private ApunteBean[] apuntes;
    public BackServiceAllApuntes(String name) {
        super("BackServiceAllApuntes");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            ApunteManager apunteManager = ApunteAPIClient.getClient();
            Call<ApuntesBeans> call = apunteManager.findAll();
            Response<ApuntesBeans> result = call.execute();
            apuntes = new ApunteBean[result.body().getApuntes().size()];
            result.body().getApuntes().toArray(apuntes);
            Intent resultItem = new Intent();
            resultItem.putExtra("result","resultado bien hecho");
           // setResult(RESULT_OK,resultItem);
            //finish();
        }catch (Exception e){

        }
    }

}
