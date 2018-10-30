package br.usp.pcs2018.rastreamentopacotesapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.Activities.DetalhesPacoteActivity;
import br.usp.pcs2018.rastreamentopacotesapp.Adapters.ListaPacotesAdapter;
import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.HttpRequestTask;
import br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Pacote;
import br.usp.pcs2018.rastreamentopacotesapp.R;
import br.usp.pcs2018.rastreamentopacotesapp.Services.PacoteService;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class ListaPacotesFragment extends _BaseFragment {

    private List<Pacote> pacotes;

    private ListView listView;
    private View viewSemPacote;
    private ProgressBar progress;

    public static ListaPacotesFragment newInstance(String origem) {

        ListaPacotesFragment fr = new ListaPacotesFragment();
        fr.pacotes = new ArrayList<>();
        Bundle b = new Bundle();
        b.putString("Origem",origem);
        fr.setArguments(b);

        return fr;
    }

    public ListaPacotesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        Bundle b = getArguments();

        try {
            this.origem = b.getString("Origem");
        }
        catch (Exception e) {
            this.origem = "";
        }
    }

    @Override
    public void onAsyncFinished(Object obj, int callerCode, int type) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pacotes, container, false);

        viewSemPacote = view.findViewById(R.id.semPacotes);
        listView = view.findViewById(R.id.listView);
        progress = view.findViewById(R.id.progressBar);

        viewSemPacote.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        if(!origem.equals("historico")) {
            PacoteService.buscarPacotesAtivos(this.getContext());
        }
        else {
            PacoteService.buscarPacotesHistorico(this.getContext());
        }

        return view;
    }

    private static List<Pacote> deserializePacotes(String json) {

        List<Pacote> lista = new ArrayList<>();

        try {
            JSONArray arrayJson = new JSONArray(json);

            for(int i = 0; i < arrayJson.length(); i++) {
                Pacote p = (Pacote) Metodos.JsonToObject(arrayJson.getJSONObject(i).toString(),Pacote.class);
                lista.add(p);
            }

            return lista;
        }
        catch (JSONException e){
            return null;
        }
    }

    public void listarPacotes(HttpResponse resp) {

        if(resp.getResponseStatus()) {
            this.pacotes = deserializePacotes(resp.getResponseMessage());
        }

        progress.setVisibility(View.GONE);

        if(pacotes != null) {

            if (pacotes.size() > 0) {

                viewSemPacote.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                ListaPacotesAdapter adapter = new ListaPacotesAdapter(this.getContext(),R.layout.list_item_pacotes,this.pacotes);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Pacote p = (Pacote) parent.getItemAtPosition(position);

                        Intent intent = new Intent(ListaPacotesFragment.this.getActivity(),DetalhesPacoteActivity.class);
                        intent.putExtra("PacoteId",p.getPacoteId());
                        startActivity(intent);
                    }
                });
            }
            else {
                listView.setVisibility(View.GONE);
                viewSemPacote.setVisibility(View.VISIBLE);
            }
        }
        else {
            listView.setVisibility(View.GONE);
            viewSemPacote.setVisibility(View.VISIBLE);
        }
    }

}
