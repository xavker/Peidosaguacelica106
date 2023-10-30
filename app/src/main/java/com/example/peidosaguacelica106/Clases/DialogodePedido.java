package com.example.peidosaguacelica106.Clases;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;



import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Datos.FCMBody;
import com.example.peidosaguacelica106.Datos.FCMResponse;
import com.example.peidosaguacelica106.Provider.NotificationProvider;
import com.example.peidosaguacelica106.Provider.ProviderPedido;
import com.example.peidosaguacelica106.Provider.TokenProvider;
import com.example.peidosaguacelica106.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import org.apache.harmony.awt.datatransfer.DataSnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogodePedido {

    Context context;
    float valorbidones,valorpacas600,valorpacas1000,valorgalones,valorgalones10;
    Button  buttonbllmas,bttonbllmeno,botonaceptar,botoncancelar,buttonbmas,bttonbmeno,button600mas,button600menos,button1000mas,button1000menos, buttongalonmas,buttongalonmenos,buttongalones10mas,buttongalones10menos;
    TextView botellonesll,botellones, b600ml, b1000ml , galones,total,galones10;
    Spinner spinner,spinnerll,spinner_tipo_botellon,spinner_barrios,spinnerp600;
    Float valorb0,valorb1;
    Float p0,p1;
    Float p2;
    Float p3;
    Float p4;
    Float p5;
    String t;
    String vendidos,pedidos,b0,b1,b2,b3,b4,b5;
    DecimalFormat f;
    PedidosyVendidos pedidosyVendidos;
    ProviderPedido provider_pedido;
    public NotificationProvider notificationProvider;
    public TokenProvider tokenProvider;
    private AlertDialog.Builder dialog;

    public DialogodePedido( Context context) {

        this.context=context;
        valorbidones= 1.5F;
        valorpacas600=4.8F;
        valorpacas1000=6.00F;
        valorgalones=0.9F;
        valorgalones10=1.25F;
        valorb1=1.5f;
        valorb0=1.5f;
        vendidos="0";
        pedidos="0";
        pedidosyVendidos=new PedidosyVendidos();
        notificationProvider=new NotificationProvider();
        tokenProvider=new TokenProvider();
        provider_pedido=new ProviderPedido();
        dialog=new AlertDialog.Builder(context);
        f=new DecimalFormat("##.00");

    }



    public void agregarcliente(Datos datos)  {



        dialog.setTitle("Pantalla de pedido");
        dialog.setMessage("Revisa el pedido antes de aceptar");
        LayoutInflater inflater=LayoutInflater.from(context);
        View regis=inflater.inflate(R.layout.agregarclientepedido,null);

        final EditText nombre=regis.findViewById(R.id.nombrea);
        //final EditText direccion=regis.findViewById(R.id.direcciona);
        final EditText celular=regis.findViewById(R.id.celulara);
        final EditText pedido=regis.findViewById(R.id.pedidoa);
        buttonbllmas=regis.findViewById(R.id.buttonBllmas);
        bttonbllmeno=regis.findViewById(R.id.buttonBllmenos);
        buttonbmas=regis.findViewById(R.id.buttonBmas);
        bttonbmeno=regis.findViewById(R.id.buttonBmenos);
        button600mas=regis.findViewById(R.id.button600mlmas);
        button600menos=regis.findViewById(R.id.button600mlmenos);
        button1000mas=regis.findViewById(R.id.button1000mlmas);
        button1000menos=regis.findViewById(R.id.button1000mlmenos);
        buttongalonmas=regis.findViewById(R.id .button5lmas);
        buttongalonmenos=regis.findViewById(R.id.buttongalonesmenos);
        buttongalones10mas=regis.findViewById(R.id.button10lmas);
        buttongalones10menos=regis.findViewById(R.id.buttongalones10menos);

        botellonesll=regis.findViewById(R.id.botellonesll);
        botellones=regis.findViewById(R.id.botellones);
        b600ml=regis.findViewById(R.id.pacas600);
        b1000ml=regis.findViewById(R.id.pacas1000);
        galones=regis.findViewById(R.id.galones);
        galones10=regis.findViewById(R.id.galones10);
        spinner=regis.findViewById(R.id.spinner);
        spinnerll=regis.findViewById(R.id.spinnerbll);
        spinnerp600=regis.findViewById(R.id.spinnerp600);

        spinner_barrios=regis.findViewById(R.id.direcciona);
        total=regis.findViewById(R.id.totalpedido);

        botonaceptar=regis.findViewById(R.id.baceptar);
        botoncancelar=regis.findViewById(R.id.bcancelar);



       // ArrayAdapter<CharSequence> arrayAdapter0=ArrayAdapter.createFromResource(context,R.a, android.R.layout.simple_spinner_item);
       // arrayAdapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Resources res = context.getResources();
        String[]aux1=res.getStringArray(R.array.barrios);
        spinner_barrios.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,aux1));
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(context,R.array.preciobotellones, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerll.setAdapter(arrayAdapter);

        arrayAdapter=ArrayAdapter.createFromResource(context,R.array.preciop600, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerp600.setAdapter(arrayAdapter);


        if(datos!=null){

            nombre.setText(datos.getNombre());
            celular.setText(datos.getCelular());
           // spinner_barrios.set;
            String[]aux2=ordenarbarrio(datos.getDireccion());
            spinner_barrios.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,aux2));
        }

        buttonbllmas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String aux=botellonesll.getText().toString();
                botellonesll.setText(String.valueOf(Integer.parseInt(aux)+1));
                valorb1=Float.parseFloat(spinnerll.getSelectedItem().toString());
                valortotal();
            }
        });
        bttonbllmeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(botellonesll.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                valorb1=Float.parseFloat(spinnerll.getSelectedItem().toString());
                botellonesll.setText(String.valueOf(aux-1));
                valortotal();


            }
        });
        buttonbmas.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String aux=botellones.getText().toString();
                botellones.setText(String.valueOf(Integer.parseInt(aux)+1));
                valorb0=Float.parseFloat(spinner.getSelectedItem().toString());
                valortotal();
            }
        });
        bttonbmeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(botellones.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                valorb0=Float.parseFloat(spinner.getSelectedItem().toString());
                botellones.setText(String.valueOf(aux-1));
                valortotal();


            }
        });

        button600mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux=b600ml.getText().toString();
                b600ml.setText(String.valueOf(Integer.parseInt(aux)+1));
                valorpacas600=Float.parseFloat(spinnerp600.getSelectedItem().toString());
                valortotal();


            }
        });
        button600menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(b600ml.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                b600ml.setText(String.valueOf(aux-1));
                valortotal();


            }
        });
        button1000mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux=b1000ml.getText().toString();
                b1000ml.setText(String.valueOf(Integer.parseInt(aux)+1));
                valortotal();


            }
        });
        button1000menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(b1000ml.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                b1000ml.setText(String.valueOf(aux-1));
                valortotal();


            }
        });

        buttongalonmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux=galones.getText().toString();
                galones.setText(String.valueOf(Integer.parseInt(aux)+1));
                valortotal();


            }
        });
        buttongalonmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(galones.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                galones.setText(String.valueOf(aux-1));
                valortotal();
            }
        });

        buttongalones10mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux=galones10.getText().toString();
                galones10.setText(String.valueOf(Integer.parseInt(aux)+1));
                valortotal();
            }
        });
        buttongalones10menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int aux=Integer.parseInt(galones10.getText().toString());
                if(aux<=0){
                    aux=1;
                }
                galones10 .setText(String.valueOf(aux-1));
                valortotal();

            }
        });
        dialog.setView(regis);
        //dialog.setCancelable(false);
        final AlertDialog show = dialog.show();

        botonaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nombre.getText().toString())){
                    Toast.makeText(context, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
                    nombre.requestFocus();
                    nombre.setHint("!!!Falta nombre!!!");

                }else if(TextUtils.isEmpty(celular.getText().toString())){
                    Toast.makeText(context, "Por favor ingresa tu numero de celular", Toast.LENGTH_SHORT).show();
                    celular.requestFocus();
                    celular.setHint("!!!Falta celular!!!");
                }else if(TextUtils.equals(botellones.getText().toString(),"0")
                        &&  TextUtils.equals(botellonesll.getText().toString(),"0")
                        &&  TextUtils.equals(b600ml.getText().toString(),"0")
                        &&  TextUtils.equals(b1000ml.getText().toString(),"0")
                        &&  TextUtils.equals(galones.getText().toString(),"0")
                        &&  TextUtils.equals(galones10.getText().toString(),"0")){
                    Toast.makeText(context, "Por ingrese algun valor en los pedidos.", Toast.LENGTH_SHORT).show();
                }else { Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss  dd-MM-yyyy ");
                    String formattedDate = df.format(c.getTime());

                    /*VALOR RESUMEN ENVIADO A LA SIGUIENTE ACTIVIDTY
                    b0= n de botellones
                    p0=valor botellon
                    b1= numero de botellone de llave
                     p1=valor de botellon de llave
                    b2= numero de botellones de 600
                     p2=valor de 600
                    b3= numero de botellones de 1000
                     p3=valor botellon de litro
                    b4= numero de botellones de 5000
                    p4=valor  de galon
                    b5= numero de botellones de 1000
                    p5=valor botellon de galon 10 l
                    t=total

                    */
                    f=new DecimalFormat("##.00");
                    pedidos=pedidosyVendidos.pedidos(
                            b0,f.format(p0),
                            b1,f.format(p1),
                            b2,f.format(p2),
                            b3,f.format(p3),
                            b4,f.format(p4),
                            b5,f.format(p5),
                            t);

                    Datos datosaEnviar = new Datos();
                    datosaEnviar.setNombre(nombre.getText().toString());
                    datosaEnviar.setCelular(celular.getText().toString());
                    datosaEnviar.setDireccion(spinner_barrios.getSelectedItem().toString());
                    datosaEnviar.setFecha(formattedDate);
                    datosaEnviar.setPedidos(pedidos);

                    if(datos!=null) {
                        datosaEnviar.setVendidos(datos.getVendidos());
                        datosaEnviar.setLat(datos.getLat());
                        datosaEnviar.setLng(datos.getLng());
                        datosaEnviar.setNota(datos.getNota());
                        datosaEnviar.setId(datos.getId());

                        //datosaEnviar.setIdPedido();
                        datosaEnviar.setVendidos(datos.getVendidos());
                    }
                    datosaEnviar.setEstado(false);
                    datosaEnviar.setDespachador("");
                    datosaEnviar.setNota("");
                    Log.d("pedidoenvido",pedidos);
                    //enviar notificacion
                    //sendNotification();

                    provider_pedido.create(datosaEnviar);
                    Intent intent=new Intent(context,Pedidos.class);
                    //intent.putExtra("pedido",datosaEnviar);
                    context.startActivity(intent);
                    enviar_whatsapp_pedido(datosaEnviar);


                    show.dismiss();
                }
            }
        });
        botoncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });


        //dialog.show();



    }

    private String[] ordenarbarrio(String direccion) {
        Resources res = context.getResources();
        String[] aux1 = res.getStringArray(R.array.barrios);
        List<String> barrio = new ArrayList<String>();
        barrio=Arrays.asList(aux1);

        for(int i=0;i<barrio.size();i++){
            if(direccion.equals(barrio.get(i))){
                //barrio.remove(i);
                barrio.set(0,barrio.get(i));
                i=barrio.size();
            }
        }
        return (String[]) barrio.toArray();

    }



    private void sendNotification() {
        //String t="lo4LtZx72TRAtqfczjFI2DTVPLU2";
        String t = "ANIPHZzSzlQrzUmt1Eis3vpWV653";
        tokenProvider.getToken(t).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String token=snapshot.child("token").getValue().toString();
                    Map<String,String>map=new HashMap<>();
                    map.put("tittle","Nuevo Pedido a  ");
                    map.put("body", "Entregar un bidon al cliente n en a direccion" );

                    FCMBody fcmBody=new FCMBody(token,"high","1",map);
                    notificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body()!=null){
                                if(response.body().getSuccess()==1){
                                    Toast.makeText(context,"la notificacion sea enviado correctamente",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,"no se pudo enviar la notificacion",Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(context,"no se pudo enviar la notificacion",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("error",t.getMessage());
                        }
                    });
                } else{
                    Toast.makeText(context,"no se pudo enviar la notificacion porque el usuario no tiene token",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void enviar_whatsapp_pedido(Datos cliente){
        String message="!!!Nuevo pedido!!!\n"+
                       "Nombre: "+cliente.getNombre()+"\n"+
                       "Direccion: "+cliente.getDireccion()+"\n"+
                       "Celular: "+cliente.getCelular()+"\n"+
                       "Pedido: "+pedidosyVendidos.pedidos_resume(pedidosyVendidos.obtener_botellones(cliente.getPedidos()));
        //https://chat.whatsapp.com/GoTK2ArX1S90nEJU1Jqdv0  grupo agua celica
/*        Uri uri =Uri.parse("https://chat.whatsapp.com/GoTK2ArX1S90nEJU1Jqdv0");
        String numero="+593 993 020451";
        numero=numero.replace("+","").replace(" ","");
        //numero="Agua Celica";
        //Uri uri = Uri.parse("https://chat.whatsapp.com/GoTK2ArX1S90nEJU1Jqdv0");

         //Intent i = new Intent("android.intent.action.MAIN");
         //i.putExtra("jid",numero+"@s.whatsapp.net");
         Intent i = new Intent(Intent.ACTION_VIEW, uri);
         i.putExtra(Intent.EXTRA_TEXT,message);
         //i.setAction(Intent.ACTION_SEND);
         i.setPackage("com.whatsapp");
         i.setType("text/plain");*/


        Intent intentWhatsAppGroup = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("https://chat.whatsapp.com/IHF8bFCrPwbINBkKKMrAyj");
        intentWhatsAppGroup.setType("text/plain");
        //intentWhatsAppGroup.setData(uri);
        //intentWhatsAppGroup.setType("text/plain");
        intentWhatsAppGroup.setPackage("com.whatsapp");
        intentWhatsAppGroup.setAction(Intent.ACTION_SEND);
        intentWhatsAppGroup.putExtra(Intent.EXTRA_TEXT,message);
        context.startActivity(Intent.createChooser(intentWhatsAppGroup,"Compartindo con whataspp"));

        //context.startActivity(i);
    }
    private void valortotal() {

        b0= botellones.getText().toString();
        b1= botellonesll.getText().toString();
        p0=Float.parseFloat(b0) * valorb0;
        p1=Float.parseFloat(b1) * valorb1;

        b2=b600ml.getText().toString();
        p2=Float.parseFloat(b2) * valorpacas600 ;

        b3=b1000ml.getText().toString();
        p3= Float.parseFloat(b3) * valorpacas1000;

        b4=galones.getText().toString();
        p4=Float.parseFloat(b4) * valorgalones ;

        b5=galones10.getText().toString();
        p5=Float.parseFloat(b5) * valorgalones10 ;


        t=f.format(p0+p1+p2+p3+p4+p5);
        total.setText(t);
    }
    }



