package action;

import java.io.IOException;
import java.util.List;

import factory.CustomerFactory;
import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/search.jsp")
public class CustomerServlet extends HttpServlet {
    private final CustomerFactory customerFactory = new CustomerFactory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy parameters từ request
        String name = request.getParameter("customerName");
        String sex = request.getParameter("sex");
        String birthdayFrom = request.getParameter("birthdayFrom");
        String birthdayTo = request.getParameter("birthdayTo");
        String pageStr = request.getParameter("page");

        // Xử lý thông qua factory
        int totalRecords = customerFactory.getTotalRecords(name, sex, birthdayFrom, birthdayTo);
        int maxPage = customerFactory.getMaxPage(totalRecords);
        int page = customerFactory.validateAndGetPage(pageStr, maxPage);
        
        // Lấy danh sách khách hàng
        List<CustomerDTO> customers = customerFactory.processSearch(name, sex, birthdayFrom, birthdayTo, page);

        // Set attributes cho request
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("totalRecords", totalRecords);
        
        // Lưu lại các giá trị tìm kiếm
        request.setAttribute("searchName", name);
        request.setAttribute("searchSex", sex);
        request.setAttribute("searchBirthdayFrom", birthdayFrom);
        request.setAttribute("searchBirthdayTo", birthdayTo);
        
        request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            String[] customerIds = request.getParameterValues("customerIds");
            boolean success = customerFactory.processDelete(customerIds);
            
            if (success) {
                // Lấy lại các tham số tìm kiếm và trang hiện tại
                String name = request.getParameter("customerName");
                String sex = request.getParameter("sex");
                String birthdayFrom = request.getParameter("birthdayFrom");
                String birthdayTo = request.getParameter("birthdayTo");
                String currentPage = request.getParameter("currentPage");
                
                // Tạo URL redirect với các tham số
                StringBuilder redirectURL = new StringBuilder("search.jsp?");
                if (name != null && !name.isEmpty()) {
                    redirectURL.append("customerName=").append(name).append("&");
                }
                if (sex != null && !sex.isEmpty()) {
                    redirectURL.append("sex=").append(sex).append("&");
                }
                if (birthdayFrom != null && !birthdayFrom.isEmpty()) {
                    redirectURL.append("birthdayFrom=").append(birthdayFrom).append("&");
                }
                if (birthdayTo != null && !birthdayTo.isEmpty()) {
                    redirectURL.append("birthdayTo=").append(birthdayTo).append("&");
                }
                if (currentPage != null && !currentPage.isEmpty()) {
                    redirectURL.append("page=").append(currentPage);
                }
                
                response.sendRedirect(redirectURL.toString());
                return;
            }
        }
        
        doGet(request, response);
    }
}

///////////////////////////

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
//////////////////////////

package factory;

import java.util.List;
import dao.CustomerDAO;
import dto.CustomerDTO;

public class CustomerFactory {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private static final int RECORDS_PER_PAGE = 5;

    public List<CustomerDTO> processSearch(String name, String sex, String birthdayFrom, 
            String birthdayTo, int page) {
        return customerDAO.searchCustomers(name, sex, birthdayFrom, birthdayTo, page);
    }

    public int getTotalRecords(String name, String sex, String birthdayFrom, String birthdayTo) {
        return customerDAO.getTotalRecords(name, sex, birthdayFrom, birthdayTo);
    }

    public int getMaxPage(int totalRecords) {
        return customerDAO.getMaxPage(totalRecords);
    }

    public boolean processDelete(String[] customerIds) {
        if (customerIds == null || customerIds.length == 0) {
            return false;
        }
        return customerDAO.deleteCustomers(customerIds);
    }

    public int validateAndGetPage(String pageStr, int maxPage) {
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
                if (page > maxPage) page = maxPage;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        return page;
    }
} 

