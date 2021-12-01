package com.service.viajemos.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.service.viajemos.R;
import com.service.viajemos.conductor.RegistroCuenta;
import com.service.viajemos.gestion.InformacionViajemos;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarUsuarioPpal extends AppCompatActivity {

    Spinner perfil;
    TextView txtUsuarioRegistro ;
    TextView txtPassRegistro;
    Button btnRegistroCuenta;


    private String usuarioDB="";
    private String passwordDB ="";
    private String perfilDB = "";

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        txtPassRegistro = (TextView) findViewById(R.id.txtPassRegistroPpal);
        txtUsuarioRegistro = (TextView) findViewById(R.id.txtUsrRegistroPpal);
        btnRegistroCuenta = (Button) findViewById(R.id.btnRegistroPpal);

        perfil = (Spinner) findViewById(R.id.spinPerfil);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.perfil, android.R.layout.simple_spinner_item);
        perfil.setAdapter(adapter);
        perfil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegistroCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario, password;
                usuario =txtUsuarioRegistro.getText().toString().trim();
                password = txtPassRegistro.getText().toString().trim();
                perfilDB = perfil.getSelectedItem().toString();
                if(validateForm(usuario,password)){
                    registrarUsuarioPpal(usuario,password);

                }
                else{
                    Toast.makeText(RegistrarUsuarioPpal.this,"Error en Formulario",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void registrarUsuarioPpal(String usuario, String password){
        progressDialog.setMessage("Realizando registro en Línea...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(usuario, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Usuario", usuario);
                    map.put("password", password);
                    map.put("Perfil", perfilDB);

                    String idUser = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Usuarios").child(idUser).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Intent intent = new Intent(RegistrarUsuarioPpal.this, RegistroCuenta.class);
                                intent.putExtra("correo", usuario);
                                intent.putExtra("id", idUser);
                                intent.putExtra("perfil", perfilDB);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(RegistrarUsuarioPpal.this,"No se pudo crear usuario en la BD", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(RegistrarUsuarioPpal.this, "No se pudo RegistrarUsuarioPpal este usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private boolean validateForm(String emailS, String passwordS)
    {
        boolean esValido=true;

        emailS.trim();
        if(TextUtils.isEmpty(emailS)){
            esValido=false;
            txtUsuarioRegistro.setError("Campo Requerido");
        }

        passwordS.trim();
        if(TextUtils.isEmpty(passwordS)){
            esValido=false;
            txtPassRegistro.setError("Campo Requerido");
        }

        if(passwordS.length()<6)
        {
            esValido=false;
            txtPassRegistro.setError("La contraseña debe tener al menos 6 caracteres");
        }

        if(esValido=true && TextUtils.isEmpty(passwordS)!=true){
            if (!validarEmail(emailS)){
                txtUsuarioRegistro.setError("Email no válido");
                esValido=false;
            }
        }

        return esValido;
    }


    private void updateUI(FirebaseUser user) {
        if(perfil!=null){
            startActivity(new Intent(RegistrarUsuarioPpal.this, RegistrarUsuarioPpal.class));

        }else{
            startActivity(new Intent(RegistrarUsuarioPpal.this, InformacionViajemos.class));
        }
    }

    public static boolean validarEmail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
    private void reload() { }


}