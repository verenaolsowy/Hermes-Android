package hermesolsowy.comunicador.laboratorio;

import android.app.Activity;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Verena on 16/12/2015.
 */


public class ImageData {
    private Alumno alumno;
    private Activity activity;

    public ImageData(Alumno alumno, Activity activity) {
        this.alumno = alumno;
        this.activity = activity;
    }

    public Hashtable<String, Data> getImages(Database db) {
        Hashtable<String, Data> images = new Hashtable<String, Data>();
        ArrayList<Integer> pista = new ArrayList<>();
        ArrayList<String> pistaAudio = new ArrayList<>();
        pista.add(R.drawable.casco);
        pistaAudio.add("casco");
        pista.add(R.drawable.chapas);
        pistaAudio.add("chapas");
        pista.add(R.drawable.letras);
        pistaAudio.add("letras");
        pista.add(R.drawable.cubos);
        pistaAudio.add("cubos");
        pista.add(R.drawable.maracas);
        pistaAudio.add("maracas");
        pista.add(R.drawable.palos);
        pistaAudio.add("palos");
        pista.add(R.drawable.pato);
        pistaAudio.add("pato");
        pista.add(R.drawable.pelota);
        pistaAudio.add("pelota");
        pista.add(R.drawable.riendas);
        pistaAudio.add("riendas");
        pista.add(R.drawable.burbujas);
        pistaAudio.add("burbujas");
        pista.add(R.drawable.broches);
        pistaAudio.add("broches");
        pista.add(R.drawable.aro);
        pistaAudio.add("aro");
        pista.add(R.drawable.tarima);
        pistaAudio.add("tarima");
        Data uno = new Data(pista, pistaAudio);
        images.put("pista", uno);

        ArrayList<Integer> establo = new ArrayList<>();
        ArrayList<String> establoAudios = new ArrayList<>();
        establo.add(R.drawable.cepillo);
        establoAudios.add("cepillo");
        establo.add(R.drawable.limpieza);
        establoAudios.add("limpieza");
        establo.add(R.drawable.escarbavasos);
        establoAudios.add("escarbavasos");
        establo.add(R.drawable.montura);
        establoAudios.add("montura");
        establo.add(R.drawable.matra);
        establoAudios.add("matra");
        establo.add(R.drawable.raquetadura);
        establoAudios.add("rasquetadura");
        establo.add(R.drawable.raquetablanda);
        establoAudios.add("rasquetablanda");
        establo.add(R.drawable.pasto);
        establoAudios.add("pasto");
        establo.add(R.drawable.zanahoria);
        establoAudios.add("zanahoria");
        establo.add(R.drawable.caballo1);
        establoAudios.add("caballo1");
        establo.add(R.drawable.caballo2);
        establoAudios.add("caballo2");
        establo.add(R.drawable.caballo3);
        establoAudios.add("caballo3");
        Data dos = new Data(establo, establoAudios);
        images.put("establo", dos);

        ArrayList<Integer> necesidades = new ArrayList<>();
        ArrayList<String> necesidadesAudios = new ArrayList<>();
        necesidades.add(R.drawable.bano);
        necesidadesAudios.add("bano");
        necesidades.add(R.drawable.sedf);
        necesidadesAudios.add("sedf");
        necesidades.add(R.drawable.sedm);
        necesidadesAudios.add("sedm");
        Data tres = new Data(necesidades, necesidadesAudios);
        images.put("necesidades", tres);

        ArrayList<Integer> emociones = new ArrayList<>();
        ArrayList<String> emocionesAudios = new ArrayList<>();
        emociones.add(R.drawable.dolorido);
        emocionesAudios.add("meduelem");
        emociones.add(R.drawable.dolorida);
        emocionesAudios.add("meduelef");
        emociones.add(R.drawable.cansado);
        emocionesAudios.add("cansado");
        emociones.add(R.drawable.cansada);
        emocionesAudios.add("cansada");
        emociones.add(R.drawable.sorprendido);
        emocionesAudios.add("sorprendido");
        emociones.add(R.drawable.sorprendida);
        emocionesAudios.add("sorprendida");
        emociones.add(R.drawable.asustado);
        emocionesAudios.add("asustado");
        emociones.add(R.drawable.asustada);
        emocionesAudios.add("asustada");
        emociones.add(R.drawable.contento);
        emocionesAudios.add("contento");
        emociones.add(R.drawable.contenta);
        emocionesAudios.add("contenta");
        emociones.add(R.drawable.enojado);
        emocionesAudios.add("enojado");
        emociones.add(R.drawable.enojada);
        emocionesAudios.add("enojada");
        Data cuatro = new Data(emociones, emocionesAudios);
        images.put("emociones", cuatro);

        ArrayList<Integer> alum = new ArrayList<>();
        ArrayList<String>alumnoAudios = new ArrayList<>();
        alum.add(R.drawable.si);
        alumnoAudios.add("si");
        alum.add(R.drawable.no);
        alumnoAudios.add("no");
        ArrayList<String> pictogramas = db.listaPicogramaAlumno(alumno.getId());
        for (String pictograma:pictogramas) {
            alum.add(activity.getResources().getIdentifier(pictograma, "drawable", activity.getPackageName()));
            alumnoAudios.add(pictograma);
        }
        Data cinco = new Data(alum, alumnoAudios);
        images.put(alumno.toString(), cinco);
        return images;
    }
}