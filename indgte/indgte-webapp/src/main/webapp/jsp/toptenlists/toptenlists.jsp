<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@include file="../tiles/links.jsp" %>

<title>Top Ten Lists In Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/topten/toptenlists.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script type="text/javascript" src="${jsAutocomplete }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/toptens.css' />" />

<script src="${jsEasyPaginate }"></script>
<link rel="stylesheet" href="${cssEasyPaginate }" />

<div class="grid_8 maingrid">

<section class="lists-section recent">
	<div class="section-header">Recent Top Ten Lists</div>
	<c:forEach items="${recentLists }" var="list">
	<div class="list-container toptens">
		<div class="list-title"><a href="${urlToptenList }${list.id}">${list.title }</a></div>
		<ul class="topten-list">
			<c:forEach items="${list.ordered }" var="candidate" varStatus="i">
			<c:if test="${i.index lt 3 }">
			<li class="candidate-container">
				<div class="candidate-rank">${i.index + 1}.</div>
				<div class="candidate-img-container">
					<c:if test="${not empty candidate.attachment && not empty candidate.attachment.imgur }">
					<img class="candidate-img" src="${candidate.attachment.imgur.smallSquare }" />
					</c:if>
					<c:if test="${empty candidate.attachment || empty candidate.attachment.imgur}">
					<img class="candidate-img" src="${noimage50 }" />
					</c:if>
				</div>
				<div class="candidate-info">
					<div class="candidate-title">
						<c:if test="${empty candidate.attachment }">${candidate.title }	</c:if>
						<c:if test="${not empty candidate.attachment }">
						<c:choose>
							<c:when test="${candidate.attachment.attachmentType eq 'business'}">
								<a href="${urlProfile }${candidate.attachment.id}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'imgur' }">
								<a href="${urlImgur }${candidate.attachment.hash}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'product' }">
								<a href="${urlProducts }${candidate.attachment.id}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'category' }">
								<a href="${urlCategories }${candidate.attachment.id}">
							</c:when>
						</c:choose>
						${candidate.attachment.name }
						</a>
						</c:if>
					</div>
					<div class="candidate-votes">${fn:length(candidate.voters) } Votes</div>
				</div>			
			</li>
			</c:if>
			</c:forEach>
		</ul>
		<div class="topten-info">
			${fn:length(list.candidates) } items in list.
			<c:if test="${fn:length(list.candidates) > 3 }">Showing top 3.</c:if>
		</div>
	</div>
	</c:forEach>
</section>

<section class="lists-section popular">
	<div class="section-header">Popular Top Ten Lists</div>
	<c:forEach items="${popularLists }" var="list">
	<div class="list-container toptens">
		<div class="list-title"><a href="${urlToptenList }${list.id}">${list.title }</a></div>
		<ul class="topten-list">
			<c:forEach items="${list.ordered }" var="candidate" varStatus="i">
			<c:if test="${i.index lt 3 }">
			<li class="candidate-container">
				<div class="candidate-rank">${i.index + 1}.</div>
				<div class="candidate-img-container">
					<c:if test="${not empty candidate.attachment && not empty candidate.attachment.imgur }">
					<img class="candidate-img" src="${candidate.attachment.imgur.smallSquare }" />
					</c:if>
					<c:if test="${empty candidate.attachment || empty candidate.attachment.imgur}">
					<img class="candidate-img" src="${noimage50 }" />
					</c:if>
				</div>
				<div class="candidate-info">
					<div class="candidate-title">
						<c:if test="${empty candidate.attachment }">${candidate.title }	</c:if>
						<c:if test="${not empty candidate.attachment }">
						<c:choose>
							<c:when test="${candidate.attachment.attachmentType eq 'business'}">
								<a href="${urlProfile }${candidate.attachment.id}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'imgur' }">
								<a href="${urlImgur }${candidate.attachment.hash}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'product' }">
								<a href="${urlProducts }${candidate.attachment.id}">
							</c:when>
							<c:when test="${candidate.attachment.attachmentType eq 'category' }">
								<a href="${urlCategories }${candidate.attachment.id}">
							</c:when>
						</c:choose>
						${candidate.attachment.name }
						</a>
						</c:if>
					</div>
					<div class="candidate-votes">${fn:length(candidate.voters) } Votes</div>
				</div>			
			</li>
			</c:if>
			</c:forEach>
		</ul>
		<div class="topten-info">
			${fn:length(list.candidates) } items in list.
			<c:if test="${fn:length(list.candidates) > 3 }">Showing top 3.</c:if>
		</div>
	</div>
	</c:forEach>
</section>

