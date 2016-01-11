PrimeFaces.locales['es'] = {
	closeText : 'Cerrar',
	prevText : 'Anterior',
	nextText : 'Siguiente',
	currentText : 'Inicio',
	monthNames : [ 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
			'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre',
			'Diciembre' ],
	monthNamesShort : [ 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago',
			'Sep', 'Oct', 'Nov', 'Dic' ],
	dayNames : [ 'Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves',
			'Viernes', 'Sábado' ],
	dayNamesShort : [ 'Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab' ],
	dayNamesMin : [ 'D', 'L', 'M', 'X', 'J', 'V', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Sólo hora',
	timeText : 'Tempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	currentText : 'Fecha actual',
	ampm : false,
	month : 'Mes',
	week : 'Semana',
	day : 'Día',
	allDayText : 'Todo el día'
};
PrimeFaces.locales['de'] = {
	closeText : 'Schließen',
	prevText : 'Zurück',
	nextText : 'Weiter',
	currentText : 'Start',
	monthNames : [ 'Januar', 'Februar', 'MÃ¤rz', 'April', 'Mai', 'Juni',
			'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember' ],
	monthNamesShort : [ 'Jan', 'Feb', 'MÃ¤r', 'Apr', 'Mai', 'Jun', 'Jul',
			'Aug', 'Sep', 'Okt', 'Nov', 'Dez' ],
	dayNames : [ 'Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag',
			'Freitag', 'Samstag' ],
	dayNamesShort : [ 'Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam' ],
	dayNamesMin : [ 'S', 'M', 'D', 'M ', 'D', 'F ', 'S' ],
	weekHeader : 'Woche',
	FirstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Nur Zeit',
	timeText : 'Zeit',
	hourText : 'Stunde',
	minuteText : 'Minute',
	secondText : 'Sekunde',
	currentText : 'Aktuelles Datum',
	ampm : false,
	month : 'Monat',
	week : 'Woche',
	day : 'Tag',
	allDayText : 'Ganzer Tag'
};

PrimeFacesExt.locales.Timeline['fr'] = {
	'MONTHS' : [ "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
			"Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" ],
	'MONTHS_SHORT' : [ "Jan", "Fev", "Mar", "Avr", "Mai", "Jun", "Jul", "Aou",
			"Sep", "Oct", "Nov", "Dec" ],
	'DAYS' : [ "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi",
			"Samedi" ],
	'DAYS_SHORT' : [ "Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam" ],
	'ZOOM_IN' : "Zoomer",
	'ZOOM_OUT' : "Dézoomer",
	'MOVE_LEFT' : "Déplacer à gauche",
	'MOVE_RIGHT' : "Déplacer à droite",
	'NEW' : "Nouveau",
	'CREATE_NEW_EVENT' : "Créer un nouvel évènement"
};

PrimeFacesExt.locales.TimePicker['fr'] = {
	hourText : 'Heures',
	minuteText : 'Minutes',
	amPmText : [ 'AM', 'PM' ],
	closeButtonText : 'Fermer',
	nowButtonText : 'Maintenant',
	deselectButtonText : 'Désélectionner'
};

PrimeFaces.locales['fr'] = {
	closeText : 'Fermer',
	prevText : 'Précédent',
	nextText : 'Suivant',
	monthNames : [ 'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
			'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre' ],
	monthNamesShort : [ 'Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aoû',
			'Sep', 'Oct', 'Nov', 'Déc' ],
	dayNames : [ 'Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi',
			'Samedi' ],
	dayNamesShort : [ 'Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam' ],
	dayNamesMin : [ 'D', 'L', 'M', 'M', 'J', 'V', 'S' ],
	weekHeader : 'Semaine',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'Choisir l\'heure',
	timeText : 'Heure',
	hourText : 'Heures',
	minuteText : 'Minutes',
	secondText : 'Secondes',
	currentText : 'Maintenant',
	ampm : false,
	month : 'Mois',
	week : 'Semaine',
	day : 'Jour',
	allDayText : 'Toute la journée'
};

function setMenuActiveIndex(activeIndex) {

	var tabMenuPanel = $("#headerForm\\:tabMenu");

	if (tabMenuPanel.hasClass("rio-menu")) {

		tabMenuPanel.children().eq(activeIndex).addClass("active-menu-parent");
		tabMenuPanel.children().eq(activeIndex).children().eq(0).addClass(
				"active-menu active-menu-restore");
	}

}

function removeMenuActiveIndex() {

	var tabMenuPanel = $("#headerForm\\:tabMenu");

	if (tabMenuPanel.hasClass("rio-menu")) {

		var i = 0;
		while (i < tabMenuPanel.children().length) {
			if (tabMenuPanel.children().eq(i).hasClass("active-menu-parent")) {
				tabMenuPanel.children().eq(i).removeClass("active-menu-parent");
				tabMenuPanel.children().eq(i).children().eq(0).removeClass(
						"active-menu active-menu-restore");
			}

			i++;
		}
	}

}

$(window)
		.resize(
				function() {
					
					$(".ui-dialog-content").css("maxWidth",($(window).width() * 90 / 100) + "px");
					$(".ui-dialog-content").css("maxHeight",($(window).height() * 80 / 100) + "px");

					var $dlg = $(".ui-dialog:visible");
					// $dlg.dialog("option", "position", {my: "center", at:
					// "center", of: window});

					$dlg.position({my : "center center",at : "center center",of : window});

//					var width = $(window).width() * 82 / 100;
//
//					if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i
//							.test(navigator.userAgent)) {
//						width = $(window).width() * 95 / 100;
//					} else if ($(window).width() < 960)
//						width = $(window).width() * 95 / 100;
//					$("#layoutsGrid").width(width);
				});
$(function() {
	// var height = $("#requirementForm\\:requirementFormPanelGrid").height() *
	// 81.1 / 100;
	// var width = $("#treeForm\\:treePanelGrid").width() * 96 / 100;
	// $(".reqTree").css( "maxWidth", ( $("#treeForm\\:treePanelGrid").width() *
	// 95/100) + "px" );
	// $(".reqTree").height(height);

	// ui-dialog-content

	$(".ui-dialog-content").css("maxWidth",
			($(window).width() * 90 / 100) + "px");
	$(".ui-dialog-content").css("maxHeight",
			($(window).height() * 80 / 100) + "px");
});

//$(function() {
//	var width = $(window).width() * 82 / 100;
//
//	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i
//			.test(navigator.userAgent)) {
//		width = $(window).width() * 95 / 100;
//	} else if ($(window).width() < 960)
//		width = $(window).width() * 95 / 100;
//	$("#layoutsGrid").width(width);
//});

$(document).ready(function() {
	$("#rowToggler").click(function() {
		$("#rowExpansion").slideUp("fast");
		$('.active').removeClass('active');
		return false;
	});
	function OpenDailog() {
		var $dlg = $('#categoryPanel');
		// Here We check if dailog is open or not
		if ($dlg.dialog('isOpen') == true) {
			PF('categoryPanel').hide();
		} else {
			PF('categoryPanel').show();
		}

	}
});