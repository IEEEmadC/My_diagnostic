package utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import db.Database;
import db.Disease;
import db.Symptom;

/**
 * Created by admin on 4/6/17.
 */

public class DiseaseUtilitesSingleton {

    private ArrayList<Symptom> temporarySymptoms;
    private ArrayList<Disease> diseasesList;
    private ArrayList<Symptom> allSymptomsList;
    private ArrayList<String> diseasesNames;
    private ArrayList<String> symptomsNames;
    private double minimunPercentage;
    private Database db;
    private Context context;
    private Thread fillThread;
    private Handler fillHandler = new Handler();
    private static DiseaseUtilitesSingleton instance = null;

    protected DiseaseUtilitesSingleton(){
        temporarySymptoms=new ArrayList<>();
    }

    public static DiseaseUtilitesSingleton getInstance(){
        if(instance==null){
            instance = new DiseaseUtilitesSingleton();
        }
        return instance;
    }

    public void init(Context c){
        db = new Database(c);
        context = c;
    }

    public Disease searchDisease(String name){
        for (Disease disease : diseasesList) {
            if (disease.getName_disease().equals(name)) {
                return disease;
            }
        }
        return null;
    }

    public void fillDataThread(){
        fillThread = new Thread(new Runnable() {
            public void run() {
                fillData();
                fillHandler.post(new Runnable() {
                    public void run() {
                    Toast.makeText(context, "Diseases Insertado en lista " + diseasesList.size(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        fillThread.start();
    }

    public void fillData(){
        Cursor diseaseCursor = db.getDiseases();
        Cursor allSymptomsCursor = db.getSymptoms();
        diseaseCursor.moveToFirst();
        allSymptomsCursor.moveToFirst();
        diseasesList = new ArrayList<>();
        allSymptomsList = new ArrayList<>();
        symptomsNames = new ArrayList<>();
        diseasesNames = new ArrayList<>();
        //llenando todas las enfermedades y sus sintomas
        if(diseaseCursor != null && diseaseCursor.getCount() > 0){
            diseaseCursor.moveToFirst();
            do{
                //filling up the diseases and the symptoms
                ArrayList<Symptom> symptomsList = new ArrayList<>();
                Cursor symptomCursor = db.getDiseaseSymptoms(diseaseCursor.getString(0));
                symptomCursor.moveToFirst();
                if(symptomCursor != null && symptomCursor.getCount() > 0){
                    do{
                        symptomsList.add(new Symptom(symptomCursor.getString(0),symptomCursor.getString(1)));
                        Log.d("Enfemedad : "+diseaseCursor.getString(1),"Sintoma "+symptomCursor.getString(1));
                    }while (symptomCursor.moveToNext());
                    diseasesNames.add(diseaseCursor.getString(0));
                    Collections.sort(symptomsList);
                    diseasesList.add(new Disease(diseaseCursor.getString(0),diseaseCursor.getString(1),diseaseCursor.getString(2),symptomsList));
                }
            }while(diseaseCursor.moveToNext());
            Collections.sort(diseasesList);
            Collections.sort(diseasesNames);
        }
        //llenando todos los sintomas
        if(allSymptomsCursor != null && allSymptomsCursor.getCount() > 0){
            do{
                allSymptomsList.add(new Symptom(allSymptomsCursor.getString(0),allSymptomsCursor.getString(1)));
                symptomsNames.add(allSymptomsCursor.getString(1));
            }while (allSymptomsCursor.moveToNext());
            Collections.sort(allSymptomsList);
            Collections.sort(symptomsNames);
        }
    }
    public ArrayList<Disease> getDiseasesMatches(ArrayList<String> inputs){
        ArrayList<Disease> diseasesfound = new ArrayList<>();
        for(int i=0;i<diseasesList.size();i++)
            if(diseasesList.get(i).evaluateSymptoms(inputs)>=minimunPercentage) diseasesfound.add(diseasesList.get(i));
        return diseasesfound;
    }
    public ArrayList<Symptom> getAllSymptomsList(){return this.allSymptomsList;}

    public ArrayList<String> getDiseasesNames() {
        return diseasesNames;
    }

    public ArrayList<String> getSymptomsNames() {
        return symptomsNames;
    }

    public ArrayList<Symptom> getTemporarySymptoms() {
        return temporarySymptoms;
    }

    public void setTemporarySymptoms(ArrayList<Symptom> temporarySymptoms) {
        this.temporarySymptoms = temporarySymptoms;
    }

    public ArrayList<Disease> getDiseasesList() {
        return diseasesList;
    }

    public void setDiseasesList(ArrayList<Disease> diseasesList) {
        this.diseasesList = diseasesList;
    }


}