<section class="lists-section all">
	<div class="section-header">All the Lists (Alphabetical)</div>
	<ol id="allLists">
		<c:forEach items="${allLists }" var="list" varStatus="index">
		<li class="topten-all-list">
			${index.index + 1 }. <a href="${urlToptenList }${list.id}">${list.title }</a>
			<span class="subtitle"> - Created <span class="timeme">${list.time.time }</span>, <strong>${list.totalVotes  }</strong> total votes</span>
		</li>
		</c:forEach>
	</ol>
</section>

</div>

<div class="newlist-form-container hide" title="Create a new List">
	<form:form id="newtopten-form" method="post" commandName="form">
		<form:label path="title">Title</form:label><br>
		<form:input path="title" />
	</form:form>
	
	<dl class="candidates"></dl>
	
	<button class="btn-new-candidate">Add Candidate</button>
</div>

<div class="topten-autocomplete-results ui-state-highlight" style="display: none;"></div>

<script>
window.urls = {
	newlist : '<spring:url value="/i/toptens.json" />',
	listablegroups : '<spring:url value="/s/listablegroups.json" />',
	
	//grids
	business : '<spring:url value="/p/" />',
	toptensearch : '<spring:url value="/s/toptens/" />',
	topTensPage: '<spring:url value="/i/toptens/" />'
}

window.messages = {
	candidates: 'Candidates',
	save: '<spring:message code="generics.save" />',
	remove: '<spring:message code="generics.remove" />',
	cancel: '<spring:message code="generics.cancel" />'
}

$(function(){
	var $btnNewlist = $('.btn-newlist'),
		$formContainer = $('.newlist-form-container'),
		$newlistform = $('#newtopten-form'),
		$fieldList = $('#newtopten-form dl'),
		$iptNewlistTitle = $('.newlist-title'),
		$btnNewCandidate = $('.btn-new-candidate'),
		$candidates = $('.candidates'),
		$autocompleteResults = $('.topten-autocomplete-results');
	
	$btnNewlist.click(function(){
		$formContainer.dialog({
			buttons: {
				'<spring:message code="generics.save" />' : function() {
					debug('Submitting form...');
					$newlistform.submit();
				},
				'<spring:message code="generics.cancel" />' : function() {
					$formContainer.dialog('close');
				}
			}
		});
	});
	
	$btnNewCandidate.click(function(){
		if($formContainer.find('dt.candidates').length == 0) {
			$('<dt class="candidates">').text(messages.candidates).appendTo($candidates);
		}
		
		var $dd = $('<dd class="new-candidate">').appendTo($candidates);
		$('<span>').text(($dd.index('dd') + 1) + '. ').appendTo($dd);
		$('<input class="input-candidate-name" type="text">').appendTo($dd);
		$('<button class="save-candidate">').text(messages.save)
			.button({icons:{secondary: 'ui-icon-check'}}).appendTo($dd);
		$('<button class="remove-candidate">').text(messages.remove)
			.button({icons:{secondary: 'ui-icon-close'}}).appendTo($dd);
		$(this).button('disable');
	});
	
	var trueIndex = 0;
	$formContainer.on({
		click : function() {
			var $dd = $(this).closest('dd');
			var index = $dd.index('dd');
			var title = $dd.find('input[type="text"]').val();
			
			var $newDd = $('<dd class="saved">');
			var _trueIndex = trueIndex++;
			$newDd.attr('trueIndex', _trueIndex);
			$('<span class="index">').text((index + 1) + '. ').appendTo($newDd);
			$('<span class="title">').text(title).appendTo($newDd);
			$('<span class="remove-saved-candidate subtitle hide ml5 pointer">').text('remove').appendTo($newDd);
			$('<input type="hidden">').attr('name', 'candidates[' + _trueIndex + '].title')
				.attr('index', _trueIndex)
				.attr('value', title)
				.appendTo($newlistform);
			
			$dd.replaceWith($newDd);
			$btnNewCandidate.button('enable');
			return false;
		}
	}, '.save-candidate');
	
	$formContainer.on({
		click : function() {
			$(this).closest('dd').fadeOut('fast', function() {
				$(this).remove();
				$btnNewCandidate.button('enable');
			});
			return false;
		}
	}, '.remove-candidate');
	
	$candidates.on({
		mouseenter : function(){
			$(this).addClass('ui-state-highlight')
				.find('.remove-saved-candidate').show();
		},
		mouseleave : function(){
			$(this).removeClass('ui-state-highlight')
				.find('.remove-saved-candidate').hide();
		}
	}, 'dd.saved');
	
	function reindexCandidates() {
		$candidates.find('dd').each(function(i, dd) {
			$(dd).find('span.index').text((i + 1) + '. ');
		});
	}
	
	$candidates.on({
		click: function() {
			var $this = $(this);
			var $dd = $this.closest('dd');
			var trueIndex = $dd.attr('trueIndex');
			
			//remove hidden and reindex the rest
			$newlistform.find('input[index="' + trueIndex + '"]').remove();

			$dd.fadeOut('fast', function() {
				$(this).remove();
				$btnNewCandidate.button('enable');
				reindexCandidates();
			});
			return false;
		}
	}, '.remove-saved-candidate');
	
	$candidates.on({
		keyup: function(){
			startTimeout(this);
		},
		paste: function(){
			startTimeout(this);
		},
		focus: function(){
			startTimeout(this);
		}
	}, '.input-candidate-name');
	
	var searchtimeout;
	function startTimeout(input) {
		if(searchtimeout) {
			clearTimeout(searchtimeout);
		}
		searchtimeout = setTimeout(function(){
			useful.autocomplete({
				parent				: $(input),
				minlength			: 4,
				url					: '<spring:url value="/s/" />',
				resultsContainer	: $autocompleteResults,
				descLength			: 80,
				onClick				: function(){
						var $this = $(this);
						var $dd = $('dd.new-candidate');
						var index = $dd.index('dd');
	
						var _trueIndex = trueIndex++;
						var $newDd = $('<dd class="saved">').attr('trueIndex', _trueIndex);
						$(this).clone().appendTo($newDd);
						$('<span class="remove-saved-candidate subtitle hide ml5 pointer">').text('remove').appendTo($newDd);
						$('<input type="hidden">')
							.attr('name', 'candidates[' + _trueIndex + '].attachmentId')
							.attr('index', _trueIndex)
							.attr('value', $this.attr('attachmentId'))
							.appendTo($newlistform);
						$('<input type="hidden">')
							.attr('name', 'candidates[' + _trueIndex + '].attachmentType')
							.attr('index', _trueIndex)
							.attr('value', $this.attr('attachmentType'))
							.appendTo($newlistform);
						
						$dd.replaceWith($newDd);
	
						$btnNewCandidate.button('enable');
					}
			});
		}, 500);
	}
	
	$('#allLists').easyPaginate();
	
	//time
	$('.timeme').each(function(i, timeme) {
		var f = moment(new Date(parseInt($(this).html()))).format('MMM Do YYYY');
		debug(f);
		$(this).html(f);
	});
	
	$(document).on({
		click: function(){
			$autocompleteResults.hide();
		}
	});
	
	$(window).resize(function(){
		$autocompleteResults.hide();
	})
});
</script>

