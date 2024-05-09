package com.pan.servlets;

import com.pan.bean.User;
import com.pan.dao.UserDAO;
import com.pan.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/register")
public class Register extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        UserDAO userDAO = new UserDAO();
        boolean flag = userDAO.register(user);
        if(flag){
            String fileRoot = getServletContext().getInitParameter("fileRoot");
            Files.createDirectories(Paths.get(fileRoot, name));
            System.out.println("Register successfully!");
            request.setAttribute("errorMsg", "Register successfully, please login now!");
            super.processTemplate("index",request,response);
        } else{
            System.out.println("Register unsuccessfully!");
            request.setAttribute("errorMsg", "Register unsuccessfully, username has been used!");
            super.processTemplate("index",request,response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
