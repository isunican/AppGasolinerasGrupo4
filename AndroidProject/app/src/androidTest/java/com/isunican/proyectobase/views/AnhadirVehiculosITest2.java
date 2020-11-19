package com.isunican.proyectobase.views;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterVehiculos;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AnhadirVehiculosITest2 {

    //@Rule
    //public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<VehiclesActivity> mActivityTestRuleVehicle = new ActivityTestRule<>(VehiclesActivity.class);

    private PresenterVehiculos presenter;
    private Context context;
    private VehiclesActivity activity;
    private ListView vista;

    //Declaración de los vehículos
    private Vehiculo vehiculoMatriculaDistinta;
    private Vehiculo vehiculoMatriculaIgual;
    private Vehiculo vehiculoAnhadido;


    @Before
    public void setUp() throws Exception{
        context = ApplicationProvider.getApplicationContext();
        context.deleteFile("vehiculos.txt");

        activity =  mActivityTestRuleVehicle.getActivity();
        VehiclesActivity v=((VehiclesActivity) activity);
        presenter=v.getPresenter();

        vista = (ListView) mActivityTestRuleVehicle.getActivity().findViewById(R.id.listViewVehiculos);

        //Inicializamos los vehículos
        vehiculoMatriculaDistinta=new Vehiculo("Toyota", "Celica", "1234ABC", "Gasolina95");
        vehiculoMatriculaIgual=new Vehiculo("Volkswagen", "Golf Mk2", "1234BCA", "Gasolina95");
        vehiculoAnhadido=new Vehiculo("Honda", "Civic", "1234BCA", "Gasolina95");
        presenter.anhadirVehiculo(vehiculoAnhadido);
        presenter.escribeVehiculo(vehiculoAnhadido.toString(),context);

    }
    @Test
    public void matriculaUnicaTest(){

        //Caso válido IT.1
        try{
            presenter.anhadirVehiculo(vehiculoMatriculaDistinta);
            presenter.escribeVehiculo(vehiculoMatriculaDistinta.toString(),context);

        } catch (PresenterVehiculos.VehiculoYaExiste e){
            fail();
        };

        //Caso no válido IT.2
        try{
            presenter.anhadirVehiculo(vehiculoMatriculaIgual);
            presenter.escribeVehiculo(vehiculoMatriculaIgual.toString(),context);
            fail();
        } catch (PresenterVehiculos.VehiculoYaExiste e){

        };
    }


}