package com.service.viajemos.gestion;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import  com.service.viajemos.utilidades.Ciudades;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.service.viajemos.R;

public class BuscarViaje extends AppCompatActivity {

    TextView fechaViaje;
    String fechaViajeBD;
    Button  btnBuscarViajes;
    Bundle datosUsuario;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_viaje);

        datosUsuario = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Ciudades.CIUIDADES);
        AutoCompleteTextView autoCompleteCiudaOrigen = (AutoCompleteTextView) findViewById(R.id.txtBuscarOrigen);
        AutoCompleteTextView autoCompleteCiudaDestino = (AutoCompleteTextView) findViewById(R.id.txtBuscarDestino);
        autoCompleteCiudaOrigen.setAdapter(adapter);
        autoCompleteCiudaDestino.setAdapter(adapter);

        fechaViaje = (TextView) findViewById(R.id.txtBuscarFecha);


        btnBuscarViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }



    }
