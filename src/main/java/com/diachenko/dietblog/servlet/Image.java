package com.diachenko.dietblog.servlet;
/*  diet-blog
    20.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.utils.IPService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@WebServlet("/uploads/*")
public class Image extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileName = req.getPathInfo();
        if (fileName == null || fileName.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            String userIp = IPService.getClientIp(req);
            log.warn("Attempt to access invalid file: userIP={} imageName=INVALID", userIp);
            return;
        }

        File file = new File("uploads", fileName);
        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            String userIp = IPService.getClientIp(req);
            log.warn("Attempt to access non-reacheable file: userIP={} imageName={}", userIp, fileName);
            return;
        }

        resp.setContentType(getServletContext().getMimeType(file.getName()));
        Files.copy(file.toPath(), resp.getOutputStream());
    }
}
