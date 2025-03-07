<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
                             http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- Thêm vào phần đầu của web.xml -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Struts Tag Library Descriptors -->
    <jsp-config>
        <taglib>
            <taglib-uri>http://struts.apache.org/tags-bean</taglib-uri>
            <taglib-location>/WEB-INF/struts-bean.tld</taglib-location>
        </taglib>
        <taglib>
            <taglib-uri>http://struts.apache.org/tags-html</taglib-uri>
            <taglib-location>/WEB-INF/struts-html.tld</taglib-location>
        </taglib>
        <taglib>
            <taglib-uri>http://struts.apache.org/tags-logic</taglib-uri>
            <taglib-location>/WEB-INF/struts-logic.tld</taglib-location>
        </taglib>
    </jsp-config>

    <servlet>
        <servlet-name>action</servlet-name>
        <servlet-class>org.apache.struts.action.ActionServlet</servlet-class>
        <init-param>
            <param-name>config</param-name>
            <param-value>/WEB-INF/struts-config.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>action</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>

    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>
\\\\\\\\\\\\\\\\\\\\\\\\
  <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts-config PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN"
        "http://struts.apache.org/dtds/struts-config_1_3.dtd">

<struts-config>
    <!-- Form Beans Configuration -->
    <form-beans>
        <form-bean name="loginForm" type="form.LoginForm"/>
        <form-bean name="customerForm" type="form.CustomerForm"/>
        <form-bean name="customerAddForm" type="form.CustomerAddForm"/>
    </form-beans>

    <!-- Global Forwards Configuration -->
    <global-forwards>
        <forward name="welcome" path="/login.do"/>
    </global-forwards>

    <!-- Action Mappings Configuration -->
    <action-mappings>
        <action path="/login"
                type="org.springframework.web.struts.DelegatingActionProxy"
                name="loginForm"
                scope="request"
                input="/login.jsp">
            <forward name="success" path="/search.do"/>
            <forward name="failure" path="/login.jsp"/>
        </action>

        <action path="/search"
                type="org.springframework.web.struts.DelegatingActionProxy"
                name="customerForm"
                scope="request">
            <forward name="success" path="/WEB-INF/views/search.jsp"/>
            <forward name="failure" path="/login.jsp"/>
        </action>

        <action path="/add"
                type="org.springframework.web.struts.DelegatingActionProxy"
                name="customerAddForm"
                scope="request">
            <forward name="add" path="/WEB-INF/views/customerAdd.jsp"/>
            <forward name="edit" path="/WEB-INF/views/customerAdd.jsp"/>
            <forward name="search" path="/search.do"/>
            <forward name="login" path="/login.do"/>
        </action>
    </action-mappings>

    <!-- Message Resources Configuration -->
    <message-resources parameter="resources.ApplicationResources"/>

    <!-- Plugins Configuration -->
    <plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
        <set-property property="contextConfigLocation"
                     value="/WEB-INF/applicationContext.xml"/>
    </plug-in>

</struts-config>\\\\\\\\\\\\\\\\\\\\\\\\\\
  <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

    <!-- Data Source Configuration -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
        <property name="url" value="jdbc:sqlserver://MOI:1433;databaseName=CustomerSyste;encrypt=false;trustServerCertificate=true"/>
        <property name="username" value="sa"/>
        <property name="password" value="abcabc"/>
    </bean>

    <!-- Hibernate SessionFactory Configuration -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mappingResources">
            <list>
                <value>User.hbm.xml</value>
                <value>Customer.hbm.xml</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.SQLServerDialect</prop>
                <prop key="hibernate.show_sql">true</prop>
            </props>
        </property>
    </bean>

    <!-- DAO Beans -->
    <bean id="userDAO" class="dao.UserDAO">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <bean id="customerDAO" class="dao.CustomerDAO">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <!-- Service/Factory Beans -->
    <bean id="loginFactory" class="factory.LoginFactory">
        <property name="userDAO" ref="userDAO"/>
    </bean>

    <!-- Action Beans - Tên bean phải khớp với path trong struts-config.xml -->
    <bean name="/login" class="action.LoginAction">
        <property name="loginFactory" ref="loginFactory"/>
    </bean>

    <bean name="/search" class="action.CustomerAction">
        <property name="customerDAO" ref="customerDAO"/>
    </bean>

    <!-- Thêm bean cho CustomerAddAction -->
    <bean name="/add" class="action.CustomerAddAction">
        <property name="customerDAO" ref="customerDAO"/>
    </bean>
