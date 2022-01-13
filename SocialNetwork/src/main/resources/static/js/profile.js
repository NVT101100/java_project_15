const accessUserId = document.getElementById("userId").innerText;
const webSocket = new WebSocket('wss://'+window.location.hostname+'/SocialNetwork/user/WebSocket/profilePage/'+accessUserId)
function editProfileImage(userId){
	 let input = document.createElement("input");
	 var profileImage = document.getElementById("profileImage");
	 var formData = new FormData();
	 var reader = new FileReader();
	 var src;
	 input.type = "file";
	 input.name = "imagefile";
	 input.onchange = () => {
    	 reader.onload = function (e) {
         	 src = e.target.result;
          };
         reader.readAsDataURL(input.files[0]);
         formData.append("images", input.files[0]);
         $.ajax({
        	 type : "POST",
        	 url : "/SocialNetwork/user/profile/addprofile/"+userId,
        	 data : formData,
        	 processData: false,
        	 contentType: false,
        	 success : function(data) {
        		 alert("Thay đổi ảnh đại diện thành công");
        		 profileImage.src = src;
        	 },
        	 error : function(e) {
        		 alert("Đã xảy ra lỗi");
        	 }
         });
	 };
	 input.click();
}

function editCoverImage(userId){
	 let input = document.createElement("input");
	 var coverImage = document.getElementById("coverImage");
	 var formData = new FormData();
	 var reader = new FileReader();
	 var src;
	 input.type = "file";
	 input.name = "imagefile";
	 input.onchange = () => {
   	 reader.onload = function (e) {
        	 src = e.target.result;
         };
        reader.readAsDataURL(input.files[0]);
        formData.append("images", input.files[0]);
        $.ajax({
       	 type : "POST",
       	 url : "/SocialNetwork/user/profile/addcover/"+userId,
       	 data : formData,
       	 processData: false,
       	 contentType: false,
       	 success : function(data) {
       		 alert("Thay đổi ảnh bìa thành công");
       		 coverImage.src = src;
       	 },
       	 error : function(e) {
       		 alert("Đã xảy ra lỗi");
       	 }
        });
	 };
	 input.click();
}

var btnFriend = document.getElementById("btnFriend")
var icon = document.getElementById("iconAdd")
function handleFriend (toUserId) {
	if(btnFriend.classList.value == "my-wall__head-more-btn"&&btnFriend.innerHTML.includes("Hủy lời mời")) {
		$.ajax({
	       	 type : "POST",
	       	 url : "/SocialNetwork/user/addfriend/"+toUserId,
	       	 data : "cancel",
	       	 processData: false,
	       	 contentType: false,
	       	 success : function(data) {
	       		btnFriend.classList.add("btn-add")
	    		icon.innerHTML = "Thêm bạn bè"
	       	 },
	       	 error : function(e) {
	       		 alert("Đã xảy ra lỗi");
	       	 }
	    });
	} else if(btnFriend.innerHTML.includes("Thêm bạn bè")) {
		$.ajax({
	       	 type : "POST",
	       	 url : "/SocialNetwork/user/addfriend/"+toUserId,
	       	 data : "add",
	       	 processData: false,
	       	 contentType: false,
	       	 success : function(data) {
	       		btnFriend.classList.remove("btn-add")
	    		icon.innerHTML = "Hủy lời mời";
	    		webSocket.send(JSON.stringify({page: "profilePage",toUser: toUserId,content: {type: "addfriend",data: data}}))
	       	 },
	       	 error : function(e) {
	       		 alert("Đã xảy ra lỗi");
	       	 }
	    });
	}
}