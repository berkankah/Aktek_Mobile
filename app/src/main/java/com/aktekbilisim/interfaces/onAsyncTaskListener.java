package com.aktekbilisim.interfaces;

/**
 * Created by berkan.kahyaoglu on 24.06.2015.
 */
public interface onAsyncTaskListener {
    void onTaskStarted();
    void onCancelled();
    void onTaskFinished(String result);
    void onTaskError();
}

