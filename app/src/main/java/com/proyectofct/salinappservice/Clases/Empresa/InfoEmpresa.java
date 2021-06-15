package com.proyectofct.salinappservice.Clases.Empresa;
import java.io.Serializable;

public class InfoEmpresa implements Serializable {
    private String logoURL;
    private String nombre;
    private String sector;
    private String resumen;
    private String direccion;
    private String cif;
    private String cod_empresa;


    public InfoEmpresa(String logoURL, String nombre, String sector, String resumen, String direccion, String cif, String cod_empresa) {
        this.logoURL = logoURL;
        this.nombre = nombre;
        this.sector = sector;
        this.resumen = resumen;
        this.direccion = direccion;
        this.cif = cif;
        this.cod_empresa = cod_empresa;
    }

    public InfoEmpresa() {
        this.logoURL = "https://firebasestorage.googleapis.com/v0/b/salinappsdam.appspot.com/o/logo_empresa%2Fimages.jpg?alt=media&token=bed42a47-37f2-4329-a200-d3177b811e58";
        this.nombre = "nombre";
        this.sector = "sector";
        this.resumen = "resumen";
        this.direccion = "direccion";
        this.cif = "cif";
        this.cod_empresa = "cod_empresa";
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getCod_empresa() {
        return cod_empresa;
    }

    public void setCod_empresa(String cod_empresa) {
        this.cod_empresa = cod_empresa;
    }

    @Override
    public String toString() {
        return "InfoEmpresa{" +
                "logoURL='" + logoURL + '\'' +
                ", nombre='" + nombre + '\'' +
                ", sector='" + sector + '\'' +
                ", resumen='" + resumen + '\'' +
                ", direccion='" + direccion + '\'' +
                ", cif='" + cif + '\'' +
                ", cod_empresa='" + cod_empresa + '\'' +
                '}';
    }

}


