package com.cellum.rnd.protoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import com.cellum.rnd.protoapp.utils.asynch.Task;
import com.cellum.rnd.protoapp.utils.json.JsonMapper;
import com.cellum.rnd.protoapp.utils.json.JsonScreenFlow;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

/**
 * Created by oszkotamas on 26/09/15.
 */
@Singleton
public class SessionManagerImpl implements SessionManager {

    private final Provider<ExecutorService> executorServiceProvider;

    private final Context context;
    private SessionBase session;

    @Inject
    public SessionManagerImpl(Context context, Provider<ExecutorService> executorServiceProvider) {
        this.context = context;
        this.executorServiceProvider = executorServiceProvider;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public void createSession(Uri uri, final OnLoadSessionListener listener) {
        if ("http".equals(uri.getScheme())
                || "https".equals(uri.getScheme())) {

        } else if ("defaultjson".equals(uri.getScheme())) {
            session = new DefaultSessionWithJson(context);
        } else {
            session = new DefaultSession(context);
        }
        setupSession(listener);
    }

    private void setupSession(final OnLoadSessionListener listener) {
        executorServiceProvider.get().execute(new Task<Void, Void>(null) {
            @Override
            public Void doInBackground(Void aVoid) throws Exception {
                session.setup();
                return null;
            }

            @Override
            public void onSuccess(Void aVoid) {
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(Exception exception) {
                if (listener != null) {
                    listener.onError(exception);
                }
            }
        });

    }

    @Override
    public Session getSession() {
        return session;
    }

    private static class DefaultSessionWithJson extends  SessionBase {

        @Inject @Named("Resource")
        private BitmapFactory bitmapFactory;

        public DefaultSessionWithJson(Context context) {
            super(context);
        }

        @Override
        protected void setup() throws Exception {
            String json = IOUtils.toString(context.getResources().openRawResource(R.raw.screenflow));
            System.out.println(json);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<JsonScreenFlow> adapter = moshi.adapter(JsonScreenFlow.class);
            screenFlow = new JsonMapper(injector).screenFlow(adapter.fromJson(json));
        }

        @Override
        protected BitmapFactory getBitmapFactory() {
            return bitmapFactory;
        }
    }

    private static class DefaultSession extends SessionBase {

        @Inject @Named("Resource")
        private BitmapFactory bitmapFactory;

        public DefaultSession(Context context) {
            super(context);
        }

        @Override
        protected void setup() throws Exception {


            screenFlow = new ScreenFlow();
            screenFlow.add(new Screen.Builder()
                    .name("main")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.PushScreen(injector, "drawer")))
                    .event(new TapEvent(injector, new Rect(288, 151, 288 + 56, 151 + 56))
                            .on(new Action.PushScreen(injector, "notification_new_card")))
                    .event(new TapEvent(injector, new Rect(5, 182, 5 + 348, 182 + 106))
                            .on(new Action.PushScreen(injector, "nfc1_card_selecting")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("drawer")
                    .event(new TapEvent(injector, new Rect(0, 0, 293, 120))
                            .on(new Action.Main(injector)))
                    .event(new TapEvent(injector, new Rect(295, 0, 295 + 64, 566))
                            .on(new Action.Main(injector)))
                    .event(new TapEvent(injector, new Rect(26, 146, 26 + 96, 146 + 96))
                            .on(new Action.PushScreen(injector, "bank_cards")))
                    .event(new TapEvent(injector, new Rect(173, 146, 173 + 96, 146 + 96))
                            .on(new Action.PushScreen(injector, "transactions_list")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("nfc1_card_selecting")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .event(new TapEvent(injector, new Rect(74, 504, 74 + 128, 504 + 44))
                            .on(new Action.PushScreen(injector, "nfc2_paying")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("nfc2_paying")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .event(new TapEvent(injector, new Rect(0, 216, 176, 216 + 350))
                            .on(new Action.PushScreen(injector, "nfc3_timeout")))
                    .event(new TapEvent(injector, new Rect(182, 216, 182 + 176, 216 + 350))
                            .on(new Action.PushScreen(injector, "nfc3_paid")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("nfc3_timeout")
                    .event(new TapEvent(injector, new Rect(146, 335, 146 + 68, 335 + 35))
                            .on(new Action.Main(injector)))
                    .event(new TapEvent(injector, new Rect(228, 335, 228 + 82, 335 + 35))
                            .on(new Action.ReplaceScreen(injector, "nfc2_paying")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("nfc3_paid")
                    .event(new TapEvent(injector, new Rect(74, 505, 74 + 211, 505 + 44))
                            .on(new Action.Main(injector)))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("drawer")
                    .event(new TapEvent(injector, new Rect(0, 0, 293, 120))
                            .on(new Action.Main(injector)))
                    .event(new TapEvent(injector, new Rect(295, 0, 295 + 64, 566))
                            .on(new Action.Main(injector)))
                    .event(new TapEvent(injector, new Rect(26, 146, 26 + 96, 146 + 96))
                            .on(new Action.PushScreen(injector, "bank_cards")))
                    .event(new TapEvent(injector, new Rect(173, 146, 173 + 96, 146 + 96))
                            .on(new Action.PushScreen(injector, "transactions_list")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("bank_cards")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .event(new TapEvent(injector, new Rect(46, 80, 46 + 266, 80 + 170))
                            .on(new Action.PushScreen(injector, "bank_card_detail_off")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("transactions_list")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .event(new TapEvent(injector, new Rect(0, 108, 360, 208))
                            .on(new Action.PushScreen(injector, "nfc3_paid")))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("bank_card_detail_off")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .build());

            screenFlow.add(new Screen.Builder()
                    .name("notification_new_card")
                    .event(new TapEvent(injector, new Rect(0, 0, 56, 56))
                            .on(new Action.Back(injector)))
                    .build());

        }

        @Override
        protected BitmapFactory getBitmapFactory() {
            return bitmapFactory;
        }
    }
}
