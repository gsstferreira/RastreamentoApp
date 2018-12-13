package br.usp.pcs2018.rastreamentopacotesapp.Interfaces;

// Interface utilizada para receber resultados de Async Tasks
public interface AsyncResultListener {

    void onAsyncFinished(Object obj, int callerCode, int type, long origin, long selfId);

    void onAsyncUpdate(int progresso, int type, int callerCode, long origin);
}
