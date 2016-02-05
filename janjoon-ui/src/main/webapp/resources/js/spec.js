function updateTreeHeight() {

	$(".treeWidth").each(function(i) {
		if ($(this).attr('id').indexOf("true") >= 0) {
			var tableId = $(this).attr('id').replace("_true", "");
			var table = $("#specForm\\:categoryTable_" + tableId);
			var tree = $("#specForm\\:chapterTree_" + tableId);

			tree.css("height", table.height() - 4 + "px");

		}
	});
}

$(window).resize(function() {
	updateTreeHeight();
});

$(document).ready(function() {
	updateTreeHeight();
});