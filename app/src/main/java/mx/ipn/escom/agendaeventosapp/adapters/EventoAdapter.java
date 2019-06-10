package mx.ipn.escom.agendaeventosapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.ipn.escom.agendaeventosapp.R;
import mx.ipn.escom.agendaeventosapp.beans.Evento;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolderEvento>
        implements View.OnClickListener, View.OnLongClickListener {

    private List<Evento> listaEvento;
    private View.OnClickListener listener;
    private View.OnLongClickListener onLongClickListener;

    public EventoAdapter(List<Evento> listaEvento) {
        this.listaEvento = listaEvento;
    }

    @NonNull
    @Override
    public ViewHolderEvento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento_recycler, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolderEvento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvento holder, int position) {
        holder.tvFechaRv.setText(listaEvento.get(position).getFecha());
        holder.tvHoraRv.setText(listaEvento.get(position).getHora());
        holder.tvTituloRv.setText(listaEvento.get(position).getTipoEvento().toUpperCase());
        holder.tvDescripcionRv.setText(listaEvento.get(position).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaEvento.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (onLongClickListener != null) {
            onLongClickListener.onLongClick(view);
        }
        return false;
    }

    public void setLista(ArrayList<Evento> lista) {
        this.listaEvento = lista;
    }

    class ViewHolderEvento extends RecyclerView.ViewHolder {
        TextView tvTituloRv, tvFechaRv, tvDescripcionRv, tvHoraRv;

        ViewHolderEvento(View itemView) {
            super(itemView);
            tvTituloRv = itemView.findViewById(R.id.tvCategoriaRv);
            tvDescripcionRv = itemView.findViewById(R.id.tvDescripcionRv);
            tvFechaRv = itemView.findViewById(R.id.tvFechaRv);
            tvHoraRv = itemView.findViewById(R.id.tvHoraRv);
        }
    }
}
