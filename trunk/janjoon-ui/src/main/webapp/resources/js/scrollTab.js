$(function() {

	$(".ui-layout-content").scroll(function() {
		$(".ui-layout-content").scrollTop($(this).scrollTop());
		var scroll=$(this).scrollTop();
		$(".stickyHeader").css('transform','translate(0px,'+scroll+'px)');
		$(".stickyHeader").css('z-index','50');
		$(".timeline-axis").css('transform','translate(0px,'+scroll+'px)');
		$(".timeline-axis").css('z-index','50');
		$(".timeline-axis-text").css('transform','translate(0px,'+scroll+'px)');
		$(".timeline-groups-axis-onleft").css('transform','translate(0px,'+scroll+'px)');
		$(".timeline-axis-grid-major").css('transform','translate(0px,'+scroll+'px)');
	});

});

$(function() {
	$(".wmd-view-topscroll").scroll(function() {
		$(".ui-tree").scrollLeft($(".wmd-view-topscroll").scrollLeft());
	});
});
