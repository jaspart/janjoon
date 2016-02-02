function updateTabViewWidth() {

	var width = 0;
	if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 264;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 14;

	$("#versionTabView").css("maxWidth", width + "px");
}

$(window).resize(function() {updateTabViewWidth();});

$(document).ready(function() {updateTabViewWidth();});