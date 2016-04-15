$(document)
		.ready(
				function() {
					$(".ui-layout-content")
							.scroll(
									function() {
										$(".ui-layout-content").scrollTop(
												$(this).scrollTop());
										var scroll = $(this).scrollTop();
										// $(".stickyHeader").css('transform',
										// 'translateY(' + scroll + 'px)');
										$(
												"#projecttabview\\:planningForm\\:taskDataTable_head")
												.css(
														'transform',
														'translateY(' + scroll
																+ 'px)');
										// $(".stickyHeader").css('z-index',
										// '50');
										$(".timeline-navigation").css(
												'transform',
												'translateY(' + scroll + 'px)');
										$(".timeline-axis").css('transform',
												'translateY(' + scroll + 'px)');
										// $(".timeline-axis").css('z-index',
										// '50');
										$(".timeline-axis-text").css(
												'transform',
												'translateY(' + scroll + 'px)');
										$(".timeline-groups-axis-onleft").css(
												'transform',
												'translateY(' + scroll + 'px)');
										$(".timeline-axis-grid-major").css(
												'transform',
												'translateY(' + scroll + 'px)');

										// var elementArray =
										// $(".littleButton").map(function() {
										// return this.innerHTML;
										// }).get();

										// for (var i = 0; i <
										// elementArray.length; ++i) {
										// $(elementArray[i]).css('display',
										// 'none');
										// }
										var max = $(
												"#projecttabview\\:planningForm\\:taskDataTable_head")
												.offset().top + 30;

										$(".littleButton")
												.each(
														function(i) {
															if ($(this)
																	.attr('id')
																	.indexOf(
																			"optionsButton") >= 0) {
																var offset = $(
																		this)
																		.parent()
																		.offset();
																if (offset.top == 0) {

																	// $(this).css("display",
																	// "inline-block");
																	$(this)
																			.show();
																} else if (offset.top < max) {
																	// $(this).css("display",
																	// "none");
																	$(this)
																			.hide();
																} else {
																	//$(this).css("display", "inline-block");
																	$(this)
																			.show();
																}
															}
														});

									});

				});

$(function() {
	$(".wmd-view-topscroll").scroll(function() {
		$(".ui-tree").scrollLeft($(".wmd-view-topscroll").scrollLeft());
	});
});