package br.usp.pcs2018.rastreamentopacotesapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.Activities.DetalhesPacoteActivity;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Pacote;
import br.usp.pcs2018.rastreamentopacotesapp.R;

public class ListaPacotesAdapter extends ArrayAdapter<Pacote> {

    private List<Pacote> pacotes;

    public ListaPacotesAdapter(Context context, int resource, List<Pacote> ps) {
        super(context,resource,ps);

        this.pacotes = ps;
    }

    @Override
    public int getCount() {
        return pacotes.size();
    }

    @Override
    public Pacote getItem(int position) {
        try {
            return pacotes.get(position);
        }
        catch (Exception e) {
            return  null;
        }
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if(itemView == null){
            LayoutInflater lf = LayoutInflater.from(getContext());
            itemView = lf.inflate(R.layout.list_item_pacotes, parent, false);
        }

        final Pacote item = getItem(position);

        TextView remetente = itemView.findViewById(R.id.remetente);
        TextView destinatario = itemView.findViewById(R.id.destinatario);
        TextView codigo = itemView.findViewById(R.id.codigo);

        remetente.setText(String.format(getContext().getString(R.string.listaRemetente),item.getRemetente()));
        destinatario.setText(String.format(getContext().getString(R.string.listaDestinatario),item.getDestinatario()));
        codigo.setText(item.getPacoteId().substring(0,6));

        return itemView;
    }
}
