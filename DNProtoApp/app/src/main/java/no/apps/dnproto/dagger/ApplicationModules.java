package no.apps.dnproto.dagger;

import dagger.Module;
import no.apps.dnproto.MainActivity;
import no.apps.dnproto.MainActivityFragment;
import no.apps.dnproto.MainFragmentAdapter;

@Module(includes = AndroidModule.class,
    injects = {MainActivity.class, MainFragmentAdapter.class}
)
public class ApplicationModules {

}
