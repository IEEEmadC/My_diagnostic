package org.dev4u.hv.my_diagnostic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.net.URI;

/**
 * Created by toni on 07/07/2017.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
Context mycontext;
    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.



        View v = inflater.inflate(R.layout.info_windows_layout, null);


        if(m.getTitle().compareTo("I am Here")==0){
            ((TextView)v.findViewById(R.id.info_window_estado)).setText("I am Here");
            ((ImageView)v.findViewById(R.id.info_window_imagen)).setImageResource(R.drawable.ic_user);
        }
        else {
            String direccion = String.valueOf(m.getSnippet());
            String separada[] = direccion.split("<");
            ((TextView) v.findViewById(R.id.info_window_nombre)).setText(m.getTitle());
            ((TextView) v.findViewById(R.id.info_window_placas)).setText(separada[0]);
            ((TextView)v.findViewById(R.id.info_window_estado)).setText(" ");
        }
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}