//////////////////////////

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
///////////////////////////////////////////
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.util.List, dto.CustomerDTO, jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách khách hàng</title>
    <style>
        table { width: 80%; margin: auto; border-collapse: collapse; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .user-info { text-align: right; margin-bottom: 20px; }
        .search-form { width: 80%; margin: 20px auto; padding: 15px; border: 1px solid #ddd; }
        .search-form input, .search-form select { margin: 5px; padding: 5px; }
        button {
            padding: 5px 15px;
            margin: 0 5px;
            cursor: pointer;
            background-color: #ffffff;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        
        button:hover:not(:disabled) {
            background-color: #e9ecef;
        }
        
        button:disabled {
            cursor: not-allowed;
            opacity: 0.6;
        }
        
        span {
            display: inline-block;
            vertical-align: middle;
        }
        
        /* Style cho link ID và nút Add New */
        td a {
            color: blue;
            text-decoration: none;
            display: inline-block;
            vertical-align: middle;
        }
        
        td a:hover {
            text-decoration: underline;
        }
        
        .triangle {
            color: blue;
            font-size: 10px;
            margin-right: 5px;
            display: inline-block;
            vertical-align: middle;
        }
        
        /* Đảm bảo cell chứa ID và tam giác được căn chỉnh đúng */
        td {
            vertical-align: middle;
        }
    </style>
    <script>
        function isValidDate(dateStr) {
            // Kiểm tra định dạng YYYY/MM/DD
            var dateFormat = /^\d{4}\/\d{2}\/\d{2}$/;
            if (!dateFormat.test(dateStr)) {
                return false;
            }

            // Tách ngày tháng năm
            var parts = dateStr.split('/');
            var year = parseInt(parts[0], 10);
            var month = parseInt(parts[1], 10);
            var day = parseInt(parts[2], 10);

            // Kiểm tra năm (4 chữ số)
            if (year < 1000 || year > 9999) {
                return false;
            }

            // Kiểm tra tháng
            if (month < 1 || month > 12) {
                return false;
            }

            // Kiểm tra ngày
            var daysInMonth = new Date(year, month, 0).getDate();
            if (day < 1 || day > daysInMonth) {
                return false;
            }

            return true;
        }

        function validateDates() {
            var birthdayFrom = document.getElementsByName("birthdayFrom")[0].value;
            var birthdayTo = document.getElementsByName("birthdayTo")[0].value;
            
            // Kiểm tra birthdayFrom nếu có giá trị
            if (birthdayFrom) {
                if (!isValidDate(birthdayFrom)) {
                    alert("Ngày sinh từ không hợp lệ. Vui lòng nhập theo định dạng YYYY/MM/DD và đảm bảo ngày tháng hợp lệ");
                    return false;
                }
            }
            
            // Kiểm tra birthdayTo nếu có giá trị
            if (birthdayTo) {
                if (!isValidDate(birthdayTo)) {
                    alert("Ngày sinh đến không hợp lệ. Vui lòng nhập theo định dạng YYYY/MM/DD và đảm bảo ngày tháng hợp lệ");
                    return false;
                }
            }
            
            // Kiểm tra khoảng thời gian nếu cả hai trường đều có giá trị
            if (birthdayFrom && birthdayTo) {
                var fromParts = birthdayFrom.split('/');
                var toParts = birthdayTo.split('/');
                
                var fromDate = new Date(fromParts[0], fromParts[1] - 1, fromParts[2]);
                var toDate = new Date(toParts[0], toParts[1] - 1, toParts[2]);
                
                if (fromDate > toDate) {
                    alert("Khoảng thời gian không hợp lệ (Từ ngày phải nhỏ hơn hoặc bằng Đến ngày)");
                    return false;
                }
            }
            
            return true;
        }

        function toggleAll(source) {
            var checkboxes = document.getElementsByName('customerIds');
            for(var i=0; i<checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>
</head>
<body>
    <div class="user-info">
        <c:set var="username" value="${sessionScope.username != null ? sessionScope.username : 'Người dùng'}" />
        <label>Chào, ${username}</label> | 
        <a href="login?action=logout">Đăng xuất</a>
    </div>

    <!-- Form tìm kiếm -->
    <div class="search-form">
        <form action="search.jsp" method="get" onsubmit="return validateDates()">
            <label>Tên khách hàng:</label>
            <input type="text" name="customerName" value="${searchName}">
            
            <label>Giới tính:</label>
            <select name="sex">
                <option value="-1" ${searchSex == '-1' ? 'selected' : ''}>Tất cả</option>
                <option value="0" ${searchSex == '0' ? 'selected' : ''}>Nam</option>
                <option value="1" ${searchSex == '1' ? 'selected' : ''}>Nữ</option>
            </select>
            
            <label>Ngày sinh từ:</label>
            <input type="text" name="birthdayFrom" value="${searchBirthdayFrom}" placeholder="YYYY/MM/DD">
            
            <label>Đến:</label>
            <input type="text" name="birthdayTo" value="${searchBirthdayTo}" placeholder="YYYY/MM/DD">
            
            <input type="submit" value="Tìm kiếm">
        </form>
    </div>

    <!-- Thêm form bao quanh bảng để xử lý delete -->
    <form id="customerForm" action="search.jsp" method="post">
        <input type="hidden" name="action" value="delete">
        <!-- Thêm các tham số tìm kiếm và trang hiện tại -->
        <input type="hidden" name="customerName" value="${searchName}">
        <input type="hidden" name="sex" value="${searchSex}">
        <input type="hidden" name="birthdayFrom" value="${searchBirthdayFrom}">
        <input type="hidden" name="birthdayTo" value="${searchBirthdayTo}">
        <input type="hidden" name="currentPage" value="${currentPage}">
        
        <!-- Bảng dữ liệu -->
        <h2 style="text-align: center;">Danh sách khách hàng</h2>
        <table>
            <tr>
                <th><input type="checkbox" onclick="toggleAll(this)" id="checkAll"></th>
                <th>ID</th>
                <th>Tên</th>
                <th>Giới tính</th>
                <th>Ngày sinh</th>
                <th>Địa chỉ</th>
            </tr>
            <c:choose>
                <c:when test="${not empty customers}">
                    <c:forEach var="customer" items="${customers}">
                        <tr>
                            <td><input type="checkbox" name="customerIds" value="${customer.id}"></td>
                            <td>
                                <span class="triangle">▶</span>
                                <a href="customer?action=edit&id=${customer.id}">${customer.id}</a>
                            </td>
                            <td>${customer.name}</td>
                            <td>${customer.sex == 0 ? 'Nam' : 'Nữ'}</td>
                            <td>${not empty customer.birthday ? customer.birthday : 'Không có thông tin'}</td>
                            <td>${customer.address}</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6" style="text-align: center;">Không có dữ liệu</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>

        <!-- Các nút chức năng -->
        <div style="text-align: right; margin: 20px 10% 0 0;">
            <button type="button" onclick="window.location.href='customer?action=add'" style="margin-right: 10px;">Add New</button>
            <button type="button" onclick="deleteSelected()" id="deleteButton">Delete</button>
        </div>
    </form>

    <!-- Phân trang -->
<div style="text-align: center; margin-top: 20px;">
    <form action="search.jsp" method="get">
        <input type="hidden" name="customerName" value="${searchName}">
        <input type="hidden" name="sex" value="${searchSex}">
        <input type="hidden" name="birthdayFrom" value="${searchBirthdayFrom}">
        <input type="hidden" name="birthdayTo" value="${searchBirthdayTo}">
        
        <button type="submit" name="page" value="1" ${currentPage == 1 ? 'disabled' : ''}>First</button>
        <button type="submit" name="page" value="${currentPage - 1}" ${currentPage == 1 ? 'disabled' : ''}>Previous</button>
        
        <span>Trang ${currentPage} / ${maxPage}</span>
        
        <button type="submit" name="page" value="${currentPage + 1}" ${currentPage == maxPage ? 'disabled' : ''}>Next</button>
        <button type="submit" name="page" value="${maxPage}" ${currentPage == maxPage ? 'disabled' : ''}>Last</button>
    </form>
</div>

    <!-- Thêm script xử lý delete và checkbox -->
    <script>
        function deleteSelected() {
            var checkboxes = document.getElementsByName('customerIds');
            var selected = false;
            
            for(var i = 0; i < checkboxes.length; i++) {
                if(checkboxes[i].checked) {
                    selected = true;
                    break;
                }
            }
            
            if (!selected) {
                alert('Vui lòng chọn ít nhất một khách hàng để xóa!');
                return;
            }
            
            if(confirm('Bạn có chắc chắn muốn xóa các khách hàng đã chọn?')) {
                document.getElementById('customerForm').submit();
            }
        }

        function toggleAll(source) {
            var checkboxes = document.getElementsByName('customerIds');
            for(var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>
</body>
</html>
