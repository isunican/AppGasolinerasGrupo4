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

    //Base de datos de donde se obtiene los vehiculos
    public static class DatoNoValido extends RuntimeException {}
    public static class VehiculoYaExiste extends RuntimeException {}
    public static class MatriculaNoValida extends RuntimeException {}
    public static class CombustibleNoValido extends RuntimeException {}
    public static class VehiculoNulo extends RuntimeException {}

    /**
     * Constructor, getters y setters
     */
    public PresenterVehiculos() {
        vehiculos = new HashMap<String, Vehiculo>();
        this.cargaDatosDummy();
    }

    /**
     * Actualiza y retorna el mapa con los vehiculos
     * @return
     */
    public Map<String, Vehiculo> getVehiculos(Context context) {
        cargaDatosVehiculos(context);
        return vehiculos;
    }


    /**
     * cargaDatosVehiculos
     * <p>
     * Carga los datos de los vehiculos almacenados en la base de datos
     *
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
    public void anhadirVehiculo(Vehiculo v, Context context) {
        //Metodos para guardar vehiculo en el fichero asi como comprobar las matriculas y demas.
        String marca = v.getMarca();
        String modelo = v.getModelo();
        String matricula = v.getMatricula();
        String combustible = v.getCombustible();

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

        // Lanza excepcion si el combustible pasado no es GasoleoA o Gasolina95
        if(!combustible.equals("Gasolina95") && !combustible.equals("GasoleoA")){
            throw new CombustibleNoValido();
        }

        // Lanza excepcion si el vehiculo es nulo
        if(v.equals(null)){
            throw new VehiculoNulo();
        }
        escribeVehiculo(v.toString(), context);
        vehiculos.put(matricula, v);
    }

    /**
     * Escribe el vehículo pasado como parámetro en la base de datos
     */
    private void escribeVehiculo(String vehiculo, Context context){
        try{
            FileWriter outputStreamWriter = new FileWriter (context.getFileStreamPath("vehiculos.txt"), true);
            outputStreamWriter.write(vehiculo);
            outputStreamWriter.close();
        }catch (IOException e){
            Log.e("Excepción","Fallo al escribir en la base de datos");
        }
    }

    /**
     * Retorna false si el fichero estaba vacio y true si tenia contenido
     * @param db
     * @return
     */
    private boolean obtieneMapa(String db){
        HashMap<String, Vehiculo> lista = new HashMap<>();
        String marca="";
        String modelo="";
        String matricula="";
        String combustible="";
        System.out.println(db);
        char[] charDB = db.toCharArray();
        if(charDB.length==0){
            vehiculos = new HashMap<String, Vehiculo>();
            return false;
        }

        String tmp;
        int cont=0;
        System.out.println(charDB.length);
        while(cont < charDB.length-1){

            // Se reinicializan las variables para que no se acumulen los datos
            marca = "";
            modelo = "";
            matricula = "";
            combustible = "";

            tmp=Character.toString(charDB[cont]);
            //Busca la marca de cada vehiculo
            while(!tmp.matches(":")){
                cont++;
                tmp=Character.toString(charDB[cont]);
            }
            cont++;
            tmp=Character.toString(charDB[cont]);
            while(!tmp.matches("-")){
                marca = marca.concat(tmp);
                cont++;
                tmp=Character.toString(charDB[cont]);
            }

            //Busca el modelo de cada vehiculo
            while(!tmp.matches(":")){
                cont++;
                tmp=Character.toString(charDB[cont]);
            }
            cont++;
            tmp=Character.toString(charDB[cont]);
            while(!tmp.matches("-")){
                modelo = modelo.concat(tmp);
                cont++;
                tmp=Character.toString(charDB[cont]);
            }

            //Busca la matricula de cada vehiculo
            while(!tmp.matches(":")){
                cont++;
                tmp=Character.toString(charDB[cont]);
            }
            cont++;
            tmp=Character.toString(charDB[cont]);
            while(!tmp.matches("-")){
                matricula = matricula.concat(tmp);
                cont++;
                tmp=Character.toString(charDB[cont]);
            }

            //Busca el tipo de combustible de cada vehiculo
            while(!tmp.matches(":")){
                cont++;
                tmp=Character.toString(charDB[cont]);
            }

            cont++;
            tmp=Character.toString(charDB[cont]);

            while(!tmp.matches("-")){
                combustible = combustible.concat(tmp);
                cont++;
                tmp=Character.toString(charDB[cont]);

                if(combustible.equals("Gasolina95") || combustible.equals("GasoleoA")){
                    tmp = "-";
                    System.out.println(cont);
                }

                if(cont > charDB.length){
                    cont = charDB.length;
                }
            }
            lista.put( matricula, new Vehiculo(marca, modelo, matricula, combustible));
        }
        vehiculos = lista;
        return true;
    }

    /**
     * Devuelve el contenido de la base de datos como un String
     * @return
     */
    private String consultaDB(Context context){
        String ret = " ";
        try{
            InputStream inputStream = context.openFileInput("vehiculos.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "No se ha encontrado el fichero: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "No se ha podido leer el fichero " + e.toString());
        }
        return ret;
    }
}
