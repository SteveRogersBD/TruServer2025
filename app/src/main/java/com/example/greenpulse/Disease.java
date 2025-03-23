package com.example.greenpulse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Disease {

    public String name;
    public String description;
    public String prevention;
    public String treatment;
    public String symptoms;
    public String postManagement;
    public String potentialLoss;
}
