package com.isunican.proyectobase.model;

/*
------------------------------------------------------------------
    Clase que almacena la informacion de un vehiculo
    Implementa la interfaz Parceable, que permite que luego podamos
    pasar objetos de este tipo entre activities a traves de una llamada intent
------------------------------------------------------------------
*/

import android.os.Parcel;
import android.os.Parcelable;

public class Vehiculo implements Parcelable {

    private String marca;
    private String modelo;
    private String matricula;
    private String combustible;

    /**
     *Constructor, getters y setters
     */

    public Vehiculo( String marca, String modelo, String matricula, String combustible){
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.combustible = combustible;
    }

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    public String getMatricula() {return matricula;}
    public void setMatricula(String matricula) {this.matricula = matricula;}

    public String getCombustible() {return combustible;}
    public void setCombustible(String combustible) {this.combustible = combustible;}


    /**
     * interfaz Parcelable
     *
     * MÃ©todos necesarios para implementar la interfaz Parcelable
     * que nos permitirÃ¡ pasar objetos del tipo Vehiculo
     * directamente entre actividades utilizando intents
     * Se enviarÃ­an utilizando putExtra
     * myIntent.putExtra("id", objeto vehiculo);
     * y recibiÃ©ndolos con
     * Vehiculo v = getIntent().getExtras().getParcelable("id")
     */
    protected Vehiculo(Parcel in) {
        marca = in.readString();
        modelo = in.readString();
        matricula = in.readString();
        combustible = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(marca);
        dest.writeString(modelo);
        dest.writeString(matricula);
        dest.writeString(combustible);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Vehiculo> CREATOR = new Parcelable.Creator<Vehiculo>() {
        @Override
        public Vehiculo createFromParcel(Parcel in) {
            return new Vehiculo(in);
        }

        @Override
        public Vehiculo[] newArray(int size) {
            return new Vehiculo[size];
        }
    };

    @Override
    public String toString(){
        //Se eliminan los espacios
        String tmpMarca = marca.replaceAll("\\s+", "");
        String tmpModelo = modelo.replaceAll("\\s+", "");
        String tmpMatricula = matricula.replaceAll("\\s+", "");
        String tmpCombustible = combustible.replaceAll("\\s+", "");
        return("Marca:" + tmpMarca + "-" + "Modelo:" + tmpModelo + "-" + "Matricula:"+ tmpMatricula + "-" + "Combustible:" + tmpCombustible + "-");
    }
}

