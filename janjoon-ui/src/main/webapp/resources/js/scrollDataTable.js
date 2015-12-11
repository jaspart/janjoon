var scrollTop = 0;
var scrollLeft = 0;
var row = null;
var column = null;

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

			highLitedRow.addClass("ui-state-highlight");

			setTimeout(function() {
				highLitedRow.removeClass("ui-state-highlight");
			}, 2000);
		}
	}
	scrollTop = 0;
	scrollLeft = 0;
	row = null;
	column = null;
}

function setRowIndex(rowIndex) {
	var highLitedRow = $("#projecttabview\\:planningForm\\:taskDataTable_data");

	var i = 0;

	while (i <= rowIndex) {

		//var className = highLitedRow.children().eq(i);
		//console.log(className.attr('class'));
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
