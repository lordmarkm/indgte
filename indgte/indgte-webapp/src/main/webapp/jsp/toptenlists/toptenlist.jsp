<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../tiles/links.jsp" %>

<title>Top Ten Lists In Dumaguete</title>
<script type="text/javascript" src="${jsApplication }"></script>

<div class="grid_8">

<section class="list-details-head">
	<h1 class="list-title">${topten.title }</h1>
	<div class="list-description embedded-images-200">${topten.description }</div>
	<div class="list-editdescription-container hide">
		<textarea class="list-editdescription-box" rows="4" cols="80">${topten.description }</textarea>
		<a href="javascript:;" class="list-editdescription-save"><spring:message code="generics.save" /></a>
		<a href="javascript:;" class="list-editdescription-cancel"><spring:message code="generics.cancel" /></a>
	</div>
	<c:if test="${topten.creator.username eq user.username }">
		<a href="javascript:;" class="list-editdescription">Edit Description</a>
		<br />
	</c:if>
	<div class="list-message subtitle"><spring:message code="toptens.head.message" /></div>
</section>

<section class="candidates">
	<ul>
		<c:forEach items="${topten.ordered }" var="candidate" varStatus="index">
		<c:if test="${index.index <= 9 }">
		<li class="candidate-container" candidateId="${candidate.id }">
			<div class="rank ui-widget-header ui-corner-all">${index.index + 1}</div>
			<c:if test="${not empty candidate.attachment }">
				<div class="candidate-title">${candidate.attachment.name }</div>
				<div class="link-vote" candidateId="${candidate.id }"></div>
				<div class="clear"></div>
				<img class="candidate-img" src="${candidate.attachment.imgur.largeThumbnail }" />
				<div class="candidate-description embedded-images-200">${candidate.attachment.description }</div>
			</c:if>
			<c:if test="${empty candidate.attachment}">
				<div class="candidate-title">${candidate.title }</div>
				<div class="link-vote" candidateId="${candidate.id }"></div>
				<div class="candidate-description embedded-images-200"></div>
			</c:if>
			<div class="candidate-votes">
				<c:set var="votes" value="${fn:length(candidate.voters) }" />
				<span class="candidate-votes">${votes } vote<c:if test="${votes != 1 }">s</c:if></span>
			</div>
			<c:if test="${candidate.creator.username eq user.username && (candidate.attachmentType eq 'imgur' || candidate.attachmentType eq 'none') }">
				<div class="candidate-footer">
					<span class="candidate-attachimage-container">
						<a class="candidate-attachimage" href="javascript:;">Attach Image</a>
					</span>
					<div class="candidate-newdescription-container hide">
						<textarea class="candidate-newdescription" cols="70">${candidate.attachment.description }</textarea>
					</div>
					<span class="candidate-savedescription-container hide">
						<a class="candidate-savedescription" href="javascript:;">Save Description</a>
						<a class="candidate-canceleditdescription" href="javascript:;"><spring:message code="generics.cancel" /></a>
					</span>
					
					<c:choose>
						<c:when test="${candidate.attachmentType eq 'imgur' }">
							<span class="candidate-adddescription-container">						
						</c:when>
						<c:otherwise>
							<span class="candidate-adddescription-container hide">
						</c:otherwise>
					</c:choose>
						<a class="candidate-adddescription embedded-images" href="javascript:;">Edit Description</a>
					</span>
				</div>
			</c:if>
		</li>
		</c:if>
		</c:forEach>
	</ul>
</section>

<section class="list-details-foot">
	<div class="list-totalVotes">${topten.totalVotes } Total votes</div>
	<div class="list-creator">
		<div class="list-creator-img"><img src="${topten.creator.imageUrl }" /></div>
		<div class="list-creator-name">${topten.creator.username }</div>
	</div>
</section>

<section class="newcandidate">
Add a new option
	<div class="newcandidate-form-container">
		<form id="newcandidate-form">
			List title: <input type="text" class="newcandidate-title" />
			<input type="submit" />
		</form>
	</div>
</section>

</div>

<!-- Image upload -->
<div id="image-upload" style="display: none;" title="<spring:message code="generics.imageupload.title" />">
	<div class="image-upload-message"></div>
	<br/>
	<div class="form-pic">
		<div class="image-upload-dropbox">
			<span class="dropbox-message"><spring:message code="generics.imageupload.dropbox.message" /></span>
		</div>
		<input type="file" class="image-upload-file" style="visibility: collapse; width: 0px;" />
	</div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/imageupload.css' />" />
<script src="<spring:url value='/resources/javascript/grids/imageupload.js' />"></script>

