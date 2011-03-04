$(function() {
	var alphas = "ABCDEFGHIJ";
	var nums = "1234567890";

	$inner = $("div#content div.field div.inner");
	for (var j = 0; j < 10; j++) {
		for (var i = 0; i < 10; i++)
			$("<div></div>")
			.attr({h: nums[j], w:alphas[i]})
			.appendTo($inner);
		$("<br style='clear: both'/>").appendTo($inner);
	}
	
	$vnums = $("div#content div.field div.vnums");
	$hnums = $("div#content div.field div.hnums");
	
	for (var i = 0; i < 10; i++) {
		$("<div ind='" + alphas[i] + "'>" + alphas[i] + "</div>").appendTo($hnums);
		$("<div ind='" + nums[i] + "'>" + nums[i] + "</div>").appendTo($vnums);
	}
	
	$("div.inner > div").hover( function() {
		var h = $(this).attr('h');
		var w = $(this).attr('w');
		$("div[ind='" + h + "']").css('color', 'yellow');
		$("div[ind='" + w + "']").css('color', 'yellow');
	}, function() {
		var h = $(this).attr('h');
		var w = $(this).attr('w');
		$("div[ind='" + h + "']").css('color', 'white');
		$("div[ind='" + w + "']").css('color', 'white');
	});
});