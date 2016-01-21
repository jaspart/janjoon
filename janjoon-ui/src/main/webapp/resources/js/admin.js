function updateTabViewWidth() {

	var width = 0;
	if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 274;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 24;

	$("#tabview").css("maxWidth", width + "px");
}

$(window).resize(function() {updateTabViewWidth();});

$(document).ready(function() {updateTabViewWidth();});