<style>
.list-title {
	text-transform: capitalize;
	font-size: 2em;
}
.list-message {
	margin: 10px 0 0 0;
}
.candidates ul {
	list-style-type: none;
}
.candidates li {
	margin: 0 0 15px 0;
	padding: 10px;
	position: relative;
}
.candidates li:not(.ui-state-active) {
	border-bottom: 1px solid #e9e9e9;
}
.candidates li.ui-state-active {
	background-image: none;
	background-color: white;
}
.candidates li.ui-state-active a {
	text-decoration: underline;
	margin: 0 3px;
}
.rank.ui-widget-header {
	font-size: 1.4em;
	width: 20px;
	height: 20px;
	display: inline-block;
	padding: 10px;
	text-align: center;
}
.candidate-title {
	display: inline-block;
	font-size: 1.4em;
	font-weight: bold;
	text-transform: capitalize;
}
.link-vote {
	position: absolute;
	right: 10px;
	top: 10px;
	height: 53px;
	width: 40px;
	cursor: pointer;
	background-image: url("<spring:url value='/resources/images/icons/vote-icon.jpg' />");
}
.link-vote:hover {
	background-position: 40px 0;
}
.candidate-img {
	margin: 10px;
	max-height: 300px;
	max-width: 90%;
    /*max-width: 400px; allow panoramic pics */
}
.candidate-description {
	margin-left: 10px;
}

.candidate-votes {
	font-weight: bolder;
	font-size: 1.1em;
	margin: 5px;
}
	
.candidate-newdescription {
	overflow: hidden;
	resize: none;
}

.candidate-footer {
	margin-left: 10px;
}
</style>

<script>
window.urls = {
	toptens : '<spring:url value="/i/toptens/" />',
	attach : '<spring:url value="/i/toptens/attach/" />',
	adddescription : '<spring:url value="/i/toptens/description/" />',
	listdescription : '<spring:url value="/i/toptens/listdescription/" />',
	
	//grids
	business : '<spring:url value="/p/" />',
}

window.topten = {
	id : '${topten.id}',
	userVoted : '${userVoted}'
}