</beans> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
    <class name="dto.UserDTO" table="MSTUSER">
        <id name="psn" type="int">
            <column name="Psn"/>
            <generator class="assigned"/>
        </id>
        <property name="userid" type="string">
            <column name="Userid" length="50" not-null="true" unique="true"/>
        </property>
        <property name="pass" type="string">
            <column name="Pass" length="255" not-null="true"/>
        </property>
        <property name="username" type="string">
            <column name="Username" length="100" not-null="true"/>
        </property>
        <property name="deleteYmd" type="date">
            <column name="Delete_ymd"/>
        </property>
        <property name="insertYmd" type="date">
            <column name="Insert_ymd"/>
        </property>
        <property name="insertPsncd" type="int">
            <column name="Insertpsncd" not-null="true"/>
        </property>
        <property name="updateYmd" type="date">
            <column name="Updateymd"/>
        </property>
        <property name="updatePsncd" type="int">
            <column name="Updatepsncd" not-null="true"/>
        </property>
    </class>
</hibernate-mapping> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  <?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
    <class name="dto.CustomerDTO" table="MSTCUSTOMER">
        <id name="id" type="int">
            <column name="Id"/>
            <generator class="assigned"/>
        </id>
        <property name="name" type="string">
            <column name="Name" length="100" not-null="true"/>
        </property>
        <property name="sex" type="int">
            <column name="Sex" length="1"/>
        </property>
        <property name="birthday" type="string">
            <column name="Birthday" length="10" not-null="true"/>
        </property>
        <property name="email" type="string">
            <column name="Email" length="255" not-null="true"/>
        </property>
        <property name="address" type="string">
            <column name="Address" length="255"/>
        </property>
        <property name="deleteYmd" type="date">
            <column name="DeleteYMD"/>
        </property>
        <property name="insertYmd" type="date">
            <column name="Insert_ymd"/>
        </property>
        <property name="insertPsncd" type="int">
            <column name="Insertpsncd" not-null="true"/>
        </property>
        <property name="updateYmd" type="date">
            <column name="Updateymd"/>
        </property>
        <property name="updatePsncd" type="int">
            <column name="Updatepsncd" not-null="true"/>
        </property>
    </class>
</hibernate-mapping> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
} \\\\\\\\\\\\\\\\\\
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
    private int maxPage = 1;
    
    // Kết quả tìm kiếm
    private List<CustomerDTO> searchResults;
    
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
    
    // Thêm getter/setter cho maxPage
    public int getMaxPage() { return maxPage; }
    public void setMaxPage(int maxPage) { this.maxPage = maxPage; }
}\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
}\\\\\\\\\\\\\\\\\\\\\\\\\\\
package factory;

import java.util.List;
import dao.UserDAO;
import dto.UserDTO;
import form.LoginForm;
import javax.servlet.http.HttpSession;

public class LoginFactory {
    private UserDAO userDAO;
    
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
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

        List<UserDTO> users = userDAO.validateAndGetUser(userid, pass);
        return !users.isEmpty() ? "" : "Userid hoặc password không đúng";
    }
    
    public LoginForm getUserInfo(String userid, String pass) {
        List<UserDTO> users = userDAO.validateAndGetUser(userid, pass);
        if (!users.isEmpty()) {
            UserDTO user = users.get(0);
            LoginForm loginForm = new LoginForm();
            loginForm.setPsn(user.getPsn());
            loginForm.setUserid(user.getUserid());
            loginForm.setUsername(user.getUsername());
            return loginForm;
        }
        return null;
    }
} \\\\\\\\\\\\\\\\\\\\\
package dto;

import java.util.Date;

public class UserDTO {
    private int psn;
    private String userid;
    private String pass;
    private String username;
    private Date deleteYmd;
    private Date insertYmd;
    private int insertPsncd;
    private Date updateYmd;
    private int updatePsncd;

    // Constructor mặc định
    public UserDTO() {
    }

    // Constructor đầy đủ tham số
    public UserDTO(int psn, String userid, String pass, String username, 
                  Date deleteYmd, Date insertYmd, int insertPsncd, 
                  Date updateYmd, int updatePsncd) {
        this.psn = psn;
        this.userid = userid;
        this.pass = pass;
        this.username = username;
        this.deleteYmd = deleteYmd;
        this.insertYmd = insertYmd;
        this.insertPsncd = insertPsncd;
        this.updateYmd = updateYmd;
        this.updatePsncd = updatePsncd;
    }

    // Constructor cho select new UserDTO()
    public UserDTO(int psn, String userid, String pass, String username) {
        this.psn = psn;
        this.userid = userid;
        this.pass = pass;
        this.username = username;
    }

