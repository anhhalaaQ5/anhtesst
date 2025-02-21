 Chuyển đổi UserDTO sang Struts 1.2
Trong Struts 1.2, các DTO thường được kế thừa từ ActionForm. Ta cần sửa UserDTO như sau:

🔹 UserForm.java (Struts 1.2)
package dto;

import org.apache.struts.action.ActionForm;

public class UserForm extends ActionForm {
    private int psn;
    private String userid;
    private String pass;
    private String username;

    public int getPsn() { return psn; }
    public void setPsn(int psn) { this.psn = psn; }

    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}


\\\\\\\\\\\\\\\\\\\\\
 2. Chuyển LoginServlet thành LoginAction
Trong Struts 1.2, ta không dùng Servlet mà thay bằng Action.

🔹 LoginAction.java
java
Copy
Edit

package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import dto.UserForm;
import factory.LoginFactory;

public class LoginAction extends Action {
    private final LoginFactory loginFactory = new LoginFactory();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
                                 HttpServletRequest request, HttpServletResponse response) {
        UserForm userForm = (UserForm) form;

        String userid = userForm.getUserid();
        String pass = userForm.getPass();
        String errorMsg = loginFactory.processLogin(userid, pass);

        if (!errorMsg.isEmpty()) {
            request.setAttribute("error", errorMsg);
            return mapping.findForward("fail");
        }

        UserForm user = loginFactory.getUserInfo(userid, pass);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
        }
        return mapping.findForward("success");
    }
}
\\\\\\\\\\\\\\\\\
🛠 3. Cấu hình struts-config.xml
Thêm cấu hình trong WEB-INF/struts-config.xml:
<action-mappings>
    <action path="/login"
            type="action.LoginAction"
            name="userForm"
            scope="request"
            validate="false"
            input="/login.jsp">
        <forward name="success" path="/search.jsp" />
        <forward name="fail" path="/login.jsp" />
    </action>
</action-mappings>
\\\\\\\\\\\\\\\
🛠 4. Cập nhật login.jsp để dùng Struts 1.2
Chỉnh sửa form trong login.jsp để dùng Struts FormBean:

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
    <div class="container">
        <h2>Đăng nhập</h2>
        <html:form action="/login" method="post">
            <label for="userid">User ID:</label>
            <html:text property="userid" />
            
            <label for="pass">Password:</label>
            <html:password property="pass" />
            
            <p class="error">
                <bean:write name="error"/>
            </p>
            
            <html:submit>Login</html:submit>
            <html:reset>Clear</html:reset>
        </html:form>
    </div>
</body>
</html>
\\\\\\\\\\\\\\\\\\\
🛠 5. Cập nhật UserDAO để trả về UserForm
Chỉnh sửa DAO để trả về UserForm thay vì UserDTO:

java
Copy
Edit

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnection;
import dto.UserForm;

public class UserDAO {
    public UserForm getUserById(String userid, String pass) {
        String sql = "SELECT Psn, Userid, Pass, Username FROM MSTUSER WHERE Userid = ? AND Pass = ? AND Delete_ymd IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserForm user = new UserForm();
                user.setPsn(rs.getInt("Psn"));
                user.setUserid(rs.getString("Userid"));
                user.setPass(rs.getString("Pass"));
                user.setUsername(rs.getString("Username"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
\\\\\\\\\\\\\\\\\\\

Cập nhật LoginFactory để trả về UserForm

package factory;

import dao.UserDAO;
import dto.UserForm;

import javax.servlet.http.HttpSession;

public class LoginFactory {
    private final UserDAO userDAO = new UserDAO();
    
    public void processLogout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
    
    public String processLogin(String userid, String pass) {
        if (userid == null || userid.trim().isEmpty()) {
            return "Vui lòng nhập userid";
        }
        if (pass == null || pass.trim().isEmpty()) {
            return "Vui lòng nhập password";
        }

        UserForm user = userDAO.getUserById(userid, pass);
        return user == null ? "Userid hoặc password không đúng" : "";
    }
    
    public UserForm getUserInfo(String userid, String pass) {
        return userDAO.getUserById(userid, pass);
    }
}
