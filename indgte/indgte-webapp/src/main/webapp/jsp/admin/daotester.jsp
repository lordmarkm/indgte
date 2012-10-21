<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="urlDaoTester" value="/a/daotest/" />

<section class="grid_12">
	<ul>
		<li>
			Get Posts by Domain:
			<fieldset>
			<legend>postsGetByDomain</legend>
			<ul>
			<li>
				<label for="postsGetByDomain-domain">Domain:</label>
				<input type="text" id="postsGetByDomain-domain" value="baldwinternet"/>
			</li>
			<li>
				<label for="postsGetByDomain-start">Start:</label>
				<input type="number" id="postsGetByDomain-start" value="0" />
			</li>
			<li>			
				<label for="postsGetByDomain-howmany">How many?</label>
				<input type="number" id="postsGetByDomain-howmany" value="10" />
			</li>
			</ul>
			
			<button class="postsGetByDomain-submit">Query</button>
			</fieldset>
		</li>
		<li>
			Get subposts for:
			<input type="text" class="postsGetSubposts-username" value="${user.username }"/>
			<button class="postsGetSubposts-get">Get</button>
		</li>
	</ul>
</section>

<div class="dialog" style="display: none;"></div>

<script>
$(function(){
	var urlDaoTester = '${urlDaoTester}';
	
	var $dialog = $('.dialog');
	
	//1. Get Posts by domain
	var $1domain = $('#postsGetByDomain-domain'),
		$1start = $('#postsGetByDomain-start'),
		$1howmany = $('#postsGetByDomain-howmany'),
		$1submit = $('.postsGetByDomain-submit');
	
	$1submit.click(function(){
		var domain = $1domain.val(),
			start = $1start.val(),
			howmany = $1howmany.val();
		
		console.debug(domain + ', ' + start + ', ' + howmany);
		
		$.get(urlDaoTester + 'postsGetByDomain', {'domain': domain, 'start': start, 'howmany': howmany}, function(response) {
			debug("Response received. Status: " + response.status + " Message: " + response.message);
			switch(response.status) {
			case '200':
				if(response.posts.length > 0) {
					$dialog.text(response.posts);
				} else {
					$dialog.text('No posts by ' + domain);
				}
				$dialog.dialog({title: 'postsGetByDomain'});
				break;
			default:
				$dialog.text(response.message).dialog({title:'postsGetByDomain Error'});
				break;
			}
		});
	});
	
	//2. Get subposts
	var $2username = $('.postsGetSubposts-username'),
		$2get = $('.postsGetSubposts-get');
	
	$2get.click(function(){
		$.get(urlDaoTester + 'postsGetSubposts', {'username' : $2username.val()}, function(response) {
			console.debug(response);
		});
	});
});
</script>

<spring:url var="jsApplication" value="/resources/javascript/application.js" />
<script type="text/javascript" src="${jsApplication }"></script>