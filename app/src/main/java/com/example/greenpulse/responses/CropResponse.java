package com.example.greenpulse.responses;

public class CropResponse {
    public String status;
    public String message;
    public int statusCode;
    public Data data;
    public class Data{
        public int cropId;
        public String cropName;
        public String scientificName;
        public String cropType;
        public double optimalTemperatureMin;
        public double optimalTemperatureMax;
        public String soilType;
        public double soilPhMin;
        public double soilPhMax;
        public String waterRequirement;
        public String sunlightRequirement;
        public String plantingSeason;
        public String harvestingPeriod;
        public String fertilizerRecommendations;
        public String nutrientRequirements;
        public String pests;
        public String diseases;
        public String treatmentSuggestions;
        public String irrigationMethods;
        public String storageConditions;
        public String shortDescription;
    }

}
