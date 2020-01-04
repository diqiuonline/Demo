<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
    <script src="js/jquery-1.8.3.js" type="text/javascript"></script>
    <script>
        $(function () {
            $("#sendJms").click(function () {
                var telephone = $("#telephone").val();
                $.post("sendJms.do",{telephone:telephone},function (data) {
                    if (data == 'NONE') {
                        alert("短信发送成功")
                    }
                })
            });
            $("#regist").click(function () {
                $("#registFoem").submit();
            });
        })

    </script>
</head>

<body>
<form action="regist.do" method="post" id="registFoem">
    请输入用户名：<input type="text" name="username" > <br>
    请输入密码  ：<input type="password" name="password"> <br>
    请输入手机号 ：<input type="text" name="telephone" id="telephone"> <input type="button" value="获取手机验证码" id="sendJms"><br>
    请输入验证码：<input type="text" name="randomCode" id="randomCode">
    <input type="button" value="注册" id="regist">
</form>
</body>
</html>
