package com.example.peidosaguacelica106.Datos;




import java.io.Serializable;

public class Datos implements Serializable {
    String id,pedidos,celular,direccion,fecha,lat,lng,nombre, nota,idPedido,despachador;
    Double vendidos;
    Boolean estado;
    public Datos() {
    }


    public Datos(String idPedido,Boolean estado,Double vendidos,String pedidos, String celular, String direccion, String fecha, String lat, String lng, String nombre, String nota,String id,String despachador) {
        this.estado=estado;
        this.id=id;
        this.vendidos = vendidos;
        this.pedidos=pedidos;
        this.celular = celular;
        this.direccion = direccion;
        this.fecha = fecha;
        this.lat = lat;
        this.lng = lng;
        this.nombre = nombre;
        this.nota = nota;
        this.idPedido=idPedido;
        this.despachador=despachador;

    }
    public Datos(String nombre, String celular) {
          this.celular = celular;
          this.nombre = nombre;
          }

    public String getDespachador() {
        return despachador;
    }

    public void setDespachador(String despachador) {
        this.despachador = despachador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPedidos() {
        return pedidos;
    }

    public void setPedidos(String pedidos) {
        this.pedidos = pedidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public Double getVendidos() {
        return vendidos;
    }

    public void setVendidos(Double vendidos) {
        this.vendidos = vendidos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
