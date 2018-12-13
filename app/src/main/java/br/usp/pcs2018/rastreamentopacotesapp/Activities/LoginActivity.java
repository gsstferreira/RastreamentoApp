package br.usp.pcs2018.rastreamentopacotesapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.usp.pcs2018.rastreamentopacotesapp.Global.Metodos;
import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;
import br.usp.pcs2018.rastreamentopacotesapp.R;
import br.usp.pcs2018.rastreamentopacotesapp.Services.UsuarioService;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_HTTP_CODE;

public class LoginActivity extends  _BaseActivity {

    private EditText emailForm;
    private EditText senhaForm;

    private String loginEmail;
    private String loginSenha;

    @Override
    public void onAsyncFinished(Object obj, int callerCode, int type, long origin, long selfId){

        switch (type) {

            case ASYNC_HTTP_CODE:

                progressDialog.hide();

                boolean success = UsuarioService.validarLogin(this,(HttpResponse) obj,loginEmail,loginSenha);

                if(!success) {
                    Metodos.makeTextDialog(this,"Erro!",((HttpResponse) obj).getResponseMessage()).show();
                }

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
        setContentView(R.layout.activity_login);

        originId = System.currentTimeMillis();

        emailForm = findViewById(R.id.edit_email);
        senhaForm = findViewById(R.id.edit_senha);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Realizando login...");

        TextView forgotPassword = findViewById(R.id.forgotPassword);

        Button login = findViewById(R.id.login_button);
        Button signup = findViewById(R.id.signup_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForms()) {

                    loginEmail = emailForm.getText().toString();
                    loginSenha = senhaForm.getText().toString();

                    UsuarioService.realizarLogin(LoginActivity.this,loginEmail,loginSenha, originId);
                    progressDialog.show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private boolean checkForms() {

        boolean ok = true;

        emailForm.setError(null);
        senhaForm.setError(null);

        String email = emailForm.getText().toString();
        String senha = senhaForm.getText().toString();

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailForm.setError("Este email não é válido");
            ok = false;
        }

        if(senha.length() < 6) {
            senhaForm.setError("Senha inválida");
            ok = false;
        }
        return  ok;
    }

}
