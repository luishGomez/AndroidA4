package com.example.reto1_android.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.reto1_android.R;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.ui.ListadoTiendaPackActivity;

import java.io.Serializable;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ricardo Peinado Lastra
 */
public class MenuCliente extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(MenuCliente.class.getName());
    private ClienteBean cliente;
    private Button btnMiPerfil;
    private Button btnBiblioteca;
    private Button btnSubirApunte;
    private Button btnTiendaPacks;
    private Button btnTiendaApunte;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cliente);

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        mp = MediaPlayer.create(this, R.raw.pulsar_boton);

        btnBiblioteca = (Button) findViewById(R.id.btnBiblioteca);
        btnMiPerfil = (Button) findViewById(R.id.btnMiPerfil);
        btnSubirApunte = (Button) findViewById(R.id.btnSubirApunte);
        btnTiendaApunte = (Button) findViewById(R.id.btnTiendaApuntes);
        btnTiendaPacks = (Button) findViewById(R.id.btnTiendaPacks);
        btnBiblioteca.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    ClienteManager clienteManager = ClienteAPIClient.getClient();
                    Call<ClienteBean> callCliente = clienteManager.find(cliente.getId());
                    callCliente.enqueue(new Callback<ClienteBean>() {
                        @Override
                        public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                            if (response.isSuccessful()) {
                                cliente = response.body();
                                iniciarActivityBiblioteca();
                            } else {
                                LOGGER.severe("Error al intentar abir la tienda de apuntes: " + response.code());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ClienteBean> call, Throwable t) {
                            LOGGER.severe("Fallo al intentar abir la tienda de apuntes: " + t.getMessage());
                            informar(getResources().getString(R.string.gda2));
                        }
                    });

                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnMiPerfil.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    ClienteManager clienteManager = ClienteAPIClient.getClient();
                    Call<ClienteBean> callCliente = clienteManager.find(cliente.getId());
                    callCliente.enqueue(new Callback<ClienteBean>() {
                        @Override
                        public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                            if (response.isSuccessful()) {
                                cliente = response.body();
                                iniciarActivityPerfil();
                            } else {
                                LOGGER.severe("Error al intentar abir la tienda de apuntes: " + response.code());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ClienteBean> call, Throwable t) {
                            LOGGER.severe("Fallo al intentar abir la tienda de apuntes: " + t.getMessage());
                            informar(getResources().getString(R.string.gda2));
                        }
                    });

                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnSubirApunte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    ClienteManager clienteManager = ClienteAPIClient.getClient();
                    Call<ClienteBean> callCliente = clienteManager.find(cliente.getId());
                    callCliente.enqueue(new Callback<ClienteBean>() {
                        @Override
                        public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                            if (response.isSuccessful()) {
                                cliente = response.body();
                                iniciarActivitySubirApunte();
                            } else {
                                LOGGER.severe("Error al intentar abir la tienda de apuntes: " + response.code());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ClienteBean> call, Throwable t) {
                            LOGGER.severe("Fallo al intentar abir la tienda de apuntes: " + t.getMessage());
                            informar(getResources().getString(R.string.gda2));
                        }
                    });

                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnTiendaApunte.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    ClienteManager clienteManager = ClienteAPIClient.getClient();
                    Call<ClienteBean> callCliente = clienteManager.find(cliente.getId());
                    callCliente.enqueue(new Callback<ClienteBean>() {
                        @Override
                        public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                            if (response.isSuccessful()) {
                                cliente = response.body();
                                iniciarActivityTiendaApunte();
                            } else {
                                LOGGER.severe("Error al intentar abir la tienda de apuntes: " + response.code());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ClienteBean> call, Throwable t) {
                            LOGGER.severe("Fallo al intentar abir la tienda de apuntes: " + t.getMessage());
                            informar(getResources().getString(R.string.gda2));
                        }
                    });

                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });
        btnTiendaPacks.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.start();
                    ClienteManager clienteManager = ClienteAPIClient.getClient();
                    Call<ClienteBean> callCliente = clienteManager.find(cliente.getId());
                    callCliente.enqueue(new Callback<ClienteBean>() {
                        @Override
                        public void onResponse(Call<ClienteBean> call, Response<ClienteBean> response) {
                            if (response.isSuccessful()) {
                                cliente = response.body();
                                iniciarActivityTiendaPacks();
                            } else {
                                LOGGER.severe("Error al intentar abir la tienda de apuntes: " + response.code());
                                informar(getResources().getString(R.string.gda1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ClienteBean> call, Throwable t) {
                            LOGGER.severe("Fallo al intentar abir la tienda de apuntes: " + t.getMessage());
                            informar(getResources().getString(R.string.gda2));
                        }
                    });

                } catch (Exception e) {
                    LOGGER.severe("Error al intentar abir la tienda de apuntes: " + e.getMessage());
                    informar(getResources().getString(R.string.gda1));
                }
            }
        });

    }

    private void iniciarActivityTiendaPacks() {
        Intent intent = new Intent(this, ListadoTiendaPackActivity.class);
        intent.putExtra("cliente", (Serializable) cliente);
        startActivity(intent);
    }

    private void iniciarActivityBiblioteca() {
        Intent intent = new Intent(this, BibliotecaActivity.class);
        intent.putExtra("cliente", (Serializable) cliente);
        startActivity(intent);
    }

    private void iniciarActivityPerfil() {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("cliente", (Serializable) cliente);
        startActivity(intent);
    }

    /**
     * Inicia la actividad subir apunte
     */
    private void iniciarActivitySubirApunte() {
        Intent intent = new Intent(this, MisApuntes.class);
        intent.putExtra("cliente", (Serializable) cliente);
        startActivity(intent);
    }

    /**
     * Inicia la actividad tienda apunte.
     */
    private void iniciarActivityTiendaApunte() {
        Intent intent = new Intent(this, TiendaApuntes.class);
        intent.putExtra("cliente", (Serializable) cliente);
        startActivity(intent);
    }

    /**
     * Informa al cliente del mensaje que se pasa como parametro.
     * @param frase El mensaje a trasmitir.
     */
    public void informar(String frase) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(frase);
        alertDialog.show();
    }
}
