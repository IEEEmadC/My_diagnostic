package utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DataBaseVersion;
import db.Database;
import db.Disease;

/**
 * Created by admin on 12/7/17.
 */

public class SearchUpdates {
    private Context mContext;
    private Gson gson;

    public SearchUpdates(Context mContext) {
        this.mContext = mContext;
        gson = new Gson();
    }
    public void getVersion() {
        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETData,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        processVersion(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //error
                                        Toast.makeText(mContext,"1 An error has occurred while My Diagnostic trying to connect the server",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                );
    }
    private void processVersion(JSONObject response){
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("dataversion");
                    DataBaseVersion[] version = gson.fromJson(mensaje.toString(), DataBaseVersion[].class);
                    Toast.makeText(mContext,"Version "+version[0].getId_version()+" date "+version[0].getTo_date(),Toast.LENGTH_LONG).show();
                    return;
                default: // FAIL
                    //hasError();
                    Toast.makeText(mContext,"2 An error has occurred while My Diagnostic trying to connect the server",Toast.LENGTH_SHORT).show();
                    return;
            }
        } catch (JSONException e) {
            Toast.makeText(mContext,"3 An error has occurred while My Diagnostic trying to connect the server",Toast.LENGTH_SHORT).show();
        }
    }
}
