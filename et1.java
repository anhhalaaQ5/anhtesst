dao
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import dto.CustomerDTO;

public class CustomerDAO {
    private static final int RECORDS_PER_PAGE = 5;

    public int getTotalRecords(String name, String sex, String birthdayFrom, String birthdayTo) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM MSTCUSTOMER WHERE DeleteYMD IS NULL");
        int paramIndex = 1;

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND Name LIKE ?");
        }

        if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex.trim())) {
            sql.append(" AND Sex = ?");
        }

        if (birthdayFrom != null && !birthdayFrom.trim().isEmpty()) {
            sql.append(" AND Birthday >= ?");
        }

        if (birthdayTo != null && !birthdayTo.trim().isEmpty()) {
            sql.append(" AND Birthday <= ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (name != null && !name.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + name.trim() + "%");
            }

            if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex.trim())) {
                ps.setString(paramIndex++, sex.trim());
            }

            if (birthdayFrom != null && !birthdayFrom.trim().isEmpty()) {
                ps.setString(paramIndex++, birthdayFrom.trim());
            }

            if (birthdayTo != null && !birthdayTo.trim().isEmpty()) {
                ps.setString(paramIndex++, birthdayTo.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<CustomerDTO> searchCustomers(String name, String sex, String birthdayFrom, String birthdayTo, int page) {
        List<CustomerDTO> customers = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT Id, Name, Sex, Birthday, Email, Address FROM MSTCUSTOMER WHERE DeleteYMD IS NULL");
        int paramIndex = 1;

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND Name LIKE ?");
        }

        if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex.trim())) {
            sql.append(" AND Sex = ?");
        }

        if (birthdayFrom != null && !birthdayFrom.trim().isEmpty()) {
            sql.append(" AND Birthday >= ?");
        }

        if (birthdayTo != null && !birthdayTo.trim().isEmpty()) {
            sql.append(" AND Birthday <= ?");
        }

        sql.append(" ORDER BY Id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (name != null && !name.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + name.trim() + "%");
            }

            if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex.trim())) {
                ps.setString(paramIndex++, sex.trim());
            }

            if (birthdayFrom != null && !birthdayFrom.trim().isEmpty()) {
                ps.setString(paramIndex++, birthdayFrom.trim());
            }

            if (birthdayTo != null && !birthdayTo.trim().isEmpty()) {
                ps.setString(paramIndex++, birthdayTo.trim());
            }

            ps.setInt(paramIndex++, (page - 1) * RECORDS_PER_PAGE);
            ps.setInt(paramIndex, RECORDS_PER_PAGE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerDTO customer = new CustomerDTO(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Sex").equals("0") ? 0 : 1,
                        rs.getString("Birthday"),
                        rs.getString("Email"),
                        rs.getString("Address")
                    );
                    customers.add(customer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public int getMaxPage(int totalRecords) {
        if (totalRecords == 0) return 1;
        return (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
    }

    public void deleteCustomers(String[] customerIds) {
        StringBuilder sql = new StringBuilder("UPDATE MSTCUSTOMER SET DeleteYMD = GETDATE() WHERE Id IN (");
        
        for (int i = 0; i < customerIds.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < customerIds.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(customerIds[i]));
            }
            
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa khách hàng", e);
        }
    }
    
    public static void main(String[] args) {
        // Khởi tạo đối tượng CustomerDAO
        CustomerDAO customerDAO = new CustomerDAO();
        
        customerDAO.deleteCustomers(new String[]{"1"});
        
    }
    
    public int getNextCustomerId() {
        String sql = "SELECT MAX(Id) + 1 FROM MSTCUSTOMER";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int nextId = rs.getInt(1);
                return rs.wasNull() ? 1 : nextId;
            }
            return 1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addCustomer(String name, String sex, String birthday, 
                          String email, String address, int userPsn) {
        String sql = "INSERT INTO MSTCUSTOMER (Id, Name, Sex, Birthday, Email, Address, " +
                    "DeleteYMD, Insert_ymd, Insertpsncd, Updateymd, Updatepsncd) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NULL, GETDATE(), ?, GETDATE(), ?)";

        try (Connection conn = DBConnection.getConnection()) {
            int newId = getNextCustomerId();
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setString(2, name);
                ps.setString(3, sex);
                ps.setString(4, birthday);
                ps.setString(5, email);
                ps.setString(6, address);
                ps.setInt(7, userPsn);
                ps.setInt(8, userPsn);

                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm khách hàng", e);
        }
    }

    public CustomerDTO getCustomerById(int customerId) {
        String sql = "SELECT Id, Name, Sex, Birthday, Email, Address FROM MSTCUSTOMER " +
                    "WHERE Id = ? AND DeleteYMD IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDTO(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Sex").equals("0") ? 0 : 1,
                        rs.getString("Birthday"),
                        rs.getString("Email"),
                        rs.getString("Address")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCustomer(int customerId, String name, String sex, 
                             String birthday, String email, String address, int userPsn) {
        String sql = "UPDATE MSTCUSTOMER SET " +
                    "Name = ?, Sex = ?, Birthday = ?, Email = ?, Address = ?, " +
                    "DeleteYMD = NULL, Updateymd = GETDATE(), Updatepsncd = ? " +
                    "WHERE Id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            ps.setString(2, sex);
            ps.setString(3, birthday);
            ps.setString(4, email);
            ps.setString(5, address);
            ps.setInt(6, userPsn);
            ps.setInt(7, customerId);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật khách hàng", e);
        }
    }
}
/////////////////
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dto.UserDTO;
import db.DBConnection;

