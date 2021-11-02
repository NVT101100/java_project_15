function editProfileImage(){
	 let input = document.createElement("input");
	 var profileImage = document.getElementById("profileImage");
	 var formData = new FormData();
	 var reader = new FileReader();
	 var userId = document.getElementById("userId").innerText;
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

function editCoverImage(){
	 let input = document.createElement("input");
	 var coverImage = document.getElementById("coverImage");
	 var formData = new FormData();
	 var reader = new FileReader();
	 var userId = document.getElementById("userId").innerText;
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