package com.example.jpab01.controller;

import com.example.jpab01.dto.upload.UploadFileDTO;
import com.example.jpab01.dto.upload.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Tag(name="upload", description="파일 업로드 REST API")
@RestController
@RequestMapping("/upload")
@Slf4j
@RequiredArgsConstructor
public class UpDownController {

    @Value("${com.example.upload.path}") // import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    @Operation(summary = "파일 업로드")
    @PostMapping(value= "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        log.info("upload: {}", uploadFileDTO);

        if(uploadFileDTO.getFiles() !=null) {

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename(); // 서버 콘솔에 파일명 출력
                log.info(originalName);

                String uuid = UUID.randomUUID().toString(); // 중복 위험 없는 큰 숫자 생성
                Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);

                boolean image = false; // 기본은 이미지인지 아닌지 모르고, 밑 if 문에서 설정

                try {
                    multipartFile.transferTo(savePath); // 실제파일 물리적으로 저장

                    // 이미지 파일의 종류라면
                    if(Files.probeContentType(savePath).startsWith("image")) {

                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(),thumbFile, 200, 200);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(image)
                        .build()
                );
            });
            return list;
        }

        return null;
    }

    @Operation(summary = "첨부파일 조회")
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileName") String fileName) {

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(summary = "첨부파일 삭제")
    @DeleteMapping("/{fileName}")
    public Map<String, Boolean> deleteFile(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            // 썸네일이 존재한다면
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator +"s_" + fileName);
                thumbnailFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }
}
