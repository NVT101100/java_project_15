var textAreaBox = document.getElementById("postContent");
var postBtn = document.getElementById("postBtn");
var overplay = document.getElementById("overplay");
var newPostBtn = document.getElementById("newPost");
var postImage = document.getElementById("postImage");
var listNotify = document.getElementById("listNotify");
var settingLists = document.getElementsByClassName("newsfeed__info-setting-list")
var filearray = [];
const userId = document.getElementById("userId").innerText;
const websocket = new WebSocket('wss://'+window.location.hostname+':8443/SocialNetwork/user/WebSocket/homePage/'+userId);
websocket.onmessage = function(event) {
	var message = JSON.parse(event.data)
	console.log(message)
	if(message.content.type == "like") {
		newLike(message.content.postId);
		if(message.content.non != "" && message.toUser == userId) createNontification(message);
	}
	if(message.content.type == "unlike") newUnLike(message.content.postId);
	if(message.content.type == "comment"){
		createNewComment(message.content.postId,message.content.data);
		if(message.content.non != null && message.toUser == userId) createNontification(message);
	}
	if(message.content.type == "addfriend"){
		createNewFriendRequest(message);
	}
	if(message.content.type == "acceptfriend") createNontification(message);
	if(message.content.type == "message") newMessageNotify();
};
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
function postStatus(userId){
	var formdata = {};
	formdata["image"] = filearray;
	formdata["status"]= textAreaBox.value;
	$.ajax({
   	 type : "POST",
   	 contentType : "application/json",
   	 url : "/SocialNetwork/user/post/"+userId,
   	 data : JSON.stringify(formdata),
   	 dataType: 'json',
   	 success : function(data) {
   		 alert("Đã đăng tin của bạn");
   		 textAreaBox.value = "";
   		 document.getElementById("uploadImage").src ="";
   		 closeOverplay();
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

function closeSetting() {
	for(let i = 0;i<settingLists.length;i++){
		settingLists[i].style.display = "none";
	}
	listNotify.style.display = "none";
}
function showSetting(postId) {
	var settingList = document.getElementById("settingList"+postId);
	settingList.style.display = "block";
}

var input = document.getElementById("search-input");
input.addEventListener("keyup", function(event) {
  if (event.keyCode === 13) {
	  var formdata = input.value;
	  $.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/user/search/"+userId,
		   	 data : formdata,
		   	 success : function(data) {
		   		 data.forEach(createUserList)
		   	 },
		   	 error : function(e) {
		   		 alert("Đã xảy ra lỗi");
		   	 }
		    });
  }
});

function createUserList(item) {
	var ul = document.getElementById("friendList");
	var li = document.createElement("li");
	var img = document.createElement("img")
	var a = document.createElement("a")
	var div = document.createElement("div")
	li.classList.add("friend-content__item");
	ul.appendChild(li);
	img.classList.add("friend-content__item-avatar");
	li.appendChild(img);
	div.classList.add("friend-content__item-content");
	img.src = "data:image/png;base64,"+item.image;
	li.appendChild(div);
	a.classList.add("friend-content__item-content-text");
	div.appendChild(a);
	a.innerHTML = item.fullname;
	a.href = "/SocialNetwork/user/profile/"+item.id;
}
function showNotify(userId) {
	listNotify.style.display = "block"
	var count = document.getElementById("count"); 
	if(count != null)
		$.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/user/seenNotify/"+userId,
		   	 data : "seen",
		   	 success : function(data) {
		   		count.style.display = "none";
		   	 },
		   	 error : function(e) {
		   		 alert("Đã xảy ra lỗi");
		   	 }
		    });
}
function handleHeart(postId,userId,userPost){
	var heartPost = document.getElementById("heartPost"+postId);
	if(heartPost.classList.value.includes("active"))
		$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/user/action/like/"+postId,
	   	 data : "unlike",
	   	 success : function(data) {
	   			websocket.send(JSON.stringify({page: "homePage",toUser: userPost,content: {type: "unlike",postId: postId}}));
	   			heartPost.classList.toggle("active");
	   	 },
	   	 error : function(e) {
	   		 alert("Đã xảy ra lỗi");
	   	 }
	    });
	else {
		$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/user/action/like/"+postId,
	   	 data : "like",
	   	 success : function(data) {
	   		websocket.send(JSON.stringify({page: "homePage",toUser: userPost,content: {type: "like",postId: postId,non: data}}));
	   		 heartPost.classList.toggle("active");
	   	 },
	   	 error : function(e) {
	   		 alert("Đã xảy ra lỗi");
	   	 }
	    });
	}
}

