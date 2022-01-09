	google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);
    const options1 = {
            //title: 'Company Performance',
  		  smoothLine: true,
            hAxis: {textPosition: 'none', gridlines: {
                color: 'transparent'
            }},
            vAxis: {minValue: 0, gridlines: {
                color: 'transparent'
            }},
  		  height: 280,
  		  legend: 'none',
  		  width: 850,
  		  colors: ['#eeeeee'],
  		  backgroundColor: '#001100',
  		  chartArea: {
  			backgroundColor: {
  			fill: '#001100'
  			},
  			width: 760,
  			height: 250
  			},
  			position: 'static',
          };
  		const options2 = {
            //title: 'Company Performance',
  		  smoothLine: true,
            hAxis: {textPosition: 'none', gridlines: {
                color: 'transparent'
            }},
            vAxis: {minValue: 0,textPosition: 'none', gridlines: {
                color: 'transparent'
            }},
  		  height: 70,
  		  legend: 'none',
  		  width: 70,
  		  colors: ['#eeeeee'],
  		  backgroundColor: '#001100',
  		  chartArea: {
  			backgroundColor: {
  			fill: '#cc0000'
  			},
  			width: 70,
  			height: 70
  			}
          };
  		const options3 = {
  	            //title: 'Company Performance',
  	  		  smoothLine: true,
  	            hAxis: {textPosition: 'none', gridlines: {
  	                color: 'transparent'
  	            }},
  	            vAxis: {minValue: 0,textPosition: 'none', gridlines: {
  	                color: 'transparent'
  	            }},
  	  		  height: 70,
  	  		  legend: 'none',
  	  		  width: 70,
  	  		  colors: ['#eeeeee'],
  	  		  backgroundColor: '#33cc00',
  	  		  chartArea: {
  	  			backgroundColor: {
  	  			fill: '#33cc00'
  	  			},
  	  			width: 70,
  	  			height: 70
  	  			}
  	          };
  		const options4 = {
  	            //title: 'Company Performance',
  	  		  smoothLine: true,
  	            hAxis: {textPosition: 'none', gridlines: {
  	                color: 'transparent'
  	            }},
  	            vAxis: {minValue: 0,textPosition: 'none', gridlines: {
  	                color: 'transparent'
  	            }},
  	  		  height: 70,
  	  		  legend: 'none',
  	  		  width: 70,
  	  		  colors: ['#eeeeee'],
  	  		  backgroundColor: '#660099',
  	  		  chartArea: {
  	  			backgroundColor: {
  	  			fill: '#660099'
  	  			},
  	  			width: 70,
  	  			height: 70
  	  			}
  	          };
	function drawChart() {
		var userData;
		var postData;
		var logData;
		$.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/admin/getDataTable",
		   	 data : "getdata",
		   	 success : function(data) {
		   		userData = data.userData;
		   		drawUserChart(userData);
		   		postData = data.postData;
		   		drawPostChart(postData);
		   		logData = data.logData;
		   		drawLogChart(logData);
		   	 },
		   	 error : function(e) {
		   		 alert("Đã xảy ra lỗi");
		   	 }
		});
	}
	function drawUserChart(userData) {
        var table = [];
        table.push(['Ngày', 'Số lượng']);
        for (var i=0;i<userData.length;i++) {
        	var date = new Date(userData[i].date)
        	var stringDate = date.toString().split(" ")
        	var formatDate = stringDate[2]+"/"+stringDate[1]+"/"+stringDate[3];
        	table.push([formatDate,userData[i].number])
        }
        var data = google.visualization.arrayToDataTable(
        		table
        	);
        var chart = new google.visualization.AreaChart(document.getElementById('chart_account'));
        chart.draw(data, options1);
		chart = new google.visualization.ColumnChart(document.getElementById('chart_user_count'));
		chart.draw(data, options2);
    }
	function drawPostChart(postData) {
        var table = [];
        table.push(['Ngày', 'Số lượng']);
        for (var i=0;i<postData.length;i++) {
        	var date = new Date(postData[i].date)
        	var stringDate = date.toString().split(" ")
        	var formatDate = stringDate[2]+"/"+stringDate[1]+"/"+stringDate[3];
        	table.push([formatDate,postData[i].number])
        }
        var data = google.visualization.arrayToDataTable(
        		table
        	);
        var chart = new google.visualization.AreaChart(document.getElementById('chart_post'));
        chart.draw(data, options1);
		chart = new google.visualization.ColumnChart(document.getElementById('chart_post_count'));
		chart.draw(data, options3);
    }
	function drawLogChart(logData) {
		console.log(logData)
        var table = [];
        table.push(['Ngày', 'Số lượng']);
        for (var i=0;i<logData.length;i++) {
        	var date = new Date(logData[i].date)
        	var stringDate = date.toString().split(" ")
        	var formatDate = stringDate[2]+"/"+stringDate[1]+"/"+stringDate[3];
        	table.push([formatDate,logData[i].number])
        }
        var data = google.visualization.arrayToDataTable(
        		table
        	);
        var chart = new google.visualization.AreaChart(document.getElementById('chart_log'));
        chart.draw(data, options1);
		chart = new google.visualization.ColumnChart(document.getElementById('chart_log_count'));
		chart.draw(data, options4);
    }
	var score = document.getElementById("disk-space-score").innerText;
	var fill = document.getElementById("disk-space-fill")
	fill.style.height = score;
	score = document.getElementById("ram-space-score").innerText;
	fill = document.getElementById("ram-space-fill")
	fill.style.height = score;
	score = document.getElementById("cpu-space-score").innerText;
	fill = document.getElementById("cpu-space-fill")
	fill.style.height = score;
	score = document.getElementById("bandwidth-space-score").innerText;
	fill = document.getElementById("bandwidth-space-fill")
	fill.style.height = score;
	
