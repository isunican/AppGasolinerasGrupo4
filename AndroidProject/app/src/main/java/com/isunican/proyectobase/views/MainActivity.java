package com.isunican.proyectobase.views;

import com.isunican.proyectobase.presenter.*;
import com.isunican.proyectobase.model.*;
import com.isunican.proyectobase.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.List;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/*
------------------------------------------------------------------
    Vista principal

    Presenta los datos de las gasolineras en formato lista.

------------------------------------------------------------------
*/
public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    //String de opciones del spinner de combustibles
    String[] combustibles = {GASOLINA95, GASOLEOA};

    PresenterGasolineras presenterGasolineras;

    // Vista de lista y adaptador para cargar datos en ella
    ListView listViewGasolineras;
    ArrayAdapter<Gasolinera> adapter;

    // Barra de progreso circular para mostar progeso de carga
    ProgressBar progressBar;

    // Swipe and refresh (para recargar la lista con un swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

    // Botón a través del cuál se podrá filtrar el rango de precios con el que queremos que se muestren las gasolineras
    Button botonFiltrarPrecio;

    // Obtención de los precios max y min
    EditText precioMin;
    EditText precioMax;

    String combustibleActual = combustibles[0];

    static final String GASOLINA95 = "Gasolina95";
    static final String GASOLEOA = "GasoleoA";

    /**
     * onCreate
     * <p>
     * Crea los elementos que conforman la actividad
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.presenterGasolineras = new PresenterGasolineras();

        // Barra de progreso
        // https://materialdoc.com/components/progress/
        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout layout = findViewById(R.id.activity_precio_gasolina);
        layout.addView(progressBar, params);

        // Muestra el logo en el actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.por_defecto_mod);

        // Swipe and refresh
        // Al hacer swipe en la lista, lanza la tarea asíncrona de carga de datos
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CargaDatosGasolinerasTask(MainActivity.this).execute();
            }
        });

        // Al terminar de inicializar todas las variables
        // se lanza una tarea para cargar los datos de las gasolineras
        // Esto se ha de hacer en segundo plano definiendo una tarea asíncrona
        new CargaDatosGasolinerasTask(this).execute();

        //Cogemos el spinner
        Spinner spin = (Spinner) findViewById(R.id.idSpinnerCombustible);
        spin.setOnItemSelectedListener(this);

        //Creamos el arrayAdapter con la lista del spinner
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, combustibles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Colocamos los datos en el spinner
        spin.setAdapter(aa);


    }

    public void myClickHandler(View target) {


        precioMin = findViewById(R.id.idPrecioMin);
        precioMax = findViewById(R.id.idPrecioMax);

        String min = precioMin.getText().toString();
        String max = precioMax.getText().toString();

        if (!min.equals("") && !min.equals("")) {

            double pMin = Double.parseDouble(min);
            double pMax = Double.parseDouble(max);


            List<Gasolinera> gasolinerasFiltradas;

            try {
                switch (combustibleActual) {
                    case GASOLINA95:
                        gasolinerasFiltradas = PresenterGasolineras.filtrarCombustibleGasolina(presenterGasolineras.getGasolineras());
                        gasolinerasFiltradas = PresenterGasolineras.filtraPrecioGasolina(gasolinerasFiltradas, pMin, pMax);
                        cargaGasolineras(gasolinerasFiltradas, 2);
                        break;

                    case GASOLEOA:
                        gasolinerasFiltradas = PresenterGasolineras.filtrarCombustibleGasoleo(presenterGasolineras.getGasolineras());
                        gasolinerasFiltradas = PresenterGasolineras.filtraPrecioGasoleo(gasolinerasFiltradas, pMin, pMax);
                        cargaGasolineras(gasolinerasFiltradas, 1);
                        break;
                    default:
                }
            } catch (PresenterGasolineras.DatoNoValido e) {
                notificaDatoNoValido();
            }
        } else {
            notificaDatoNoValido();
        }


    }

    private void notificaDatoNoValido() {
        Toast toast;
        toast = Toast.makeText(getApplicationContext(), "Datos introducidos invalidos, introduzca parámetros correctos", Toast.LENGTH_LONG);
        toast.show();
    }


    /**
     * Menú action bar
     * <p>
     * Redefine métodos para el uso de un menú de tipo action bar.
     * <p>
     * onCreateOptionsMenu
     * Carga las opciones del menú a partir del fichero de recursos menu/menu.xml
     * <p>
     * onOptionsItemSelected
     * Define las respuestas a las distintas opciones del menú
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Actualiza la lista de gasolineras o muestra la Información de la aplicación,
     * dependiendo del botón pulsado.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemActualizar) {
            mSwipeRefreshLayout.setRefreshing(true);
            new CargaDatosGasolinerasTask(this).execute();
        } else if (item.getItemId() == R.id.itemInfo) {
            Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
        return true;
    }


    /**
     * Cuando una de las opciones del spinner se pulsen, se activa el método.
     * Dependiendo del combustible seleccionado, filtra las gasolineras ignorando a las
     * que no tengan ese combustible. Además, muestra en cada gasolinera el precio del combustible
     * seleccionado.
     *
     * @param arg0
     * @param arg1
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        combustibleActual = combustibles[position];
        Toast.makeText(getApplicationContext(), combustibles[position], Toast.LENGTH_LONG).show();
        List<Gasolinera> gasolineras;
        switch (combustibles[position]) {
            case GASOLINA95:
                gasolineras = PresenterGasolineras.filtrarCombustibleGasolina(presenterGasolineras.getGasolineras());
                cargaGasolineras(gasolineras, 2);
                break;
            case GASOLEOA:
                gasolineras = PresenterGasolineras.filtrarCombustibleGasoleo(presenterGasolineras.getGasolineras());
                cargaGasolineras(gasolineras, 1);
                break;
            default:
        }
    }

    private void cargaGasolineras(List<Gasolinera> gasolineras, int opciones) {
        Toast toast;
        // Definimos el array adapter
        adapter = new GasolineraArrayAdapter(this, 0, gasolineras, opciones);

        // Obtenemos la vista de la lista
        listViewGasolineras = findViewById(R.id.listViewGasolineras);

        // Cargamos los datos en la lista
        if (!presenterGasolineras.getGasolineras().isEmpty()) {
            // datos obtenidos con exito
            listViewGasolineras.setAdapter(adapter);
            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_exito), Toast.LENGTH_LONG);
        } else {
            // los datos estan siendo actualizados en el servidor, por lo que no son actualmente accesibles
            // sucede en torno a las :00 y :30 de cada hora
            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_no_accesibles), Toast.LENGTH_LONG);
        }

        // Muestra el mensaje del resultado de la operación en un toast
        if (toast != null) {
            toast.show();
        }
    }


    /**
     * Método que actua cuando un objeto desaparece del spinner. Ya que en el código nuestro las gasolineras están fijas,
     * no debe hacer nada.
     *
     * @param arg0
     */
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        //Método que actua cuando un objeto desaparece del spinner. Ya que en el código nuestro las gasolineras están fijas,
        //no debe hacer nada.
    }

    /**
     * CargaDatosGasolinerasTask
     * <p>
     * Tarea asincrona para obtener los datos de las gasolineras
     * en segundo plano.
     * <p>
     * Redefinimos varios métodos que se ejecutan en el siguiente orden:
     * onPreExecute: activamos el dialogo de progreso
     * doInBackground: solicitamos que el presenter cargue los datos
     * onPostExecute: desactiva el dialogo de progreso,
     * muestra las gasolineras en formato lista (a partir de un adapter)
     * y define la acción al realizar al seleccionar alguna de ellas
     * <p>
     * http://www.sgoliver.net/blog/tareas-en-segundo-plano-en-android-i-thread-y-asynctask/
     */
    private class CargaDatosGasolinerasTask extends AsyncTask<Void, Void, Boolean> {

        Activity activity;

        /**
         * Constructor de la tarea asincrona
         *
         * @param activity
         */
        public CargaDatosGasolinerasTask(Activity activity) {
            this.activity = activity;
        }

        /**
         * onPreExecute
         * <p>
         * Metodo ejecutado de forma previa a la ejecucion de la tarea definida en el metodo doInBackground()
         * Muestra un diálogo de progreso
         */
        @Override
        @Deprecated
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
        }

        /**
         * doInBackground
         * <p>
         * Tarea ejecutada en segundo plano
         * Llama al presenter para que lance el método de carga de los datos de las gasolineras
         *
         * @param params
         * @return boolean
         */
        @Override
        @Deprecated
        protected Boolean doInBackground(Void... params) {
            return presenterGasolineras.cargaDatosGasolineras();
        }

        /**
         * onPostExecute
         * <p>
         * Se ejecuta al finalizar doInBackground
         * Oculta el diálogo de progreso.
         * Muestra en una lista los datos de las gasolineras cargadas,
         * creando un adapter y pasándoselo a la lista.
         * Define el manejo de la selección de los elementos de la lista,
         * lanzando con una intent una actividad de detalle
         * a la que pasamos un objeto Gasolinera
         *
         * @param res
         */
        @Override
        @Deprecated
        protected void onPostExecute(Boolean res) {

            boolean result = res;

            // Si el progressDialog estaba activado, lo oculta
            progressBar.setVisibility(View.GONE);     // To Hide ProgressBar

            mSwipeRefreshLayout.setRefreshing(false);

            // Si se ha obtenido resultado en la tarea en segundo plano
            if (result) {

                List<Gasolinera> gasolinerasTemporales = presenterGasolineras.getGasolineras();

                switch (combustibleActual) {
                    case GASOLINA95:
                        gasolinerasTemporales = PresenterGasolineras.filtrarCombustibleGasolina(gasolinerasTemporales);
                        cargaGasolineras(gasolinerasTemporales, 2);
                        break;

                    case GASOLEOA:
                        gasolinerasTemporales = PresenterGasolineras.filtrarCombustibleGasoleo(gasolinerasTemporales);
                        cargaGasolineras(gasolinerasTemporales, 1);
                        break;

                    default:
                }
            }




            /*
             * Define el manejo de los eventos de click sobre elementos de la lista
             * En este caso, al pulsar un elemento se lanzará una actividad con una vista de detalle
             * a la que le pasamos el objeto Gasolinera sobre el que se pulsó, para que en el
             * destino tenga todos los datos que necesita para mostrar.
             * Para poder pasar un objeto Gasolinera mediante una intent con putExtra / getExtra,
             * hemos tenido que hacer que el objeto Gasolinera implemente la interfaz Parcelable
             */
            listViewGasolineras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                    /* Obtengo el elemento directamente de su posicion,
                     * ya que es la misma que ocupa en la lista
                     */
                    //Alternativa 1: a partir de posicion obtener algun atributo int opcionSeleccionada = ((Gasolinera) a.getItemAtPosition(position)).getIdeess();
                    //Alternativa 2: a partir de la vista obtener algun atributo String opcionSeleccionada = ((TextView)v.findViewById(R.id.textViewRotulo)).getText().toString();
                    Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                    myIntent.putExtra(getResources().getString(R.string.pasoDatosGasolinera), presenterGasolineras.getGasolineras().get(position));
                    MainActivity.this.startActivity(myIntent);

                }
            });
        }
    }


    /*
    ------------------------------------------------------------------
        GasolineraArrayAdapter

        Adaptador para inyectar los datos de las gasolineras
        en el listview del layout principal de la aplicacion
        @param opciones
        0: muestra todos los precios
        1: muestra el precio del gasoleo
        2: muestra el precio de la gasolina
    ------------------------------------------------------------------
    */
    class GasolineraArrayAdapter extends ArrayAdapter<Gasolinera> {

        private Context context;
        private List<Gasolinera> listaGasolineras;
        private int opciones = 0;

        // Constructor
        public GasolineraArrayAdapter(Context context, int resource, List<Gasolinera> objects, int opciones) {
            super(context, resource, objects);
            this.context = context;
            this.listaGasolineras = objects;
            this.opciones = opciones;
        }

        // Constructor
        public GasolineraArrayAdapter(Context context, int resource, List<Gasolinera> objects) {
            super(context, resource, objects);
            this.context = context;
            this.listaGasolineras = objects;
        }

        // Llamado al renderizar la lista
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Obtiene el elemento que se está mostrando
            Gasolinera gasolinera = listaGasolineras.get(position);

            // Indica el layout a usar en cada elemento de la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.item_gasolinera, null);

            // Asocia las variables de dicho layout
            ImageView logo = view.findViewById(R.id.imageViewLogo);
            TextView rotulo = view.findViewById(R.id.textViewRotulo);
            TextView direccion = view.findViewById(R.id.textViewDireccion);
            TextView gasoleoA = view.findViewById(R.id.textViewGasoleoA);
            TextView gasolina95 = view.findViewById(R.id.textViewGasolina95);

            // Y carga los datos del item
            rotulo.setText(gasolinera.getRotulo());
            direccion.setText(gasolinera.getDireccion());
            TextView viewGasoleo;
            TextView viewGasolina;

            viewGasoleo = view.findViewById(R.id.textViewGasoleoALabel);
            viewGasolina = view.findViewById(R.id.textViewGasolina95Label);


            TextView viewGasoleoEspacio;
            TextView viewGasolinaEspacio;

            viewGasoleoEspacio = view.findViewById(R.id.textViewGasoleoA);
            viewGasolinaEspacio = view.findViewById(R.id.textViewGasolina95);

            switch (opciones) {

                case 0:
                    gasoleoA.setText(" " + gasolinera.getGasoleoA() + getResources().getString(R.string.moneda));
                    gasolina95.setText(" " + gasolinera.getGasolina95() + getResources().getString(R.string.moneda));
                    viewGasoleo.setVisibility(View.VISIBLE);
                    viewGasolina.setVisibility(View.VISIBLE);

                    viewGasoleoEspacio.setVisibility(View.VISIBLE);
                    viewGasolinaEspacio.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    gasoleoA.setText(" " + gasolinera.getGasoleoA() + getResources().getString(R.string.moneda));
                    viewGasoleo.setVisibility(View.VISIBLE);
                    viewGasolina.setVisibility(View.GONE);

                    viewGasoleoEspacio.setVisibility(View.VISIBLE);
                    viewGasolinaEspacio.setVisibility(View.GONE);
                    break;

                case 2:
                    gasolina95.setText(" " + gasolinera.getGasolina95() + getResources().getString(R.string.moneda));
                    viewGasoleo.setVisibility(View.GONE);
                    viewGasolina.setVisibility(View.VISIBLE);

                    viewGasoleoEspacio.setVisibility(View.GONE);
                    viewGasolinaEspacio.setVisibility(View.VISIBLE);
                    break;
                default:

            }
            // carga icono
            cargaIcono(gasolinera, logo);


            // Si las dimensiones de la pantalla son menores
            // reducimos el texto de las etiquetas para que se vea correctamente
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics.widthPixels < 720) {
                TextView tv = view.findViewById(R.id.textViewGasoleoALabel);
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) tv.getLayoutParams());
                params.setMargins(15, 0, 0, 0);
                tv.setTextSize(11);
                TextView tmp;
                tmp = view.findViewById(R.id.textViewGasolina95Label);
                tmp.setTextSize(11);
                tmp = view.findViewById(R.id.textViewGasoleoA);
                tmp.setTextSize(11);
                tmp = view.findViewById(R.id.textViewGasolina95);
                tmp.setTextSize(11);
            }

            return view;
        }

        private void cargaIcono(Gasolinera gasolinera, ImageView logo) {
            String rotuleImageID = gasolinera.getRotulo().toLowerCase();

            // Tengo que protegerme ante el caso en el que el rotulo solo tiene digitos.
            // En ese caso getIdentifier devuelve esos digitos en vez de 0.
            int imageID = context.getResources().getIdentifier(rotuleImageID,
                    "drawable", context.getPackageName());

            if (imageID == 0 || TextUtils.isDigitsOnly(rotuleImageID)) {
                imageID = context.getResources().getIdentifier(getResources().getString(R.string.pordefecto),
                        "drawable", context.getPackageName());
            }
            logo.setImageResource(imageID);
        }
    }

}