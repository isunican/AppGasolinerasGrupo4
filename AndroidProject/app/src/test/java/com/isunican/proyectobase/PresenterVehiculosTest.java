package com.isunican.proyectobase;

import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterVehiculos;
import com.isunican.proyectobase.views.VehiclesActivity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PresenterVehiculosTest {

    //Declaracion de distintos vehiculos con diferentes datos que se estableceran posteriormente
    private Vehiculo vehiculoA;
    private Vehiculo vehiculoB;
    private Vehiculo vehiculoC;
    private Vehiculo vehiculoConDatoNulo;
    private Vehiculo vehiculoNulo;
    private PresenterVehiculos presenterVehiculos;


    @Before
    public void setUp() throws Exception{

        //Inicializacion de los vehiculos
        vehiculoA = new Vehiculo("Ford", "Fiesta","1234ABC","gasolina95");
        vehiculoB = new Vehiculo("Opel", "Corsa","ESTODAERROR","gasolina95");
        vehiculoC =  new Vehiculo("Toyota", "Corolla", "1086AEH", "gasoleoA");
        vehiculoConDatoNulo =  new Vehiculo("Subaru", null,"2501ERR","gasoleoA");
        vehiculoNulo = null;
    }

    @Test
    public void anhadirVehiculoTest()
    {
        //Caso UT.1a
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoA);
        } catch(Exception e){
            fail();
        }

        //Caso UT.1b
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoC);
        } catch(Exception e){
            fail();
        }

        //Caso UT.1c
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoA);
            fail();
        } catch(PresenterVehiculos.VehiculoYaExiste v){

        }

        //Caso UT.1d
        try {
            presenterVehiculos.anhadirVehiculo(vehiculoB);
            fail();
        } catch(PresenterVehiculos.MatriculaNoValida m){

        }
        //Caso UT.1e
        try{
            presenterVehiculos.anhadirVehiculo(vehiculoConDatoNulo);
            fail();
        } catch(PresenterVehiculos.DatoNoValido d){

        }
    }

    @Test
    public void cargarVehiculosTest()
    {

    }
}
