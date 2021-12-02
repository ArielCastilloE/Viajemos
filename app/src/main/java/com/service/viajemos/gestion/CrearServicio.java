package com.service.viajemos.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.viajemos.R;
import com.service.viajemos.conductor.RegistroCuenta;
import com.service.viajemos.utilidades.Ciudades;

import java.util.HashMap;
import java.util.Map;

public class CrearServicio extends AppCompatActivity {

    private final String ESTADO = "Programado";
    TextView fechaViaje, horaViaje,cupos, costo;
    String ubicacionOrigenDB, ubicacionDestinoDB, fechaViajeBD, idUserBD, numeroDocumentoDB, horaviajeDB, costoDB, cuposDB, estadoViajeDB, idFechaHora;
    Button btnCrearServicio;
    AutoCompleteTextView autoCompleteCiudaOrigen;
    AutoCompleteTextView autoCompleteCiudaDestino ;

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
        autoCompleteCiudaOrigen = (AutoCompleteTextView) findViewById(R.id.txtUbicacionOrigen);
        autoCompleteCiudaDestino = (AutoCompleteTextView) findViewById(R.id.txtUbicacionDestino);
        autoCompleteCiudaOrigen.setAdapter(adapter);
        autoCompleteCiudaDestino.setAdapter(adapter);


        fechaViaje = (TextView) findViewById(R.id.txtFechaViaje);
        btnCrearServicio = (Button) findViewById(R.id.btnCrearOferta);
        horaViaje = (TextView) findViewById(R.id.txtHoraViaje);
        costo = (TextView) findViewById(R.id.txtCosto);
        cupos = (TextView) findViewById(R.id.txtCupos);


        btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ubicacionOrigenDB = autoCompleteCiudaOrigen.getText().toString();
                    ubicacionDestinoDB = autoCompleteCiudaDestino.getText().toString();
                    fechaViajeBD = fechaViaje.getText().toString();
                    horaviajeDB = horaViaje.getText().toString();
                    costoDB = costo.getText().toString();
                    cuposDB = cupos.getText().toString();
                    if(validateForm()){
                        crearViaje();
                    }else{
                        Toast.makeText(CrearServicio.this,"Datos imcompletos en el Formaulario", Toast.LENGTH_LONG).show();
                    }
            }
        });

    }

    private boolean validateForm()
    {
        boolean esValido=true;

        ubicacionOrigenDB.trim();
        if(TextUtils.isEmpty(ubicacionOrigenDB)){
            esValido=false;
            autoCompleteCiudaOrigen.setError("Campo Requerido");
        }

        ubicacionDestinoDB.trim();
        if(TextUtils.isEmpty(ubicacionDestinoDB)){
            esValido=false;
            autoCompleteCiudaDestino.setError("Campo Requerido");
        }

        fechaViajeBD.trim();
        if(TextUtils.isEmpty(fechaViajeBD)){
            esValido=false;
            fechaViaje.setError("Campo Requerido");
        }

        horaviajeDB.trim();
        if(TextUtils.isEmpty(horaviajeDB)){
            esValido=false;
            horaViaje.setError("Campo Requerido");
        }
        cuposDB.trim();
        if(TextUtils.isEmpty(cuposDB)){
            esValido=false;
            cupos.setError("Campo Requerido");
        }
        costoDB.trim();
        if(TextUtils.isEmpty(costoDB)){
            esValido=false;
            costo.setError("Campo Requerido");
        }
        return esValido;
    }

    private String consultarConductor(){
        mDatabase.child("Conductor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    numeroDocumentoDB = snapshot.child(idUserBD).child("numeroDocumento").getValue().toString();

                }
                else {
                    Toast.makeText(CrearServicio.this,"Error Consulta  "+ idUserBD, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return numeroDocumentoDB;
    }

    private void crearViaje(){
        fechaHora();
        Map<String, Object> map = new HashMap<>();
        map.put("origen", ubicacionOrigenDB);
        map.put("destino", ubicacionDestinoDB);
        map.put("fecha", fechaViajeBD);
        map.put("hora", horaviajeDB);
        map.put("cupos", cuposDB);
        map.put("costo", costoDB);
        map.put("estado",ESTADO);
        map.put("conductor",idUserBD);
        map.put("pasajero1","");

        mDatabase.child("Servicio").child(idFechaHora).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CrearServicio.this,"Servicio Creado exitosamente... ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CrearServicio.this, InformacionViajemos.class);
                                intent.putExtra("id", idUserBD);
                                intent.putExtra("idFechaHora", idFechaHora);
                                startActivity(intent);
                            }else{
                                Toast.makeText(CrearServicio.this,"Error en la creación del Servicio ", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
    }

    private void fechaHora(){
        String mytime = (DateFormat.format("yyyyMMddhhmmss", new java.util.Date()).toString());
        idFechaHora =  mytime;

    }

}