function editProfileImage(){
	 let input = document.createElement("input");
	 var profileImage = document.getElementById("profileImage");
	 var formData = new FormData();
	 var reader = new FileReader();
	 input.type = "file";
	 input.name = "imagefile";
	 input.onchange = () => {
    	 reader.onload = function (e) {
         	 profileImage.src = e.target.result;
          };
         reader.readAsDataURL(input.files[0]);
         formData.append("images", input.files[0]);
         $.ajax({
        	 type : "POST",
        	 url : "/SocialNetwork/user/profile/addprofile",
        	 data : formData,
        	 processData: false,
        	 contentType: false,
        	 success : function(data) {
        		 alert("Thay đổi ảnh đại diện thành công");
        	 },
        	 error : function(e) {
        		 alert("Đã xảy ra lỗi");
        	 }
         });
	 };
	 input.click();
}
