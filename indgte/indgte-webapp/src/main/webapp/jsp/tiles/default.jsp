<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>

<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/vader/jquery-ui.css" type="text/css" media="all" />
<link rel="stylesheet" href="//cachedcommons.org/cache/960/0.0.0/stylesheets/960-min.css" type="text/css" media="all" />
<link rel="stylesheet" href="<c:url value='/resources/css/application.css' />" />

</head>
<body>
<div class="container_12">
<tiles:insertAttribute name="header" />
<tiles:insertAttribute name="navbar" />
	<div id="body">
	<tiles:insertAttribute name="body" />
	</div>
</div>
</body>
</html>