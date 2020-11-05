package com.isunican.proyectobase.presenter;

import android.util.Log;

import com.isunican.proyectobase.model.*;
import com.isunican.proyectobase.utilities.ParserJSONGasolineras;
import com.isunican.proyectobase.utilities.RemoteFetch;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

/*
------------------------------------------------------------------
    Clase presenter con la logica de gasolineras
    Mantiene un objeto ListaGasolineras que es el que mantendrá
    los datos de las gasolineras cargadas en nuestra aplicación
    Incluye métodos para gestionar la lista de gasolineras y
    cargar datos en ella.
------------------------------------------------------------------
*/
public class PresenterGasolineras {

    private List<Gasolinera> gasolineras;

    //URLs para obtener datos de las gasolineras
    //https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help
    public static final String URL_GASOLINERAS_SPAIN = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/";
    public static final String URL_GASOLINERAS_CANTABRIA = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/06";
    public static final String URL_GASOLINERAS_SANTANDER = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroMunicipio/5819";
    public static final String SANTANDER = "Santander";

    public static class DatoNoValido extends RuntimeException {}

    /**
     * Constructor, getters y setters
     */
    public PresenterGasolineras() {
        gasolineras = new ArrayList<>();
    }

    public List<Gasolinera> getGasolineras() {
        return gasolineras;
    }

    public void setGasolineras(List<Gasolinera> l) {
        this.gasolineras = l;
    }

    /**
     * cargaDatosGasolineras
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
    public boolean cargaDatosGasolineras() {
        return cargaDatosRemotos(URL_GASOLINERAS_CANTABRIA);
    }

    /**
     * cargaDatosDummy
     * <p>
     * Carga en la lista de gasolineras varias gasolineras definidas a "mano"
     * para hacer pruebas de funcionamiento
     *
     * @param
     * @return boolean
     */
    public boolean cargaDatosDummy() {
        this.gasolineras.add(new Gasolinera(1000, SANTANDER, SANTANDER, "Av Valdecilla", 1.299, 1.359, "AVIA"));
        this.gasolineras.add(new Gasolinera(1053, SANTANDER, SANTANDER, "Plaza Matias Montero", 1.270, 1.349, "CAMPSA"));
        this.gasolineras.add(new Gasolinera(420, SANTANDER, SANTANDER, "Area Arrabal Puerto de Raos", 1.249, 1.279, "E.E.S.S. MAS, S.L."));
        this.gasolineras.add(new Gasolinera(9564, SANTANDER, SANTANDER, "Av Parayas", 1.189, 1.269, "EASYGAS"));
        this.gasolineras.add(new Gasolinera(1025, SANTANDER, SANTANDER, "Calle el Empalme", 1.259, 1.319, "CARREFOUR"));
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
     * cargaDatosRemotos
     * <p>
     * A partir de la dirección pasada como parámetro:
     * Utiliza RemoteFetch para cargar el fichero JSON ubicado en dicha URL
     * en un stream de datos.
     * Luego utiliza ParserJSONGasolineras para parsear dicho stream
     * y extraer una lista de gasolineras.
     * Finalmente, dicha lista queda almacenada en la clase.
     *
    // * @param String Dirección URL del JSON con los datos
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosRemotos(String direccion) {
        try {
            BufferedInputStream buffer = RemoteFetch.cargaBufferDesdeURL(direccion);
            gasolineras = ParserJSONGasolineras.parseaArrayGasolineras(buffer);
            Log.d("ENTRA", "Obten gasolineras:" + gasolineras.size());
            return true;
        } catch (Exception e) {
            Log.e("ERROR", "Error en la obtención de gasolineras: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retorna la lista de gasolineras eliminando las gasolineras que no tengan gasolina.
     */
    public static List<Gasolinera> filtrarCombustibleGasolina(List<Gasolinera> gasolineras) {
        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            if (g.getGasolina95() != 0.0) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }

    /**
     * Retorna la lista de gasolineras eliminando las gasolineras que no tengan gasoleo.
     */
    public static List<Gasolinera> filtrarCombustibleGasoleo(List<Gasolinera> gasolineras) {
        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            if (g.getGasoleoA() != 0.0) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }

    /**
     * Filtra las gasolineras, eliminando de la lista todas las gasolineras que no se encuentren en el
     * rango de precio especificado.
     *
     * @param gasolineras
     * @param min,        minimo precio del rango.
     * @param max,        maximo precio del rango.
     * @return
     */
    public static List<Gasolinera> filtraPrecioGasolina(List<Gasolinera> gasolineras, double min, double max){

        //Comprueba número negativo en ambas etiquetas
        if(min < 0 || max < 0)
        {
            throw new DatoNoValido();
        }

        //Comprueba que min no pueda ser mayor que max
        if(min > max)
        {
            throw new DatoNoValido();
        }

        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            double combustibleActual = g.getGasolina95();

            if (combustibleActual >= min && combustibleActual <= max) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }

    /**
     * Filtra las gasolineras, eliminando de la lista todas las gasolineras que no se encuentren en el
     * rango de precio especificado.
     *
     * @param gasolineras
     * @param min,        minimo precio del rango.
     * @param max,        maximo precio del rango.
     * @return
     */
    public static List<Gasolinera> filtraPrecioGasoleo(List<Gasolinera> gasolineras, double min, double max){

        //Comprueba número negativo en ambas etiquetas
        if(min < 0 || max < 0)
        {
            throw new DatoNoValido();
        }

        //Comprueba que min no pueda ser mayor que max
        if(min > max)
        {
            throw new DatoNoValido();
        }

        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            double combustibleActual = g.getGasoleoA();

            if (combustibleActual >= min && combustibleActual <= max) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }
}
