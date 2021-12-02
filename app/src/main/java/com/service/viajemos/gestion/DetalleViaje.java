package com.service.viajemos.gestion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.service.viajemos.R;
import com.service.viajemos.pasajero.PerfilPasajero;

public class DetalleViaje extends AppCompatActivity {
    ImageButton imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_viaje);

        imgHome = (ImageButton) findViewById(R.id.imgHome);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleViaje.this, InformacionViajemos.class);
                startActivity(intent);
            }
        });
    }
}