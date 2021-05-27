package com.proyectofct.salinappservice.Clases.Productos;

import java.sql.Blob;

public class Coches extends Producto {
    private String matricula;
    private String modeloCoche;
    private String numeroBastidor;
    private String ano;
    private String color;
    private int kilometros;

    public Coches(String cod_producto, String cod_QR, String marca, String modelo, String descripción, Blob imagen, String matricula, String modeloCoche, String numeroBastidor, String ano, String color, int kilometros) {
        super(cod_producto, cod_QR, marca, modelo, descripción, imagen);
        this.matricula = matricula;
        this.modeloCoche = modeloCoche;
        this.numeroBastidor = numeroBastidor;
        this.ano = ano;
        this.color = color;
        this.kilometros = kilometros;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNumeroBastidor() {
        return numeroBastidor;
    }

    public void setNumeroBastidor(String numeroBastidor) {
        this.numeroBastidor = numeroBastidor;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getKilometros() {
        return kilometros;
    }

    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }

    public String getModeloCoche() {
        return modeloCoche;
    }

    public void setModeloCoche(String modeloCoche) {
        this.modeloCoche = modeloCoche;
    }

    @Override
    public String toString() {
        return "Coches{" +
                "matricula='" + matricula + '\'' +
                ", modeloCoche='" + modeloCoche + '\'' +
                ", numeroBastidor='" + numeroBastidor + '\'' +
                ", ano='" + ano + '\'' +
                ", color='" + color + '\'' +
                ", kilometros=" + kilometros +
                '}';
    }
}
