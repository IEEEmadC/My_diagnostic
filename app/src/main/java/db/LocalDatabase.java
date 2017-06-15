package db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import utils.ConnectionSettings;
import utils.VolleySingleton;

/**
 * Created by admin on 14/6/17.
 */

public class LocalDatabase {
    private Context context;
    private Gson gson;
    private Database db;
    public LocalDatabase(Context context){
        this.context = context;
        gson = new Gson();
        db = new Database(this.context);
    }

    public void deleteData(){
        db.deleteData();
    }

    public Database getDatabase(){
        return db;
    }

    public void getSymptoms() {
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETSymptom,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        saveSymptoms(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context,"Error Volley: "+error.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("INIT", "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }

    private void saveSymptoms(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    JSONArray mensaje = response.getJSONArray("symptoms");
                    // Parsear con Gson
                    //Symptoms_diseases[] symptoms = gson.fromJson(mensaje.toString(), Symptoms_diseases[].class); //multitabla
                    Symptom[] symptom = gson.fromJson(mensaje.toString(), Symptom[].class); //solo tabla sintomas
                    // Inicializar adaptador
                    for (Symptom s:symptom) {
                        db.saveSymptom(s.getid_symptom(),s.getSymptom());
                        Log.d("Insercion","id_simptom : "+s.getid_symptom()+" sintoma :"+s.getSymptom());
                    }
                    //simple confirmacion
                    Cursor c = db.getSymptoms();
                    c.moveToFirst();
                    String contenido="";
                    if(c != null && c.getCount() > 0){
                        c.moveToFirst();
                        do{
                            //do logic with cursor.
                            Log.d("Cursor : "+c.getPosition(),"id_symptom : "+c.getString(0)+" sintoma : "+c.getString(1));
                        }while(c.moveToNext());
                    }else{
                        Log.d("Cursor: ","vacio");
                    }
                    Toast.makeText(context,"Symptom insertados "+c.getCount(),Toast.LENGTH_SHORT).show();
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getDisease() {
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GetDisease,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        saveDiseases(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context,"Error Volley: "+error.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("INIT", "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }

    private void saveDiseases(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    JSONArray mensaje = response.getJSONArray("diseases");
                    // Parsear con Gson
                    Disease[] diseases = gson.fromJson(mensaje.toString(), Disease[].class);
                    // Inicializar adaptador
                    for (Disease s:diseases) {
                        String n = s.getName_disease().replaceAll("'","''");
                        String d = s.getDescription().replaceAll("'","''");
                        db.saveDisease(s.getId_disease(),n,d);
                        Log.d("Insercion","id_disease : "+s.getId_disease()+" nombre :"+s.getName_disease());
                    }
                    //simple confirmacion
                    Cursor c = db.getDiseases();
                    c.moveToFirst();
                    String contenido="";
                    if(c != null && c.getCount() > 0){
                        c.moveToFirst();
                        do{
                            //do logic with cursor.
                            Log.d("Cursor : "+c.getPosition(),"id_disease : "+c.getString(0)+" nombre : "+c.getString(1));
                        }while(c.moveToNext());
                    }else{
                        Log.d("Cursor: ","vacio");
                    }
                    Toast.makeText(context,"Diseases insertadas "+c.getCount(),Toast.LENGTH_SHORT).show();
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getSymptomsDisease() {
        // Petición GET
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GetDiseaseSymptom,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        saveSymptomsDisease(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context,"Error Volley: "+error.toString(),Toast.LENGTH_SHORT).show();
                                        Log.d("INIT", "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }

    private void saveSymptomsDisease(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    JSONArray mensaje = response.getJSONArray("multitable");
                    // Parsear con Gson
                    SymptomDisease[] symptomdiseases = gson.fromJson(mensaje.toString(), SymptomDisease[].class);
                    // Inicializar adaptador
                    for (SymptomDisease s:symptomdiseases) {
                        db.saveSymptomDisease(s.getId_sympdiseases(),s.getId_diseases(),s.getId_symptom());
                        Log.d("Insercion","id_disease : "+s.getId_diseases()+" id_symptom :"+s.getId_symptom());
                    }
                    //simple confirmacion
                    Cursor c = db.getDiseasesSymptoms();
                    c.moveToFirst();
                    String contenido="";
                    if(c != null && c.getCount() > 0){
                        c.moveToFirst();
                        do{
                            //do logic with cursor.
                            Log.d("Cursor : "+c.getPosition(),"id_disease : "+c.getString(0)+" id_symptom : "+c.getString(1));
                        }while(c.moveToNext());
                    }else{
                        Log.d("Cursor: ","vacio");
                    }
                    Toast.makeText(context,"SymptomDiseases insertados "+c.getCount(),Toast.LENGTH_SHORT).show();
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
