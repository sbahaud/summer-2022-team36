package com.google.sps.model;

import java.util.ArrayList;
import java.util.List;

public class BudgetResponse {
    List<String> htmlToIntegrate;
    String title;
    String tripID;
    double contribution;
    double tripBudget;

    public BudgetResponse(){
        htmlToIntegrate = new ArrayList<String>();
        contribution = 0.0;
        tripBudget = 0.0;
    }

    public String writeHTMLLine(double budget, double contribution){
        return String.format("<p>Trip Budget: %f, Your Expected Contribution: %f</p>", budget, contribution);
    }

    public void addToHTML(double budget, double contribution){
        String newLine = writeHTMLLine(budget, contribution);
        htmlToIntegrate.add(newLine);
    }

    public void addToErrors(String newError){
        htmlToIntegrate.add(String.format("<p>%s</p>", newError));
    }

    public void addTripBudget(double tripBudget){
        this.tripBudget = tripBudget;
    }

    public void addContribution(double contribution){
        this.contribution = contribution;
    }

    public void addtitle(String title){
        this.title = title;
    }

    public void addID(String id){
        this.tripID = id;
    }

}
