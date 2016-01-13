package no.apps.dnproto.dal;

import java.util.Date;

/**
 * Created by oszi on 13/01/16.
 */
public class ArticleIndex {

    private String id;

    private Date updatedAt;

    ArticleIndex(Builder builder) {
        this.id = builder.id;
        this.updatedAt = builder.updatedAt;
    }

    public String getId() {
        return id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    static class Builder {
        private String id;
        private Date updatedAt;

        Builder() {
        }

        Builder id(String id) {
            this.id = id;
            return this;
        }

        Builder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        ArticleIndex build() {
            return new ArticleIndex(this);
        }
    }
}
