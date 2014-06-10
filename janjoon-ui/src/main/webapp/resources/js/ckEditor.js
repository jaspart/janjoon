CKEDITOR.editorConfig = function(config) {
 var contextPath = $(".contextPath").html();
 config.filebrowserImageUploadUrl =contextPath+"/pages/ckeditor/uploadimage"; 
};