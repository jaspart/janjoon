function updateTabViewWidth() {

	var width = 0;
	if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 288;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 38;

	$("#statsTabView\\:SprintTabView").css("maxWidth", width + "px");
	$("#statsTabView\\:SprintTabView").css("display", "block");

	if ($(".barChart:visible").last().length != 0) {
		updateBarChart();
	}

	if ($(".bugTab:visible").last().length != 0) {
		updateBugTab();
	}
	
	if ($(".kpiTab:visible").last().length != 0) {
		updateKpiTab();
	}

}

$(window).resize(function() {
	updateTabViewWidth();
});

$(document).ready(function() {
	updateTabViewWidth();
});