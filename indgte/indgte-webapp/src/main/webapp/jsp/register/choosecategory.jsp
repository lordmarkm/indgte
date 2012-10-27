<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script src="http://ajax.microsoft.com/ajax/jQuery.Validate/1.6/jQuery.Validate.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-scrollTo/1.4.3/jquery.scrollTo.min.js"></script>

<spring:url value="/r/save/2/" var="urlSubmit" />

<div class="tabs-container grid_12">

<div class="tabs">
	<ul class="tabs-heads"></ul>
</div>

<form:form method="post" commandName="regform" action="${urlSubmit }">
	<form:hidden path="category" />
</form:form>

<div style="text-align: center; margin-top: 10px;">
	<button class="submit capitalize">No Category chosen</button>
	<button class="newcategory">My business fits into none of these</button>
	<button class="uncategorized">I prefer to leave my business Uncategorized</button>
</div>
</div>

<div class="newcategory-dialog" style="display: none;">
	<p>Looks like we missed a category of business. We're sorry. Please enter your business category here:</p>
	<div style="text-align: center;">
	<form id="newcategory-form" action="javascript:;">
		<input id="fldNewcategory" class="field-newcategory required" minlength="4" maxlength="45" type="text" />
		<div>&nbsp;<span></span></div>
	</form>
	</div>
</div>

<style>
.tabs {
	height: 420px;
	min-height: 420px;
}
.tabs-title {
    float: left;
    font-size: 1em;
    height: 100%;
    margin: 4px 15px 0 15px;
    vertical-align: middle;
}
.ui-tabs .ui-tabs-nav li a {
    float: left;
    padding: 0.2em;
    text-decoration: none;
    font-family: monospace;
}
.tab.ui-tabs-panel {
	height: 340px;
	overflow-x: hidden;
	overflow-y: auto;
}
.category {
	display: inline-block;
	width: 250px;
	white-space: nowrap;
	text-transform: capitalize;
	overflow: hidden;
	text-overflow: ellipsis;
	padding: 2px 0 2px 20px;
	cursor: pointer;
	border: 1px solid transparent;
}
</style>

<script>
window.urls = {
	getCategories : '<spring:url value="/r/categories/" />'
}

$(function(){
	var $regform = $('#regform'),
		$fieldCategory = $('#category'),
		$tabs = $('.tabs'),
		$tabsHeads = $('.tabs-heads'),
		$btnSubmit = $('button.submit'),
		$newcategory = $('.newcategory-dialog'),
		$formNewCategory = $('#newcategory-form'),
		$fldNewCategory = $('.field-newcategory'),
		$btnNewCategory = $('button.newcategory'),
		$btnUncategorized = $('button.uncategorized');
	
	var letters = dgte.search.letters;
	for(var i = 0; i < 26; ++i) {
		//construct header
		var $li = $('<li>').appendTo($tabsHeads);
		$('<a>').attr('href', '#' + letters[i]).text(letters[i].toUpperCase()).appendTo($li);
		
		//construct content container
		$('<div class="tab">').attr('id', letters[i]).appendTo($tabs);
	}
	
	var loaded = [];
	$tabs.tabs({
		create: function(event, ui) {
			if(!isEdit()) onTab('a', $('#a'));
			$('.tabs-heads').prepend('<div class="tabs-title">Choose a Category</div>');
		},
		select: function(event, ui){
			var firstLetter = ui.panel.id;
			if(loaded.indexOf(firstLetter) != -1) return;
			onTab(firstLetter, $(ui.panel));
		}
	});
	
	function onTab(firstLetter, $panel, callback) {
		loaded.push(firstLetter);
		
		var $overlay = $('<div class="overlay">').appendTo($panel);
		$.get(urls.getCategories + firstLetter, function(response) {
			switch(response.status) {
			case '200':
				var categories = response.categories.split(',');
				for(var i = 0, length = categories.length; i < length; ++i) {
					$panel.append(makeCategory(categories[i]));
				}
				if(typeof(callback) == typeof(Function)) {
					callback();
				}
				break;
			default:
				debug(response);
			}
			
			$overlay.delay(800).fadeOut(200, function() { $overlay.remove(); });

			var category = $('#category').val();
			if(category) {
				var catFirstLetter = category[0];
				if(catFirstLetter === firstLetter) {
					var $cat = $('.category[title="' + category + '"]').click();
					$('#' + firstLetter).scrollTo($cat, 1000, {over:-5});
				}
			}
		});
	}
	
	function makeCategory(category) {
		return $('<div class="category">').text(category).attr('title', category);
	}
	
	$btnSubmit.button('disable').click(function(){
		$regform.submit();
	});
	
	$btnUncategorized.click(function(){
		$fieldCategory.val('uncategorized');
		$regform.submit();
	});
	
	$formNewCategory.validate({
		errorPlacement: function(error, element) {
			element.next().find('span').html('').append(error);
		}
	});
	$btnNewCategory.click(function(){
		$newcategory.dialog({
			title: 'Create a new category',
			buttons: {
				'Save' : function() {
					if(!$formNewCategory.valid()) {
						return;
					}
					var newcategory = $fldNewCategory.val();
					$fieldCategory.val(newcategory);
					$regform.submit();
				},
				'Cancel' : function() {
					$newcategory.dialog('close');
				}
			}
		});
	});
	
	//handle edit
	function isEdit(){
		var category = $('#category').val();
		if(category === '') {
			return false;
		}
		var firstLetter = category[0];
		
		if(firstLetter === 'a') {
			onTab('a', $('#a'));
		} else {
			$tabs.tabs('select', '#' + firstLetter);
		}
		return true;
	}
	
	$(document).on({
		click : function(){
			var $this = $(this);
			$('.category.ui-state-active').removeClass('ui-state-active');
			$this.addClass('ui-state-active');
			$fieldCategory.val($this.text());
			
			$btnSubmit.button('option', 'label', 'Choose ' + $this.text());
			if($btnSubmit.is(':disabled')) {
				$btnSubmit.button('enable');
			}
		}
	}, '.category')
});
</script>

<script type="text/javascript" src="${jsApplication }"></script>