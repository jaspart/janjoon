$(document).ready(
		function() {
			$(".ui-layout-content").scroll(
					function() {
						$(".ui-layout-content").scrollTop($(this).scrollTop());
						var scroll = $(this).scrollTop();
						$(".stickyHeader").css('transform',
								'translateY(' + scroll + 'px)');
						// $(".stickyHeader").css('z-index',
						// '50');
						$(".timeline-navigation").css('transform',
								'translateY(' + scroll + 'px)');
						$(".timeline-axis").css('transform',
								'translateY(' + scroll + 'px)');
						// $(".timeline-axis").css('z-index',
						// '50');
						$(".timeline-axis-text").css('transform',
								'translateY(' + scroll + 'px)');
						$(".timeline-groups-axis-onleft").css('transform',
								'translateY(' + scroll + 'px)');
						$(".timeline-axis-grid-major").css('transform',
								'translateY(' + scroll + 'px)');

						// var elementArray =
						// $(".ui-splitbutton").map(function() {
						// return this.innerHTML;
						// }).get();
						//
						// for (var i = 0; i <
						// elementArray.length; ++i) {
						// $(elementArray[i]).css('display',
						// 'none');
						// }
						// var max = $(".stickyHeader").offset().top + 37;
						//
						// $(".ui-splitbutton")
						// .each(
						// function(i) {
						// if ($(this)
						// .attr('id')
						// .indexOf(
						// "viewTaskButton") >= 0) {
						// var offset = $(
						// this)
						// .offset();
						// if (offset.top == 0) {
						// if ($(this)
						// .hasClass(
						// "splitButtonDisplayNone"))
						// $(this)
						// .removeClass(
						// "splitButtonDisplayNone");
						// } else if (offset.top < max) {
						// if (!$(this)
						// .hasClass(
						// "splitButtonDisplayNone"))
						// $(this)
						// .addClass(
						// "splitButtonDisplayNone");
						// } else {
						// if ($(this)
						// .hasClass(
						// "splitButtonDisplayNone"))
						// $(this)
						// .removeClass(
						// "splitButtonDisplayNone");
						//
						// }
						// }
						// });
						//
					});

		});

// $(function() {
// $(".rsh").draggable({
// axis : "y",
// containment : "parent",
// helper : "clone",
// drag : function(event, ui) {
// var height = ui.offset.top;
// $(this).prev().height(height);
// }
// });
// });
$(function() {
	var width = $(window).width() * 95 / 100;
	$("#layoutsGrid").width(width);
});

$(function() {
	$(".wmd-view-topscroll").scroll(function() {
		$(".ui-tree").scrollLeft($(".wmd-view-topscroll").scrollLeft());
	});
});
