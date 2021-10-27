function verifyPassword() {
	var password = document.getElementById("password").value;
	var confirm = document.getElementById("password-confirmation").value;
	var message = document.getElementById("message");
	if (password == "") {
		message.innerHTML = "Vui lòng nhập mật khẩu";
		return false;
	} else if (password != confirm) {
		message.innerHTML = "Mật khẩu không khớp";
		return false;
	} else if (password == confirm) {
		message = "";
		return true;
	}
}
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