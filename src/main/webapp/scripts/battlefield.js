function drawContent(content, postfix){
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
		if (currentShipType){
			$list = new Array();
			$cssClass = "transient_ship";
			for (k=0;k<currentShipCoordinates.length;k++){
				i1 = parseInt(ti) + parseInt(currentShipCoordinates[k].x);
				j1 = parseInt(tj) + parseInt(currentShipCoordinates[k].y);
				$element = $("#c_"+postfix+"_"+i1+"_"+j1);
				if ($element.length==1){
						$list.push($element);
					if ($element.hasClass("placed_ship")){
						$cssClass = "wrong_ship";
					}
				} else {
					$cssClass = "wrong_ship";
				}
			}
			for (k=0;k<$list.length;k++){
				$list[k].addClass($cssClass);
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
				$("#c_"+postfix+"_"+i1+"_"+j1).removeClass("transient_ship").removeClass("wrong_ship");
			}
		}
	});

	$(content + " > div.field > div.inner > div").click(function() {
		if (currentShipType){
			var params = {"type":currentShipType, "x":$(this).attr('i'), "y":$(this).attr('j'), "coords":currentShipCoordinates, "postfix":postfix};
			$.ajax({
				url: "deployShip.htm",
				data: {"type":currentShipType, "x":$(this).attr('i'), "y":$(this).attr('j')},
				dataType: "json",
				success: function(data){
						if(data.msg=='OK'){
							placeShip(params);
							$("#operation_result").html("");
						} else {
							$("#operation_result").html(data.msg).css("color", "red");
						}
					}
			});
		} else if (deleting){
			$.ajax({
				url: "deleteShip.htm",
				data: {"x":$(this).attr('i'), "y":$(this).attr('j')},
				dataType: "json",
				success: function(data){
							if(data){
								deleteShip({"coords":data, "postfix":postfix});
								$("#operation_result").html("");
							} else {
								$("#operation_result").html(delete_error).css("color", "red");
							}
						}
				});
		}
	});
}

/**
* draws all ships that are currently on the battlefield
* @param list - list of cells that contain ships now
*/
function drawShips(url, postfix){
	$.ajax({
		"url": url,
		dataType: "json",
		success: function(data){
				for (k=0;k<data.length;k++){
					$("#c_"+postfix+"_"+data[k].x+"_"+data[k].y).addClass("placed_ship");
				}
			}
	});
}

/**
* @param params - contains "type" of the ship, "x" coordinate and "y" coordinate, "coords" - ship coordinates offset
*/
function placeShip(params){
	for (k=0;k<params.coords.length;k++){
		i1 = parseInt(params.x) + parseInt(params.coords[k].x);
		j1 = parseInt(params.y) + parseInt(params.coords[k].y);
		$("#c_"+params.postfix+"_"+i1+"_"+j1).addClass("placed_ship");
	}
}


/**
* @param params - contains "type" of the ship, "x" coordinate and "y" coordinate, "coords" - ship coordinates offset, "postfix" - container id postfix
*/
function deleteShip(params){
	for (k=0;k<params.coords.length;k++){
		$("#c_"+params.postfix+"_"+params.coords[k].x+"_"+params.coords[k].y).removeClass("placed_ship");
	}
}