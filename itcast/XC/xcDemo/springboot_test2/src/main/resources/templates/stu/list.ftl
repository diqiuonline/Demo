<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
欢迎${name}
<table border="1">
    <tr>
        <td>ID</td>
        <td>名字</td>
    </tr>
    <#list list as stu>
        <tr>
        <td> ${stu.username}</td>
        <td> ${stu.password}</td>
        </tr>
    </#list>
</table>

</body>
</html>