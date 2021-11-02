var textAreaBox = document.getElementById("postContent");
var postBtn = document.getElementById("postBtn");
var overplay = document.getElementById("overplay");
var newPostBtn = document.getElementById("newPost");
var postImage = document.getElementById("postImage");
var filearray = [];

function checkSubmit() {
	if (textAreaBox.value == ""){
		postBtn.classList.add("btn-disable")
		postBtn.classList.remove("btn-active")
	} 
	else {
		postBtn.classList.add("btn-active")
		postBtn.classList.remove("btn-disable")
	}
}
function closeOverplay(){
	overplay.style.display = "none";
	document.body.style.overflow = "auto";
}
function showOverplay(){
	overplay.style.display = "flex";
	document.body.style.overflow = "hidden";
}
function showBox() {
	showOverplay()
	checkSubmit()
}
function postStatus(){
	var formdata = {};
	var userId = document.getElementById("userId").innerText;
	formdata["image"] = filearray;
	formdata["status"]= textAreaBox.value;
	console.log(formdata);
	$.ajax({
   	 type : "POST",
   	 contentType : "application/json",
   	 url : "/SocialNetwork/user/post/"+userId,
   	 data : JSON.stringify(formdata),
   	 dataType: 'json',
   	 success : function(data) {
   		 alert("Đã đăng tin của bạn");
   	 },
   	 error : function(e) {
   		 alert("Đã xảy ra lỗi");
   	 }
    });
}
postImage.addEventListener("change",function(event){
	var byte = this.files[0];
	var reader = new FileReader();
	 reader.onload = function (e) {
		 document.getElementById("uploadImage").src = e.target.result;
      };
     reader.readAsDataURL(byte);

     var reader = new FileReader();
	 reader.onload = function (e) {
		 var buffer = reader.result;
		 var img = new Uint8Array(buffer);
		 for (const a of img) {
			 filearray.push(a);
		 }
      };
     reader.readAsArrayBuffer(byte);
})
			
