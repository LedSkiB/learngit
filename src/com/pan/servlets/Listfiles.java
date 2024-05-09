package com.pan.servlets;

import com.pan.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/listfiles")
public class Listfiles extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Redirect to listfiles");

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String fileRoot = getServletContext().getInitParameter("fileRoot");

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String uname = (String) session.getAttribute("user");
        System.out.println("uname: " + uname);
        String userRoot = fileRoot + "\\" + uname;

        File folder = new File(userRoot);
        File[] listOfFiles = folder.listFiles();
        System.out.println("len: " + listOfFiles.length);

        List<File> files = new ArrayList<>();

        if (listOfFiles != null) {
            for (File f : listOfFiles) {
                System.out.println("Get: " + f.getName());
                if (f.isFile()) {
                    files.add(f);

                }
            }
        } else {
            System.out.println("Files Folder is NULL");
        }


        request.setAttribute("allfiles", files);
        super.processTemplate("listfiles", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
