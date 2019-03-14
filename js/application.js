function loadSection(id) {
	$('.item-list').slideUp(300);
	$('#selection_' + id).slideDown(300);
}

function selectAllEmails(id) {
	if ($('#allEmails_' + id).is(':checked')) {
		$('#emailTo_' + id).val($('#allEmails').val());
	} else {
		$('#emailTo_' + id).val("");
	}
}

$( function() {
    $(".date-field").datepicker({ dateFormat: 'yy-mm-dd' });
    $("input").attr("autocomplete", "off");
});

function deleteItem(id) {
	$("#myform_" + id).attr("action", $("#myform_" + id).attr("action").replace("saveNotification", "deleteNotification"));
}