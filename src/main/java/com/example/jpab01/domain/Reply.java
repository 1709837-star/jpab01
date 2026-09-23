package com.example.jpab01.domain;
/* DB 테이블과 연결되는 실제 Entity */
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
//@ToString(exclude = "board")
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@ToString
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 번호를 자동으로 만들어 줌
    // -> DB의 PK
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY) // ★ "댓글 여러개가 하나의 게시글에 속한다" ★
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String text) {

        this.replyText = text;
    } // 수정할 때 사용하는 메서드
}
