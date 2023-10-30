package com.example.peidosaguacelica106.Clases;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PedidosyVendidos {
    Context context;


    public String pedidos(String b0,String p0, String b1 ,String p1,String b2, String p2,String b3, String p3,String b4, String p4,String b5, String p5,String t){
        return b0+"-"+p0+"-"+b1+"-"+p1+"-"+b2+"-"+p2+"-"+b3+"-"+p3+"-"+b4+"-"+p4+"-"+b5+"-"+p5+"-"+t;
    }
    public String[] obtener_botellones(String palabra){
        String[] precio = palabra.split("-");
        return precio;

    }


    public String pedidos_resume(String[] desgloce) {
        String  pedido="";

            if(!TextUtils.equals(desgloce[0],"0")){
                pedido=pedido+desgloce[0]+" botellon"+".";
            }
            if(!TextUtils.equals(desgloce[2],"0")){
                pedido=pedido+desgloce[2]+" botellon de llave"+".";
        }
            if (!TextUtils.equals(desgloce[4],"0")) {
                pedido = pedido+desgloce[4] + " paca de 600ml. ";
                }
            if (!TextUtils.equals(desgloce[6],"0")) {
                pedido = pedido+desgloce[6] + " paca de 1L. ";
                }
            if (!TextUtils.equals(desgloce[8],"0")) {
                pedido = pedido+desgloce[8] + " Galones de 5L. ";
                }
            if (!TextUtils.equals(desgloce[10],"0")) {
                pedido = pedido+desgloce[10] + " Galones de 10L. ";
                }


        return pedido;
    }
    public String[] pedidos_factura(String[] desgloce) {
        String [] pedido=new String[22];
        int limite=0;
        Arrays.fill(pedido,"0");

        if(!TextUtils.equals(desgloce[0],"0")){
            pedido[0]="Botellon.";
            pedido[1]=desgloce[0];
            pedido[2]=desgloce[1];
            pedido[3]=String.valueOf(Float.parseFloat(desgloce[0])*Float.parseFloat(desgloce[1]));
            limite++;
        }
        if(!TextUtils.equals(desgloce[2],"0")){
            pedido[0]="Botellon de llave.";
            pedido[1]=desgloce[2];
            pedido[2]=desgloce[3];
            pedido[3]=String.valueOf(Float.parseFloat(desgloce[1])*Float.parseFloat(desgloce[3]));
            limite++;
        }
        if (!TextUtils.equals(desgloce[4],"0")) {
            pedido[5]="Paca de 600ml.";
            pedido[6]=desgloce[4];
            pedido[7]=desgloce[5];
            pedido[8]=String.valueOf(Float.parseFloat(desgloce[4])*Float.parseFloat(desgloce[5]));
            limite++;
        }
        if (!TextUtils.equals(desgloce[6],"0")) {
            pedido[9]="Paca de 1 litro.";
            pedido[10]=desgloce[6];
            pedido[11]=desgloce[7];
            pedido[12]=String.valueOf(Float.parseFloat(desgloce[6])*Float.parseFloat(desgloce[7]));
            limite++;
        }
        if (!TextUtils.equals(desgloce[8],"0")) {
            pedido[13]="Galon de 5 litros.";
            pedido[14]=desgloce[8];
            pedido[15]=desgloce[9];
            pedido[16]=String.valueOf(Float.parseFloat(desgloce[8])*Float.parseFloat(desgloce[9]));
            limite++;
        }
        if (!TextUtils.equals(desgloce[10],"0")) {
            pedido[17]="Galon de 10 litros.";
            pedido[18]=desgloce[10];
            pedido[19]=desgloce[11];
            pedido[20]=String.valueOf(Float.parseFloat(desgloce[10])*Float.parseFloat(desgloce[11]));
            limite++;
        }

        pedido[21]=Integer.toString(limite);
       /* List<String> list=new ArrayList<>();
        list=Arrays.asList(pedido);
        list.remove("0");
        pedido=new String[list.size()];
        pedido=list.toArray(pedido);*/
        return pedido;
    }

    public String pedido_whataspp(String[] desgloce){
        String pedido_whatsapp="";
        String primera_parte="";
        String segunda_parte=pedidos_resume(desgloce);
        pedido_whatsapp=primera_parte+segunda_parte;
        return pedido_whatsapp;


    }
}
