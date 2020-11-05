package com.isunican.proyectobase.Views;

import com.isunican.proyectobase.R;
import com.isunican.proyectobase.Model.*;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;


/*
------------------------------------------------------------------
    Vista de detalle

    Presenta datos de detalle de una Gasolinera concreta.
    La gasolinera a mostrar se le pasa directamente a la actividad
    en la llamada por intent (usando putExtra / getExtra)
    Para ello Gasolinera implementa la interfaz Parcelable
------------------------------------------------------------------
*/
public class DetailActivity extends AppCompatActivity {

    TextView textNombreEmpresa;
    TextView textNombreCarretera;
    TextView textNombreProvincia;
    TextView textNombreLocalidad;
    TextView textPrecioDiesel;
    TextView textPrecioGasolina;
    ImageView imagenEmpresa;
    Gasolinera gasolinera;

    /**
     * onCreate
     *
     * Crea los elementos que conforman la actividad
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // muestra el logo en el actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.por_defecto_mod);

        // obtiene el objeto Gasolinera a mostrar
        gasolinera = getIntent().getExtras().getParcelable(getResources().getString(R.string.pasoDatosGasolinera));

        //Recogemos los id de los text View.
        textNombreEmpresa = findViewById(R.id.idNombreEmpresa);
        textNombreCarretera = findViewById(R.id.idCarretera);
        textNombreProvincia = findViewById(R.id.idProvincia);
        textNombreLocalidad = findViewById(R.id.idLocalidad);
        textPrecioDiesel = findViewById(R.id.idDiesel);
        textPrecioGasolina = findViewById(R.id.idGasolina);

        //Asignamos los datos de la gasolinera a las text View.
        textNombreEmpresa.setText(gasolinera.getRotulo());
        textNombreCarretera.setText(gasolinera.getDireccion());
        textNombreProvincia.setText(gasolinera.getProvincia());
        textNombreLocalidad.setText(gasolinera.getLocalidad());

        textPrecioDiesel.setText(String.valueOf(gasolinera.getGasoleoA()));
        textPrecioGasolina.setText(String.valueOf(gasolinera.getGasolina95()));

       //textView.setText(gasolinera.toString());
        imagenEmpresa = findViewById(R.id.idFotoEmpresa);

        // carga icono
        cargaIcono();

    }

    private void cargaIcono() {
        String rotuleImageID = gasolinera.getRotulo().toLowerCase();

        // Tengo que protegerme ante el caso en el que el rotulo solo tiene digitos.
        // En ese caso getIdentifier devuelve esos digitos en vez de 0.
        int imageID = getResources().getIdentifier(rotuleImageID,
                "drawable", getPackageName());

        if (imageID == 0 || TextUtils.isDigitsOnly(rotuleImageID)) {
            imageID = getResources().getIdentifier(getResources().getString(R.string.pordefecto),
                    "drawable", getPackageName());
        }
        imagenEmpresa.setImageResource(imageID);
    }

}