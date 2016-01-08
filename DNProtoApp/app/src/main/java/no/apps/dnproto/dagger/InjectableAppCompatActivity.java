package no.apps.dnproto.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import no.apps.dnproto.Application;

/**
 * Created by oszi on 07/01/16.
 */
public abstract class InjectableAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((Application) getApplication()).getObjectGraph().inject(this);
    }
}
