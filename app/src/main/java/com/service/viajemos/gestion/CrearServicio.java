package com.service.viajemos.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.viajemos.R;
import com.service.viajemos.utilidades.Ciudades;

public class CrearServicio extends AppCompatActivity {

    TextView fechaViaje;
    String fechaViajeBD, idUserBD, numeroDocumentoDB;
    Button btnCrearServicio;
    Bundle datosUsuario;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio);

        datosUsuario = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        idUserBD = datosUsuario.getString("id");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Ciudades.CIUIDADES);
        AutoCompleteTextView autoCompleteCiudaOrigen = (AutoCompleteTextView) findViewById(R.id.txtUbicacionOrigen);
        AutoCompleteTextView autoCompleteCiudaDestino = (AutoCompleteTextView) findViewById(R.id.txtUbicacionDestino);
        autoCompleteCiudaOrigen.setAdapter(adapter);
        autoCompleteCiudaDestino.setAdapter(adapter);

        fechaViaje = (TextView) findViewById(R.id.txtBuscarFecha);
        btnCrearServicio = (Button) findViewById(R.id.btnCrearOferta);


        btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarConductor();
            }
        });

    }

    private void consultarConductor(){
        mDatabase.child("Conductor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    numeroDocumentoDB = snapshot.child(idUserBD).child("numeroDocumento").getValue().toString();
                    Toast.makeText(CrearServicio.this,"Cédula Recuperada: " + numeroDocumentoDB, Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(CrearServicio.this,"Error Consulta  "+ idUserBD, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}