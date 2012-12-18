<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="../tiles/links.jsp" %>

<title>Your Businesses</title>

<section class="grid_9 maingrid">
	<div class="page-header">Your Pages</div>
	<p>Manage your pages. Warning: Editing a page domain will reset the number of likes for that page. That's just how Facebook works. Sorry.
	
	<c:choose>
	<c:when test="${fn:length(businesses) gt 0 }">
	
	<c:forEach items="${businesses }" var="business">
	
	<div class="business ui-state-default" businessId="${business.id }" domain="${business.domain }">
		<div class="business-img-container inline-block">
			<img class="business-img" src="${business.profilepic.smallSquare }" />
		</div>
		<div class="business-info-container inline-block">
			<div class="business-title">
				<a href="${urlProfile}${business.domain}">
					${business.fullName }
					<c:if test="${business.deleted }">
						<span class="redtext bold">(Deleted)</span>
					</c:if>
				</a>
			</div>
			<div class="business-description">${business.description }</div>
		</div>
		<div class="details hide">
			<ul>
				<li>Domain: ${business.domain }</li>
				<li>URL: <a href="http://indgte.com/${business.domain }">http://indgte.com/${business.domain }</a></li>
				<li>Group: <span class="capitalize">${business.category.name }</span></li>
				<li>Subscribers: <span class="business-subscribers">Querying Indgte...</span></li>
				<li>Likes: <span class="business-likes">Querying Facebook...</span></li>
				<li>Product likes: </li>
			</ul>
		</div>
		<div class="controls">
			<c:if test="${!business.deleted }">
			<button class="delete-business hide">Delete ${business.fullName }</button>
			<button class="edit-business hide">Edit</button>
			<button class="show-details hide">Show details</button>
			</c:if>
		</div>
	</div>
	
	</c:forEach>
	
	</c:when>
	<c:otherwise>
	<div class="grid_8">
		<spring:message code="businesses.nobusiness" />
	</div>
	</c:otherwise>
	</c:choose>
</section>

<div class="dialog-delete"></div>

<div class="grid_3 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Menu</div>
		<a class="btn-register button" href="${urlRegister }"><spring:message code="businesses.register" /></a>
	</div>
</div>

<div class="grid_3 sidebar-section">
	<div class="sidebar-container">
		<div class="sidebar-section-header">Help</div>
	</div>
</div>

<style>
.business {
	padding: 10px;
	margin-bottom: 10px;
	overflow: hidden;
	text-overflow: ellipsis;
}

.business-img-container {
	vertical-align: top;
}

.business-info-container {
	max-width: 580px;
}

.business-description {
	overflow: hidden;
	text-overflow: ellipsis;
	font-weight: normal;
}

.details {
	position: relative;
}

.controls {
	text-align: right;
	height: 20px;
	margin: 5px 0;
}

.btn-register {
	width: 100%;
}
</style>

<script>
window.urls = {
	profile: 'http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}' + '<spring:url value="/p/" />',
	edit : '<spring:url value="/r/edit/" />',
	countSubscribers : '<spring:url value="/i/countsubs/business/" />',
	deleteBusiness : '<spring:url value="/b/deletebusiness/" />'
}

$(function(){
	var $business = $('.business');
	
	$business.on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight')
				.find('.controls button').show();
		},
		mouseleave: function(){
			$(this).removeClass('ui-state-highlight')
				.find('.controls button').hide();
		}
	});
	
	$business.on({
		click: function(){
			var domain = $(this).closest('.business').attr('domain');
			window.location.href = urls.edit + domain;
		}
	}, '.edit-business');
	
	$business.on({
		click: function(){
			var $this = $(this);
			var $business = $this.closest('.business');
			var $details = $business.find('.details').show();
			
			var domain = $business.attr('domain');
			var url = urls.profile + domain;
			var id = $business.attr('businessId');

			debug('Querying facebook likes for id ' + url);
			$.ajax({
				type: 'get',
				url: 'http://graph.facebook.com/?ids=' + url,
				dataType: 'json',
				success: function(response) {
					debug('success. response: ' + JSON.stringify(response));
					
					var json = response[url];
					if(json.shares) {
						$details.find('.business-likes').text(json.shares);
					} else {
						$details.find('.business-likes').text('0. Maybe you should advertise!');
					}
				},
				error: function(response){
					debug('error');
					debug(response);
				}
			});
			
			debug('Querying subs count for business id ' + id);
			$.get(urls.countSubscribers + id + '/json', function(response) {
				switch(response.status) {
				case '200':
					$details.find('.business-subscribers').text(response.subscount);
					break;
				default:
					debug('error counting subs');
					debug(response);
				}
			});
			
			if($this.hasClass('.show-details')) $this.remove();
		}
	}, '.show-details,.delete-business');
	
	//delete
	var $delete = $('.dialog-delete');
	
	$business.on({
		click: function(){
			var $this = $(this);
			var $business = $this.closest('.business');
			var $details = $business.find('.details').show();
			
			var name = $business.find('.business-title').text();
			var id = $business.attr('businessId');

			debug('Found business with name ' + name + ' and id ' + id);
			
			$delete
				.attr('title', 'Really delete ' + name + '?')
				.text('Are you sure you want to delete ' + name + '? This cannot be undone.')
				.dialog({
					buttons: {
						'Delete Forever': function(){
							var $dialog = $(this);
							$.post(urls.deleteBusiness + id + '/json', function(response) {
								switch(response.status) {
								case '200':
									$business.fadeOut(function(){
										$business.remove();
									});
									$dialog.dialog('close');
									break;
								default:
									debug('error');
									debug(response);
								}
							});
						},
						
						'Nevermind': function() {
							$(this).dialog('close');
						}
					}
				});
		}
	}, '.delete-business');
});
</script>