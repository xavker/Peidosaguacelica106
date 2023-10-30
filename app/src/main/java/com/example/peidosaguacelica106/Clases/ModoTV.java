package com.example.peidosaguacelica106.Clases;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.peidosaguacelica106.Adaptadores.AdapterPedidos;
import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Provider.GeofireProvider;
import com.example.peidosaguacelica106.R;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ModoTV extends FragmentActivity implements OnMapReadyCallback {
    AdapterPedidos myAdapter;
    ArrayList<Datos> list;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    ArrayList<Marker> tmRealtimeMarker = new ArrayList<>();
    ArrayList<Marker> RealtimeMarker = new ArrayList<>();
    private LatLng celica;
    private GeofireProvider geoFireProvider;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LatLng mcurrentLatLng;
    private List<Marker> mDriversMarker=new ArrayList<>();

    private MarkerOptions markerOptions = new MarkerOptions();
    private TextView pendientes;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private  boolean mIsFiirtsTime=true;
    private String ubicacion=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_tv);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        geoFireProvider = new GeofireProvider();
        recyclerView = findViewById(R.id.recycler);

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Administracion/Pedidos");

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new AdapterPedidos( list,this);
        recyclerView.setAdapter(myAdapter);

        pendientes = findViewById(R.id.pedientes);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        celica = new LatLng(-4.10252710889623, -79.95616104822938);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);


        mLocationRequest = new LocationRequest();
        //tiempo en q se actualiza en el mapa
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (Marker marker : RealtimeMarker) {
                        marker.remove();
                    }
                    int n_pendiente = 0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Datos datos = snapshot1.getValue(Datos.class);

                        assert datos != null;
                        datos.setIdPedido(snapshot1.getKey());
                        Log.d("Ejemplo Firebase", "Valor: " + datos.getIdPedido());

                        ubicacion=datos.getLat();
                        if (!datos.getEstado()) {
                            n_pendiente++;

                            if(ubicacion!=null){
                                if(ubicacion.length()>0){
                                    double longitud1 = Double.valueOf(datos.getLng());
                                    double latitud1 = Double.valueOf(datos.getLat());
                                    markerOptions.position(new LatLng(latitud1, longitud1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario));
                                    markerOptions.title(datos.getNombre());
                                    tmRealtimeMarker.add(mMap.addMarker(markerOptions));
                                    Log.d("Ejemplo Firebase", "Valor: " + ubicacion);
                                }
                            }
                        }
                        list.add(datos);

                        Log.d("Ejemplo Firebase", "Valor: " + datos.getNombre());
                    }
                    Collections.sort(list, new ComparadorDatosEstado());


                    //String value = snapshot.getValue(String.class);
                    myAdapter.notifyDataSetChanged();

                    //RealtimeMarker.clear();
                    //RealtimeMarker.addAll(tmRealtimeMarker);
                    if (n_pendiente == 0) {
                        pendientes.setText("Existen " +
                                n_pendiente +
                                " pedido pendientes " +
                                snapshot.getChildrenCount());}
                    else if (n_pendiente == 1) {
                        pendientes.setText("Existen " +
                                n_pendiente +
                                " pedido pendiente de un total de " +
                                snapshot.getChildrenCount());
                    } else {
                        pendientes.setText("Existen " +
                                n_pendiente +
                                " pedidos pendientes de un total de " +
                                snapshot.getChildrenCount());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "no encontrado");
            }
        });
        if(mIsFiirtsTime){
            mIsFiirtsTime=false;
            getctiveDrivers();
        }

        mMap.addMarker(new MarkerOptions()
                .position(celica)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop))
                .title("Celica"));


        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(celica)
                        .zoom(16f)
                        .build()
        ));

    }
    public  void getctiveDrivers(){
        //celica direccion de referencia desde el punto del cual se busca a los conductors
        geoFireProvider.getActiveDriver(celica).addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                for(Marker marker:mDriversMarker){
                    if(marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            return;
                        }
                    }
                }
                LatLng driverlatlng=new LatLng(location.latitude,location.longitude);
                Marker marker=mMap.addMarker(new MarkerOptions().position(driverlatlng).title("GOE-0090").icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_delivery_64)));
                marker.setTag(key);
                mDriversMarker.add(marker);


            }

            @Override
            public void onKeyExited(String key) {
                for(Marker marker:mDriversMarker){
                    if(marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            marker.remove();
                            mDriversMarker.remove(marker);
                            return;
                        }

                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for(Marker marker:mDriversMarker){
                    if(marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            Log.d("conductores","usuario: "+key+
                                    "longitude:  "+ String.valueOf(location.longitude)+
                                    "latitude: " + String.valueOf(location.latitude)+
                                    "tag: "+marker.getTag());
                            marker.setPosition(new LatLng(location.latitude,location.longitude));
                            return;
                        }

                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private void Ordenar_fecha() {
        Collections.sort(list,new ComparadorDatosFecha());
        reiniciar_lista();
    }

    private void Ordenarestado(){
        Collections.sort(list,new ComparadorDatosEstado());
        reiniciar_lista();
    }
    private void reiniciar_lista(){

        myAdapter.notifyDataSetChanged();
    }
    private class ComparadorDatosEstado implements Comparator<Datos> {
        @Override
        public int compare(Datos datos, Datos t1) {
            return  datos.getEstado().compareTo(t1.getEstado());
        }
    }

    private class ComparadorDatosFecha implements Comparator<Datos> {

        @Override
        public int compare(Datos t1, Datos datos) {
            Date DateObject1 = StringToDate(datos.getFecha());
            Date DateObject2 = StringToDate(t1.getFecha());

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(DateObject1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(DateObject2);

            int month1 = cal1.get(Calendar.MONTH);
            int month2 = cal2.get(Calendar.MONTH);



            if (month1 < month2)
                return -1;
            else if (month1 == month2)
                return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);

            else return 1;

        }
    }
    public static Date StringToDate(String theDateString) {
        Date returnDate = new Date();
        if (theDateString.contains("-")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy ");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return returnDate;
    }
    public Bitmap bitmapSizeByScall(Bitmap bitmapIn, float scall_zero_to_one_f) {
        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn, Math.round(bitmapIn.getWidth() * scall_zero_to_one_f), Math.round(bitmapIn.getHeight() * scall_zero_to_one_f), false);
        return bitmapOut; }
}