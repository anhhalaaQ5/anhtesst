  <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts-config PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 1.2//EN"
        "http://struts.apache.org/dtds/struts-config_1_2.dtd">

<struts-config>
    <form-beans>
        <form-bean name="loginForm" type="form.LoginForm"/>
        <form-bean name="customerForm" type="form.CustomerForm"/>
        <form-bean name="customerAddForm" type="form.CustomerAddForm"/>
    </form-beans>

    <action-mappings>
        <action path="/login"
                type="action.LoginAction"
                name="loginForm"
                scope="request"
                input="/login.jsp">
            <forward name="success" path="/search.do" redirect="true"/>
            <forward name="failure" path="/login.jsp"/>
        </action>

        <action path="/search"
                type="action.CustomerAction"
                name="customerForm"
                scope="request">
            <forward name="success" path="/WEB-INF/views/search.jsp"/>
            <forward name="failure" path="/login.jsp"/>
        </action>

        <action path="/add"
                type="action.CustomerAddAction"
                name="customerAddForm"
                scope="request">
            <forward name="add" path="/WEB-INF/views/customerAdd.jsp"/>
            <forward name="edit" path="/WEB-INF/views/customerAdd.jsp"/>
            <forward name="search" path="/search.do" redirect="true"/>
            <forward name="login" path="/login.do" redirect="true"/>
        </action>
    </action-mappings>

    <global-forwards>
        <forward name="welcome" path="/login.do"/>
    </global-forwards>

    <message-resources parameter="resources.ApplicationResources"/>
</struts-config>\\\\\\\\\\\\\\
package form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

public class LoginForm extends ActionForm {
    private int psn;
    private String userid;
    private String pass;
    private String username;
    private String errorMessage;

    public int getPsn() { return psn; }
    public void setPsn(int psn) { this.psn = psn; }
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }
    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        psn = 0;
        userid = "";
        pass = "";
        username = "";
        errorMessage = "";
    }
} \\\\\\\\\\\\\\\\\\\\\\\\
package form;

import org.apache.struts.action.ActionForm;
import dto.CustomerDTO;
import java.util.List;

public class CustomerForm extends ActionForm {
    // Các trường điều kiện tìm kiếm
    private String customerName;
    private String sex;
    private String birthdayFrom;
    private String birthdayTo;
    
    // Các trường xử lý form và hiển thị
    private String[] customerIds;
    private String action;
    private int currentPage = 1;
    
    // Kết quả tìm kiếm
    private List<CustomerDTO> searchResults;
    
    // New field for targetPage
    private String targetPage;
    
    // Constructor
    public CustomerForm() {
        super();
    }
    
    // Getters và Setters cho điều kiện tìm kiếm
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public String getBirthdayFrom() { return birthdayFrom; }
    public void setBirthdayFrom(String birthdayFrom) { this.birthdayFrom = birthdayFrom; }
    
    public String getBirthdayTo() { return birthdayTo; }
    public void setBirthdayTo(String birthdayTo) { this.birthdayTo = birthdayTo; }
    
    // Getters và Setters cho xử lý form
    public String[] getCustomerIds() { return customerIds; }
    public void setCustomerIds(String[] customerIds) { this.customerIds = customerIds; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    
    // Getter và Setter cho kết quả tìm kiếm
    public List<CustomerDTO> getSearchResults() { return searchResults; }
    public void setSearchResults(List<CustomerDTO> searchResults) { this.searchResults = searchResults; }
    
    // New getter and setter for targetPage
    public String getTargetPage() { return targetPage; }
    public void setTargetPage(String targetPage) { this.targetPage = targetPage; }
}\\\\\\\\\\\\\\\\\\\\
package form;

import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

public class CustomerAddForm extends ActionForm {
    private String customerId;
    private String customerName;
    private String sex;
    private String birthday;
    private String email;
    private String address;

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        customerId = null;
        customerName = "";
        sex = "-1";
        birthday = "";
        email = "";
        address = "";
    }
}\\\\\\\\\\\\\\\\\
package factory;

import dao.UserDAO;
import form.LoginForm;
import javax.servlet.http.HttpSession;
import java.util.List;

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

        List<LoginForm> users = userDAO.validateAndGetUser(userid, pass);
        if (!users.isEmpty()) {
            return "";
        }
        return "Userid hoặc password không đúng";
    }
    
    public LoginForm getUserInfo(String userid, String pass) {
        List<LoginForm> users = userDAO.validateAndGetUser(userid, pass);
        return !users.isEmpty() ? users.get(0) : null;
    }
}\\\\\\\\\\\\\\\\\\\\\
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import form.LoginForm;
import db.DBConnection;

