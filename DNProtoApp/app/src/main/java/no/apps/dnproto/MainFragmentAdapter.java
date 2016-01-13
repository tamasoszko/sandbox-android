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
import no.apps.dnproto.dagger.ApplicationContext;
import no.apps.dnproto.dal.ArticleIndex;
import no.apps.dnproto.dal.ArticleProvider;
import no.apps.dnproto.dal.Article;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by oszi on 05/01/16.
 */
public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ArticleHolder> {

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    @Named("Parse")
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
        articles.clear();
        articleProvider.getArticleIndexes()
            .flatMap(downloadArticle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(articleUpdate(), errorHandler(), refreshFinished());
    }

    private Func1<ArticleIndex, Observable<Article>> downloadArticle() {
        return new Func1<ArticleIndex, Observable<Article>>() {
            @Override
            public Observable<Article> call(ArticleIndex articleIndex) {
                return articleProvider.getArticle(articleIndex.getId());
            }
        };
    }

    private Action1<Article> articleUpdate() {
        return new Action1<Article>() {
            @Override
            public void call(Article article) {
                articles.add(article);
                notifyDataSetChanged();
            }
        };
    }

    private Action1<Throwable> errorHandler() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(getClass().getName(), throwable.getLocalizedMessage(), throwable);
            }
        };
    }

    private Action0 refreshFinished() {
        return new Action0() {
            @Override
            public void call() {
                Log.d(getClass().getName(), "Articles updated.");
            }
        };
    }

}
