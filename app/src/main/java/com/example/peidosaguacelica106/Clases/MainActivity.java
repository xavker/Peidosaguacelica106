package com.example.peidosaguacelica106.Clases;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetik.Alphabetik;
import com.example.peidosaguacelica106.Adaptadores.MyAdapter;
import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Provider.AuthProvider;
import com.example.peidosaguacelica106.Provider.GeofireProvider;
import com.example.peidosaguacelica106.Provider.TokenProvider;
import com.example.peidosaguacelica106.R;
import com.example.peidosaguacelica106.services.ConexionInternet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    MyAdapter myAdapter;

    ArrayList<Datos> list;
    RecyclerView recyclerView;
    SearchView buscar;
    TextView n_clientes;

    Datos datos;
    Button agreagar;
    private TokenProvider mtoknProvider;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private LatLng mCurrentLatLng;
    private List<Marker> mDriversMarker=new ArrayList<>();
    private GeofireProvider mGeofireprovider;
    private AuthProvider authProvider;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private String[] orderednombres = null;

    LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //obtiene la localiacionn en tiempo real
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    updateLocation();
                }
            }
        }
    };

    private void updateLocation() {

        mGeofireprovider.saveLocation("camion",mCurrentLatLng);

    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {}

        // Write a message to the database
        mtoknProvider=new TokenProvider();
        mGeofireprovider=new GeofireProvider();
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        myAdapter=new MyAdapter(list,this);
        recyclerView.setAdapter(myAdapter);

        buscar=findViewById(R.id.buscar);
        agreagar=findViewById(R.id.buttonagregar);
        n_clientes=findViewById(R.id.clientes);
        authProvider=new AuthProvider();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Administracion/Clientes");
        //database.setPersistenceEnabled(true);
        //myRef.setValue("Hello, World!");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        datos=dataSnapshot1.getValue(Datos.class);
                        datos.setId(dataSnapshot1.getKey());
                        list.add(datos);
                       // Log.d(TAG, "Value is: " + datos.getNombre());


                    }
                    orderednombres=new String[list.size()];
                    for(int i=0;i<list.size();i++){
                        orderednombres[i]=list.get(i).getNombre();
                       //Log.i("View: ",list.get(i).getNombre());
                    }
                    n_clientes.setText(String.valueOf("Existen un total de "+dataSnapshot.getChildrenCount())+" clientes en la base de datos");
                    myAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        Alphabetik alphabetik = (Alphabetik) findViewById(R.id.alphSectionIndex);
        alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
            @Override
            public void onItemClick(View view, int position, String character) {
                String info = " Position = " + position + " Char = " + character;
                Log.i("View: ", view + "," + info);
                //Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                recyclerView.smoothScrollToPosition(getPositionFromData(character));
            }
        });


        agreagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarcliente();

            }
        });
        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscarcliente(s);
                return true;
            }
        });
        mFusedLocation= LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);


        startLocation();
        generteToken();
    }
    private int getPositionFromData(String character) {

        int position = 0;
        for (String s : orderednombres) {
            String letter = "" + s.charAt(0);
            if (letter.equals("" + character)) {
                return position;
            }
            position++;
        }
        return 0;
    }
    private void buscarcliente(String s) {
        ArrayList<Datos>busqueda=new ArrayList<>();

        for(Datos d:list){
            Log.d("busqueda", s);
            Log.d("busqueda", d.getNombre());
            if(d.getNombre().toLowerCase().contains(s.toLowerCase())){
                busqueda.add(d);
            }


        }
        MyAdapter adapterbusqueda=new MyAdapter(busqueda,this);
        recyclerView.setAdapter(adapterbusqueda);
    }


    public void agregarcliente() {
        DialogodePedido dialogodePedido = new DialogodePedido(this);
        dialogodePedido.agregarcliente(null);

    }

    //compartir ubicacion en tiempo real

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else {
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }


    private void disconnect() {

        if (mFusedLocation != null) {
            //mButtonConnect.setText("Conectarse");
            //mIsConnect = false;
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            // if (mAuthProvider.existSession()) {
            //    mGeofireProvider.removeLocation(mAuthProvider.getId());
            // }
        }
        else {
            Toast.makeText(this, "No te puedes desconectar", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    //mButtonConnect.setText("Desconectarse");
                    //mIsConnect = true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }


    public void generteToken(){
        mtoknProvider.create(authProvider.getId());
        //Toast.makeText(this,authProvider.getId(),Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                CerrarSecion();
                return true;
            case R.id.help:
                Acercade();
                return true;
            case R.id.pedidos:
                Pantalla_pedido();
                return true;
            case R.id.factura:
                Facturar();
                return true;
            case R.id.actualizar:
                actualizar_contactos(list);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressLint("Range")
    private void actualizar_contactos(ArrayList<Datos> list) {
        Map<Long, List<String>> contacts = new HashMap<Long, List<String>>();
        ContentResolver cr=getApplicationContext().getContentResolver();
        String[] projection = {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1, ContactsContract.Data.DATA2, ContactsContract.Data.DATA3};
        String selection = ContactsContract.Data.MIMETYPE + " IN ('" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "')";
        Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null);
        List<Datos> contactosnuevos=new ArrayList<>();

        while (cur != null && cur.moveToNext()) {
            long id = cur.getLong(0);
            String name = cur.getString(1);
            String mime = cur.getString(2); // type of data (e.g. "phone")
            String data = cur.getString(3); // the actual info, e.g. +1-212-555-1234
            int type = cur.getInt(4); // a numeric value representing type: e.g. home / office / personal
            String label = cur.getString(5); // a custom label in case type is "TYPE_CUSTOM"

            //String labelStr = Phone.getTypeLabel(getResources(), type, label);
            //Log.d("CONTACTOS", "got " + id + ", " + name +  " - " + data + " ("  + type+")"+"-"+label+"-"+mime);

            // add info to existing list if this contact-id was already found, or create a new list in case it's new
            //contactosnuevos.add(new Datos(name,data));
            contactosnuevos.add(new Datos(null,false,0.0,null,data,null,null,null,null,name,null,null,null));

            Log.d("CONTACTOS", "lista de contactos nuevos: "+contactosnuevos.size());
            Log.d("CONTACTOS", "lista de contactos antiguos: "+list.size());

        }
        //List<Datos> contactos_actualizados = contactosnuevos.stream().filter(p -> !list.contains(p)).collect(Collectors.toList());
        List<Datos> contactos_actualizados =new ArrayList<>();
       // contactos_actualizados=contactosnuevos;
        for(int i=0;i<contactosnuevos.size();i++){
            for(int j=0;j<list.size();j++){
                if(contactosnuevos.get(i).getCelular().equals(list.get(j).getCelular())){
                    int l=contactos_actualizados.size();
                    contactos_actualizados.add(new Datos("",false,list.get(j).getVendidos(),"",list.get(j).getCelular(),list.get(j).getDireccion(),list.get(j).getFecha(),list.get(j).getLat(),list.get(j).getLng(), list.get(j).getNombre(),"",list.get(j).getId(),""));
                    Log.d("CONTACTOS", contactos_actualizados.get(l).getIdPedido()
                                    +" "+contactos_actualizados.get(l).getEstado()
                                    +" "+ contactos_actualizados.get(l).getVendidos()
                                    +" "+contactos_actualizados.get(l).getPedidos()
                                    +" "+contactos_actualizados.get(l).getCelular()
                                    +" "+contactos_actualizados.get(l).getDireccion()
                                    +" "+contactos_actualizados.get(l).getFecha()
                                    +" "+contactos_actualizados.get(l).getLat()
                                    +" "+contactos_actualizados.get(l).getLng()
                                    +" "+contactos_actualizados.get(l).getNombre()
                                    +" "+contactos_actualizados.get(l).getNota()
                                    +" "+contactos_actualizados.get(l).getId());


                    j=list.size();

                }else{
                    if(j>list.size()-2){
                        //contactos_actualizados.add(new Datos(null,false,list.get(j).getVendidos(),null,list.get(j).getCelular(),list.get(j).getDireccion(),list.get(j).getFecha(),list.get(j).getLat(),list.get(j).getLng(), list.get(j).getNombre(),null,list.get(j).getId()));
                        contactos_actualizados.add(new Datos("",false,0.0,"",contactosnuevos.get(i).getCelular(),"Sin direccion","","","",contactosnuevos.get(i).getNombre(),"","",""));
                        int k=contactos_actualizados.size()-1;
                        Log.d("CONTACTOS",
                                " "+contactos_actualizados.get(k).getIdPedido()
                                + " "+contactos_actualizados.get(k).getEstado()
                                +" "+ contactos_actualizados.get(k).getVendidos()
                                +" "+contactos_actualizados.get(k).getPedidos()
                                +" "+contactos_actualizados.get(k).getCelular()
                                +" "+contactos_actualizados.get(k).getDireccion()
                                +" "+contactos_actualizados.get(k).getFecha()
                                +" "+contactos_actualizados.get(k).getLat()
                                +" "+contactos_actualizados.get(k).getLng()
                                +" "+contactos_actualizados.get(k).getNombre()
                                +" "+contactos_actualizados.get(k).getNota()
                                +" "+contactos_actualizados.get(k).getId());
                    }
                }
            }
        }
        Log.d("CONTACTOS", "lista de contactos nuevos: "+contactosnuevos.size());
        Log.d("CONTACTOS", "lista de contactos actualizados: "+contactos_actualizados.size());

        final androidx.appcompat.app.AlertDialog.Builder dialog=new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setTitle("Actualizacion de nuevos clientes");
        int diferencia=contactos_actualizados.size()-list.size();
        dialog.setMessage("Debes ser administrador para ingresar nuevos datos a la base de datos, debido a la segmentacion de clientes y estar seguros que todos los clientes son locales. \n" +
                " Clientes Antiguos: "+list.size()+".\n" +
                " Cleintes Nuevos: "+diferencia+".\n\n");
        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.password,null);
        EditText password=v.findViewById(R.id.password1);


        dialog.setView(v);

        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(password.getText().toString().equals("2021")){
                    Toast.makeText(getApplicationContext(),"Contraseña ingrasada corectamente",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Error de contraseña",Toast.LENGTH_LONG).show();


                }
            }
        });
        dialog.setNegativeButton("Descartar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

    }

    private void Pantalla_pedido() {

        Intent intent = new Intent(MainActivity.this, Pedidos.class);
        intent.putExtra("pedido","null");
        startActivity(intent);
    }

    private void Acercade() {
        Intent intent = new Intent(MainActivity.this, Splash.class);
        //intent.putExtra("pedido","null");
        startActivity(intent);
        //Toast.makeText(this," Hello World \n 0979724195",Toast.LENGTH_LONG).show();

    }
    private void Facturar() {
        Intent intent = new Intent(MainActivity.this, FacturaSRI.class);
        //intent.putExtra("pedido","null");
        startActivity(intent);
        //Toast.makeText(this," Hello World \n 0979724195",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {

        if(ConexionInternet.isOline(this)){
            super.onResume();
        }else{
            super.onResume();
            Snackbar.make(findViewById(R.id.princupal),"Revise la conexion a Internet.",Snackbar.LENGTH_LONG).show();


        }
    }

    private void CerrarSecion() {
        authProvider.logout();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
