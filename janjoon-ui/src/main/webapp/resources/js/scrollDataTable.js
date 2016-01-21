var scrollTop = 0;
var scrollLeft = 0;
var row = null;
var column = null;
var isReal = false;
var isRevised1 = false;
var timelineContentIndex = null;


function updateTabViewWidth() {

	var width = 0;
	if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 288;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 38;

	$("#projecttabview\\:SprintTabView").css("maxWidth", width + "px");

	var $chart = $(".barChart:visible").last();

	if ($chart != null) {
		var value = $chart.attr("id");

		if (PF('' + value + '_Widget') != null)
			PF('' + value + '_Widget').plot.replot();
	}
	

}

$(window).resize(function() {
			var width = 0;
			if ($(window).width() > 960)
				width = $("#layout-topbar").width()
						- (0.1 / 100 * $("#layout-topbar").width()) - 288;
			else
				width = $("#layout-topbar").width()
						- (2 / 100 * $("#layout-topbar").width()) - 38;

			$("#projecttabview\\:SprintTabView").css("maxWidth", width + "px");
		});

$(document).ready(function() {
			var width = 0;
			if ($(window).width() > 960)
				width = $("#layout-topbar").width()
						- (0.1 / 100 * $("#layout-topbar").width()) - 288;
			else
				width = $("#layout-topbar").width()
						- (2 / 100 * $("#layout-topbar").width()) - 38;

			$("#projecttabview\\:SprintTabView").css("maxWidth", width + "px");
		});

function saveScrollPos() {
	var area = $("#projecttabview\\:planningForm\\:westLayout").children()
			.eq(1);
	scrollTop = area.prop("scrollTop");
	scrollLeft = area.prop("scrollLeft");
	// $("#formId\\:scrollPos").value = scrollTop;
}

function autoScroll() {
	// var scrollPos = $("#formId\\:scrollPos").value;
	var area = $("#projecttabview\\:planningForm\\:westLayout").children()
			.eq(1);
	area.animate({
		scrollTop : scrollTop
	}, scrollTop);

	area.animate({
		scrollLeft : scrollLeft
	}, scrollLeft);

	// area.effect("highlight", {}, 3000);
	if (row != null && column != null) {
		var highLitedRow = $(
				"#projecttabview\\:planningForm\\:taskDataTable_data")
				.children().eq(row).children().eq(column);
		if (highLitedRow != null) {

			highLitedRow.addClass("highLightRow");

			setTimeout(function() {
				highLitedRow.removeClass("highLightRow");
			}, 2000);
		}
	}
	scrollTop = 0;
	scrollLeft = 0;
	row = null;
	column = null;
}

function getTimelineEvent() {
	var timelineEvent = $(".timeline-event-selected.ui-state-active");
	// timelineEventIndex = timelineEvent.index();
	timelineContentIndex = timelineEvent.parent().index();
	isReal = timelineEvent.hasClass("real");
	if (!isReal)
		isRevised1 = timelineEvent.hasClass("revised1")
				|| timelineEvent.hasClass("planned1");
	else
		isRevised1 = false;
}

function highlightEvent() {

	if (timelineContentIndex != -1) {
		var highLitedRow;
		var className;
		var highlightClass;
		if (isReal) {
			highLitedRow = $(".timeline-content").children().eq(
					timelineContentIndex).find(".real").last();
			className = "real";
			highlightClass = "highLightReal";
		} else if (isRevised1) {
			highLitedRow = $(".timeline-content").children().eq(
					timelineContentIndex).find(".revised1").last();

			className = "revised1";
			highlightClass = "highLightRevised1";
		} else {
			highLitedRow = $(".timeline-content").children().eq(
					timelineContentIndex).find(".revised2").last();

			className = "revised2";
			highlightClass = "highLightRevised2";
		}

		if (highLitedRow != null) {

			highLitedRow.removeClass(className);
			highLitedRow.addClass(highlightClass);

			setTimeout(function() {
				highLitedRow.removeClass(highlightClass);
				highLitedRow.addClass(className);
			}, 1000);
		}
	}

	timelineContentIndex == -1;
	isReal = false;
	isRevised1 = false;

}

function setRowIndex(rowIndex) {
	var highLitedRow = $("#projecttabview\\:planningForm\\:taskDataTable_data");

	var i = 0;

	while (i <= rowIndex) {

		if (highLitedRow.children().eq(i).hasClass(
				"ui-datatable-summaryrow ui-widget-header"))
			rowIndex++;
		i++;
	}

	highLitedRow = highLitedRow.children().eq(rowIndex);
	row = rowIndex;

	var max = highLitedRow.children().length;
	i = 0;
	while (i < max) {

		if (highLitedRow.children().eq(i).hasClass(
				"ui-editable-column ui-state-highlight ui-cell-editing")) {
			rowIndex = i;
			i = max;
		}
		i++;
	}

	highLitedRow = highLitedRow.children().eq(rowIndex);
	column = rowIndex;

}

// You need to select elements by exactly that ID instead.
// The : is however a special character in CSS identifiers representing a pseudo
// selector.
// To select an element with a : in the ID using CSS selectors in jQuery,
// you need to either escape it by backslash or to use the [id=...]
// attribute selector or just use the old getElementById():
//
// var $element1 = $("#foo\\:bar");
// // or
// var $element2 = $("[id='foo:bar']");
// // or
// var $element3 = $(document.getElementById("foo:bar"));
