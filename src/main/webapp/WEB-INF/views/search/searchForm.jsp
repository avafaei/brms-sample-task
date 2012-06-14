<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Search Form</title>
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/print.css" />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
</head>	
<body>
<div class="container">
	<h1>
		Search Form
	</h1>
	<div class="span-12 last">	
		<form:form modelAttribute="search" action="search" method="post">
		  	<fieldset>		
				<legend>Account Fields</legend>
				<p>
					<form:label	for="firstName" path="firstName" cssErrorClass="error">First Name</form:label><br/>
					<form:input path="firstName" /> <form:errors path="firstName" />			
				</p>
				<p>
					<form:label	for="lastName" path="lastName" cssErrorClass="error">Last Name</form:label><br/>
					<form:input path="lastName" /> <form:errors path="lastName" />			
				</p>
				<p>
					<form:label	for="ssn" path="ssn" cssErrorClass="error">SSN</form:label><br/>
					<form:input path="ssn" /> <form:errors path="ssn" />			
				</p>
			
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>
	</div>
	<hr>	
	
</div>
</body>
</html>