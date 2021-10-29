function hiddenPassword(){
	var passwordfield = document.getElementById("password")
	if(passwordfield.type == "password") passwordfield.type = "text";
	else if (passwordfield.type == "text") passwordfield.type = "password";
}
function OverlayClose(){
	document.getElementById("myModal").style.display="none";	
}
