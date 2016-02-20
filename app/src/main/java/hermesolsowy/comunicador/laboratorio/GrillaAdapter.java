package hermesolsowy.comunicador.laboratorio;

import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GrillaAdapter extends BaseAdapter {

    private Activity _activity;
    private List<Integer> listaIdImagenes = new ArrayList<Integer>();
    private List<String> listaNombreImagenes = new ArrayList<String>();
    private Alumno alumno;
    private Boolean modoEdiccion;

    private int imageWidth;

    public GrillaAdapter(Activity activity, List<Integer> listaIdImagenes,
                                int imageWidth, List<String> listaNombreImagenes, Alumno alumno, Boolean modoEdiccion) {
        this._activity = activity;
        this.listaIdImagenes = listaIdImagenes;
        this.listaNombreImagenes = listaNombreImagenes;
        this.imageWidth = imageWidth;
        this.alumno = alumno;
        this.modoEdiccion = modoEdiccion;
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
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        ((GrillaAlumnoActivity) _activity).loadBitmap(this.listaIdImagenes.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modoEdiccion) {
                    int soundId = _activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", _activity.getPackageName());
                    MediaPlayer mediaPlayer = MediaPlayer.create(_activity, soundId);
                    mediaPlayer.start();

                    String nombreContenido = _activity.getResources().getResourceEntryName(soundId);
                    if (nombreContenido != "si" && nombreContenido != "no") {
                        Database db = new Database(_activity);
                        String categoria = db.getCategoria(nombreContenido);
                        Notificacion notificacion = new Notificacion(alumno.getApellido(), alumno.getNombre(), categoria, "Cedica", nombreContenido);
                        List<Notificacion> lista = new ArrayList<Notificacion>();
                        lista.add(notificacion);
                        new SendNotificationTask().execute(lista, _activity.getApplicationContext());
                    }
                } else {
                    imageView.setColorFilter(2);
                    Database db = new Database(_activity);
                    int soundId = _activity.getResources().getIdentifier(listaNombreImagenes.get(position), "raw", _activity.getPackageName());
                    String nombreContenido = _activity.getResources().getResourceEntryName(soundId);
                    db.cargarPictogramaAlumno(alumno.getId(), nombreContenido);
                }
            }
        });
        return imageView;
    }
}
