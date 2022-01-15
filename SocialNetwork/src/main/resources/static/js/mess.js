window.onload = function(){
	var listMainContain = document.getElementsByClassName("container-main")
	listMainContain[0].style.display = "flex";
	var listMessage = listMainContain[0].getElementsByClassName("container-main__item");
	listMessage[listMessage.length - 1].scrollIntoView(true);
}
function toggleListNew(){
	var listCreate = document.getElementById("listSearch")
	if(listCreate.style.display == "none") listCreate.style.display = "block";
	else listCreate.style.display = "none";
}
var userId = document.getElementById("userId").innerText;
var sound = document.getElementById("myAudio"); 
function getWebSocket() {
        var webSocket = new WebSocket( 'wss://'+window.location.hostname+'/SocialNetwork/user/WebSocket/messagePage/'+userId);
        webSocket.onopen = function (event) {
        	
        };
        webSocket.onmessage = function (event) {
        	var message = JSON.parse(event.data);
        	console.log(message);
        	if(message.content.data) {
        		if(message.content.data.type == "chat") {
        			createNewMessage(message.content.data);
        			if(message.toUser == userId) playAudio();
        		}
        		if(message.content.data.type == "like") createLike(message.content.data);
        		if(message.content.data.type == "unlike") unLike(message.content.data);
        		if(message.content.data.type == "delete") removeMessage(message.content.data);
        		if(message.content.data.type == "creategroup") createGroupSuccess(message.content.data.data);
        	}
        	else if(message.content.type == "call" && message.toUser == userId)  remoteCreateCall(message);
            else if(message.content.type == "acceptcall" && message.toUser == userId) localCreateCall(message);
            else if(message.content.type == "refusecall" && message.toUser == userId) noCreateCall(); 
            else if(message.content.type == "cancelcall" && message.toUser == userId) remoteCancelCall(); 
            else if(message.content.type == "offer" && message.toUser == userId) acceptOffer(message); 
            else if(message.content.type == "answer" && message.toUser == userId) acceptAnswer(message);
        	else if(message.content.candidate != null && message.toUser == userId) acceptCandidate(message);
        	else if(message.content.type == "hangup" && message.toUser == userId) remoteHangUp();
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
    function checkIfEnter(key,withUserId,groupId,userid){
    	if(key.keyCode == 13) {
    		var messageText = document.getElementById("messageText"+groupId).value;
    		document.getElementById("messageText"+groupId).value = "";
    		var data = JSON.stringify({withUserId: withUserId, message: messageText,userId: userid,groupId: groupId,type: "chat",messsageId: ""});
    		$.ajax({
   		   	 type : "POST",
   		   	 contentType : "application/json",
   		   	 url : "/SocialNetwork/user/saveMessage",
   		   	 data : data,
   		   	 success : function(data) {
   		   			sendMsgToServer(JSON.stringify({page: "messagePage",toUser: data.withUserId,content: {type: "message",data: data}}));
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
    	var listMessage = mainContainer.getElementsByClassName("container-main__item");
    	listMessage[listMessage.length-1].scrollIntoView(true);
    }
    
    function createNewMessage(data){
    	const newMessage = document.getElementById("lastMessage"+data.groupId)
    	const timeText = document.getElementById("timeText"+data.groupId)
    	moveUpLeftItem(data.groupId);
    	newMessage.innerHTML = data.message;
    	timeText.innerHTML = "Vừa xong"
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
										+'<i style="color: #ff504b" class="fas fa-heart id='+data.messageId+'"'+'onclick="likeMessage('+data.messageId+','+data.withUserId+','+data.groupId+','+data.userId+')"></i>'
									+'</div>'
									+'<div class="container-main__item-group-btn">'
										+'<i class="fad fa-trash-alt id = '+data.messageId+'"'+'onclick="deleteMessage('+data.messageId+','+data.withUserId+','+data.groupId+','+data.userId+')"></i>'
									+'</div></div></li></ul>';
    	ul.appendChild(li);
    	li.scrollIntoView(true);
    }
    function moveUpLeftItem(groupId){
    	var leftListItem = document.getElementsByClassName("container-left__item");
    	const listItem = document.getElementById("groupList");
    	for(var i=0;i<leftListItem.length;i++){
    		if(leftListItem[i].id == "group"+groupId) {
    			if(i!=0) { 
    				var newItem = leftListItem[i];
    				listItem.removeChild(leftListItem[i]);
    				listItem.prepend(newItem);
    			}
    		}
    	}  	
    }
    
    function likeMessage(messageId,withUserId,groupId,userid) {
    	var li = document.getElementById("containerMainItem"+messageId);
    	if(li.classList.value.includes("isReaction")) {
    		var data = JSON.stringify({withUserId: withUserId, message: "",userId: userid,groupId: groupId,type: "unlike",messageId: messageId});
    		$.ajax({
      		   	 type : "POST",
      		   	 contentType : "application/json",
      		   	 url : "/SocialNetwork/user/likeMessage",
      		   	 data : data,
      		   	 success : function(data) {
      		   		 sendMsgToServer(JSON.stringify({page: "messagePage",toUser: data.withUserId,content: {type: "message",data: data}}));
      		   	 },
      		   	 error : function(e) {
      		   		 alert("Failed");
      		   	 }
      		    });
    	}
    	else {
    		var data = JSON.stringify({withUserId: withUserId, message: "",userId: userid,groupId: groupId,type: "like",messageId: messageId});
    		$.ajax({
      		   	 type : "POST",
      		   	 contentType : "application/json",
      		   	 url : "/SocialNetwork/user/likeMessage",
      		   	 data : data,
      		   	 success : function(data) {
      		   		sendMsgToServer(JSON.stringify({page: "messagePage",toUser: data.withUserId,content: {type: "message",data: data}}));
      		   	 },
      		   	 error : function(e) {
      		   		 alert("Failed");
      		   	 }
      		    });
    	}
    }
    function deleteMessage(messageId,withUserId,groupId,userid){
    	var data = JSON.stringify({withUserId: withUserId, message: "",userId: userid,groupId: groupId,type: "delete",messageId: messageId});
		$.ajax({
  		   	 type : "POST",
  		   	 contentType : "application/json",
  		   	 url : "/SocialNetwork/user/deleteMessage",
  		   	 data : data,
  		   	 success : function(data) {
  		   			 sendMsgToServer(JSON.stringify({page: "messagePage",toUser: data.withUserId,content: {type: "message",data: data}}));
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
   
function createGroup(receiverId) {
	$.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/user/createGroup",
		   	 data : receiverId,
		   	 success : function(data) {
		   			 sendMsgToServer(JSON.stringify({page: "messagePage",toUser: receiverId,content: {type: "message",data:{type:"creategroup",data: data}}}));
		   	 },
		   	 error : function(e) {
		   		 alert("Failed");
		   	 }
		    });
}

function createGroupSuccess(data) {
	const listItem = document.getElementById("groupList");
	var item = document.createElement("li");
	item.innerHTML = '<li class="container-left__item"'
		+'onclick="displayMain('+data.groupId+')"'
			+'id="group'+data.groupId+'">'
		+'	<div class="container-left__item-avatar">'
		+'	<img'
		+'			src="data:image/png;base64,'+data.withUser.profile+'"'
		+'			alt="" class="container-left__item-avatar-img" '
					+'id="withUserProfile'+data.groupId+'">'
		+'	</div>'
		+'	<div class="container-left__item-info">'
			+'	<div class="container-left__item-info-name">'+data.withUser.fullname+'</div>'
			+'	<div class="container-left__item-info-sub"> '
			+'		<div class="container-left__item-info-last-mess"'
				+'		id="lastMessage'+data.groupId+'"></div>'
				+'	<div class="container-left__item-info-time">'
				+'		<span'
				+'			id="timeText'+data.groupId+'"> </span>'
				+'	</div>'
				 +'</div>'
			+'</div>'
		+'	<div class="container-left__item-see"> '
			+'	<div class="container-left__item-see-img"> '
			+'		<img src="img/user_1.jpg" alt=""> '
			+'	</div> '
			+'	<div class="container-left__item-see-icon"> '
			+'		<i class="fas fa-check-circle"></i> '
			+'	</div> '
			+'</div>'
		+'</li>'
		listItem.prepend(item);
	var listMainContain = document.getElementsByClassName("container-main")
	for(var i=0;i<listMainContain.length;i++) {
		listMainContain[i].style.display = "none";
	}
	const appContainer = document.getElementById("appContainer");
	var newMainContain = document.createElement("div");
	newMainContain.classList.add("container-main");
	newMainContain.id = 'mainContainer'+data.groupId;
	newMainContain.innerHTML = '<div class="container-main__head">'
					+'	<div class="container-main__head-left"> '
					+'		<div class="container-main__head-left-avt"> '
					+'			<img class="container-main__head-left-avt-img avt-mess"'
									+'src="data:image/png;base64,'+data.withUser.profile+'"'
									+'alt="">'
							+'</div>'
							+'<div class="container-main__head-left-info">'
								+'<div class="container-main__head-left-info__name name-mess">'+data.withUser.fullname+'</div>'
								+'<div class="container-main__head-left-info-time online">'
									+'<span></span>'
								+'</div>'
							+'</div>'
						+'</div>'
						+'<div class="container-main__head-right">'
							+'<div class="container-main__head-right-btn more-info-btn">'
								+'<i class="fas fa-phone"></i>'
							+'</div>'
							+'<div class="container-main__head-right-btn more-info-btn">'
								+'<i class="fas fa-video"></i>'
							+'</div>'
							+'<div class="container-main__head-right-btn more-info-btn">'
								+'<i class="fas fa-info-circle"></i>'
							+'</div>'
						+'</div>'
					+'</div>'
					+'<div class="container-main__body">'
						+'<ul class="container-main__list"'
							+'id="containerBody'+data.groupId+'">'
						+'</ul>'
					+'</div>'
					+'<div class="container-main__bottom">'
						+'<div class="container-main__bottom-left">'
							+'<div class="container-main__bottom-left-icon">'
								+'<i class="fas fa-plus-circle"></i>'
							+'</div>'
							+'<div class="container-main__bottom-left-icon hide">'
								+'<i class="fas fa-portrait" onclick="redirectCall()"></i>'
							+'</div>'
							+'<div class="container-main__bottom-left-icon hide">'
								+'<i class="fas fa-air-freshener"></i>'
							+'</div>'
							+'<div class="container-main__bottom-left-icon hide">'
								+'<i class="fas fa-gift"></i>'
							+'</div>'
						+'</div>'
						+'<div class="container-main__bottom-search">'
							+'<input type="text" placeholder="Aa"'
								+'class="container-main__bottom-search-input"'
								+'id="messageText'+data.groupId+'"'
								+'onkeypress="checkIfEnter(event,'+data.withUser.user_id+','+data.groupId+','+data.user.user_id+')">'
							+'<div class="container-main__bottom-search__icon">'
								+'<i class="fas fa-smile-wink"></i>'
								+'<div class="container-main__bottom-search__list-icon">'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf4da;"'
											+'class="fas fa-smile-wink"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf5b4;"'
											+'class="fas fa-sad-tear"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf586;"'
											+'class="fas fa-grin-squint-tears"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf556;" class="fas fa-angry"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf579;"'
											+'class="fas fa-flushed"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf5a4;"'
											+'class="fas fa-meh-blank"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf5a5;"'
											+'class="fas fa-meh-rolling-eyes"></i>'
									+'</div>'
									+'<div class="container-main__bottom-icon-item">'
										+'<i style="color: #ffbc22;" alt="&#xf11a;" class="fas fa-meh"></i>'
									+'</div>'
								+'</div>'
							+'</div>'
						+'</div>'
						+'<div class="container-main__bottom-right">'
							+'<div class="container-main__bottom-thumb-up">'
								+'<i class="fas fa-thumbs-up"></i>'
							+'</div>'
							+'<div class="container-main__bottom-send">'
								+'<i class="fas fa-arrow-circle-right"></i>'
							+'</div>'
						+'</div>'
					+'</div>'
	appContainer.appendChild(newMainContain);
	newMainContain.style.display = "flex";
	document.getElementById("listSearch").style.display = "none";
}

const localVideo = document.getElementById("localVideo")
const remoteVideo = document.getElementById("remoteVideo")
const acceptButton = document.getElementById("acceptButton")
const cancelButton = document.getElementById("cancelButton")
const hangUpButton = document.getElementById("hangUpButton")
const callArea = document.getElementById("callArea");
const chatArea = document.getElementById("chatArea");
const offerOptions = {
  offerToReceiveVideo: true,
  OfferToReceiveAudio: true
};
const constraints = {
	video: true,audio: true
};

let localStream;
let localPeer;
let remotePeer;
let callTo;let callFrom;let groupid;let islocal;
const ICE_config = {
		iceServers: [{
			 	urls: [ "stun:hk-turn1.xirsys.com" ]
			}, {
			   username: "Sd2vpMxQkRdV-G_gj4gr04XdD4Q1e27m_CFsjaUTkU7q9MbP7SMtdf_vIPIZXHeTAAAAAGGWOTRuZ3V5ZW50aG9p",
			   credential: "db57866c-4862-11ec-971e-0242ac120004",
			   urls: [
			       "turn:hk-turn1.xirsys.com:80?transport=udp",
			       "turn:hk-turn1.xirsys.com:3478?transport=udp",
			       "turn:hk-turn1.xirsys.com:80?transport=tcp",
		/*	       "turn:hk-turn1.xirsys.com:3478?transport=tcp",
			       "turns:hk-turn1.xirsys.com:443?transport=tcp",
			       "turns:hk-turn1.xirsys.com:5349?transport=udp"*/
			   ]
			}
			]
}
function videoCall(to,from,groupId) {
	callTo = to;
	callFrom = from;
	groupid = groupId;
	islocal = true;
	var modalCall = document.getElementById("modalCallSender");
	modalCall.style.display = "block";
	var receiverProfile = document.getElementById("withUserProfileIn"+groupId).src;
	document.getElementById("toUserName").innerHTML = document.getElementById("withUserNameIn"+groupId).innerText;
	document.getElementById("toUserProfile").src = receiverProfile;
	getLocalMedia();
}

function getLocalMedia(){
	 navigator.mediaDevices.getUserMedia(constraints)
	 .then(getLocalStream).catch()
}
function getLocalStream(mediaStream){
	localStream = mediaStream;
	if(islocal) sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: {type: "call",fromId: callFrom,groupId: groupid}}))
	else {
		localPeer = new RTCPeerConnection(ICE_config);
		localPeer.addStream(localStream);
		localPeer.addEventListener('icecandidate', handleConnection);
		localVideo.srcObject = localStream;
		localPeer.createOffer(offerOptions).then(createdOffer).catch();
	}
}

function localCreateCall(data) {
	callArea.style.display = "block";
	chatArea.style.display = "none";
	document.getElementById("modalCallSender").style.display = "none";
	localPeer = new RTCPeerConnection(ICE_config)
	localPeer.addEventListener('icecandidate',handleConnection);
	localPeer.addStream(localStream);
	localVideo.srcObject = localStream;
	localPeer.createOffer(offerOptions).then(createdOffer).catch();
}

function remoteCreateCall(data) {
	callTo = data.content.fromId;
	callFrom = data.toUser;
	groupid = data.content.groupId;
	const modalCallReceiver = document.getElementById("modalCallReceiver")
	modalCallReceiver.style.display = "block";
	var senderProfile = document.getElementById("withUserProfileIn"+groupid).src;
	document.getElementById("fromUserName").innerHTML = document.getElementById("withUserNameIn"+groupid).innerText;
	document.getElementById("fromUserProfile").src = senderProfile;
}

function cancelCall(){
	document.getElementById("modalCallSender").style.display = "none";
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: {type: "cancelcall",fromId: callFrom,groupId: groupid}}));
	reset();
}
function remoteCancelCall(){
	document.getElementById("modalCallReceiver").style.display = "none";
	reset();
}

function acceptCall(){
	callArea.style.display = "block";
	chatArea.style.display = "none";
	document.getElementById("modalCallReceiver").style.display = "none";
	islocal = false;
	getLocalMedia();
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: {type: "acceptcall",fromId: callFrom,groupId: groupid}}));
}

