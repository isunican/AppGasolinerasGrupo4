package com.isunican.proyectobase.views.FiltrarCombustible;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.widget.ListView;

import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.R;
import com.isunican.proyectobase.views.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FiltrarCombustibleUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {

        Gasolinera gasolinera;

        //CASO 1

        //Se hace click en el spinner
        onView(withId(R.id.idSpinnerCombustible)).perform(click());

        //Se hace click en la opción del spinner que pone GasoleoA
        onData(allOf(is(instanceOf(String.class)), is("GasoleoA"))).perform(click());

        //Se comprueba que el texto del spinner es el adecuado
        onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString("GasoleoA"))));

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que solo se muestran los precios de un tipo de gasolinera
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);

            //Se comprueba que los valores de la vista son iguales que los de la gasolinera obtenida y que no se muestra gasolina 95
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText((" " + String.valueOf(gasolinera.getGasoleoA()) + "€"))));
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(not(isDisplayed())));
        }

        //CASO 2

        //Se hace click en el spinner
        onView(withId(R.id.idSpinnerCombustible)).perform(click());

        //Se hace click en la opción del spinner que pone Gasolina95
        onData(allOf(is(instanceOf(String.class)), is("Gasolina95"))).perform(click());

        //Se comprueba que el texto del spinner es el adecuado
        onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString("Gasolina95"))));

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que solo se muestran los precios de un tipo de gasolinera
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);

            //Se comprueba que los valores de la vista son iguales que los de la gasolinera obtenida y que no se muestra gasoleo A
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText((" " + String.valueOf(gasolinera.getGasolina95()) + "€"))));
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(not(isDisplayed())));
        }
    }
}
