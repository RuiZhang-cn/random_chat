var websocket = null;
var host = document.location.host;
$(function(){
	extracted();
	window.addEventListener("keydown", function (event) {
		if (event.key !== undefined) {
			// 使用keyboardvent.key处理事件，并将handled设置为true。
			if(event.key=="Enter"){
				send();
			}
		}
	}, true);
	window.onbeforeunload = function (e) {
		var warning="You will quit daks system, are you sure?";
		return warning;
	};
});
function send(){
	let data = $('#neirong').val();
	if (data!==null&&data!=""){
		$('#UI').append("<div class=\"cright cmsg\">\n" +
			"\t\t\t\t\t\t<img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\"  src=\"/images/A.png\" />\n" +
			"\t\t\t\t\t\t<span class=\"name\"> &nbsp; </span>\n" +
			"\t\t\t\t\t\t<span class=\"content\">"+data+"</span>\n" +
			"\t\t\t\t\t</div>");
		websocket.send(data);
		$('#neirong').val('');
	}
}


function upload_file(){
 var formData = new FormData();
    // Attach file
    formData.append('file', document.getElementById('input_upload').files[0]);
    // console.log(formData);

    $.ajax({
      url: '/uploadFile',
      data: formData,
      method: 'POST',
      contentType: false, // NEEDED, DON'T OMIT THIS (requires jQuery 1.6+)
      processData: false, // NEEDED, DON'T OMIT THIS
      success: function(result) {
		  var ex = result.substring(result.lastIndexOf("."));
		  if (ex===".jpg"||
			ex===".jpeg"||
			ex===".png"||
			ex===".gif"
		){
			  $('#UI').append("<div class=\"cright cmsg\">\n" +
				  "\t\t\t\t\t\t<img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\"  src=\"/images/A.png\" />\n" +
				  "\t\t\t\t\t\t<span class=\"name\"> &nbsp; </span>\n" +
				  "\t\t\t\t\t\t<span class=\"content\"><img src=\"/file/"+result+"\"/></span>\n" +
				  "\t\t\t\t\t</div>");
			  websocket.send("img:"+result);
		  }else {
			  $('#UI').append("<div class=\"cright cmsg\">\n" +
				  "\t\t\t\t\t\t<img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\"  src=\"/images/A.png\" />\n" +
				  "\t\t\t\t\t\t<span class=\"name\"> &nbsp; </span>\n" +
				  "\t\t\t\t\t\t<span class=\"content\"><video width=\"320\" height=\"240\" controls><source type='video/mp4' src='/file/" + result + "'></video></span>\n" +
				  "\t\t\t\t\t</div>")
			  websocket.send("video:"+result);
		  }
      },
      error: function(responseStr) {
        alert("error", responseStr);
      }
    });
}
function extracted() {
	//判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
        var protocolStr = document.location.protocol;
        if(protocolStr == "http:")
        {
       	    websocket = new WebSocket("ws://" + host + "/websocket");
        }
        else if(protocolStr == "https:")
        {
         	websocket = new WebSocket("wss://" + host + "/websocket");
        }else{
        		alert('你的浏览器不支持该网站');
        }
	} else {
		alert('你的浏览器不支持该网站');
	}
	//连接发生错误的回调方法
	websocket.onerror = function (e) {
		console.log('连接发生错误' + e)
	};

	//连接成功建立的回调方法
	websocket.onopen = function (event) {
		console.log('连接成功建立');
	};
	//接收到消息的回调方法
	websocket.onmessage = function (event) {
		console.log(event);
		console.log(event.data.replace("\\\"", "\""));
		var obj = jQuery.parseJSON(event.data.replace("\\\"", "\""));
		if (obj.code==1000){
			console.log(obj.code);
			if(obj.type==1){
			 $('#UI').append("<div class=\"cleft cmsg\">\n" +
                  			"\t\t\t\t\t\t<img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\"  src=\"/images/A.png\" />\n" +
                  			"\t\t\t\t\t\t<span class=\"name\"> &nbsp; </span>\n" +
                  			"\t\t\t\t\t\t<span class=\"content\"><img src=\"/file/"+obj.msg+"\"/></span>\n" +
                  			"\t\t\t\t\t</div>");
			}else if (obj.type == 2) {
				$('#UI').append("<div class=\"cleft cmsg\">\n" +
					"\t\t\t\t\t\t<img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\"  src=\"/images/A.png\" />\n" +
					"\t\t\t\t\t\t<span class=\"name\"> &nbsp; </span>\n" +
					"\t\t\t\t\t\t<span class=\"content\"><video width=\"320\" height=\"240\" controls><source type='video/mp4' src='/file/" + obj.msg + "'/></video></span>\n" +
					"\t\t\t\t\t</div>")
			} else{
			$('#UI').append("\n" +
				"             \t\t<div class=\"cleft cmsg\">\n" +
				"             \t\t    <img class=\"headIcon radius\" ondragstart=\"return false;\"  oncontextmenu=\"return false;\" src=\"/images/A.png\" />\n" +
				"             \t\t    <span class=\"name\"> &nbsp; </span>\n" +
				"             \t\t    <span class=\"content\">"+obj.msg+"</span>\n" +
				"             \t\t</div>")
				}
		}else if (obj.code == 1003) {
			$('#neirong').removeAttr("disabled");
			$('#UI').append("<div class=\"tips\">\n" +
				"        <span class='tips-success'>" + obj.msg + "</span>\n" +
				"    </div>")
		} else if (obj.code == 1005) {
			$('#UI').append("<div class=\"tips\">\n" +
				"        <span class='tips-danger'>" + obj.msg + "</span>\n" +
				"    </div>")
		} else if (obj.code == 1006) {
			$('#UI').append("<div class=\"tips\">\n" +
				"        <span class='tips-warning'>" + obj.msg + ",请关闭其余窗口后刷新页面</span>\n" +
				"    </div>")
		}
	};

	//连接关闭的回调方法
	websocket.onclose = function (e) {
		console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean);
		if (e.code == 1006 && e.reason == false) {
			$('#UI').append("<div class=\"tips\">\n" +
				"        <span class='tips-warning'>由于您的等待时间过长,服务器不忍心再看到，您的链接已被服务器关闭</span>\n" +
				"    </div>")
		}
		console.log('连接關閉')
	};

	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function () {
		websocket.close();
	}
}
