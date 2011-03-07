$(function() {
	drawContent( "div#content1", "1");
	drawContent( "div#content2", "2");
});

function drawContent(content, postfix){
	$inner = $(content + " div.field div.inner")

	var alphas = "ABCDEFGHIJ";
	var nums = "1234567890";

	for (var i = 0; i < 10; i++) {
		for (var j = 0; j < 10; j++)
			$("<div id='"+"c_"+postfix+"_"+i+"_"+j+"'></div>")
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
		if (currentShipType){
			for (k=0;k<currentShipCoordinates.length;k++){
				i1 = parseInt(ti) + parseInt(currentShipCoordinates[k].x);
				j1 = parseInt(tj) + parseInt(currentShipCoordinates[k].y);
				$("#c_"+postfix+"_"+i1+"_"+j1).addClass("transient_ship");
			}
		}
	}, function() {
		var ti = $(this).attr('i');
		var tj = $(this).attr('j');
		$(content + " > div.field > div.vnums > div[ind='" + postfix + "_" + alphas[ti] + "']").css('color', 'black');
		$(content + " > div.field > div.hnums > div[ind='" + postfix + "_" + nums[tj] + "']").css('color', 'black');
		if (currentShipType){
			for (k=0;k<currentShipCoordinates.length;k++){
				i1 = parseInt(ti) + parseInt(currentShipCoordinates[k].x);
				j1 = parseInt(tj) + parseInt(currentShipCoordinates[k].y);
				$("#c_"+postfix+"_"+i1+"_"+j1).removeClass("transient_ship");
			}
		}
	});

	$(content + " > div.field > div.inner > div").click(function() {
		$.ajax({
		url: "checkShip.htm",
		dataType: "json",
		success: function(data){
			alert(data);
		}
		})
	});
}