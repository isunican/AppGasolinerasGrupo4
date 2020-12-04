    package com.isunican.proyectobase.views.SeleccionarVehiculo;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.views.VehiclesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
@RunWith(AndroidJUnit4.class)
public class SeleccionarVehiculoITest {
    @Rule
    public ActivityTestRule<VehiclesActivity> mActivityTestRuleVehicle = new ActivityTestRule<>(VehiclesActivity.class);

    @Test
    public void anhadirVehiculoTest() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteFile("vehiculos.txt");

        ListView vista = (ListView) mActivityTestRuleVehicle.getActivity().findViewById(R.id.listViewVehiculos);

        //Anhadimos el primer vehiculo y se selecciona automáticamente
        String marca = "Ford";
        String modelo = "Focus";
        String matricula = "1234BBC";
        String combustible = "Gasolina95";
        escribeDatosVehiculo(marca, modelo, matricula, combustible);
        onView(withId(R.id.idBotonAceptar)).perform(click());

        //Anhadimos el segundo vehiculo
        marca = "Seat";
        modelo ="Ibiza";
        matricula= "1111BBB";
        combustible = "Gasolina95";
        escribeDatosVehiculo(marca, modelo, matricula, combustible);

        onView(withId(R.id.idBotonAceptar)).perform(click());

        //Seleccionamos el segundo vehiculo
        /*//Hace click en el primer vehiculo
        DataInteraction evento = onData(anything()).inAdapterView(withId(R.id.listViewVehiculos)).atPosition(0);
        evento.perform(click());

        //Comprobamos que se encuentra en el fichero
*/

        //Hace click en el segundo vehiculo
        DataInteraction evento = onData(anything()).inAdapterView(withId(R.id.listViewVehiculos)).atPosition(1);
        evento.perform(click());

        //Guarda el string del vehiculo seleccionado
        String seleccionado = consultaDBSeleccionado(context);

        //Comprueba que el vehiculo en el fichero es el vehiculo que realmente esta seleccionado
        seleccionado.equals("Marca:Seat-Modelo:Ibiza-Matricula:1111BBB-Combustible:Gasolina95-");

        //Deseleccionamos el vehiculo
        //Hace click en el segundo vehiculo
        evento = onData(anything()).inAdapterView(withId(R.id.listViewVehiculos)).atPosition(1);
        evento.perform(click());

        String seleccionado2 = consultaDBSeleccionado(context);

        //Comprobamos que el fichero ha borrado los datos
        seleccionado2.equals("");

        context.deleteFile("vehiculos.txt");
    }


    private void escribeDatosVehiculo(String marca, String modelo, String matricula, String combustible){

        onView(withId(R.id.imageButton2)).perform(click());
        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento1 = onView(withId(R.id.idIntroduceMarca));
        evento1.perform(click());
        evento1.perform(replaceText(marca));

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento2 = onView(withId(R.id.idIntroduceModelo));
        evento2.perform(click());
        evento2.perform(replaceText(modelo));

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento4 = onView(withId(R.id.idIntroduceMatricula));
        evento4.perform(click());
        evento4.perform(replaceText(matricula));

        Espresso.closeSoftKeyboard();

        //Se hace click en el spinner
        onView(withId(R.id.idSpinnerCombustible)).perform(click());

        //Se hace click en la opción del spinner que pone GasoleoA
        onData(allOf(is(instanceOf(String.class)),
                is(combustible))).inRoot(isPlatformPopup()).perform(click());
        //Se comprueba que el texto del spinner es el adecuado
        onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString(combustible))));

    }

    private String consultaDBSeleccionado(Context context) {
        String ret = " ";
        try {
            InputStream inputStream = context.openFileInput("seleccionado.txt");

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
            Log.e("login activity", "No se ha encontrado el fichero: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "No se ha podido leer el fichero " + e.toString());
        }
        return ret;
    }

}
