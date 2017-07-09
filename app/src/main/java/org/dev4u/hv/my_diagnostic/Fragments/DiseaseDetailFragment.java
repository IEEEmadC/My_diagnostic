package org.dev4u.hv.my_diagnostic.Fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;
import org.dev4u.hv.my_diagnostic.Thermometer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import db.Disease;
import db.MedicalHistory;
import db.Symptom;
import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;
import utils.SymptomAdapter;

public class DiseaseDetailFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private SymptomAdapter symptomAdapter;
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
        Thermometer t = new Thermometer(this.getContext());
        t.setPercent(80);
        ArrayList<String> symptomsWrite = null;
        if(getArguments()!=null) id_disease = getArguments().getString("ID_DISEASE");
        if(getArguments().getBoolean("SEARCH")){
            symptomsWrite = new ArrayList<>();
            for (Symptom s:DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms()) {
                symptomsWrite.add(s.getSymptom());
            }
        }
        lblDiseaseName          = (TextView) view.findViewById(R.id.lblDetailDiseaseName);
        lblDiseaseCategory      = (TextView) view.findViewById(R.id.lblDetailCategory);
        lblDiseaseDescription   = (TextView) view.findViewById(R.id.lblDetailDescription);
        lblAddToHistory         = (TextView) view.findViewById(R.id.lblAddToHistory);
        coordinatorLayout       = (CoordinatorLayout) view.findViewById(R.id.frm_detail_disease);
        recyclerView            = (RecyclerView)      view.findViewById(R.id.recycler_view_disease_detail);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        symptomAdapter = new SymptomAdapter(
                symptomsWrite,
                disease.getSymptoms(),
                getContext()
        );
        recyclerView.setAdapter(symptomAdapter);
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
}