function acceptFriend(friendId,userId,nonId){
	$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/user/acceptFriend/"+userId+"/"+friendId+"/"+nonId,
	   	 data : "accept",
	   	 success : function(data) {
	   		var li = document.getElementById("non"+nonId);
	   		li.remove();
	   		websocket.send(JSON.stringify({page: "homePage",toUser: friendId,content: {type: "acceptfriend",non: data}}))
	   	 },
	   	 error : function(e) {
	   		 alert("Đã xảy ra lỗi");
	   	 }
	    });
}
function refuseFriend(friendId,userId,nonId){
	$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/user/refuseFriend/"+userId+"/"+friendId+"/"+nonId,
	   	 data : "refuse",
	   	 success : function(data) {
	   		if(data == "success") {
	   			var li = document.getElementById("non"+nonId);
	   			li.remove();
	   		}
	   	 },
	   	 error : function(e) {
	   		 alert("Đã xảy ra lỗi");
	   		heartPost.classList.toggle("active");
	   	 }
	    });
}
function deletePost(postId){
	$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/user/deletePost/"+postId,
	   	 data : "delete",
	   	 success : function(data) {
	   		if(data == "success") {
	   			alert("Đã xóa bài viết")
	   			var div = document.getElementById("post"+postId);
	   			div.remove();
	   		}
	   	 },
	   	 error : function(e) {
	   		 alert(e);
	   		heartPost.classList.toggle("active");
	   	 }
	    });
}

function updatePost(postId){
	
}

function hiddenAndShowComment(postId){
	var commentedBox = document.getElementById("commentedBox"+postId);
	 if (commentedBox.style.display === "none") {
		 commentedBox.style.display = "block";
	 } else {
		    commentedBox.style.display = "none";
	 }
}

function checkIfEnter(key,postId,userPost){
	if(key.keyCode == 13){
		var comment = document.getElementById("commentText"+postId).value
		$.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/user/comment/"+postId,
		   	 data : comment,
		   	 success : function(data) {
		   		 websocket.send(JSON.stringify({page: "homePage",toUser: userPost,content: {type: "comment",postId: postId,non: data.nonSheetCopy,data: data.userCommentSheet }}))
		   	 },
		   	 error : function(e) {
		   		 alert("Đã xảy ra lỗi");
		   	 }
		    });
	}
}

function createNewComment(postId,data){
	var div0 = document.getElementById("commentedBox"+postId)
	var div9 = document.createElement("div")
	var div1 = document.createElement("div")
	var div2 = document.createElement("div")
	var div3 = document.createElement("div")
	var div4 = document.createElement("div")
	var div5 = document.createElement("div")
	var div6 = document.createElement("div")
	var div7 = document.createElement("div")
	var div8 = document.createElement("div")
	var img = document.createElement("img");
	div9.classList.add("commented-box");
	div0.appendChild(div9);
	div1.classList.add("commented-box__item");
	div9.appendChild(div1);
	div2.classList.add("commented-box__item-user");
	div1.appendChild(div2);
	div4.classList.add("commented-box__item-info");
	div1.appendChild(div4);
	div2.appendChild(div3);
	div3.classList.add("commented-box__item-avatar");
	div3.appendChild(img);
	img.src = "data:image/png;base64,"+data.profile;
	div5.classList.add("wrap");
	div4.appendChild(div5);
	div6.classList.add("comented-box__item-content");
	div5.appendChild(div6);
	div7.classList.add("comented-box__item-name");
	div7.innerHTML = data.fullname;
	div6.appendChild(div7);
	div8.classList.add("comented-box__item-text");
	div8.innerHTML = data.text;
	div6.appendChild(div8);
	document.getElementById("commentText"+postId).value = "";
	var numComment = document.getElementById("numcomment"+postId)
	var textNumComment = numComment.innerText.split(' ');
	var n_numComment = textNumComment[0];
	n_numComment++;
	document.getElementById("commentedBox"+postId).style.display = "block";
	numComment.innerHTML = n_numComment+" Bình luận";
}

