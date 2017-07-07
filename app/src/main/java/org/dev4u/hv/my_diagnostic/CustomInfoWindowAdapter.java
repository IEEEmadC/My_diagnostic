package org.dev4u.hv.my_diagnostic;

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
    private ImageView imagen;
    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.



        View v = inflater.inflate(R.layout.info_windows_layout, null);

        int estado=m.getTitle().compareTo("I am Here");
        if(estado==0){
            ((TextView)v.findViewById(R.id.info_window_estado)).setText("I am Here");
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