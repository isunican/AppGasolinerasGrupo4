package com.isunican.proyectobase.views.SeleccionarVehiculo;

import android.content.Context;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.views.VehiclesActivity;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SeleccionarVehiculoUITest {

    //@Rule
    //public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<VehiclesActivity> mActivityTestRuleVehicle = new ActivityTestRule<>(VehiclesActivity.class);

    @Test
    public void SeleccionarVehiculoTest() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteFile("vehiculos.txt");
        // Open the overflow menu OR open the options menu,
        // depending on if the device has a hardware or software overflow menu button.
        //openActionBarOverflowOrOptionsMenu(context);

        // Click the item.
        //onView(withText("Vehiculos")).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro

        ListView vista = (ListView) mActivityTestRuleVehicle.getActivity().findViewById(R.id.listViewVehiculos);

        //CASO UIT.1
        String marca = "Seat";
        String modelo = "Leon";
        String matricula = "1235TPL";
        String combustible = "GasoleoA";

        escribeDatosVehiculo(marca, modelo, matricula, combustible);
        onView(withId(R.id.idBotonAceptar)).perform(click());

        //Se recorren la lista de vehiculos de la app y se meten en la lista

        List<Vehiculo> vehiculosIniciales = new ArrayList<Vehiculo>();
        cargaListaVehiculos(vista, vehiculosIniciales);

        //Obtenemos el primer coche y en este caso unico
        onData(anything()).inAdapterView(withId(R.id.listViewVehiculos)).atPosition(0).onChildView(withId(R.id.vehiculoSeleccionado)).check(matches(withTagValue(CoreMatchers.<Object>equalTo(R.drawable.boton2))));

        //CASO UIT.2
        marca = "Ford";
        modelo ="Focus";
        matricula= "1111PPP";
        combustible = "GasoleoA";
        escribeDatosVehiculo(marca, modelo, matricula, combustible);
        onView(withId(R.id.idBotonAceptar)).perform(click());

        cargaListaVehiculos(vista, vehiculosIniciales);

        // Obtenemos el segundo coche que no deberia de estar seleccionado
        //onData(anything()).inAdapterView(withId(R.id.listViewVehiculos)).atPosition(1).onChildView(withId(R.id.vehiculoSeleccionado)).check((ViewAssertion) withTagValue(CoreMatchers.<Object>equalTo(R.drawable.boton1)));
        context.deleteFile("vehiculos.txt");
    }

    private void cargaListaVehiculos(ListView vista, List<Vehiculo> vehiculosIniciales) {
        for(int i = 0; i < vista.getAdapter().getCount(); i++) {
            Vehiculo vehiculo = (Vehiculo) vista.getAdapter().getItem(i);
            vehiculosIniciales.add(vehiculo);
        }
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
}