package no.apps.dnproto;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MainFragmentAdapter(((Application)getActivity().getApplication()).getObjectGraph()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab)
    public void fabClicked(FloatingActionButton fab) {
        ((MainFragmentAdapter)recyclerView.getAdapter()).add();
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
    }

}
