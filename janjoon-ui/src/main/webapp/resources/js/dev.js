function updateTabViewWidth() {

	var table = $("#form\\:tabView");
	var width = 0;
	if ($(window).width() > 1200)
		width = (60 * ($("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 264) / 100) - 20;
	else if ($(window).width() > 960)
		width = $("#layout-topbar").width()
				- (0.1 / 100 * $("#layout-topbar").width()) - 264 - 20 - 15;
	else
		width = $("#layout-topbar").width()
				- (2 / 100 * $("#layout-topbar").width()) - 14 - 20 - 15;

	table.css("maxWidth", width + "px");
	table.css("display", "block");

}

$(window).resize(function() {
	updateTabViewWidth();
});

$(document).ready(function() {
	updateTabViewWidth();
});

function getSelectedRange(editor) {
	return {
		from : editor.getCursor(true),
		to : editor.getCursor(false)
	};
}

//function refreshCodeMirror() {
//	var div = $(".codeMirrorId:visible").last();
//
//	if (div.length != 0
//			&& $("#form\\:tabView\\:" + div.attr("id") + "\\:codeMirror").next(
//					'.CodeMirror').get(0).length != 0) {
//
//		var editor = $("#form\\:tabView\\:" + div.attr("id") + "\\:codeMirror")
//				.next('.CodeMirror').get(0).CodeMirror;
//		if (editor.length != 0) {
//			editor.refresh();
//		}
//	}
//}

function commentSelection(isComment) {
	var div = $(".codeMirrorId:visible").last();

	if (div.length != 0) {

		var editor = $("#form\\:tabView\\:" + div.attr("id") + "\\:codeMirror")
				.next('.CodeMirror').get(0).CodeMirror;
		if (editor.length != 0) {
			var range = getSelectedRange(editor);
			editor.commentRange(isComment, range.from, range.to);
		}
	}
}

function autoFormatSelection() {

	var div = $(".codeMirrorId:visible").last();

	if (div.length != 0) {

		var editor = $("#form\\:tabView\\:" + div.attr("id") + "\\:codeMirror")
				.next('.CodeMirror').get(0).CodeMirror;
		if (editor.length != 0) {
			var firstLineNumber = editor.getOption('firstLineNumber');
			var totalLines = editor.lineCount();
			var totalChars = editor.getValue().length;

			editor.autoFormatRange({
				line : firstLineNumber,
				ch : 0
			}, {
				line : totalLines - 1 + firstLineNumber,
				ch : totalChars
			});
		}
	}
}
