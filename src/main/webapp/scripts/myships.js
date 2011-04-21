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