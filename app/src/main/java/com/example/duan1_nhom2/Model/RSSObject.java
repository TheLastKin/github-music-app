package com.example.duan1_nhom2.Model;

import java.util.ArrayList;

public class RSSObject {
        private String status;
        public Feed FeedObject;
        public ArrayList<Item> items;

    public RSSObject(String status, Feed feedObject, ArrayList<Item> items) {
        this.status = status;
        FeedObject = feedObject;
        this.items = items;
    }


    // Getter Methods

        public String getStatus() {
            return status;
        }

        public Feed getFeed() {
            return FeedObject;
        }

        // Setter Methods

        public void setStatus(String status) {
            this.status = status;
        }

        public void setFeed(Feed feedObject) {
            this.FeedObject = feedObject;
        }
}
