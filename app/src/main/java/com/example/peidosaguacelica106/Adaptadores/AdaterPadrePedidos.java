package com.example.peidosaguacelica106.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peidosaguacelica106.Datos.Datos;
import com.example.peidosaguacelica106.Datos.DatosPadre;
import com.example.peidosaguacelica106.R;

import java.util.ArrayList;
import java.util.List;

public class AdaterPadrePedidos extends RecyclerView.Adapter<AdaterPadrePedidos.ViewHolder> {
   List<DatosPadre> datosPadreList;

    public AdaterPadrePedidos(List<DatosPadre> datosPadreList) {
        this.datosPadreList = datosPadreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.activity_agregarpedido2,parent,false);
        return new ViewHolder(view);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatosPadre section=datosPadreList.get(position);
        String seccioton_name=section.getSectionName();
        List<Datos> iten=section.getSectioniten();

        holder.titulo_padre.setText(seccioton_name);

        //AdapterPedidos adapterPedidos=new AdapterPedidos(iten);
    }



    @Override
    public int getItemCount() {
        return datosPadreList.size();

    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titulo_padre;
        RecyclerView recyclerViewpadre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo_padre=itemView.findViewById(R.id.titulo_padre);
            recyclerViewpadre=itemView.findViewById(R.id.recycler_padre);
        }
    }
}
