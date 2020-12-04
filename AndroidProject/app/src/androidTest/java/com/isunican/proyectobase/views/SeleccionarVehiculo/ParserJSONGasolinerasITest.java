package com.isunican.proyectobase.views.SeleccionarVehiculo;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.utilities.ParserJSONGasolineras;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.json.simple.parser.JSONParser;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ParserJSONGasolinerasITest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void readGasolineraTest() throws IOException {
        //Creamos la gasolinera que queremos que se parsee correctamente
        String cadenaParseable = "{\"Localidad\": \"LAREDO\"," +
                "\"Precio Gasoleo A\": \"0,996\"," +
                "\"Precio Gasolina 95 E5\": \"0,912\"," +
                "\"Provincia\": \"CANTABRIA\"," +
                "\"IDEESS\": \"77777\"," +
                "\"Rótulo\": \"CEPSA\"," +
                "\"Dirección\": \"Calle Inventada 1\"" +
                "}";

        Gasolinera gasolineraOkcreada = new Gasolinera(77777,"LAREDO", "CANTABRIA",
                "Calle Inventada 1",0.996,0.912,"CEPSA");

        //Creamos un JSONreader para leer la gasolinera
        JsonReader reader = new JsonReader(new StringReader(cadenaParseable));

        Gasolinera gasolineraOk = ParserJSONGasolineras.readGasolinera(reader);

        print(gasolineraOk);
        //comprobamos que la gasolinera se ha parseado correctamente
        assertEquals(gasolineraOk, gasolineraOkcreada);

        //Creamos la gasolinera sin campos
        String cadenaNoParseable = "{"+
                "}";

        //Creamos un JSONreader para leer la gasolinera
        reader = new JsonReader(new StringReader(cadenaNoParseable));

        Gasolinera gasolineraNoOk = ParserJSONGasolineras.readGasolinera(reader);

        Gasolinera gasolineraNoOkCreada = new Gasolinera(-1,"", "",
                "",0.0,0.0,"");

        assertEquals(gasolineraNoOk, gasolineraNoOkCreada);
        print(gasolineraNoOk);
    }

    @Test
    public void parseaArrayGasolinerasTest() throws JSONException, ParseException, IOException {
        List<Gasolinera> listaGasolinerasOk;

        //Creamos el array de gasolineras a leer
        String cadenaLargaParseable = "{\"ListaEESSPrecio\": [{\"Localidad\": \"COLINDRES\"," +
                "\"Precio Gasoleo A\": \"1,096\"," +
                "\"Precio Gasolina 95 E5\": \"1,012\"," +
                "\"Provincia\": \"CANTABRIA\"," +
                "\"IDEESS\": \"55555\"," +
                "\"Dirección\": \"Avenida Real 1\"," +
                "\"Rótulo\": \"REPSOL\"" +
                "}," +
                "{\"Localidad\": \"LIMPIAS\"," +
                "\"Precio Gasoleo A\": \"0,896\"," +
                "\"Precio Gasolina 95 E5\": \"0,812\"," +
                "\"Provincia\": \"CANTABRIA\"," +
                "\"IDEESS\": \"66666\"," +
                "\"Dirección\": \"Avenida Real 2\"," +
                "\"Rótulo\": \"AVIA\"" +
                "}]}";

        //Creamos un JSONreader para leer las gasolineras
        JsonReader reader = new JsonReader(new StringReader(cadenaLargaParseable));

        listaGasolinerasOk = readArrayGasolineras(reader);

        List<Gasolinera> listaGasolinerasOkCreada = new ArrayList<Gasolinera>();

        Gasolinera gasPrueba1 = new Gasolinera(55555,"COLINDRES", "CANTABRIA",
                "Avenida Real 1",1.096,1.012,"REPSOL");
        Gasolinera gasPrueba2 = new Gasolinera(66666,"LIMPIAS", "CANTABRIA",
                "Avenida Real 2",0.896,0.812,"AVIA");

        listaGasolinerasOkCreada.add(gasPrueba1);
        listaGasolinerasOkCreada.add(gasPrueba2);

        assertEquals(listaGasolinerasOk, listaGasolinerasOkCreada);

        List<Gasolinera> listaGasolinerasNoOk;

        String cadenaLargaNoParseable = "{\"ListaEESSPrecio\": [{},{}]}";

        reader = new JsonReader(new StringReader(cadenaLargaNoParseable));

        listaGasolinerasNoOk = readArrayGasolineras(reader);

        List<Gasolinera> listaGasolinerasNoOkCreada = new ArrayList<Gasolinera>();

        Gasolinera gasPruebaMal1 = new Gasolinera(-1,"", "",
                "",0.0,0.0,"");
        Gasolinera gasPruebaMal2 = new Gasolinera(-1,"", "",
                "",0.0,0.0,"");

        listaGasolinerasNoOkCreada.add(gasPruebaMal1);
        listaGasolinerasNoOkCreada.add(gasPruebaMal2);

        assertEquals(listaGasolinerasNoOk, listaGasolinerasNoOkCreada);

    }

    private Gasolinera readGasolinera (JsonReader reader) throws IOException {
        reader.beginObject();
        String rotulo = "";
        String localidad = "";
        String provincia = "";
        String direccion = "";
        int id = -1;
        double gasoleoA = 0.0;
        double sinplomo95 = 0.0;

        while(reader.hasNext()){
            String name = reader.nextName();

            if (name.equals("Rótulo") && reader.peek() != JsonToken.NULL) {
                rotulo = reader.nextString();
            }else if (name.equals("Localidad")) {
                localidad = reader.nextString();
            }else if(name.equals("Provincia")){
                provincia = reader.nextString();
            }else if(name.equals("IDEESS")){
                id = reader.nextInt();
            }else if(name.equals("Precio Gasoleo A") && reader.peek() != JsonToken.NULL) {
                gasoleoA = parseDouble(reader.nextString().replace(",", "."));
            }else if(name.equals("Precio Gasolina 95 E5") && reader.peek() != JsonToken.NULL) {
                sinplomo95 = parseDouble(reader.nextString().replace(",", "."));
            }else if(name.equals("Dirección")){
                direccion = reader.nextString();
            }else{
                reader.skipValue();
            }

        }
        reader.endObject();
        return new Gasolinera(id,localidad,provincia,direccion,gasoleoA, sinplomo95,rotulo);
    }


    public List<Gasolinera> readArrayGasolineras (JsonReader reader) throws IOException {
        List<Gasolinera> listGasolineras = new ArrayList<>();

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            Log.d("ENTRA", "Nombre del elemento: "+name);
            if(name.equals("ListaEESSPrecio")){
                reader.beginArray();
                while (reader.hasNext()){
                    listGasolineras.add(readGasolinera(reader));
                }
                reader.endArray();
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return listGasolineras;
    }

    private void print(Gasolinera g){
        String textoGasolineras = "";
        textoGasolineras +=
                g.getRotulo() + "\n"+
                        g.getDireccion() + "\n" +
                        g.getLocalidad() + "\n" +
                        "Precio diesel: " + g.getGasoleoA() + " " + "\n" +
                        g.getProvincia() + "\n" +
                        "Precio gasolina 95: " + g.getGasolina95() + " " + "\n\n";

        System.out.println(textoGasolineras);
    }

}
