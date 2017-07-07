package db;

import java.util.ArrayList;

/**
 * Created by admin on 4/6/17.
 */

public class Disease implements Comparable<Disease>{

    String id_disease;
    String name_disease;
    String description;
    ArrayList<Symptom> symptoms;
    double matchPercentage;
    int symptoms_match;
    String id_disease_category;
    String category_name;
    public Disease(String id, String name, String description,String id_disease_category, ArrayList<Symptom> symptoms) {
        this.id_disease = id;
        this.name_disease = name;
        this.description = description;
        this.symptoms = symptoms;
        this.matchPercentage=0;
        this.symptoms = symptoms;
        this.symptoms_match=0;
        this.id_disease_category=id_disease_category;
    }
    public Disease(String id, String name, String description,String id_disease_category) {
        this.id_disease = id;
        this.name_disease = name;
        this.description = description;
        this.matchPercentage=0;
        this.symptoms = new ArrayList<>();
        this.symptoms_match=0;
        this.id_disease_category=id_disease_category;
    }
    public double evaluateSymptoms(ArrayList<String> input){
        double matches=0;
        for(int i=0;i<input.size();i++){
            for(int j=0;j<symptoms.size();j++) if(input.get(i).equals(symptoms.get(j).getSymptom())) matches++;
        }
        this.symptoms_match=(int)matches;
        matchPercentage = (100*matches)/(symptoms.size()*1.0);
        return matchPercentage;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }


    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }
    public String getId_disease() {
        return id_disease;
    }

    public void setId_disease(String id_disease) {
        this.id_disease = id_disease;
    }

    public String getName_disease() {
        return name_disease;
    }

    public void setName_disease(String name_disease) {
        this.name_disease = name_disease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSymptoms_match() {
        return symptoms_match;
    }

    public String getId_disease_category() {
        return id_disease_category;
    }

    public void setId_disease_category(String id_disease_category) {
        this.id_disease_category = id_disease_category;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public int compareTo(Disease o) {
        return this.name_disease.compareTo(o.getName_disease());
    }
}

