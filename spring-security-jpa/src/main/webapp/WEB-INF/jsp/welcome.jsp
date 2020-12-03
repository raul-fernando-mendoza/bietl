<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Spring BOOT MVC Example - Home JSP</title>
</head>
<body>
	<b>Hello from welcome ${name}</b>
<form action="/"> 
welcome: <input type="text" name="name" /><br>
<input type="submit" value="Submit Details!!"/> 
</form> 
<c:if test="${not empty user}">  
   user: {user.userName} 
	<c:forEach var="r" items="${roles}">  
	   Item <c:out value="${r}"/><p>
	</c:forEach>     
</c:if>  
<c:if test="${empty user}">
    Please login <a href="<c:url value='/login'/>">login</a>.
</c:if>
<c:if test="${not empty user}">
    <a href="<c:url value='/logout'/>">logout</a>.
</c:if>

</body>
</html>