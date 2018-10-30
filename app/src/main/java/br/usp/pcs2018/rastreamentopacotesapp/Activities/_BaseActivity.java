package br.usp.pcs2018.rastreamentopacotesapp.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import br.usp.pcs2018.rastreamentopacotesapp.Interfaces.AsyncResultListener;

abstract class _BaseActivity extends AppCompatActivity implements AsyncResultListener {

    ProgressDialog progressDialog;
}
