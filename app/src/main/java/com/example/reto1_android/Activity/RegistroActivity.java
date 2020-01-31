package com.example.reto1_android.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.Thread.HiloAndroid;

import clases.User;

/**
 * sergio
 */
public class RegistroActivity extends AppCompatActivity{
    private HiloAndroid hilo;
    private User user = new User();
    private Boolean correcto = false;
    private EditText nombreCompleto;
    private EditText email;
    private EditText login;
    private EditText contraseña;
    private EditText confirmar;
    private Button registro;
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("main");
    private MediaPlayer mp ;


    /**
     * Al crear la acitividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        nombreCompleto = findViewById(R.id.txtNombreCompleto);
        email = findViewById(R.id.txtEmail);
        login = findViewById(R.id.txtLogin);
        contraseña = findViewById(R.id.pswContrasena);
        confirmar = findViewById(R.id.pswConfirmar);
        registro = findViewById(R.id.btnRgistro);
        mp= MediaPlayer.create(this,R.raw.pulsar_boton);



    }

    /**
     * Comprobamos si los campos estan rellenados y son correctos
     * @param v
     */
    public void onClickRegistrar(View v) {
        mp.start();
        if(!nombreCompleto.getText().toString().trim().isEmpty() && !email.getText().toString().trim().isEmpty() && !login.getText().toString().trim().isEmpty() && !contraseña.getText().toString().trim().isEmpty() && !confirmar.getText().toString().isEmpty()){
            if(!esEmail(email.getText().toString().trim())) {
                email.setTextColor(Color.RED);
                correcto = false;
            }else {
                email.setTextColor(Color.BLACK);
                correcto = true;
            }
            if((login.getText().toString().trim().length()<3)) {
                login.setTextColor(Color.RED);
                correcto = false;
            }else{
                login.setTextColor(Color.BLACK);
                correcto=true;
            }if(contraseña.getText().toString().length() < 3){
                contraseña.setText("");
                confirmar.setText("");
                contraseña.setHintTextColor(Color.RED);
                contraseña.setHint(R.string.errorContrasenaDebil);
                correcto = false;
            }else if(!confirmar.getText().toString().equals(contraseña.getText().toString())){
                Toast.makeText(RegistroActivity.this,R.string.errorContrasenasIncorrectas,Toast.LENGTH_SHORT).show();
                confirmar.setText("");
                contraseña.setText("");
                correcto = false;
            }else {
                contraseña.setHintTextColor(Color.BLACK);
                correcto = true;
            }
            if(correcto && esEmail(email.getText().toString().trim())) {
                user.setEmail(email.getText().toString().trim());
                user.setFullname(nombreCompleto.getText().toString().trim());
                user.setLogin(login.getText().toString().trim());
                user.setPassword(contraseña.getText().toString());
                if(isNetworkAvailable()) {
                    Object resultado = null;
                    try {
                        HiloAndroid hilo = new HiloAndroid(1, user);
                        hilo.start();
                        hilo.join();
                        resultado = hilo.getRespuesta();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                    }
                    if (resultado instanceof Boolean) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Has sido registrado.");
                        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        if (resultado == null) {
                            //Toast.makeText(this, "Hubo un error", Toast.LENGTH_LONG).show();
                            alertDialog.setMessage("Hubo un error");
                        } else {
                            //Toast.makeText(this, analizaError((Integer)resultado), Toast.LENGTH_LONG).show();
                            alertDialog.setMessage(analizaError((Integer) resultado));
                        }
                        alertDialog.setPositiveButton(android.R.string.ok, null);
                        alertDialog.show();
                    }
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setMessage("No tienes conexión a internet.");
                    alertDialog.setPositiveButton(android.R.string.ok, null);
                    alertDialog.show();
                }

                nombreCompleto.setHintTextColor(Color.GRAY);
                nombreCompleto.setHint(R.string.nombre);
                login.setHintTextColor(Color.GRAY);
                login.setHint(R.string.etNombreUsuario);
                email.setHintTextColor(Color.GRAY);
                email.setHint(R.string.email);
                contraseña.setHintTextColor(Color.GRAY);
                contraseña.setHint(R.string.etContra);;
                confirmar.setHintTextColor(Color.GRAY);
                confirmar.setHint(R.string.repContra);

            }

        }
        else {
            Toast.makeText(RegistroActivity.this,"Faltan datos por completar",Toast.LENGTH_SHORT).show();
            if(nombreCompleto.getText().toString().isEmpty())
                nombreCompleto.setHintTextColor(Color.RED);
            if(login.getText().toString().isEmpty())
                login.setHintTextColor(Color.RED);
            if(email.getText().toString().isEmpty())
                email.setHintTextColor(Color.RED);
            if(contraseña.getText().toString().isEmpty())
                contraseña.setHintTextColor(Color.RED);
            if(confirmar.getText().toString().isEmpty())
                confirmar.setHintTextColor(Color.RED);
        }
    }

    /**
     * Cancelamos la operacion y volvemos al Login
     * @param v
     */
    public void onClickVolver(View v) {
        mp.start();
        this.finish();
    }
    /**
     * Comprobar email tiene formato correcto
     * @param email
     * @return true si esta correcto
     */
    private  boolean esEmail(String email) {
        boolean resu=true;
        String firstPart,secondPart,thirdPart;
        if(email.length()<5 || email.length()>40){
            resu=false;
        }else{
            try{
                firstPart=email.substring(0, email.indexOf('@'));
                secondPart=email.substring(email.indexOf('@')+1,email.indexOf('.', email.indexOf('@')));
                thirdPart=email.substring(email.indexOf('.',email.indexOf('.', email.indexOf('@')))+1);
                if(!isEmailFirstPart(firstPart) || !isEmailSecondPart(secondPart) || !isEmailThridPart(thirdPart))
                    resu=false;
            }catch(StringIndexOutOfBoundsException e){
                resu=false;
            }
        }
        return resu;
    }

    /**
     * Comprobar los caracteres que hay hasta el "@" y los caracteres permitidos
     * @param cadena
     * @return true si esta correcto
     */
    private  boolean isEmailFirstPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) && cadena.charAt(i)!='.' && cadena.charAt(i)!='-' && cadena.charAt(i)!='_'){
                resu=false;
                break;
            }
        }
        return resu;
    }

    /**
     *  Comprobar si la cadena entre el "@" y el "." contiene los caracteres permitidos
     * @param cadena
     * @return true si esta correcto
     */
    private  boolean isEmailSecondPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i)) && cadena.charAt(i)!='-'){
                resu=false;
                break;
            }
        }
        return resu;
    }

    /**
     * Comprobar la cadena despues del punto si es correcto con sus caracteres permitidos
     * @param cadena
     * @return true si es correcto
     */
    private  boolean isEmailThridPart(String cadena) {
        boolean resu=true;
        for(int i=0;i<cadena.length();i++){
            if(!Character.isAlphabetic(cadena.charAt(i)) && !Character.isDigit(cadena.charAt(i))){
                resu=false;
                break;
            }
        }
        return resu;
    }

    /**
     * Analiza y devuelve una cadena para el usuario apropiada.
     * @param error Es el numero que identifica el error.
     * @return 1:LogicException, 2:EsperaCompletaException, 3:ServerException
     * 4:DAOException, 5:LoginIDException, 6:PasswordException
     */
    public String analizaError(Integer error) {
        String resultado="Hubo un Error.";
        switch (error) {
            case 1:
                resultado=getString(R.string.errorLogic);
                break;
            case 2:
                resultado=getString(R.string.errorEspera);
                break;
            case 3:
                resultado=getString(R.string.errorServer);
                break;
            case 4:
                resultado=getString(R.string.errorDAO);
                break;
            case 7:
                resultado=getString(R.string.errorIDExiste);
                break;
        }
        return resultado;
    }

    /**
     * Comprueba si hay conexion a internet.
     * @return true si hay conexion a internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}