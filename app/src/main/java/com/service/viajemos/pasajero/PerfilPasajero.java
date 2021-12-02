package com.service.viajemos.pasajero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.service.viajemos.R;
import com.service.viajemos.gestion.CrearServicio;
import com.service.viajemos.gestion.InformacionViajemos;

public class PerfilPasajero extends AppCompatActivity {

    ImageButton imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pasajero);

        imgHome = (ImageButton) findViewById(R.id.imgHome);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilPasajero.this, InformacionViajemos.class);
                startActivity(intent);
            }
        });

    }
}