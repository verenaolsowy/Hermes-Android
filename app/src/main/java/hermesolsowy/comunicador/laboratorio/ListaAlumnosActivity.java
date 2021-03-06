package hermesolsowy.comunicador.laboratorio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ListaAlumnosActivity extends AppCompatActivity {
    ListView listaAlumnos;
    Button nuevoAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alumnos);
        Database db = new Database(this);
        ArrayList<Alumno> lista = db.listaAlumnos();
        listaAlumnos = (ListView) findViewById(R.id.listaAlumnos);
        ListAdapter adapter = new ArrayAdapter<Alumno>(this, R.layout.style_lista_alumno, lista);
        listaAlumnos.setAdapter(adapter);
        listaAlumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListaAlumnosActivity.this, GrillaAlumnoActivity.class);
                intent.putExtra("alumno", (Alumno) adapterView.getAdapter().getItem(position));
                intent.putExtra("modoEdicion", false);
                startActivity(intent);
            }
        });
        setTitle("Lista de Alumnos");
        nuevoAlumno = (Button) findViewById(R.id.nuevoAlumno);
        nuevoAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlumnosActivity.this, NuevoAlumnoActivity.class);
                intent.putExtra("alumno",(Alumno) null);
                startActivity(intent);
                finish();
            }
        });
    }
}
