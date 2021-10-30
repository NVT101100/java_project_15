var textAreaBox = document.getElementById("postContent");
var postBtn = document.getElementById("postBtn");
var overplay = document.getElementById("overplay");
var newPostBtn = document.getElementById("newPost");

			function checkSubmit() {
                if (document.getElementById("postContent").textLength == 0){
                	document.getElementById("postBtn").classList.add("btn-disable")
                	document.getElementById("postBtn").classList.remove("btn-active")
                } 
				else {
					document.getElementById("postBtn").classList.add("btn-active")
					document.getElementById("postBtn").classList.remove("btn-disable")
                }
			}

			function closeOverplay(){
				document.getElementById("overplay").style.display = "none"
                document.body.style.overflow = "auto"
            }
            function showOverplay(){
            	document.getElementById("overplay").style.display = "flex"
                document.body.style.overflow = "hidden"
            }
			function showBox() {
                showOverplay()
				checkSubmit()
            }