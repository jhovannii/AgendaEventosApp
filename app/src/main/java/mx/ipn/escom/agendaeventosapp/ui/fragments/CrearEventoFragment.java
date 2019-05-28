package mx.ipn.escom.agendaeventosapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ipn.escom.agendaeventosapp.R;

public class CrearEventoFragment extends Fragment {

    public CrearEventoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_evento, container, false);
    }
}
