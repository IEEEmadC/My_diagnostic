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
                                ConnectionSettings.GET,
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
                        Log.d("Insercion","id : "+s.getid_symptom()+" sintoma :"+s.getSymptom());
                    }
                    //simple confirmacion
                    Cursor c = db.getSymptoms();
                    c.moveToFirst();
                    String contenido="";
                    if(c != null && c.getCount() > 0){
                        c.moveToFirst();
                        do{
                            //do logic with cursor.
                            Log.d("Cursor : "+c.getPosition(),"id : "+c.getString(0)+" sintoma : "+c.getString(1));
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
}
