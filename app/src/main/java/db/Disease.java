package db;

import java.util.ArrayList;

/**
 * Created by admin on 4/6/17.
 */

public class Disease {

    String id;
    String name;
    String description;
    ArrayList<Symptom> symptoms;
    double matchPercentage;

    public Disease(String id, String name, String description, ArrayList<Symptom> symptoms) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.symptoms = symptoms;
        this.matchPercentage=0;
        this.symptoms = new ArrayList<>();
    }
    public Disease(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.matchPercentage=0;
        this.symptoms = new ArrayList<>();
    }
    public double evaluateSymptoms(ArrayList<String> input){
        double matches=0;
        for(int i=0;i<input.size();i++){
          //  for(int j=0;j<symptoms.size();j++) if(input.get(i).equals(symptoms.get(j).getName())) matches++;
        }
        matchPercentage = (100*matches)/(input.size()*1.0);
        return matchPercentage;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

}

