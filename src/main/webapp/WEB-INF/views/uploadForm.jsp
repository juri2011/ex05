<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
	<form action="uploadFormAction" method="post" enctype="multipart/form-data">
		<!-- 환경설정으로 맞춘 파일 설정에 맞춰서 들어감 -->
		<input type="file" name='uploadFile' multiple />
		
		<button>submit</button>
	</form>
</body>
</html>
