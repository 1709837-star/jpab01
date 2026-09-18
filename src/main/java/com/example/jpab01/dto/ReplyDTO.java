package com.example.jpab01.dto;
/* DTO : 댓글 데이터를 전달하는 상자 */
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

    private Long rno; // 댓글 번호

    @NotNull
    private Long bno; // 게시글 번호 - 어떤 게시글에 달린 댓글인지

    @NotEmpty
    private String replyText;

    @NotEmpty
    private String replyer;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    @JsonIgnore // "JSON으로 변환할 때 이 필드는 제외해라."
    private LocalDateTime modDate;

}
