package com.demo.customasyntasktest;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class CustomAsyncTask<Params,Progress,Result> {

    private Progress mProgress;
    private Result mResult;
    private Params[] mParams;
    private ExecutorService mExecutor;
    protected String TAG = "CustomAsyncTask";

    /*
    Variable to track if the AsyncTask is cancelled
     */
    private boolean isCancelled;

    /*
    Variable to track the state of AsyncTask
     */
    private boolean isRunning;


    protected  void onPreExecute(){
        Log.i(TAG, "OnPreExecute Callback Called");
    }

    protected abstract Result doInBackground(Params... params);

    protected abstract void onProgressUpdate(Progress progress);

    protected void onPostExecute(Result result) {
        Log.i(TAG, "OnPostExecute Callback Called");
    }

    private Handler mHandler;

    /*
    Runnable to execute doInBackground in background thread
     */
    private Runnable doInBackgroundTask = new Runnable() {
        @Override
        public void run() {
            mResult = doInBackground(mParams);
            mHandler.post(notifyTaskCompletion);
        }
    };

    /*
    Runnable to notify and update UI thread with completion status
     */
    private Runnable notifyTaskCompletion = new Runnable() {
        @Override
        public void run() {
                if(!isCancelled)
                    onPostExecute(mResult);
                reset();
        }
    };

    /*
    Runnable to notify and update UI thread with progress status
     */
    private Runnable notifyProgressUpdate = new Runnable() {
        @Override
        public void run() {
            onProgressUpdate(mProgress);
        }
    };

    /*
    Cancels the asynctask
     */
    public void cancel() {
        if(mExecutor != null)
            mExecutor.shutdownNow();
        isCancelled = true;
    }

    /*
    Checks task cancellation status
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /*
    Method to publish progress update
     */
    protected void publishProgress(Progress progress) {
        if(isCancelled)
            return;
        mProgress = progress;
        mHandler.post(notifyProgressUpdate);
    }

    private void execute() {
        if(isRunning)
            return;
        mHandler = new Handler();
        onPreExecute();
        isCancelled = false;
        isRunning = true;
        mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(doInBackgroundTask);
    }

    /*
    Method to start the asynctask
     */
    public void execute(Params... params) {
        mParams = params;
        execute();
    }

    private void reset() {
        isCancelled = true;
        isRunning = false;
        mHandler = null;
        mExecutor = null;
        mParams = null;
        mProgress = null;
        mResult = null;
    }
}
