package mx.ipn.escom.agendaeventosapp.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import mx.ipn.escom.agendaeventosapp.R;
import mx.ipn.escom.agendaeventosapp.adapters.EventoAdapter;
import mx.ipn.escom.agendaeventosapp.beans.Evento;

public class CrearEventoFragment extends DialogFragment implements RealmChangeListener<RealmResults<Evento>> {
    public Realm realm;
    private String titulo;
    private TextView tvTitulo;
    private EditText edtFecha, edtHora, edtDescripcion;
    private Spinner spEstatus;
    private Button btnGuardar;
    private RadioGroup radioGroup;
    private int hour, minute1;
    private EventoAdapter eventoAdapter;

    public CrearEventoFragment() {
        this.titulo = "Crear Evento";
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.fragment_crear_evento, null);
        realm = Realm.getDefaultInstance();
        RealmResults<Evento> listaEventos = realm.where(Evento.class).findAll();
        listaEventos.addChangeListener(this);
        eventoAdapter = new EventoAdapter(listaEventos);
        iniciarElementosVisuales(vista);
        llenarCampos();
        builder.setView(vista);
        return builder.create();
    }

    private void iniciarElementosVisuales(View vista) {
        tvTitulo = vista.findViewById(R.id.tvTitulo);
        spEstatus = vista.findViewById(R.id.spStatus);
        edtFecha = vista.findViewById(R.id.edtFecha);
        edtHora = vista.findViewById(R.id.edtHora);
        edtDescripcion = vista.findViewById(R.id.edtDescripcion);
        btnGuardar = vista.findViewById(R.id.btnGuardar);
        radioGroup = vista.findViewById(R.id.rgTipoEvento);
        TextView tvEstatus = vista.findViewById(R.id.tvStatus);
        spEstatus.setVisibility(View.INVISIBLE);
        tvEstatus.setVisibility(View.INVISIBLE);
        spEstatus.setSelection(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitulo.setText(titulo);
        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(edtHora);
            }
        });
        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtFecha);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    final String fecha = edtFecha.getText().toString();
                    final String descripcion = edtDescripcion.getText().toString();
                    final String hora = edtHora.getText().toString();
                    String categoria = "otro";

                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    if (checkedId == R.id.rbCita) {
                        categoria = "cita";
                    } else if (checkedId == R.id.rbJunta) {
                        categoria = "junta";
                    } else if (checkedId == R.id.rbEntregaProyecto) {
                        categoria = "entrega";
                    } else if (checkedId == R.id.rbExamen) {
                        categoria = "examen";
                    } else if (checkedId == R.id.rbOtro) {
                        categoria = "otro";
                    }
                    guardarEvento(categoria, fecha, hora, descripcion, "pendiente");
                }
            }
        });
    }

    public void guardarEvento(String categoria, String fecha, String hora, String descripcion, String estatus) {
        realm.beginTransaction();
        Evento eventoModelo;
        eventoModelo = new Evento(categoria, descripcion, fecha, hora, estatus);
        realm.copyToRealm(eventoModelo);
        realm.commitTransaction();
        getDialog().dismiss();
    }

    private void showTimePicker(final EditText edtHora) {
        TimePickerDialog timepick = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edtHora.setText(twoDigits(hourOfDay).concat(" : ").concat(twoDigits(minute)));
                hour = hourOfDay;
                minute1 = minute;
            }
        }, hour, minute1, true);
        timepick.setTitle("Selecciona la hora");
        timepick.show();
    }

    private void showDatePicker(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    private boolean validarCampos() {
        if (edtDescripcion.getText() == null || edtDescripcion.getText().toString().equalsIgnoreCase("")) {
            edtDescripcion.setError("Campo Obligatorio");
            return false;
        } else if (edtFecha.getText() == null || edtFecha.getText().toString().equalsIgnoreCase("")) {
            edtFecha.setError("Campo Obligatorio");
            return false;
        } else if (edtHora.getText() == null || edtHora.getText().toString().equalsIgnoreCase("")) {
            edtHora.setError("Campo Obligatorio");
            return false;
        } else
            return true;
    }

    private void llenarCampos() {
        ArrayList<String> arrayListAdapter = new ArrayList<>();
        arrayListAdapter.add("Pendiente");
        arrayListAdapter.add("Realizado");
        arrayListAdapter.add("Aplazado");
        spEstatus.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListAdapter));
    }

    @Override
    public void onChange(RealmResults<Evento> eventos) {
        eventoAdapter.notifyDataSetChanged();
    }
}
