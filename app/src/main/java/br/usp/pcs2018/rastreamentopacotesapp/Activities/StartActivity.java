package br.usp.pcs2018.rastreamentopacotesapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks.TimerTask;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.R;
import br.usp.pcs2018.rastreamentopacotesapp.Services.UsuarioService;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;
import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class StartActivity extends  _BaseActivity {

    private TextView textoLoading;

    private String loginEmail;
    private String loginSenha;

    private int loadingCount = 0;

    @Override
    public void onAsyncFinished(Object obj, int callerCode, int type, long origin, long selfId){

        switch (type) {
            case ASYNC_HTTP_CODE:

                boolean success = UsuarioService.validarLogin(this,(HttpResponse) obj,loginEmail,loginSenha);

                if(!success) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Erro!");
                    builder.setMessage(((HttpResponse) obj).getResponseMessage());

                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });

                    builder.create().show();
                }
                else {
                    Intent intent = new Intent(StartActivity.this,ListaPacotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case ASYNC_TIMER_CODE:
                loadingCount++;
                update();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAsyncUpdate(int progresso, int type, int callerCode, long origin) {

    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_start);

        originId = System.currentTimeMillis();

        textoLoading = findViewById(R.id.textoLoading);
    }

    @Override
    public void onResume() {
        super.onResume();

        update();
    }

    private void update() {

        TimerTask timer;

        switch (loadingCount) {
            case 0:
                textoLoading.setText("Checando versão...");
                timer = new TimerTask(this,1,3000, originId);
                timer.execute();
                break;
            case 1:
                textoLoading.setText("Carregando app...");
                timer = new TimerTask(this,1,1000, originId);
                timer.execute();
                break;
            case 2:

                SharedPreferences sharedPrefs = getSharedPreferences("LoginData",0);

                boolean autoLogin = sharedPrefs.getBoolean("AutoLogin",false);

                if(autoLogin) {

                    try {
                        loginEmail = sharedPrefs.getString("Email","");
                        String senhaEncoded = sharedPrefs.getString("Senha","");
                        loginSenha = new String(Base64.decode(senhaEncoded,Base64.DEFAULT),"UTF-8");

                        textoLoading.setText("Realizando Login...");

                        UsuarioService.realizarLogin(this,loginEmail,loginSenha, originId);

                    }
                    catch (UnsupportedEncodingException e){
                        Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                finish();
                break;
        }
    }
}