function refuseCall(){
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: {type: "refusecall",fromId: callFrom,groupId: groupid}}));
	remoteCancelCall();
	reset()
}

function handleConnection(event){
	const iceCandidate = event.candidate;
	if(iceCandidate) {
		sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: iceCandidate}));
	}
}

function createdOffer(description){
	localPeer.setLocalDescription(description);
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: description}));
}

function acceptOffer(message){
	remotePeer = new RTCPeerConnection(ICE_config)
	remotePeer.addEventListener('icecandidate', handleConnection);
	remotePeer.addEventListener('addstream', gotRemoteMediaStream);
	remotePeer.setRemoteDescription(message.content);
	remotePeer.createAnswer().then(createdAnswer).catch();
}

function createdAnswer(description){
	remotePeer.setLocalDescription(description);
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: description}));
}

function acceptAnswer(message){
	localPeer.setRemoteDescription(message.content);
}

function acceptCandidate(message){
	const newCandidate = new RTCIceCandidate(message.content);
	if(remotePeer.localDescription) remotePeer.addIceCandidate(newCandidate);
	console.log(remotePeer)
}
function gotRemoteMediaStream(event){
	remoteVideo.srcObject = event.stream;
}

function noCreateCall(){
	document.getElementById("modalCallSender").style.display = "none";
	reset();
}

