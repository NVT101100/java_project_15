
function hiddenPassword(){
	var passwordfield = document.getElementById("password")
	if(passwordfield.type == "password") passwordfield.type = "text";
	else if (passwordfield.type == "text") passwordfield.type = "password";
}
function hiddenConPassword(){
	var passwordfield = document.getElementById("password-confirmation")
	if(passwordfield.type == "password") passwordfield.type = "text";
	else if (passwordfield.type == "text") passwordfield.type = "password";
}