package com.example.duan1_nhom2.Model;

public class Feed {
        private String url;
        private String title;
        private String link;
        private String author;
        private String description;
        private String image;

    public Feed(String url, String title, String link, String author, String description, String image) {
        this.url = url;
        this.title = title;
        this.link = link;
        this.author = author;
        this.description = description;
        this.image = image;
    }


    // Getter Methods

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getAuthor() {
            return author;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            if (description.startsWith("<a ")) {
                String cleanUrl = description.substring(description.indexOf("src=") + 5, description.indexOf("/>") - 2);
                return cleanUrl;
            } else {
                return image;
            }
        }

        // Setter Methods

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImage(String image) {
            this.image = image;
        }
}
