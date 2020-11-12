package com.isunican.proyectobase.presenter;


import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.utilities.ParserJSONGasolineras;

import java.util.ArrayList;
import java.util.List;

public class PresenterVehiculos {
    private List<Vehiculo> vehiculos;

    //Base de datos de donde se obtiene los vehiculos
    public static final String URL_GASOLINERAS_CANTABRIA = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/06";
    public static class DatoNoValido extends RuntimeException {}

    /**
     * Constructor, getters y setters
     */
    public PresenterVehiculos() {
        vehiculos = new ArrayList<>();
        this.cargaDatosDummy();
    }

    public List<Vehiculo> getVehiculos() {
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
        this.vehiculos.add(new Vehiculo(1, "Renault", "Clio", "1234AAA", "Gasolina"));
        this.vehiculos.add(new Vehiculo(2, "BMW", "M3", "1234BBB", "Gasolina"));
        this.vehiculos.add(new Vehiculo(3, "Ford", "Fiesta", "1234CCC", "Gasoleo"));
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
}
