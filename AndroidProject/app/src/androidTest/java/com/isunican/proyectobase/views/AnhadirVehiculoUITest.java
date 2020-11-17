package com.isunican.proyectobase.views;

import android.content.Context;
import android.widget.ListView;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Gasolinera;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
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

public class AnhadirVehiculoUITest {

        @Rule
        public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

        @Test
        public void useAppContext() {
            Context context = ApplicationProvider.getApplicationContext();


            // Open the overflow menu OR open the options menu,
            // depending on if the device has a hardware or software overflow menu button.
            openActionBarOverflowOrOptionsMenu(context);

            // Click the item.
            onView(withText("Vehiculos")).perform(click());

            onView(withId(R.id.imageButton2)).perform(click());
            onView(withId(R.id.imageButton2)).perform(click());


            //Hace click en la primera gasolinera e introduce un precio mínimo
            ViewInteraction evento1 = onView(withId(R.id.idIntroduceMarca));
            evento1.perform(click());
            evento1.perform(replaceText("Ford"));

            //Hace click en la primera gasolinera e introduce un precio mínimo
            ViewInteraction evento2 = onView(withId(R.id.idIntroduceModelo));
            evento2.perform(click());
            evento2.perform(replaceText("Focus"));

            //Hace click en la primera gasolinera e introduce un precio mínimo
            ViewInteraction evento4 = onView(withId(R.id.idIntroduceMatricula));
            evento4.perform(click());
            evento4.perform(replaceText("1234ABC"));

            Espresso.closeSoftKeyboard();

            //Se hace click en el spinner
            onView(withId(R.id.idSpinnerCombustible)).perform(click());

            //Se hace click en la opción del spinner que pone GasoleoA
            onData(allOf(is(instanceOf(String.class)),
                    is("Gasolina95"))).inRoot(isPlatformPopup()).perform(click());
            //Se comprueba que el texto del spinner es el adecuado
            onView(withId(R.id.idSpinnerCombustible)).check(matches(withSpinnerText(containsString("Gasolina95"))));

            //Se hace click en el spinner
            onView(withId(R.id.idBotonAceptar)).perform(click());
          
        }
    }