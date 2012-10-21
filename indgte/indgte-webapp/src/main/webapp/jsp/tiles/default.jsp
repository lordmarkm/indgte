<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="icon" href="<c:url value='/resources/images/favicon.ico?v=4' />" type="image/x-icon" />
</head>

<body>
<div class="container_12">
<tiles:insertAttribute name="header" />

<div id="navbar">
<tiles:insertAttribute name="navbar" />
</div>

<div id="body">
<tiles:insertAttribute name="body" />
</div>

<div id="footer">
<tiles:insertAttribute name="footer" />
</div>	
</div>
</body>

</html>