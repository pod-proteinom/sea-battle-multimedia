function drawContent(content, postfix, opponent){
	$inner = $(content + " div.field div.inner")

	var alphas = "ABCDEFGHIJ";
	var nums = "1234567890";

	for (var i = 0; i < 10; i++) {
		for (var j = 0; j < 10; j++)
			$("<div id='c_"+postfix+"_"+i+"_"+j+"'></div>")
			.attr({"i":i, "j": j})
			.appendTo($inner);
		$("<br style='clear: both'/>").appendTo($inner);
	}

	$vnums = $(content + " div.field div.vnums");
	$hnums = $(content + " div.field div.hnums");

	for (var i = 0; i < 10; i++) {
		$("<div ind='"+postfix + "_" + alphas[i] + "'>" + alphas[i] + "</div>").appendTo($hnums);
		$("<div ind='"+postfix + "_" + nums[i] + "'>" + nums[i] + "</div>").appendTo($vnums);
	}

	$(content + " > div.field > div.inner > div").hover( function() {
		var ti = $(this).attr('i');
		var tj = $(this).attr('j');
		$(content + " > div.field > div.vnums > div[ind='" + postfix + "_" + alphas[ti] + "']").css('color', 'red');
		$(content + " > div.field > div.hnums > div[ind='" + postfix + "_" + nums[tj] + "']").css('color', 'red');
		$("#c_"+postfix+"_"+ti+"_"+tj).addClass("transient_ship");
	}, function() {
		var ti = $(this).attr('i');
		var tj = $(this).attr('j');
		$(content + " > div.field > div.vnums > div[ind='" + postfix + "_" + alphas[ti] + "']").css('color', 'black');
		$(content + " > div.field > div.hnums > div[ind='" + postfix + "_" + nums[tj] + "']").css('color', 'black');
		$("#c_"+postfix+"_"+ti+"_"+tj).removeClass("transient_ship");
	});
	if (opponent) {
		$(content + " > div.field > div.inner > div").click(function() {
				$.ajax({
					url: "shoot.htm",
					data: {"x":$(this).attr('i'), "y":$(this).attr('j')},
					dataType: "json",
					success: function(data){
						  if (data && data.msg) {
							  $("#operation_result").html(data.msg);
						  }
						}
				});
		});
	}
}