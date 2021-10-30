function editProfileImage(){
	 let input = document.createElement("input");
	 var profileImage = document.getElementById("profileImage");
	  input.type = "file";
	  input.onchange = _ => {
		  var reader = new FileReader();
          reader.onload = function (e) {
        	  var formData = new FormData();
        	  formData.append("myFile", e.target);
        	 $.ajax({
        		type : "POST",
 				contentType : "application/json",
 				url : "/SocialNetwork/user/profile/addprofile",
 				data : formdata,
 				dataType : 'json',
 				timeout : 100000,
 				success : function(data) {
 					console.log("SUCCESS: ", data);

 				},
 				error : function(e) {
 					console.log("ERROR: ", e);
 				}
        	 });
          };
          reader.readAsDataURL(input.files[0]);
	   };
	  input.click();
}
