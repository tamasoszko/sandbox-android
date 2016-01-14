package no.apps.dnproto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.ObjectGraph;
import no.apps.dnproto.asynch.AsyncTaskWithError;
import no.apps.dnproto.dagger.ApplicationContext;
import no.apps.dnproto.dal.Article;
import no.apps.dnproto.dal.ArticleIndex;
import no.apps.dnproto.dal.ArticleProvider;


/**
 * Created by oszi on 05/01/16.
 */
public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ArticleHolder> {

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    @Named("Simple")
    ArticleProvider articleProvider;

    private List<Article> articles = new ArrayList<>();

    public MainFragmentAdapter(ObjectGraph og) {
        og.inject(this);
        refresh();
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ArticleHolder vh = new ArticleHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {
        Article article = articles.get(position);
        holder.categoryTextView.setText(article.getCategory());
        holder.headlineTextView.setText(article.getHeadlineText());
        holder.summaryTextView.setText(article.getSummaryText());
        Picasso.with(context).load(R.drawable.img3).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void add() {
        notifyDataSetChanged();
    }

    public void remove() {
        notifyDataSetChanged();
    }

    public static class ArticleHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.categoryText)
        TextView categoryTextView;

        @Bind(R.id.headlineText)
        TextView headlineTextView;

        @Bind(R.id.summaryText)
        TextView summaryTextView;

        @Bind(R.id.imageView)
        ImageView imageView;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.categoryButton)
        public void onClickCategoryButton(ImageButton imageButton) {
            Log.d(getClass().getName(), "CategoryButton tapped");
        }
    }

    public void refresh() {

        new AsyncTaskArticleArticlesLoader().execute(null);
    }

    private class AsyncTaskArticleArticlesLoader extends AsyncTaskWithError<Void, Void, List<ArticleIndex>> {

        @Override
        public List<ArticleIndex> doInBackground(Void... params) throws Exception {
            return articleProvider.getArticleIndexes();
        }

        @Override
        protected void onSuccess(List<ArticleIndex> indexes) {
            for(ArticleIndex index : indexes) {
                new AsyncTaskArticleLoader().execute(index);
            }
        }

        @Override
        protected void onError(Exception exception) {
            Log.e(getClass().getName(), exception.getLocalizedMessage(), exception);
        }
    }

    private class AsyncTaskArticleLoader extends AsyncTaskWithError<ArticleIndex, Integer, Article> {

        @Override
        public Article doInBackground(ArticleIndex... params) throws Exception {
            return articleProvider.getArticle(params[0].getId());
        }

        @Override
        protected void onSuccess(Article article) {
            int index = articles.indexOf(article);
            if(index != -1) {
                articles.remove(index);
                articles.add(index, article);
                notifyItemChanged(index);
            } else {
                articles.add(article);
                notifyDataSetChanged();
            }
        }

        @Override
        protected void onError(Exception exception) {
            Log.e(getClass().getName(), exception.getLocalizedMessage(), exception);
        }
    }
}