$(function(){
	var $newcandidateform = $('#newcandidate-form'),
		$iptNewcandidateTitle = $('.newcandidate-title'),
		$upload = $('#image-upload'),
		$uploadMessage = $('.image-upload-message');
	
	$newcandidateform.submit(function(){
		var $this = $(this);
		//if(!$this.valid()) return;		
		
		$.post(urls.toptens + topten.id + '.json', {title: $iptNewcandidateTitle.val()}, function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug(response);
			}
		});
		
		return false;
	});
	
	if(topten.userVoted) {
		$('li[candidateId="' + topten.userVoted + '"]').addClass('ui-state-active')
			.find('.link-vote').remove();
	}
	
	//add description (list)
	var $listDescription = $('.list-description'),
		$listEditDescription = $('.list-editdescription'),
		$editDescriptionContainer = $('.list-editdescription-container'),
		$boxListDescription = $('.list-editdescription-box'),
		$linkListDescriptionSave = $('.list-editdescription-save'),
		$linkListDescriptionCancel = $('.list-editdescription-cancel');
	
	$boxListDescription.val(unescapeBrs2($boxListDescription.html()));
	$listEditDescription.click(function(){
		$(this).hide();
		$editDescriptionContainer.show();
		resizeVertical($boxListDescription);
	});
	
	$linkListDescriptionSave.click(function(){
		$.post(urls.listdescription + topten.id + '.json', {description: $boxListDescription.val()}, function(response) {
			switch(response.status) {
			case '200':
				$listDescription.html(response.description);
				break;
			default:
				debug('error saving list description');
				debug(response);
			}
		}).complete(function(){
			$editDescriptionContainer.hide();
			$listEditDescription.show();
		});
	});
	
	$linkListDescriptionCancel.click(function(){
		$editDescriptionContainer.hide();
		$listEditDescription.show();
	});
	
	//vote
	$('.link-vote').click(function(){
		var candidateId = $(this).attr('candidateId');
		$.post(urls.toptens + topten.id + '/' + candidateId + '.json', function(response) {
			switch(response.status) {
			case '200':
				window.location.reload();
				break;
			default:
				debug(response);
			}
		});
	});
	
	//attach image
	var $activeLi, 
		$linkAttachImage = $('.candidate-attachimage'),
		$dropbox = $('.image-upload-dropbox');
	
	function uploadCallback(imgurResponse) {
		var candidateId = $activeLi.attr('candidateId');
		$.post(urls.attach + candidateId + '.json', {
				hash: imgurResponse.upload.image.hash, 
				deletehash: imgurResponse.upload.image.deletehash,
				title: imgurResponse.upload.image.title,
				description: imgurResponse.upload.image.caption
			},
			function(response) {
				switch(response.status) {
				case '200':	
					$activeLi.find('.candidate-img').remove();
					
					var $voteLink = $activeLi.find('.link-vote');
					var $clear = $('<div class="clear">').insertAfter($voteLink);
					var $img = $('<img class="candidate-img">')
						.attr('src', dgte.urls.imgur + imgurResponse.upload.image.hash + 'l.jpg')
						.insertAfter($clear);
					$activeLi.find('.candidate-adddescription-container').show();
					break;
				default:
					debug('Error uploading img data');
					debug(response);
				}
			}
		).complete(function(){
			$upload.dialog('close');
		});
	}
	
	$linkAttachImage.click(function(){
		$activeLi = $(this).closest('li');
		$uploadMessage.html('You will be uploading an image for <strong>' + $activeLi.find('.candidate-title').text() + '</strong>');
		$upload.dialog({
			buttons: {
				'<spring:message code="generics.cancel" />' : function(){
					$upload.dialog('close');
				}
			}
		});
	});
	
	//drop
	$dropbox.on('drop', function(e){
		e.preventDefault();
		var $this = $(this);
		if($this.hasClass('waiting')) return;
		dgte.upload(e.originalEvent.dataTransfer.files[0], uploadCallback, $activeLi.find('.candidate-title').text());
	});
	
	//click
	$('.image-upload-file').change(function() {
		$dropbox.removeClass('hungry')
		.append($('<div class="overlay">'))
		.addClass('waiting');
		
		dgte.upload(this.files[0], uploadCallback, $activeLi.find('.candidate-title').text());
	});
	
	//add description (candidate)
	var $linkAddDescription = $('.candidate-adddescription'),
		$textboxDescription = $('.candidate-newdescription'),
		$linkSaveDescription = $('.candidate-savedescription'),
		$linkCancelEditDescription = $('.candidate-canceleditdescription');
	
	$textboxDescription.each(function(i, box) {
		var $box = $(box);
		var html = unescape($box.html());
		$box.val(unescapeBrs2(html));
	});
	
	$linkAddDescription.click(function(){
		var $parent = $(this).closest('.candidate-footer');
		$parent
			.find('.candidate-adddescription-container').hide().end()
			.find('.candidate-attachimage-container').hide().end()
			.find('.candidate-newdescription-container').show().end()
			.find('.candidate-savedescription-container').show();
		
		resizeVertical($parent.find('.candidate-newdescription'));
	});
	
	$linkSaveDescription.click(function(){
		var $activeLi = $(this).closest('li');
		$.post(urls.adddescription + $activeLi.attr('candidateId') + '.json', {description: $activeLi.find('.candidate-newdescription').val()}, function(response) {
			switch(response.status) {
			case '200':
				$activeLi
					.find('.candidate-adddescription-container').show().end()
					.find('.candidate-attachimage-container').show().end()
					.find('.candidate-newdescription-container').hide().end()
					.find('.candidate-savedescription-container').hide().end()
					.find('.candidate-description').html(response.imgur.description);
				break;
			default:
				debug('error adding description.');
				debug(response);
			}
		});
	});
	
	$linkCancelEditDescription.click(function(){
		$(this).closest('li')
			.find('.candidate-adddescription-container').show().end()
			.find('.candidate-attachimage-container').show().end()
			.find('.candidate-newdescription-container').hide().end()
			.find('.candidate-savedescription-container').hide();
	});
	
	
	//resize description textboxes
	$(document).on({
		keyup: function(){
			resizeVertical($(this));
		},
		paste: function(){
			resizeVertical($(this));
		}
	}, '.candidate-newdescription,.list-editdescription-box');
	
	function resizeVertical(box) {
		var content = box.val();
		var cols = box.attr('cols');
		var linecount = 0;
		var splitcontent = content.split('\n');
		for(var i = 0, length = splitcontent.length; i < length; ++i) {
			linecount += 1 + Math.floor(splitcontent[i].length / cols);
		}
		if(linecount < 4) linecount = 4;
		box.attr('rows', linecount);
	}
});
</script>

<!-- Top Tens -->
<div class="toptens-container grid_4 sidebar-section">
<div class="sidebar-section-header">Other Top Tens</div>
	<div class="toptens">
		Popular:
		<ul class="popular"></ul>
		Recent:
		<ul class="recent"></ul>
		
		<a href="<spring:url value='/i/toptens/' />">View all...</a>
	</div>
	<div class="sidebar-divider"></div>
</div>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/toptens.css' />" />
<script>
window.urls.topTens = '<spring:url value="/i/toptens.json" />',
window.urls.topTenLeader = '<spring:url value="/i/toptens/leader/" />',
window.urls.topTensPage = '<spring:url value="/i/toptens/" />'
</script>
<script src="${jsTopTens }"></script>
<!-- End Top Tens -->

<!-- Reviews -->
<div class="reviewqueue grid_4 sidebar-section">
	<div class="sidebar-section-header">Recently Viewed Business for Review</div>
	<div class="review-container">
		<div>Please take some time to review the businesses below if you have completed any transactions with them or have knowledge of their operations.</div>
		<ul class="reviewlist"></ul>
	</div>
	<div class="sidebar-divider"></div>
</div>
<script>
window.urls.reviewqueue = '<spring:url value="/i/reviewqueue.json" />',
window.urls.noreview = '<spring:url value="/i/noreview/" />',
window.urls.neverreview = '<spring:url value="/i/neverreview/" />'
</script>
<script src="${jsReviewQueue }"></script>
<link rel="stylesheet" href="<spring:url value='/resources/css/grids/reviewqueue.css' />" />
<!-- End Reviews -->