public class UserDAO {
    public boolean validateUser(String userid, String pass) {
        String sql = "SELECT COUNT(*) AS cnt FROM MSTUSER WHERE Userid = ? AND Pass = ? AND Delete_ymd IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("cnt") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserDTO getUserById(String userid) {
        String sql = "SELECT Psn, Userid, Pass, Username FROM MSTUSER WHERE Userid = ? AND Delete_ymd IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserDTO(
                    rs.getInt("Psn"),
                    rs.getString("Userid"),
                    rs.getString("Pass"),
                    rs.getString("Username")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

\\\\\\\\\\\\\\\\\\\\\action
package action;

import java.io.IOException;
import java.util.List;

import dao.CustomerDAO;
import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/search.jsp")
public class CustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy session
        HttpSession session = request.getSession();
        
        String name = request.getParameter("customerName");
        String sex = request.getParameter("sex");
        String birthdayFrom = request.getParameter("birthdayFrom");
        String birthdayTo = request.getParameter("birthdayTo");
        
        // Lưu các điều kiện tìm kiếm vào session
        session.setAttribute("searchName", name);
        session.setAttribute("searchSex", sex);
        session.setAttribute("searchBirthdayFrom", birthdayFrom);
        session.setAttribute("searchBirthdayTo", birthdayTo);
        
        // Xử lý phân trang
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        session.setAttribute("currentPage", String.valueOf(page));

        // Lấy tổng số bản ghi và tính số trang
        int totalRecords = customerDAO.getTotalRecords(name, sex, birthdayFrom, birthdayTo);
        int maxPage = customerDAO.getMaxPage(totalRecords);

        // Điều chỉnh page nếu vượt quá giới hạn
        if (page < 1) page = 1;
        if (page > maxPage) page = maxPage;

        // Lấy dữ liệu theo trang
        List<CustomerDTO> customers = customerDAO.searchCustomers(name, sex, birthdayFrom, birthdayTo, page);

        // Lưu các giá trị vào request
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("totalRecords", totalRecords);
        
        request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            String[] customerIds = request.getParameterValues("customerIds");
            if (customerIds != null && customerIds.length > 0) {
                try {
                    customerDAO.deleteCustomers(customerIds);
                    response.sendRedirect("search.jsp");
                } catch (RuntimeException e) {
                    request.setAttribute("error", e.getMessage());
                    doGet(request, response);
                }
            }
        }
        
        // Nếu không thành công hoặc không có action, chuyển về trang tìm kiếm
        doGet(request, response);
    }
}


\\\\\\\\\\\\\\\\\\\\\
package action;

