package com.example.peidosaguacelica106.Clases;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.peidosaguacelica106.Adaptadores.AdapterPedidos;
import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Datos.DatosPadre;
import com.example.peidosaguacelica106.R;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Pedidos extends AppCompatActivity  {
    private AdapterPedidos adapterPedidos;
    ArrayList<Datos> list;
    RecyclerView recyclerView;
    TextView pendientes;
    List<DatosPadre> sectionList=new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    public int n_pendiente = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pendientes = findViewById(R.id.pendientes_final);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Administracion/Pedidos");
        // Query myRef1 = database.getReference("Administracion/Pedidos");

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();


/*        if(!getIntent().getSerializableExtra("pedido").toString().equals("null")){
            Log.d("valorintent",getIntent().getSerializableExtra("pedido").toString());
            pedidos1= (Datos) getIntent().getSerializableExtra("pedido");
            myRef.push().setValue(pedidos1);
        }*/

        // Query dataQuery
        //myRef.orderByChild("estado").startAt(false).endAt(true);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        Datos datos = dataSnapshot1.getValue(Datos.class);

                        assert datos != null;
                        datos.setIdPedido(dataSnapshot1.getKey());

                        Log.d("Ejemplo Firebase", "Valor: " + datos.getIdPedido());

                        if (!datos.getEstado()) {
                            n_pendiente++;
                        }

                        list.add(datos);

                        Log.d("Firebase", "Valor: " + datos.getEstado());

                    }
                    Collections.sort(list, new ComparadorDatosEstado());
                    adapterPedidos.notifyDataSetChanged();
                    if (n_pendiente == 1) {
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

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        adapterPedidos = new AdapterPedidos(list, this);
        recyclerView.setAdapter(adapterPedidos);

    }


    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position1=viewHolder.getBindingAdapterPosition();

            // Take action for the swiped item
            switch (direction){
                case ItemTouchHelper.LEFT:
                    String pdfname="";

                    CrearFactura crearFactura=new CrearFactura();
                    String fecha=list.get(position1).getFecha().toString();
                    String nombre=list.get(position1).getNombre().toString();
                    fecha=fecha.replace(":","").replace("-","");
                    nombre=nombre.replace(" ","");
                    pdfname=nombre+fecha+".pdf";

                    try {
                        crearFactura.createPDF(pdfname,fecha,getApplicationContext(),list,position1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //enviar_whatsapp_factur(list.get(position1).getCelular(),"a.pdf");
                    adapterPedidos.notifyDataSetChanged();



                    break;

                case ItemTouchHelper.RIGHT:
                    String phoneNo = list.get(position1).getCelular();
                    String dial = "tel:" + phoneNo;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                    adapterPedidos.notifyDataSetChanged();

                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(Pedidos.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Pedidos.this, R.color.izquierdo))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_picture_as_pdf_24)
                    .addSwipeLeftLabel("Generar Recibo PDF")
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Pedidos.this, R.color.derecho))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_call_24)
                    .addSwipeRightLabel("Llamar")
                    .setActionIconTint(ContextCompat.getColor(recyclerView.getContext(), android.R.color.white))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

/*    public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(Pedidos.this, R.color.derecho))
                .addActionIcon(R.drawable.ic_baseline_call_24)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menupedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ordenar_nombre:
                Ordenar_nombre();
                return true;
            case R.id.ordenar_fecha:
                Ordenar_fecha();
                return true;
            case R.id.ordenar_estado:
                Ordenarestado();
                return true;
            case R.id.actualizar:
                reiniciarActivity();
                return true;
            case R.id.modotv:
                 Modo_TV();
                 return true;
            case R.id.exportar_pdf:
                Toast.makeText(this, "PROXIMAMENTE", Toast.LENGTH_LONG).show();
                CrearFactura crearFactura=new CrearFactura();
                List list_hoy=new ArrayList(){};
                String fechaHOY = "2015-02-01";

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

//                Date hoy = formato.parse(fechaHOY);
                //for(int i=0;i<list.size();i++){

               // }

                try {
                   crearFactura.createPDF("prueba","reporte_diario.pdf",this,null,0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private class ComparadorDatosNombre implements Comparator<Datos> {

        @Override
        public int compare(Datos datos, Datos t1) {
            return  datos.getNombre().compareTo(t1.getNombre());
        }
    }
    private class ComparadorDatosEstado implements Comparator<Datos>{
        @Override
        public int compare(Datos datos, Datos t1) {
            return  datos.getEstado().compareTo(t1.getEstado());
         }
    }

    public void reiniciarActivity(){
        Collections.sort(list,new ComparadorDatosEstado());
        reiniciar_lista();
        }
    private void Ordenarestado(){
        Collections.sort(list,new ComparadorDatosEstado());
        reiniciar_lista();
    }

    private void Ordenar_fecha() {
        Collections.sort(list,new ComparadorDatosFecha());
        reiniciar_lista();
    }

    private void Ordenar_nombre() {
        Collections.sort(list,new ComparadorDatosNombre());
        reiniciar_lista();
    }

    private void reiniciar_lista(){

        adapterPedidos.notifyDataSetChanged();
    }




    //Check READ/WRITE PERMISSION
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
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

    private void Modo_TV() {

        Intent intent = new Intent(Pedidos.this, ModoTV.class);
        startActivity(intent);
    }

    private void enviar_whatsapp_factur(String numerocliente ,String path){

        File outputfile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),path);
        Uri uri=Uri.fromFile(outputfile);

        String message="Estimeado cliente gracias por tu prefencia.\n" +
                "En el pdf encontraras un recibo de tu compra\n"+
                "Un excelente dia";

        String numero=numerocliente;
        numero=numero.replace("+","").replace(" ","");



         Intent i = new Intent("android.intent.action.MAIN");
         i.putExtra("jid",numero+"@s.whatsapp.net");
         i = new Intent(Intent.ACTION_SEND);
         i.putExtra(Intent.EXTRA_TEXT,message);
         i.setPackage("com.whatsapp");
         //i.setType("text/plain");
        i.setType("application/pdf");
        i.putExtra(Intent.EXTRA_STREAM,uri);

        this.startActivity(i);

    }
}

