<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="modify" method="post">
name: <input type="text" name="name" value="${name}"><br>
id: <input type="text" value="${id}" disabled="disabled" name="id"><br>
password: <input type="password" name="pw" value="${pw}"><br>
<input type="submit" value="Submit">
</form>
</body>
</html>