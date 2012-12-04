<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<title>Yellow Pages | In Dumaguete</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_12">

<ul class="hide">
<c:forEach items="${businesses }" var="entry">
	<li class="category ${fn:substring(entry.key, 0, 1)}" categoryName="${entry.key }" categoryId="${entry.value[0] }" businessCount="${entry.value[1] }">
		<span class="category-name">${entry.key } (${entry.value[1] })</span>
	</li>
</c:forEach>
</ul>

<div class="grid_6">
	<ul class="alphabetized"></ul>
</div>

<div class="topbusinesses grid_5"></div>

</div>

<style>
.alphabetized {
	list-style-type: none;
	padding-left: 0;
}
.alphabetized > li {
	margin-bottom: 15px;
}

.sublist {
	list-style-type: none;
	padding: 0;
}
.sublist li {
	padding: 5px;
}

.category {
	cursor: pointer;
	height: 15px;
}
.category:not(.ui-state-active):not(.ui-state-highlight) {
	border: 1px solid transparent;
}

.category-name {
	text-transform: capitalize;
}

.topbusinesses {
	position: relative;
	border: 1px dotted lightskyblue;
	width: 420px !important;
	max-width: 420px !important;
	margin-top: 13px;
	padding: 5px;
}

.category-title {
	text-align: center;
	text-transform: capitalize;
}

.autocomplete-container {
	margin-bottom: 10px;
	cursor: pointer;
}
.autocomplete-container:not(:hover) {
	max-height: 60px;
	overflow-y: hidden;
}
</style>

<script>
window.urls = {
	getCategoryBusinesses: '<spring:url value="/s/businesses/" />',
	imgur: 'http://i.imgur.com/',
	businessProfile: '<spring:url value="/" />',
	categories : '<spring:url value="/s/categories/" />'
}

$(function(){
	var $alphabetized = $('.alphabetized'),
		$topbusinesses = $('.topbusinesses'),
		$category = $('.category');
	
	var letters = dgte.search.letters;
	for(var i = 0, l = letters.length; i < l; ++i) {
		var letter = letters[i];
		
		var $categories = $('.category.' + letter);
		if($categories.length > 0) {
			var $group = $('<li>').appendTo($alphabetized);
			
			$('<div class="section-header">').text(letter.toUpperCase()).appendTo($group);
			
			var $sublist = $('<ul class="sublist">').appendTo($group);
			$categories.appendTo($sublist);
		}
	}
	
	$category.on({
		click: function(){
			if($topbusinesses.find('.overlay').length) return;
			$('<div class="overlay">').appendTo($topbusinesses);
			var $this = $(this);
			$category.removeClass('ui-state-active');
			$this.addClass('ui-state-active');
			loadBusinesses($this.attr('categoryName'), $this.attr('categoryId'), $this.attr('businessCount'));
		},
		mouseenter: function() {
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function() {
			$(this).removeClass('ui-state-highlight');
		}
	});
	
	var responses = {};
	
	function processSuccess(categoryName, categoryId, response, businessCount) {
		$topbusinesses.find('h3,.autocomplete-container,.viewall').remove();
		$('<h3 class="category-title">').text(categoryName).appendTo($topbusinesses);
		for(var i = 0, len = response.businesses.length; i < len; ++i) {
			var business = response.businesses[i];
			addBusiness(business);
		}
		var $viewall = $('<div class="viewall">').appendTo($topbusinesses);
		var descriptionText = response.businesses.length > 1 ? 'Showing the top ' + response.businesses.length + ' businesses in ' + categoryName
				: 'Showing the only business in ' + categoryName;
		$('<div>').text(descriptionText).appendTo($viewall);
		
		var linkText = businessCount > dgte.yellowpages.preview ? 'View all ' + businessCount + ' businesses in ' + categoryName 
				: 'See additional details';
		$('<a>').attr('href', urls.categories + categoryId).text(linkText).appendTo($viewall);
		
		$('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
	}
	
	function loadBusinesses(categoryName, categoryId, businessCount) {
		if(responses[categoryId]) {
			processSuccess(categoryName, categoryId, responses[categoryId], businessCount);
			return;
		}
		
		$.get(urls.getCategoryBusinesses + categoryId + '/' + dgte.yellowpages.preview + '.json', function(response) {
			switch(response.status) {
			case '200':
				responses[categoryId] = response;
				processSuccess(categoryName, categoryId, response, businessCount);
				debug(JSON.stringify(responses));
				break;
			default:
				debug(response);
				$('.overlay').delay(800).fadeOut(200, function() { $(this).remove(); });
			}
		});
	}
	
	function addBusiness(business) {
		var $businessContainer = $('<div class="yellowpages autocomplete-container">').attr('domain', business.identifier).appendTo($topbusinesses);
		
		$('<img class="yellowpages autocomplete-img">').attr('src', business.thumbnailHash ? urls.imgur + business.thumbnailHash + 's.jpg' : dgte.urls.blackSquareSmall).appendTo($businessContainer);
		var $title = $('<div class="yellowpages autocomplete-title">').appendTo($businessContainer);
		$('<a>').attr('href', urls.businessProfile + business.identifier).text(business.title).appendTo($title);
		$('<div class="yellowpages autocomplete-description">').html(business.description).appendTo($businessContainer);
	}
	
	$(document).on({
		click: function(){
			window.location.href = urls.businessProfile + $(this).attr('domain');
		},
		mouseenter: function() {
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function() {
			$(this).removeClass('ui-state-highlight');
		}
	}, '.yellowpages.autocomplete-container');
	
	//topbusinesses
	var originalOffsetTop = $topbusinesses.offset().top;
	var originalPositionTop = $topbusinesses.position().top;
	
	var i = 0;
	$(window).scroll(function(event) {
		if($(this).scrollTop() + 13 > originalOffsetTop) {
			$topbusinesses.css({'position': 'fixed', 'top': 0});
		} else {
			$topbusinesses.css({'position': 'relative', 'top': 0});
		}
	});
	
	//load first
	$('.category:first').click();
});
</script>