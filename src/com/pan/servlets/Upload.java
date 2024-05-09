package com.pan.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@WebServlet("/upload")
@MultipartConfig
public class Upload extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String uname = (String) session.getAttribute("user");
        Part part = request.getPart("upfile");
        String upfilename = part.getSubmittedFileName();

        String fileRoot = getServletContext().getInitParameter("fileRoot");
        String userRoot = fileRoot + "\\" + uname;
        System.out.println(userRoot);

        Path rootPath = Paths.get(userRoot);
        Path filePath;

        int tmp = 1;
        int dot = upfilename.lastIndexOf(".");
        String base = upfilename.substring(0, dot);
        String ext = upfilename.substring(dot + 1);
        String newUpfile = upfilename;

        while (true) {
            filePath = Paths.get(userRoot + "\\" + newUpfile);
            if (Files.exists(filePath)) {
                newUpfile = base + " (" + tmp + ")." + ext;
                tmp += 1;
            } else {
                try {
                    System.out.println("Try upload " + userRoot + "\\" + newUpfile);
                    part.write(userRoot + "\\" + newUpfile);
                    break;
                } catch (IOException e) {
                    if (!Files.exists(rootPath)) {
                        Files.createDirectories(rootPath);
                    } else {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
