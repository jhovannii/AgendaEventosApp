package mx.ipn.escom.agendaeventosapp.database;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import mx.ipn.escom.agendaeventosapp.beans.Evento;

public class DbEventos {
    private static ArrayList<Evento> listaTotal;
    private Realm realm;

    public DbEventos(final RealmList<Evento> eventos) {
        init();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Evento.class).findAll().deleteAllFromRealm();
                realm.insert(eventos);
            }
        });
        poblarListasSolicitudes();
    }

    public DbEventos() {
        init();
        poblarListasSolicitudes();
    }

    public static ArrayList<Evento> getSolicitudes() {
        return listaTotal;
    }

    public static ArrayList<Evento> getFilter(String busqueda) {
        busqueda = busqueda.toLowerCase();
        ArrayList<Evento> listaFiltrada = new ArrayList<>();

        for (int i = 0; i < listaTotal.size(); i++) {
            if (busqueda.length() > 0) {
                if ((listaTotal.get(i).getTipoEvento().toLowerCase().contains(busqueda)
                        || listaTotal.get(i).getStatusEvento().toLowerCase().contains(busqueda)
                        || listaTotal.get(i).getDescripcion().toLowerCase().contains(busqueda)
                        || listaTotal.get(i).getFecha().toLowerCase().contains(busqueda))) {
                    listaFiltrada.add(listaTotal.get(i));
                }
            }
        }
        return listaFiltrada;
    }

    private void init() {
        realm = Realm.getDefaultInstance();
        if (listaTotal == null) listaTotal = new ArrayList<>();
        else listaTotal.clear();
    }

    private void poblarListasSolicitudes() {
        RealmResults<Evento> lista = realm.where(Evento.class).findAll();
        listaTotal.addAll(realm.copyFromRealm(lista));
    }
}
