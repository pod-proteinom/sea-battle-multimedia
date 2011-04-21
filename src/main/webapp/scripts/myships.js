/**
* draws all ships that are currently on the battlefield
* @param list - list of cells that contain ships now
*/
function drawShips(url, postfix) {
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
/*
 * draws all the shots 
 */
function drawShots(options) {
	if (!options.url) options.url = "shots.htm";
	if (!options.opponent) options.opponent = false;//means current player
	if (!options.postfix) options.postfix = "1";//current players field

	$.ajax({
		"url": options.url,
		data: {"opponent": options.opponent},
		dataType: "json",
		success: function(data){
				for (k=0;k<data.length;k++){
					if (data[k].hit){
						$("#c_"+options.postfix+"_"+data[k].coordinates.x+"_"+data[k].coordinates.y).addClass("hited");
					} else {
						$("#c_"+options.postfix+"_"+data[k].coordinates.x+"_"+data[k].coordinates.y).addClass("missed");
					}
				}
			}
	});
}