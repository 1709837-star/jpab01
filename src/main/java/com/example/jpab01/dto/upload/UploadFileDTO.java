package com.example.jpab01.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {

    private List<MultipartFile> files;
    // 여러개를 저장할 수 있는 객체<배열>
}
