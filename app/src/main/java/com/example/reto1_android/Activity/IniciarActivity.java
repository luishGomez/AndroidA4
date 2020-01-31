package com.example.reto1_android.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reto1_android.R;
import com.example.reto1_android.encriptaciones.Encriptador;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.UserBean;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.retrofit.UserAPIClient;
import com.example.reto1_android.retrofit.UserManager;
import com.example.reto1_android.sql.AdminSQLiteOpenHelper;

import java.io.Serializable;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity Iniciar.
 *
 * @author Ricardo
 */
public class IniciarActivity extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(GestorDeApuntes.class.getName());
    private Encriptador encriptador;
    EditText etNombreUsuario;
    EditText etContra;
    Button btnRegistrar;
    Button btnAcceder;
    private TextView etPaswordForgot;
    private CheckBox cbRecordar;
    boolean puedeSerCliente = false;
    private boolean recordar = false;
    private  MediaPlayer mp ;

    /**
     * Al crear la actividad.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mp= MediaPlayer.create(this,R.raw.pulsar_boton);
        etNombreUsuario = findViewById(R.id.etNombreUsuarioInicio);
        etContra = findViewById(R.id.etContraInicio);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnAcceder = findViewById(R.id.btnAcceder);
        encriptador = new Encriptador();
        etPaswordForgot = (TextView) findViewById(R.id.etPasswordForgot);
        SpannableString content = new SpannableString(getResources().getString(R.string.g11));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        etPaswordForgot.setText(content);
        etPaswordForgot.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                irRecuperarPassword();
            }
        });
        cbRecordar = (CheckBox) findViewById(R.id.cbRecordar);
        cbRecordar.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recordar = isChecked;
            }
        });
        boolean versionAnterior = false;
        SQLiteDatabase BaseDeDatos = null;
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            BaseDeDatos = admin.getWritableDatabase();
            Cursor fila = BaseDeDatos.rawQuery("select codigo,recordar from cuenta", null);
            if (fila.moveToFirst()) {
                versionAnterior = true;
            }
            BaseDeDatos.close();

        } catch (Exception e) {
            if (BaseDeDatos.isOpen()) {
                BaseDeDatos.close();
            }
        }
        if(versionAnterior){
            try {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                BaseDeDatos = admin.getWritableDatabase();
                Cursor fila = BaseDeDatos.rawQuery("select codigo,login,password,recordar from cuenta", null);
                if (fila.moveToFirst()) {
                    if(fila.getInt(3)==1) {
                        etNombreUsuario.setText(fila.getString(1));
                        etPaswordForgot.setText(fila.getString(2));
                        cbRecordar.setChecked(true);
                    }
                }
                BaseDeDatos.close();

            } catch (Exception e) {
                if (BaseDeDatos.isOpen()) {
                    BaseDeDatos.close();
                }
            }
        }
    }

    private void irRecuperarPassword() {
        mp.start();
        Intent intent = new Intent(this,PasswordForgot.class);
        startActivity(intent);
    }


    /**
     * Comprueba los datos, crea el hilo y analiza el resultado.
     *
     * @param v Controlador sobre la que se ha ejecutado la acción.
     */
    public void clickOnbtnAcceder(View v) {
        mp.start();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setPositiveButton(android.R.string.ok, null);
        if (!isEmpty()) {
            if (isNetworkAvailable()) {
                Object resultado = null;
                try {

                    UserManager userManager = UserAPIClient.getClient();
                    Call<UserBean> call = userManager.iniciarSesion(etNombreUsuario.getText().toString().trim(), encriptador.encriptar(etContra.getText().toString().trim()));
                    //Call<UserBean> call = userManager.iniciarSesion(etNombreUsuario.getText().toString().trim(), etContra.getText().toString().trim());
                    call.enqueue(new Callback<UserBean>() {
                        @Override
                        public void onResponse(Call<UserBean> call, Response<UserBean> response) {

                            tratarInicioSesion(call, response);
                        }

                        @Override
                        public void onFailure(Call<UserBean> call, Throwable t) {
                            falloCallUser(t);
                        }
                    });

                /*}catch(EncriptarException e){
                    LOGGER.severe("Error al encriptar: "+e.getMessage());*/
                } catch (Exception e) {
                    alertDialog.setMessage(R.string.error + e.getMessage());
                    alertDialog.show();
                }
            } else {
                alertDialog.setMessage(R.string.noInternet);
                alertDialog.show();
            }
        } else {
            alertDialog.setMessage(R.string.campoVacio);
            alertDialog.show();
        }
    }

    private void falloCallCliente(Throwable t) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("ERROR" + t.getMessage());
        alertDialog.show();
    }

    private void falloCallUser(Throwable t) {
        LOGGER.severe("Intentar con cliente");
        try {
            ClienteManager clienteManager = ClienteAPIClient.getClient();
            Call<ClienteBean> callCliente = clienteManager.iniciarSesion(etNombreUsuario.getText().toString().trim(), encriptador.encriptar(etContra.getText().toString().trim()));
            //Call<ClienteBean> callCliente = clienteManager.iniciarSesion(etNombreUsuario.getText().toString().trim(), etContra.getText().toString().trim());
            callCliente.enqueue(new Callback<ClienteBean>() {
                @Override
                public void onResponse(Call<ClienteBean> callCliente, Response<ClienteBean> response) {
                    tratarInicioSesionCliente(callCliente, response);
                }

                @Override
                public void onFailure(Call<ClienteBean> callCliente, Throwable t) {
                    falloCallCliente(t);
                }
            });
       /* }catch(EncriptarException e){
            LOGGER.severe("Error al encriptar: "+e.getMessage());*/
        } catch (Exception e) {
            LOGGER.severe("Error al usar user: " + e.getMessage());
        }

    }

    private void tratarInicioSesion(Call<UserBean> callDelUser, Response<UserBean> response) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if (response.code() == 401) {
            alertDialog.setMessage("La contraseña es incorrecta.");
            alertDialog.show();
            etContra.setText("");
        } else if (response.code() == 404) {
            alertDialog.setMessage("El login es incorrecto.");
            alertDialog.show();
            etContra.setText("");
            etNombreUsuario.setText("");
        } else if (response.code() != 500) {

            UserBean user = response.body();
            //Intent intent = new Intent(this, GestorDeApuntes.class);
            Intent intent = new Intent(this, MenuAdmin.class);
            intent.putExtra("user", (Serializable) user);
            startActivity(intent);
            guardarInfo();
            finish();
        } else {
            LOGGER.severe("Error " + response.code() + " al iniciar sesion como administrador");
        }
    }

    private void tratarInicioSesionCliente(Call<ClienteBean> callCliente, Response<ClienteBean> response) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if (response.code() == 401) {
            alertDialog.setMessage("La contraseña es incorrecta.");
            alertDialog.show();
            etContra.setText("");
        } else if (response.code() == 404) {
            alertDialog.setMessage("El login es incorrecto.");
            alertDialog.show();
            etContra.setText("");
            etNombreUsuario.setText("");
        } else if (response.code() != 500) {
            ClienteBean cliente = response.body();
            //LOGGER.severe("El saldo "+cliente.getSaldo());
           // Intent intent = new Intent(this, TiendaApuntes.class);
            //Intent intent = new Intent(this, MisApuntes.class);
            Intent intent = new Intent(this,MenuCliente.class);
            intent.putExtra("cliente", (Serializable) cliente);
            startActivity(intent);
            guardarInfo();
            finish();
        } else {
            LOGGER.severe("Error al intentar iniciar sesión con cliente." + response.code());
        }
    }

    private void guardarInfo() {
        if (this.recordar) {
            boolean versionAnterior = false;
            SQLiteDatabase BaseDeDatos = null;
            try {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                BaseDeDatos = admin.getWritableDatabase();
                Cursor fila = BaseDeDatos.rawQuery("select codigo,recordar from cuenta", null);
                if (fila.moveToFirst()) {
                    versionAnterior = true;
                }
                BaseDeDatos.close();

            } catch (Exception e) {
                if (BaseDeDatos.isOpen()) {
                    BaseDeDatos.close();
                }
            }
            if (versionAnterior) {
                try {
                    //modificar login
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                    BaseDeDatos = admin.getWritableDatabase();
                    ContentValues modificarLogin = new ContentValues();
                    modificarLogin.put("codigo", 0);
                    modificarLogin.put("login", etNombreUsuario.getText().toString());
                    modificarLogin.put("password", etPaswordForgot.getText().toString());
                    modificarLogin.put("recordar", 1);
                    BaseDeDatos.update("cuenta", modificarLogin, "codigo=" + 0, null);
                    BaseDeDatos.close();
                } catch (Exception e) {
                    if (BaseDeDatos.isOpen()) {
                        BaseDeDatos.close();
                    }
                }
            } else {
                try {
                    //crear login
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                    BaseDeDatos = admin.getWritableDatabase();
                    ContentValues crearLogin = new ContentValues();
                    crearLogin.put("codigo", 0);
                    crearLogin.put("login", etNombreUsuario.getText().toString());
                    crearLogin.put("password", etPaswordForgot.getText().toString());
                    crearLogin.put("recordar", 1);
                    BaseDeDatos.insert("cuenta", null, crearLogin);
                    BaseDeDatos.close();

                } catch (Exception e) {
                    if (BaseDeDatos.isOpen()) {
                        BaseDeDatos.close();
                    }
                }
            }
        } else {
            boolean versionAnterior = false;
            SQLiteDatabase BaseDeDatos = null;
            try {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                BaseDeDatos = admin.getWritableDatabase();
                Cursor fila = BaseDeDatos.rawQuery("select codigo,recordar from cuenta", null);
                if (fila.moveToFirst()) {
                    versionAnterior = true;
                }
                BaseDeDatos.close();

            } catch (Exception e) {
                if (BaseDeDatos.isOpen()) {
                    BaseDeDatos.close();
                }
            }
            if (versionAnterior) {
                try {
                    //modificar poner a false
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
                    BaseDeDatos = admin.getWritableDatabase();
                    ContentValues crearLogin = new ContentValues();
                    crearLogin.put("codigo", 0);
                    crearLogin.put("login", etNombreUsuario.getText().toString());
                    crearLogin.put("password", etPaswordForgot.getText().toString());
                    crearLogin.put("recordar", 0);
                    BaseDeDatos.update("cuenta", crearLogin,"codigo="+0,null);
                    BaseDeDatos.close();
                } catch (Exception e) {
                    LOGGER.severe("ERROR SQL"+e.getMessage());
                    if (BaseDeDatos.isOpen()) {
                        BaseDeDatos.close();
                    }
                }
            }
        }
    }


    /**
     * Inicia la activity Registro.
     *
     * @param v Controlador sobre la que se ha ejecutado la acción.
     */
    public void clickOnbtnRegistrar(View v) {
        mp.start();
        //this.btnRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    /**
     * Comprueba que los campos de texto no esten vacios.
     *
     * @return true si estan vacios.
     */
    public boolean isEmpty() {
        boolean esta = false;
        if (etNombreUsuario.getText().length() == 0 || etContra.getText().length() == 0) {
            esta = true;
        }
        return esta;
    }

    /**
     * Comprueba si hay conexion a internet
     *
     * @return true si hay conexion a internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Analiza y devuelve una cadena para el usuario apropiada.
     *
     * @param error Es el numero que identifica el error.
     * @return 1:LogicException, 2:EsperaCompletaException, 3:ServerException
     * 4:DAOException, 5:LoginIDException, 6:PasswordException
     */
    public String analizaError(Integer error) {
        String resultado = getString(R.string.huboError);
        switch (error) {
            case 1:
                resultado = getString(R.string.errorLogic);
                break;
            case 2:
                resultado = getString(R.string.errorEspera);
                break;
            case 3:
                resultado = getString(R.string.errorServer);
                break;
            case 4:
                resultado = getString(R.string.errorDAO);
                break;
            case 5:
                resultado = getString(R.string.errorIDLogin);
                break;
            case 6:
                resultado = getString(R.string.errorPassword);
                break;
        }
        return resultado;
    }
}