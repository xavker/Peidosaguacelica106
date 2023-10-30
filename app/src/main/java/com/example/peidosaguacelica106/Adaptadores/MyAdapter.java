package com.example.peidosaguacelica106.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Clases.DialogodePedido;
import com.example.peidosaguacelica106.Clases.MapaUbicacion;
import com.example.peidosaguacelica106.R;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<Datos> list;
    Context context;

    public MyAdapter( ArrayList<Datos> list, Context context) {

        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Datos cliente=list.get(position);
        holder.nombre.setText(cliente.getNombre());
        holder.direccion.setText(cliente.getDireccion());
        holder.lat.setText(cliente.getLat());
        holder.lmg.setText(cliente.getLng());
        holder.celular.setText(cliente.getCelular());
        holder.vendido.setText("$ "+String.valueOf(cliente.getVendidos()));

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogodePedido dialogo=new DialogodePedido(context);
                dialogo.agregarcliente(cliente);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    String phoneNo = cliente.getCelular();
                    String dial = "tel:" + phoneNo;
                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));

                return false;
            }
        });
        holder.ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Pantalla para ingresar ubicación del cliente", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, MapaUbicacion.class);
                intent.putExtra("pedido",cliente);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, celular, direccion, lmg, lat, fecha, bidones, vendido;
        Spinner spinner_precio_b1;
        Button ubicacion;
        LinearLayout parent;
        String key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.parentlayout);
            nombre = itemView.findViewById(R.id.nombre1);
            celular = itemView.findViewById(R.id.celular);
            direccion = itemView.findViewById(R.id.direccion);
            lmg = itemView.findViewById(R.id.lng);
            lat = itemView.findViewById(R.id.lat);
            fecha = itemView.findViewById(R.id.fecha);
            vendido = itemView.findViewById(R.id.vendido);
            ubicacion=itemView.findViewById(R.id.ubicacion);


        }
    }





}
