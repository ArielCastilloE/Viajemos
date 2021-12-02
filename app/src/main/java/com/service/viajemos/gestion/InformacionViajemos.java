package com.service.viajemos.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.viajemos.R;
import com.service.viajemos.login.LoginActivity;
import com.service.viajemos.pasajero.HistorialViajes;

public class InformacionViajemos extends AppCompatActivity {

    private String PASAJERO = "Pasajero";
    private String CONDUCTOR = "Conductor";
    private ImageView crearViaje, buscarViaje, verPerfil, consultaHistorial;
    String idUserBD, numeroDocumentoBD, nombreBD, celularBD, correoBD, perfilBD;
    private ImageButton cerrarSesion;

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

        mAuth = FirebaseAuth.getInstance();

        datosUsuario = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        idUserBD = datosUsuario.getString("id");
        correoBD = datosUsuario.getString("correo");
        cerrarSesion = (ImageButton) findViewById(R.id.imgCerrarSesion);

        if(datosUsuario.getString("perfil") !=null){
            perfilBD = datosUsuario.getString("perfil");
        }
        else{
             consultarPerfil();
        }

        crearViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(perfilBD.equals(CONDUCTOR)){
                    Intent intent = new Intent(InformacionViajemos.this, CrearServicio.class);
                    intent.putExtra("correo", correoBD);
                    intent.putExtra("id", idUserBD);
                    intent.putExtra("perfil",perfilBD);
                    startActivity(intent);
                }
                else if(perfilBD.equals(PASAJERO)){
                   Toast.makeText(InformacionViajemos.this,"Tu perfil es de Pasajero y no puedes crear servicios",Toast.LENGTH_LONG).show();
                }


            }
        });

        buscarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformacionViajemos.this, BuscarViaje.class);
                intent.putExtra("correo", correoBD);
                intent.putExtra("id", idUserBD);
                intent.putExtra("perfil",perfilBD);
                startActivity(intent);
            }
        });

        consultaHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacionViajemos.this, HistorialViajes.class);
                intent.putExtra("correo", correoBD);
                intent.putExtra("id", idUserBD);
                intent.putExtra("perfil",perfilBD);
                startActivity(intent);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InformacionViajemos.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void consultarPerfil(){
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    perfilBD = snapshot.child(idUserBD).child("Perfil").getValue().toString();
                }
                else {
                    Toast.makeText(InformacionViajemos.this,"Error Consulta  "+ idUserBD, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}