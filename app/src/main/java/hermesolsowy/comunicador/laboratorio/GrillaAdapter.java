package hermesolsowy.comunicador.laboratorio;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GrillaAdapter extends BaseAdapter {

    private Activity activity;
    private List<Integer> listaIdImagenes = new ArrayList<Integer>();
    private List<String> listaNombreImagenes = new ArrayList<String>();
    private Alumno alumno;
    private Boolean modoEdicion;
    private int numeroPagina;
    private List<String> listaPictogramaAlumno;

    private int imageWidth;

    public GrillaAdapter(Activity activity, List<Integer> listaIdImagenes, int imageWidth, List<String> listaNombreImagenes,
                         Alumno alumno, Boolean modoEdicion, int numeroPagina, List<String> listaPictogramaAlumno) {
        this.activity = activity;
        this.listaIdImagenes = listaIdImagenes;
        this.listaNombreImagenes = listaNombreImagenes;
        this.imageWidth = imageWidth;
        this.alumno = alumno;
        this.modoEdicion = modoEdicion;
        this.numeroPagina = numeroPagina;
        this.listaPictogramaAlumno = listaPictogramaAlumno!=null ? listaPictogramaAlumno : new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return this.listaIdImagenes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaIdImagenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(activity);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        int soundId = activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", activity.getPackageName());
        String nombreContenido = activity.getResources().getResourceEntryName(soundId);
        if (modoEdicion && numeroPagina != 4 && listaPictogramaAlumno.contains(nombreContenido)) {
            imageView.setPadding(8, 8, 8, 8);
            imageView.setBackgroundColor(Color.BLACK);
        }

        ((GrillaAlumnoActivity) activity).loadBitmap(this.listaIdImagenes.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modoEdicion) {
                    int soundId = activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", activity.getPackageName());
                    String nombreContenido = activity.getResources().getResourceEntryName(soundId);
                    MediaPlayer player = MediaPlayer.create(activity, soundId);
                    player.start();
                    if (!nombreContenido.equals("si") && !nombreContenido.equals("no")) {
                        Database db = new Database(activity);
                        String categoria = db.getCategoria(nombreContenido);
                        Notificacion notificacion = new Notificacion(alumno.getApellido(), alumno.getNombre(), categoria, "Cedica", nombreContenido);
                        List<Notificacion> lista = new ArrayList<Notificacion>();
                        lista.add(notificacion);
                        new SendNotificationTask().execute(notificacion, activity.getApplicationContext());
                    }
                } else {
                    if (numeroPagina != 4) {
                        Database db = new Database(activity);
                        int soundId = activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", activity.getPackageName());
                        String nombreContenido = activity.getResources().getResourceEntryName(soundId);
                        if (listaPictogramaAlumno.contains(nombreContenido)) {
                            listaPictogramaAlumno.remove(nombreContenido);
                            db.borrarPictogramaAlumno(alumno.getId(), nombreContenido);
                            imageView.setPadding(0, 0, 0, 0);
                        } else {
                            listaPictogramaAlumno.add(nombreContenido);
                            db.cargarPictogramaAlumno(alumno.getId(), nombreContenido);
                            imageView.setPadding(8, 8, 8, 8);
                            imageView.setBackgroundColor(Color.BLACK);
                        }
                    }
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (modoEdicion && numeroPagina == 4) {
                    int soundId = activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", activity.getPackageName());
                    String nombreContenido = activity.getResources().getResourceEntryName(soundId);
                    if (!nombreContenido.equals("si") && !nombreContenido.equals("no")) {
                        Database db = new Database(activity);
                        listaPictogramaAlumno.remove(nombreContenido);
                        db.borrarPictogramaAlumno(alumno.getId(), nombreContenido);
                        imageView.setPadding(0, 0, 0, 0);
                    }
                    // ((GrillaAlumnoActivity) activity).refresh(4);
                }
                return true;
            }
        });
        return imageView;
    }
}
