package com.isunican.proyectobase;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.Views.DetailActivity;
import com.isunican.proyectobase.Views.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        Gasolinera gasolinera;

        //Hace click en la primera gasolinera
        //¿Como hacer click a una posición especifica de la lista?
        onView(withId(R.id.listViewGasolineras)).perform(click());

        //Obtiene el objeto Gasolinera del click pulsado
        //¿Como obtener el objeto Activity especifico del elemento al que hemos hecho click en la lista?
        //¿Como llamar al getIntent y get Resources sin una clase activity?

        gasolinera = mActivityTestRule.getIntent().getExtras().getParcelable(getResources().getString(R.string.pasoDatosGasolinera));

        //Comprobaciones de los strings del detailActivity pulsado.
        onView(withId(R.id.idNombreEmpresa)).check(matches(withText("repsol")));

        onView(withId(R.id.txtOperador1)).perform(replaceText("3.0"));

        // Select Operation
        onView(withId(R.id.spnOperacion)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("+"))).perform(click());

        // Call operation
        onView(withId(R.id.btnCalcula)).perform(click(), closeSoftKeyboard());

        // Check behaviour
        onView(withId(R.id.txtResultado)).check(matches(withText("5.0")));
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        onView(withId(R.id.btnSaludar)).perform(click);onView(withId(R.id.txtSaludo)).check(matches(withText(“”)));
        assertEquals("com.isunican.proyectobase", appContext.getPackageName());
    }



}
