package mx.ipn.escom.agendaeventosapp.ui.fragments;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
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

@SuppressLint("ValidFragment")
public class EditarEventoFragment extends DialogFragment implements RealmChangeListener<RealmResults<Evento>> {
    private RadioButton rbCitaEdit, rbEntregaEdit, rbJuntaEdit, rbExamenEdit, rbOtroEdit;
    private Realm realm;
    private Evento mEvento;
    private String titulo;
    private TextView tvTitulo;
    private EditText edtFecha, edtHora, edtDescripcion;
    private Spinner spEstatus;
    private Button btnActualizar, btnCancelar;
    private RadioGroup radioGroup;
    private int hour, minute1;
    private EventoAdapter eventoAdapter;

    public EditarEventoFragment(Evento evento) {
        this.mEvento = evento;
        this.titulo = "Modificar Evento";
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
        View vista = inflater.inflate(R.layout.fragment_editar_evento, null);
        iniciarElementosVisuales(vista);
        llenarSpinner();
        inicializarVariables();
        builder.setView(vista);
        return builder.create();
    }

    private void inicializarVariables() {
        realm = Realm.getDefaultInstance();
        RealmResults<Evento> listaEventos = realm.where(Evento.class).findAll();
        listaEventos.addChangeListener(this);
        eventoAdapter = new EventoAdapter(listaEventos);

        tvTitulo.setText(titulo);
        edtDescripcion.setText(mEvento.getDescripcion());
        edtFecha.setText(mEvento.getFecha());
        edtHora.setText(mEvento.getHora());

        if (mEvento.getStatusEvento().equalsIgnoreCase("pendiente")) {
            spEstatus.setSelection(0);
        } else if (mEvento.getStatusEvento().equalsIgnoreCase("realizado")) {
            spEstatus.setSelection(1);
        } else if (mEvento.getStatusEvento().equalsIgnoreCase("aplazado")) {
            spEstatus.setSelection(2);
        }

        if (mEvento.getTipoEvento().equalsIgnoreCase("cita")) {
            rbCitaEdit.setChecked(true);
        } else if (mEvento.getTipoEvento().equalsIgnoreCase("junta")) {
            rbJuntaEdit.setChecked(true);
        } else if (mEvento.getTipoEvento().equalsIgnoreCase("entrega")) {
            rbEntregaEdit.setChecked(true);
        } else if (mEvento.getTipoEvento().equalsIgnoreCase("examen")) {
            rbExamenEdit.setChecked(true);
        } else if (mEvento.getTipoEvento().equalsIgnoreCase("otro")) {
            rbOtroEdit.setChecked(true);
        }
        hour = Integer.parseInt(mEvento.getHora().substring(0, 2));
        minute1 = Integer.parseInt(mEvento.getHora().substring(5, 7));
    }

    private void iniciarElementosVisuales(View vista) {
        rbCitaEdit = vista.findViewById(R.id.rbCitaEdit);
        rbEntregaEdit = vista.findViewById(R.id.rbEntregaProyectoEdit);
        rbJuntaEdit = vista.findViewById(R.id.rbJuntaEdit);
        rbExamenEdit = vista.findViewById(R.id.rbExamenEdit);
        rbOtroEdit = vista.findViewById(R.id.rbOtroEdit);
        tvTitulo = vista.findViewById(R.id.tvTituloEdit);
        spEstatus = vista.findViewById(R.id.spStatusEdit);
        edtFecha = vista.findViewById(R.id.edtFechaEdit);
        edtHora = vista.findViewById(R.id.edtHoraEdit);
        edtDescripcion = vista.findViewById(R.id.edtDescripcionEdit);
        btnActualizar = vista.findViewById(R.id.btnActualizar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        radioGroup = vista.findViewById(R.id.rgTipoEventoEdit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    String fechaEdit = edtFecha.getText().toString();
                    String descripcionEdit = edtDescripcion.getText().toString();
                    String horaEdit = edtHora.getText().toString();
                    int positionSpinner = spEstatus.getSelectedItemPosition();
                    realm.beginTransaction();

                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    if (checkedId == R.id.rbCitaEdit) {
                        mEvento.setTipoEvento("cita");
                    } else if (checkedId == R.id.rbJuntaEdit) {
                        mEvento.setTipoEvento("junta");
                    } else if (checkedId == R.id.rbExamenEdit) {
                        mEvento.setTipoEvento("examen");
                    } else if (checkedId == R.id.rbEntregaProyectoEdit) {
                        mEvento.setTipoEvento("entrega");
                    } else if (checkedId == R.id.rbOtroEdit) {
                        mEvento.setTipoEvento("otro");
                    }

                    switch (positionSpinner) {
                        case 0:
                            mEvento.setStatusEvento("pendiente");
                            break;
                        case 1:
                            mEvento.setStatusEvento("realizado");
                            break;
                        case 2:
                            mEvento.setStatusEvento("aplazado");
                            break;
                    }
                    mEvento.setFecha(fechaEdit);
                    mEvento.setHora(horaEdit);
                    mEvento.setDescripcion(descripcionEdit);
                    realm.commitTransaction();
                    getDialog().dismiss();
                }
            }
        });
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

    private void llenarSpinner() {
        ArrayList<String> arrayListAdapter = new ArrayList<>();
        arrayListAdapter.add("Pendiente");
        arrayListAdapter.add("Realizado");
        arrayListAdapter.add("Aplazado");
        spEstatus.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListAdapter));
    }

    @Override
    public void onChange(@NonNull RealmResults<Evento> eventos) {
        eventoAdapter.notifyDataSetChanged();
    }
}