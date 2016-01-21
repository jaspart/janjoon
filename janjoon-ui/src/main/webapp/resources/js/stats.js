function updateTabViewWidth() {

	var width = 0;
	if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 288;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 38;

	$("#statsTabView\\:SprintTabView").css("maxWidth", width + "px");
	
	var chart = $(".barChart:visible").last();

	if (chart != null) {
		var value = chart.attr("id");

		if (PF('' + value + '_Widget') != null)
			PF('' + value + '_Widget').plot.replot();
	}

}

$(window).resize(function() {updateTabViewWidth();});

$(document).ready(function() {updateTabViewWidth();});