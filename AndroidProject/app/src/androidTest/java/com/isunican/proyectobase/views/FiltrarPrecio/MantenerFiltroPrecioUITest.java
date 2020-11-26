package com.isunican.proyectobase.views.FiltrarPrecio;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.views.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class MantenerFiltroPrecioUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    //Se utilizan estos atributos para cambiar los valores del matcher
    private double comparacion_min;
    private double comparacion_max;


    @Test
    public void pruebasInterfazMantenerPrecio() {

        Gasolinera gasolinera;
        HashSet<Gasolinera> gasolineras = new HashSet<Gasolinera>();
        comparacion_max = 1.200;
        comparacion_min = 1.1;


        //CASO 6

        //Hace click en el primer filtro e introduce un precio mínimo
        ViewInteraction evento1 = onView(withId(R.id.idPrecioMin));
        evento1.perform(click());
        evento1.perform(replaceText("1.100"));

        //Hace click en el segundo filtro e introduce un precio máximo
        ViewInteraction evento2 = onView(withId(R.id.idPrecioMax));
        evento2.perform(click());
        evento2.perform(replaceText("1.200"));
        closeSoftKeyboard();

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 esta en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText(" " + String.valueOf(gasolinera.getGasolina95() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(new MantenerFiltroPrecioUITest.TextViewValueMatcher()));
        }

        //Se hace click en el spinner
        onView(withId(R.id.idSpinnerCombustible)).perform(click());

        //Se hace click en la opción del spinner que pone GasoleoA
        onData(allOf(is(instanceOf(String.class)), is("GasoleoA"))).perform(click());

        //Se comprueba que el texto del spinner es el adecuado
        onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString("GasoleoA"))));

        //Se recorren las gasolineras devueltas y se comprueba que gasoleoA esta en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(" " + String.valueOf(gasolinera.getGasoleoA() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(new MantenerFiltroPrecioUITest.TextViewValueMatcher()));
        }

        //CASO 7

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento3 = onView(withId(R.id.idPrecioMin));
        evento3.perform(click());
        evento3.perform(replaceText("1.000"));

        //Hace click en la segunda gasolinera e introduce un precio máximo
        ViewInteraction evento4 = onView(withId(R.id.idPrecioMax));
        evento4.perform(click());
        evento4.perform(replaceText("1.199"));
        closeSoftKeyboard();

        comparacion_min=1.000;
        comparacion_max=1.199;

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 esta en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(" " + String.valueOf(gasolinera.getGasoleoA() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasoleoA)).check(matches(new MantenerFiltroPrecioUITest.TextViewValueMatcher()));
        }

        //Se hace click en el spinner
        onView(withId(R.id.idSpinnerCombustible)).perform(click());

        //Se hace click en la opción del spinner que pone GasoleoA
        onData(allOf(is(instanceOf(String.class)), is("Gasolina95"))).perform(click());

        //Se comprueba que el texto del spinner es el adecuado
        onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString("Gasolina95"))));

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 esta en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText(" " + String.valueOf(gasolinera.getGasolina95() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(new MantenerFiltroPrecioUITest.TextViewValueMatcher()));
        }

    }

    public class TextViewValueMatcher extends TypeSafeMatcher<View> {
        @Override
        protected boolean matchesSafely(View item) {
            TextView textView = (TextView) item;
            String value = textView.getText().toString();
            value = value.replace("€","");
            boolean matching = Double.parseDouble(value)<=comparacion_max && Double.parseDouble(value)>=comparacion_min;
            return matching;
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}
