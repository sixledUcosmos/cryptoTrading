package com.example.criptolist.Model;

public class History {
  String name;
  String description;
  double valor;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public History(String name, String description, double valor) {
    this.name = name;
    this.description = description;
    this.valor = valor;
  }
}
