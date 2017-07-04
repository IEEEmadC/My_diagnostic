package org.dev4u.hv.my_diagnostic.MyPlacesUI;

import java.util.List;

public interface PlacesListener {
    void onPlacesFailure(PlacesException e);

    void onPlacesStart();

    void onPlacesSuccess(List<Place> places);

    void onPlacesFinished();
}