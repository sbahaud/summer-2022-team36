package com.google.sps.model;

import java.util.ArrayList;
import java.util.List;

public class BudgetResponse {
    List<String> htmlToIntegrate;

    public BudgetResponse(){
        htmlToIntegrate = new ArrayList<String>();
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
}