var websocket = null;

var selectedUserId = "";
var selectedUserName = "";

function connectWebsocket() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    if (username === "" || password === "") {
        alert("请输入用户名和密码");
        return;
    }
    websocket = new WebSocket("ws://127.0.0.1:8000/imServer/" + username + "/" + password);

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function (data) {
        setMessageInnerHTML("WebSocket连接成功");
        console.log(data);
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let jsonObj = JSON.parse(event.data)
        console.log(jsonObj);
        if (jsonObj.messageType === 1) {
            setSelectOption(JSON.parse(jsonObj.msg))
        } else {
            setMessageInnerHTML(jsonObj);
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }
}


//将消息显示在网页上
function setMessageInnerHTML(jsonObj) {
    if (typeof jsonObj === 'object') {
        var username = jsonObj.fromUserName;
        var type = jsonObj.messageType;

        //消息类型type：0.自己，1.别人
        if (0 === type) {
            document.getElementById('message').innerHTML += '<div style="text-align: center">' + jsonObj.currentDate + '</div>';// 消息发送时间
            document.getElementById('message').innerHTML += '<div style="text-align: right;">' + "我" + '</div>';// 消息发送人
            document.getElementById('message').innerHTML += '<div style="text-align: right; margin-right: 30px;">' + jsonObj.msg + '</div>';// 内容
            document.getElementById('message').scrollTop = document.getElementById('message').scrollHeight;// 当出现滚动条时，滚动条将自动保持在底部
        } else {
            document.getElementById('message').innerHTML += '<div style="text-align: center">' + jsonObj.currentDate + '</div>';// 消息发送时间
            document.getElementById('message').innerHTML += '<div style="text-align: left;">' + username + '</div>';// 消息发送人
            document.getElementById('message').innerHTML += '<div style="text-align: left; margin-left: 30px;">' + jsonObj.msg + '</div>';// 内容
            document.getElementById('message').scrollTop = document.getElementById('message').scrollHeight;// 当出现滚动条时，滚动条将自动保持在底部
        }
    } else {
        document.getElementById('message').innerHTML += '<div>' + jsonObj + '</div>';
    }
    // try {
    //
    // } catch (e) {
    //
    // }
}

function getSelectUser(userName, userId) {
    selectedUserId = userId;
    selectedUserName = userName;
    // console.log(userName,userId);
}

//发送消息
function send() {
    var message = document.getElementById('text').value;
    var toUserId = selectedUserId;
    if (toUserId === '') {
        alert("未选择任何用户！");
        return;
    }
    var jsonObj = {msg: message, toUserId: toUserId, messageType: 3};
    websocket.send(JSON.stringify(jsonObj));
}


function setSelectOption(data) {
    let element = document.getElementById("userList");
    for (let i = 0; i < data.length; i++) {
        let op = document.createElement("option");
        op.setAttribute("label", data[i].userName);
        op.setAttribute("value", data[i].userId);
        element.appendChild(op);
    }

}