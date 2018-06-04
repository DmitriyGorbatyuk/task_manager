package ua.khai.gorbatiuk.taskmanager.web.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    private static final String REFERER = "referer";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("currentUser");
        }
        response.sendRedirect(request.getHeader(REFERER));
    }
}
