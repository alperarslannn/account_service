package account.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println("{");
        writer.println("\"timestamp\" : \""+ LocalDateTime.now() +"\",");
        writer.println("\"status\" :" + HttpServletResponse.SC_FORBIDDEN + ",");
        writer.println("\"error\" : \"Forbidden\",");
        writer.println("\"message\" : \"Access Denied!\",");
        writer.println("\"path\" : \""+request.getServletPath()+"\"");
        writer.println("}");
    }
}
