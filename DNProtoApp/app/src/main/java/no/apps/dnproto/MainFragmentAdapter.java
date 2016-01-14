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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Named;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.ObjectGraph;
import no.apps.dnproto.asynch.Async;
import no.apps.dnproto.asynch.Callback;
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

//        new ArticleBoltsLoader().run();
        new SimpleArticleLoader(articleProvider, new Callback<Void>() {
            @Override
            public void onError(Exception exception) {
                Log.e(getClass().getName(), exception.getLocalizedMessage(), exception);
            }
            @Override
            public void onSuccess(Void param) {
                Log.d(getClass().getName(), "All done.");
            }
        },
        new SimpleArticleLoader.Observer() {
            @Override
            public void onIndexes(List<ArticleIndex> indexes) {
                Log.d(getClass().getName(), "Indexes downloaded, size=" + indexes.size());
            }

            @Override
            public void onArticle(Article article) {
                Log.d(getClass().getName(), "Article downloaded, id=" + article.getId());
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
        }).run();
    }

    private static class SimpleArticleLoader implements Runnable {

        interface Observer {
            void onIndexes(List<ArticleIndex> indexes);
            void onArticle(Article article);
        }

        private final Callback<Void> callback;
        private final Observer observer;
        private final ArticleProvider articleProvider;

        private SimpleArticleLoader(ArticleProvider articleProvider
            , Callback<Void> callback
            , Observer observer) {
            this.articleProvider = articleProvider;
            this.callback = callback;
            this.observer = observer;
        }

        @Override
        public void run() {
            Async.runInBackground(new Runnable() {
                @Override
                public void run() {
                    doRun();
                }
            });
        }

        private void doRun() {
            try {
                List<ArticleIndex> indexes = articleProvider.getArticleIndexes();
                notifyIndexes(Collections.unmodifiableList(indexes));
                final CountDownLatch latch = new CountDownLatch(indexes.size());
                for(final ArticleIndex index : indexes) {
                    new no.apps.dnproto.asynch.Task<Article>(new Callback<Article>() {
                        @Override
                        public void onSuccess(Article article) {
                            notifyArticle(article);
                            latch.countDown();
                        }
                        @Override
                        public void onError(Exception error) {
                            notifyError(error);
                            latch.countDown();
                        }
                    }) {
                        @Override
                        public Article call() throws Exception {
                            return articleProvider.getArticle(index.getId());
                        }
                    }.submit();
                }
                latch.await();
                notifyFinished();
            } catch (Exception e) {
                notifyError(e);
            }
        }

        private void notifyIndexes(final List<ArticleIndex> indexes) {
            if(observer == null) {
                return;
            }
            Async.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    observer.onIndexes(indexes);
                }
            });
        }

        private void notifyError(final Exception exception) {
            Async.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    callback.onError(exception);
                }
            });
        }

        private void notifyArticle(final Article article) {
            if(observer == null) {
                return;
            }
            Async.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    observer.onArticle(article);
                }
            });
        }

        private void notifyFinished() {
            Async.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(null);
                }
            });
        }
    }


    private class ArticleBoltsLoader implements Runnable {

        @Override
        public void run() {
            articles.clear();
            notifyDataSetChanged();


            Task.callInBackground(new DownloadIndexes(articleProvider))
            .onSuccessTask(new DownloadArticlesParallel(articleProvider));
        }
    }

    private class DownloadIndexes implements Callable<List<ArticleIndex>> {

        private final ArticleProvider articleProvider;

        private DownloadIndexes(ArticleProvider articleProvider) {
            this.articleProvider = articleProvider;
        }
        @Override
        public List<ArticleIndex> call() throws Exception {
            return articleProvider.getArticleIndexes();
        }
    }

    private class DownloadArticlesSequentially implements Continuation<List<ArticleIndex>, Task<List<Article>>> {

        private final ArticleProvider articleProvider;

        private DownloadArticlesSequentially(ArticleProvider articleProvider) {
            this.articleProvider = articleProvider;
        }

        @Override
        public Task<List<Article>> then(final Task<List<ArticleIndex>> task) throws Exception {
            return Task.call(new Callable<List<Article>>() {
                @Override
                public List<Article> call() throws Exception {
                    List<Article> articles = new ArrayList<Article>();
                    for (ArticleIndex index : task.getResult()) {
                        articles.add(articleProvider.getArticle(index.getId()));
                    }
                    return Collections.unmodifiableList(articles);
                }
            });
        }
    }

    private class DownloadArticlesParallel implements Continuation<List<ArticleIndex>, Task<Void>> {

        private final ArticleProvider articleProvider;

        private DownloadArticlesParallel(ArticleProvider articleProvider) {
            this.articleProvider = articleProvider;
        }

        @Override
        public Task<Void> then(final Task<List<ArticleIndex>> task) throws Exception {
            List<Task<Void>> tasks = new ArrayList<>();
            for (ArticleIndex index : task.getResult()) {
                tasks.add(
                    Task.callInBackground(new DownloadArticle(index))
                        .continueWith(new AppendArticle()));
            }
            return Task.whenAll(tasks);
        }
    }

    private class DownloadArticle implements Callable<Article> {

        private final ArticleIndex index;

        private DownloadArticle(ArticleIndex index) {
            this.index = index;
        }

        @Override
        public Article call() throws Exception {
            return articleProvider.getArticle(index.getId());
        }
    }

    private class AppendArticle implements Continuation<Article, Void> {

        @Override
        public Void then(Task<Article> task) throws Exception {
            if (task.isFaulted()) {
                Log.e(getClass().getName(), task.getError().getLocalizedMessage(), task.getError());
            } else {
                Article article = task.getResult();
                int index = MainFragmentAdapter.this.articles.indexOf(article);
                if(index != -1) {
                    MainFragmentAdapter.this.articles.remove(index);
                    MainFragmentAdapter.this.articles.add(index, article);
                    notifyItemChanged(index);
                } else {
                    MainFragmentAdapter.this.articles.add(article);
                    notifyItemInserted(MainFragmentAdapter.this.articles.size() - 1);
                }
            }
            return null;
        }
    }

    private class ReplaceArticles implements Continuation<List<Article>, Void> {

        @Override
        public Void then(Task<List<Article>> task) throws Exception {
            if (task.isFaulted()) {
                Log.e(getClass().getName(), task.getError().getLocalizedMessage(), task.getError());
            } else {
                List<Article> articles = task.getResult();
                MainFragmentAdapter.this.articles = articles;
                notifyDataSetChanged();
            }
            return null;
        }
    }
}
