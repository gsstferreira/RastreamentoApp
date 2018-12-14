package br.usp.pcs2018.rastreamentopacotesapp.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.TimerTask;
import br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Endereco;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Item;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Localizacao;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Pacote;
import br.usp.pcs2018.rastreamentopacotesapp.Models.Rota;
import br.usp.pcs2018.rastreamentopacotesapp.R;
import br.usp.pcs2018.rastreamentopacotesapp.Services.PacoteService;

import static br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.HttpRequestTask.PACOTE_DETALHES;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class DetalhesPacoteActivity extends _BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private Pacote pacote;
    private boolean pacoteReady;
    private boolean mapReady;

    private ImageView imgStatus;
    private TextView textStatus;

    private LinearLayout formEmAgencia;
    private TextView textRuaNum;
    private TextView textBairroCep;
    private TextView textCidadeEstado;

    private LinearLayout listaItens;

    private TextView remetente;

    private TextView destinatario;
    private TextView destinatarioRuaNum;
    private TextView destinatarioBairroCep;
    private TextView destinatarioCidadeEstado;

    private TextView textDataPostagem;

    private TextView labelDataChegada;
    private TextView textDataChegada;

    private SimpleDateFormat dateFormatter;

    private TextView textProgress;
    private ProgressBar barProgress;

    private String pacoteId;
    private int waitTimer;


    private List<TimerTask> timers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pacoteId = getIntent().getStringExtra("PacoteId");
        pacote = new Pacote();
        pacoteReady = false;
        mapReady = false;

        originId = System.currentTimeMillis();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm",Locale.getDefault());

        imgStatus = findViewById(R.id.imgStatus);
        textStatus = findViewById(R.id.textStatus);

        formEmAgencia = findViewById(R.id.formEmAgencia);
        textRuaNum = findViewById(R.id.textRuaNum);
        textBairroCep = findViewById(R.id.textBairroCep);
        textCidadeEstado = findViewById(R.id.textCidadeEstado);

        listaItens = findViewById(R.id.listaItens);

        remetente = findViewById(R.id.remetente);

        destinatario = findViewById(R.id.destinatario);
        destinatarioRuaNum = findViewById(R.id.destinoRuaNum);
        destinatarioBairroCep = findViewById(R.id.destinoBairroCep);
        destinatarioCidadeEstado = findViewById(R.id.destinoCidadeEstado);

        textDataPostagem = findViewById(R.id.textDataPostagem);

        labelDataChegada = findViewById(R.id.labelDataChegada);
        textDataChegada = findViewById(R.id.textDataChegada);

        textProgress = findViewById(R.id.textProgress);
        barProgress = findViewById(R.id.barProgress);
        textProgress.setText(R.string.detalhesLoading);
        barProgress.setProgress(0);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando Pacote...");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapFragment.getMapAsync(this);
        PacoteService.buscarDetalhesPacote(this, pacoteId, originId);
        progressDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        for (TimerTask t:timers) {
            t.cancel(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap.getUiSettings() != null) {
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }

        mapReady = true;
    }

    @Override
    public void onAsyncFinished(Object obj, int callerCode, int type, long origin, long selfId) {

        switch (type){
            case ASYNC_HTTP_CODE:

                if(origin == originId) {
                    progressDialog.dismiss();

                    switch(callerCode){
                        case PACOTE_DETALHES:

                            HttpResponse resp = (HttpResponse) obj;

                            if(resp.getResponseStatus()) {

                                this.pacote = (Pacote) Metodos.JsonToObject(resp.getResponseMessage(),Pacote.class);
                                sortRotasLocalizacoes(this.pacote);
                                pacoteReady = true;
                                TimerTask t = new TimerTask(DetalhesPacoteActivity.this,10,100, this.originId);
                                timers.add(t);
                                t.execute();
                            }

                            break;
                        default:
                            break;
                    }
                }
                break;
            case ASYNC_TIMER_CODE:
                if(origin == originId) {

                    for (TimerTask t:timers) {
                        if(t.selfId == selfId) {
                            timers.remove(t);
                            break;
                        }
                    }

                    switch (callerCode) {
                        case 10:
                            if(!pacoteReady || !mapReady) {
                                new TimerTask(DetalhesPacoteActivity.this,10,100, originId).execute();
                            }
                            else {
                                popularMapa();
                                popularDados();

                                TimerTask t = new TimerTask(DetalhesPacoteActivity.this,11,waitTimer, this.originId);
                                timers.add(t);
                                t.execute();
                            }
                            break;
                        case 11:
                            textProgress.setText(R.string.detalhesLoading);
                            PacoteService.buscarDetalhesPacote(this, pacoteId, this.originId);
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAsyncUpdate(int progresso, int type, int callerCode, long origin) {

        if(origin == this.originId){
            switch (type) {
                case ASYNC_TIMER_CODE:

                    switch (callerCode) {
                        case 11:
                            updateProgress(progresso);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void sortRotasLocalizacoes(Pacote pacote){

        for(Rota r:pacote.getRotas()) {
            Collections.sort(r.getAmostrasLocalizacao(), new Comparator<Localizacao>() {
                @Override
                public int compare(Localizacao o1, Localizacao o2) {
                    return o1.getHorarioAmostra().compareTo(o2.getHorarioAmostra());
                }
            });
        }

        Collections.sort(pacote.getRotas(), new Comparator<Rota>() {
            @Override
            public int compare(Rota o1, Rota o2) {
                return o1.getDataInicio().compareTo(o2.getDataInicio());
            }
        });
    }


    private void popularMapa() {

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.marker_maps_18);
        BitmapDescriptor descr = BitmapDescriptorFactory.fromBitmap(bitmap);

        mMap.clear();

        for(Rota r:this.pacote.getRotas()) {

            PolylineOptions opt = new PolylineOptions()
                    .clickable(false)
                    .color(getResources().getColor(R.color.colorPrimaryDark))
                    .visible(true);

            MarkerOptions markOrigem = new MarkerOptions()
                    .position(r.getOrigem().getLatLng())
                    .visible(true)
                    .icon(descr)
                    .anchor(0.5f,0.5f);

            mMap.addMarker(markOrigem);

            MarkerOptions markDestino = new MarkerOptions()
                    .position(r.getDestino().getLatLng())
                    .visible(true)
                    .icon(descr)
                    .anchor(0.5f,0.5f);

            mMap.addMarker(markDestino);

            bounds.include(r.getOrigem().getLatLng());
            bounds.include(r.getDestino().getLatLng());

            for(Localizacao l:r.getAmostrasLocalizacao()) {

                LatLng ll = new LatLng(l.getLatitude(),l.getLongitude());

                bounds.include(new LatLng(l.getLatitude(),l.getLongitude()));

                if((pacote.getRotas().indexOf(r) == pacote.getRotas().size() - 1) && (r.getAmostrasLocalizacao().indexOf(l) == r.getAmostrasLocalizacao().size() - 1)) {
                    MarkerOptions mark = new MarkerOptions()
                            .position(ll)
                            .snippet(l.getHorarioAmostra().toString())
                            .visible(true);

                    mMap.addMarker(mark);
                }
                opt = opt.add(ll);
            }

            mMap.addPolyline(opt);

        }

        if(pacote.getRotas().isEmpty()) {

            MarkerOptions mark = new MarkerOptions()
                    .position(pacote.getDestino().getLatLng())
                    .title("Destino")
                    .visible(true);

            mMap.addMarker(mark);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pacote.getDestino().getLatLng(),16));
        }

        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),50));
        }

    }

    private void popularDados() {

        if(pacote.getRotas().size() > 0) {
            Rota r = pacote.getRotas().get(pacote.getRotas().size() - 1);

            if(pacote.isEntregue()) {
                imgStatus.setImageResource(R.mipmap.ic_pacote_entregue);
                textStatus.setText("Entregue");
                textStatus.setTextColor(getColor(R.color.green));

                labelDataChegada.setText("Data de entrega:");
                textDataChegada.setText(dateFormatter.format(r.getDataFim()));
            }
            else {

                labelDataChegada.setText("Estimativa de entrega:");

                if(r.getDataFim().after(r.getDataInicio())) {

                    waitTimer = 300000;
                    imgStatus.setImageResource(R.mipmap.ic_pacote_em_agencia);
                    textStatus.setText("Em Agência");
                    textStatus.setTextColor(getColor(R.color.yellow));

                    Endereco e = r.getDestino();
                    String ruaNum;

                    if(e.getComplemento() != null) {
                        ruaNum = String.format("%s, %s, %s",e.getLogradouro(),e.getNumero(),e.getComplemento());
                    }
                    else {
                        ruaNum = String.format("%s, %s",e.getLogradouro(),e.getNumero());
                    }

                    String bairroCep = String.format("%s, %s",e.getCep(),e.getBairro());
                    String cidadeEstado = String.format("%s, %s",e.getMunicipio(),e.getEstado());

                    textRuaNum.setText(ruaNum);
                    textBairroCep.setText(bairroCep);
                    textCidadeEstado.setText(cidadeEstado);

                    formEmAgencia.setVisibility(View.VISIBLE);
                }
                else {
                    waitTimer = 20000;
                    imgStatus.setImageResource(R.mipmap.ic_pacote_transporte);
                    textStatus.setText("Em Transporte");
                    textStatus.setTextColor(getColor(R.color.blue));
                }
            }
        }

        else {
            waitTimer = 300000;
            imgStatus.setImageResource(R.mipmap.ic_pacote_em_agencia);
            textStatus.setText("Sem Rotas Agendadas");
            textStatus.setTextColor(getColor(R.color.yellow));
        }

        remetente.setText(pacote.getRemetente());
        destinatario.setText(pacote.getDestinatario());

        Endereco e = pacote.getDestino();
        String ruaNum;

        if(e.getComplemento().isEmpty()) {
            ruaNum = String.format("%s, %s",e.getLogradouro(),e.getNumero());
        }
        else {
            ruaNum = String.format("%s, %s, %s",e.getLogradouro(),e.getNumero(),e.getComplemento());
        }

        String bairroCep = String.format("%s, %s",e.getCep(),e.getBairro());
        String cidadeEstado = String.format("%s, %s",e.getMunicipio(),e.getEstado());

        destinatarioRuaNum.setText(ruaNum);
        destinatarioBairroCep.setText(bairroCep);
        destinatarioCidadeEstado.setText(cidadeEstado);


        textDataPostagem.setText(dateFormatter.format(pacote.getDataPostagem()));

        listaItens.removeAllViews();

        for (Item i:pacote.getConteudo()) {

            View v = getLayoutInflater().inflate(R.layout.view_itens_pacote,null);
            TextView t1 = v.findViewById(R.id.itemNome);
            TextView t2 = v.findViewById(R.id.itemQtd);

            t1.setText(i.getDescricao());
            t2.setText(String.format(Locale.getDefault(),"x %d",i.getQuantidade()));

            listaItens.addView(v);
        }


    }

    private void updateProgress(final int progress) {

        final int tempoRestante = waitTimer* (100 - progress)/100000;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textProgress.setText(String.format(Locale.getDefault(),getString(R.string.detalhesProgress),tempoRestante));
                barProgress.setProgress(progress);

            }
        });
    }
}
