package no.apps.dnproto.dal;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by oszi on 13/01/16.
 */
public class Article {

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("objectId")
    private String id;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("category")
    private String category;

    @SerializedName("title")
    private String title;

    @SerializedName("summaryText")
    private String summaryText;

    @SerializedName("headlineText")
    private String headlineText;

    @SerializedName("bodyText")
    private String bodyText;


    @Override
    public String toString() {
        return "Article{" +
            "createdAt=" + createdAt +
            ", id='" + id + '\'' +
            ", updatedAt=" + updatedAt +
            ", category='" + category + '\'' +
            ", title='" + title + '\'' +
            ", summaryText='" + summaryText + '\'' +
            ", bodyText='" + bodyText + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return !(id != null ? !id.equals(article.id) : article.id != null);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public String getHeadlineText() {
        return headlineText;
    }

    public String getBodyText() {
        return bodyText;
    }
}
