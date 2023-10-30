package com.example.peidosaguacelica106;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasedeDatos {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Administracion/Clientes");
    DatabaseReference myRefpedido = database.getReference("Administracion/Pedidos");
    public void actualizar_vendido(String key,Double valor){
        myRef.child(key).child("vendidos").setValue(valor);
    }

    public void actualizar_estado_pedido(String key,Boolean valor){
        myRefpedido.child(key).child("estado").setValue(valor);
    }
    public void actualizar_pendiente_pedido(String key,String valor){
        myRefpedido.child(key).child("nota").setValue(valor);
    }
    public void actualizar_despachador(String key,String valor){
        myRefpedido.child(key).child("despachador").setValue(valor);
    }
}
