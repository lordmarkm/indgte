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
			beforeClose: function(){updateCookieData(true);},
			close: closeChat,
			width: 500,
			dragStop: updateCookieData
		});
		$dialog.parent().offset(offset);
		$btnChat.removeClass('ui-state-highlight').addClass('ui-state-active');
		
		//setTimeout(function(){
		//	getChatters(true);
		//}, 500);
		
		if(persistentChannel) {
			p2pOpen(persistentChannel, true);
		} else {
			channelOpen('#dumaguete', true);
		}
		
		//getMessages(true);
	}
	
	function isChatOpen() {
		return $dialog.dialog('isOpen') === true;
	}
	
	function switchToChannel(channel, openIfClosed) {
		debug('Switching to channel ' + channel);
		
		var chatter = extractChatterFromChannel(channel);
		if(isChatOpen()) {
			p2pOpen(chatter, true);
		} else if(openIfClosed) {
			debug('Chat is closed. Opening chat..');
			openChat(chatter, getCookieData().offset);
		}
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
		$btnChat.hasClass('ui-state-active') ? closeChat() : (function() {
			debug('opening chat');
			var data = getCookieData();
			if(data) {
				debug('Will try to open channel ' + data.ac + ' and set offset ' + data.l + ',' + data.t);
				openChat(extractChatterFromChannel(data.ac), {left: data.l, top: data.t});
			} else {
				openChat();
			}
		})();
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
	var lastReceivedId = 0;
	function live() {
		
		var onlineChatters = getOnlineChatters();
		var channels = getOpenChannels();
		var lastNotifId = getLastNotifId();
		
		$.get(chat.urlLive + '?time=' + Date.now(),
				{
					chatters: onlineChatters,
					initial: initial,
					channels: channels,
					lastReceivedId : lastReceivedId,
					lastNotifId : lastNotifId
				},
				function(response) {
					switch(response.status) {
					case '200':
						//old getChannels(...)
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
						
						//old getMessages(...)
						if(response.messages) {
							if(typeof response.messages.reverse == 'function') response.messages.reverse();
							for(var i = 0, len = response.messages.length; i < len; ++i) {
								processMessage(response.messages[i], true);
							}
						}
						
						//notifs
						if(response.notifications) {
							processNotifications(response.notifications);
						}
						break;
					default:
						debug('No response. Will resend request.');
						debug(response);
					}
					
					initial = false;
					live();
				}
		);
	}
	live();
	
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
	
	var extractCache = {};
	function extractChatterFromChannel(channel) {
		var chatter = extractCache[channel];
		if(null != chatter) {
			return chatter;
		}
		
		var usernames = channel.split('|');
		chatter = chat.user;
		for(var n in usernames) {
			if(usernames[n] != chat.user) {
				chatter = usernames[n];
				break;
			}
		}
		extractCache[channel] = chatter;
		
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
			updateCookieData();
		} else {
			$chatwindow.hide();
		}
		
		scrollToBottom($chatwindow);
		$iptMessage.focus();
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
		$.post(chat.urlSendMessage, 
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
		var activeChattername = activeChannelName ? extractChatterFromChannel(activeChannelName) : '';
		
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
	
	function scrollToBottom($window) {
		var $textbox = $window.find('.chat-text');
		$textbox.scrollTop($textbox[0].scrollHeight);
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
	//set closing to true if updating cookie data right before closing the chat window
	function updateCookieData(closing) {
		var off = $dialog.parent().offset();
		var offset = {left: off.left, top: off.top - $(window).scrollTop()};
		
		var data = {
			//open
			o: closing ? 'f' : $dialog.dialog('isOpen') == true ? 't' : 'f',
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
	
	function getCookieData() {
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
		debug('got cookie data: ');
		debug('open? ' + data.o);
		debug('active channel: ' + data.ac);
		debug('left: ' + data.l);
		debug('top: ' + data.t);
		debug('appearOffline? ' + data.ao);
		
		return data;
	}
	
	function restoreStateFromCookies() {
		var data = getCookieData();
		
		if(data.o === 't') {
			openChat(extractChatterFromChannel(data.ac), {left: data.l, top: data.t});
			$chkAppearOffline.attr('checked', data.ao === 't');
			return true;
		} else {
			return false;	
		}
	}
	restoreStateFromCookies();
	
	//notifications
	var 
		$notifs = $('.notifications'),
		$oldNotifs = $('.old-notifications'),
		$linkShowOld = $('.link-showoldnotifs'),
		$linkClearOld = $('.link-clearoldnotifs'),
		$msgUptodate = $('.msg-uptodate'),
		$msgClearhistory = $('.msg-clearhistory');
	
	function getLastNotifId() {
		if(!$notifs || $notifs.length == 0) {
			$notifs = $('.notifications');
			if($notifs.length == 0) return -1;
		}
		
		var $lastNotif = $notifs.find('.notification:first-child');
		if($lastNotif.length == 0) return 0;
		
		return $lastNotif.attr('notifId');
	}
	
	function addNotification(notification) {
		$('.notification[notifId="' + notification.id + '"]').remove();
		var $notif = $('<li class="notification">').attr('notifId', notification.id);
		
		switch(notification.type) {
		case 'message':
			addMessageNotification(notification, $notif);
			break;
		case 'comment':
			addCommentNotification(notification, $notif);
			break;
		case 'like':
			addLikeNotification(notification, $notif);
			break;
		default:
			debug('Unsupported notif type: ' + notification.type);
		}
		
		$notif.prependTo($notifs).hide().fadeIn('slow');
	}
	
	function addMessageNotification(notification, $notif) {
		$('<img class="notifimg">').attr('src', notification.senderSummary.thumbnailHash).appendTo($notif);
		var $notiftxt = $('<div class="notiftxt">').html('<strong>' + notification.senderSummary.title + '</strong> has sent you ').appendTo($notif);
		var $linkShowmessages = $('<a class="notif-shownewmessages fatlink">').attr('href', 'javascript:;')
			.attr('channel', notification.channel)
			.text(notification.howmany > 1 ? notification.howmany + ' new messages.' : ' a new message.')
			.appendTo($notiftxt);
		var $footer = $('<div class="subtitle">').text(moment(notification.time).fromNow()).appendTo($notiftxt);
		var $clearnotif = $('<span class="clearnotif-container">').hide().appendTo($footer);
		$('<a class="clearnotif">').attr('href', 'javascript:;').text('Clear').appendTo($clearnotif);
	}
	
	function addCommentNotification(notification, $notif) {
		$('<img class="notifimg">').attr('src', 'http://graph.facebook.com/' + notification.lastCommenterId + '/picture').appendTo($notif);
		var $notiftxt = $('<div class="notiftxt">').html('<strong>' + presentNames(notification.commenters) + '</strong> commented on your ' + notification.commentableType + ' ').appendTo($notif);
		var url = '#';
		switch(notification.commentableType) {
		case 'post':
			url = chat.urlPost + notification.targetId;
			break;
		default:
			debug('unsupported commentable type: ' + notification.commentableType);
		}
		$('<a class="fatlink">').text(notification.targetTitle).attr('href', url).appendTo($notiftxt);
		
		var $footer = $('<div class="subtitle">').text(moment(notification.time).fromNow()).appendTo($notiftxt);
		var $clearnotif = $('<span class="clearnotif-container">').hide().appendTo($footer);
		$('<a class="clearnotif">').attr('href', 'javascript:;').text('Clear').appendTo($clearnotif);	
	}
	
	function addLikeNotification(notification, $notif) {
		$('<img class="notifimg">').attr('src', 'http://graph.facebook.com/' + notification.lastLikerId + '/picture').appendTo($notif);
		var names = presentNames(notification.likers);
		var $notiftxt = $('<div class="notiftxt">').html('<strong>' + names + '</strong> ' + (names.indexOf(' and ') == -1 ? 'likes' : 'like') + ' your ' + notification.likeableType + ' ').appendTo($notif);
		var url = '#';
		switch(notification.likeableType) {
		case 'post':
			url = chat.urlPost + notification.targetId;
			break;
		default:
			debug('unsupported interactable type: ' + notification.likeableType);
		}
		$('<a class="fatlink">').text(notification.targetTitle).attr('href', url).appendTo($notiftxt);
		
		var $footer = $('<div class="subtitle">').text(moment(notification.time).fromNow()).appendTo($notiftxt);
		var $clearnotif = $('<span class="clearnotif-container">').hide().appendTo($footer);
		$('<a class="clearnotif">').attr('href', 'javascript:;').text('Clear').appendTo($clearnotif);	
	}

	$notifs.on({
		click: function(){
			clearNotif($(this).closest('.notification'));
		}
	}, '.clearnotif');
	
	function presentNames(names) {
		var arr = names.split(',');
		
		if(arr.length == 1) {
			return arr[0];
		} else if(arr.length == 2) {
			return arr[0] + ' and ' + arr[1];
		} else if(arr.length == 3) {
			return arr[0] + ', ' + arr[1] + ' and ' + arr[2];
		} else {
			return arr[0] + ', ' + arr[1] + ' and ' + (arr.length - 2) + ' others';
		}
	}
	
	//actions available to notifs (both new and old)
	//differentiate on the handler method with attr 'cleared'='cleared'
	$('.hasnotifs').on({
		click: function(){
			var $this = $(this);
			var channel = $this.attr('channel');
			switchToChannel(channel, true);
			clearNotif($this.closest('li'));
		}
	}, '.notif-shownewmessages');
	
	function showMsgUptodateIfAppropriate() {
		var notifsleft = $notifs.find('.notification').length;
		if(notifsleft == 0) {
			$msgUptodate.fadeIn();
		} else {
			debug('Still ' + notifsleft + ' notifs left');
		}		
	}
	
	$notifs.on({
		mouseenter: function(){
			$(this).find('.clearnotif-container').css('display', 'inline-block');
		},
		mouseleave: function(){
			$(this).find('.clearnotif-container').css('display', 'none');
		}
	}, '.notification');
	
	function clearNotif($li) {
		if($li.attr('cleared') == 'cleared') return;
		$.post(chat.urlClearNotif + $li.attr('notifId') + '/json', function(response) {
			switch(response.status) {
			case '200':
				$li.attr('cleared', 'cleared').fadeOut(1000, function(){
					$msgClearhistory.hide();
					$li.prependTo($oldNotifs);
					if($oldNotifs.is(':visible')) {
						$li.dgteFadeIn();
					}
					showMsgUptodateIfAppropriate();
				});
				break;
			default:
				debug('problem clearing notif');
				debug(response);
			}
		});
	}
	
	function processNotifications(notifications) {
		if(!$notifs) {
			$notifs = $('.notifications');
			if(!$notifs) return;
		}
		
		var len = notifications.length;
		if(len > 0) $msgUptodate.hide();

		for(var i = 0; i < len; ++i) {
			addNotification(notifications[i]);
		}
	}
	
	var oldNotifsStart = 0;
	var oldNotifsPerLoad = 5;
	var showOldNotifsEnabled = true;
	$linkShowOld.click(function(){
		debug('loading old notifs...');
		
		if(!showOldNotifsEnabled) return false;
		showOldNotifsEnabled = false;
		
		var $container = $oldNotifs.parent().spinner(true);
		
		$.get(chat.urlGetOldNotifs + oldNotifsStart + '/' + oldNotifsPerLoad + '/json?time=' + Date.now(), function(response) {
			switch(response.status) {
			case '200':
				$oldNotifs.parent().show();
				
				if(oldNotifsStart === 0 && response.notifs.length === 0) {
					//empty notification history
					$msgClearhistory.fadeIn();
					$linkShowOld.fadeOut();
				} else {
					$msgClearhistory.fadeOut();
					$linkClearOld.show();
					
					for(var i = 0, len = response.notifs.length; i < len; ++i) {
						addOldNotif(response.notifs[i]);
					}
					
					if(response.notifs.length >= oldNotifsPerLoad) {
						$linkShowOld.text('Load more');
					} else {
						$linkShowOld.fadeOut();
					}
					
					oldNotifsStart += response.notifs.length;
				}
				
				break;
			default:
				debug('error getting old notifs');
				debug(response);
			}
			showOldNotifsEnabled = true;
		}).complete(function(){
			$container.fadeSpinner();
		});
	});
	
	function addOldNotif(notification) {
		var $notif = $('<li class="notification">')
			.attr('cleared', 'cleared')
			.attr('notifId', notification.id);
		
		switch(notification.type) {
		case 'message':
			addMessageNotification(notification, $notif);
			break;
		case 'comment':
			addCommentNotification(notification, $notif);
			break;
		case 'like':
			addLikeNotification(notification, $notif);
			break;
		default:
		}
		
		$notif.appendTo($oldNotifs).hide().fadeIn('slow');
	}
	
	$linkClearOld.click(function(){
		var notifIds = [];
		
		var $clearedNotifs = $('.notification[cleared="cleared"]');
		$clearedNotifs.each(function(i, li) {
			notifIds.push($(li).attr('notifId'));
		});
		
		if(notifIds.length === 0) return false;
		
		debug('Deleting notifs with ids in ' + notifIds);
		
		$.post(chat.urlDeleteNotifs, {notifIds: notifIds}, function(response) {
			switch(response.status) {
			case '200':
				debug('clearing...');
				$clearedNotifs.fadeOut(1000, function(){
					$(this).remove();
				});
				break;
			default:
				debug('Error deleting notifs');
				debug(response);
			}
			
			debug('about to retrieve old notifs after clearing visible ones...');
			oldNotifsStart = 0;
			$linkShowOld.click(); //after displayed notifs are cleared, load the older ones
		});
	});
	
	$(document).mousedown(function(){
		$smileys.hide();
	}).scroll(function(){
		$smileys.hide();
	});
});