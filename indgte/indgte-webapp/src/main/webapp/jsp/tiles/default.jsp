<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	
</div>
</body>

</html>