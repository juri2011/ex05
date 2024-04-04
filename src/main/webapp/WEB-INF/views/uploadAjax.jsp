<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<title>Upload with Ajax</title>
</head>
<body>
	<h1>Upload with Ajax</h1>
	<div class='uploadDiv'>
		<input type="file" name="uploadFile" multiple />
	</div>
	
	<button id="uploadBtn">upload</button>
	
	

	
</body>

<script>
	$(document).ready(function(){
		$('#uploadBtn').on('click',function(){
			
			let formData = new FormData();
			let inputFile = $("input[name='uploadFile']");
			let files = inputFile[0].files;
			
			console.log(files);
		});
	});
</script>	
</html>