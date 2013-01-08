<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="r" value="${pageContext.request }" />
<c:set var="baseURL" value="http://dgteph.tomcathostingservice.com" />

<spring:url var="urlLogin" value="/login/" />

<spring:url var="logo" value="/resources/images/logo.png" />
<spring:url var="pin" value="/resources/images/icons/pin.png" />
<spring:url var="urlImgur" value="http://imgur.com/" />
<spring:url var="urlImgRoot" value="http://i.imgur.com/" />

<spring:url var="noimage" value="/resources/images/noimage.jpg" />
<spring:url var="noimage50" value="/resources/images/noimage50.png" />
<spring:url var="spinner" value="/resources/images/spinner.gif" />
<spring:url var="paperclip" value="/resources/images/icons/paperclip.png" />

<spring:url var="urlRegister" value="/r/" />

<spring:url var="urlProfile" value="/" />
<spring:url var="urlBusinessProfile" value="/" />
<spring:url var="urlOwnProfile" value="/p/" />
<spring:url var="urlUserProfile" value="/p/user/" />

<spring:url var="urlNewCategory" value="/b/newcategory/" />
<spring:url var="urlCategories" value="/b/categories/" />

<spring:url var="urlProducts" value="/b/products/" />
<spring:url var="urlNewProduct" value="/b/newproduct/" />
<spring:url var="urlEditProduct" value="/b/editproduct/" />

<spring:url var="urlPics" value="/b/pics/" />
<spring:url var="urlEditPics" value="/b/editpics/" />
<spring:url var="urlHidePics" value="/b/hidepics/" />
<spring:url var="urlShowPics" value="/b/showpics/" />
<spring:url var="urlDeletePics" value="/b/deletepics/" />

<spring:url var="urlToptenList" value="/i/toptens/" />
<spring:url var="urlPosts" value="/i/posts/" />
<spring:url var="urlReview" value="/i/review/" />

<spring:url var="urlTrade" value="/t/" />
<spring:url var="urlTag" value="/t/tags/" />

<spring:url var="jsValidator" value="/resources/jquery-plugins/jquery-validator.js" />

<spring:url var="jsEasyPaginate" value="/resources/easypaginate/easypaginate.js" />
<spring:url var="cssEasyPaginate" value="/resources/easypaginate/easypaginate.css" />

<spring:url var="jsAwesomecloud" value="/resources/awesomecloud/jquery.awesomeCloud-0.2.min.js" />
<spring:url var="jsTagcloud" value="/resources/tagcloud/tagcloud.js" />
<spring:url var="jsDgteTagCloud" value="/resources/javascript/grids/tagcloud.js" />
<spring:url var="jsCookie" value="/resources/jquery-plugins/jquery-cookie.js" />

<spring:url var="jsColumnize" value="/resources/javascript/libs/columnize/columnize.js" />

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<spring:url var="jsTopTens" value="/resources/javascript/grids/toptens.js" />
<spring:url var="jsReviewQueue" value="/resources/javascript/grids/reviewqueue.js" />
<spring:url var="jsReviews" value="/resources/javascript/reviews.js" />
<spring:url var="jsAutocomplete" value="/resources/javascript/autocomplete.js" />
<spring:url var="jsWatchedTags" value="/resources/javascript/grids/watchedtags.js" />

<spring:url var="jspChat" value="/resources/javascript/navbar/chat.jsp" />
<spring:url var="jsChat" value="/resources/javascript/navbar/chat.js" />