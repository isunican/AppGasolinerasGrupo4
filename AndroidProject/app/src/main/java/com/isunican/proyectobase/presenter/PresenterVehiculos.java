package com.isunican.proyectobase.presenter;

import android.content.Context;
import android.util.Log;

import com.isunican.proyectobase.model.Vehiculo;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresenterVehiculos {
    private HashMap<String, Vehiculo> vehiculos;
    private HashMap<String, Vehiculo> seleccionado;
    static final String MENSAJE_LOG = "login activity";
    static final String FALLO_DB = "Fallo al escribir en la base de datos";
    static final String FICHERO_SELECCIONADO = "seleccionado.txt";
    static final String GASOLINA95 = "Gasolina95";
    static final String GASOLEOA = "GasoleoA";
    static final String EXCEPCION = "Excepción";

    //Base de datos de donde se obtiene los vehiculos
    public static class DatoNoValido extends RuntimeException {
    }

    public static class VehiculoYaExiste extends RuntimeException {
    }

    public static class MatriculaNoValida extends RuntimeException {
    }

    public static class CombustibleNoValido extends RuntimeException {
    }

    public static class VehiculoNulo extends RuntimeException {
    }

    public static class DatoNulo extends RuntimeException {
    }


    /**
     * Constructor, getters y setters
     */
    public PresenterVehiculos() {
        vehiculos = new HashMap<>();
        seleccionado = new HashMap<>();
        this.cargaDatosDummy();
    }

    /**
     * Actualiza y retorna el mapa con los vehiculos
     *
     * @return
     */
    public Map<String, Vehiculo> getVehiculos(Context context) {
        cargaDatosVehiculos(context);
        return vehiculos;
    }

    /**
     * Actualiza y retorna el mapa con los vehiculos
     *
     * @return
     */
    public Map<String, Vehiculo> getSeleccionado(Context context) {
        cargaDatosVehiculosSeleccionado(context);
        return seleccionado;
    }


    /**
     * cargaDatosVehiculos
     * <p>
     * Carga los datos de los vehiculos almacenados en la base de datos
     */
    public boolean cargaDatosVehiculosSeleccionado(Context context) {
        String db = consultaDBSeleccionado(context);
        return obtieneMapaSeleccionado(db);
    }

    /**
     * cargaDatosVehiculos
     * <p>
     * Carga los datos de los vehiculos almacenados en la base de datos
     */
    public boolean cargaDatosVehiculos(Context context) {
        String db = consultaDB(context);
        return obtieneMapa(db);
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
     * Metodo que añade el vehiculo al fichero y al arrayList de vehiculos
     */
    public void anhadirVehiculo(Vehiculo v) {

        // Lanza excepcion si el vehiculo es nulo
        if (v == null) {
            throw new VehiculoNulo();
        }

        //Lanza excepcion si algun dato es nulo
        if (v.getMarca() == null || v.getMatricula() == null || v.getModelo() == null) {
            throw new DatoNulo();
        }

        //Metodos para guardar vehiculo en el fichero asi como comprobar las matricuals y demas.
        String marca = v.getMarca();
        String modelo = v.getModelo();
        String matricula = v.getMatricula();
        String combustible = v.getCombustible();

        //Formato de matricula
        Pattern patron = Pattern.compile("[0-9]{4}+[A-Z]{3}");
        Matcher mat = patron.matcher(matricula);

        // Lanza excepcion si uno de los campos estan vacios
        if (marca.equals("") || modelo.equals("") || matricula.equals("")) {
            throw new DatoNoValido();
        }

        // Lanza excepcion si el vehiculo ya existe
        if (vehiculos.get(matricula) != null) {
            throw new VehiculoYaExiste();
        }

        // Lanza excepcion si la matricula no es valida
        if (!mat.matches()) {
            throw new MatriculaNoValida();
        }
        // Lanza excepcion si el combustible pasado no es GasoleoA o Gasolina95
        if (!combustible.equals(GASOLINA95) && !combustible.equals(GASOLEOA)) {
            throw new CombustibleNoValido();
        }
        vehiculos.put(v.getMatricula(), v);
    }

    /**
     * Método que borra el/los vehiculos almacenados en seleccionados
     */
    public void borraSeleccionados(Context context) throws IOException {
        seleccionado.clear();
        try(FileWriter ignored = new FileWriter(context.getFileStreamPath(FICHERO_SELECCIONADO), false)) {
            ;
        } catch (IOException e) {
            Log.e(EXCEPCION, FALLO_DB);
        }
    }

    /**
     * Metodo que añade el vehiculo seleccionado al fichero y al arrayList de vehiculos
     */
    public void anhadirVehiculoSeleccionado(Vehiculo v) {

        // Lanza excepcion si el vehiculo es nulo
        if (v == null) {
            throw new VehiculoNulo();
        }

        //Lanza excepcion si algun dato es nulo
        if (v.getMarca() == null || v.getMatricula() == null || v.getModelo() == null) {
            throw new DatoNulo();
        }

        //Metodos para guardar vehiculo en el fichero asi como comprobar las matricuals y demas.
        String marca = v.getMarca();
        String modelo = v.getModelo();
        String matricula = v.getMatricula();
        String combustible = v.getCombustible();

        //Formato de matricula
        Pattern patron = Pattern.compile("[0-9]{4}+[A-Z]{3}");
        Matcher mat = patron.matcher(matricula);

        // Lanza excepcion si uno de los campos estan vacios
        if (marca.equals("") || modelo.equals("") || matricula.equals("")) {
            throw new DatoNoValido();
        }

        // Lanza excepcion si la matricula no es valida
        if (!mat.matches()) {
            throw new MatriculaNoValida();
        }
        // Lanza excepcion si el combustible pasado no es GasoleoA o Gasolina95
        if (!combustible.equals(GASOLINA95) && !combustible.equals(GASOLEOA)) {
            throw new CombustibleNoValido();
        }

        seleccionado.clear();
        seleccionado.put(v.getMatricula(), v);
    }

    /**
     * Escribe el vehículo pasado como parámetro en la base de datos
     */
    public void escribeVehiculo(String vehiculo, Context context) {

        try (FileWriter outputStreamWriter = new FileWriter(context.getFileStreamPath("vehiculos.txt"), true)) {
            outputStreamWriter.write(vehiculo);

        } catch (IOException e) {
            Log.e(EXCEPCION, FALLO_DB);
        }
    }

    /**
     * Escribe el vehículo pasado como parámetro en la base de datos
     */
    public void escribeVehiculoSeleccionado(String vehiculo, Context context) {

        try (FileWriter outputStreamWriter = new FileWriter(context.getFileStreamPath(FICHERO_SELECCIONADO), false)) {
            outputStreamWriter.write(vehiculo);

        } catch (IOException e) {
            Log.e(EXCEPCION, FALLO_DB);
        }
    }

    /**
     * Retorna false si el fichero estaba vacio y true si tenia contenido
     *
     * @param db
     * @return
     */
    private boolean obtieneMapa(String db) {
        HashMap<String, Vehiculo> lista = new HashMap<>();
        char[] charDB = db.toCharArray();
        if (charDB.length == 0) {
            vehiculos = new HashMap<>();
            return false;
        }

        int cont = 0;
        buscaVehiculosEnDB(lista, charDB, cont);
        vehiculos = lista;
        return true;
    }

    private boolean obtieneMapaSeleccionado(String db) {
        HashMap<String, Vehiculo> lista = new HashMap<>();
        char[] charDB = db.toCharArray();
        if (charDB.length == 0) {
            seleccionado = new HashMap<>();
            return false;
        }

        int cont = 0;
        buscaVehiculosEnDB(lista, charDB, cont);
        seleccionado = lista;
        return true;
    }

    private void buscaVehiculosEnDB(HashMap<String, Vehiculo> lista, char[] charDB, int cont) {
        String marca;
        String modelo;
        String matricula;
        String combustible;

        while (cont < charDB.length - 1) {
            // Se reinicializan las variables para que no se acumulen los datos
            marca = "";
            modelo = "";
            matricula = "";
            combustible = "";

            String tmp = Character.toString(charDB[cont]);

            //Busca la marca de cada vehiculo
            cont = buscaSiguienteDato(charDB, tmp, cont);

            cont++;
            tmp = Character.toString(charDB[cont]);
            while (!tmp.matches("-")) {
                marca = marca.concat(tmp);
                cont++;
                tmp = Character.toString(charDB[cont]);
            }

            //Busca el modelo de cada vehiculo
            cont = buscaSiguienteDato(charDB, tmp, cont);


            cont++;
            tmp = Character.toString(charDB[cont]);
            while (!tmp.matches("-")) {
                modelo = modelo.concat(tmp);
                cont++;
                tmp = Character.toString(charDB[cont]);
            }

            //Busca la matricula de cada vehiculo
            cont = buscaSiguienteDato(charDB, tmp, cont);

            cont++;
            tmp = Character.toString(charDB[cont]);
            while (!tmp.matches("-")) {
                matricula = matricula.concat(tmp);
                cont++;
                tmp = Character.toString(charDB[cont]);
            }

            //Busca el tipo de combustible de cada vehiculo
            cont = buscaSiguienteDato(charDB, tmp, cont);

            cont++;
            tmp = Character.toString(charDB[cont]);

            while (!tmp.matches("-")) {
                combustible = combustible.concat(tmp);
                cont++;
                tmp = Character.toString(charDB[cont]);

                if (combustible.equals(GASOLINA95) || combustible.equals(GASOLEOA)) {
                    tmp = "-";
                }

                if (cont > charDB.length) {
                    cont = charDB.length;
                }
            }
            lista.put(matricula, new Vehiculo(marca, modelo, matricula, combustible));
        }
    }

    private int buscaSiguienteDato(char[] charDB, String tmp, int cont) {
        while (!tmp.matches(":")) {
            cont++;
            tmp = Character.toString(charDB[cont]);
        }
        return cont;
    }

    /**
     * Devuelve el contenido de la base de datos como un String
     *
     * @return
     */
    private String consultaDB(Context context) {
        String ret = " ";
        try {
            InputStream inputStream = context.openFileInput("vehiculos.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder;
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String receiveString = "";
                stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(MENSAJE_LOG, "No se ha encontrado el fichero: " + e.toString());
        } catch (IOException e) {
            Log.e(MENSAJE_LOG, "No se ha podido leer el fichero " + e.toString());
        }
        return ret;
    }

    /**
     * Devuelve el contenido de la base de datos como un String
     *
     * @return
     */
    private String consultaDBSeleccionado(Context context) {
        String ret = " ";
        try {
            InputStream inputStream = context.openFileInput(FICHERO_SELECCIONADO);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder;
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String receiveString = "";
                stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(MENSAJE_LOG, "No se ha encontrado el fichero: " + e.toString());
        } catch (IOException e) {
            Log.e(MENSAJE_LOG, "No se ha podido leer el fichero " + e.toString());
        }
        return ret;
    }
}
