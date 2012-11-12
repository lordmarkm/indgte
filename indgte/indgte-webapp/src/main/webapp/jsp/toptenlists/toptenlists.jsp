<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../tiles/links.jsp" %>

<title>Top Ten Lists In Dumaguete</title>
<link rel="stylesheet" href="<spring:url value='/resources/css/toptens.css' />" />
<script type="text/javascript" src="${jsApplication }"></script>
<script type="text/javascript" src="${jsAutocomplete }"></script>

<div class="grid_12">

<section class="newlist">
	<div class="ui-widget-header">Create a new list</div>		
	<button class="btn-newlist">Create a new list</button>
</section>

<section class="lists-recent">
	<div class="ui-widget-header">Recent Top Ten Lists</div>
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
						<c:if test="${empty candidate.attachment }">
						${candidate.title }
						</c:if>
						<c:if test="${not empty candidate.attachment }">
						<c:choose>
							<c:when test="${candidate.attachment.attachmentType eq 'business'}">
							<a href="${urlProfile }${candidate.attachment.id}">
							</c:when>
						</c:choose>
						${candidate.attachment.name }
						</a>
						</c:if>
					</div>
					<c:if test="${not empty candidate.attachment }">
					<div class="candidate-description subtitle">${candidate.attachment.description }</div>
					</c:if>
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

<section class="lists-popular">

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

<style>
dd.saved:not(.ui-state-highlight) {
	border: 1px solid transparent;
}
</style>

<script>
window.urls = {
	newlist : '<spring:url value="/i/toptens.json" />',
	listablegroups : '<spring:url value="/s/listablegroups.json" />'
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