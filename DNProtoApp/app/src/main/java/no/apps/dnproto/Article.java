package no.apps.dnproto;

/**
 * Created by oszi on 05/01/16.
 */
public class Article {

    private final String category;
    private final String headline;
    private final String shortText;
    private final String fullText;

    private Article(String category, String headline, String shortText, String fullText) {
        this.category = category;
        this.headline = headline;
        this.shortText = shortText;
        this.fullText = fullText;
    }

    public static class Builder {

        private String category;
        private String headline;
        private String shortText;
        private String fullText;

        public Article build() {
            return new Article(category, headline, shortText, fullText);
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }
        public Builder headline(String headline) {
            this.headline = headline;
            return this;
        }
        public Builder shortText(String shortText) {
            this.shortText = shortText;
            return this;
        }
        public Builder fullText(String fullText) {
            this.fullText = fullText;
            return this;
        }
    }
}
