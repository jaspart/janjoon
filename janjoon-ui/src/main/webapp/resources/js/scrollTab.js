$(function() {

	$(".ui-layout-content").scroll(function() {
		$(".ui-layout-content").scrollTop($(this).scrollTop());
	});

});

$(function() {
	$(".wmd-view-topscroll").scroll(function() {
		$(".ui-tree").scrollLeft($(".wmd-view-topscroll").scrollLeft());
	});
});
