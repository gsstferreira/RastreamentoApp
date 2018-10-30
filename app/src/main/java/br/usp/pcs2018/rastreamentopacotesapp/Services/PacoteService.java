package br.usp.pcs2018.rastreamentopacotesapp.Services;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.HttpRequestTask;
import br.usp.pcs2018.rastreamentopacotesapp.Global.Data;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpHeader;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.URL_API_PACOTE;

public abstract class PacoteService {

    public static void buscarPacotesAtivos(Context context) {

        String url = URL_API_PACOTE.concat("Pacote/ObterPacotesAtivos");

        HttpHeader header = new HttpHeader("Authorization", Data.getUsuario().getId());

        List<HttpHeader> headers = new ArrayList<>();
        headers.add(header);

        HttpRequestTask task = new HttpRequestTask(context,HttpRequestTask.PACOTES_ATIVOS,url,"GET",headers,"",5000,5000);
        task.execute();
    }

    public static void buscarPacotesHistorico(Context context) {

        String url = URL_API_PACOTE.concat("Pacote/ObterPacotesHistorico");

        HttpHeader header = new HttpHeader("Authorization", Data.getUsuario().getId());

        List<HttpHeader> headers = new ArrayList<>();
        headers.add(header);

        HttpRequestTask task = new HttpRequestTask(context,HttpRequestTask.PACOTES_HISTORICO,url,"GET",headers,"",5000,5000);
        task.execute();
    }

    public static void buscarDetalhesPacote(Context context, String pacoteId) {

        String url = URL_API_PACOTE.concat("Pacote/ObterDetalhesPacote");

        HttpHeader header = new HttpHeader("Authorization", Data.getUsuario().getId());

        List<HttpHeader> headers = new ArrayList<>();
        headers.add(header);

        JSONObject obj = new JSONObject();

        try {

            obj.put("PacoteId",pacoteId);

            HttpRequestTask task = new HttpRequestTask(context,HttpRequestTask.PACOTE_DETALHES,url,"POST",headers,obj,5000,5000);
            task.execute();
        }
        catch (Exception ignored){}
    }
}
