package com.service.viajemos.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class InformacionViajemos extends AppCompatActivity {

    private String PASAJERO = "Pasajero";
    private String CONDUCTOR = "Conductor";
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        idUserBD = datosUsuario.getString("id");
        correoBD = datosUsuario.getString("correo");

        if(datosUsuario.getString("perfil") !=null){
            perfilBD = datosUsuario.getString("perfil");
        }
        else{
             consultarPerfil();
        }

    }

    private void consultarPerfil(){
        mDatabase.child(idUserBD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    perfilBD = snapshot.child("perfil").getValue().toString();
                    Toast.makeText(InformacionViajemos.this,"Perfil Recuperado: ", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}