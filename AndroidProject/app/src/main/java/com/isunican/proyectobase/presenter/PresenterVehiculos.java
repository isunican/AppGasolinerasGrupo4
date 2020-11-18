package com.isunican.proyectobase.presenter;

import com.isunican.proyectobase.model.Vehiculo;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresenterVehiculos {
    private HashMap<String, Vehiculo> vehiculos;

    //Base de datos de donde se obtiene los vehiculos
    public static class DatoNoValido extends RuntimeException {}
    public static class VehiculoYaExiste extends RuntimeException {}
    public static class MatriculaNoValida extends RuntimeException {}

    /**
     * Constructor, getters y setters
     */
    public PresenterVehiculos() {
        vehiculos = new HashMap<String, Vehiculo>();
        this.cargaDatosDummy();
    }

    public HashMap<String, Vehiculo> getVehiculos() {
        return vehiculos;
    }


    /**
     * cargaDatosVehiculos
     * <p>
     * Carga los datos de las gasolineras en la lista de gasolineras de la clase.
     * Para ello llama a métodos de carga de datos internos de la clase ListaGasolineras.
     * En este caso realiza una carga de datos remotos dada una URL
     * <p>
     * Habría que mejorar el método para que permita pasar un parámetro
     * con los datos a cargar (id de la ciudad, comunidad autónoma, etc.)
     *
     * @param
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosVehiculos() {
        // De momento vamos a hacer que cargue la lista ya creada pero la idea es que lo cargue
        // desde un fichero JSON
        return cargaDatosDummy();
    }

    /**
     * cargaDatosDummy
     * <p>
     * Carga en la lista de vehiculos varios vehiculos definidos a "mano"
     * para hacer pruebas de funcionamiento
     *
     * @param
     * @return boolean
     */
    public boolean cargaDatosDummy() {
        this.vehiculos.put("1234AAA", new Vehiculo("Renault", "Clio", "1234AAA", "Gasolina"));
        this.vehiculos.put("1234BBB", new Vehiculo("BMW", "M3", "1234BBB", "Gasolina"));
        this.vehiculos.put("1234CCC", new Vehiculo("Ford", "Fiesta", "1234CCC", "Gasoleo"));
        return true;
    }

    /**
     * cargaDatosLocales
     * <p>
     * A partir de la dirección de un fichero JSON pasado como parámetro:
     * Parsea la información para obtener una lista de gasolineras.
     * Finalmente, dicha lista queda almacenada en la clase.
     *
     // * @param String Nombre del fichero
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosLocales(String fichero) {
        return (fichero != null);
    }

    /**
     * Metodo que añade el vehiculo al fichero y al arrayList de vehiculos
     */
    public void anhadirVehiculo(Vehiculo v) throws DatoNoValido, VehiculoYaExiste, MatriculaNoValida{
        //Metodos para guardar vehiculo en el fichero asi como comprobar las matricuals y demas.
        String marca = v.getMarca();
        String modelo = v.getModelo();
        String matricula = v.getMatricula();

        //Formato de matricula
        Pattern patron = Pattern.compile("[0-9]{4}+[A-Z]{3}");
        Matcher mat = patron.matcher(matricula);

        // Lanza excepcion si uno de los campos estan vacios
        if(marca.equals("") || modelo.equals("") || matricula.equals("")){
            throw new DatoNoValido();
        }

        // Lanza excepcion si el vehiculo ya existe
        if(vehiculos.get(matricula) != null){
            throw new VehiculoYaExiste();
        }


        // Lanza excepcion si la matricula no es valida
        if(!mat.matches()){
            throw new MatriculaNoValida();
        }
        vehiculos.put(matricula, v);
    }//a
}
