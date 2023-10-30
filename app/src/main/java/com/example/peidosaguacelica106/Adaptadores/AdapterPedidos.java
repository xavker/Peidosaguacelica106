package com.example.peidosaguacelica106.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peidosaguacelica106.BasedeDatos;
import com.example.peidosaguacelica106.Clases.MainActivity;
import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Clases.Pedidos;
import com.example.peidosaguacelica106.Clases.PedidosyVendidos;
import com.example.peidosaguacelica106.Provider.AuthProvider;
import com.example.peidosaguacelica106.Provider.ProviderPedido;
import com.example.peidosaguacelica106.R;

import java.util.ArrayList;
import java.util.List;


//public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.MyViewHolder> {
public class AdapterPedidos extends RecyclerView.Adapter<com.example.peidosaguacelica106.Adaptadores.AdapterPedidos.MyViewHolder> {

        boolean auxb;
    List<Datos> list;
    List<Datos> list2=new ArrayList<>();
    Context context;
    PedidosyVendidos pedidos;
    Pedidos actividad_pedidos;
    BasedeDatos basedeDatos;
    Datos cliente1;
    private  Datos datosantes;
    ProviderPedido providerPedido;
    private int n_pendientes;
    private String nota_pendiente="";
    private AuthProvider mAuth;
    public AdapterPedidos(ArrayList<Datos> list, Context context) {

        this.list = list;
        this.context=context;
        pedidos=new PedidosyVendidos();
        actividad_pedidos=new Pedidos();
        providerPedido=new ProviderPedido();
        basedeDatos=new BasedeDatos();
        n_pendientes=0;
        auxb=false;
        mAuth=new AuthProvider();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewpedidos,parent,false);
        for(int i=0;i<list.size();i++){
            if(!list.get(i).getEstado()){
                n_pendientes++;
            }
        }
        return new MyViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       // PedidosDatos pedidosdatos =list.get(position);
        cliente1=list.get(position);

        basedeDatos=new BasedeDatos();
        Log.d("pedido",cliente1.getEstado().toString());
        holder.nombre.setText(cliente1.getNombre());
        holder.direccion.setText(cliente1.getDireccion());
        holder.celular.setText(cliente1.getCelular());

        String[] desgloce = pedidos.obtener_botellones(cliente1.getPedidos());


        String pedido1="Bidon: "+ desgloce[0]+"\n"+
                        "B llave: "+ desgloce[2]+"\n"+
                        "600 ml: "+desgloce[4]+"\n"+
                        "1000 ml: "+desgloce[6]+"\n"+
                        "Galones 5l: "+desgloce[8]+"\n"+
                        "Galones 10l: "+desgloce[10];
        holder.resumen_pedido.setText(pedidos.pedidos_resume(desgloce));
        holder.pedido.setText(pedido1);
        holder.total.setText("$ "+desgloce[12]);
        holder.fecha.setText(cliente1.getFecha());
        holder.nota.setText(cliente1.getNota());
        holder.despachador.setText(cliente1.getDespachador());

        Log.d(/**/"valorintent",cliente1.getEstado().toString());

