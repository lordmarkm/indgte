<link rel="stylesheet" href="<c:url value='/resources/css/navbar/smileys-big.css' />" />
<link rel="stylesheet" href="<c:url value='/resources/css/navbar/chat.css' />" />
<script src="${jsCookie }"></script>

<div class="chat-dialog hide" title="Chat with other People in Dumaguete">
	<div class="chatwindows"></div>
	<div class="chatters-container">
		<ul class="chatters"></ul>
	</div>
	<div class="chat-form-container">
		<form id="chat-form">
			<input type="text" class="ipt-message" />
			<button class="btn-send">Send</button>
		</form>
	</div>
	<div class="chat-extras">
		<div class="smiley-box">
			<div class="btn-smileys smiley grin"></div>
		</div>
		<input type="checkbox" class="chkAppearOffline" id="chkAppearOffline" />
		<label for="chkAppearOffline">Appear offline</label>
	</div>
</div>

<div class="smileys-container" style="display: none;"></div>

<script>
window.chat = {
	user : '${user.username}',
	urlGetChatters : '<spring:url value="/c/" />',
	urlPost: '<spring:url value="/c/messages/" />',
	urlGet: '<spring:url value="/c/messages/" />',
	urlChannelMessages: '<spring:url value="/c/messages/" />',
	channelImageUrl: 'http://i.imgur.com/unfvF.png',
	urlAppearOffline: '<spring:url value="/c/appearoffline/" />'
}
</script>
<script src="${jsChat}"></script>