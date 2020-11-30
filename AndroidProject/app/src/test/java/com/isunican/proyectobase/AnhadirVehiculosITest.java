package com.isunican.proyectobase;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterGasolineras;
import com.isunican.proyectobase.presenter.PresenterVehiculos;
import com.isunican.proyectobase.views.VehiclesActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class AnhadirVehiculosITest {

    private PresenterVehiculos presenter;
    private Context context;
    private VehiclesActivity activity;

    //Declaración de los vehículos
    private Vehiculo vehiculoMatriculaDistinta;
    private Vehiculo vehiculoMatriculaIgual;
    private Vehiculo vehiculoAnhadido;




    @Before
    public void setUp() throws Exception{
        activity = Robolectric.buildActivity(VehiclesActivity.class).create().resume().get();
        context= RuntimeEnvironment.systemContext;
        presenter= activity.getPresenterVehiculos();

        //Inicializamos los vehículos
        vehiculoMatriculaDistinta=new Vehiculo("Toyota", "Celica", "1234BBC", "Gasolina95");
        vehiculoMatriculaIgual=new Vehiculo("Volkswagen", "Golf Mk2", "1234BCB", "Gasolina95");
        vehiculoAnhadido=new Vehiculo("Honda", "Civic", "1234BCD", "Gasolina95");
        presenter.anhadirVehiculo(vehiculoAnhadido);
    }

    @Test
    public void matriculaUnicaTest(){

        //Caso válido IT.1
        try{
            presenter.anhadirVehiculo(vehiculoMatriculaDistinta);
        } catch (PresenterVehiculos.VehiculoYaExiste e){
            fail();
        };

        //Caso no válido IT.2
        try{
            presenter.anhadirVehiculo(vehiculoMatriculaIgual);
            fail();
        } catch (PresenterVehiculos.VehiculoYaExiste e){

        };
    }
}