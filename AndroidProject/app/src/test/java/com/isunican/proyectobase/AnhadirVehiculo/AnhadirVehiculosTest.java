package com.isunican.proyectobase.AnhadirVehiculo;

import android.content.Context;
import android.os.Build;

import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterVehiculos;
import com.isunican.proyectobase.views.VehiclesActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class AnhadirVehiculosTest {

    //Declaracion de distintos vehiculos con diferentes datos que se estableceran posteriormente
    private Vehiculo vehiculoValidoA;
    private Vehiculo vehiculoMatriculaNoValida;
    private Vehiculo vehiculoValidoB;
    private Vehiculo vehiculoConDatoNulo;
    private Vehiculo vehiculoConDatoVacio;
    private Vehiculo vehiculoConOtroCarburante;
    private Vehiculo vehiculoNulo;

    PresenterVehiculos presenterVehiculos;

    // Declaracion de los vehiculos para probar el ticket NoVocalesMatriculas
    private Vehiculo VehiculoValidoC;
    private Vehiculo vehiculoConVocal;

    //Declaracion de distintos vehiculos para testear el ticket NoCaracteresExtranhosvehiculo
    private Vehiculo vehiculoMarcaExtranha;
    private Vehiculo vehiculoModeloExtranho;
    private Vehiculo vehiculoModeloMarcaExtranha;
    private Vehiculo vehiculoValidoC;

    @Before
    public void setUp() throws Exception{

        //Inicializacion de los vehiculos
        vehiculoValidoA = new Vehiculo("Ford", "Fiesta","1234BBC","Gasolina95");
        vehiculoMatriculaNoValida = new Vehiculo("Opel", "Corsa","JVDLHQW","Gasolina95");
        vehiculoValidoB =  new Vehiculo("Toyota", "Corolla", "1086KKH", "GasoleoA");
        vehiculoConDatoNulo =  new Vehiculo("Subaru", null,"2501RRR","GasoleoA");
        vehiculoConDatoVacio =  new Vehiculo("", "Multipla","0000QBC","GasoleoA");
        vehiculoConOtroCarburante =  new Vehiculo("Seat", "Leon","1337WWW","GasolinaFalsa");
        vehiculoNulo = null;

        // Inicializacion de los vehiculos para probar el ticket NoVocalesMatriculas
        vehiculoConVocal =  new Vehiculo("Seat", "Leon","1337AAA","Gasolina95");
        VehiculoValidoC =  new Vehiculo("Seat", "Leon","1337BBB","Gasolina95");

        //Inicializacion de los vehiculos para el ticket NoCaracteresExtranhosVehiculo
        vehiculoMarcaExtranha = new Vehiculo("Renault*/-+/","Clio", "1234TPW", "GasoleoA");
        vehiculoModeloExtranho = new Vehiculo("Renault", "Cl/-o/o--", "7654PWL", "Gasolina95");
        vehiculoModeloMarcaExtranha = new Vehiculo("Rena--ult--", "Cl-i+..o", "3426PLS", "Gasolina95");
        vehiculoValidoC = new Vehiculo("Renault", "Clio","1456WTS","Gasolina95");
    }

    @Test
    public void anhadirVehiculoTest()
    {

        VehiclesActivity activity = Robolectric.buildActivity(VehiclesActivity.class).create().resume().get();
        Context context = RuntimeEnvironment.systemContext;
        presenterVehiculos = activity.getPresenterVehiculos();


        //Caso UT.1a
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoValidoA);
        } catch(Exception e){
            fail();
        }

        //Caso UT.1b
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoValidoB);
        } catch(Exception e){
            fail();
        }

        //Caso UT.1c
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoValidoA);
            fail();
        } catch(PresenterVehiculos.VehiculoYaExiste v){

        }

        //Caso UT.1d
        try {
            presenterVehiculos.anhadirVehiculo(vehiculoMatriculaNoValida);
            fail();
        } catch(PresenterVehiculos.MatriculaNoValida m){

        }

        //Caso UT.1e
        try {
            presenterVehiculos.anhadirVehiculo(vehiculoNulo);
            fail();
        } catch(PresenterVehiculos.VehiculoNulo m){

        }

        //Caso UT.1f
        try {
            presenterVehiculos.anhadirVehiculo(vehiculoConOtroCarburante);
            fail();
        } catch(PresenterVehiculos.CombustibleNoValido m){

        }

        //Caso UT.1g
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoConDatoNulo);
            fail();
        } catch(PresenterVehiculos.DatoNulo d){

        }

        //Caso UT.1h
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoConDatoVacio);
            fail();
        } catch(PresenterVehiculos.DatoNoValido d){

        }


        /*
         * Casos de pruebas para el ticket NoCaracteresExtranhosVehiculo (Victor Perez y Daniel Llovio)
         */

        //Caso UT.1a
        try{
            presenterVehiculos.anhadirVehiculo(VehiculoValidoC);

        } catch(Exception e){
            fail();
        }

        //Caso UT.1b
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoConVocal);
            fail();
        } catch(PresenterVehiculos.VocalesEnMatricula d){

        } catch(PresenterVehiculos.CaracterEspecial e){

        }

        //Caso UT.1c
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoModeloExtranho);
            fail();
        } catch(PresenterVehiculos.CaracterEspecial e){
            
        }

        //Caso UT.1d
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoModeloMarcaExtranha);
            fail();
        } catch(PresenterVehiculos.CaracterEspecial e){

        }
    }
}