    // Getters và Setters
    public int getPsn() {
        return psn;
    }

    public void setPsn(int psn) {
        this.psn = psn;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDeleteYmd() {
        return deleteYmd;
    }

    public void setDeleteYmd(Date deleteYmd) {
        this.deleteYmd = deleteYmd;
    }

    public Date getInsertYmd() {
        return insertYmd;
    }

    public void setInsertYmd(Date insertYmd) {
        this.insertYmd = insertYmd;
    }

    public int getInsertPsncd() {
        return insertPsncd;
    }

    public void setInsertPsncd(int insertPsncd) {
        this.insertPsncd = insertPsncd;
    }

    public Date getUpdateYmd() {
        return updateYmd;
    }

    public void setUpdateYmd(Date updateYmd) {
        this.updateYmd = updateYmd;
    }

    public int getUpdatePsncd() {
        return updatePsncd;
    }

    public void setUpdatePsncd(int updatePsncd) {
        this.updatePsncd = updatePsncd;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "psn=" + psn +
                ", userid='" + userid + '\'' +
                ", pass='" + pass + '\'' +
                ", username='" + username + '\'' +
                ", deleteYmd=" + deleteYmd +
                ", insertYmd=" + insertYmd +
                ", insertPsncd=" + insertPsncd +
                ", updateYmd=" + updateYmd +
                ", updatePsncd=" + updatePsncd +
                '}';
    }
}\\\\\\\\\\\\\\\\\\\\\\\\\\\
package dto;

import java.util.Date;

public class CustomerDTO {
    private int id;
    private String name;
    private int sex;
    private String birthday;
    private String email;
    private String address;
    private Date deleteYmd;
    private Date insertYmd;
    private int insertPsncd;
    private Date updateYmd;
    private int updatePsncd;

    // Constructor không tham số cho Hibernate
    public CustomerDTO() {}

    // Constructor đầy đủ
    public CustomerDTO(int id, String name, int sex, String birthday, 
                      String email, String address) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSex() { return sex; }
    public void setSex(int sex) { this.sex = sex; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDeleteYmd() { return deleteYmd; }
    public void setDeleteYmd(Date deleteYmd) { this.deleteYmd = deleteYmd; }

    public Date getInsertYmd() { return insertYmd; }
    public void setInsertYmd(Date insertYmd) { this.insertYmd = insertYmd; }

    public int getInsertPsncd() { return insertPsncd; }
    public void setInsertPsncd(int insertPsncd) { this.insertPsncd = insertPsncd; }

    public Date getUpdateYmd() { return updateYmd; }
    public void setUpdateYmd(Date updateYmd) { this.updateYmd = updateYmd; }

    public int getUpdatePsncd() { return updatePsncd; }
    public void setUpdatePsncd(int updatePsncd) { this.updatePsncd = updatePsncd; }
}\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
package dao;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import dto.UserDTO;

public class UserDAO extends HibernateDaoSupport {
    
    @SuppressWarnings("unchecked")
    public List<UserDTO> validateAndGetUser(String userid, String pass) {
        Session session = getSession();
        try {
            // Query đếm số lượng user
            String countQuery = "select count(*) from UserDTO " + 
                              "where userid = :userid " + 
                              "and pass = :pass " + 
                              "and deleteYmd is null";
                              
            Long count = (Long) session.createQuery(countQuery)
                              .setString("userid", userid)
                              .setString("pass", pass)
                              .uniqueResult();
            
            if (count != 1) return new ArrayList<UserDTO>();
            
            // Query lấy thông tin user
            String selectQuery = "select new UserDTO(u.psn, u.userid, u.pass, u.username) " + 
                               "from UserDTO u " + 
                               "where u.userid = :userid " + 
                               "and u.pass = :pass " + 
                               "and u.deleteYmd is null";
                               
            return session.createQuery(selectQuery)
                         .setString("userid", userid)
                         .setString("pass", pass)
                         .list();
                
        } catch (Exception e) {
            // Xử lý lỗi ở đây
            e.printStackTrace();
            return new ArrayList<UserDTO>();
        } finally {
            // Luôn đóng session, dù có lỗi hay không
            releaseSession(session);
        }
    }
}
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
package dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import dto.CustomerDTO;
import java.util.Date;

public class CustomerDAO extends HibernateDaoSupport {
    private static final int RECORDS_PER_PAGE = 5;

