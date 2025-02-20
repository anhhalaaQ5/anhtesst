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
            "SELECT Id, Name, Sex, Birthday, Address FROM MSTCUSTOMER WHERE DeleteYMD IS NULL");
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

    public boolean deleteCustomers(String[] customerIds) {
        // Tạo câu SQL base
        StringBuilder sql = new StringBuilder("UPDATE MSTCUSTOMER SET DeleteYMD = GETDATE() WHERE Id IN (");
        
        // Tạo chuỗi tham số (?,?,?) tương ứng với số lượng ID
        for (int i = 0; i < customerIds.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Set giá trị cho từng tham số
            for (int i = 0; i < customerIds.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(customerIds[i]));
            }
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        // Khởi tạo đối tượng CustomerDAO
        CustomerDAO customerDAO = new CustomerDAO();
        
        boolean deleteResult = customerDAO.deleteCustomers(new String[]{"1"});
        System.out.println("Kết quả xóa: " + (deleteResult ? "Thành công" : "Thất bại"));
        
    }
    
}


\\\\\\\\\\\\\\\\\\\\\\\\\\\

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

@WebServlet("/search.jsp")
public class CustomerServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("customerName");
        String sex = request.getParameter("sex");
        String birthdayFrom = request.getParameter("birthdayFrom");
        String birthdayTo = request.getParameter("birthdayTo");
        
        // Xử lý phân trang
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

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
            if (customerIds != null && customerIds.length > 0) {
                boolean success = customerDAO.deleteCustomers(customerIds);
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
        }
        
        // Nếu không thành công hoặc không có action, chuyển về trang tìm kiếm
        doGet(request, response);
    }
}

\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

package dto;

public class CustomerDTO {
    private int id;
    private String name;
    private int sex;
    private String birthday;
    private String address;

    public CustomerDTO(int id, String name, int sex, String birthday, String address) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}


\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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
            padding: 5px 10px;
            margin: 0 5px;
            cursor: pointer;
        }
        
        button:disabled {
            cursor: not-allowed;
            opacity: 0.6;
        }
        
        span {
            display: inline-block;
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
            updateDeleteButton();
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
                            <td>${customer.id}</td>
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

        <!-- Thêm nút Delete -->
        <div style="text-align: right; margin: 20px 10% 0 0;">
            <button type="button" onclick="deleteSelected()" id="deleteButton" disabled>Delete</button>
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
        // Hàm kiểm tra và cập nhật trạng thái nút Delete
        function updateDeleteButton() {
            var checkboxes = document.getElementsByName('customerIds');
            var deleteButton = document.getElementById('deleteButton');
            var selected = false;
            
            for(var i = 0; i < checkboxes.length; i++) {
                if(checkboxes[i].checked) {
                    selected = true;
                    break;
                }
            }
            
            deleteButton.disabled = !selected;
        }

        // Cập nhật khi click vào checkbox riêng lẻ
        var checkboxes = document.getElementsByName('customerIds');
        for(var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].onclick = updateDeleteButton;
        }

        function deleteSelected() {
            if(confirm('Bạn có chắc chắn muốn xóa các khách hàng đã chọn?')) {
                document.getElementById('customerForm').submit();
            }
        }
    </script>
</body>
</html>
