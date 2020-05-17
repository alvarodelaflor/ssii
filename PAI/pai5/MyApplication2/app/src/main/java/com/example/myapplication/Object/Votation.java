package com.example.myapplication.Object;

import java.io.Serializable;
import java.util.List;

public class Votation implements Serializable {
    private String votationId;
    private String name;
    private String description;
    private List<String> options;

    public Votation() {
    }

    public Votation(String name, String description, List<String> options, String votationId) {
        this.votationId = votationId;
        this.name = name;
        this.description = description;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getVotationId() {
        return votationId;
    }

    public void setVotationId(String votationId) {
        this.votationId = votationId;
    }
}
