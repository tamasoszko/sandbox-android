package no.apps.dnproto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.ObjectGraph;
import no.apps.dnproto.dagger.ApplicationContext;


/**
 * Created by oszi on 05/01/16.
 */
public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ArticelHolder> {

    @Inject @ApplicationContext
    Context context;

    private int itemCount = 3;

    public MainFragmentAdapter(ObjectGraph og) {
        og.inject(this);
    }

    @Override
    public ArticelHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ArticelHolder vh = new ArticelHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArticelHolder holder, int position) {
        holder.categoryTextView.setText(String.format("Category@%d", position));
        holder.headlineTextView.setText(String.format("Title@%d", position));
        holder.summaryTextView.setText(R.string.lorem_ipsum);
        Picasso.with(context).load(R.drawable.img3).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void add() {
        notifyItemInserted(itemCount);
        itemCount++;
    }

    public void remove() {
        itemCount--;
        notifyItemRemoved(itemCount);
    }

    public static class ArticelHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.categoryText)
        TextView categoryTextView;

        @Bind(R.id.headlineText)
        TextView headlineTextView;

        @Bind(R.id.summaryText)
        TextView summaryTextView;

        @Bind(R.id.imageView)
        ImageView imageView;

        public ArticelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
