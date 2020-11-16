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

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.model.Gasolinera;
import com.isunican.proyectobase.model.Vehiculo;
import com.isunican.proyectobase.presenter.PresenterGasolineras;
import com.isunican.proyectobase.presenter.PresenterVehiculos;

import java.util.List;

public class VehiclesActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    static final String GASOLINA95 = "Gasolina95";
    static final String GASOLEOA = "GasoleoA";

    //Opciones del spinner
    String[] combustibles = {GASOLINA95, GASOLEOA};

    String combustibleActual = combustibles[0];

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
    }


    /**
     * Permite seleccionar el combustible del vehiculo que se va a anhadir.
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
    }

    public void myClickHandler(View view) {
        System.out.println("BOTON");
        btn_anhadirVehiculo = findViewById(R.id.imageButton2);

        btn_anhadirVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anhadirVehiculo();
            }
        });
    }

    private void anhadirVehiculo(){
        AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else{
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_alert_dialog, null);
        alert.setView(view);

        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        //Cogemos el spinner
        spiner_tipo_combustible = (Spinner) view.findViewById(R.id.idSpinnerCombustible);
        spiner_tipo_combustible.setOnItemSelectedListener(this);

        //Creamos el arrayAdapter con la lista del spinner
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, combustibles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Colocamos los datos en el spinner
        spiner_tipo_combustible.setAdapter(aa);

        txt_Marca = view.findViewById(R.id.idIntroduceMarca);
        txt_Modelo = view.findViewById(R.id.idIntroduceModelo);
        txt_Matricula = view.findViewById(R.id.idIntroduceMatricula);
        btn_aceptar = view.findViewById(R.id.idBotonAceptar);
        btn_cancelar = view.findViewById(R.id.idBotonCancelar);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mar = txt_Marca.getText().toString();
                String model = txt_Modelo.getText().toString();
                String matric = txt_Matricula.getText().toString();

                Vehiculo vehiculo = new Vehiculo(1 ,mar, model, matric, combustibleActual);
                //vehiculo.escribeVehiculo();
                //Leer json para mostrar lista

                presenterVehiculos.getVehiculos().add(vehiculo);

                //Llamada al metodo de escritura en JSON
                Toast.makeText(getApplicationContext(), "Datos añadidos", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //Llamada para escribir en el json
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