function reDirectMessage(){
	window.location.href = "/SocialNetwork/user/messagePage";
}

function newLike(postId){
	var countLike = document.getElementById("numlike"+postId)
	var likeText = countLike.innerText.split(' ');
	var currentNumLike = parseInt(likeText[0]) + 1;
	countLike.innerHTML = currentNumLike+" lượt thích" 
}
function newUnLike(postId){
	var countLike = document.getElementById("numlike"+postId)
	var likeText = countLike.innerText.split(' ');
	var currentNumLike = parseInt(likeText[0]) - 1;
	countLike.innerHTML = currentNumLike+" lượt thích" 
}
function createNontification(message){
	var sound = document.getElementById("notifySound");
	if(message.toUser == userId) sound.play();
	var ul = document.getElementById("notifyContent")
	if(ul == null) {
		div= document.getElementById("notifyContentNewHead");
		ul = document.createElement("ul")
		ul.classList.add("notify-content__new-list")
		ul.id = "notifyContent"
		div.appendChild(ul);
	}
	var li = document.createElement("li");
	li.classList.add("notify-content__item")
	li.innerHTML = '<img src="data:image/png;base64,'+message.content.non.sender.profile+'"'
													+'alt="" class="notify-content__item-avatar">'
													+'<div class="notify-content__item-content">'
													+'	<div class="notify-content__item-content-text">'
													+'		<a>'
													+ message.content.non.sender.fullname+ ' '
													+ message.content.non.text
													+'	</a>'
													+'	</div>'
													+'	<p class=" notify-content__item-time"'
													+'>' +message.content.non.time+'</p>'
													+'</div>'
	ul.appendChild(li);
	var count = document.getElementById("count"); 
	if(count.style.display != "none"){
		var ncount = parseInt(count.innerText)+1;
		count.innerHTML = ncount;
	}
	else{
		count.innerHTML = 1;
		count.style.display = "flex";
	}
}

function createNewFriendRequest(message){
	var sound = document.getElementById("notifySound");
	sound.play();
	var ul = document.getElementById("friendContentList")
	if(ul == null) {
		const div = document.getElementById("friendContentNew")
		ul = document.createElement("ul");
		ul.classList.add("friend-content__new-list")
		ul.id = "friendContentList";
		div.appendChild(ul);
	}
	var li = document.createElement("li")
	li.classList.add("friend-content__item")
	li.id = "non"+message.content.data.non.non_id;
	li.innerHTML = '<img'
		+' src="data:image/png;base64,'+message.content.data.sender.profile+'"'
		+'alt="" class="friend-content__item-avatar"> '
		+'<div class="friend-content__item-content"> '
		+'	<a class="friend-content__item-content-text" '
		+'		style="text-decoration: none"'
		
		+'		href="/user/profile/'+message.content.data.sender.user_id+'">'
		+ message.content.data.sender.fullname+' đã gửi cho bạn lời mời kết bạn '
		+'	</a><span class="friend-content__item-content-text" '
		+'>'+message.content.data.time+'</span>'
		+'	<button class="friend-accept" '
		+'		onclick="acceptFriend('+message.content.data.sender.user_id+','+userId+','+message.content.data.non.non_id+')">Chấp '
		+'nhận</button>'
		+'	<button class="friend-remove" '
		+'onclick="refuseFriend('+message.content.data.sender.user_id+','+userId+','+message.content.data.non.non_id+'">Xóa</button>'
		+'</div></li>'
	ul.appendChild(li);
}

function newMessageNotify(){
	var sound = document.getElementById("notifySound");
	sound.play();
	var countNewMessage = document.getElementById("countNewMessage");
	var count = parseInt(countNewMessage.innerText)+1;
	countNewMessage.innerHTML = count;
}