package com.isunican.proyectobase;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;

import androidx.test.espresso.DataInteraction;
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

import static org.hamcrest.Matchers.anything;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

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
    DataInteraction evento = onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0);

    //Obtiene el objeto Gasolinera de la primera gasolinera de la lista
    ListView vista = (ListView) mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
    gasolinera = (Gasolinera) vista.getAdapter().getItem(0);

    //Comprobaciones de los strings del detailActivity pulsado.
    onView(withId(R.id.idNombreEmpresa)).check(matches(withText(gasolinera.getRotulo())));
    onView(withId(R.id.idGasolina)).check(matches(withText(String.valueOf(gasolinera.getGasolina95()))));

        /*
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
        */
  }



}