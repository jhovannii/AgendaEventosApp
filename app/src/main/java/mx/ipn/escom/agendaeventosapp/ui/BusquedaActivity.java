package mx.ipn.escom.agendaeventosapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.ipn.escom.agendaeventosapp.R;
import mx.ipn.escom.agendaeventosapp.adapters.EventoAdapter;
import mx.ipn.escom.agendaeventosapp.beans.Evento;
import mx.ipn.escom.agendaeventosapp.database.DbEventos;

public class BusquedaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private SearchView searchView;
    private EventoAdapter statusAdapter;
    private TextView rlBusquedaVacia;
    private RecyclerView mRecyclerView;
    private MenuItem search;
    private Realm realm;
    private DbEventos dbEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        Toolbar toolbar = findViewById(R.id.toolbarBusqueda);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.rvListaBusqueda);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        rlBusquedaVacia = findViewById(R.id.tvBusquedaVacia);
        rlBusquedaVacia.setVisibility(View.GONE);

        ImageView ivReturn = findViewById(R.id.ivReturn);
        ivReturn.setOnClickListener(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Evento> listaEventos = realm.where(Evento.class).findAll();
        statusAdapter = new EventoAdapter(listaEventos);
        mRecyclerView.setAdapter(statusAdapter);
        dbEventos = new DbEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        search = menu.findItem(R.id.action_busqueda);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        search.expandActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Tipo de Evento, Descripción...");
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.blanco));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.blanco));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_busqueda) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            filtro(searchView.getQuery().toString());
        } else {
            filtro("");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("Query Text Change", newText.toLowerCase());
        filtro(newText);
        return false;
    }

    private void filtro(String busqueda) {
        ArrayList<Evento> solicitudes = DbEventos.getFilter(busqueda);
        if (solicitudes.size() > 0) {
            rlBusquedaVacia.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            statusAdapter.setLista(solicitudes);
            statusAdapter.notifyDataSetChanged();
        } else {
            rlBusquedaVacia.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent next = new Intent(this, MainActivity.class);
        startActivity(next);
        finish();
    }
}
