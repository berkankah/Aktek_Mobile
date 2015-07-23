package com.aktekbilisim.services;


import android.content.Context;
import android.os.AsyncTask;

import com.aktekbilisim.interfaces.onAsyncTaskListener;


public class Worker extends AsyncTask<String, Integer, String> {

    private final onAsyncTaskListener listener;
    private Context ctx;

    public Worker(Context ctx, onAsyncTaskListener listener) {
        // TODO Auto-generated constructor stub
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected String doInBackground(String... query) {
        try {
            ServiceHandler sh = new ServiceHandler(ctx);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(query[0], ServiceHandler.POST,
                    query[1]);
            if (jsonStr.equals("time")) {
                cancel(true);
            } else if (jsonStr.equals("500")) {
                cancel(true);
            }

            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        listener.onCancelled();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String jsonSt) {
        super.onPostExecute(jsonSt);
        // Log.v("jsonSt", jsonSt);

        if (jsonSt == null)
            listener.onTaskError();
        else
            listener.onTaskFinished(jsonSt);

    }

}