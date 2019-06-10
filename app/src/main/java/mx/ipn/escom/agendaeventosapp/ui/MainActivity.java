package mx.ipn.escom.agendaeventosapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import mx.ipn.escom.agendaeventosapp.R;
import mx.ipn.escom.agendaeventosapp.adapters.EventoAdapter;
import mx.ipn.escom.agendaeventosapp.beans.Evento;
import mx.ipn.escom.agendaeventosapp.ui.fragments.CrearEventoFragment;
import mx.ipn.escom.agendaeventosapp.ui.fragments.EditarEventoFragment;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Evento>> {
    public Realm realm;
    private FloatingActionButton fabNuevoEvento;
    private RealmResults<Evento> listaEventos;
    private RecyclerView recyclerViewEventos;
    private EventoAdapter eventoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        findViewID();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Eventos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_buscarEvento) {
            Intent searchIntent = new Intent(this, BusquedaActivity.class);
            startActivity(searchIntent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void findViewID() {
        fabNuevoEvento = findViewById(R.id.fabNuevoEvento);
        listaEventos = realm.where(Evento.class).findAll();
        listaEventos.addChangeListener(this);
        recyclerViewEventos = findViewById(R.id.recyclerCategoria);
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        eventoAdapter = new EventoAdapter(listaEventos);
        recyclerViewEventos.setAdapter(eventoAdapter);
        listenOnClick();
    }

    private void listenOnClick() {
        fabNuevoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrearEventoFragment crearEventoFragment = new CrearEventoFragment();
                crearEventoFragment.show(getSupportFragmentManager(), "addEvent");
            }
        });
        eventoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Evento even = listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view));
                EditarEventoFragment editarEventoFragment = new EditarEventoFragment(even);
                editarEventoFragment.show(getSupportFragmentManager(), "editEvent");
            }
        });
        eventoAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                eliminarEvento(view);
                return false;
            }
        });
    }

    public void eliminarEvento(final View view) {
        final Evento eventoModelo = listaEventos.get(recyclerViewEventos.getChildAdapterPosition(view));
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Eliminar Evento");
        alertDialog.setMessage("¿Está seguro de eliminar este evento?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ACEPTAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        realm.beginTransaction();
                        if (eventoModelo != null) {
                            eventoModelo.deleteFromRealm();
                        } else
                            Toast.makeText(getApplicationContext(), "Problemas al eliminar de la BD", Toast.LENGTH_SHORT).show();
                        realm.commitTransaction();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCELAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getColor(R.color.transparente));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getColor(R.color.transparente));
    }

    @Override
    public void onChange(@NonNull RealmResults<Evento> eventoModelo) {
        eventoAdapter.notifyDataSetChanged();
    }
}
