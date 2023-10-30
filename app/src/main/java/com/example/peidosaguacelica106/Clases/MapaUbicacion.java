package com.example.peidosaguacelica106.Clases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapaUbicacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private GoogleMap gmap;
    private SupportMapFragment supportMapFragment;
    Button aceptar, cancelar;
    LatLng latLng_seleccionado;
    DatabaseReference myRef;
    Datos agregaubicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ubicacion);

        agregaubicacion=new Datos();
        latLng_seleccionado=null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         myRef = database.getReference("Administracion/Clientes");

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        aceptar = findViewById(R.id.cancelar);
        cancelar=findViewById(R.id.cancelar);

        agregaubicacion= (Datos) getIntent().getSerializableExtra("pedido");




        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapaUbicacion.this, MainActivity.class);
                startActivity(intent);
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubcaion();
                Intent intent = new Intent(MapaUbicacion.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void ubcaion() {
        if(latLng_seleccionado!=null){
            agregaubicacion.setLat(String.valueOf(latLng_seleccionado.latitude));
            agregaubicacion.setLng(String.valueOf(latLng_seleccionado.longitude));
            myRef.child(agregaubicacion.getId()).setValue(agregaubicacion);
            Toast.makeText(MapaUbicacion.this,"Agregado ubicación de "+agregaubicacion.getNombre(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MapaUbicacion.this,"No se actualizo ubicación",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng celica = new LatLng(-4.102319,-79.9564921);


        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(celica, 16));

        gmap.addMarker(new MarkerOptions()
                .title("Celica")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_water_cooler_50))
                .snippet("Agua Celica.")
                .position(celica));

        gmap.setOnMapClickListener(this);
        gmap.setOnMapLongClickListener(this);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(MapaUbicacion.this,
                "onMapClick:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        //gmap.clear();

        Toast.makeText(MapaUbicacion.this,
                "onMapLongClick:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

        //Add marker on LongClick position
        MarkerOptions markerOptions =new MarkerOptions().position(latLng).title(latLng.toString());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_map_65));
        gmap.addMarker(markerOptions);

        latLng_seleccionado=latLng;
    }
}