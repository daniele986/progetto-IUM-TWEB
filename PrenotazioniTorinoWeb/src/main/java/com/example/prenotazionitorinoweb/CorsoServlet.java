package com.example.prenotazionitorinoweb;

import DAO.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet(name = "corsoservlet", value = "/corso-servlet")
public class CorsoServlet extends HttpServlet {
    public void init() {
        DAO.registerDriver();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String post=request.getParameter("post");
        PrintWriter out = response.getWriter();
        String sessione = request.getParameter("sessione");
        HttpSession s = request.getSession();
        if(sessione.equals(s.getId())){
            if (Objects.equals(post, "aggiungi")) {
                String nome = request.getParameter("nome");
                if (DAO.setCorso(nome)) {
                    out.print("Corso aggiunto");
                } else {
                    out.print("Corso non aggiunto");
                }
            }else if (Objects.equals(post, "elimina")) {
                int id = Integer.parseInt(request.getParameter("id"));
                if(!DAO.deleteCorso(id)){
                    out.print("false");
                }
            }
        }else{
            s.invalidate();
            out.print("sessione scaduta");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        ArrayList<corsi> corsi= DAO.getAllCorsi();
        JsonArray allCourses=null;
        String sessione = request.getParameter("sessione");
        HttpSession s = request.getSession();
        String sessionID = s.getId();
        if(sessione.equals(sessionID)){
            allCourses = new JsonArray();
            for (corsi value : corsi){
                int id = value.getId();
                String nome = value.getNome();
                JsonObject course = new JsonObject();
                course.addProperty("id", id);
                course.addProperty("nome", nome);
                allCourses.add(course);
            }
        }
        out.print(allCourses);



    }


}