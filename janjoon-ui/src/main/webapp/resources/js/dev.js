function updateTabViewWidth() {
	
		var table = $("#form\\:tabView");
		var width = 0;
		if ($(window).width() > 1200)
			width = (60 * ($("#layout-topbar").width()
					- (0.1 / 100 * $("#layout-topbar").width()) - 264) / 100) - 20;
		else if ($(window).width() > 960)
			width = $("#layout-topbar").width()
					- (0.1 / 100 * $("#layout-topbar").width()) - 264 - 20 - 15 ;
		else
			width = $("#layout-topbar").width()
					- (2 / 100 * $("#layout-topbar").width()) - 14 - 20 - 15;

		table.css("maxWidth", width + "px");	
	
}

$(window).resize(function() {
	updateTabViewWidth();
});

$(document).ready(function() {
	updateTabViewWidth();
});