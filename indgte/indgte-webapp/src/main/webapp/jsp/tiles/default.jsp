<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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