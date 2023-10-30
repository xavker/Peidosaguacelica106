package com.example.peidosaguacelica106.Provider;

import com.example.peidosaguacelica106.Datos.Datos;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProviderPedido {
    public DatabaseReference mDatabase;
    public DatabaseReference referencia_cliente;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ProviderPedido() {
        mDatabase = database.getReference().child("Administracion/Pedidos");
        referencia_cliente=database.getReference().child("Administracion/Clientes");
    }

    public Task<Void> create(Datos datos) {
        String key1=mDatabase.push().getKey().toString();
        mDatabase.child(key1).setValue(datos);
        return mDatabase.child(key1).child("idPedido").setValue(key1);
        //#return mDatabase.child(mDatabase.push().toString()).setValue(datos);
    }
    public void actualizar_estado_pedido(String key, Boolean valor) {

        mDatabase.child(key).child("estado").setValue(valor);
    }

    public DatabaseReference getClient(String idPedido) {
        return mDatabase.child(idPedido);
    }

    public void actualizar_despachador(String key,String valor1){
        mDatabase.child(key).child("despachador").setValue(valor1);
    }

    public void actualizar_vendido(String key,Double valor){
        referencia_cliente.child(key).child("vendidos").setValue(valor);
    }

}
