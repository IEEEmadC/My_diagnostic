package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesDetails;
import org.json.JSONObject;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, PlacesDetails> {

    JSONObject googlePlacesJson;
  String googleMap="soy el mapa";
    PlacesDetails detailsJson= new PlacesDetails();
   MyPlacesJson myJson = new MyPlacesJson();
    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }
    Context mycontext;

    @Override
    protected PlacesDetails doInBackground(Object... inputObj) {

       String googlePlacesList = null;
        MyPlacesJson placeJsonParser = new MyPlacesJson();

        try {
            googleMap = (String) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);

           detailsJson=(myJson.GetPlaceDetails(googlePlacesJson));
            return detailsJson;
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return detailsJson;
    }

    @Override
    protected void onPostExecute(PlacesDetails details) {
//mostrar aqui el materialD dialog

        Toast.makeText(mycontext,details.getPhone_number() , Toast.LENGTH_SHORT).show();



    }
}

