package com.isunican.proyectobase.Views;

import com.isunican.proyectobase.Presenter.*;
import com.isunican.proyectobase.Model.*;
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

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    String[] combustibles = {"Gasolina95", "GasoleoA"};

    PresenterGasolineras presenterGasolineras;

    // Vista de lista y adaptador para cargar datos en ella
    ListView listViewGasolineras;
    ArrayAdapter<Gasolinera> adapter;

    // Barra de progreso circular para mostar progeso de carga
    ProgressBar progressBar;

    // Swipe and refresh (para recargar la lista con un swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;

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

        Toast.makeText(getApplicationContext(), combustibles[position], Toast.LENGTH_LONG).show();
        List<Gasolinera> gasolineras;
        switch (combustibles[position]) {
            case "Gasolina95":
                gasolineras = filtrarCombustibleGasolina(presenterGasolineras.getGasolineras());
                cargaGasolineras(gasolineras, 2);
                break;
            case "GasoleoA":
                gasolineras = filtrarCombustibleGasoleo(presenterGasolineras.getGasolineras());
                cargaGasolineras(gasolineras, 1);
                break;
        }
    }

    private void cargaGasolineras(List<Gasolinera> gasolineras, int opciones) {
        Toast toast;
        // Definimos el array adapter
        adapter = new GasolineraArrayAdapter(this, 0, (ArrayList<Gasolinera>) gasolineras, opciones);

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
     * Retorna la lista de gasolineras eliminando las gasolineras que no tengan gasolina.
     */
    private List<Gasolinera> filtrarCombustibleGasolina(List<Gasolinera> gasolineras) {
        List<Gasolinera> gasolinerasFiltradas = new ArrayList<Gasolinera>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            if (g.getGasolina95() != 0.0) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }

    /**
     * Retorna la lista de gasolineras eliminando las gasolineras que no tengan gasoleo.
     */
    private List<Gasolinera> filtrarCombustibleGasoleo(List<Gasolinera> gasolineras) {
        List<Gasolinera> gasolinerasFiltradas = new ArrayList<Gasolinera>();

        //Añadimos todas las gasolineras que tengan el combustible deseado.
        for (Gasolinera g : gasolineras) {
            if (g.getGasoleoA() != 0.0) {
                gasolinerasFiltradas.add(g);
            }
        }
        return gasolinerasFiltradas;
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
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
         *
         */
        @Override
        protected void onPostExecute(Boolean res) {
            Toast toast;

            // Si el progressDialog estaba activado, lo oculta
            progressBar.setVisibility(View.GONE);     // To Hide ProgressBar

            mSwipeRefreshLayout.setRefreshing(false);

            // Si se ha obtenido resultado en la tarea en segundo plano
            if (res) {

                // Definimos el array adapter
                adapter = new GasolineraArrayAdapter(activity, 0, (ArrayList<Gasolinera>) presenterGasolineras.getGasolineras());

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
            } else {
                // error en la obtencion de datos desde el servidor
                toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_no_obtenidos), Toast.LENGTH_LONG);
            }

            // Muestra el mensaje del resultado de la operación en un toast
            if (toast != null) {
                toast.show();
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
                     * Alternativa 1: a partir de posicion obtener algun atributo int opcionSeleccionada = ((Gasolinera) a.getItemAtPosition(position)).getIdeess();
                     * Alternativa 2: a partir de la vista obtener algun atributo String opcionSeleccionada = ((TextView)v.findViewById(R.id.textViewRotulo)).getText().toString();
                     */
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

            switch (opciones) {

                case 0:
                    gasoleoA.setText(" " + gasolinera.getGasoleoA() + getResources().getString(R.string.moneda));
                    gasolina95.setText(" " + gasolinera.getGasolina95() + getResources().getString(R.string.moneda));
                    break;

                case 1:
                    gasoleoA.setText(" " + gasolinera.getGasoleoA() + getResources().getString(R.string.moneda));
                    break;

                case 2:
                    gasolina95.setText(" " + gasolinera.getGasolina95() + getResources().getString(R.string.moneda));
                    break;

            }
            // carga icono
            {
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
    }

}