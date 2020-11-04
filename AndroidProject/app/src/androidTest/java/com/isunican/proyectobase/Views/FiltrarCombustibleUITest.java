package com.isunican.proyectobase.Views;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;


public class FiltrarCombustibleUITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {

        Gasolinera gasolinera;

        //Hace click en el filtro de Gasoleo A, TODO UTILIZAR BOTON DE FILTRO
        DataInteraction evento1 = onData(anything()).inAdapterView(ViewMatchers.withId(R.id.idSpinnerCombustible));
        evento1.perform(click());

        //Hace click en el filtro de Gasolina 95, TODO LO MISMO QUE ANTES
        //DataInteraction evento2 = onData(anything()).inAdapterView(ViewMatchers.withId(R.id.));
        //evento2.perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que las gasolineras tienen las indicadas
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            //Assert, TODO COMPROBAR QUE TIENE TIPO DE GASOLINA INDICADO
            onView(withId(R.id.idGasolina)).check(matches(withText(String.valueOf(gasolinera.getGasolina95()))));
            onView(withId(R.id.idDiesel)).check(matches(withText(String.valueOf(gasolinera.getGasoleoA()))));
        }
    }
}
