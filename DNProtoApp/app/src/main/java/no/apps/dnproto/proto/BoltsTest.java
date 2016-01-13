package no.apps.dnproto.proto;

import android.util.Log;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import no.apps.dnproto.asynch.BoltsUtil;
import no.apps.dnproto.asynch.Callback;

/**
 * Created by oszi on 11/01/16.
 */
public class BoltsTest implements AsynchTest {

    @Override
    public void preform() {
        BoltsUtil.completeOnMainThread(
            Task.callInBackground(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    return "One";
                }
            })
                .onSuccessTask(new Continuation<String, Task<String>>() {
                    @Override
                    public Task<String> then(final Task<String> task) throws Exception {
                        return Task.call(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                System.out.println(task.getResult());
                                Thread.sleep(2000);
//                            throw new Exception("Failed to do work");
                                return task.getResult() + "Two";
                            }
                        });
                    }
                })
                .onSuccessTask(new Continuation<String, Task<String>>() {
                    @Override
                    public Task<String> then(final Task<String> task) throws Exception {
                        return Task.call(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                System.out.println(task.getResult());
                                Thread.sleep(2000);
                                return task.getResult() + "Three";
                            }
                        });
                    }
                })
            ,
            new Callback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d(AsynchTest.LOG_TAG, "Success, result=" + result);
                }

                @Override
                public void onError(Exception error) {
                    Log.e(AsynchTest.LOG_TAG, "Error, reason=" + error.getLocalizedMessage(), error);
                }

                @Override
                public void onCancel() {
                    Log.d(AsynchTest.LOG_TAG, "Cancelled");
                }
            }
        );
    }
}
