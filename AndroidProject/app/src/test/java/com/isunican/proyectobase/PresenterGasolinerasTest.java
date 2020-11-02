package com.isunican.proyectobase;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.Presenter.PresenterGasolineras;

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
    private PresenterGasolineras sut;

    private List<Gasolinera> gasolinerasCompleto = new ArrayList<Gasolinera>();     //Lista de Gasolineras con gasolineras con distintos combustibles
    private List<Gasolinera> gasolinerasGasoleo = new ArrayList<Gasolinera>();     // Lista de Gasolineras solo con gasoleoA
    private List<Gasolinera> gasolinerasGasolina = new ArrayList<Gasolinera>();   // Lista Gasolineras solo gasolina95
    private List<Gasolinera> gasolinerasVacias = new ArrayList<Gasolinera>();         // Lista de gasolineras vacia

    //Declaracion de distintas gasolineras con distintos combustibles
    private Gasolinera gasoleo;
    private Gasolinera gasoleo2;
    private Gasolinera gasolina;
    private Gasolinera gasolina2;

    @Before
    public void setUp() throws Exception{
        sut = new PresenterGasolineras();
        //Inicializacion de las gasolineras y las listas
        gasoleo = new Gasolinera(0, "","","",1.0, 0.0, "");
        gasoleo2 = new Gasolinera(0, "","","",2.0, 0.0, "");
        gasolina = new Gasolinera(0, "","","",0.0, 1.0, "");
        gasolina2 = new Gasolinera(0, "","","",0.0, 2.0, "");

        //Añadimos las distintas gasolineras a las listas de gasolineras.
        gasolinerasCompleto.add(gasoleo);
        gasolinerasCompleto.add(gasoleo2);
        gasolinerasCompleto.add(gasolina);
        gasolinerasCompleto.add(gasolina2);

        gasolinerasGasoleo.add(gasoleo);
        gasolinerasGasoleo.add(gasoleo2);

        gasolinerasGasolina.add(gasolina);
        gasolinerasGasolina.add(gasolina2);
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
}