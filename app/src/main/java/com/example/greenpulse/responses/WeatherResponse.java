package com.example.greenpulse.responses;

import java.util.ArrayList;

public class WeatherResponse {
    public String city_name;
    public String country_code;
    public ArrayList<Datum> data;
    public String lat;
    public String lon;
    public String state_code;
    public String timezone;
    public class Datum{
        public double app_max_temp;
        public double app_min_temp;
        public int clouds;
        public int clouds_hi;
        public int clouds_low;
        public int clouds_mid;
        public String datetime;
        public double dewpt;
        public double high_temp;
        public double low_temp;
        public Object max_dhi;
        public double max_temp;
        public double min_temp;
        public double moon_phase;
        public double moon_phase_lunation;
        public int moonrise_ts;
        public int moonset_ts;
        public int ozone;
        public int pop;
        public double precip;
        public int pres;
        public int rh;
        public int slp;
        public double snow;
        public double snow_depth;
        public int sunrise_ts;
        public int sunset_ts;
        public double temp;
        public int ts;
        public int uv;
        public String valid_date;
        public double vis;
        public Weather weather;
        public String wind_cdir;
        public String wind_cdir_full;
        public int wind_dir;
        public double wind_gust_spd;
        public double wind_spd;
    }

    public class Weather{
        public String description;
        public int code;
        public String icon;
    }
}
