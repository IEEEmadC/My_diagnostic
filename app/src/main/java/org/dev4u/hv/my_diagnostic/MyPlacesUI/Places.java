package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Places {

    public String GetPlaceDetails(JSONObject jsonObject) {
        String name ="N/A";
        JSONArray jsonArray = null;
        String json=jsonObject.toString();
        try {
            JSONObject parentObject = new JSONObject(json);
            JSONObject userDetails = parentObject.getJSONObject("result");

            //And then read attributes like
            name= userDetails.getString("formatted_phone_number");

            Log.d("TELEFONO",name);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return name;
    }

 
    
}