public class UserDAO {
    public List<LoginForm> validateAndGetUser(String userid, String pass) {
        List<LoginForm> users = new ArrayList<>();
        
        String countSql = "SELECT COUNT(*) AS cnt FROM MSTUSER WHERE Userid = ? AND Pass = ? AND Delete_ymd IS NULL";
        String userSql = "SELECT Psn, Userid, Pass, Username FROM MSTUSER WHERE Userid = ? AND Delete_ymd IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement countPs = conn.prepareStatement(countSql);
             PreparedStatement userPs = conn.prepareStatement(userSql)) {

            // Thực hiện query đếm số lượng user
            countPs.setString(1, userid);
            countPs.setString(2, pass);
            ResultSet countRs = countPs.executeQuery();

            if (countRs.next() && countRs.getInt("cnt") == 1) {
                // Nếu tìm thấy đúng 1 user, thực hiện query lấy thông tin
                userPs.setString(1, userid);
                ResultSet userRs = userPs.executeQuery();

                if (userRs.next()) {
                    LoginForm user = new LoginForm();
                    user.setPsn(userRs.getInt("Psn"));
                    user.setUserid(userRs.getString("Userid"));
                    user.setPass(userRs.getString("Pass"));
                    user.setUsername(userRs.getString("Username"));
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

        public static void main(String[] args) {

            UserDAO userDAO = new UserDAO();
            List<LoginForm> users = userDAO.validateAndGetUser("user1", "password123");
            for (LoginForm user : users) {
                System.out.println(user.getUsername());
            }
        }

}\\\\\\\\\\\\\\\\\
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
            sql.append(" AND Name LIKE ? ESCAPE '\\'");
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
                ps.setString(paramIndex++, "%" + escapeLikePattern(name.trim()) + "%");
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
            sql.append(" AND Name LIKE ? ESCAPE '\\'");
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
                ps.setString(paramIndex++, "%" + escapeLikePattern(name.trim()) + "%");
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

    public boolean addCustomer(String name, String sex, String birthday, 
                             String email, String address, int userPsn) {
        String sql = "INSERT INTO MSTCUSTOMER (Id, Name, Sex, Birthday, Email, Address, " +
                    "DeleteYMD, Insert_ymd, Insertpsncd, Updateymd, Updatepsncd) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NULL, GETDATE(), ?, GETDATE(), ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy ID mới
            int newId = getNextCustomerId();
            if (newId == -1) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setString(2, name);
                ps.setString(3, sex);
                ps.setString(4, birthday);
                ps.setString(5, email);
                ps.setString(6, address);
                ps.setInt(7, userPsn);
                ps.setInt(8, userPsn);

                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public boolean updateCustomer(int customerId, String name, String sex, 
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

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String escapeLikePattern(String pattern) {
        if (pattern == null) return null;
        return pattern.replace("%", "\\%")
                     .replace("_", "\\_");
    }
}\\\\\\\\\\\\\\\\\\\
package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import factory.LoginFactory;
import form.LoginForm;

public class LoginAction extends Action {
    private final LoginFactory loginFactory = new LoginFactory();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        LoginForm loginForm = (LoginForm) form;
        String action = request.getParameter("action");

        // Xử lý logout
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            loginFactory.processLogout(session);
            return mapping.findForward("failure");
        }

        // Nếu là GET request, hiển thị trang login
        if (request.getMethod().equals("GET")) {
            return mapping.findForward("failure");
        }

        // Xử lý login
        String errorMsg = loginFactory.processLogin(loginForm.getUserid(), loginForm.getPass());
        
        if (!errorMsg.isEmpty()) {
            loginForm.setErrorMessage(errorMsg);
            return mapping.findForward("failure");
        }

        // Login thành công, lấy thông tin user
        LoginForm userInfo = loginFactory.getUserInfo(loginForm.getUserid(), loginForm.getPass());
        if (userInfo != null) {
            loginForm.setPsn(userInfo.getPsn());
            loginForm.setUsername(userInfo.getUsername());
            
            HttpSession session = request.getSession();
            session.setAttribute("user", loginForm);
            return mapping.findForward("success");
        }
        
        return mapping.findForward("failure");
    }
}\\\\\\\\\\\\\\\\\\\\\
package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import form.CustomerForm;
import form.LoginForm;
import dao.CustomerDAO;
import java.util.List;
import dto.CustomerDTO;

public class CustomerAction extends Action {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        LoginForm user = (LoginForm) session.getAttribute("user");
        
        // Kiểm tra session, nếu không có thì redirect về login.do
        if (user == null) {
            response.sendRedirect("login.do");
            return null;
        }

        CustomerForm customerForm = (CustomerForm) form;
        String action = customerForm.getAction();

        // Thêm xử lý cho tham số page từ request
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            customerForm.setCurrentPage(Integer.parseInt(pageParam));
        }

        // Xử lý delete
        if ("delete".equals(action)) {
            String[] customerIds = customerForm.getCustomerIds();
            if (customerIds != null && customerIds.length > 0) {
                try {
                    customerDAO.deleteCustomers(customerIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Kiểm tra xem có phải là submit từ nút Search không
        boolean isSearchAction = request.getParameter("search") != null;
        
        // Xử lý phân trang - Sửa lại phần này
        if (!isSearchAction) {
            // Lấy điều kiện tìm kiếm từ session
            customerForm.setCustomerName((String) session.getAttribute("searchName"));
            customerForm.setSex((String) session.getAttribute("searchSex"));
            customerForm.setBirthdayFrom((String) session.getAttribute("searchBirthdayFrom"));
            customerForm.setBirthdayTo((String) session.getAttribute("searchBirthdayTo"));
        } else {
            // Lưu điều kiện tìm kiếm vào session và reset về trang 1
            session.setAttribute("searchName", customerForm.getCustomerName());
            session.setAttribute("searchSex", customerForm.getSex());
            session.setAttribute("searchBirthdayFrom", customerForm.getBirthdayFrom());
            session.setAttribute("searchBirthdayTo", customerForm.getBirthdayTo());
            customerForm.setCurrentPage(1);
        }

        // Xử lý phân trang
        int totalRecords = customerDAO.getTotalRecords(
            customerForm.getCustomerName(), 
            customerForm.getSex(), 
            customerForm.getBirthdayFrom(), 
            customerForm.getBirthdayTo()
        );
        
        int maxPage = customerDAO.getMaxPage(totalRecords);
        
        // Kiểm tra và điều chỉnh trang hiện tại
        int currentPage = customerForm.getCurrentPage();
        if (currentPage < 1) currentPage = 1;
        if (currentPage > maxPage) currentPage = maxPage;
        customerForm.setCurrentPage(currentPage);
        
        // Lấy danh sách khách hàng theo điều kiện tìm kiếm và phân trang
        List<CustomerDTO> customers = customerDAO.searchCustomers(
            customerForm.getCustomerName(),
            customerForm.getSex(),
            customerForm.getBirthdayFrom(),
            customerForm.getBirthdayTo(),
            currentPage
        );
        customerForm.setSearchResults(customers);

        // Set thông tin vào request/session
        session.setAttribute("maxPage", maxPage);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("customerForm", customerForm);
        
        return mapping.findForward("success");
    }
}\\\\\\\\\\\\\\\\\\\\\\\\\\\\
package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import form.LoginForm;
import form.CustomerAddForm;
import dto.CustomerDTO;
import dao.CustomerDAO;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomerAddAction extends Action {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        LoginForm user = (LoginForm) session.getAttribute("user");
        
        if (user == null) {
            return mapping.findForward("login");
        }

        CustomerAddForm customerForm = (CustomerAddForm) form;
        String action = request.getParameter("action");

        // Xử lý hiển thị form add/edit
        if ("add".equals(action)) {
            customerForm.reset(mapping, request);
            return mapping.findForward("add");
        } 
        else if ("edit".equals(action)) {
            String customerId = request.getParameter("id");
            if (customerId == null || customerId.isEmpty()) {
                return mapping.findForward("search");
            }
            
            CustomerDTO customer = getCustomerById(customerId);
            if (customer != null) {
                customerForm.setCustomerId(String.valueOf(customer.getId()));
                customerForm.setCustomerName(customer.getName());
                customerForm.setSex(String.valueOf(customer.getSex()));
                customerForm.setBirthday(customer.getBirthday());
                customerForm.setEmail(customer.getEmail());
                customerForm.setAddress(customer.getAddress());
                
                request.setAttribute("customer", customer);
                request.setAttribute("isEdit", true);
            }
            return mapping.findForward("edit");
        }
        // Xử lý lưu dữ liệu
        else if ("save".equals(action)) {
            try {
                boolean isEdit = customerForm.getCustomerId() != null && !customerForm.getCustomerId().isEmpty();
                
                if (isEdit) {
                    customerDAO.updateCustomer(
                        Integer.parseInt(customerForm.getCustomerId()),
                        customerForm.getCustomerName(),
                        customerForm.getSex(),
                        customerForm.getBirthday(),
                        customerForm.getEmail(),
                        customerForm.getAddress(),
                        user.getPsn()
                    );
                } else {
                    customerDAO.addCustomer(
                        customerForm.getCustomerName(),
                        customerForm.getSex(),
                        customerForm.getBirthday(),
                        customerForm.getEmail(),
                        customerForm.getAddress(),
                        user.getPsn()
                    );
                }
                
                // Tạo URL redirect với các tham số tìm kiếm
                StringBuilder redirectURL = new StringBuilder("search.do?");
                
                // Lấy các tham số tìm kiếm từ session
                String searchName = (String) session.getAttribute("searchName");
                String searchSex = (String) session.getAttribute("searchSex");
                String searchBirthdayFrom = (String) session.getAttribute("searchBirthdayFrom");
                String searchBirthdayTo = (String) session.getAttribute("searchBirthdayTo");
                String currentPage = isEdit ? 
                    (String) session.getAttribute("currentPage") : "1";

                if (searchName != null && !searchName.isEmpty()) {
                    redirectURL.append("customerName=")
                              .append(URLEncoder.encode(searchName, StandardCharsets.UTF_8))
                              .append("&");
                }
                if (searchSex != null && !searchSex.isEmpty() && !"-1".equals(searchSex)) {
                    redirectURL.append("sex=")
                              .append(URLEncoder.encode(searchSex, StandardCharsets.UTF_8))
                              .append("&");
                }
                if (searchBirthdayFrom != null && !searchBirthdayFrom.isEmpty()) {
                    redirectURL.append("birthdayFrom=")
                              .append(URLEncoder.encode(searchBirthdayFrom, StandardCharsets.UTF_8))
                              .append("&");
                }
                if (searchBirthdayTo != null && !searchBirthdayTo.isEmpty()) {
                    redirectURL.append("birthdayTo=")
                              .append(URLEncoder.encode(searchBirthdayTo, StandardCharsets.UTF_8))
                              .append("&");
                }
                if (currentPage != null && !currentPage.isEmpty()) {
                    redirectURL.append("page=")
                              .append(URLEncoder.encode(currentPage, StandardCharsets.UTF_8));
                }

                response.sendRedirect(redirectURL.toString());
                return null;
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
                return mapping.findForward("add");
            }
        }

        return mapping.findForward("search");
    }

    private CustomerDTO getCustomerById(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerById(Integer.parseInt(customerId));
    }
}\\\\\\\\\\\\\\\\\\\\\\\\<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <style>
        body { text-align: center; font-family: Arial, sans-serif; }
        .container { width: 300px; margin: 100px auto; padding: 20px; border: 1px solid black; border-radius: 5px; }
        input { width: 100%; padding: 8px; margin: 5px 0; }
        .error-label { 
            color: red; 
            margin: 10px 0;
            display: block;
            text-align: left;
            font-size: 14px;
            height: 20px; /* Thêm chiều cao cố định */
        }
        .form-group { margin-bottom: 15px; }
        .buttons { margin-top: 20px; }
        .buttons input { width: auto; margin: 0 5px; padding: 8px 20px; }
        label { display: block; text-align: left; margin-bottom: 5px; }
    </style>
    <script type="text/javascript">
        function clearForm() {
            document.getElementById('userid').value = '';
            document.getElementById('pass').value = '';
            document.getElementById('errorLabel').innerHTML = '&nbsp;';
            return false;
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Đăng nhập</h2>
        <html:form action="/login" method="post">
            <div class="form-group">
                <label for="userid">User ID:</label>
                <html:text property="userid" styleId="userid"/>
            </div>
            
            <div class="form-group">
                <label for="pass">Password:</label>
                <html:password property="pass" styleId="pass"/>
            </div>
            
            <!-- Label error luôn hiển thị -->
            <label id="errorLabel" class="error-label">
                <logic:notEmpty name="loginForm" property="errorMessage">
                    <bean:write name="loginForm" property="errorMessage"/>
                </logic:notEmpty>
                <logic:empty name="loginForm" property="errorMessage">
                    &nbsp;
                </logic:empty>
            </label>
            
            <div class="buttons">
                <html:submit value="Login"/>
                <html:button property="clearButton" onclick="clearForm()" value="Clear"/>
            </div>
        </html:form>
    </div>
</body>
</html>\\\\\\\\\\\\\\
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
        <bean:define id="user" name="user" scope="session"/>
        <label>Chào, <bean:write name="user" property="username"/></label> | 
        <html:link action="/login?action=logout">Đăng xuất</html:link>
    </div>

    <!-- Form tìm kiếm -->
    <html:form action="/search" method="post">
        <div class="search-form">
            <label>Tên khách hàng:</label>
            <html:text property="customerName"/>
            
            <label>Giới tính:</label>
            <html:select property="sex">
                <html:option value="-1">Tất cả</html:option>
                <html:option value="0">Nam</html:option>
                <html:option value="1">Nữ</html:option>
            </html:select>
            
            <label>Ngày sinh từ:</label>
            <html:text property="birthdayFrom" styleId="birthdayFrom"/>
            
            <label>Đến:</label>
            <html:text property="birthdayTo" styleId="birthdayTo"/>
            
            <html:submit property="search" value="Tìm kiếm"/>
        </div>
    </html:form>

    <!-- Form phân trang -->
    <html:form action="/search" method="post">
        <div style="text-align: center; margin-top: 20px;">
            <html:hidden property="currentPage"/>
            <html:hidden property="customerName"/>
            <html:hidden property="sex"/>
            <html:hidden property="birthdayFrom"/>
            <html:hidden property="birthdayTo"/>
            <html:hidden property="targetPage" styleId="targetPage"/>
            <input type="hidden" name="maxPage" value="<bean:write name='maxPage'/>"/>
            
            <!-- Nút First -->
            <logic:equal name="customerForm" property="currentPage" value="1">
                <html:submit property="submitButton" disabled="true">First</html:submit>
            </logic:equal>
            <logic:notEqual name="customerForm" property="currentPage" value="1">
                <html:submit property="submitButton" onclick="return setPage(1)">First</html:submit>
            </logic:notEqual>

            <!-- Nút Previous -->
            <logic:equal name="customerForm" property="currentPage" value="1">
                <html:submit property="submitButton" disabled="true">Previous</html:submit>
            </logic:equal>
            <logic:notEqual name="customerForm" property="currentPage" value="1">
                <html:submit property="submitButton" onclick="return setPage(parseInt(document.forms[1].currentPage.value) - 1)">Previous</html:submit>
            </logic:notEqual>
            
            <span>
                Trang <bean:write name="customerForm" property="currentPage"/> / <bean:write name="maxPage"/>
            </span>
            
            <!-- Nút Next -->
            <logic:equal name="customerForm" property="currentPage" value="maxPage">
                <html:submit property="submitButton" disabled="true">Next</html:submit>
            </logic:equal>
            <logic:notEqual name="customerForm" property="currentPage" value="maxPage">
                <html:submit property="submitButton" onclick="return setPage(parseInt(document.forms[1].currentPage.value) + 1)">Next</html:submit>
            </logic:notEqual>

            <!-- Nút Last -->
            <logic:equal name="customerForm" property="currentPage" value="maxPage">
                <html:submit property="submitButton" disabled="true">Last</html:submit>
            </logic:equal>
            <logic:notEqual name="customerForm" property="currentPage" value="maxPage">
                <html:submit property="submitButton" onclick="return setPage(parseInt(document.getElementsByName('maxPage')[0].value))">Last</html:submit>
            </logic:notEqual>
        </div>
    </html:form>

    <script>
    function setPage(page) {
        // Copy các giá trị tìm kiếm từ form tìm kiếm
        var searchForm = document.forms[0];
        var paginationForm = document.forms[1];
        
        paginationForm.customerName.value = searchForm.customerName.value;
        paginationForm.sex.value = searchForm.sex.value;
        paginationForm.birthdayFrom.value = searchForm.birthdayFrom.value;
        paginationForm.birthdayTo.value = searchForm.birthdayTo.value;
        
        // Set trang mục tiêu
        paginationForm.currentPage.value = page;
        
        return true;
    }
    </script>

    <!-- Form xử lý delete -->
    <html:form action="/search" method="post" styleId="customerForm">
        <html:hidden property="action" value="delete"/>
        <html:hidden property="customerName"/>
        <html:hidden property="sex"/>
        <html:hidden property="birthdayFrom"/>
        <html:hidden property="birthdayTo"/>
        <html:hidden property="currentPage"/>
        
        <h2 style="text-align: center;">Danh sách khách hàng</h2>
        <table>
            <tr>
                <th><input type="checkbox" onclick="toggleAll(this)" id="checkAll"/></th>
                <th>ID</th>
                <th>Tên</th>
                <th>Giới tính</th>
                <th>Ngày sinh</th>
                <th>Địa chỉ</th>
            </tr>
            <logic:present name="customerForm" property="searchResults">
                <logic:iterate id="customer" name="customerForm" property="searchResults">
                    <tr>
                        <td><html:checkbox property="customerIds" value="${customer.id}"/></td>
                        <td>
                            <span class="triangle">▶</span>
                            <html:link action="/add?action=edit" paramId="id" paramName="customer" paramProperty="id">
                                <bean:write name="customer" property="id"/>
                            </html:link>
                        </td>
                        <td><bean:write name="customer" property="name"/></td>
                        <td>
                            <logic:equal name="customer" property="sex" value="0">Nam</logic:equal>
                            <logic:equal name="customer" property="sex" value="1">Nữ</logic:equal>
                        </td>
                        <td>
                            <logic:notEmpty name="customer" property="birthday">
                                <bean:write name="customer" property="birthday"/>
                            </logic:notEmpty>
                            <logic:empty name="customer" property="birthday">
                                Không có thông tin
                            </logic:empty>
                        </td>
                        <td><bean:write name="customer" property="address"/></td>
                    </tr>
                </logic:iterate>
            </logic:present>
            <logic:notPresent name="customerForm" property="searchResults">
                <tr>
                    <td colspan="6" style="text-align: center;">Không có dữ liệu</td>
                </tr>
            </logic:notPresent>
        </table>

        <!-- Các nút chức năng -->
        <div style="text-align: right; margin: 20px 10% 0 0;">
            <html:link action="/add?action=add" styleClass="button">Add New</html:link>
            <html:button property="" onclick="deleteSelected()" styleId="deleteButton" disabled="${empty customerForm.searchResults}">Delete</html:button>
        </div>
    </html:form>
</body>
</html>\\\\\\\\\\\\\\\\\\\\\\\\\
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
    <%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
    <%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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
            .user-info {
                text-align: right;
                margin-bottom: 20px;
                padding-right: 20px;
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
        <div class="user-info">
            <logic:present name="user">
                Chào, <bean:write name="user" property="username"/> | 
                <html:link action="/login?action=logout">Đăng xuất</html:link>
            </logic:present>
        </div>

        <div class="container">
            <h2>
                <logic:present name="isEdit">
                    Chỉnh sửa khách hàng
                </logic:present>
                <logic:notPresent name="isEdit">
                    Thêm mới khách hàng
                </logic:notPresent>
            </h2>
            
            <div id="errorMessage" class="error">
                <html:errors/>
                <logic:present name="error">
                    <div class="error">${error}</div>
                </logic:present>
            </div>
            
            <html:form action="/add" method="post" onsubmit="return validateForm()">
                <html:hidden property="action" value="save"/>
                <html:hidden property="customerId"/>
                
                <div class="form-group">
                    <label>Mã khách hàng:</label>
                    <label style="font-weight: normal;">
                        <logic:present name="customer">
                            <bean:write name="customer" property="id"/>
                        </logic:present>
                        <logic:notPresent name="customer">[Tự động]</logic:notPresent>
                    </label>
                </div>
                
                <div class="form-group">
                    <label>Tên khách hàng:</label>
                    <html:text property="customerName" styleId="customerName"/>
                </div>
                
                <div class="form-group">
                    <label>Giới tính:</label>
                    <html:select property="sex">
                        <html:option value="-1">Chọn giới tính</html:option>
                        <html:option value="0">Nam</html:option>
                        <html:option value="1">Nữ</html:option>
                    </html:select>
                </div>
                
                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <html:text property="birthday" styleId="birthday"/>
                </div>
                
                <div class="form-group">
                    <label>Email:</label>
                    <html:text property="email" styleId="email"/>
                </div>
                
                <div class="form-group">
                    <label>Địa chỉ:</label>
                    <html:textarea property="address" rows="3"/>
                </div>
                
                <div class="buttons">
                    <html:submit value="Lưu"/>
                    <html:button property="" onclick="clearForm()" value="Xóa"/>
                </div>
            </html:form>
        </div>
    </body>
    </html>
