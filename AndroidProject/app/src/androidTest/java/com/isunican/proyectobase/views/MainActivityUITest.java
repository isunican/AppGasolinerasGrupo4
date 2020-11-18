package com.isunican.proyectobase.views;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.views.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import android.widget.ListView;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        Gasolinera gasolinera;

        //Hace click en la primera gasolinera
        DataInteraction evento = onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0);
        evento.perform(click());

        //Obtiene el objeto Gasolinera de la primera gasolinera de la lista
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
        gasolinera = (Gasolinera) vista.getAdapter().getItem(0);

        //Comprobaciones de los strings del detailActivity pulsado.
        onView(withId(R.id.idNombreEmpresa)).check(matches(withText(gasolinera.getRotulo())));
        onView(withId(R.id.idGasolina)).check(matches(withText(String.valueOf(gasolinera.getGasolina95()))));
        onView(withId(R.id.idDiesel)).check(matches(withText(String.valueOf(gasolinera.getGasoleoA()))));
        onView(withId(R.id.idCarretera)).check(matches(withText(gasolinera.getDireccion())));
        onView(withId(R.id.idLocalidad)).check(matches(withText(gasolinera.getLocalidad())));
        onView(withId(R.id.idProvincia)).check(matches(withText(gasolinera.getProvincia())));
    }
}
