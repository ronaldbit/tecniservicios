package com.example.simplemvc.model;

public class Producto {
  private Long id;         // id_producto
  private String codigo;
  private String nombre;
  private Double precio;
  private Boolean publicarOnline;

  public Producto(){}
  public Producto(Long id, String codigo, String nombre, Double precio, Boolean publicarOnline){
    this.id=id; this.codigo=codigo; this.nombre=nombre; this.precio=precio; this.publicarOnline=publicarOnline;
  }
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getCodigo(){return codigo;} public void setCodigo(String codigo){this.codigo=codigo;}
  public String getNombre(){return nombre;} public void setNombre(String nombre){this.nombre=nombre;}
  public Double getPrecio(){return precio;} public void setPrecio(Double precio){this.precio=precio;}
  public Boolean getPublicarOnline(){return publicarOnline;} public void setPublicarOnline(Boolean publicarOnline){this.publicarOnline=publicarOnline;}
}
