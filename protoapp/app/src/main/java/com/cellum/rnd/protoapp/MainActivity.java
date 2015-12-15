package com.cellum.rnd.protoapp;

 import android.app.AlertDialog;
 import android.net.Uri;
 import android.os.Bundle;
 import android.view.Menu;
import android.view.MenuItem;

 import com.google.inject.Inject;

 import java.util.concurrent.Executors;

 import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {

    @Inject
    SessionManager sessionManager;

    @Inject
    Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationImpl navigationImpl = (NavigationImpl) navigation;
        navigationImpl.setFragmentManager(getSupportFragmentManager());

        sessionManager.createSession(Uri.fromParts("defaultjson", "", "") , new SessionManager.OnLoadSessionListener() {
            @Override
            public void onSuccess() {
                navigation.push("main");
            }
            @Override
            public void onError(Throwable exception) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage(exception.getMessage())
                        .show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        boolean backHandled = navigation.back();
        if(!backHandled) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
