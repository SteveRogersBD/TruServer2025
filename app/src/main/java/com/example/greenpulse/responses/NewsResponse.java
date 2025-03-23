package com.example.greenpulse.responses;

import java.util.ArrayList;

public class NewsResponse {
    public SearchMetadata search_metadata;
    public SearchParameters search_parameters;
    public ArrayList<NewsResult> news_results;
    public ArrayList<MenuLink> menu_links;
    public class MenuLink{
        public String title;
        public String topic_token;
        public String serpapi_link;
    }

    public class NewsResult{
        public int position;
        public String title;
        public Source source;
        public String link;
        public String thumbnail;
        public String thumbnail_small;
        public String date;
    }
    public class SearchMetadata{
        public String id;
        public String status;
        public String json_endpoint;
        public String created_at;
        public String processed_at;
        public String google_news_url;
        public String raw_html_file;
        public double total_time_taken;
    }

    public class SearchParameters{
        public String engine;
        public String gl;
        public String hl;
        public String q;
    }

    public class Source{
        public String name;
        public String icon;
        public ArrayList<String> authors;
    }


}
