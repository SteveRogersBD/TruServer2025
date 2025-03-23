package com.example.greenpulse.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class YoutubeVideo {
    public SearchMetadata search_metadata;
    public SearchParameters search_parameters;
    public SearchInformation search_information;
    public ArrayList<MovieResult> movie_results;
    public ArrayList<VideoResult> video_results;
    public ArrayList<ShortsResult> shorts_results;
    public ArrayList<ChannelsNewToYou> channels_new_to_you;
    public ArrayList<ChannelResult> channel_results;
    public Pagination pagination;
    public SerpapiPagination serpapi_pagination;
    public class Channel{
        public String name;
        public boolean verified;
        public String thumbnail;
        public String link;
    }

    public class ChannelResult{
        public int position_on_page;
        public String title;
        public String link;
        public boolean verified;
        public String handle;
        public int subscribers;
        public String description;
        public String thumbnail;
    }

    public class ChannelsNewToYou{
        public Object position_on_page;
        public String title;
        public String link;
        public String serpapi_link;
        public Channel channel;
        public String published_date;
        public int views;
        public String length;
        public String description;
        public ArrayList<String> extensions;
        public Thumbnail thumbnail;
    }

    public class MovieResult{
        public int position_on_page;
        public String title;
        public String link;
        public String serpapi_link;
        public Channel channel;
        public String length;
        public String description;
        public ArrayList<String> info;
        public ArrayList<String> extensions;
        public String thumbnail;
    }

    public class Pagination{
        public String current;
        public String next;
        public String next_page_token;
    }

    public class SearchInformation{
        public int total_results;
        public String video_results_state;
    }

    public class SearchMetadata{
        public String id;
        public String status;
        public String json_endpoint;
        public String created_at;
        public String processed_at;
        public String youtube_url;
        public String raw_html_file;
        public double total_time_taken;
    }

    public class SearchParameters{
        public String engine;
        public String search_query;
    }

    public class SerpapiPagination{
        public String current;
        public String next;
        public String next_page_token;
    }

    public class Short{
        public String title;
        public String link;
        public String thumbnail;
        public String views_original;
        public int views;
        public String video_id;
    }

    public class ShortsResult{
        public int position_on_page;
        public ArrayList<Short> shorts;
    }

    public class Thumbnail{
        @SerializedName("static")
        public String mystatic;
        public String rich;
    }

    public class VideoResult{
        public int position_on_page;
        public String title;
        public String link;
        public String serpapi_link;
        public Channel channel;
        public String published_date;
        public int views;
        public String length;
        public String description;
        public ArrayList<String> extensions;
        public Thumbnail thumbnail;
    }


}
