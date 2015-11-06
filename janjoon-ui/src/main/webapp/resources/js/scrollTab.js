$(document).ready(
		function() {
			$(".ui-layout-content").scroll(
					function() {
						$(".ui-layout-content").scrollTop($(this).scrollTop());
						var scroll = $(this).scrollTop();
						$(".stickyHeader").css('transform',
								'translate(0px,' + scroll + 'px)');
						// $(".stickyHeader").css('z-index', '50');
						$(".timeline-axis").css('transform',
								'translate(0px,' + scroll + 'px)');
						// $(".timeline-axis").css('z-index', '50');
						$(".timeline-axis-text").css('transform',
								'translate(0px,' + scroll + 'px)');
						$(".timeline-groups-axis-onleft").css('transform',
								'translate(0px,' + scroll + 'px)');
						$(".timeline-axis-grid-major").css('transform',
								'translate(0px,' + scroll + 'px)');
						// $(".ui-splitbutton").css('display','none');

						// var elementArray =
						// $(".ui-splitbutton").map(function() {
						// return this.innerHTML;
						// }).get();
						//				
						// for (var i = 0; i < elementArray.length; ++i) {
						// $(elementArray[i]).css('display','none');
						// }
						var max = $(".stickyHeader").offset().top + 37;
						$(".ui-splitbutton").each(
								function(i) {
									// $(this).css('display', 'none');

									// console.log($(this).attr('id') + ':' +
									// offset.top);
									// var str = $(this).attr('id');
									if ($(this).attr('id').indexOf(
											"viewTaskButton") >= 0) {
										var offset = $(this).offset();
										if (offset.top == 0) {
											$(this).css('display','inline-block');
										} else if (offset.top < max) {
											$(this).css('display', 'none');
											// $(this).hide();
										} else {
											$(this).css('display',
													'inline-block');
											// $(this).show();
										}
									}
								});

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
	$(".wmd-view-topscroll").scroll(function() {
		$(".ui-tree").scrollLeft($(".wmd-view-topscroll").scrollLeft());
	});
});
