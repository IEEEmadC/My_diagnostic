package org.dev4u.hv.my_diagnostic.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.dev4u.hv.my_diagnostic.R;
import org.dev4u.hv.my_diagnostic.Thermometer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import db.Disease;
import db.MedicalHistory;
import db.Symptom;
import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;
import utils.SymptomAdapter;

public class DiseaseDetailFragment extends BaseFragment implements OnChartValueSelectedListener {
    private View view;

    private ImageView imgIconDisease;
    private TextView lblDiseaseName;
    private TextView lblDiseaseCategory;
    private TextView lblDiseaseDescription;
    private TextView lblDiseasePercentage;
    private TextView lblAddToHistory;
    private TextView lblSymptomsList;
    private TextView lblDetailSymptomsCount;
    private TextView lblStats;
    private String id_disease;
    private CoordinatorLayout coordinatorLayout;
    private CardView cardViewStats;
    private Thermometer thermometer;

    public DiseaseDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view = inflater.inflate(R.layout.fragment_disease_detail, container, false);
        Thermometer t = new Thermometer(this.getContext());
        t.setPercent(80);
        ArrayList<String> symptomsWrite = null;
        if(getArguments()!=null) id_disease = getArguments().getString("ID_DISEASE");
        cardViewStats           = (CardView) view.findViewById(R.id.cardView_stats);
        imgIconDisease          = (ImageView)view.findViewById(R.id.img_disease);
        lblSymptomsList         = (TextView) view.findViewById(R.id.lblSymptomsList);
        lblDiseaseName          = (TextView) view.findViewById(R.id.lblDetailDiseaseName);
        lblDiseaseCategory      = (TextView) view.findViewById(R.id.lblDetailCategory);
        lblDiseaseDescription   = (TextView) view.findViewById(R.id.lblDetailDescription);
        lblAddToHistory         = (TextView) view.findViewById(R.id.lblAddToHistory);
        lblDetailSymptomsCount  = (TextView) view.findViewById(R.id.lblDetailSymptomsCount);
        lblDiseasePercentage    = (TextView) view.findViewById(R.id.lblDetailPercentage);
        lblStats                = (TextView) view.findViewById(R.id.lblStats);
        thermometer             = (Thermometer) view.findViewById(R.id.thermometerDisease);
        coordinatorLayout       = (CoordinatorLayout) view.findViewById(R.id.frm_detail_disease);


        Disease disease = DiseaseUtilitesSingleton.getInstance().getDisease(id_disease);



        if(disease!=null){

            int id_category = Integer.parseInt(disease.getId_disease_category())-1;

            if (id_category > 0 && id_category < DiseaseAdapter.getIcons().size()) {
                imgIconDisease.setImageResource(DiseaseAdapter.getIcons().get(id_category));
            }
            lblDiseaseName.setText(disease.getName_disease());
            lblDiseaseCategory.setText(disease.getCategory_name());
            lblDiseaseDescription.setText(disease.getDescription());
            lblDetailSymptomsCount.setText("Symptoms "+disease.getSymptoms().size());
            lblAddToHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToHistory();
                }
            });
            String list="";
            for(Symptom s:disease.getSymptoms() ){
                list +="\n\u2022 "+s.getSymptom();
            }

            String content = "My diagnostic suggested this condition on the basis of the following ";
            if(getArguments().getBoolean("SEARCH",false)) {
                content+="symptoms (match "+disease.getSymptoms_match()+") :\n";
            }else{
                content+="symptoms :\n";
            }
            content+=list;
            Spannable sb = new SpannableString(content);
            if(getArguments().getBoolean("SEARCH",false)){
                cardViewStats.setVisibility(View.VISIBLE);
                int start  = content.indexOf("(")+1;
                int end    = content.indexOf(")");
                sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for(Symptom s:DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms()){
                    start   = content.indexOf(s.getSymptom());
                    end     = start+s.getSymptom().length();
                    if(start>1) sb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                DecimalFormat decimal = new DecimalFormat("###.##");
                String stats="Local match percentage : "+decimal.format(disease.getMatchPercentage())+"%\n";
                stats+="Global match percentage : "+decimal.format(disease.getGlobalMatchPercentage())+"%";
                lblDiseasePercentage.setText(decimal.format(disease.getMatchPercentage())+"%");
                lblStats.setText(stats);
                thermometer.setPercent((float)disease.getMatchPercentage());
                //Chart Symptoms Found
                PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);
                ChartSymptomFound((float)disease.getGlobalMatchPercentage() ,pieChart);
                PieChart pieChart2 = (PieChart) view.findViewById(R.id.piechart2);
                ChartSymptomMatch((float)disease.getMatchPercentage() ,pieChart2);
                pieChart.setUsePercentValues(true);

            }else{
                lblDiseasePercentage.setVisibility(View.INVISIBLE);
                cardViewStats.setVisibility(View.GONE);
                thermometer.setVisibility(View.INVISIBLE);
                //((ViewGroup)view.getParent()).removeView(cardViewStats);
            }
            lblSymptomsList.setText(sb);
        }
        return view;
    }
public void ChartSymptomFound(float p1,PieChart pieChart){

    ArrayList<Entry> yvalues = new ArrayList<Entry>();
     float value=(100f-p1);
    yvalues.add(new Entry( value, 0));
    yvalues.add(new Entry(p1,1));
    PieDataSet dataSet = new PieDataSet(yvalues, " ");
    ArrayList<String> xVals = new ArrayList<String>();
    xVals.add("Symptoms Found");
    xVals.add("Symptoms Not Found");
    PieData data = new PieData(xVals, dataSet);
    data.setValueFormatter(new PercentFormatter());
    pieChart.setData(data);
    pieChart.setDescriptionPosition(335,14);
    pieChart.setDescriptionTextSize(11);

    pieChart.setDrawHoleEnabled(true);
    pieChart.setTransparentCircleRadius(35f);
    pieChart.setHoleRadius(15f);
    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
    data.setValueTextSize(9f);
    data.setValueTextColor(Color.DKGRAY);
    pieChart.setOnChartValueSelectedListener(this);
    pieChart.animateXY(1400, 1400);

}
    public void ChartSymptomMatch(float p1,PieChart pieChart){

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        float value=(100f-p1);
        yvalues.add(new Entry( value, 0));
        yvalues.add(new Entry(p1,1));
        PieDataSet dataSet = new PieDataSet(yvalues, " ");
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Symptoms Match ");
        xVals.add("Symptoms Not Match");
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDescriptionPosition(335,14);
        pieChart.setDescriptionTextSize(11);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleRadius(15f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.animateXY(1400, 1400);

    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
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
