<section id="navbar" class="grid_12" style="height: 100px;">
	<h3>This part is all javascript:</h3>
	
	<div class="fb-info" class="hide">
		<img class="fb-img" />
		<span class="fb-name"></span>
	</div>
	<div class="fb-login-button">Login with Facebook</div>
	<div id="fb-root"></div>
</section>

<script>
window.facebook = {
	statusChange : function(statusChangeResponse) {
		if(statusChangeResponse.authResponse) {
			$('.fb-login-button').hide();
			FB.api('/me', function(user){
				if(user) {
					$('.fb-info').show()
						.find('.fb-img').attr('src', 'https://graph.facebook.com/' + user.id + '/picture?type=square').css('width', '25px').end()
						.find('.fb-name').text(user.name);
				} 
			});
		} else {
			$('.fb-login-button').show();
		}
	}
}

window.fbAsyncInit = function() {
    FB.init({
      appId      : '270450549726411',
      status     : true, 
      cookie     : true, 
      xfbml      : true  
    });
    FB.Event.subscribe('auth.statusChange', facebook.statusChange);
};
(function(d){
	var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	if (d.getElementById(id)) {return;}
	js = d.createElement('script'); js.id = id; js.async = true;
	js.src = "//connect.facebook.net/en_US/all.js#xfbml=1";
	ref.parentNode.insertBefore(js, ref);
}(document));
</script>