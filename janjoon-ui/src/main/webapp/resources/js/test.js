function updateDataTableContainerWidth() {

	var table=$("#testFormCenter\\:dataTableContainer");
	var width = 0;
	if ($(window).width() > 1200)
		width = 70*($("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 264)/100;
	else if($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 264;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 14;

	table.css("maxWidth", width + "px");
	updateTable();
}

$(window).resize(function() {updateDataTableContainerWidth();});

$(document).ready(function() {updateDataTableContainerWidth();});