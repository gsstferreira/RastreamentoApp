package br.usp.pcs2018.rastreamentopacotesapp.Services;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.usp.pcs2018.rastreamentopacotesapp.Activities.ListaPacotesActivity;
import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.HttpRequestTask;
import br.usp.pcs2018.rastreamentopacotesapp.Global.Data;
import br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpHeader;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.Models.User;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.URL_API_GERAL;

public abstract class UsuarioService {

    public static void realizarLogin(Context context, String email, String senha) {

        JSONObject json = new JSONObject();

        try {
            json.put("EmailUsuario",email);
            json.put("SenhaUsuario",senha);

            String url = URL_API_GERAL.concat("Usuario/Login");

            HttpHeader header = new HttpHeader("Content-Type","application-json");
            List<HttpHeader> headers = new ArrayList<>();
            headers.add(header);

            HttpRequestTask task = new HttpRequestTask(context,HttpRequestTask.USUARIO,url,"POST",headers,json,5000,5000);

            task.execute();
        }
        catch (JSONException e) {
            Metodos.makeTextDialog(context,"Erro!","Ocorreu um erro apo tentar realizar o login. Tente novamente mais tarde").show();
        }
    }

    public static boolean validarLogin(Context context, HttpResponse resp, String email, String senha) {

        if(resp.getResponseStatus()) {
            User u = new User();
            u.setId(resp.getResponseMessage());

            Data.setUsuario(u);

            SharedPreferences sharedPrefs = context.getSharedPreferences("LoginData",0);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            try {
                editor.putBoolean("AutoLogin",true);
                editor.putString("Email",email);
                editor.putString("Senha",Base64.encodeToString(senha.getBytes("UTF-8"),Base64.DEFAULT));
            }
            catch (UnsupportedEncodingException e) {
                editor.putBoolean("AutoLogin",false);
                editor.putString("Email","");
                editor.putString("Senha","");
            }

            finally {
                editor.apply();
            }

            Intent intent = new Intent(context,ListaPacotesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity)context).finish();

            return true;
        }
        else {
            return false;
        }
    }

}
