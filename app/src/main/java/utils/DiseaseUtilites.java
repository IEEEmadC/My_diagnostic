package utils;

import java.util.ArrayList;

import db.Disease;

/**
 * Created by admin on 4/6/17.
 */

public class DiseaseUtilites {
    ArrayList<Disease> diseasesList;
    double minimunPercentage;

    Disease searchDisease(String name){
        for (Disease disease : diseasesList) {
            if (disease.getName_disease().equals(name)) {
                return disease;
            }
        }
        return null;
    }
    void fillDiseases(){
        //TODO get the query and save it
    }
    ArrayList<Disease> getDiseasesMatches(ArrayList<String> inputs){
        ArrayList<Disease> diseasesfound = new ArrayList<>();
        for(int i=0;i<diseasesList.size();i++)
            if(diseasesList.get(i).evaluateSymptoms(inputs)>=minimunPercentage) diseasesfound.add(diseasesList.get(i));
        return diseasesfound;
    }

}
