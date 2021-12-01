package com.service.viajemos.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.service.viajemos.conductor.RegistroCuenta;
import com.service.viajemos.gestion.InformacionViajemos;
import com.service.viajemos.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    private String usuario, password, idUserBD;
    public static String TAG = "Viajemos";
    // firebase intent
    private FirebaseAuth mAuth;
    private EditText  txtUsuario, txtPassword;
    private TextView txRegistrarse,accedeGoogle;
    private Button btnButton;
    private ProgressDialog processingDialog;
    TextView forgotpassword;

    //FirebaseAuth
    DatabaseReference reference;
    PreferManag preferenceManager;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        preferenceManager= new PreferManag(getApplicationContext());

        txtUsuario = (EditText) findViewById(R.id.txtUsuarioLogin);
        txtPassword = (EditText)  findViewById(R.id.txtUsrRegistroPpal);
        btnButton = (Button)  findViewById(R.id.btnIngresarLogin);
        txRegistrarse = (TextView) findViewById(R.id.txtIdentificacionRegistro);
        progressDialog = new ProgressDialog(this);
        accedeGoogle = (TextView) findViewById(R.id.txtAccederGoogle);



         btnButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    usuario = txtUsuario.getText().toString();
                    password = txtPassword.getText().toString();
                    if(validateForm()){
                        loginUser();
                 }
             }
         });

        txRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrarUsuarioPpal.class);
                startActivity(i);
            }
        });


    }

    private boolean validateForm()
    {
        boolean esValido=true;

        usuario.trim();
        if(TextUtils.isEmpty(usuario)){
            esValido=false;
            txtUsuario.setError("Campo Requerido");
        }

        password.trim();
        if(TextUtils.isEmpty(password)){
            esValido=false;
            txtPassword.setError("Campo Requerido");
        }

        if(password.length()<6)
        {
            esValido=false;
            txtPassword.setError("La contraseña debe tener al menos 6 caracteres");
        }

        if(esValido=true && TextUtils.isEmpty(password)!=true){
            if (!validarEmail(usuario)){
                txtUsuario.setError("Email no válido");
                esValido=false;
            }
        }

        return esValido;
    }

    private void loginUser(){
                mAuth.signInWithEmailAndPassword(usuario,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    idUserBD =  mAuth.getCurrentUser().getUid();
                    Intent intent = new Intent(LoginActivity.this, InformacionViajemos.class);
                    intent.putExtra("correo", usuario);
                    intent.putExtra("id", idUserBD);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"No se pudo iniciar Sesión, valide sus datos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public static boolean validarEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void Salir(View v){
        //cierre sesion
        mAuth.signOut();
        //UpdateUi
        Intent intent=new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private String fecha(){
        String mytime = (DateFormat.format("yyyyMMdd", new java.util.Date()).toString());
        return mytime;
    }

    private String hora(){
        String mytime = (DateFormat.format("hhmm", new java.util.Date()).toString());
        return mytime;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser() != null){
            idUserBD =  mAuth.getCurrentUser().getUid();
            Intent intent = new Intent(LoginActivity.this, InformacionViajemos.class);
            intent.putExtra("correo", usuario);
            intent.putExtra("id", idUserBD);
            startActivity(intent);
        }
    }

}