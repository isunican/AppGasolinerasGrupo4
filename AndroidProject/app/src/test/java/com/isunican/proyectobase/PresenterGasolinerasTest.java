package com.isunican.proyectobase;

import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.presenter.PresenterGasolineras;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PresenterGasolinerasTest {

    // Declaracion de los objetos que vamos a usar en los test
    private List<Gasolinera> gasolinerasCompleto = new ArrayList<Gasolinera>();     //Lista de Gasolineras con gasolineras con distintos combustibles
    private List<Gasolinera> gasolinerasGasoleo = new ArrayList<Gasolinera>();     // Lista de Gasolineras solo con gasoleoA
    private List<Gasolinera> gasolinerasGasolina = new ArrayList<Gasolinera>();   // Lista Gasolineras solo gasolina95


    private List<Gasolinera> gasolinerasPrecioCompleto = new ArrayList<Gasolinera>();     //Lista de Gasolineras con gasolineras con distintos combustibles
    private List<Gasolinera> gasolinerasPrecioGasolina = new ArrayList<Gasolinera>();     // Lista de Gasolineras solo con gasoleoA
    private List<Gasolinera> gasolinerasPrecioGasoleo = new ArrayList<Gasolinera>();   // Lista Gasolineras solo gasolina95
    private List<Gasolinera> gasolinerasVacias = new ArrayList<Gasolinera>();         // Lista de gasolineras vacia

    //Declaracion de distintas gasolineras con distintos combustibles
    private Gasolinera gasoleo;
    private Gasolinera gasoleo2;
    private Gasolinera gasoleo3;

    private Gasolinera gasolina;
    private Gasolinera gasolina2;
    private Gasolinera gasolina3;

    @Before
    public void setUp() throws Exception{

        //Inicializacion de las gasolineras y las listas
        gasoleo = new Gasolinera(0, "","","",1.0, 0.0, "");
        gasoleo2 = new Gasolinera(0, "","","",1.5, 0.0, "");
        gasoleo3 =  new Gasolinera(0, "","","",1.65, 0.0, "");

        gasolina = new Gasolinera(0, "","","",0.0, 1.0, "");
        gasolina2 = new Gasolinera(0, "","","",0.0, 1.50, "");
        gasolina3 = new Gasolinera(0, "","","",0.0, 1.6, "");

        //Añadimos las distintas gasolineras para el test filtrarPorTipoDeCombustible a las listas de gasolineras.
        gasolinerasCompleto.add(gasoleo);
        gasolinerasCompleto.add(gasoleo2);
        gasolinerasCompleto.add(gasolina);
        gasolinerasCompleto.add(gasolina2);

        gasolinerasGasoleo.add(gasoleo);
        gasolinerasGasoleo.add(gasoleo2);

        gasolinerasGasolina.add(gasolina);
        gasolinerasGasolina.add(gasolina2);

        //Añadimos las distintas gasolineras para probar test filtrarPorPrecio a las listas de gasolineras.
        gasolinerasPrecioCompleto.add(gasoleo);
        gasolinerasPrecioCompleto.add(gasoleo2);
        gasolinerasPrecioCompleto.add(gasoleo3);
        gasolinerasPrecioCompleto.add(gasolina);
        gasolinerasPrecioCompleto.add(gasolina2);
        gasolinerasPrecioCompleto.add(gasolina3);

        gasolinerasPrecioGasolina.add(gasolina);
        gasolinerasPrecioGasolina.add(gasolina2);

        gasolinerasPrecioGasoleo.add(gasoleo);
        gasolinerasPrecioGasoleo.add(gasoleo2);
    }

    @Test
    public void filtrarCombustibleGasolinaTest()
    {
        // Caso valido UT.1a
        assertEquals(gasolinerasGasolina, PresenterGasolineras.filtrarCombustibleGasolina(gasolinerasCompleto));

        // Caso valido UT.1b
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtrarCombustibleGasolina(gasolinerasGasoleo));

        // Caso valido UT.1c
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtrarCombustibleGasolina(gasolinerasVacias));
    }

    @Test
    public void filtrarCombustibleGasoleoTest()
    {
        // Caso valido UT.2a
        assertEquals(gasolinerasGasoleo, PresenterGasolineras.filtrarCombustibleGasoleo(gasolinerasCompleto));

        // Caso valido UT.2b
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtrarCombustibleGasoleo(gasolinerasGasolina));

        // Caso valido UT.2c
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtrarCombustibleGasoleo(gasolinerasVacias));
    }


    @Test
    public void filtrarPrecioGasolina() throws PresenterGasolineras.DatoNoValido{

        //Caso valido UT.1a
        assertEquals(gasolinerasPrecioGasolina, PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 1.0, 1.5));

        //Caso valido UT.1b
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 100.0, 200.0));

        //Caso valido UT.1c
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioGasoleo, 0.5,1.0));

        //Caso valido UT.1d
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasolina(gasolinerasVacias, 1.0,1.5));


        //Caso no valido Ut.1e
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 2.0,1.5);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

        //Caso no valido Ut.1f
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, -1.0,1.0);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

        //Caso no valido Ut.1g
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 1.0,-1.5);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

    }

    @Test
    public void filtrarPrecioGasoleo() throws PresenterGasolineras.DatoNoValido{

        //Caso valido UT.2a
        assertEquals(gasolinerasPrecioGasoleo, PresenterGasolineras.filtraPrecioGasoleo(gasolinerasPrecioCompleto, 1.0, 1.5));

        //Caso valido UT.2b
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 100.0, 200.0));

        //Caso valido UT.2c
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasoleo(gasolinerasPrecioGasolina, 0.5,1.0));

        //Caso valido UT.2d
        assertEquals(gasolinerasVacias, PresenterGasolineras.filtraPrecioGasoleo(gasolinerasVacias, 1.0,1.5));

        //Caso no valido Ut.2e
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 2.0,1.5);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

        //Caso no valido Ut.2f
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, -1.0,1.0);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

        //Caso no valido Ut.2g
        try{
            PresenterGasolineras.filtraPrecioGasolina(gasolinerasPrecioCompleto, 1.0,-1.5);
            fail();
        } catch (PresenterGasolineras.DatoNoValido e){

        };

    }
}

