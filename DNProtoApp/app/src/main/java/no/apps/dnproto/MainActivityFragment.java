package no.apps.dnproto;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.apps.dnproto.proto.AsynchTest;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Named("RetrofitTest")
//    @Named("RxJavaTest")
//    @Named("BoltsTest")
    @Inject
    AsynchTest asynchTest;

    private MainFragmentAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new MainFragmentAdapter(((Application) getActivity().getApplication()).getObjectGraph());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @OnClick(R.id.fab)
    public void fabClicked(FloatingActionButton fab) {
        ((MainFragmentAdapter)recyclerView.getAdapter()).add();
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings :{
                return true;
            }
            case R.id.action_test: {
                asynchTest.preform();
            }
            case R.id.action_refresh: {
                adapter.refresh();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
