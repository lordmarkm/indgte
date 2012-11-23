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
</div>

<style>
.chatwindows {
	width: 345px;
	border: 1px dotted lightskyblue;
}

.chat-form-container {
	margin-top: 10px;
}

.ipt-message {
	width: 270px;
}

.btn-send {
	width: 65px;
}

.chatters-container {
	position: absolute;
	top: 0;
	right: 0;
	height: 230px;
	overflow-x: hidden;
	overflow-y: auto;
}

.chatters {
	padding: 0;
	margin: 5px 0;
	list-style-type: none;
	width: 130px;
	font-size: 11px;
}

.chatter {
	cursor: pointer;
	padding: 3px 2px;
}

.chatter:not(.ui-state-highlight):not(.ui-state-active) {
	border: 1px solid transparent;
}

.chatter-img {
	width: 28px;
	height: 28px;
	margin-right: 8px;
	vertical-align: middle;
}

.chatter-info {
	vertical-align: middle;
	display: inline-block;
}

.chattername {
	width: 80px;
	display: block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.chatter-status {
	width: 40px;
	display: inline-block;
}
.chatter.offline .chatter-status {
	opacity: 0.4;
	font-style: italic;
}

.chatter-notification {
	width: 40px;
	display: inline-block;
	color: red;
	font-weight: bolder;
}

.chat-text {
	height: 200px;
	overflow-x: hidden;
	overflow-y: auto;
}

.chat-message {
	font-size: 11px;
	margin: 0 5px 5px 5px;
	padding-top: 5px;
}

.chat-message:not(:first-child) {
	border-top: 1px solid #eeeeee;
}

.chat-img {
	vertical-align: text-top;
	height: 32px;
	width: 32px;
	margin: 2px 5px 2px 2px;
}

.chat-onemessage-container {
	width: 275px;
	vertical-align: text-top;
	display: inline-block;
}

.chat-onemessage {
	margin-bottom: 3px;
}
.chat-onemessage:first-child {
	margin-top: 4px;
}
</style>

<script>
window.chat = {
	user : '${user.username}',
	urlGetChatters : '<spring:url value="/c/" />',
	urlPost: '<spring:url value="/c/messages/" />',
	urlGet: '<spring:url value="/c/messages/" />',
	urlChannelMessages: '<spring:url value="/c/messages/" />',
	channelImageUrl: 'http://i.imgur.com/unfvF.png'
}

$(function(){
	var 
		$btnChat = $('.navigation.chat'),
		$dialog = $('.chat-dialog'),
		$chatters = $('.chatters'),
		$chatwindows = $('.chatwindows'),
		$form = $('#chat-form'),
		$iptMessage = $('.ipt-message');
	
	//general
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
		
		channelOpen('#dumaguete', true);
		getChatters(true);
		getMessages(true);
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
	
	//presence
	function getOnlineChatters() {
		var chatters = [];
		$('.chatter.online').each(function(i, li) {
			chatters.push($(li).attr('username'));
		});
		if(chatters.length == 0) chatters = ['none'];
		return chatters;
	}
	
	function getChatters(loop) {
		var onlineChatters = getOnlineChatters();
		debug('Getting chatters. Online chatters: ' + onlineChatters);
		
		$.get(chat.urlGetChatters, {chatters: onlineChatters}, function(response) {
			switch(response.status) {
			case '200':
				if(response.channels) {
					for(var i = 0, len = response.channels.length; i < len; ++i) {
						addChannelOption(response.channels[i]);
						//channelOpen(response.channels[i], false);
					}
				}
				if(response.goneOnline) {
					for(var i = 0, len = response.goneOnline.length; i < len; ++i) {
						makeOnline(response.goneOnline[i]);
					}
				}
				if(response.goneOffline) {
					for(var i = 0, len = response.goneOffline.length; i < len; ++i) {
						makeOffline(response.goneOffline[i]);
					}
				}
				if(loop) getChatters(true);
				break;
			default:
				debug('Timeout. Resending request.');
				if(loop) getChatters(true);
			}
		});		
	}
	
	function addChannelOption(channel) {
		debug('Adding channel ' + channel);
		var $li = $('<li class="chatter">').attr('username', channel).appendTo($chatters);
		$('<img class="chatter-img">').attr('src', chat.channelImageUrl).appendTo($li);
		
		var $chatterinfo = $('<div class="chatter-info">').appendTo($li);
		
		$('<div class="chattername bluetext">').text(channel).appendTo($chatterinfo);
		$('<div class="chatter-notification">').text(' ').appendTo($chatterinfo);
	}
	
	function constructChatter(chatter) {
		debug('Adding chatter ' + chatter.username);
		var $li = $('<li class="chatter">').attr('username', chatter.username).appendTo($chatters);
		$('<img class="chatter-img">').attr('src', chatter.imageUrl).appendTo($li);
		
		var $chatterinfo = $('<div class="chatter-info">').appendTo($li);
		
		$('<div class="chattername">').text(chatter.username).appendTo($chatterinfo);
		$('<div class="chatter-status">').text(' ').appendTo($chatterinfo);
		$('<div class="chatter-notification">').text(' ').appendTo($chatterinfo);
		return $li;
	}
	
	function makeOnline(chatter) {
		var $chatter = $('.chatter[username="' + chatter.username + '"]');
		if($chatter.length == 0) {
			$chatter = constructChatter(chatter);
		}
		$chatter.removeClass('offline').addClass('online')
			.find('.chatter-status').text('Online');
	}
	
	function makeOffline(chatter) {
		var $chatter = $('.chatter[username="' + chatter.username + '"]');
		if($chatter.length == 0) {
			$chatter = constructChatter(chatter);
		}
		$chatter.removeClass('online').addClass('offline')
			.find('.chatter-status').text('Offline');
	}

	//open/close windows

	$chatters.on({
		mouseenter: function(){
			$(this).addClass('ui-state-highlight');
		},
		mouseleave: function(){
			$(this)
			.removeClass('ui-state-highlight');
		},
		click: function(){
			var $this = $(this);
			$this.find('.chatter-notification').fadeOut('500', function(){$(this).text(' ')});
			var chatter = $(this).attr('username');
			p2pOpen(chatter, true);
			$iptMessage.focus();
		}
	}, '.chatter');
	
	function findActiveChannel() {
		var $window = $('.chatwindow:visible');
		if($window.length == 0) return null;
		return $window.attr('channel');
	}
	
	function extractChatterFromChannel(channel) {
		var usernames = channel.split('|');
		
		var chatter = chat.user;
		for(var n in usernames) {
			if(usernames[n] != chat.user) {
				chatter = usernames[n];
				break;
			}
		}
		return chatter;
	}
	
	function createChannelWindow(channel) {
		$window = $('<div class="chatwindow">')
			.attr('channel', channel)
			.appendTo($chatwindows);
		$('<div class="ui-widget-header">').text(extractChatterFromChannel(channel)).appendTo($window);
		$messagebox = $('<div class="chat-text">').appendTo($window);

		$.post(chat.urlChannelMessages + encodeURIComponent(channel) + '/' + 10 + '.json', function(response) {
			if(!response.messages) return;
			response.messages.reverse();
			for(var i = 0, len = response.messages.length; i < len; ++i) {
				var message = response.messages[i];
				processMessage(message);
			}
		});
		
		return $window;
	}
	
	function findWindow(channel, create) {
		var $window = $('.chatwindow[channel="' + channel + '"]');
		if(create && $window.length == 0) {
			$window = createChannelWindow(channel);
		}
		return $window;
	}
	
	function addMessage($window, message, messageId) {
		var $box = $window.find('.chat-text');
		
		var lastPresentId = $box.find('.message:last').attr('messageId');
		
		$('<div class="message">').attr('messageId', messageId).text(message).appendTo($box);
		$box.scrollTop($box[0].scrollHeight);
	}
	
	function p2pOpen(chatter, switchTo) {
		var channel = chatter.indexOf('#') == 0 ? chatter : chatter < chat.user ? chatter + '|' + chat.user : chat.user + '|' + chatter;
		var $chatwindow = findWindow(channel, true);
		
		if(switchTo) {
			$('.chatwindow').hide();
			$chatwindow.show();
		} else {
			$chatwindow.hide();
		}
	}
	
	function channelOpen(channel, switchTo) {
		var $chatwindow = findWindow(channel, true);
		
		if(switchTo) {
			$('.chatwindow').hide();
			$chatwindow.show();
		}
	}
	
	//submit chat
	$form.submit(function(){
		var message = $iptMessage.val();
		if(message.length < 1) return false;
		var channel = findActiveChannel();
		debug('Posting message [' + message + '] to channel [' + channel + ']');
		$.post(chat.urlPost, 
			{
				channel: channel,
				message: message
			},
			function(response) {
				switch(response.status) {
				case '200':
					$iptMessage.val('');
					break;
				default:
					debug('Error posting message');
					debug(response);
				}
			}
		);
		return false;
	});
	
	//get messages
	var lastReceivedId = 0;
	var waiting = false;
	function getMessages(loop) {
		if(waiting) return;
		waiting = true;
		var channels = getOpenChannels();
		
		debug('Getting messages on channels ' + channels + ' with last received ID at ' + lastReceivedId);
		
		$.get(chat.urlGet, 
			{
				channels: channels,
				lastReceivedId : lastReceivedId
			},	
			function(messages){
				debug('XHR success on get messages');
				if(!messages || messages.length == 0) {
					debug('No new messages. Will fire new request.');
					waiting = false;
					if(loop) getMessages(true);
					return;
				}
				if(typeof messages.reverse == 'function') messages.reverse();
				for(var i = 0, len = messages.length; i < len; ++i) {
					processMessage(messages[i], true);
				}
				//this is the loop
				if(loop) {
					waiting = false;
					getMessages(true);
				}
			}
		).error(function(){
			//on error, try again after 10 seconds
			debug('XHR returned error on get messages');
			setTimeout(function(){
				waiting = false;
				getMessages(true);
			}, 10000);
		});
	}
	
	function getOpenChannels() {
		var channels = [];
		$('.chatwindow').each(function(i, window) {
			channels.push($(window).attr('channel'));
		});
		if(channels.length == 0) channels.push('none');
		return channels;
	}
	
	function processMessage(message, postInit) {
		if(message.id > lastReceivedId) {
			lastReceivedId = message.id;
		}
		
		var $window = findWindow(message.channel, false);

		var chatter = extractChatterFromChannel(message.channel); 
		//we do not user message.sender because user might be the sender
		
		if($window.length == 0) {
			p2pOpen(chatter, false)
		} else {
			appendMessage(message);
		}
		
		if(postInit) {
			notify(chatter);
		}
	}
	
	function notify(sender) {
		var activeChannelName = findActiveChannel();
		var activeChattername = extractChatterFromChannel(activeChannelName);
		
		if(sender != activeChattername) {
			//add notif to chatter summary
			var $chatter = $('.chatter[username="' + sender + '"]'); 
			var $notif = $chatter.find('.chatter-notification');
			
			var count = $notif.find('.count').text();
			if(!count) {
				$notif.html('<span class="count">1</count>').append(' new');
			} else {
				var currentcount = parseInt($notif.find('.count').text()) + 1;
				$notif.html('<span class="count">' + currentcount + '</count>').append(' new');
			}
			
			$notif.dgteFadeIn();
		}
	}
	
	//accepts ChatMessage in JSON form
	function appendMessage(message) {
		var $box = findWindow(message.channel, true).find('.chat-text');
		
		//append sender img if it is different from the last on, i.e. if the same guy sends 2 msgs in a row no need to add his pic again
		var lastImg = $box.find('.chat-img:last').attr('src');
		if(lastImg != message.senderImageUrl) {
			var $msg = $('<div class="chat-message">').appendTo($box);
			$('<img class="chat-img">').attr('src', message.senderImageUrl).prependTo($msg);
			var $text = $('<div class="chat-onemessage-container">').appendTo($msg);
			$('<div class="chat-onemessage">').attr('messageId', message.id).text(message.message).appendTo($text);
		} else {
			var $lastcontainer = $box.find('.chat-onemessage-container:last');
			$('<div class="chat-onemessage">').attr('messageId', message.id).text(message.message).appendTo($lastcontainer);
		}

		$box.scrollTop($box[0].scrollHeight);
	}
});
</script>