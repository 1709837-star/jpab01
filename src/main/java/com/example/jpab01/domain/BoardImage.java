package com.example.jpab01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board") // bno 겹치니까 이거 제외한 부분 제외하고 가져오기
public class BoardImage implements Comparable<BoardImage>
{
    @Id
    private String uuid;
    private String fileName;
    private int ord;

    // BoardImage.java의 @ManyToOne 과 Board.java의 @OneToMany 관계로
    // JPA가 자동으로 DB 테이블 생성해 줌 (위에 @Entity), 필드들도 칼럼이 됨
    @ManyToOne
    private Board board;

    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord; // Board.java의 ord (이미지 순서)
    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}