    public int getTotalRecords(String name, String sex, String birthdayFrom, String birthdayTo) {
        Session session = getSession();
        try {
            Criteria criteria = session.createCriteria(CustomerDTO.class);
            criteria.add(Restrictions.isNull("deleteYmd"));

            if (name != null && !name.trim().isEmpty()) {
                String escapedName = name.trim()
                    .replace("%", "\\%")
                    .replace("_", "\\_");
                criteria.add(Restrictions.like("name", "%" + escapedName + "%"));
            }
            if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex.trim())) {
                criteria.add(Restrictions.eq("sex", Integer.parseInt(sex.trim())));
            }
            if (birthdayFrom != null && !birthdayFrom.trim().isEmpty()) {
                criteria.add(Restrictions.ge("birthday", birthdayFrom.trim()));
            }
            if (birthdayTo != null && !birthdayTo.trim().isEmpty()) {
                criteria.add(Restrictions.le("birthday", birthdayTo.trim()));
            }

            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();
            return count.intValue();
        } finally {
            releaseSession(session);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CustomerDTO> searchCustomers(String name, String sex, 
            String birthdayFrom, String birthdayTo, int page) {
        Session session = getSession();
        try {
            Criteria criteria = session.createCriteria(CustomerDTO.class);
            criteria.add(Restrictions.isNull("deleteYmd"));

            if (name != null && !name.trim().isEmpty()) {
                String escapedName = name.trim()
                    .replace("%", "\\%")
                    .replace("_", "\\_");
                criteria.add(Restrictions.like("name", "%" + escapedName + "%"));
            }
            
            if (sex != null && !sex.trim().isEmpty() && !"-1".equals(sex)) {
                criteria.add(Restrictions.eq("sex", Integer.parseInt(sex)));
            }
            if (birthdayFrom != null && !birthdayFrom.isEmpty()) {
                criteria.add(Restrictions.ge("birthday", birthdayFrom));
            }
            if (birthdayTo != null && !birthdayTo.isEmpty()) {
                criteria.add(Restrictions.le("birthday", birthdayTo));
            }

            criteria.setFirstResult((page - 1) * RECORDS_PER_PAGE);
            criteria.setMaxResults(RECORDS_PER_PAGE);

            return criteria.list();
        } finally {
            releaseSession(session);
        }
    }

    public int getMaxPage(int totalRecords) {
        if (totalRecords == 0) return 1;
        return (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
    }

    public boolean deleteCustomers(String[] customerIds) {
        Session session = getSession();
        try {
            session.beginTransaction();
            
            Criteria criteria = session.createCriteria(CustomerDTO.class);
            Integer[] ids = new Integer[customerIds.length];
            for (int i = 0; i < customerIds.length; i++) {
                ids[i] = Integer.parseInt(customerIds[i]);
            }
            
            criteria.add(Restrictions.in("id", ids));
            List<CustomerDTO> customers = criteria.list();
            
            Date now = new Date();
            for (CustomerDTO customer : customers) {
                customer.setDeleteYmd(now);
                session.update(customer);
            }
            
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            releaseSession(session);
        }
    }

    public int getNextCustomerId() {
        Session session = getSession();
        try {
            Criteria criteria = session.createCriteria(CustomerDTO.class)
                .setProjection(Projections.max("id"));
            Integer maxId = (Integer) criteria.uniqueResult();
            return maxId == null ? 1 : maxId + 1;
        } finally {
            releaseSession(session);
        }
    }

    public boolean addCustomer(String name, String sex, String birthday, 
                             String email, String address, int userPsn) {
        Session session = getSession();
        try {
            session.beginTransaction();
            
            CustomerDTO customer = new CustomerDTO();
            customer.setId(getNextCustomerId());
            customer.setName(name);
            customer.setSex(Integer.parseInt(sex));
            customer.setBirthday(birthday);
            customer.setEmail(email);
            customer.setAddress(address);
            
            Date now = new Date();
            customer.setInsertYmd(now);
            customer.setUpdateYmd(now);
            customer.setInsertPsncd(userPsn);
            customer.setUpdatePsncd(userPsn);
            
            session.save(customer);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            releaseSession(session);
        }
    }

    public CustomerDTO getCustomerById(int customerId) {
        Session session = getSession();
        try {
            return (CustomerDTO) session.createCriteria(CustomerDTO.class)
                .add(Restrictions.eq("id", customerId))
                .add(Restrictions.isNull("deleteYmd"))
                .uniqueResult();
        } finally {
            releaseSession(session);
        }
    }

    public boolean updateCustomer(int customerId, String name, String sex, 
                                String birthday, String email, String address, int userPsn) {
        Session session = getSession();
        try {
            session.beginTransaction();
            
            CustomerDTO customer = (CustomerDTO) session.get(CustomerDTO.class, customerId);
            if (customer == null) return false;
            
            customer.setName(name);
            customer.setSex(Integer.parseInt(sex));
            customer.setBirthday(birthday);
            customer.setEmail(email);
            customer.setAddress(address);
            customer.setUpdateYmd(new Date());
            customer.setUpdatePsncd(userPsn);
            
            session.update(customer);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            releaseSession(session);
        }
    }
}
\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
    private LoginFactory loginFactory;

    public void setLoginFactory(LoginFactory loginFactory) {
        this.loginFactory = loginFactory;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        LoginForm loginForm = (LoginForm) form;
        String action = request.getParameter("action");

        // Xử lý logout
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            loginFactory.processLogout(session);
            response.sendRedirect("login.do");
            return null;
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
}\\\\\\\\\\\\\\\\\\\
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
    private CustomerDAO customerDAO;

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        LoginForm user = (LoginForm) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.do");
            return null;
        }

