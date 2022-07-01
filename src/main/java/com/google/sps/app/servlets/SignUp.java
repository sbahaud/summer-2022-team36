package com.google.sps.app.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void writeToDatastore(){
        
    }

    public String validateInput(HttpServletRequest request) {
        return "";//TODO: Complete
    }
}
