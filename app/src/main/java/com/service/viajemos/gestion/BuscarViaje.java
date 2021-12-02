package com.service.viajemos.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.viajemos.login.LoginActivity;
import com.service.viajemos.pasajero.HistorialViajes;
import com.service.viajemos.pasajero.PerfilPasajero;
import  com.service.viajemos.utilidades.Ciudades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.service.viajemos.R;

import java.util.Timer;
import java.util.TimerTask;

public class BuscarViaje extends AppCompatActivity {

    TextView buscaFechaViaje;
    ConstraintLayout contenedor;
    String buscaFechaOrigenBD, buscafechaDestinoBD, buscaFechaBD;
    Button  btnBuscarViajes;
    Boolean busquedaExitosa;
    ImageButton imgHome;
    ImageView imgCarro2;

    Bundle datosUsuario;
    AutoCompleteTextView autoCompleteCiudaOrigen;
    AutoCompleteTextView autoCompleteCiudaDestino;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_viaje);

        final Timer[] timer = new Timer[1];

        contenedor = (ConstraintLayout) findViewById(R.id.constBusquedaViajes);
        contenedor.setVisibility(View.GONE);

        datosUsuario = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Ciudades.CIUIDADES);
        autoCompleteCiudaOrigen = (AutoCompleteTextView) findViewById(R.id.txtBuscarOrigen);
        autoCompleteCiudaDestino = (AutoCompleteTextView) findViewById(R.id.txtBuscarDestino);
        autoCompleteCiudaOrigen.setAdapter(adapter);
        autoCompleteCiudaDestino.setAdapter(adapter);

        buscaFechaViaje = (TextView) findViewById(R.id.txtBuscarFecha);
        btnBuscarViajes = (Button) findViewById(R.id.btnBuscarViajes);
        imgCarro2 = (ImageView) findViewById(R.id.imgCarro2);
        imgHome = (ImageButton) findViewById(R.id.imgHome);

        btnBuscarViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscaFechaOrigenBD = autoCompleteCiudaOrigen.getText().toString();
                buscafechaDestinoBD = autoCompleteCiudaDestino.getText().toString();
                buscaFechaBD = buscaFechaViaje.getText().toString();
                if(validateForm()) {
                    //buscarViaje();
                    contenedor.setVisibility(View.VISIBLE);
                    //Toast.makeText(BuscarViaje.this, "No hay registros para los criterios, se siguieren otras similares", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(BuscarViaje.this,"Datos imcompletos en el Formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgCarro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuscarViaje.this, DetalleViaje.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateForm()
    {
        boolean esValido=true;

        buscaFechaOrigenBD.trim();
        if(TextUtils.isEmpty(buscaFechaOrigenBD)){
            esValido=false;
            autoCompleteCiudaOrigen.setError("Campo Requerido");
        }

        buscafechaDestinoBD.trim();
        if(TextUtils.isEmpty(buscafechaDestinoBD)){
            esValido=false;
            autoCompleteCiudaDestino.setError("Campo Requerido");
        }

        buscaFechaBD.trim();
        if(TextUtils.isEmpty(buscaFechaBD)){
            esValido=false;
            buscaFechaViaje.setError("Campo Requerido");
        }

        return esValido;
    }

    public void buscarViaje(){
        progressDialog.setMessage("Realizando consulta en Línea...");
        progressDialog.show();
        Log.i("Pasé por aqui: ", "Inicio de Busqueda");

        mDatabase.child("Servicio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()){

                     for(DataSnapshot ds: snapshot.getChildren()){

                          String fechaValidacion = ds.child(buscaFechaBD).getValue().toString();
                           Log.i("Dato", fechaValidacion);
                     }
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