import java.io.IOException;
import dao.CustomerDAO;
import dto.CustomerDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer")
public class CustomerAddServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Kiểm tra session
        HttpSession session = request.getSession();
        if (session.getAttribute("userPsn") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("add".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/customerAdd.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            String customerId = request.getParameter("id");
            if (customerId != null && !customerId.isEmpty()) {
                CustomerDTO customer = customerDAO.getCustomerById(Integer.parseInt(customerId));
                if (customer != null) {
                    request.setAttribute("customer", customer);
                    request.setAttribute("isEdit", true);
                    request.getRequestDispatcher("/WEB-INF/views/customerAdd.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("search.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Kiểm tra session
        HttpSession session = request.getSession();
        Integer userPsn = (Integer) session.getAttribute("userPsn");
        
        if (userPsn == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        if ("save".equals(action)) {
            try {
                String customerId = request.getParameter("customerId");
                String name = request.getParameter("customerName");
                String sex = request.getParameter("sex");
                String birthday = request.getParameter("birthday");
                String email = request.getParameter("email");
                String address = request.getParameter("address");
                
                boolean isEdit = customerId != null && !customerId.isEmpty();
                
                if (isEdit) {
                    customerDAO.updateCustomer(
                        Integer.parseInt(customerId), name, sex, birthday, email, address, userPsn);
                } else {
                    customerDAO.addCustomer(name, sex, birthday, email, address, userPsn);
                }
                
                // Lấy các tham số tìm kiếm từ session
                String searchName = (String) session.getAttribute("searchName");
                String searchSex = (String) session.getAttribute("searchSex");
                String searchBirthdayFrom = (String) session.getAttribute("searchBirthdayFrom");
                String searchBirthdayTo = (String) session.getAttribute("searchBirthdayTo");
                String currentPage;
                if (isEdit) {
                    currentPage = (String) session.getAttribute("currentPage");
                } else {
                    currentPage = "1";
                }
                
                // Tạo URL cơ bản cho trang search
                StringBuilder redirectURL = new StringBuilder("search.jsp");

                // Biến để kiểm tra đã thêm tham số nào chưa
                boolean isFirstParam = true;

                // Thêm tham số tên nếu có
                if (searchName != null && !searchName.isEmpty()) {
                    redirectURL.append(isFirstParam ? "?" : "&");
                    redirectURL.append("customerName=").append(searchName);
                    isFirstParam = false;
                }

                // Thêm tham số giới tính nếu có và khác "Tất cả"
                if (searchSex != null && !searchSex.isEmpty() && !"-1".equals(searchSex)) {
                    redirectURL.append(isFirstParam ? "?" : "&");
                    redirectURL.append("sex=").append(searchSex);
                    isFirstParam = false;
                }

                // Thêm tham số ngày sinh từ ngày nếu có
                if (searchBirthdayFrom != null && !searchBirthdayFrom.isEmpty()) {
                    redirectURL.append(isFirstParam ? "?" : "&");
                    redirectURL.append("birthdayFrom=").append(searchBirthdayFrom);
                    isFirstParam = false;
                }

                // Thêm tham số ngày sinh đến ngày nếu có
                if (searchBirthdayTo != null && !searchBirthdayTo.isEmpty()) {
                    redirectURL.append(isFirstParam ? "?" : "&");
                    redirectURL.append("birthdayTo=").append(searchBirthdayTo);
                    isFirstParam = false;
                }

                // Thêm số trang nếu có điều kiện tìm kiếm
                if (!isFirstParam && currentPage != null && !currentPage.isEmpty()) {
                    redirectURL.append("&page=").append(currentPage);
                }
                
                response.sendRedirect(redirectURL.toString());
                
            } catch (RuntimeException e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/customerAdd.jsp").forward(request, response);
            }
        }
    }
} \\\\\\\\\\\\\\\\\\\\\\html
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
    <script src="${pageContext.request.contextPath}/js/search.js"></script>
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
</body>
</html>
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm mới khách hàng</title>
    <style>
        .container {
            width: 600px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: inline-block;
            width: 120px;
            margin-right: 10px;
        }
        input[type="text"], select, textarea {
            width: 300px;
            padding: 5px;
        }
        .buttons {
            margin-top: 20px;
            text-align: center;
        }
        button {
            padding: 5px 20px;
            margin: 0 10px;
        }
        .error {
            color: red;
            margin-bottom: 10px;
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

            // Kiểm tra năm hợp lệ (từ năm 1 đến năm hiện tại)
            var currentYear = new Date().getFullYear();
            if (year < 1 || year > currentYear) {
                return false;
            }

            // Kiểm tra tháng
            if (month < 1 || month > 12) {
                return false;
            }

            // Kiểm tra ngày trong tháng
            var daysInMonth = new Date(year, month, 0).getDate();
            if (day < 1 || day > daysInMonth) {
                return false;
            }

            return true;
        }

        function validateForm() {
            var name = document.getElementById("customerName").value;
            var birthday = document.getElementById("birthday").value;
            var email = document.getElementById("email").value;
            var sex = document.getElementsByName("sex")[0].value;
            
            // Reset error message
            document.getElementById("errorMessage").innerHTML = "";
            
            // Kiểm tra giới tính
            if (sex === "-1" || sex === "" || (sex !== "0" && sex !== "1")) {
                document.getElementById("errorMessage").innerHTML = "Vui lòng chọn giới tính (Nam hoặc Nữ)";
                return false;
            }
            
            if (!birthday.trim()) {
                document.getElementById("errorMessage").innerHTML = "Vui lòng nhập ngày sinh";
                return false;
            }
            
            if (!isValidDate(birthday)) {
                document.getElementById("errorMessage").innerHTML = "Birthday wrong";
                return false;
            }
            
            if (!email.trim()) {
                document.getElementById("errorMessage").innerHTML = "Vui lòng nhập email";
                return false;
            }
            
            // Kiểm tra định dạng email
            var emailFormat = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailFormat.test(email)) {
                document.getElementById("errorMessage").innerHTML = "Email không đúng định dạng";
                return false;
            }
            
            return true;
        }
        
        function clearForm() {
            // Xóa giá trị các trường thông tin
            document.getElementById("customerName").value = "";
            document.getElementById("email").value = "";
            document.getElementById("birthday").value = "";
            document.getElementsByName("address")[0].value = "";
            
            // Reset giới tính về giá trị mặc định
            document.getElementsByName("sex")[0].value = "-1";
            
            // Focus vào trường đầu tiên
            document.getElementById("customerName").focus();
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>${customer != null ? 'Edit' : 'Add New'}</h2>
        
        <div id="errorMessage" class="error"></div>
        
        <form id="customerForm" action="customer" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="customerId" value="${customer.id}">
            
            <div class="form-group">
                <label>Customer ID:</label>
                <label style="font-weight: normal;">
                    ${customer != null ? customer.id : '[Tự động]'}
                </label>
            </div>
            
            <div class="form-group">
                <label>Tên khách hàng:</label>
                <input type="text" id="customerName" name="customerName" value="${customer.name}">
            </div>
            
            <div class="form-group">
                <label>Giới tính:</label>
                <select name="sex">
                    <option value="-1">Chọn giới tính</option>
                    <option value="0" ${customer.sex == 0 ? 'selected' : ''}>Nam</option>
                    <option value="1" ${customer.sex == 1 ? 'selected' : ''}>Nữ</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>Ngày sinh:</label>
                <input type="text" id="birthday" name="birthday" value="${customer.birthday}" placeholder="YYYY/MM/DD">
            </div>
            
            <div class="form-group">
                <label>Email:</label>
                <input type="text" id="email" name="email" value="${customer.email}">
            </div>
            
            <div class="form-group">
                <label>Địa chỉ:</label>
                <textarea name="address" rows="3">${customer.address}</textarea>
            </div>
            
            <div class="buttons">
                <button type="submit">Save</button>
                <button type="button" onclick="clearForm()">Clear</button>
            </div>
        </form>
    </div>
</body>
</html> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//////////////////////////////
public boolean isEmailExists(String email, Integer excludeCustomerId) {
    String sql = "SELECT COUNT(*) FROM MSTCUSTOMER WHERE Email = ? AND DeleteYMD IS NULL";
    
    // Nếu đang edit, loại trừ customer hiện tại
    if (excludeCustomerId != null) {
        sql += " AND Id != ?";
    }
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, email);
        if (excludeCustomerId != null) {
            ps.setInt(2, excludeCustomerId);
        }
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi kiểm tra email", e);
    }
    return false;
}\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    
    // Kiểm tra session
    HttpSession session = request.getSession();
    Integer userPsn = (Integer) session.getAttribute("userPsn");
    
    if (userPsn == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    if ("save".equals(action)) {
        try {
            String customerId = request.getParameter("customerId");
            String name = request.getParameter("customerName");
            String sex = request.getParameter("sex");
            String birthday = request.getParameter("birthday");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            
            boolean isEdit = customerId != null && !customerId.isEmpty();
            Integer excludeCustomerId = isEdit ? Integer.parseInt(customerId) : null;
            
            // Kiểm tra email tồn tại
            if (customerDAO.isEmailExists(email, excludeCustomerId)) {
                request.setAttribute("error", "Email đã tồn tại trong hệ thống");
                request.setAttribute("customer", new CustomerDTO(
                    isEdit ? Integer.parseInt(customerId) : null,
                    name, 
                    sex.equals("0") ? 0 : 1,
                    birthday,
                    email,
                    address
                ));
                request.getRequestDispatcher("/WEB-INF/views/customerAdd.jsp").forward(request, response);
                return;
            }
            
            if (isEdit) {
                customerDAO.updateCustomer(
                    Integer.parseInt(customerId), name, sex, birthday, email, address, userPsn);
            } else {
                customerDAO.addCustomer(name, sex, birthday, email, address, userPsn);
            }
            
            // ... phần code redirect giữ nguyên ...
            
        } catch (RuntimeException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customerAdd.jsp").forward(request, response);
        }
    }
}
