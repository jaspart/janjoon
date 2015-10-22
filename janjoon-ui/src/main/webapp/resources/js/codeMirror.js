//var editor = CodeMirror.fromTextArea(document.getElementById("form:tabView:0:codeMirror"), {
//	lineNumbers : true,
//	extraKeys:{"Ctrl-Shift-F":autoFormatSelection}
//});
//
//CodeMirror.commands["selectAll"](editor);
//extraKeys="{ 'Ctrl-Space': function(cm) {CodeMirror.simpleHint(cm, CodeMirror.javascriptHint);},'Ctrl-Shift-F':autoFormatSelection(cm)}"

function getSelectedRange(editor) {
	return {
		from : editor.getCursor(true),
		to : editor.getCursor(false)
	};
}

function autoFormatSelection(editor) {
	var range = getSelectedRange(editor);
	editor.autoFormatRange(range.from, range.to);
}

function commentSelection(isComment,editor) {
	var range = getSelectedRange(editor);
	editor.commentRange(isComment, range.from, range.to);
}
