package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	String uploadFolder = "C:\\upload";
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		
		
		//업로드된 파일
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-----------------------------------");
			log.info("Upload File Name: "+multipartFile.getOriginalFilename());
			log.info("Upload File Size: "+multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				//지금 파일을 지정한 경로와 이름으로 저장한다.
				multipartFile.transferTo(saveFile);
			}catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	@GetMapping("uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	@PostMapping("/uploadAjaxAction")
	@ResponseBody
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("update ajax post.........");
		
		//return할 list 생성
		List<AttachFileDTO> list = new ArrayList<>();
		
		//getFolder 함수를 이용해서 업로드할 함수 경로 지정(오늘날짜로)
        String uploadFolderPath = getFolder();
        
        //맨 위에 지정한 경로와 지금 경로를 합쳐서 사용
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        log.info("upload Path: " + uploadPath);
        
        if(uploadPath.exists() == false) {
        	uploadPath.mkdirs();
        }
        
        for (MultipartFile multipartFile : uploadFile) {

            log.info("-------------------------------------");
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            log.info("Upload File Size: " + multipartFile.getSize());
            
            //DTO생성
            AttachFileDTO attachDTO = new AttachFileDTO();
            
            //업로드할 파일 이름을 원본 파일 이름으로 가져온다
            String uploadFileName = multipartFile.getOriginalFilename();

            // IE has file path
            //마지막 구분자 뒤에 있는 이름을 가져온다 -> 파일 이름
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            
            //DTO에 파일 이름 할당
            log.info("only file name: " + uploadFileName);
            attachDTO.setFileName(uploadFileName);
            
            //UUID 생성
            UUID uuid = UUID.randomUUID();
            
            //파일 이름을 생성한 uuid_와 결합시킨다.
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
            	//지정한 경로로(여기서는 C:\\upload) uuid_와 결합한 파일을 저장한다.
            	File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);
                
                //dto에 생성한 uuid와 폴더 경로를 저장한다.
                attachDTO.setUuid(uuid.toString());
                attachDTO.setUploadPath(uploadFolderPath);
                
                //이미지 파일이면
                if(checkImageType(saveFile)) {
                	//attachDTO에 이미지임을 표시
                	attachDTO.setImage(true);
                	//썸네일 생성(s_접두사를 붙인 파일명으로)
                	FileOutputStream thumbnail =
                			new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
                	
                	//파일 생성, width와 height 지정
                	Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                	
                	thumbnail.close();
                }
                //리스트에 추가
                list.add(attachDTO);

            } catch (Exception e) {
                log.error(e.getMessage());
            } // end catch

        } // end for
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	private String getFolder() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        //현재 날짜와 시간 (연_월_일)
        return str.replace("_", File.separator);
    }
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
}
