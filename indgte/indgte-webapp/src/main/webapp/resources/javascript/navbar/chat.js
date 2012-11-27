$(function(){
	var 
		$btnChat = $('.navigation.chat'),
		$dialog = $('.chat-dialog'),
		$chatters = $('.chatters'),
		$chatwindows = $('.chatwindows'),
		$form = $('#chat-form'),
		$iptMessage = $('.ipt-message');
	
	//general
	function openChat(persistentChannel, offset) {
		$dialog.dialog({
			modal: false,
			resizable: false,
			maxHeight: false,
			open: makefixed,
			close: closeChat,
			width: 500,
			dragStop: updateCookieData
		});
		$dialog.parent().offset(offset);
		$btnChat.removeClass('ui-state-highlight').addClass('ui-state-active');
		
		setTimeout(function(){
			getChatters(true);
		}, 500);
		if(persistentChannel) {
			p2pOpen(persistentChannel, true);
		} else {
			channelOpen('#dumaguete', true);
		}
		
		//getMessages(true);
	}
	
	function makefixed() {
		$dialog.parent().css('position', 'fixed');
	}
	
	function closeChat() {
		if($dialog.dialog('isOpen') == true) $dialog.dialog('close');
		$btnChat.removeClass('ui-state-highlight').removeClass('ui-state-active');
		updateCookieData();
	}
	
	$btnChat.click(function(event) {
		event.stopPropagation(); //important! prevent $(doc).on click(.navigation)
		$btnChat.hasClass('ui-state-active') ? closeChat() : restoreStateFromCookies() ? false : openChat();
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
	
	var initial = true;
	function getChatters(loop) {
		var onlineChatters = getOnlineChatters();
		debug('Getting chatters. Online chatters: ' + onlineChatters);
		
		$.get(chat.urlGetChatters + '?time=' + Date.now(), {chatters: onlineChatters, initial: initial}, function(response) {
			switch(response.status) {
			case '200':
				if(response.channels) {
					for(var i = 0, len = response.channels.length; i < len; ++i) {
						addChannelOption(response.channels[i]);
						//channelOpen(response.channels[i], false);
					}
				}
				if(response.goneOffline) {
					for(var i = 0, len = response.goneOffline.length; i < len; ++i) {
						makeOffline(response.goneOffline[i]);
					}
				}
				if(response.usersubs) {
					for(var i = 0, len = response.usersubs.length; i < len; ++i) {
						makeOffline(response.usersubs[i]);
					}
				}
				if(response.goneOnline) {//must come after subs so that overlaps or Online
					for(var i = 0, len = response.goneOnline.length; i < len; ++i) {
						makeOnline(response.goneOnline[i]);
					}
				}
				if(initial) initial = false;
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
			$this.find('.chatter-notification').fadeOut('500', function(){$(this).text(' ').show()});
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
			
			if(!messageRetrievalLoopStarted) {
				debug('At least 1 channel is open. Starting message retrieval loop.');
				getMessages(true);
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
			updateCookieData();
		} else {
			$chatwindow.hide();
		}
	}
	
	function channelOpen(channel, switchTo) {
		var $chatwindow = findWindow(channel, true);
		
		if(switchTo) {
			$('.chatwindow').hide();
			$chatwindow.show();
			updateCookieData();
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
	var messageRetrievalLoopStarted = false;
	function getMessages(loop) {
		if(waiting) return;
		waiting = true;
		
		messageRetrievalLoopStarted = true;
		
		var channels = getOpenChannels();
		
		debug('Getting messages on channels ' + channels + ' with last received ID at ' + lastReceivedId);
		
		$.get(chat.urlGet, 
			{
				channels: channels,
				lastReceivedId : lastReceivedId
			},	
			function(messages){
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
			if(isBroadcast(message)) {
				$('<div class="chat-sender">').text(message.sender).appendTo($text);
			}
			$('<div class="chat-onemessage">').attr('messageId', message.id).html(processSmileys(message.message)).appendTo($text);
		} else {
			var $lastcontainer = $box.find('.chat-onemessage-container:last');
			$('<div class="chat-onemessage">').attr('messageId', message.id).html(processSmileys(message.message)).appendTo($lastcontainer);
		}

		$box.scrollTop($box[0].scrollHeight);
	}
	
	function isBroadcast(message) {
		return message.channel.indexOf('#') === 0;
	}
	
	//bind images and chat-sender names to open their windows
	$chatwindows.on({
		click: function(){
			var username = $(this).text();
			if(username) p2pOpen(username, true);
		}
	}, '.chat-sender');
	
	$chatwindows.on({
		click: function(){
			var username = $(this).parent().find('.chat-sender').text();
			if(username) p2pOpen(username, true);
		}
	}, '.chat-img');
	
	//smileys
	var 
		$smileys = $('.smileys-container'),
		$btnSmileys = $('.btn-smileys');
	var smileys = [
	               	//	{emoticon (string or string[]), smiley (string, must match class name)}
	               	// regexp ([.?*+^$[\]\\(){}|-])
	               		{em: [':D', ':-D'], sm: 'grin', click: ':D'},
	               		{em: ['B\\)', 'B-\\)'], sm: 'shades', click: 'B)'},
	               		{em: ':brick', sm: 'brick', click: ':brick'},
	               		{em: ':pray', sm: 'pray', click: ':pray'},
	               		{em: ':sick', sm: 'sick', click: ':sick'},
	               		{em: ':undercover', sm: 'undercover', click: ':undercover'},
	               		{em: ['T_T',':\'\\('], sm: 'cry2', click: ':\'('},
	               		{em: ['>.>', '<.<'], sm: 'leer', click: '>.>'},
	               		
	               		{em: '8\\)',  sm: 'google', click: '8)'},
	               		{em: ['>_>', '<_<'], sm: 'leer2', click: '>_>'},
	               		{em: ':\\)', sm: 'smile', click: ':)'},
	               		{em: ':hero', sm: 'spiderman', click: ':hero'},
	               		{em: '>:\\(', sm: 'angry', click: '>:('},
	               		{em: '":\\|', sm: 'embarrassed', click: '":|'},
	               		{em: ':kiss', sm: 'kiss', click: ':kiss'},
	               		{em: ':v', sm: 'wat', click: ':v'},
	               		
	               		{em: ':\\(', sm: 'cry', click: ':('},
	               		{em: ':steam', sm: 'steam', click: ':steam'}
	               ];
	
	function processSmileys(text) {
		for(var i = 0, len = smileys.length; i < len; ++i) {
			var smiley = smileys[i];
			var isMultiple = Array.isArray(smiley.em);
			
			if(isMultiple) {
				for(var j = 0, len2 = smiley.em.length; j < len2; ++j) {
					text = text.replace(new RegExp(smiley.em[j], 'g'), getSmileyCode(smiley.sm));
				}
			} else {
				text = text.replace(new RegExp(smiley.em, 'g'), getSmileyCode(smiley.sm));
			}
		}
		return text;
	}

	function getSmileyCode(sm) {
		var $dummy = $('<div>');
		var $box = $('<div class="smiley-box">').appendTo($dummy);
		$('<div class="smiley">').addClass(sm).appendTo($box);
		return $dummy.html();
	}
	
	function constructSmileyBox() {
		for(var i = 0, len = smileys.length; i < len; ++i) {
			var $box = $('<div class="smiley-box">').appendTo($smileys);
			$('<div class="smiley">').attr('click', smileys[i].click).addClass(smileys[i].sm).appendTo($box);
			if((i+1)%7 == 0 && i != 0) {
				$('<br>').appendTo($smileys);
			}
		}
	}
	constructSmileyBox();
	
	$btnSmileys.click(function(event){
		event.stopPropagation();
		var offset = $(this).offset();
		$smileys.show().offset(offset);
	});
	
	$('.smileys-container').on({
		mousedown: function(event) {
			event.stopPropagation();
		},
		click: function(event){
			var $this = $(this);
			var $smiley = $this.hasClass('smiley') ? $this : $this.find('.smiley');
			
			event.stopPropagation();
			var click = $smiley.attr('click');
			$iptMessage.val($iptMessage.val() + click);
			$iptMessage.focus();
			
			$smileys.hide();
		}
	}, '.smiley,.smiley-box');
	
	//appear offline
	var $chkAppearOffline = $('.chkAppearOffline');
	$chkAppearOffline.change(function(){
		$chkAppearOffline.attr('disabled', 'disabled');
		var checked = $chkAppearOffline.is(':checked');
		$.post(chat.urlAppearOffline + checked + '.json', function(response) {
			$chkAppearOffline.removeAttr('disabled');
			updateCookieData();
		});
	});
	
	//update cookies
	var cookieKey = "dgte-chat";
	function updateCookieData() {
		var off = $dialog.parent().offset();
		var offset = {left: off.left, top: off.top - $(window).scrollTop()};
		
		var data = {
			//open
			o: $dialog.dialog('isOpen') == true ? 't' : 'f',
			//active channel
			ac: findActiveChannel(),
			//is 'appear offline' ticked
			ao: $chkAppearOffline.is(':checked') == true ? 't' : 'f',
			
			//offset left and right (window relative)		
			l: offset.left,
			t: offset.top
		}
		
		var serialized = data.o + ':' + data.ac + ':' + data.ao + ':' + data.l + ':' + data.t;
		
		//var serialized = JSON.stringify(data);
		
		debug('Persisting cookie data: ' + serialized)
		
		$.cookie(
			//cookie name	
			cookieKey,
			
			//data
			serialized, 
			
			//options
			{
			   path    : '/',          
			}
		);
	}
	
	function restoreStateFromCookies() {
		var serialized = $.cookie(cookieKey);
		if(!serialized) return false;
		
		var splitted = serialized.split(':');
		var data = {
			o: splitted[0],
			ac: splitted[1],
			ao: splitted[2],
			l: splitted[3],
			t: splitted[4]	
		}
		
		//var data = JSON.parse(serialized);
		
		debug('open? ' + data.o);
		debug('active channel: ' + data.ac);
		debug('left: ' + data.l);
		debug('top: ' + data.t);
		debug('appearOffline? ' + data.ao);
		
		if(data.o === 't') {
			openChat(extractChatterFromChannel(data.ac), {left: data.l, top: data.t});
			$chkAppearOffline.attr('checked', data.ao === 't');
			return true;
		} else {
			return false;	
		}
	}
	restoreStateFromCookies();
	
	$(document).mousedown(function(){
		$smileys.hide();
	}).scroll(function(){
		$smileys.hide();
	});
});