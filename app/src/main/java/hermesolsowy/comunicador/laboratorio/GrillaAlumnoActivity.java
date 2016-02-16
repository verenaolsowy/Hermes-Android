package hermesolsowy.comunicador.laboratorio;

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

import java.util.ArrayList;
import java.util.List;

public class GrillaAlumnoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Alumno alumno;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla_alumno);
        alumno = (Alumno) getIntent().getExtras().getSerializable("alumno");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTitle(alumno.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grilla_alumno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(GrillaAlumnoActivity.this, NuevoAlumnoActivity.class);
            intent.putExtra("alumno", alumno);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        private static final String ARG_SECTION_NUMBER = "section_number";
        private GridView gridView;
        private GrillaAdapter imagenesAdapter;
        /*private List<Integer> imagenes;
        private List<String> nombreImagenes;*/
        private int anchoColumna;
        static private Alumno alumno;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Alumno alum) {
            alumno = alum;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_grilla_alumno, container, false);
            gridView = (GridView) rootView.findViewById(R.id.imagenes);
            this.setGrilla(3, 18);
            imagenesAdapter = new GrillaAdapter(getActivity(), ImageData.images.get(getArguments().getInt(ARG_SECTION_NUMBER)).ids, anchoColumna, ImageData.images.get(getArguments().getInt(ARG_SECTION_NUMBER)).nombres, alumno);
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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, alumno);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
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
            return null;
        }*/
            String[] solapas = alumno.getPestañas().split(","); //los nombres de las solapas están separadas por comas

            if (position < solapas.length) {
                return solapas[position];
            } else if (position == solapas.length) {
                return alumno.toString();
            } else {
                return null;
            }
        }

    }
}
