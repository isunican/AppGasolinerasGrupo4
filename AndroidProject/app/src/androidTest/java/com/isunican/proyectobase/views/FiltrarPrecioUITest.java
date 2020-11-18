package com.isunican.proyectobase.views;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.R;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class FiltrarPrecioUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    //Se utilizan estos atributos para cambiar los valores del matcher
    private double comparacion_min;
    private double comparacion_max;


    @Test
    public void pruebasInterfazFiltrarPrecio() {

        Gasolinera gasolinera;
        HashSet<Gasolinera> gasolineras = new HashSet<Gasolinera>();
        comparacion_max = 1.200;
        comparacion_min = 1.1;


        //CASO 1

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento1 = onView(withId(R.id.idPrecioMin));
        evento1.perform(click());
        evento1.perform(replaceText("1.100"));

        //Hace click en la segunda gasolinera e introduce un precio máximo
        ViewInteraction evento2 = onView(withId(R.id.idPrecioMax));
        evento2.perform(click());
        evento2.perform(replaceText("1.200"));
        closeSoftKeyboard();

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 y gasoleoA estan en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText(" " + String.valueOf(gasolinera.getGasolina95() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(new TextViewValueMatcher()));
        }

        //CASO 2

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento3 = onView(withId(R.id.idPrecioMin));
        evento3.perform(click());
        evento3.perform(replaceText("100.000"));

        //Hace click en la segunda gasolinera e introduce un precio máximo
        ViewInteraction evento4 = onView(withId(R.id.idPrecioMax));
        evento4.perform(click());
        evento4.perform(replaceText("101.000"));
        closeSoftKeyboard();

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se comprueba que la listview no tiene elementos
        assertEquals(0, vista.getCount());

        //CASO 3

        //Hace click en la primera gasolinera e introduce un precio mínimo
        ViewInteraction evento5 = onView(withId(R.id.idPrecioMin));
        evento5.perform(click());
        evento5.perform(replaceText("1.300"));

        //Hace click en la segunda gasolinera e introduce un precio máximo
        ViewInteraction evento6 = onView(withId(R.id.idPrecioMax));
        evento6.perform(click());
        evento6.perform(replaceText("1.400"));
        closeSoftKeyboard();

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 y gasoleoA estan en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);

            //Se comprueba que las gasolineras obtenidas no estan en las obtenidas en el caso 1
            assertFalse(gasolineras.contains(gasolinera));
        }

        //CASO 4

        //Hace click e introduce un precio mínimo vacio
        ViewInteraction evento7 = onView(withId(R.id.idPrecioMin));
        evento7.perform(click());
        evento7.perform(replaceText(""));

        //Hace click e introduce un precio máximo
        ViewInteraction evento8 = onView(withId(R.id.idPrecioMax));
        evento8.perform(click());
        evento8.perform(replaceText("1.200"));

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        comparacion_max = 1.200;
        comparacion_min = 0;

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 y gasoleoA estan en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText(" " + String.valueOf(gasolinera.getGasolina95() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(new TextViewValueMatcher()));
        }

        //CASO 5

        //Hace click e introduce un precio mínimo
        ViewInteraction evento10 = onView(withId(R.id.idPrecioMin));
        evento10.perform(click());
        evento10.perform(replaceText("1.200"));

        //Hace click e introduce un precio máximo vacio
        ViewInteraction evento9 = onView(withId(R.id.idPrecioMax));
        evento9.perform(click());
        evento9.perform(replaceText(""));


        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        comparacion_max = Double.MAX_VALUE;
        comparacion_min = 1.200;

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 y gasoleoA estan en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(withText(" " + String.valueOf(gasolinera.getGasolina95() + "€"))));

            //Se comprueba que el precio de la gasolinera en la interfaz esta en el rango de valores
            onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(i).onChildView(withId(R.id.textViewGasolina95)).check(matches(new TextViewValueMatcher()));
        }

        //CASO 6

        //Hace click e introduce un precio mínimo y máximo vacio
        ViewInteraction evento11 = onView(withId(R.id.idPrecioMin));
        evento5.perform(click());
        evento5.perform(replaceText(""));

        //Hace click e introduce un precio máximo
        ViewInteraction evento12 = onView(withId(R.id.idPrecioMax));
        evento8.perform(click());
        evento8.perform(replaceText(""));
        closeSoftKeyboard();

        //Pulsa el boton filtrar
        onView(withId(R.id.botonFiltrarPrecio)).perform(click());

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Se comprueba que la lista esta vacio
        if (vista.getAdapter()!=null) {
            if (vista.getAdapter().getCount() > 0) {
                // listView not empty
                fail("Error, la lista debería estar vacía");
            } else {
                // listView  empty
            }
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
