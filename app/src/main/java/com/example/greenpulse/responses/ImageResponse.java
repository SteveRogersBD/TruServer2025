package com.example.greenpulse.responses;

import java.util.ArrayList;

public class ImageResponse {
    public SearchMetadata search_metadata;
    public SearchParameters search_parameters;
    public SearchInformation search_information;
    public ArrayList<SuggestedSearch> suggested_searches;
    public ArrayList<ImagesResult> images_results;
    public ArrayList<RelatedSearch> related_searches;
    public SerpapiPagination serpapi_pagination;
    public class ImagesResult{
        public int position;
        public String thumbnail;
        public String related_content_id;
        public String serpapi_related_content_link;
        public String source;
        public String source_logo;
        public String title;
        public String link;
        public String original;
        public int original_width;
        public int original_height;
        public boolean is_product;
        public String tag;
        public boolean in_stock;
        public String license_details_url;
    }

    public class RelatedSearch{
        public String link;
        public String serpapi_link;
        public String query;
        public ArrayList<String> highlighted_words;
        public String thumbnail;
    }


    public class SearchInformation{
        public String image_results_state;
    }

    public class SearchMetadata{
        public String id;
        public String status;
        public String json_endpoint;
        public String created_at;
        public String processed_at;
        public String google_images_url;
        public String raw_html_file;
        public double total_time_taken;
    }

    public class SearchParameters{
        public String engine;
        public String q;
        public String google_domain;
        public String hl;
        public String gl;
        public String device;
    }

    public class SerpapiPagination{
        public int current;
        public String next;
    }

    public class SuggestedSearch{
        public String name;
        public String link;
        public String uds;
        public String q;
        public String serpapi_link;
        public String thumbnail;
    }

}