function activeAccount(userId) {
	const buttonActive = document.getElementById("buttonActive"+userId);
	if(buttonActive.classList.value.includes("isActive")) userStatus = "enabled";
	else userStatus = "locked";
	$.ajax({
		   	 type : "POST",
		   	 contentType : "application/json",
		   	 url : "/SocialNetwork/admin/activeAccount/"+userId,
		   	 data : userStatus,
		   	 success : function(data) {
		   		if(data) {
		   			buttonActive.classList.add("isActive")
		   			buttonActive.innerHTML = "Đã kích hoạt"
		   		}
		   		else {
		   			buttonActive.classList.remove("isActive");
		   			buttonActive.innerHTML = "Bị khóa"
		   		}
		   	 },
		   	 error : function(e) {
		   		 alert("Đã xảy ra lỗi");
		   	 }
	});
}

function searchUser(){
	var searchText = document.getElementById("userSearchText").value.toUpperCase();
	var table = document.getElementById("userTable");
	var tr = table.getElementsByTagName("tr");
	if(searchText != null) {
	for(let i=1;i<tr.length;i++){
		var tdEmail = tr[i].getElementsByTagName("td")[1];
		var tdName = tr[i].getElementsByTagName("td")[2];
		if(tdEmail!=null && tdName != null) {
			var tdText = tdEmail.innerText.toUpperCase();
			if(tdText.indexOf(searchText) < 0) tr[i].style.display = "none";
			else tr[i].style.display ="";
			tdText = tdName.innerText.toUpperCase();
			if(tdText.indexOf(searchText) < 0) tr[i].style.display = "none";
			else tr[i].style.display ="";
		}
	}
	}
	else {
		for(let i=1;i<tr.length;i++) {
			tr[i].style.display = "";
		}
	}
} 
function searchPost(){
	var searchText = document.getElementById("postSearchText").value.toUpperCase();
	var table = document.getElementById("postTable");
	var tr = table.getElementsByTagName("tr");
	if(searchText != null) {
	for(let i=1;i<tr.length;i++){
		var tdEmail = tr[i].getElementsByTagName("td")[1];
		var tdName = tr[i].getElementsByTagName("td")[2];
		if(tdEmail!=null && tdName != null) {
			var tdText = tdEmail.innerText.toUpperCase();
			if(tdText.indexOf(searchText) < 0) tr[i].style.display = "none";
			else tr[i].style.display ="";
			tdText = tdName.innerText.toUpperCase();
			if(tdText.indexOf(searchText) < 0) tr[i].style.display = "none";
			else tr[i].style.display ="";
			tdText = tdName.innerText.toUpperCase();
			if(tdText.indexOf(searchText) < 0) tr[i].style.display = "none";
			else tr[i].style.display ="";
		}
	}
	}
	else {
		for(let i=1;i<tr.length;i++) {
			tr[i].style.display = "";
		}
	}
} 
function deletePost(postId) {
	$.ajax({
	   	 type : "POST",
	   	 contentType : "application/json",
	   	 url : "/SocialNetwork/admin/deletePost",
	   	 data : postId,
	   	 success : function(data) {
	   		 console.log(data)
	   			if(data != -1) {
	   				var tr = document.getElementById("tr"+data);
	   				tr.remove();
	   			}
	   	 },
	   	 error : function(e) {
	   		 alert("Đã xảy ra lỗi");
	   	 }
});
}