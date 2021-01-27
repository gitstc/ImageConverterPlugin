var ImageConverterPlugin = {
	getBase64: function(imagePath,successCB,errorCB){        
        if(imagePath.indexOf("data:image/") > -1){
            successCB(imagePath);
            return;
        }
		cordova.exec(successCB,errorCB,"ImageConverterPlugin","getBase64Image",[imagePath]);
	}
};