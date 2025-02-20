<!DOCTYPE html>
<html>
<head>
<style>
  html, body {
    height: 100%;
    margin: 0;
  }
  
  body {
    font-family: Arial, sans-serif;
    padding: 2% 5%;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    box-sizing: border-box;
  }
  
  .content {
    flex: 1;
  }
  
  .header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
  
  .yellow-section {
    background-color: #f4d03f;
    padding: 15px;
    margin: 20px 0;
    box-sizing: border-box;
  }
  
  .input-group {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    gap: 10px;
  }
  
  .input-section {
    display: flex;
    align-items: center;
    gap: 5px;
    min-width: max-content;
  }
  
  .name-section {
    flex: 0 1 25%;
  }
  
  .sex-section {
    flex: 0 1 20%;
  }
  
  .dash-section {
    flex: 0 1 25%;
  }
  
  input[type="text"] {
    padding: 5px;
    border: 1px solid #ccc;
    min-width: 80px;
    max-width: 150px;
    width: 100%;
    box-sizing: border-box;
  }
  
  select {
    padding: 5px;
    border: 1px solid #ccc;
    width: 100px;
    box-sizing: border-box;
  }
  
  .navigation {
    display: flex;
    justify-content: space-between;
    margin: 20px 0;
    align-items: center;
  }
  
  .nav-group {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .nav-buttons {
    display: flex;
  }
  
  .nav-buttons button {
    margin: 0;
    border-right: none;
  }
  
  .nav-buttons button:last-child {
    border-right: 1px solid #999;
  }
  
  button {
    padding: 5px 10px;
    background-color: #e0e0e0;
    border: 1px solid #999;
    cursor: pointer;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  th, td {
    border: 1px solid #999;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #e0e0e0;
  }
  
  .table-container {
    background-color: #f4d03f;
    padding: 15px;
  }
  
  .action-buttons {
    margin-top: 20px;
    display: flex;
    gap: 10px;
  }
  
  .action-buttons button:first-child {
    width: 120px; /* Add new button width */
  }
  
  .action-buttons button:last-child {
    width: 60px; /* Delete button width */
  }
  
  .footer {
    margin-top: auto;
    padding-top: 20px;
    color: #666;
    font-size: 0.9em;
  }
  
  .footer hr {
    margin: 0 0 10px 0;
    border: none;
    border-top: 1px solid #000;
  }
  
  .blue-bar {
    background-color: blue;
    height: 20px;
    margin: 20px 0;
    width: 100%;
  }

  @media screen and (max-width: 768px) {
    .input-group {
      flex-wrap: wrap;
    }
    
    .input-section {
      flex: 1 1 auto;
    }
  }
</style>
</head>
<body>
  <div class="content">
    <div class="header">
      <div>Welcome <></div>
      <div>Log out</div>
    </div>
    
    <div class="blue-bar"></div>
    
    <div class="yellow-section">
      <div class="input-group">
        <div class="input-section name-section">
          <label>Name</label>
          <input type="text">
        </div>
        <div class="input-section sex-section">
          <label>Sex</label>
          <select>
            <option value="">Select...</option>
          </select>
        </div>
        <div class="input-section dash-section">
          <input type="text">
          <span>-</span>
          <input type="text">
        </div>
        <button>Search</button>
      </div>
    </div>
    
    <div class="navigation">
      <div class="nav-group">
        <div class="nav-buttons">
          <button><<</button>
          <button><</button>
        </div>
        <span>Previous</span>
      </div>
      <div class="nav-group">
        <span>Next</span>
        <div class="nav-buttons">
          <button>></button>
          <button>>></button>
        </div>
      </div>
    </div>
    
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>□</th>
            <th>ID</th>
            <th>Name</th>
            <th>Sex</th>
            <th>Birthday</th>
            <th>Address</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
    </div>
    
    <div class="action-buttons">
      <button>Add new</button>
      <button>Delete</button>
    </div>
  </div>
  
  <div class="footer">
    <hr>
    <div>design/itMKqdp8jnrUVnrYuuf29O/Untitled?node-id=1-2&t=7AqMvY4XzGDd5tEt-0</div>
  </div>
</body>
</html>














  package action;

import java.io.IOException;
import factory.LoginFactory;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private final LoginFactory loginFactory = new LoginFactory();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            loginFactory.processLogout(session);
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userid = request.getParameter("userid");
        String pass = request.getParameter("pass");
        
        // Xử lý logic login thông qua factory
        String errorMsg = loginFactory.processLogin(userid, pass);

        if (!errorMsg.isEmpty()) {
            request.setAttribute("error", errorMsg);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // Lấy thông tin user và lưu vào session
            UserDTO user = loginFactory.getUserInfo(userid);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("username", user.getUsername());
            }
            response.sendRedirect("search.jsp");
        }
    }
}


















package factory;

import dao.UserDAO;
import dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public class LoginFactory {
    private final UserDAO userDAO = new UserDAO();
    
    public void processLogout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
    
    public String processLogin(String userid, String pass) {
        String errorMsg = "";
        
        if (userid == null || userid.trim().isEmpty()) {
            errorMsg = "nouser";
        } else if (pass == null || pass.trim().isEmpty()) {
            errorMsg = "nopass";
        } else if (!userDAO.validateUser(userid, pass)) {
            errorMsg = "Invalid userid or password!";
        }
        
        return errorMsg;
    }
    
    public UserDTO getUserInfo(String userid) {
        return userDAO.getUserById(userid);
    }
} 
