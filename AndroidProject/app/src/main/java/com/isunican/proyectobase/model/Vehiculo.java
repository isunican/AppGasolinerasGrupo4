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

    private int ideess;
    private String marca;
    private String modelo;
    private String matricula;
    private String combustible;

    /**
     *Constructor, getters y setters
     */

    public Vehiculo(int ideess, String marca, String modelo, String matricula, String combustible){
        this.ideess = ideess;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.combustible = combustible;
    }

    public int getIdeess() {return ideess;}
    public void setIdeess(int ideess) {this.ideess = ideess;}

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
        ideess = in.readInt();
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
        dest.writeInt(ideess);
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
}

