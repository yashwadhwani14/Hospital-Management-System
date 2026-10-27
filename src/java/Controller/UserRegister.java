
package Controller;

import Database.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserRegister")
public class UserRegister extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String user = request.getParameter("Username");
        String pass = request.getParameter("password");
        String repass = request.getParameter("repassword");

        if (user == null || user.trim().isEmpty()
                || pass == null || pass.isEmpty()
                || repass == null || repass.isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/userRegister.jsp?error=empty");
            return;
        }

        if (!pass.equals(repass)) {
            response.sendRedirect(
                request.getContextPath() + "/userRegister.jsp?error=password");
            return;
        }

        String sql = "INSERT INTO login VALUES (?, ?)";

        try (Connection con = DatabaseConnection.initializeDatabase();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user);
            pst.setString(2, pass);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                response.sendRedirect(
                    request.getContextPath() + "/index.jsp?registered=1");
            } else {
                response.sendRedirect(
                    request.getContextPath() + "/userRegister.jsp?error=failed");
            }

        } catch (SQLException e) {
            getServletContext().log("User registration failed", e);

            response.sendRedirect(
                request.getContextPath() + "/userRegister.jsp?error=database");
        } catch (Exception e) {
            getServletContext().log("Unexpected registration error", e);

            response.sendRedirect(
                request.getContextPath() + "/userRegister.jsp?error=unexpected");
        }
    }
}
