var userId = document.getElementById("userId").innerText;
var sound = document.getElementById("myAudio"); 
function getWebSocket() {
        var webSocket = new WebSocket( 'wss://'+window.location.hostname+':8443/SocialNetwork/user/messageServer/'+userId);
        webSocket.onopen = function (event) {
            console.log('WebSocket open connection');
        };
        webSocket.onmessage = function (event) {
        	var message = JSON.parse(event.data);
            if(message.type == "chat") {
            	createNewMessage(message);
            	if(message.userId != userId) playAudio();
            }
            else if(message.type == "like") createLike(message);
            else if(message.type == "unlike") unLike(message);
            else if(message.type == "delete") removeMessage(message);
        };
        webSocket.onclose = function (event) {
            console.log('WebSocket close connection.');
        };
        webSocket.onerror = function (event) {
            console.log('WebSocket exception.');
        };
        return webSocket;
    }
	var webSocket = getWebSocket();
    function sendMsgToServer(data) {
        webSocket.send(data);
    }
    function checkIfEnter(key,withUserId,groupId,userId){
    	if(key.keyCode == 13) {
    		var messageText = document.getElementById("messageText"+groupId).value;
    		document.getElementById("messageText"+groupId).value = "";
    		var data = JSON.stringify({withUserId: withUserId, message: messageText,userId: userId,groupId: groupId,type: "chat",messsageId: ""});
    		$.ajax({
   		   	 type : "POST",
   		   	 contentType : "application/json",
   		   	 url : "/SocialNetwork/user/saveMessage",
   		   	 data : data,
   		   	 success : function(data) {
   		   		 if(data.type == "chat") {
   		   			 sendMsgToServer(JSON.stringify(data));
   		   		 }
   		   	 },
   		   	 error : function(e) {
   		   		 alert("Failed");
   		   	 }
   		    });
    		
    	}
    }
    
    function displayMain(groupId){
    	var mainContainer = document.getElementById("mainContainer"+groupId);
    	var otherMains = document.getElementsByClassName("container-main");
    	for (let i=0;i<otherMains.length;i++){
    		if(otherMains[i] != mainContainer) otherMains[i].style.display = "none";
    	}
    	if(mainContainer.style.display == "none") mainContainer.style.display = "flex";
    	else mainContainer.style.display = "none";
    	
    }
    
    function createNewMessage(data){
    	var ul = document.getElementById("containerBody"+data.groupId);
    	var li = document.createElement("li");
    	var withUserProfile = null;
    	li.classList.add("container-main__item")
    	li.id = "containerMainItem"+data.messageId;
    	if(data.userId==userId) li.classList.add("isUser")
    	else withUserProfile = document.getElementById("withUserProfile"+data.groupId).src;
    	li.innerHTML = '<div class="container-main__item-avt">'
								+ '<img src='+withUserProfile+' alt="" '
								+	'	class="container-main__item-avt__img"> '
								+ '</div>'
								+'<div class="container-main__item-content">'
									+'<div class="container-main__item-text" >'+data.message+'</div>'
									+'<div class="container-main__item-reaction">'
									+	'<div class="container-main__item-reaction-icon">'
									+'		<i style="color: #ff504b" class="fas fa-heart"></i>'
									+'	</div>'
									+'</div>'
								+'</div>'
								+'<div class="container-main__item-group">'
									+'<div class="container-main__item-group-btn">'
										+'<i style="color: #ff504b" class="fas fa-heart id='+data.messageId+'"></i>'
									+'</div>'
									+'<div class="container-main__item-group-btn">'
										+'<i class="fad fa-trash-alt id = '+data.messageId+'"></i>'
									+'</div></div></li></ul>';
    	ul.appendChild(li);
    	li.scrollIntoView(true);
    }
    
    function likeMessage(messageId,withUserId,groupId,userId) {
    	var li = document.getElementById("containerMainItem"+messageId);
    	if(li.classList.value.includes("isReaction")) {
    		var data = JSON.stringify({withUserId: withUserId, message: "",userId: userId,groupId: groupId,type: "unlike",messageId: messageId});
    		$.ajax({
      		   	 type : "POST",
      		   	 contentType : "application/json",
      		   	 url : "/SocialNetwork/user/likeMessage",
      		   	 data : data,
      		   	 success : function(data) {
      		   			 sendMsgToServer(JSON.stringify(data));
      		   	 },
      		   	 error : function(e) {
      		   		 alert("Failed");
      		   	 }
      		    });
    	}
    	else {
    		var data = JSON.stringify({withUserId: withUserId, message: "",userId: userId,groupId: groupId,type: "like",messageId: messageId});
    		$.ajax({
      		   	 type : "POST",
      		   	 contentType : "application/json",
      		   	 url : "/SocialNetwork/user/likeMessage",
      		   	 data : data,
      		   	 success : function(data) {
      		   			 sendMsgToServer(JSON.stringify(data));
      		   	 },
      		   	 error : function(e) {
      		   		 alert("Failed");
      		   	 }
      		    });
    	}
    }
    function deleteMessage(messageId,withUserId,groupId,userId){
    	var data = JSON.stringify({withUserId: withUserId, message: "",userId: userId,groupId: groupId,type: "delete",messageId: messageId});
		$.ajax({
  		   	 type : "POST",
  		   	 contentType : "application/json",
  		   	 url : "/SocialNetwork/user/deleteMessage",
  		   	 data : data,
  		   	 success : function(data) {
  		   			 sendMsgToServer(JSON.stringify(data));
  		   	 },
  		   	 error : function(e) {
  		   		 alert("Failed");
  		   	 }
  		    });
    }
    
    function createLike(message){
    	var li = document.getElementById("containerMainItem"+message.messageId);
    	li.classList.add("isReaction");
    }
    function unLike(message){
    	var li = document.getElementById("containerMainItem"+message.messageId);
    	li.classList.remove("isReaction");
    }
    function removeMessage(message){
    	var li = document.getElementById("containerMainItem"+message.messageId);
    	li.remove();
    }
    
   function redirectCall(){
	   webSocket.onclose;
	   window.location.href = '/SocialNetwork/user/video';
   }

   function playAudio() { 
     sound.play(); 
   } 

   function pauseAudio() { 
     sound.pause(); 
   } 