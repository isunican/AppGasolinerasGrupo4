package com.isunican.proyectobase;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.isunican.proyectobase.Views.MainActivity;
import com.isunican.proyectobase.Model.Gasolinera;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class FiltrarPrecioUITest {

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

        onView(withId(R.id.idGasolina)).check(matches(withText(String.valueOf(gasolinera.getGasolina95()))));
        onView(withId(R.id.idDiesel)).check(matches(withText(String.valueOf(gasolinera.getGasoleoA()))));
    }
}
