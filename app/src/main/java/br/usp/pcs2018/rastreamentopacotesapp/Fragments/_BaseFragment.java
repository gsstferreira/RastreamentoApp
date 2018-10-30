package br.usp.pcs2018.rastreamentopacotesapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import br.usp.pcs2018.rastreamentopacotesapp.Interfaces.AsyncResultListener;

public abstract class _BaseFragment extends Fragment implements AsyncResultListener {

    String origem;
    ProgressDialog progressDialog;

}
