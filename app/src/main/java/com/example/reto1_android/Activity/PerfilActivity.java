package com.example.reto1_android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto1_android.R;
import com.example.reto1_android.encriptaciones.Encriptador;
import com.example.reto1_android.encriptaciones.EncriptarException;
import com.example.reto1_android.model.ClienteBean;
import com.example.reto1_android.model.UserBean;
import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;
import com.example.reto1_android.retrofit.UserAPIClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(PerfilActivity.class.getName());
    private static final int READ_REQUEST_CODE = 41;
    Boolean loginCorrecto = false;
    private int clickPerfil=0;
    private int clickContra=0;
    private ClienteBean cliente;
    private ImageView imagenPerfil;
    private EditText nombre;
    private EditText login;
    private EditText email;
    private EditText contrasenia;
    private EditText confirmarContrasenia;
    private Button aceptarContrasenia;
    private TextView actualizaContrasenia;
    private Button aceptarPerfil;
    private TextView txtActualizarPerfil;
    private Encriptador encriptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        imagenPerfil = findViewById(R.id.imageViewPerfil);
        imagenPerfil.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickModificarFoto(v);
            }
        });
        nombre = findViewById(R.id.txtNombreCompletoPerfil);
        login = findViewById(R.id.txtLoginPerfil);
        email = findViewById(R.id.txtEmailPerfil);
        contrasenia = findViewById(R.id.pswContrasenaPerfil);
        confirmarContrasenia = findViewById(R.id.pswConfirmarContrasenia);
        aceptarContrasenia = findViewById(R.id.btnAceptarContrasenia);
        aceptarContrasenia.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAceptarContrasenia(v);
            }
        });
        aceptarPerfil = findViewById(R.id.btnAceptarPerfil);
        aceptarPerfil.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptarActualizarPerfil(v);
            }
        });
        actualizaContrasenia = findViewById(R.id.txtActualizarContrasenia);
        actualizaContrasenia.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickActualizar(v);
            }
        });
        txtActualizarPerfil=(TextView)findViewById(R.id.txtActualizarPerfil);
        txtActualizarPerfil.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickActualizarPerfil(v);
            }
        });

        Intent intent = getIntent();
        cliente = (ClienteBean) intent.getSerializableExtra("cliente");
        if(cliente.getFoto()!=null){
            byte[] imgbytes = Base64.decode(cliente.getFoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgbytes, 0,
                    imgbytes.length);
            imagenPerfil.setImageBitmap(bitmap);
        }
        nombre.setHint(cliente.getNombreCompleto());
        nombre.setEnabled(false);
        login.setHint(cliente.getLogin());
        login.setEnabled(false);
        email.setHint(cliente.getEmail());
        email.setEnabled(false);
        encriptador=new Encriptador();
    }

    /**
     * Hace visible los campos contraseña y su respectivo "Aceptar" Button.
     * Contador de clicks para poder mostrar y ocultar los campos clicando
     * el mismo TextView.
     * @param view
     */
    public void onClickActualizar(View view){
        if(clickContra==0){
            contrasenia.setVisibility(View.VISIBLE);
            confirmarContrasenia.setVisibility(View.VISIBLE);
            aceptarContrasenia.setVisibility(View.VISIBLE);
            actualizaContrasenia.setText(R.string.cancelarContrasenia);
            clickContra++;
        }
        else{
            actualizaContrasenia.setText(R.string.actualizarContrasenia);
            contrasenia.setVisibility(View.INVISIBLE);
            confirmarContrasenia.setVisibility(View.INVISIBLE);
            aceptarContrasenia.setVisibility(View.INVISIBLE);
            clickContra=0;
        }
    }
    /**
     * Hace visible los campos de datos personales y su respectivo "Aceptar" Button.
     * Contador de clicks para poder mostrar y ocultar los campos clicando
     * el mismo TextView.
     * @param view
     */
    public void  onClickActualizarPerfil(View view){
        if(clickPerfil==0){
            nombre.setEnabled(true);
            nombre.setHint(R.string.nombre);
            login.setEnabled(true);
            login.setHint(R.string.etNombreUsuario);
            email.setEnabled(true);
            email.setHint(R.string.email);
            aceptarPerfil.setVisibility(View.VISIBLE);
            clickPerfil++;
        }else{
            limpiarCampos();
        }

    }

    /**
     * Verifica que los campos estan correctamente rellenados
     * para poderlos actualizar en la BBDD.
     * @param view
     */
    public void aceptarActualizarPerfil(View view){
        Boolean correcto = false;
        if(!nombre.getText().toString().equals("")||!login.getText().toString().equals("")||!email.getText().toString().equals("")){
            if(!nombre.getText().toString().equals("")){
                correcto = true;
                cliente.setNombreCompleto(nombre.getText().toString().trim());
            }if(!login.getText().toString().equals("")){
                correcto = true;
                cliente.setLogin(login.getText().toString().trim());
            }if(!email.getText().toString().equals("")) {
                if(esEmail(email.getText().toString().trim())){
                    correcto = true;
                    cliente.setEmail(email.getText().toString().trim());
                }
                else{
                    Toast.makeText(PerfilActivity.this,"Email incorrecto",Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    correcto = false;
                }
            }
            if(correcto){
                ClienteManager clienteManager = ClienteAPIClient.getClient();
                Call<ClienteBean> callCliente = null;
                try {
                    callCliente = clienteManager.iniciarSesion(login.getText().toString().trim(),encriptador.encriptar("-1"));
                } catch (EncriptarException e) {
                    informar(getResources().getString(R.string.gda1));
                    LOGGER.severe(getResources().getString(R.string.gda1));
                }
                callCliente.enqueue(new Callback<ClienteBean>() {
                    @Override
                    public void onResponse(Call<ClienteBean> callCliente, Response<ClienteBean> response) {
                        if (response.code() == 404) {
                            ClienteManager clienteManager = ClienteAPIClient.getClient();
                            Call<Void> callUpdate = clienteManager.edit(cliente);
                            callUpdate.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> callUpdate, Response<Void> response) {
                                    Toast.makeText(PerfilActivity.this,"Se ha actualizado los datos de perfil",Toast.LENGTH_SHORT).show();
                                    limpiarCampos();
                                }

                                @Override
                                public void onFailure(Call<Void> callUpdate, Throwable t) {
                                    Toast.makeText(PerfilActivity.this,"No se ha podido actualizar los datos de perfil",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(PerfilActivity.this,"Nombre de usuario ya en uso",Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onFailure(Call<ClienteBean> callCliente, Throwable t) {
                        LOGGER.info("no se ha podido verificar Login");
                    }
                });

            }

        }

    }
    /**
     * Limpia, oculta los campos de los datos personales (Nombre, Login, Email).
     */
    public void limpiarCampos(){
        nombre.setText("");
        login.setText("");
        email.setText("");
        nombre.setHint(cliente.getNombreCompleto());
        nombre.setEnabled(false);
        login.setHint(cliente.getLogin());
        login.setEnabled(false);
        email.setHint(cliente.getEmail());
        email.setEnabled(false);
        aceptarPerfil.setVisibility(View.INVISIBLE);
        clickPerfil=0;

    }

    /**
     * Verifica que las contraseñas coincidan y sea mayor de 3 caracteres.
     * Una vez verificado actualiza el dato en la BBDD.
     * @param view
     */
    public void onClickAceptarContrasenia(View view){
        if(contrasenia.getText().length()>3){
            if(contrasenia.getText().toString().trim().equals(confirmarContrasenia.getText().toString().trim())){
                try {
                    cliente.setContrasenia(encriptador.encriptar(contrasenia.getText().toString().trim()));
                } catch (EncriptarException e) {
                    informar(getResources().getString(R.string.gda1));
                    LOGGER.severe(getResources().getString(R.string.gda1));
                }
                ClienteManager clienteManager = ClienteAPIClient.getClient();
                Call<Void> callCliente = clienteManager.actualizarContrasenia(cliente);
                callCliente.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> callCliente, Response<Void> response) {
                        Toast.makeText(PerfilActivity.this,"Se ha actualizado la contrasenia correctamente",Toast.LENGTH_SHORT).show();
                        actualizaContrasenia.setText(R.string.actualizarContrasenia);
                        contrasenia.setVisibility(View.INVISIBLE);
                        contrasenia.setText("");
                        confirmarContrasenia.setVisibility(View.INVISIBLE);
                        confirmarContrasenia.setText("");
                        aceptarContrasenia.setVisibility(View.INVISIBLE);
                        clickContra=0;
                    }
                    @Override
                    public void onFailure(Call<Void> callCliente, Throwable t) {
                        Toast.makeText(PerfilActivity.this,"No se ha podido actualizar la contrasenia",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(PerfilActivity.this,R.string.errorContrasenasIncorrectas,Toast.LENGTH_SHORT).show();
                contrasenia.setText("");
                confirmarContrasenia.setText("");
                contrasenia.requestFocus();
            }
        }
        else{
            Toast.makeText(PerfilActivity.this,R.string.errorContrasenaDebil,Toast.LENGTH_SHORT).show();
            contrasenia.setText("");
            confirmarContrasenia.setText("");
            contrasenia.requestFocus();
        }


    }

    /**
     * Abre el gestor de busqueda de archivos android con su respectivo filtro de tipo de archivo.
     * @param view
     */
    public void onClickModificarFoto(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Permisos para poder abrir gestor de busqieda de archivos.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                InputStream inputStream=null;
                try{
                    inputStream=getContentResolver().openInputStream(uri);
                    byte[] bFile = new byte[inputStream.available()];
                    inputStream.read(bFile);
                    LOGGER.info(String.valueOf(bFile.length));
                    //tranformar bytes a string para subir al servidor
                    cliente.setFoto(Base64.encodeToString(bFile,Base64.DEFAULT));
                    ClienteManager userManager = ClienteAPIClient.getClient();
                    Call<Void> call=userManager.edit(cliente);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            LOGGER.info("He actualizado");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            LOGGER.info("No se ha actualizado");
                        }
                    });
                }catch(Exception e){
                    LOGGER.severe("Error al leer los bytes "+e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.severe("Error al cerrar el stream");
                        }
                    }
                }

            }
        }
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
     * Vuelve al Menu principal.
     * @param view
     */
    public void onClickVolver(View view){
        this.finish();
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
