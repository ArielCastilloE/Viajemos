package com.service.viajemos.gestion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.service.viajemos.R;

public class InformacionViajemos extends AppCompatActivity {

    private ImageView crearViaje, buscarViaje, verPerfil, consultaHistorial;
    String idUserBD, numeroDocumentoBD, nombreBD, celularBD, correoBD, perfilBD;

    Bundle datosUsuario;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_viajemos);

        crearViaje = (ImageView)  findViewById(R.id.imgCrearServicio);
        buscarViaje = (ImageView)  findViewById(R.id.imgConsultarViajes);
        verPerfil = (ImageView)  findViewById(R.id.imgVerPerfil);
        consultaHistorial =  (ImageView)  findViewById(R.id.imgHistoricoViajes);

        datosUsuario = getIntent().getExtras();
        idUserBD = datosUsuario.getString("id");
        correoBD = datosUsuario.getString("correo");
        perfilBD = datosUsuario.getString("perfil");

    }
}