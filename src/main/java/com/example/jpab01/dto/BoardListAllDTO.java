package com.example.jpab01.dto;
/* 기존 게시글 목록 DTO + 이미지 목록 추가 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListAllDTO {

    private Long bno;

    private String title;

    private String writer;

    private LocalDateTime regDate;

    private Long replyCount;


    private List<BoardImageDTO> boardImages;
    // ★ 하나의 게시글 DTO 안에 그 게시글에 연결된 이미지 여러 개를 넣겠다.
}