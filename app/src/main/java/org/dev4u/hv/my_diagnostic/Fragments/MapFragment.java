package org.dev4u.hv.my_diagnostic.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dev4u.hv.my_diagnostic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment {

    View view;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_maps, container, false);

        //TODO implementar mapa aqui

        return view;
    }

}
