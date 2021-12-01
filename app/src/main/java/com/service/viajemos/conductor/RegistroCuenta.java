package com.service.viajemos.conductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.service.viajemos.R;
import com.service.viajemos.gestion.InformacionViajemos;
import com.service.viajemos.login.RegistrarUsuarioPpal;

import java.util.HashMap;
import java.util.Map;

public class RegistroCuenta extends AppCompatActivity {

    private String PASAJERO = "Pasajero";
    private String CONDUCTOR = "Conductor";
    String idUserBD, numeroDocumentoBD, nombreBD, fechanacimientoBD, celularBD, correoBD, marcaCarroBD, placaBD, modeloBD, colorBD, generoBD, tipoDocumentoBD, perfilBD;
    Spinner tipodomento;
    TextView numeroDocumento, nombre, fechanacimiento, celular, correo, marcaCarro, placa, modelo, color, genero;
    Button btnRegistrar ;

    Bundle datosUsuario;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cuenta);

        numeroDocumento = (TextView) findViewById(R.id.txtDocumentoRegistro);
        nombre = (TextView) findViewById(R.id.txtNombreRegistro);
        fechanacimiento = findViewById(R.id.txtFechaNacimientoRegistro);
        celular =  (TextView) findViewById(R.id.txtTelefonoRegistro);
        correo = (TextView) findViewById(R.id.txtCorreoRegistro);
        marcaCarro = (TextView) findViewById(R.id.txtMarcaVehiculo);
        placa = (TextView) findViewById(R.id.txtPlaca);
        modelo = (TextView) findViewById(R.id.txtModelo);
        color = (TextView) findViewById(R.id.txtColor);
        genero =  (TextView) findViewById(R.id.txtGeneroRegistro);
        btnRegistrar = (Button) findViewById(R.id.btnRegistroCuenta);

        datosUsuario = getIntent().getExtras();
        idUserBD = datosUsuario.getString("id");
        correoBD = datosUsuario.getString("correo");
        perfilBD = datosUsuario.getString("perfil");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        if(perfilBD.equals(PASAJERO)){
            marcaCarro.setVisibility(View.GONE);
            placa.setVisibility(View.GONE);
            modelo.setVisibility(View.GONE);
            color.setVisibility(View.GONE);
        }

        correo.setText(correoBD);

        tipodomento = (Spinner) findViewById(R.id.spinTipoDocumento);

        ArrayAdapter<CharSequence>  adapter = ArrayAdapter.createFromResource(this,R.array.tipodocumento, android.R.layout.simple_spinner_item);
        tipodomento.setAdapter(adapter);
        tipodomento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario, password;
                numeroDocumentoBD = numeroDocumento.getText().toString();
                nombreBD = nombre.getText().toString();
                fechanacimientoBD = fechanacimiento.getText().toString();
                celularBD = celular.getText().toString();
                correoBD = correo.getText().toString();
                marcaCarroBD = marcaCarro.getText().toString();
                placaBD = placa.getText().toString();
                modeloBD = modelo.getText().toString();
                colorBD = color.getText().toString();
                generoBD = genero.getText().toString();
                tipoDocumentoBD = tipodomento.getSelectedItem().toString();

                if(perfilBD.equals(PASAJERO)){
                    registrarPasajero();
                    registrarCuenta();
                }
                if(perfilBD.equals(CONDUCTOR)){
                    registrarConductor();
                    registrarCuenta();
                    registrarVehiculo();
                }
            }
        });
    }

    private void registrarConductor(){
        progressDialog.setMessage("Realizando registro en Línea...");
        progressDialog.show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("fechaNacimiento", fechanacimientoBD);
                    map.put("genero", generoBD);
                    map.put("nombre", nombreBD);
                    map.put("numeroDocumento", numeroDocumentoBD);
                    map.put("tipoDocumento", tipoDocumentoBD);


                    mDatabase.child("Conductor").child(idUserBD).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Intent intent = new Intent(RegistroCuenta.this, InformacionViajemos.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(RegistroCuenta.this,"No se pudo crear usuario en la BD", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
    }

    private void registrarVehiculo(){
        progressDialog.setMessage("Realizando registro en Línea...");
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("color", colorBD);
        map.put("marca", marcaCarroBD);
        map.put("modelo", modeloBD);
        map.put("placa", placaBD);

        mDatabase.child("Vehiculo").child(idUserBD).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){
                    Intent intent = new Intent(RegistroCuenta.this, InformacionViajemos.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegistroCuenta.this,"No se pudo crear usuario en la BD", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registrarCuenta(){
        progressDialog.setMessage("Realizando registro en Línea...");
        progressDialog.show();

        String fecha = fecha();

        Map<String, Object> map = new HashMap<>();
        map.put("celular", celularBD);
        map.put("fechaInscripcion",fecha);
        map.put("perfil", perfilBD);
        map.put("usuario", correoBD);

        mDatabase.child("Cuenta").child(idUserBD).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){
                    Intent intent = new Intent(RegistroCuenta.this, InformacionViajemos.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegistroCuenta.this,"No se pudo crear usuario en la BD", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String fecha(){
        String mytime = (DateFormat.format("yyyyMMdd", new java.util.Date()).toString());
        return mytime;
    }

    private String hora(){
        String mytime = (DateFormat.format("hhmm", new java.util.Date()).toString());
        return mytime;
    }

    private void registrarPasajero(){
        progressDialog.setMessage("Realizando registro en Línea...");
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("fechaNacimiento", fechanacimientoBD);
        map.put("genero", generoBD);
        map.put("nombre", nombreBD);
        map.put("numeroDocumento", numeroDocumentoBD);
        map.put("tipoDocumento", tipoDocumentoBD);


        mDatabase.child("Pasajero").child(idUserBD).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){
                    Intent intent = new Intent(RegistroCuenta.this, InformacionViajemos.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegistroCuenta.this,"No se pudo crear usuario en la BD", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}