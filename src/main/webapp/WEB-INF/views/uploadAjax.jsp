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
		//파일 확장자 제한
		const regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		const maxSize = 5242880 // 5MB;
		
		
		
		//파일 사이즈가 맞는지 확인
		function checkExtension(fileName, fileSize){
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			return true;
		}
		
	$(document).ready(function(){
		
		let cloneObj = $(".uploadDiv").clone();
		
		$('#uploadBtn').on('click',function(){
			
			let formData = new FormData();
			let inputFile = $("input[name='uploadFile']");
			let files = inputFile[0].files;
			
			console.log(files);
			
			//add filedata to formdata
			for(let i=0; i<files.length; i++){
				if(!checkExtension(files[i].name, files[i].size)){
					return false;
				}
				formData.append("uploadFile", files[i]);
			}
			
			//파일을 전송할 때는 processData와 contentType을 false로 지정해야 한다.
			//(그렇지 않으면 전송이 안 됨)
			$.ajax({
				url: '/uploadAjaxAction',
				processData: false,
				contentType: false,
				data: formData,
					type: 'POST',
					dataType: 'json',
					success: function(result){
						console.log(result);
						$(".uploadDiv").html(cloneObj.html());
					}
			}); //$.ajax
		});
	});
</script>	
</html>