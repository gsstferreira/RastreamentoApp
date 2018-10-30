package br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.Interfaces.AsyncResultListener;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpHeader;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.R;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos.makeGETRequest;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos.makePOSTRequest;

public class HttpRequestTask extends AsyncTask<Void,Void,HttpResponse> {

    public static final int USUARIO = 0;
    public static final int PACOTES_ATIVOS = 1;
    public static final int PACOTES_HISTORICO = 2;
    public static final int PACOTE_DETALHES = 3;

    private String url;
    private String content;
    private int requestTimeOut;
    private int responseTimeOut;
    private List<HttpHeader> headers;
    private String method;

    private Context originContext;
    private int originId;

    public HttpRequestTask(Context context,int originId,String Url,String Method,List<HttpHeader> Headers,String Content,int requestTimeOut, int responseTimeOut){

        this.originContext = context;
        this.originId = originId;

        this.url = Url;
        this.content = Content;
        this.headers = Headers;
        this.method = Method;
        this.requestTimeOut = requestTimeOut;
        this.responseTimeOut = responseTimeOut;
    }

    public HttpRequestTask(Context context,int originId,String Url, String Method, List<HttpHeader> Headers, JSONObject ContentJson, int requestTimeOut, int responseTimeOut){

        this.originId = originId;

        this.originContext = context;

        this.url = Url;
        this.content = ContentJson.toString();
        this.headers = Headers;
        this.method = Method;
        this.requestTimeOut = requestTimeOut;
        this.responseTimeOut = responseTimeOut;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {

        switch (method) {
            case "GET":
                return makeGETRequest(originContext,url,headers,requestTimeOut,responseTimeOut);
            case "POST":
                return makePOSTRequest(originContext,url,headers,content,requestTimeOut,responseTimeOut);
            default:
                HttpResponse resp = new HttpResponse();
                resp.setResponseStatus(false);
                resp.setResponseMessage(Resources.getSystem().getString(R.string.responseProtocol));
                return resp;
        }
    }

    @Override
    protected void onPostExecute(HttpResponse response) {

        AsyncResultListener listener = (AsyncResultListener) originContext;
        listener.onAsyncFinished(response,originId,ASYNC_HTTP_CODE);
        originContext = null;
    }
}
