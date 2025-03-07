<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>Thuctap</groupId>
	<artifactId>Thuctap</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>

	<dependencies>
		<!-- Struts 1.2 dependencies -->
		<dependency>
			<groupId>org.apache.struts</groupId>
			<artifactId>struts-core</artifactId>
			<version>1.3.10</version>
		</dependency>
		<dependency>
			<groupId>org.apache.struts</groupId>
			<artifactId>struts-taglib</artifactId>
			<version>1.3.10</version>
		</dependency>
		<dependency>
			<groupId>org.apache.struts</groupId>
			<artifactId>struts-extras</artifactId>
			<version>1.3.10</version>
		</dependency>
		
		<!-- Spring Framework -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>2.5.6</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>2.5.6</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
			<version>2.5.6</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jdbc</artifactId>
			<version>2.5.6</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>2.5.6</version>
		</dependency>

		<!-- Hibernate -->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>3.6.10.Final</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-entitymanager</artifactId>
			<version>3.6.10.Final</version>
		</dependency>
		
		<!-- ANTLR Runtime -->
		<dependency>
			<groupId>antlr</groupId>
			<artifactId>antlr</artifactId>
			<version>2.7.7</version>
		</dependency>


		<!-- Servlet & JSP -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
			<version>1.2</version>
		</dependency>
		<dependency>
			<groupId>javax.transaction</groupId>
			<artifactId>jta</artifactId>
			<version>1.1</version>
		</dependency>

		<!-- Javassist -->
		<dependency>
			<groupId>org.javassist</groupId>
			<artifactId>javassist</artifactId>
			<version>3.18.1-GA</version>
		</dependency>

		<!-- Spring Struts Integration -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-struts</artifactId>
			<version>2.0.8</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.13.0</version>
				<configuration>
					<release>21</release>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-war-plugin</artifactId>
				<version>3.2.3</version>
			</plugin>
		</plugins>
	</build>
</project>
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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

    <!-- Service/Factory Beans -->
    <bean id="loginFactory" class="factory.LoginFactory">
        <property name="userDAO" ref="userDAO"/>
    </bean>

    <!-- Action Beans - Tên bean phải khớp với path trong struts-config.xml -->
    <bean name="/login" class="action.LoginAction">
        <property name="loginFactory" ref="loginFactory"/>
    </bean>
</beans> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts-config PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 1.3//EN"
        "http://struts.apache.org/dtds/struts-config_1_3.dtd">

<struts-config>
    <form-beans>
        <form-bean name="loginForm" type="form.LoginForm"/>
        <form-bean name="customerForm" type="form.CustomerForm"/>
        <form-bean name="customerAddForm" type="form.CustomerAddForm"/>
    </form-beans>

    <action-mappings>
        <action path="/login"
                type="org.springframework.web.struts.DelegatingActionProxy"
                name="loginForm"
                scope="request"
                input="/login.jsp">
            <forward name="success" path="/search.do"/>

            <forward name="failure" path="/login.jsp"/>
        </action>\

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
            <forward name="search" path="/search.do"/>
            <forward name="login" path="/login.do"/>
        </action>
    </action-mappings>

    <global-forwards>
        <forward name="welcome" path="/login.do"/>
    </global-forwards>

    <message-resources parameter="resources.ApplicationResources"/>

    <!-- Spring Plugin Configuration -->
    <plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
        <set-property property="contextConfigLocation"
                     value="/WEB-INF/applicationContext.xml"/>
    </plug-in>
</struts-config>\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\<?xml version="1.0" encoding="UTF-8"?>
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
</hibernate-mapping> \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
}\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
package dao;

import java.util.List;
import java.util.ArrayList;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import dto.UserDTO;

public class UserDAO extends HibernateDaoSupport {
    
    @SuppressWarnings("unchecked")
    public List<UserDTO> validateAndGetUser(String userid, String pass) {
        // Đếm số lượng user thỏa mãn điều kiện
        Long count = (Long) getHibernateTemplate().find(
            "select count(*) from UserDTO where userid = ? and pass = ? and deleteYmd is null",
            new Object[]{userid, pass}
        ).get(0);
        
        // Nếu count != 1, trả về list rỗng
        if (count != 1) {
            return new ArrayList<UserDTO>();
        }
        
        // Nếu count == 1, lấy thông tin user với các trường chỉ định
        return getHibernateTemplate().find(
            "select new UserDTO(u.psn, u.userid, u.pass, u.username) " +
            "from UserDTO u where u.userid = ? and u.pass = ? and u.deleteYmd is null",
            new Object[]{userid, pass}
        );
    }
}
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
} \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
}\\\\\\\\\\\\\\\\\\\\\\\\
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
</html>
