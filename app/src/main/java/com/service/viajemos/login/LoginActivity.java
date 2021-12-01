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

    private String usuario, password;
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

                 usuario = (String) txtUsuario.getText().toString();
                 password = (String) txtPassword.getText().toString();
                 if(validateForm(usuario,password)){
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

        accedeGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha = fecha();
                String hora = hora();
                Toast.makeText(LoginActivity.this,"Fecha actual: " + fecha + "Hora actual: "+hora,Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateForm(String emailS, String passwordS)
    {
        boolean esValido=true;

        emailS.trim();
        if(TextUtils.isEmpty(emailS)){
            esValido=false;
            txtUsuario.setError("Campo Requerido");
        }

        passwordS.trim();
        if(TextUtils.isEmpty(passwordS)){
            esValido=false;
            txtPassword.setError("Campo Requerido");
        }

        if(passwordS.length()<6)
        {
            esValido=false;
            txtPassword.setError("La contraseña debe tener al menos 6 caracteres");
        }

        if(esValido=true && TextUtils.isEmpty(passwordS)!=true){
            if (!validarEmail(emailS)){
                txtUsuario.setError("Email no válido");
                esValido=false;
            }
        }

        return esValido;
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(this,LoginActivity.class));
            progressDialog.dismiss();
        }else{

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(usuario,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, RegistrarUsuarioPpal.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"No se pudo iniciar Sesión, valide sus datos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


   /* private void Autenticacion(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }
*/

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

    private void reload() { }

}