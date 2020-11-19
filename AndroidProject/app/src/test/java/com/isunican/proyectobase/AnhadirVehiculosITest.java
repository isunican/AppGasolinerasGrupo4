package com.isunican.proyectobase;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterGasolineras;
import com.isunican.proyectobase.presenter.PresenterVehiculos;

import java.util.List;

public class AnhadirVehiculosITest {

    PresenterVehiculos presenter = new PresenterVehiculos();
        //Declaración de los vehículos
        private Vehiculo vehiculoMatriculaDistinta;
        private Vehiculo vehiculoMatriculaIgual;
        List<Vehiculo> vehiculosPrueba=presenter.getVehiculos();

        @Before
        public void setUp() throws Exception{
            //Inicializamos los vehículos
            vehiculoMatriculaDistinta=new Vehiculo(1, "Renault", "Clio", "1234AAB", "Gasolina");
            vehiculoMatriculaIgual=new Vehiculo(2, "BMW", "M3", "1234BBB", "Gasolina");
        }

        @Test
        public void matriculaUnicaTest2(){
            //Caso válido IT.1
            try{
                vehiculosPrueba.add(vehiculoMatriculaDistinta);
                fail();
            } catch (Exception e){

            };

            //Caso no válido IT.2
            try{
                vehiculosPrueba.add(vehiculoMatriculaIgual);
                fail();
            } catch (Exception e){
            };
        }
}