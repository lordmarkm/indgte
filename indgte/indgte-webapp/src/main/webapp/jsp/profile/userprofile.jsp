<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<title>Yellow Pages | InDumaguete</title>

<div class="grid_12">

<section class="interact">
	<button class="btn-subscribe-toggle">Subscribe to ${targetMain.username }</button>
</section>

<section class="wishlist"></section>

<section class="feed">
</section>

<section class="businesses">
</section>

</div>

<script>
window.urls = {
	subscribe: '<spring:url value="/i/subscribe/user/${targetFacebook.id}.json" />',
	unsubscribe: '<spring:url value="/i/unsubscribe/user/${targetFacebook.id}.json" />'
}

window.target = {
	facebookId: '${targetFacebook.id}',
	username: '${targetMain.username}'
}

$(function(){
	//subscribe
	var subscribed = '${subscribed}' === 'true';
	var $btnSubscribe = $('.btn-subscribe-toggle');
	
	function refreshSubsButton() {
		if(subscribed) {
			$btnSubscribe
				.button({label: 'Subscribed', icons: {secondary: 'ui-icon-circle-check'}})
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Unsubscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Subscribed', icons:{secondary: 'ui-icon-circle-check'}});
				});
		} else {
			$btnSubscribe
				.button({label: 'Not subscribed', icons: {secondary: 'ui-icon-circle-close'}})				
				.unbind('hover')
				.hover(function(){
					$btnSubscribe.button({label: 'Subscribe', icons: {}});
				}, function(){
					$btnSubscribe.button({label: 'Not subscribed', icons:{secondary: 'ui-icon-circle-close'}});
				});
		}
	}
	refreshSubsButton();

	$btnSubscribe.click(function(){
		var url = subscribed ? urls.unsubscribe : urls.subscribe;
		$.post(url, function(response) {
			switch(response.status) {
			case '200':
				subscribed = !subscribed;
				refreshSubsButton();
				break;
			default:
				debug(response);
			}
		});
	});
});
</script>