function stopCamera(elm){
	var vidTrack = localStream.getVideoTracks();
	vidTrack.forEach(function(track) {
	    if(track.enabled){
	    	track.enabled = false;
	    	elm.classList.remove("fa-video");
	    	elm.classList.add("fa-video-slash");
	    }
	    else{
	    	track.enabled = true;
	    	elm.classList.remove("fa-video-slash");
	    	elm.classList.add("fa-video");
	    }
	 });
}
function stopAudio(elm){
	var vidTrack = localStream.getAudioTracks();
	vidTrack.forEach(function(track) {
	    if(track.enabled){
	    	track.enabled = false;
	    	elm.classList.remove("fa-volume-high");
	    	elm.classList.add("fa-volume-xmark");
	    }
	    else {
	    	track.enabled = true;
	    	elm.classList.remove("fa-volume-xmark");
	    	elm.classList.add("fa-volume-high");
	    }
	 });
}

function hangUp(){
	var Track = localStream.getTracks();
	Track.forEach(function(track) {
	    track.stop();
	 });
	sendMsgToServer(JSON.stringify({page: "messagePage",toUser: callTo,content: {type: "hangup"}}));
	reset();
	callArea.style.display = "none";
	chatArea.style.display = "block";
}
function remoteHangUp() {
	if(localStream) { 
		var Track = localStream.getTracks();
		Track.forEach(function(track) {
			track.stop();
		});
	}
	reset();
	callArea.style.display = "none";
	chatArea.style.display = "block";
}
function reset(){
	localVideo.srcObject = null;
	remoteVideo.srcObject = null;
	localPeer = null;
	remotePeer = null;callTo = null; callFrom = null; groupid = null; islocal = null;
}