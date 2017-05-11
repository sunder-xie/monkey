(function ($) {
    $.Select = function (searchId, url, inputName) {
    	var search = $("#" + searchId);
    	this.autocomplete = function() {

		var availableTags = [];
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}

		// don't navigate away from the field on tab when selecting an item
		search.bind(
				"click",
				function(event) {
					var userNames = split(this.value);
                    //alert(userNames+":::"+encodeURIComponent(userNames));
					$.get(url + "?" + inputName + "=" + encodeURIComponent(userNames), function(
							data, status) {

						for (var i = 0; i < data.data.length; i++)
							availableTags[i] = data.data[i];

					});

					/* if (event.keyCode === $.ui.keyCode.TAB
							&& $(this).autocomplete(
									"instance").menu.active) {
						event.preventDefault();
					} */
				}).autocomplete(
				{
					minLength : 0,
					source : function(request, response) {
						// delegate back to autocomplete, but extract the last term
						response($.ui.autocomplete.filter(availableTags,
								extractLast(request.term)));
					},
					focus : function() {
						// prevent value inserted on focus
						return false;
					},
					select : function(event, ui) {
						var terms = split(this.value);
						// remove the current input
						terms.pop();
						// add the selected item
						terms.push(ui.item.value);
						// add placeholder to get the comma-and-space at the end
						terms.push("");
						this.value = terms.join(",");

						var userNames = split(this.value);
						$.get(url + "?" + inputName + "=" + encodeURIComponent(userNames),
								function(data, status) {

									for (var i = 0; i < data.data.length; i++)
										availableTags[i] = data.data[i];

								});

						return false;
					}
				});
    	};
	};
})(jQuery);