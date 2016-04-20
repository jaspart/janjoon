$(document).ready(
		function() {
			if ($(window).width() > 1200) {

				$(".kanbanContainer").scroll(
						function() {
							var ii = $(window).width();
							if (ii > 1200) {
								var scroll = $(this).scrollTop();
								$(".kanbanHeader").css('transform',
										'translateY(' + scroll + 'px)');
							}

						});

			}
		});