'use strict';
const userId = document.getElementById("userId").innerText;
const localVideo = document.getElementById("localVideo")
const remoteVideo = document.getElementById("remoteVideo")
const acceptButton = document.getElementById("acceptButton")
const audio = document.getElementById("audio")
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
const websocket = new WebSocket('wss://'+window.location.hostname+'/SocialNetwork/user/callServer/'+userId);
websocket.onmessage = function(event){
	var message = JSON.parse(event.data);
	if(message.content.type == "offer" && message.toUser == userId){
		acceptOffer(message);
	}
	else if(message.content.type == "answer" && message.toUser == userId){
		acceptAnswer(message);
	}
	else if(message.content.candidate != null && message.toUser == userId) {
		acceptCandidate(message);
	}
	else if(message.content == "call"){
		commingCall();
	}
	else if(message.content == "accept"){
		call();
	}
}

const ICE_config = {
		iceServers: [/*{
			   urls: [ "stun:hk-turn1.xirsys.com" ]
			},*/ {
			   username: "Sd2vpMxQkRdV-G_gj4gr04XdD4Q1e27m_CFsjaUTkU7q9MbP7SMtdf_vIPIZXHeTAAAAAGGWOTRuZ3V5ZW50aG9p",
			   credential: "db57866c-4862-11ec-971e-0242ac120004",
			   urls: [
			       //"turn:hk-turn1.xirsys.com:80?transport=udp",
			       //"turn:hk-turn1.xirsys.com:3478?transport=udp",
			       //"turn:hk-turn1.xirsys.com:80?transport=tcp",
			       //"turn:hk-turn1.xirsys.com:3478?transport=tcp",
			      // "turns:hk-turn1.xirsys.com:443?transport=tcp",
			       "turns:hk-turn1.xirsys.com:5349?transport=udp"
			   ]
			}]

}

function start(){
	getLocalMedia();
	websocket.send(JSON.stringify({toUser: userId=='1'?'6':'1',content: 'call'}))
}

function call(){
	if(localStream) {
		localVideo.srcObject = localStream;
		localPeer = new RTCPeerConnection(ICE_config)
		localPeer.addEventListener('icecandidate', handleConnection);
		remotePeer = new RTCPeerConnection(ICE_config)
		remotePeer.addEventListener('icecandidate', handleConnection);
		remotePeer.addEventListener('addstream', gotRemoteMediaStream);
		localPeer.addStream(localStream);
		localPeer.createOffer(offerOptions).then(createdOffer).catch();
	}
}

function stop(){
	const tracks = localStream.getTracks();
	tracks.forEach(function(track) {
	   track.stop();
	});
	localVideo.srcObject = null;
}

function hangup(){
	localPeer.close();
	remotePeer.close();
	localPeer = null;
	stop();
}

function getLocalMedia(){
	 navigator.mediaDevices.getUserMedia(constraints)
	 .then(getLocalStream).catch()
}
function getLocalStream(mediaStream){
	localStream = mediaStream;
}

function handleConnection(event){
	const iceCandidate = event.candidate;
	if(iceCandidate) {
		websocket.send(JSON.stringify({toUser: userId=='1'?'6':'1',content: iceCandidate}));
	}
}

function createdOffer(description){
	localPeer.setLocalDescription(description);
	websocket.send(JSON.stringify({toUser: userId=='1'?'6':'1',content: description}));
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
	websocket.send(JSON.stringify({toUser: userId=='1'?'6':'1',content: description}));
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

function commingCall() {
	alert("cuoc goi den")
	audio.play();
	acceptButton.style.display = "flex";
	getLocalMedia();
	acceptButton.onclick = function() {
		audio.pause();
		localVideo.srcObject = localStream;
		websocket.send(JSON.stringify({toUser: userId=='1'?'6':'1',content: 'accept'}));
		localPeer = new RTCPeerConnection(ICE_config)
		localPeer.addEventListener('icecandidate', handleConnection);
		localPeer.addStream(localStream);
		localPeer.createOffer(offerOptions).then(createdOffer).catch();
	}
}
