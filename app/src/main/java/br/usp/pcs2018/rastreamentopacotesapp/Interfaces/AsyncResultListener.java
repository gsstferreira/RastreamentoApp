package br.usp.pcs2018.rastreamentopacotesapp.Interfaces;

import br.usp.pcs2018.rastreamentopacotesapp.Models.HttpRequestObjects.HttpResponse;

// Interface utilizada para receber resultados de Async Tasks
public interface AsyncResultListener {

    void onAsyncFinished(Object obj, int callerCode, int type);
}