        CustomerForm customerForm = (CustomerForm) form;
        String action = customerForm.getAction();

        // Xử lý action search
        if ("search".equals(action)) {
            // Lưu điều kiện tìm kiếm vào session
            session.setAttribute("searchName", customerForm.getCustomerName());
            session.setAttribute("searchSex", customerForm.getSex());
            session.setAttribute("searchBirthdayFrom", customerForm.getBirthdayFrom());
            session.setAttribute("searchBirthdayTo", customerForm.getBirthdayTo());
            customerForm.setCurrentPage(1); // Reset về trang 1 khi tìm kiếm mới
        } 
        // Xử lý action pagination
        else if ("pagination".equals(action)) {
            // Lấy điều kiện tìm kiếm từ session
            customerForm.setCustomerName((String)session.getAttribute("searchName"));
            customerForm.setSex((String)session.getAttribute("searchSex"));
            customerForm.setBirthdayFrom((String)session.getAttribute("searchBirthdayFrom"));
            customerForm.setBirthdayTo((String)session.getAttribute("searchBirthdayTo"));
        }
        // Các action khác
        else {
            // Lấy điều kiện tìm kiếm từ session như cũ
            customerForm.setCustomerName((String)session.getAttribute("searchName"));
            customerForm.setSex((String)session.getAttribute("searchSex"));
            customerForm.setBirthdayFrom((String)session.getAttribute("searchBirthdayFrom"));
            customerForm.setBirthdayTo((String)session.getAttribute("searchBirthdayTo"));
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

        // Xử lý phân trang
        Boolean returnFromEdit = (Boolean) session.getAttribute("returnFromEdit");
        if (returnFromEdit != null && returnFromEdit) {
            // Nếu quay về từ edit, lấy trang từ session
            Integer savedPage = (Integer) session.getAttribute("currentPage");
            if (savedPage != null) {
                customerForm.setCurrentPage(savedPage);
            }
            // Xóa flag sau khi sử dụng
            session.removeAttribute("returnFromEdit");
        } else {
            // Xử lý page từ form submit như bình thường
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    int page = Integer.parseInt(pageParam);
                    customerForm.setCurrentPage(page);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Lưu trang hiện tại vào session
        session.setAttribute("currentPage", customerForm.getCurrentPage());

        // Xử lý phân trang và lấy dữ liệu
        int totalRecords = customerDAO.getTotalRecords(
            customerForm.getCustomerName(), 
            customerForm.getSex(), 
            customerForm.getBirthdayFrom(), 
            customerForm.getBirthdayTo()
        );
        
        int maxPage = customerDAO.getMaxPage(totalRecords);
        customerForm.setMaxPage(maxPage); // Set maxPage vào form
        
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

        return mapping.findForward("success");
    }
}\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
    private CustomerDAO customerDAO;

    // Thêm setter cho Spring DI
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

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
                    
                    // Khi edit, lấy trang hiện tại từ session và redirect về trang đó
                    Integer currentPage = (Integer) session.getAttribute("currentPage");
                    if (currentPage != null) {
                        // Lưu action vào session để CustomerAction biết không phải reset về trang 1
                        session.setAttribute("returnFromEdit", true);
                        response.sendRedirect("search.do");
                    } else {
                        response.sendRedirect("search.do");
                    }
                } else {
                    // Khi thêm mới
                    customerDAO.addCustomer(
                        customerForm.getCustomerName(),
                        customerForm.getSex(),
                        customerForm.getBirthday(),
                        customerForm.getEmail(),
                        customerForm.getAddress(),
                        user.getPsn()
                    );
                    // Khi add new, chỉ quay về search không có tham số
                    response.sendRedirect("search.do");
                }
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
} 