<!-- Topten Controls -->
<div class="grid_4 sidebar-section">
	<div class="sidebar-section-header">Top Ten Lists</div>
	<sec:authorize access="hasRole('ROLE_USER')">
		<button class="btn-newlist">Create a new list</button>
	</sec:authorize>
	<div class="toptens-search">
		<form id="topten-search-form">
			<input type="text" class="ipt-toptens-search" />
			<button class="btn-toptens-search">Search Lists</button>
		</form>
		<div class="toptens-search-results-container">
			<ul class="toptens-search-results"></ul>
			<a class="toptens-search-more hide" href="javascript:;">load more...</a>
		</div>
		<div class="sidebar-divider"></div>
	</div>
</div>
<spring:url var="jsTopTenSearch" value="/resources/javascript/grids/toptensearch.js" />
<script src="${jsTopTenSearch }"></script>
<style>
.toptens-search {
	margin-top: 5px;
	white-space: nowrap;
}
.ipt-toptens-search {
	width: 62%;
}
.btn-toptens-search {
	width: 35%;
}
.toptens-search-results-container {
	position: relative;
}
</style>

<sec:authorize access="hasRole('ROLE_USER')">
<!-- Reviews -->
<div class="reviewqueue grid_4 sidebar-section">
	<div class="sidebar-section-header">Recently Viewed Business for Review</div>
	<div class="review-container">
		<div>Please take some time to review the businesses below if you have completed any transactions with them or have knowledge of their operations.</div>
		<ul class="reviewlist"></ul>
	</div>
	<div class="sidebar-divider"></div>
</div>
<script>
window.urls.reviewqueue = '<spring:url value="/i/reviewqueue.json" />',
window.urls.noreview = '<spring:url value="/i/noreview/" />',
window.urls.neverreview = '<spring:url value="/i/neverreview/" />'
</script>
<script src="${jsReviewQueue }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/reviewqueue.css' />" />
<!-- End Reviews -->
</sec:authorize>