package com.example.jpab01.dto;
/* 게시글 데이터를 Controller와 Service 사이에서 전달하는 객체 */
/* 게시글 상세 페이지 */
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    // @애너테이션 - validation 조건 검사
    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    // 첨부파일의 이름들을 DTO에 담아두기
    private List<String> fileNames;
}
