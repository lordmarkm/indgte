<div class="chat-dialog" title="Chat with other People in Dumaguete">
	<div class="chatwindows"></div>
	<div class="chatters-container floatright">
		<ul class="chatters"></ul>
	</div>
	<div class="chat-form-container">
		<form id="chat-form"></form>
	</div>
</div>

<style>
.chatwindows {
	border: 1px solid black;
	width: 240px;
}
.chatters {
	padding: 0;
	list-style-type: none;
	border: 1px solid black;
	width: 60px;
}
</style>

<script>
window.chat = {
	user : '${user.username}',
	urlGetChatters : '<spring:url value="/c/" />',
	urlPost: '<spring:url value="/c/messages/" />',
	urlGet: '<spring:url value="/c/messages/" />'
}

$(function(){
	var 
		$btnChat = $('.navigation.chat'),
		$dialog = $('.chat-dialog'),
		$chatters = $('.chatters'),
		$chatwindows = $('.chatwindows'),
		$form = $('#chat-form');
	
	function openChat() {
		$dialog.dialog({
			modal: false,
			resizable: false,
			maxHeight: false,
			open: makefixed,
			close: closeChat,
			width: 500
		});
		$btnChat.removeClass('ui-state-highlight').addClass('ui-state-active');
		
		dgte.overlay($dialog);
		$.get(chat.urlGetChatters, function(response) {
			$chatters.html('');
			for(var i = 0, len = response.chatters.length; i < len; ++i) {
				addChatter(response.chatters[i]);
			}
		}).complete(function(){
			dgte.fadeOverlay($dialog);
		});
	}
	
	function addChatter(chatter) {
		var $li = $('<li class="chatter">').appendTo($chatters);
		$('<a href="javascript:;">').text(chatter).appendTo($li);
	}

	function makefixed() {
		$dialog.parent().css('position', 'fixed');
	}
	
	function closeChat() {
		if($dialog.dialog('isOpen') == true) $dialog.dialog('close');
		$btnChat.removeClass('ui-state-highlight').removeClass('ui-state-active');
	}
	
	$btnChat.click(function(event) {
		event.stopPropagation(); //important! prevent $(doc).on click(.navigation)
		$btnChat.hasClass('ui-state-active') ? closeChat() : openChat();
		return false;
	});
	
	//open/close windows
	$chatters.on({
		click: function(){
			var chatter = $(this).text();
			p2pOpen(chatter, null, true);
		}
	}, '.chatter');
	
	function findActiveChannel() {
		var $window = $('.chatwindow:visible');
		if($window.length == 0) return null;
		return $window.attr('channel');
	}
	
	function extractChatterFromChannel(channel) {
		debug('trying to extract username from ' + channel);
		var usernames = channel.split('|');
		
		var chatter = chat.user;
		for(var n in usernames) {
			if(usernames[n] != chat.user) {
				chatter = usernames[n];
				break;
			}
		}
		debug('got ' + chatter);
		return chatter;
	}
	
	function findWindow(channel, create) {
		var $window = $('.chatwindow[channel="' + channel + '"]');
		if(create && $window.length == 0) {
			$window = $('<div class="chatwindow">')
				.attr('channel', channel)
				.appendTo($chatwindows);
			$('<div class="ui-widget-header">').text(extractChatterFromChannel(channel)).appendTo($window);
			$messagebox = $('<div class="chat-text">').appendTo($window);
		}
		return $window;
	}
	
	function addMessage($window, message) {
		var $box = $window.find('.chat-text');
		$('<div class="message">').text(message).appendTo($box);
	}
	
	function p2pOpen(chatter, message, switchTo) {
		var channel = channel ? channel : chatter < chat.user ? chatter + '|' + chat.user : chat.user + '|' + chatter;
		var $chatwindow = findWindow(channel, true);
		
		if(message) {
			addMessage($chatwindow, message);
		}
		
		if(switchTo) {
			$('.chatwindow').hide();
			$chatwindow.show();
		}
	}
	
	function channelOpen(channel, switchTo) {
		var $chatwindow = findWindow(channel, true);
		
		if(switchTo) {
			$('.chatwindow').hide();
			$chatwindow.show();
		}
	}
	
	channelOpen('#all', true);
	
	//submit chat
	$form.submit(function(){
		var message = $iptMessage.val();
		if(message.length < 1) return;
		
		$.post(chat.urlPost, {
			channel: findActiveChannel(),
			message: message
		});
		return false;
	});
	
	//get messages
	var lastReceivedId = 0;
	
	getMessages(true);
	
	function getMessages(loop) {
		var channels = getOpenChannels();
		
		debug('Getting messages on channels ' + channels + ' with last received ID at ' + lastReceivedId);
		
		$.get(chat.urlGet, 
			{
				channels: channels,
				lastReceivedId : lastReceivedId
			},	
			function(response){
				for(var i = 0, len = response.messages.length; i < len; ++i) {
					processMessage(response.messages[i]);
				}
				
				//this is the loop
				if(loop) getMessages(true);
			}
		);
	}
	
	function getOpenChannels() {
		var channels = [];
		$('.chatwindow').each(function(i, window) {
			channels.push($(window).attr('channel'));
		});
		return channels;
	}
	
	function processMessage(message) {
		if(message.id > lastReceivedId) lastReceivedId = message.id;
		
		var $window = findWindow(message.channel, false);
		
		if($window.length == 0) {
			var chatter = extractChatterFromChannel(channel);
			p2pOpen(chatter, message.message, false)
		} else {
			addMessage($window, message.message);
		}
	}
});
</script>