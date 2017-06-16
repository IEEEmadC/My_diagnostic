package db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import utils.ConnectionSettings;
import utils.VolleySingleton;

/**
 * Created by admin on 14/6/17.
 */

public class LocalDatabase {
    private Context context;
    private Gson gson;
    private Database db;
    private TextView textView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    Handler handler = new Handler();
    int mRequestCount = 3;
    CountDownLatch requestCountDown = new CountDownLatch(mRequestCount);
    private Thread checkQueueThread;
    private AlertDialog alertDialog;

    public LocalDatabase(Context context){
        this.context = context;
        gson = new Gson();
        db = new Database(this.context);
        preferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences = preferences.edit();
        initDialog();
    }



    private void checkQueue(){

        checkQueueThread = new Thread(new Runnable() {
            boolean interrupted=false;
            public void run() {
                try {
                    requestCountDown.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupted=true;
                }
                handler.post(new Runnable() {
                    public void run() {
                        if(!interrupted) {
                            Toast.makeText(context, "Symptom insertados " + db.getSymptoms().getCount(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Disease insertados " + db.getDiseases().getCount(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "SymptomDisease insertados " + db.getDiseasesSymptoms().getCount(), Toast.LENGTH_SHORT).show();
                            textView.setText("Finalizado");
                            if (alertDialog.isShowing()) alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        checkQueueThread.start();
    }
    public void initDialog(){
        AlertDialog.Builder alertDialogBuilider = new AlertDialog.Builder(context);
        alertDialogBuilider.setTitle("Error");
        alertDialogBuilider.setMessage("An error occurred while downloading the data,\nPlease check the internet connection.");
        alertDialogBuilider.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(checkQueueThread!=null){
                    checkQueueThread.interrupt();
                    checkQueueThread = null;
                }
                initDatabase();
            }
        });
        alertDialogBuilider.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        alertDialog = alertDialogBuilider.create();
    }

    public void initDatabase(){
        int status = preferences.getInt("STATUS",0);
        //if status equals zero is the first time
        if(status==0){
            textView.setText("Iniciando descarga");
            checkQueue();
            deleteData();
            getSymptoms();
            getDisease();
            getSymptomsDisease();
            //aqui seguiria actualizar a status 1 que significa que ya esta guardada la bd
        }else{
            textView.setText("Ya esta actualizado");
        }
    }

    private void hasError(){
        db.deleteData();
        alertDialog.show();
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
                                        hasError();
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
                    JSONArray mensaje = response.getJSONArray("symptoms");
                    Symptom[] symptom = gson.fromJson(mensaje.toString(), Symptom[].class); //solo tabla sintomas
                    for (Symptom s:symptom) {
                        db.saveSymptom(s.getid_symptom(),s.getSymptom());
                        Log.d("Insercion","id_simptom : "+s.getid_symptom()+" sintoma :"+s.getSymptom());
                    }
                    requestCountDown.countDown();
                    break;
                case "2": // FALLIDO
                    hasError();;
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
                                        saveDiseases(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        hasError();;
                                    }
                                }

                        )
                );
    }

    private void saveDiseases(JSONObject response) {
        try {
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    JSONArray mensaje = response.getJSONArray("diseases");
                    Disease[] diseases = gson.fromJson(mensaje.toString(), Disease[].class);
                    for (Disease s:diseases) {
                        String n = s.getName_disease().replaceAll("'","''");
                        String d = s.getDescription().replaceAll("'","''");
                        db.saveDisease(s.getId_disease(),n,d);
                        Log.d("Insercion","id_disease : "+s.getId_disease()+" nombre :"+s.getName_disease());
                    }
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
                    requestCountDown.countDown();
                    break;
                case "2": // FALLIDO
                    hasError();;
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getSymptomsDisease() {
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
                                        saveSymptomsDisease(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        hasError();;
                                    }
                                }

                        )
                );
    }

    private void saveSymptomsDisease(JSONObject response) {
        try {
            String estado = response.getString("estado");
            switch (estado) {
                case "1": // EXITO
                    JSONArray mensaje = response.getJSONArray("multitable");
                    SymptomDisease[] symptomdiseases = gson.fromJson(mensaje.toString(), SymptomDisease[].class);
                    for (SymptomDisease s:symptomdiseases) {
                        db.saveSymptomDisease(s.getId_sympdiseases(),s.getId_diseases(),s.getId_symptom());
                        Log.d("Insercion","id_disease : "+s.getId_diseases()+" id_symptom :"+s.getId_symptom());
                    }
                    Cursor c = db.getDiseasesSymptoms();
                    c.moveToFirst();
                    String contenido="";
                    if(c != null && c.getCount() > 0){
                        c.moveToFirst();
                        do{
                            Log.d("Cursor : "+c.getPosition(),"id_disease : "+c.getString(0)+" id_symptom : "+c.getString(1));
                        }while(c.moveToNext());
                    }else{
                        Log.d("Cursor: ","vacio");
                    }
                    requestCountDown.countDown();
                    break;
                case "2": // FALLIDO
                    hasError();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView lblEstado) {
        this.textView = lblEstado;
    }
}
