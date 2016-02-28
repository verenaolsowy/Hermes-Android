package hermesolsowy.comunicador.laboratorio;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.Hashtable;
import java.util.List;

public class GrillaAlumnoActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    Alumno alumno;
    boolean modoEdicion;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla_alumno);
        alumno = (Alumno) getIntent().getExtras().getSerializable("alumno");
        modoEdicion = (Boolean)getIntent().getExtras().getBoolean("modoEdicion");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTitle(alumno.toString());
    }

    public void refresh(int pos){
        Fragment frag = mSectionsPagerAdapter.getFragment(pos);
        if(frag != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(frag)
                    .attach(frag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(modoEdicion) {
            getMenuInflater().inflate(R.menu.menu_grilla_alumno_edicion, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_grilla_alumno, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(GrillaAlumnoActivity.this, NuevoAlumnoActivity.class);
                intent.putExtra("alumno", alumno);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_edit:
                Intent intentEdicion = new Intent(GrillaAlumnoActivity.this, GrillaAlumnoActivity.class);
                intentEdicion.putExtra("alumno", alumno);
                intentEdicion.putExtra("modoEdicion", true);
                startActivity(intentEdicion);
                finish();
                return true;
            case R.id.action_alum:
                Intent intentAlumno = new Intent(GrillaAlumnoActivity.this, GrillaAlumnoActivity.class);
                intentAlumno.putExtra("alumno", alumno);
                intentAlumno.putExtra("modoEdicion", false);
                startActivity(intentAlumno);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, getResources());
        task.execute(resId);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private GridView gridView;
        private GrillaAdapter imagenesAdapter;
        private int anchoColumna;
        String nombreSolapa;
        Alumno alumno;
        boolean modoEdicion;
        private int numeroPagina;

        public PlaceholderFragment() {
        }

        public PlaceholderFragment(int numeroPagina, Alumno alum, boolean modo) {
            this.numeroPagina = numeroPagina;
            modoEdicion = modo;
            alumno = alum;
            nombreSolapa = getPageTitle(numeroPagina).toString();
        }

        public static PlaceholderFragment newInstance(int numeroPagina, Alumno alum, boolean modo) {
            PlaceholderFragment fragment = new PlaceholderFragment(numeroPagina, alum, modo);
            return fragment;
        }

        private CharSequence getPageTitle(int position) {
            if (modoEdicion) {
                switch (position) {
                    case 0:
                        return "Pista";
                    case 1:
                        return "Establo";
                    case 2:
                        return "Necesidades";
                    case 3:
                        return "Emociones";
                    case 4:
                        return alumno.toString();
                }
            } else {
                String[] solapas = alumno.getPestañas() != null ? alumno.getPestañas().split(",") : null;

                if (solapas != null && position < solapas.length) {
                    return solapas[position];
                } else if (solapas == null || position == solapas.length) {
                    return alumno.toString();
                } else {
                    return null;
                }
            }
            return null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_grilla_alumno, container, false);
            gridView = (GridView) rootView.findViewById(R.id.imagenes);
            int cant_columnas=1;
            switch (alumno.getTamañoPictogramas()) {
                case "Chico":
                    cant_columnas=5;
                    break;
                case "Mediano":
                    cant_columnas=4;
                    break;
                case "Grande":
                    cant_columnas=3;
                    break;
            }
            this.setGrilla(cant_columnas, 18);

            Database db = new Database(this.getContext());
            List<String>listaPictogramaAlumno = db.listaPictogramaAlumno(alumno.getId());
            List<Integer>listaIdImagenes = new ImageData(alumno, getActivity()).getImages(db).get(nombreSolapa.toLowerCase()).ids;
            List<String>listaNombreImagenes =  new ImageData(alumno, getActivity()).getImages(db).get(nombreSolapa.toLowerCase()).nombres;


            imagenesAdapter = new GrillaAdapter(getActivity(), listaIdImagenes, anchoColumna, listaNombreImagenes, alumno, modoEdicion, numeroPagina, listaPictogramaAlumno);
            gridView.setAdapter(imagenesAdapter);
            return rootView;

        }

        private void setGrilla(int cantidadColumnas, int paddingGrilla) {
            anchoColumna = (int) ((this.getScreenWidth() - ((cantidadColumnas + 1) * paddingGrilla)) / cantidadColumnas);
            gridView.setNumColumns(cantidadColumnas);
            gridView.setColumnWidth(anchoColumna);
            gridView.setStretchMode(GridView.NO_STRETCH);
            gridView.setPadding(paddingGrilla, paddingGrilla, paddingGrilla, paddingGrilla);
            gridView.setHorizontalSpacing(paddingGrilla);
            gridView.setVerticalSpacing(paddingGrilla);
        }

        public int getScreenWidth() {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;

            return width;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private Hashtable<Integer, Fragment> fragments = new Hashtable<Integer, Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = PlaceholderFragment.newInstance(position, alumno, modoEdicion);
            this.fragments.put(position, frag);
            return frag;
        }

        @Override
        public int getCount() {
            if(modoEdicion) {
                return 5;
            }else{
                String [] solapas = alumno.getPestañas()!=null ?  alumno.getPestañas().split(","): null;
                return solapas != null ? solapas.length + 1 : 1;
            }
        }

        public Fragment getFragment(int pos){
            return this.fragments.get(pos);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (modoEdicion) {
                switch (position) {
                    case 0:
                        return "Pista";
                    case 1:
                        return "Establo";
                    case 2:
                        return "Necesidades";
                    case 3:
                        return "Emociones";
                    case 4:
                        return alumno.toString();
                }
            }else {
                String[] solapas = alumno.getPestañas()!=null ? alumno.getPestañas().split(",") : null;

                if (solapas != null && position < solapas.length) {
                    return solapas[position];
                } else if (solapas == null || position == solapas.length) {
                    return alumno.toString();
                } else {
                    return null;
                }
            }
            return null;
        }

    }
}
