package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.sps.model.BudgetResponse;
import com.google.sps.model.Trip;
import com.google.sps.model.budgetResponse;

@WebServlet("/get-budget")
public class getBudget extends HttpServlet{

    /**
     * Sends a JSON response containing all the estimated contributions and budgets of trips associated with the user
     * @param req contains a userID
     * @param resp returns JSON  with list of budgets and expected contributions
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userID = req.getHeader("userID");

        List<String> associatedTripIDs = getAssociatedTrips();

        BudgetResponse responseObj = new BudgetResponse();
        for (String tripID: associatedTripIDs){

            double contribution;
            try {
                contribution = getEstimatedContribution(tripID);
            } catch (IllegalArgumentException e){
                responseObj.addToErrors("Could not calculate expected contribution");
                continue;
            }
            
            double tripBudget;
            try {
                tripBudget = getTripBuget(tripID);
            } catch (IllegalArgumentException e) {
                responseObj.addToErrors("Could not get trip budget");
                continue;
            }
            
            //no errors
            responseObj.addToHTML(tripBudget, contribution);
        }
    }
}