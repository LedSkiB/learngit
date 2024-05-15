package com.pan.servlets;

import com.pan.utils.Settings;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/download")
public class Download extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 通过session传入当前的用户名
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("user");

        String uname = request.getParameter("uname");
        String filename = request.getParameter("downfile");
        if (filename == null || "".equals(filename.trim())) {
            response.getWriter().write("Input the filename");
            return;
        }

        String fileRoot = new Settings().fileRoot;
        String userRoot = fileRoot + "\\" + uname;
        File file = new File(userRoot, filename);

        if (file.exists() && file.isFile()) {
            // 设置响应模式
            response.setContentType("application/octet-stream");
            // 设置响应头
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            // 得到输入流
            InputStream inputStream = new FileInputStream(file);
            // 得到字节输出流
            ServletOutputStream outputStream = response.getOutputStream();
            // 循环输出
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 关闭
            outputStream.close();
            inputStream.close();
        }
        else {
            response.getWriter().write("File not found");
            response.getWriter().close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
