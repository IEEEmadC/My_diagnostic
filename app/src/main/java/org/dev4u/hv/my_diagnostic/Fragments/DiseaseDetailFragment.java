package org.dev4u.hv.my_diagnostic.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import db.Disease;
import db.MedicalHistory;
import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;

import static utils.ConnectionSettings.CODIGO_DETALLE;
import static utils.ConnectionSettings.EXTRA_ID;


public class DiseaseDetailFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private DiseaseAdapter diseaseAdapter;
    private TextView lblDiseaseName;
    private TextView lblDiseaseCategory;
    private TextView lblDiseaseDescription;
    private TextView lblDiseasePercentage;
    private TextView lblAddToHistory;
    private String id_disease;
    private CoordinatorLayout coordinatorLayout;

    public DiseaseDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_disease_detail, container, false);

        id_disease              = getArguments().getString("ID_DISEASE");
        lblDiseaseName          = (TextView) view.findViewById(R.id.lblDetailDiseaseName);
        lblDiseaseCategory      = (TextView) view.findViewById(R.id.lblDetailCategory);
        lblDiseaseDescription   = (TextView) view.findViewById(R.id.lblDetailDescription);
        lblAddToHistory         = (TextView) view.findViewById(R.id.lblAddToHistory);
        coordinatorLayout       = (CoordinatorLayout) view.findViewById(R.id.frm_detail_disease);

        Disease disease = DiseaseUtilitesSingleton.getInstance().getDisease(id_disease);
        if(disease!=null){
            lblDiseaseName.setText(disease.getName_disease());
            lblDiseaseCategory.setText(disease.getCategory_name());
            lblDiseaseDescription.setText(disease.getDescription());
            lblAddToHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToHistory();
                }
            });
        }
        return view;
    }

    private void addToHistory(){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        MedicalHistory medicalHistory = new MedicalHistory(null,"No Title ",
                "No description"
                ,id_disease
                ,DiseaseUtilitesSingleton.getInstance().getActiveUser().getUsername()
                ,date);
        Log.d("Username ","Antes de guardar ["+DiseaseUtilitesSingleton.getInstance().getActiveUser().getUsername()+"]");
        medicalHistory.setName_disease(lblDiseaseName.getText().toString());
        DiseaseUtilitesSingleton.getInstance().saveOrUpdateHistory(true,medicalHistory);
        if(DiseaseUtilitesSingleton.getInstance().historyAdapter!=null)
            DiseaseUtilitesSingleton.getInstance().historyAdapter.notifyDataSetChanged();
        Snackbar.make(coordinatorLayout,"Disease added to medical history",Snackbar.LENGTH_LONG).show();
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
