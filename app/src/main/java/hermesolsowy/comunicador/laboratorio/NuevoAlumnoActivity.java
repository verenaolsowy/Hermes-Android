package hermesolsowy.comunicador.laboratorio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoAlumnoActivity extends AppCompatActivity {
    TextView nombreAlumno;
    TextView apellidoAlumno;
    TextView direccionIP;
    TextView puerto;
    Database db;
    Configuracion configuracion;
    Alumno alumno;
    String pestañas;
    CheckBox pista;
    CheckBox establo;
    CheckBox necesidades;
    CheckBox emociones;
    Spinner spinnerTamaño;
    Spinner spinnerSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_alumno);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerSexo = (Spinner) findViewById(R.id.sexoAlumno);
        ArrayAdapter<CharSequence> adapterSexo = ArrayAdapter.createFromResource(this, R.array.sexo, R.layout.style_spinners);
        adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapterSexo);

        spinnerTamaño = (Spinner) findViewById(R.id.tamañoPictograma);
        ArrayAdapter<CharSequence> adapterTamaño = ArrayAdapter.createFromResource(this, R.array.tamañoPictograma, R.layout.style_spinners);
        adapterTamaño.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTamaño.setAdapter(adapterTamaño);


        db = new Database(this);
        configuracion = db.getConfiguracion();

        direccionIP = (TextView) findViewById(R.id.direccionIP);
        puerto = (TextView) findViewById(R.id.puerto);
        if (configuracion != null){
            direccionIP.setText(configuracion.getDireccionIP());
            puerto.setText(configuracion.getPuerto().toString());
        }

        nombreAlumno = (TextView) findViewById(R.id.nombreAlumno);
        apellidoAlumno = (TextView) findViewById(R.id.apellidoAlumno);
        alumno = (Alumno)getIntent().getExtras().getSerializable("alumno");

        pista = (CheckBox) findViewById(R.id.pista);
        establo = (CheckBox) findViewById(R.id.establo);
        necesidades = (CheckBox) findViewById(R.id.necesidades);
        emociones = (CheckBox) findViewById(R.id.emociones);

        if (alumno != null) {
            switch (alumno.getTamañoPictogramas()) {
                case "Chico":
                    spinnerTamaño.setSelection(0, true);
                    break;
                case "Mediano":
                    spinnerTamaño.setSelection(1, true);
                    break;
                case "Grande":
                    spinnerTamaño.setSelection(2, true);
                    break;
            }

            switch (alumno.getSexo()) {
                case "Masculino":
                    spinnerSexo.setSelection(0, true);
                    break;
                case "Femenino":
                    spinnerSexo.setSelection(1, true);
                    break;
            }
            nombreAlumno.setText(alumno.getNombre());
            apellidoAlumno.setText(alumno.getApellido());
            pestañas = alumno.getPestañas();
            String[] solapas = alumno.getPestañas() != null ? alumno.getPestañas().split(",") : new String[]{""};
            for(String solapa: solapas) {
                switch (solapa) {
                    case "pista":
                        pista.setChecked(true);
                        break;
                    case "establo":
                        establo.setChecked(true);
                        break;
                    case "necesidades":
                        necesidades.setChecked(true);
                        break;
                    case "emociones":
                        emociones.setChecked(true);
                        break;
                }
            }
        }else{
            Button botonEliminar = (Button) findViewById(R.id.eliminarAlumno);
            botonEliminar.setEnabled(false);
        }
    }


    public void guardarAlumno(){
        setearSolapas();
        Boolean guardado;
        if (alumno != null){
            guardado = modificarAlumno();
        }else{
            guardado = nuevoAlumno();
        }
        String ip = direccionIP.getText().toString();
        Integer puertoC = puerto.getText() != null ? Integer.parseInt(puerto.getText().toString()): null;

        if (configuracion == null && (ip != null && ip.length() != 0 && puertoC != null)){
            db.agregarConfiguracion(ip, puertoC);
        }else{
            if (configuracion != null && ((configuracion.getDireccionIP() != ip && ip != null && ip.length() != 0) || (configuracion.getPuerto() != puertoC && puertoC != null))){
                db.modificarConfiguracion(ip, puertoC);
            }
        }
        if(guardado) {
            if (alumno != null) {
                Intent intent = new Intent(this, GrillaAlumnoActivity.class);
                intent.putExtra("alumno", alumno);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, ListaAlumnosActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            AlertDialog.Builder dialogGuardar = new AlertDialog.Builder(this);

            dialogGuardar.setIcon(android.R.drawable.ic_dialog_alert);
            dialogGuardar.setTitle(getResources().getString(R.string.alumno_guardar_titulo));
            dialogGuardar.setMessage(getResources().getString(R.string.alumno_guardar_mensaje));
            dialogGuardar.setCancelable(false);

            dialogGuardar.setPositiveButton(getResources().getString(R.string.salir), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int boton) {
                    Intent intent;
                    if (alumno == null) {
                        intent = new Intent(NuevoAlumnoActivity.this, ListaAlumnosActivity.class);
                    } else {
                        intent = new Intent(NuevoAlumnoActivity.this, GrillaAlumnoActivity.class);
                        intent.putExtra("modoEdicion", false);
                        intent.putExtra("alumno", alumno);
                    }
                    startActivity(intent);
                    finish();
                }
            });

            dialogGuardar.setNegativeButton(R.string.seguir, null);
            dialogGuardar.show();
        }
    }

    private boolean modificarAlumno(){
        String nombre = nombreAlumno.getText().toString();
        String apellido = apellidoAlumno.getText().toString();
        String tamañoPictograma = (String)spinnerTamaño.getSelectedItem();
        String sexoAlumno = (String)spinnerSexo.getSelectedItem();
        if ((nombre != null && nombre.length() != 0) && (apellido != null && apellido.length() != 0)) {
            alumno = db.modificarAlumno(alumno.getId(), nombre, apellido, sexoAlumno, tamañoPictograma, pestañas);
            Toast.makeText(NuevoAlumnoActivity.this, R.string.alumno_guardar_confirmacion, Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }

    public boolean nuevoAlumno() {
        String nombre = nombreAlumno.getText().toString();
        String apellido = apellidoAlumno.getText().toString();
        String tamañoPictograma = (String)spinnerTamaño.getSelectedItem();
        String sexoAlumno = (String)spinnerSexo.getSelectedItem();

        if ((nombre != null && nombre.length() != 0) && (apellido != null && apellido.length() != 0)) {
            Database database = new Database(getApplicationContext());
            database.getWritableDatabase();
            database.nuevoAlumno(nombre, apellido, sexoAlumno, tamañoPictograma, pestañas);
            Toast.makeText(NuevoAlumnoActivity.this, R.string.alumno_crear_confirmacion, Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }

    }

    public void borrar(View view)
    {
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.alumno_eliminar_titulo));
        dialogEliminar.setMessage(getResources().getString(R.string.alumno_eliminar_mensaje));
        dialogEliminar.setCancelable(false);

        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                Database database = new Database(getApplicationContext());
                database.getWritableDatabase();
                database.borrarAlumno(alumno.getId());
                Toast.makeText(NuevoAlumnoActivity.this, R.string.alumno_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                Intent intent = new Intent(NuevoAlumnoActivity.this, ListaAlumnosActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_alumno, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        guardarAlumno();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home){
            this.guardarAlumno();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setearSolapas() {
        String solapas = "";

        if (pista.isChecked()) {
            solapas += "pista,";
        }
        if (establo.isChecked()) {
            solapas += "establo,";
        }
        if (necesidades.isChecked()) {
            solapas += "necesidades,";
        }
        if (emociones.isChecked()) {
            solapas += "emociones,";
        }

        if (solapas.length() > 1) {
            solapas = solapas.substring(0, solapas.length() - 1);
            pestañas = solapas;
        }else{
            pestañas = null;
        }

    }
}
