package com.isunican.proyectobase.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterVehiculos;

import java.util.List;

public class VehiclesActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    // Vista de lista y adaptador para cargar datos en ella
    ListView listViewVehiculos;
    ArrayAdapter<Vehiculo> adapter;

    // Creacion del PresenterVehiculos
    PresenterVehiculos presenterVehiculos;

    // Barra de progreso circular para mostar progeso de carga
    ProgressBar progressBar;


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
        setContentView(R.layout.activity_vehicle);

        presenterVehiculos = new PresenterVehiculos();


        // Barra de progreso
        // https://materialdoc.com/components/progress/
        progressBar = new ProgressBar(VehiclesActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout layout = findViewById(R.id.activity_vehicle);
        layout.addView(progressBar, params);

        // Muestra el logo en el actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.por_defecto_mod);

        // Al terminar de inicializar todas las variables
        // se lanza una tarea para cargar los datos de los vehiculos
        // Esto se ha de hacer en segundo plano definiendo una tarea asíncrona
        new CargaDatosVehiculosTask(this).execute();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * CargaDatosVehiclesGasolinerasTask
     * <p>
     * Tarea asincrona para obtener los datos de los vehiculos
     * en segundo plano.
     * <p>
     * Redefinimos varios métodos que se ejecutan en el siguiente orden:
     * onPreExecute: activamos el dialogo de progreso
     * doInBackground: solicitamos que el presenter cargue los datos
     * onPostExecute: desactiva el dialogo de progreso,
     * muestra los vehiculos en formato lista (a partir de un adapter)
     * y define la acción al realizar al seleccionar alguna de ellas
     * <p>
     * http://www.sgoliver.net/blog/tareas-en-segundo-plano-en-android-i-thread-y-asynctask/
     */
    private class CargaDatosVehiculosTask extends AsyncTask<Void, Void, Boolean> {
        Activity activity;

        /**
         * Constructor de la tarea asincrona
         *
         * @param activity
         */
        public CargaDatosVehiculosTask(Activity activity) {
            this.activity = activity;
        }

        /**
         * onPreExecute
         * <p>
         * Metodo ejecutado de forma previa a la ejecucion de la tarea definida en el metodo doInBackground()
         * Muestra un diálogo de progreso
         * @deprecated
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
         * Llama al presenter para que lance el método de carga de los datos de los vehiculos
         * @deprecated
         * @param params
         * @return boolean
         */
        @Override
        @Deprecated
        protected Boolean doInBackground(Void... params) {
            return presenterVehiculos.cargaDatosVehiculos();
        }

        /**
         * onPostExecute
         * <p>
         * Se ejecuta al finalizar doInBackground
         * Oculta el diálogo de progreso.
         * Muestra en una lista los datos de las vehiculos cargados,
         * creando un adapter y pasándoselo a la lista.
         * Al pulsar al boton de añadir vehiculo lanzará un alert dialog
         * para introducir los datos de un nuevo vehiculo
         * @deprecated
         *
         * @param res
         */
        @Override
        @Deprecated
        protected void onPostExecute(Boolean res) {
            boolean result = res;

            // Si el progressDialog estaba activado, lo oculta
            progressBar.setVisibility(View.GONE);     // To Hide ProgressBar

            /*
             * Define el manejo de los eventos de click sobre elementos de la lista
             * En este caso, al pulsar un elemento se lanzará una actividad con una vista de detalle
             * a la que le pasamos el objeto Gasolinera sobre el que se pulsó, para que en el
             * destino tenga todos los datos que necesita para mostrar.
             * Para poder pasar un objeto Gasolinera mediante una intent con putExtra / getExtra,
             * hemos tenido que hacer que el objeto Gasolinera implemente la interfaz Parcelable
             */
            listViewVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                    /* Obtengo el elemento directamente de su posicion,
                     * ya que es la misma que ocupa en la lista
                     */
                    Intent myIntent = new Intent(VehiclesActivity.this, DetailActivity.class);
                    myIntent.putExtra(getResources().getString(R.string.pasoDatosGasolinera), presenterVehiculos.getVehiculos().get(position));
                    VehiclesActivity.this.startActivity(myIntent);

                }
            });
        }
    }


    /*
    ------------------------------------------------------------------
        VehiculosArrayAdapter

        Adaptador para inyectar los datos de los vehiculos
        en el listview del layout de vehiculos de la aplicacion
    ------------------------------------------------------------------
    */
    class VehiculoArrayAdapter extends ArrayAdapter<Vehiculo> {

        private Context context;
        private List<Vehiculo> listaVehiculos;

        // Constructor
        public VehiculoArrayAdapter(Context context, int resource, List<Vehiculo> objects) {
            super(context, resource, objects);
            this.context = context;
            this.listaVehiculos = objects;
        }

        // Llamado al renderizar la lista
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Obtiene el elemento que se está mostrando
            Vehiculo vehiculo = listaVehiculos.get(position);

            // Indica el layout a usar en cada elemento de la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.item_vehiculo, null);
            /*
            // Asocia las variables de dicho layout
            ImageView marca = view.findViewById(R.id.imageViewLogo);
            TextView modelo = view.findViewById(R.id.textViewRotulo);
            TextView matricula = view.findViewById(R.id.textViewDireccion);
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
            */
            return view;
        }
        /*
        private void cargaIcono(Vehiculo vehiculo, ImageView logo) {
            String rotuleImageID = vehiculo.getMarca().toLowerCase();

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
        */

    }
}

