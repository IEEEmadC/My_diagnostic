package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, String> {

    JSONObject googlePlacesJson;
  String googleMap="soy el mapa";

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }

    Context mycontext;
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number="";
    @Override
    protected String doInBackground(Object... inputObj) {

       String googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (String) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.GetPlaceDetails(googlePlacesJson);
            setNumber(googlePlacesList);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(String list) {

        Toast.makeText(mycontext,list , Toast.LENGTH_SHORT).show();



    }
}

