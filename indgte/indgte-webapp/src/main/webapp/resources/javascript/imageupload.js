window.imageupload = {
	//from http://paulrouget.com/miniuploader/
	function upload(file, notifyUrl) {
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", "${imgurKey}");
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	var response = JSON.parse(xhr.responseText);
	        
	        //notify indgte that profilepic has changed
	        $.get(response.upload.links.small_square);
	        setTimeout(function(){
	        	$.post(notifyUrl, 
		        		{
		        			hash: response.upload.image.hash,
		        			deletehash: response.upload.image.deletehash
		        		}
		        		, function(){
		        	//only now do we update the user's profile pic
	        		window.location.replace(urlProfileRoot + domain);
		        });
	        }, 2000);
	    }
		xhr.send(fd);
	}
}