package com.example.reto1_android.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.reto1_android.R;
import com.example.reto1_android.model.UserBean;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.retrofit.UserAPIClient;
import com.example.reto1_android.retrofit.UserManager;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Permite cambiar la contraseña en caso de haberla perdido.
 * @author Ricardo Peinado Lastra
 */
public class PasswordForgot extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(PasswordForgot.class.getName());
    private Button recuBoton;
    private EditText editText;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        mp = MediaPlayer.create(this, R.raw.pulsar_boton);
        recuBoton = (Button) findViewById(R.id.recu_boton);
        editText = (EditText) findViewById(R.id.recu_textField);
        recuBoton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() > 3) {
                    try {
                        mp.start();
                        ClienteManager clienteManager = ClienteAPIClient.getClientText();
                        Call<Boolean> call = clienteManager.passwordForgot(editText.getText().toString().trim());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.isSuccessful()){
                                    informar(getResources().getString(R.string.recuFinal));
                                    editText.setFocusable(false);
                                    recuBoton.setEnabled(false);
                                }else{
                                    if(response.code()==404){
                                        informar(getResources().getString(R.string.recuRevisa));
                                        editText.setText("");
                                        editText.setTextColor(Color.RED);
                                    }else{
                                        informar(getResources().getString(R.string.gda1));
                                        LOGGER.severe("Error al intentar recuperar la contraseña " + response.code());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                informar(getResources().getString(R.string.gda2));
                                LOGGER.severe("Fallo al intentar recuperar la contraseña " + t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        informar(getResources().getString(R.string.gda1));
                        LOGGER.severe("Error al intentar recuperar la contraseña " + e.getMessage());
                    }

                }else{
                    //demasiado corta
                }
            }
        });
    }

    /**
     * Permite sacar por pantalla al usuario algun problema
     * @param frase
     */
    private void informar(String frase) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }
}



