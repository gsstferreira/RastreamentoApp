package br.usp.pcs2018.rastreamentopacotesapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import br.usp.pcs2018.rastreamentopacotesapp.Interfaces.AsyncResultListener;

import static br.usp.pcs2018.rastreamentopacotesapp.Global.Constantes.ASYNC_TIMER_CODE;

public class TimerTask extends AsyncTask<Void,Integer,Boolean> {

    private int instanceId;
    private long originId;
    private AsyncResultListener listener;
    private int time;

    public long selfId;

    public TimerTask(Context context, int instanceId, int waitTime, long originId) {

        this.listener = (AsyncResultListener) context;
        this.instanceId = instanceId;
        this.originId = originId;
        this.selfId = System.currentTimeMillis();
        this.time = waitTime;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {

            int limiteIter = time/500;
            int i;

            for(i = 0; i < limiteIter; i++) {
                Thread.sleep(500);
                listener.onAsyncUpdate((i+1)*100/limiteIter, ASYNC_TIMER_CODE, instanceId, originId);
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {

        listener.onAsyncFinished(b, instanceId,ASYNC_TIMER_CODE, originId,selfId);
        listener = null;
    }

}
