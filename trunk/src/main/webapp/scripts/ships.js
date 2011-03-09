/**
 * appends boats to the div
 * @param id - id of the tag where to append boats
 * @param url - url where with available ships json 
 */
function appendBoats(id, url){
	if (!id) id = "#availableShips";
	if (!url) url = "ships.htm"
	$.ajax({
		"url": url,
		dataType: "json",
		success: function(data){

		$.each(data, function(i, val){
		$inner = $("<div></div>").appendTo(id);
		$("<div>"+val.name+"</div>").appendTo($inner);

		appendBoat($inner, val.coordinates, true, val.type);

		});
		}
	});
}

var shipTypeNumber = 0;
var currentShipType;
var currentShipCoordinates;
var deleting = false;

/**
rez - where to append your ship
coordinates - coordinates of the ship
appendID - if to append id
*/
function appendBoat(rez, coordinates, appendID, type){
shipTypeNumber++;
max = getMax(coordinates);

if (appendID){
$model = $("<table></table>").appendTo(rez);
} else {
$model = $(rez).html("<table></table>");
}

for (x=0;x<max.x+1;x++){
$tr = $("<tr></tr>").appendTo($model);
for (y=0;y<max.y+1;y++){
$("<td id='j_"+shipTypeNumber+"_"+x+"_"+y+"'></td>").appendTo($tr);
}
}

for (j=0;j<coordinates.length;j++){
$("#j_"+shipTypeNumber+"_"+coordinates[j].x+"_"+coordinates[j].y+"").addClass("ship");
if (appendID){
$("#j_"+shipTypeNumber+"_"+coordinates[j].x+"_"+coordinates[j].y+"").click(function(){
appendBoat($("#currentShips"), coordinates, false, type);
currentShipType = type;
deleting = false;
currentShipCoordinates = coordinates;
});
}
}

}

function getMax(coordinates){
x=0;
y=0;
for (i=0;i<coordinates.length;i++){
if (coordinates[i].x>x) x = coordinates[i].x;
if (coordinates[i].y>y) y = coordinates[i].y;
}
return {"x":x, "y":y};
}

/**
 * canceling ship deployment and setting ship deployment
 */
function deletingShip(msg){
	currentShipType = false;
	currentShipCoordinates = false;
	deleting = true;

	$("#currentShips").html(msg);
}