        holder.estado_box.setOnCheckedChangeListener(null);
        //colores cardview
        boolean state=cliente1.getEstado();
        boolean note=!cliente1.getNota().isEmpty();
        if(!state&&!note){
           // holder.linearLayout.setBackgroundColor(R.color.cardview);
            holder.celular.setText("opcion 0");
        }else if(!state&&note){
            holder.linearLayout.setBackgroundColor(R.color.colorPendiente);
            holder.celular.setText("opcion 1");
        }if(state&&!note){
            holder.linearLayout.setBackgroundColor(R.color.cardviewentregado);
            holder.celular.setText("opcion 2");
        }if(state&&note){
            holder.linearLayout.setBackgroundColor(R.color.colorPendiente);
            holder.celular.setText("opcion 3");
        }
        if(cliente1.getEstado()){
            holder.estado_box.setChecked(true);
            holder.estado_box.setEnabled(false);
            //holder.linearLayout.setBackgroundColor(R.color.cardviewentregado);
          // holder.parent.setCardBackgroundColor(R.color.cardviewentregado);
        }else {
            holder.estado_box.setChecked(false);
            holder.estado_box.setEnabled(true);
            //holder.linearLayout.setBackgroundColor(R.color.cardview);
        }
        holder.estado_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                int currentposition=holder.getAdapterPosition();
                if(ischecked){
                    datosantes=list.get(currentposition);

                    Toast.makeText( context,  "Entregado a "+datosantes.getNombre(), Toast.LENGTH_SHORT).show();
                    holder.estado_box.setEnabled(false);
                    holder.estado_box.setChecked(true);
                    holder.linearLayout.setBackgroundColor(R.color.cardviewentregado);

                    list.remove(datosantes);
                    //list.add(datosantes);
                    //DecimalFormat df = new DecimalFormat("#.00");
                    Double total_actualizado=cliente1.getVendidos()+Double.valueOf(desgloce[6]);
                    //notifyItemMoved(currentposition,list.size()-1 );
                    //providerPedido.actualizar_estado_pedido(cliente1.getIdPedido(),true);
                    //providerPedido.actualizar_vendido(cliente1.getId(),total_actualizado);
                    basedeDatos.actualizar_estado_pedido(datosantes.getIdPedido(),true);
                    basedeDatos.actualizar_vendido(datosantes.getId(),total_actualizado);

                    //Ingresa en base de datos el nombre del despachador una vez entregado el producto
                    String usuario=comprobar();
                    basedeDatos.actualizar_despachador(datosantes.getIdPedido(),usuario);

                    Toast.makeText( context,  "Entregado a "+datosantes.getNombre(), Toast.LENGTH_SHORT).show();
                    //notifyDataSetChanged();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    //actividad_pedidos.onBackPressed();
                }
            }
        });


        holder.linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auxb == false) {
                    holder.linearLayout2.setVisibility(View.VISIBLE);
                    auxb=true;
                }else {
                    holder.linearLayout2.setVisibility(View.GONE);
                    auxb=false;
                }

            }
        });
        holder.linearLayout1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int currentposition1=holder.getAdapterPosition();
                datosantes=list.get(currentposition1);
                String key=datosantes.getIdPedido();
                crear_pendiente(key);


                return false;
            }
        });



        //notifyDataSetChanged();
       /* holder.estado_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((CheckBox)view).isChecked()) {
                    //Toast.makeText( context,  "Entregado a "+cliente1.getNombre(), Toast.LENGTH_SHORT).show();
                    //holder.parent.setCardBackgroundColor(R.color.colorPrimary);
                    holder.estado_box.setEnabled(false);
                    holder.estado_box.setChecked(true);
                    notifyDataSetChanged();
//                    list.remove(cliente1);
  //                  list2.add(cliente1);
                }
                    // actividad_pedidos.reiniciarActivity();
                    //holder.estado_box.setChecked(true);
                    //providerPedido.actualizar_estado_pedido(cliente1.getIdPedido (),true);
                    //DecimalFormat df = new DecimalFormat("#.00");
                    //Double total_actualizado=cliente1.getVendidos()+Double.valueOf(desgloce[6]);
                    //providerPedido.actualizar_vendido(cliente1.getId(),total_actualizado);


                    //Toast.makeText(context, "valor  : "+Double.valueOf(cliente1.getVendidos())+Double.valueOf(desgloce[6]), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "id: "+cliente1.getId(), Toast.LENGTH_SHORT).show();
                    //Double total_actualizado=Double.valueOf(cliente1.getVendidos())+Double.valueOf(desgloce[6]);
                    //actividad_pedidos.reiniciarActivity();

                    ///Programar que pasa si ya es entregado

            }
        });
*/
    }

    private String comprobar() {
        String user=null;
        user=mAuth.getId();
        if(user.equals("lo4LtZx72TRAtqfczjFI2DTVPLU2")){
            user="X.RAMON";
        }else if(user.equals("gd9w1iXP0VYh6O3Ez3F6UZsOW1m2")) {
            user = "L.CORDOVA";
        }else if(user.equals("SSF8h1GMVshtZSSfiqGbiUodtNh2")) {
            user = "J.VERA";
        }else if(user.equals("GNtsDymuxnNu2dwehtcTC9ONt8f2")) {
            user = "C.POMA";
        }else if(user.equals("nqH2BJLiYufcBFEVvj8ylgcc0Av2")) {
            user = "A.CALDERON";
        }
        Toast.makeText(context,"Usuario: "+user,Toast.LENGTH_SHORT).show();
        return user;
    }

    private String  crear_pendiente(String key1) {
        final androidx.appcompat.app.AlertDialog.Builder dialog=new androidx.appcompat.app.AlertDialog.Builder(context);
        //dialog.setTitle("Pendiente");
        dialog.setMessage("\n");
        LayoutInflater inflater=LayoutInflater.from(dialog.getContext());
        View v=inflater.inflate(R.layout.pedidos_novedades,null);
        EditText pendiente=v.findViewById(R.id.note_pendiente);


        dialog.setView(v);
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nota_pendiente=pendiente.getText().toString();
                basedeDatos.actualizar_pendiente_pedido(key1,nota_pendiente);
                Toast.makeText(context,"Ingrese pendiente \n"+nota_pendiente, Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setNegativeButton("Descartar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        dialog.show();
        return nota_pendiente;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, celular, direccion, fecha, pedido, total,resumen_pedido,nota,despachador;
        CardView parent;
        CheckBox estado_box;
        LinearLayout linearLayout,linearLayout1,linearLayout2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.itenpedido);
            nombre = itemView.findViewById(R.id.nombre1);
            celular = itemView.findViewById(R.id.celular);
            direccion = itemView.findViewById(R.id.direccion);
            fecha = itemView.findViewById(R.id.fecha);
            total = itemView.findViewById(R.id.total);
            pedido=itemView.findViewById(R.id.textopedido);
            estado_box=itemView .findViewById(R.id.estado);
            linearLayout=itemView.findViewById(R.id.itencolor);
            linearLayout1=itemView.findViewById(R.id.linearLayout1);
            linearLayout2=itemView.findViewById(R.id.linearLayout2);
            resumen_pedido=itemView.findViewById(R.id.pedidos_resume);
            nota=itemView.findViewById(R.id.nota_pendiente);
            despachador=itemView.findViewById(R.id.despachador);


        }
    }





}
