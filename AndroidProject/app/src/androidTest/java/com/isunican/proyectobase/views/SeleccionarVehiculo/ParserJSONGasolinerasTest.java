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
public class ParserJSONGasolinerasTest {

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void readGasolineraTest() throws JSONException, ParseException, IOException {

    }

    @Test
    public void parseaArrayGasolinerasTest() throws IOException {
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

        Gasolinera gasolineraNoOkcreada;

        //Creamos un JSONreader para leer la gasolinera
        reader = new JsonReader(new StringReader(cadenaNoParseable));

        Gasolinera gasolineraNoOk = ParserJSONGasolineras.readGasolinera(reader);

        //Comprobamos que los campos de la gasolinera son los campos predeterminados para los casos invalidos.
        assertTrue(gasolineraNoOk.getGasoleoA() == 0.00);
        assertTrue(gasolineraNoOk.getGasolina95() == 0.00);

       // assertTrue(gasolineraNoOk.getIdeess() == -1);
        assertEquals(gasolineraNoOk.getDireccion(), "");
        assertEquals(gasolineraNoOk.getProvincia(), "");
        assertEquals(gasolineraNoOk.getRotulo(), "");

        print(gasolineraNoOk);
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
