package com.isunican.proyectobase.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Gasolinera;
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

    // Elementos del formulario para anhadir vehiculo
    Button  btn_cancelar, btn_aceptar;
    ImageButton btn_anhadirVehiculo;
    EditText txt_Marca, txt_Modelo, txt_Matricula;
    Spinner spiner_tipo_combustible;


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
        cargaVehiculos(presenterVehiculos.getVehiculos());

        /*
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
        */
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //No se espera de momento realizar ninguna accion. Sirve para los spinner

    }


    public void myClickHandler(View view) {
        System.out.println("BOTON");
        btn_anhadirVehiculo = findViewById(R.id.imageButton2);
        btn_aceptar = findViewById(R.id.idBotonAceptar);
        btn_cancelar = findViewById(R.id.idBotonCancelar);

        btn_anhadirVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else{
            alert = new AlertDialog.Builder(this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_alert_dialog, null);
        txt_Marca = view.findViewById(R.id.idIntroduceMarca);
        txt_Modelo = view.findViewById(R.id.idIntroduceModelo);
        txt_Matricula = view.findViewById(R.id.idIntroduceMatricula);
        btn_aceptar = view.findViewById(R.id.idBotonAceptar);
        btn_cancelar = view.findViewById(R.id.idBotonCancelar);
        //Anhadir el spinner

        alert.setView(view);

        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mar = txt_Marca.getText().toString();
                String model = txt_Modelo.getText().toString();
                String matric = txt_Matricula.getText().toString();
                Vehiculo vehiculo = new Vehiculo(1 ,mar, model, matric, "gasolina");
                presenterVehiculos.getVehiculos().add(vehiculo);
                Toast.makeText(getApplicationContext(), "Datos añadidos", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                cargaVehiculos(presenterVehiculos.getVehiculos());
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Datos eliminados", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });


    }

    private void cargaVehiculos(List<Vehiculo> vehiculos) {
        Toast toast;
        // Definimos el array adapter
        adapter = new VehiculoArrayAdapter(this,0, vehiculos);

        // Obtenemos la vista de la lista
        listViewVehiculos = findViewById(R.id.listViewVehiculos);

        // Cargamos los datos en la lista
        if (!presenterVehiculos.getVehiculos().isEmpty()) {
            // datos obtenidos con exito
            listViewVehiculos.setAdapter(adapter);
            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Carga_vehiculos), Toast.LENGTH_LONG);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //De momento no hace nada
    }


    /**
     * CargaDatosVehiculosTask
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
                    myIntent.putExtra(getResources().getString(R.string.pasoDatosVehiculo), presenterVehiculos.getVehiculos().get(position));
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
        @param opciones
        0: Introduce combustible gasolina
        1: Introduce combustible gasoleo
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

            // Asocia las variables de dicho layout
            ImageView marca = view.findViewById(R.id.logoMarca);
            TextView modelo = view.findViewById(R.id.TextModelo);
            TextView matricula = view.findViewById(R.id.textMatricula);
            TextView combustible = view.findViewById(R.id.textCombustible);

            // Y carga los datos del item
            modelo.setText(vehiculo.getModelo());
            matricula.setText(vehiculo.getMatricula());
            combustible.setText(vehiculo.getCombustible());

            // carga icono
            cargaIcono(vehiculo, marca);

            // Si las dimensiones de la pantalla son menores
            // reducimos el texto de las etiquetas para que se vea correctamente
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            /*
            if (displayMetrics.widthPixels < 720) {
                TextView tv = view.findViewById(R.id.logoMarca);
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
    }
}

