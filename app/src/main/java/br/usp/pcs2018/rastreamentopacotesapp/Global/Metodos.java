package br.usp.pcs2018.rastreamentopacotesapp.Global;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpHeader;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.R;

public abstract class Metodos {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

    public static String ObjectToJson(Object obj){
        return gson.toJson(obj,obj.getClass());
    }

    public static Object JsonToObject(String json, Class c) {
        return gson.fromJson(json, c);
    }

    //Realiza uma chamada HTTP GET
    public static HttpResponse makeGETRequest(Context context, String url, List<HttpHeader> headers, int requestTimeout, int responseTimeout) {
        return httpRequest(context, url,"GET",headers,null,requestTimeout,responseTimeout);
    }

    //Realiza uma chama HTTP POST, com conteúdo passado como string
    public static HttpResponse makePOSTRequest(Context context, String url, List<HttpHeader> headers, String content, int requestTimeout, int responseTimeout) {
        return httpRequest(context, url,"POST",headers,content,requestTimeout,responseTimeout);
    }

    //Realiza uma chama HTTP POST, com conteúdo passado com JSONObject
    public static HttpResponse makePOSTRequest(Context context, String url, List<HttpHeader> headers, JSONObject jsonContent, int requestTimeout, int responseTimeout) {

        return makePOSTRequest(context, url,headers,jsonContent.toString(),requestTimeout,responseTimeout);

    }

    private static HttpResponse httpRequest(Context context, String url,String method, List<HttpHeader> headers,String content,int requestTimeout, int responseTimeout) {

        HttpResponse resp = new HttpResponse();
        HttpURLConnection connection = null;

        Resources res = context.getResources();

        try {
            URL requestUrl = new URL(url);

            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(requestTimeout);
            connection.setReadTimeout(responseTimeout);

            if(headers != null) {
                for (HttpHeader h:headers) {
                    connection.setRequestProperty(h.getKey(),h.getValue());
                }
            }

            switch (method) {
                case "GET":
                    connection.setDoOutput(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    break;
                case "POST":
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.connect();

                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(content);
                    out.flush();
                    out.close();
                    break;
                default:
                    throw new ProtocolException();
            }

            int resultCode = connection.getResponseCode();

            switch (resultCode) {
                case 200: // Ok
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    JSONObject json = new JSONObject(sb.toString());

                    resp.setResponseStatus(json.getBoolean("Ok"));
                    resp.setResponseMessage(json.getString("Mensagem"));
                    break;
                case 400: // Erro no formato do conteúdo
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.response400));
                    break;
                case 401: // Erro de autenticação
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.response401));
                    break;
                case 403: // Erro de permissão
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.response403));
                    break;
                case 404: // Erro de endereço
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.response404));
                    break;
                case 500: // Erro de processamento
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.response500));
                    break;
                default:
                    resp.setResponseStatus(false);
                    resp.setResponseMessage(res.getString(R.string.responseOther));
                    break;
            }
        }
        catch (SocketTimeoutException e) {
            resp.setResponseStatus(false);
            resp.setResponseMessage(res.getString(R.string.responseTimeout));
        }
        catch (MalformedURLException e) {
            resp.setResponseStatus(false);
            resp.setResponseMessage(res.getString(R.string.responseMalformed));
        }
        catch (ProtocolException e) {
            resp.setResponseStatus(false);
            resp.setResponseMessage(res.getString(R.string.responseProtocol));
        }
        catch (JSONException e) {
            resp.setResponseStatus(false);
            resp.setResponseMessage(res.getString(R.string.responseJson));
        }
        catch (IOException e) {
            resp.setResponseStatus(false);
            resp.setResponseMessage(res.getString(R.string.responseIO));
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return resp;
    }

    public static AlertDialog makeTextDialog(Context context, String titulo, String mensagem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}
