$(function(){
	//WARNING: Owner-only script!
	var businessDomain = business.domain,
		categoryName = category.name,
		categoryId = category.id;
	
	var categoryPic = category.imageUrl,
		categoryPicImgur = category.categoryPicImgur,
		owner = category.owner;

	var urlSpinner = urls.spinner,
		urlCategories = urls.categories,
		urlNewProduct = urls.newproduct;	
		
	var $categoryPicContainer = $('.category-welcome-image-container'),
		$categoryPic = $('.category-welcome-image'),
		$btnCreateProduct = $('.btn-create-product'),
		$createProduct = $('.newproduct');
	
	var $upload = $('#image-upload'),
		$file = $('.image-upload-file'),
		$newProductName = $('.newproduct-name'),
		$newProductDescription = $('.newproduct-description'),
		$btnNewproductPic = $('.btn-newproduct-pic'),
		$newproductPicContainer = $('.newproduct-pic-container'),
		$newproductPicDisplay = $('.newproduct-pic-display'),
		$newproductPicField = $('.newproduct-pic'),
		$newproductHashField = $('.newproduct-pic-hash'),
		$newproductDeletehashField = $('.newproduct-pic-deletehash');
	
	var $btnDelete = $('.btn-delete');
	
	//override jquery ui dialog defaults
	$.extend($.ui.dialog.prototype.options, {
	    modal: true,
	    resizable: false,
	    maxHeight: 250,
	    width:500,
		closeOnEscape: false,
		hide: {effect: "fade", duration: 200}
	});
	
	//update category main pic
	$categoryPic.unbind('click').click(function(){
		$upload.dialog({
			title: messages.uploadtitle + dgte.htmlDecode(categoryName),
			buttons: {
				'Upload' : function(){
					$upload.find('div').toggle();
					$categoryPicContainer.append($('<div class="overlay">'));
					$upload.dialog('close');
					//upload($file[0].files[0], urlCategories + businessDomain + '/' + categoryId + '/mainpic/', $categoryPic);
					
					var updatePhoto = function(response) {
						$.post(urlCategories + businessDomain + '/' + categoryId + '/mainpic/',
								{
									hash: response.upload.image.hash,
		        					deletehash: response.upload.image.deletehash
		        				},
								function(){
									$categoryPic.attr('src', response.upload.links.large_thumbnail);
									$categoryPicContainer.find('.overlay').remove();
									$upload.find('div').toggle();
								}
						);
					}
					upload($file[0].files[0], updatePhoto, $categoryPic);
				},
				
				'Cancel' : function(){
					$upload.dialog('close');
				}
			}
		});
		return false;
	});
	
	//create new product
	$btnCreateProduct.click(function(){
		$createProduct.dialog({
			title: messages.newproducttitle + dgte.htmlDecode(category.name),
			width: 600,
			buttons: {
				'Save' : function(){
					var hash = $newproductHashField.val();
					var data = JSON.stringify({
						name: $newProductName.val(),
						description: $newProductDescription.val(),
						mainpic: hash ? {
							hash: hash,
							deletehash: $newproductDeletehashField.val(),
							uploaded: new Date()
						} : null
					});
					debug('data: ' + data);
					$.ajax({
							url: urlNewProduct + businessDomain + '/' + categoryId + '.json', 
							type: 'post',
							data: data, 
							success: function(response){
								switch(response.status) {
								case '200':
									products.addProduct(response.product, true);
									$createProduct.dialog('close');
									break;
								default:
									debug(response);
								}
							},
							dataType: 'json',
							contentType: 'application/json'
					});
				},
				'Cancel' : function() {
					$createProduct.dialog('close');
				}
			}
		});
	});
	
	$btnNewproductPic.click(function(){
		$newproductPicField.focus().trigger('click');
	});
	$newproductPicDisplay.click(function(){
		$newproductPicField.focus().trigger('click');
	})
	
	$newproductPicField.change(function(){
		$newproductPicContainer.append($('<div class="overlay">'));
		var productFieldOnComplete = function(response){
			$newproductHashField.val(response.upload.image.hash);
			$newproductDeletehashField.val(response.upload.image.deletehash);
			$newproductPicDisplay.attr('src', response.upload.links.large_thumbnail);
			$newproductPicContainer.find('.overlay').remove();
		}
		upload($newproductPicField[0].files[0], productFieldOnComplete, $newproductPicDisplay);
	});
	
	function upload(file, onComplete, $target) {
	    if (!file || !file.type.match(/image.*/)) return;

	    var fd = new FormData();
	    fd.append("image", file);
	    fd.append("key", constants.imgurKey);
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "http://api.imgur.com/2/upload.json");
	    xhr.onload = function() {
	    	var response = JSON.parse(xhr.responseText);
	    	onComplete(response);
	    }
		xhr.send(fd);		
	}
	
	//edit
	var 
		$btnEdit = $('.btn-edit'),
		$dlgEdit = $('.dialog-edit'),
		$formEdit = $('#form-edit');
	
	$formEdit.validate({
		rules: {
			name: {
				required: true,
				rangelength: [1, 45]
			},
			description: {
				required: true,
				rangelength: [1, 140]
			}
		}
	});
	$btnEdit.click(function() {
		$dlgEdit.dialog({
			buttons: {
				'Save': function(){
					if($formEdit.valid()) {
						$formEdit.submit();
					}
				},
				
				'Cacnel': function(){
					$(this).dialog('close');
				}
			}
		});
	});
	
	//delete
	$btnDelete.click(function() {
		var $deleteDialog = $('<div>')
			.attr('title', 'Really delete this category?')
			.html('Are you sure you want to delete ' + $('.category-name').text() + '? This cannot be undone. <strong>This will also delete all products in this category.</strong>')
			.dialog({
				buttons : {
					'Yep': function(){
						$.post(urls.deleteCategory + category.id + '/json', function(response) {
							switch(response.status) {
							case '200':
								dgte.operationSuccess('Category deleted.', false, function(){
									$deleteDialog.dialog('close');
									window.location.replace(category.urlParent);
								});
								break;
							case '500':
								dgte.operationFailed(response.message);
								$deleteDialog.dialog('close');
								break;
							default:
								dgte.operationFailed();
								$deleteDialog.dialog('close');
							}
						}).error(function(){
							dgte.operationFailed();
							$deleteDialog.dialog('close');
						});
					},
					
					'Not really, no': function(){
						$(this).dialog('close');
					}
				}
			});
	});
});