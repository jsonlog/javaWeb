<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>测试</title>
    <script>
        document.cookie = 'Hello=World';
    </script>
</head>
<body>
<h2>登陆</h2>
<form action="login/123/管理员" enctype="multipart/form-data" method="post">
    <label for="username">用户名:</label>
    <input type="text" id="username" name="username"/>
    <label for="password">密码:</label>
    <input type="password" id="password" name="password"/>
    <input type="file" name="hehe"/>
    <input type="submit"/>
</form>
</body>
</html>
