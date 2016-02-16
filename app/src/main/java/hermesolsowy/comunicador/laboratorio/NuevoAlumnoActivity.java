package hermesolsowy.comunicador.laboratorio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_alumno);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        if (alumno != null){
            nombreAlumno.setText(alumno.getNombre());
            apellidoAlumno.setText(alumno.getApellido());
        }
    }


    public void guardar(View view){
        if (alumno != null){
            modificarAlumno();
        }else{
            nuevoAlumno();
        }
        String ip = direccionIP.getText().toString();
        Integer puertoC = Integer.parseInt(puerto.getText().toString());
        if (configuracion == null && (ip != null && ip != "" && puertoC != null)){
            db.agregarConfiguracion(ip, puertoC);
        }else{
            if((configuracion.getDireccionIP() != ip && ip != null && ip != "") || (configuracion.getPuerto() != puertoC && puertoC != null)){
                db.modificarConfiguracion(ip, puertoC);
            }
        }
        if (alumno != null){
            Intent intent = new Intent(this, GrillaAlumnoActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, ListaAlumnosActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void modificarAlumno(){
        String nombre = nombreAlumno.getText().toString();
        String apellido = apellidoAlumno.getText().toString();
        String tamañoPictograma = "";
        alumno = db.modificarAlumno(alumno.getId(), nombre, apellido, "Femenino", tamañoPictograma, "establo");
        Toast.makeText(NuevoAlumnoActivity.this, R.string.alumno_guardar_confirmacion, Toast.LENGTH_SHORT).show();
    }

    public void nuevoAlumno() {
        String nombre = nombreAlumno.getText().toString();
        String apellido = apellidoAlumno.getText().toString();
        String tamañoPictograma = "";

        if ((nombre != null && nombre != "") || (apellido != null && apellido != "")) {
            Database database = new Database(getApplicationContext());
            database.getWritableDatabase();
            database.nuevoAlumno(nombre, apellido, "Femenino", tamañoPictograma, "establo");
            Toast.makeText(NuevoAlumnoActivity.this, R.string.alumno_crear_confirmacion, Toast.LENGTH_SHORT).show();

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
        if (alumno != null){
            Intent intent = new Intent(this, GrillaAlumnoActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, ListaAlumnosActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
