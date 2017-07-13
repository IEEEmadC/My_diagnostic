package db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import utils.ConnectionSettings;
import utils.SearchUpdates;
import utils.VolleySingleton;

/**
 * Created by admin on 14/6/17.
 */

public class LocalDatabase {
    private Context context;
    private Gson gson;
    private Database db;
    Handler handler = new Handler();
    int mRequestCount = 6;//six tables to fill the database
    CountDownLatch requestCountDown = new CountDownLatch(mRequestCount);
    private Thread checkQueueThread;
    private AlertDialog alertDialog;
    private Button button;
    private CoordinatorLayout coordinatorLayout;
    private boolean downloadFinished;
    private static Map stringJSONObjectMap;

    public LocalDatabase(Context context,Button button,CoordinatorLayout coordinatorLayout){
        this.context    = context;
        gson            = new Gson();
        db              = new Database(this.context);
        this.button     = button;
        this.coordinatorLayout = coordinatorLayout;
        initDialog();
    }

    private void checkQueue(){
        checkQueueThread = new Thread(new Runnable() {
            boolean interrupted=false;
            public void run() {
                try {
                    requestCountDown.await();
                    downloadFinished=false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupted=true;
                    downloadFinished=false;
                }
                handler.post(new Runnable() {
                    public void run() {
                        if(!interrupted) {
                            new saveInLocal().execute(stringJSONObjectMap);
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
        //TODO esta es la funcion que llama las peticiones
        stringJSONObjectMap = new HashMap();
        Snackbar.make(coordinatorLayout,"Download started",Snackbar.LENGTH_SHORT).show();
        button.setText("Downloading...");
        checkQueue();
        deleteData();

        getCountry();
        getBloodType();
        getDiseaseCategory();
        getSymptoms();
        getDisease();
        getSymptomsDisease();
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


    public void getCountry() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETCountry,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("COUNTRY",response);
                                        requestCountDown.countDown();
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

    private boolean saveCountry(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    Country[] countries = gson.fromJson(mensaje.toString(), Country[].class);
                    for (Country s:countries) {
                        String n = s.getName_country().replaceAll("'","''");
                        db.saveCountry(s.getId_country(),n,s.getShort_name());
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    //bloodtype
    public void getBloodType() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETBlood_type,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("BLOODTYPE",response);
                                        requestCountDown.countDown();
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

    private boolean saveBloodType(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    Bloodtype[] bloodtypes = gson.fromJson(mensaje.toString(), Bloodtype[].class);
                    for (Bloodtype s:bloodtypes) {
                        db.saveBloodType(s.getId_bloodtype(),s.getBloodtype());
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getDiseaseCategory() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETDiseases_category,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("CATEGORY",response);
                                        requestCountDown.countDown();
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

    private boolean saveDiseasesCategory(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("diseases_category");
                    Diseases_category[] diseases_categories = gson.fromJson(mensaje.toString(), Diseases_category[].class);
                    for (Diseases_category s:diseases_categories) {
                        String n = s.getCategory_name().replaceAll("'","''");
                        String d = s.getCategory_description().replaceAll("'","''");
                        db.saveDiseaseCategory(s.getId_disease_category(),n,d);
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getSymptoms() {
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
                                        stringJSONObjectMap.put("SYMPTOMS",response);
                                        requestCountDown.countDown();
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

    private boolean saveSymptoms(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("symptoms");
                    Symptom[] symptom = gson.fromJson(mensaje.toString(), Symptom[].class);
                    for (Symptom s:symptom) {
                        db.saveSymptom(s.getid_symptom(),s.getSymptom());
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getDisease() {
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
                                        stringJSONObjectMap.put("DISEASES",response);
                                        requestCountDown.countDown();
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

    private boolean saveDiseases(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("diseases");
                    Disease[] diseases = gson.fromJson(mensaje.toString(), Disease[].class);
                    for (Disease s:diseases) {
                        String n = s.getName_disease().replaceAll("'","''");
                        String d = s.getDescription().replaceAll("'","''");
                        db.saveDisease(s.getId_disease(),n,d,s.getId_disease_category());
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
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
                                        stringJSONObjectMap.put("SYMPTOM_DISEASE",response);
                                        requestCountDown.countDown();
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

    private boolean saveSymptomsDisease(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    SymptomDisease[] symptomdiseases = gson.fromJson(mensaje.toString(), SymptomDisease[].class);
                    for (SymptomDisease s:symptomdiseases) {
                        db.saveSymptomDisease(s.getId_sympdiseases(),s.getId_diseases(),s.getId_symptom());
                    }
                    return true;
                default: // FAIL
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public class saveInLocal extends AsyncTask<Map, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Map... params) {
            boolean state = fillDB(params[0]);
            if(isCancelled()) return false;
            return state;
        }
        private boolean fillDB(Map map){
            try{
                boolean state=true;
                if(map.containsKey("COUNTRY"))      state = saveCountry((JSONObject) map.get("COUNTRY"));
                if(map.containsKey("BLOODTYPE"))    state = saveBloodType((JSONObject) map.get("BLOODTYPE"));
                if(map.containsKey("CATEGORY"))     state = saveDiseasesCategory((JSONObject) map.get("CATEGORY"));
                if(map.containsKey("SYMPTOMS"))     state = saveSymptoms((JSONObject) map.get("SYMPTOMS"));
                if(map.containsKey("DISEASES"))     state = saveDiseases((JSONObject) map.get("DISEASES"));
                if(map.containsKey("SYMPTOM_DISEASE"))  state = saveSymptomsDisease((JSONObject) map.get("SYMPTOM_DISEASE"));
                SearchUpdates s = new SearchUpdates(context,true);
                s.getVersion(true);
                return state;
            }catch (Exception e){
                hasError();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean==true){
                downloadFinished=true;
                button.setText("Continue");
                Snackbar.make(coordinatorLayout,"Download finished",Snackbar.LENGTH_SHORT).show();
                if (alertDialog.isShowing()) alertDialog.dismiss();
            }else{
                hasError();
            }
        }
    }



    public boolean isDownloadFinished() {
        return downloadFinished;
    }
}
