function lookupCountries(input) {
UserValidator.getCountries(input, callback);
removeList();
}

function callback(msg) {
if (msg.length > 0) {
document.getElementById("suggestions").style.display = "block";
var sList = document.getElementById("suggestionsList");
var ul = document.createElement('ul');

for (var i = 0; i < msg.length; i++){
var li = document.createElement('li');
li.innerHTML = msg[i].name;
li.onclick = bindFunction(msg[i].name);
ul.appendChild(li);
}

sList.appendChild(ul);
}
}

function bindFunction(txt) {
return function () {fillTextField(txt);};
}

function fillTextField(txt) {
document.getElementById("country.name").value = txt;
document.getElementById("suggestions").style.display = "none";

removeList();
}

function removeList() {
var sList = document.getElementById("suggestionsList");
var children = sList.childNodes;
for (var i = 0; i < children.length; i++) {
sList.removeChild(children[i]);
}
}