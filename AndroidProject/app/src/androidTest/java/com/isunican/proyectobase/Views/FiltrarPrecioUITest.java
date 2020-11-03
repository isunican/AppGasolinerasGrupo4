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
import android.widget.TextView;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.R;
import com.isunican.proyectobase.Views.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

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
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FiltrarPrecioUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {

        Gasolinera gasolinera;

        //Hace click en la primera gasolinera e introduce un precio mínimo
        DataInteraction evento1 = onData(anything()).inAdapterView(ViewMatchers.withId(R.id.idPrecioMin));
        evento1.perform(click());
        evento1.perform(replaceText("1.8"));

        //Hace click en la segunda gasolinera e introduce un precio máximo
        DataInteraction evento2 = onData(anything()).inAdapterView(ViewMatchers.withId(R.id.idPrecioMax));
        evento2.perform(click());
        evento2.perform(replaceText("2"));

        Set<Gasolinera> gasolineras = new HashSet<Gasolinera>();

        //Obtiene la lista de cada objeto Gasolinera después de aplicar el filtro
        ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);

        //Hace click en el filtro de GasoleoA
        //DataInteraction evento3 = onData(anything()).inAdapterView(ViewMatchers.withId(R.id.));
        //evento3.perform(click());
        //evento3.perform(replaceText("2"));

        //Se recorren las gasolineras devueltas y se comprueba que gasolina95 y gasoleoA estan en el rango del filtro
        for(int i=0;i<vista.getAdapter().getCount();i++){
            gasolinera = (Gasolinera) vista.getAdapter().getItem(i);
            gasolineras.add(gasolinera);

            //Se comprueba que los valores de la vista son iguales que los de la gasolinera obtenida
            //onView(withId(R.id.textViewGasolina95)).check(matches(withText(String.valueOf(gasolinera.getGasoleoA()))));
            //onView(withId(R.id.textViewGasoleoA)).check(matches(withText(String.valueOf(gasolinera.getGasoleoA()))));

            //Assert, se comprueba que el precio de la gasolinera es, TODO TENER EN CUENTA EL TIPO DE GASOLINA
            assertTrue(gasolinera.getGasoleoA()<2);
            assertTrue(gasolinera.getGasoleoA()>1.8);

            assertTrue(gasolinera.getGasolina95()<2);
            assertTrue(gasolinera.getGasolina95()>1.8);

            //onView(withId(R.id.textViewGasolina95)).check(matches(new TextViewValueMatcher()));


        }
    }

    public class TextViewValueMatcher extends TypeSafeMatcher<View> {
        @Override
        protected boolean matchesSafely(View item) {
            TextView textView = (TextView) item;
            String value = textView.getText().toString();
            boolean matching = Double.parseDouble(value)<2 && Double.parseDouble(value)>1.8;
            return matching;
        }

        @Override
        public void describeTo(Description description) {

        }
    }
}
