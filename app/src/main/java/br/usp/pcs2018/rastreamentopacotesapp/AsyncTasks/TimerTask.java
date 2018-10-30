package br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import br.usp.pcs2018.rastreamentopacotesapp.Interfaces.AsyncResultListener;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class TimerTask extends AsyncTask<Void,Void,Boolean> {

    private Context originContext;
    private int originId;

    private int time;

    public TimerTask(Context context, int originId, int waitTime) {

        this.originContext = context;
        this.originId = originId;

        this.time = waitTime;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            Thread.sleep(time);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {

        AsyncResultListener listener = (AsyncResultListener) originContext;
        listener.onAsyncFinished(b,originId,ASYNC_TIMER_CODE);

        originContext = null;
    }

}
