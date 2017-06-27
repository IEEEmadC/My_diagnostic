package org.dev4u.hv.my_diagnostic.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;

import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;

import static utils.ConnectionSettings.CODIGO_DETALLE;
import static utils.ConnectionSettings.EXTRA_ID;


public class DiseaseDetailFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private DiseaseAdapter diseaseAdapter;
    public DiseaseDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_disease_detail, container, false);
        String variable  = getArguments().getString("posicion");
        Toast.makeText(getContext(), "detail:" + variable, Toast.LENGTH_SHORT).show();
        return view;
    }

    /**
     * Inicia una nueva instancia de la actividad
     *
     * @param activity Contexto desde donde se lanzará
     * @param id_symptom   Identificador de la meta a detallar
     */
    /*public static void launch(FragmentActivity activity, String id_symptom) {
        Intent intent = getLaunchIntent(activity, id_symptom);
        activity.startActivityForResult(intent, CODIGO_DETALLE);
    }

    public static Intent getLaunchIntent(Context context, String id_symptom) {
        Intent intent = new Intent(context, DiseaseDetailFragment.class);
        intent.putExtra(EXTRA_ID, id_symptom);
        return intent;
    }*/


}
