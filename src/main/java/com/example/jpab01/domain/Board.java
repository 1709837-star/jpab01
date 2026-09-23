package com.example.jpab01.domain;
/* Board  : 실제 게시판 데이터를 표현하는 Entity (내가 만듦) */
/* QBoard : Board를 대상으로 SQL 조건을 만들 때 사용하는 도구 */
/* QueryDSL : 복잡한 DB 검색 조건(select * from ...)을 Java 코드로 편하게 작성 */
/* QueryDSL이 Board.java의 @Entity를 보고 QBoard(검색용 Java 클래스)를 자동으로 생성 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "board")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 250, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(length = 45, nullable = false)
    private String writer;

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /* 이미지 작업 추가 */

    // BoardImage.java의 @ManyToOne 과 Board.java의 @OneToMany 관계로
    // JPA가 자동으로 DB 테이블 생성해 줌
    @OneToMany(mappedBy = "board",
                cascade = {CascadeType.ALL},
                // 학과코드를 학생테이블에 외래키로 들고 올 때, 학과테이블에서 하나의 학과 코드를 다 지우면 오류가 남! 이걸 가능하게 하는 코드
                fetch = FetchType.LAZY,
                orphanRemoval = true)
                // 일단 조회하지 않고 있다가 코드에서 imageSet을 사용하면 그 때 이미지를 다룸!
    @Builder.Default
    @BatchSize(size=20)
    private Set<BoardImage> imageSet = new HashSet<>();
    // "Board 하나가 여러 개의 BoardImage를 가지고 있네?" (1:N 관계)
    // BUT, DB에는 Set, HashSet, List 같은 자료 구조가 x
    // -> JPA가 관계를 DB에 저장하기 위한 방법을 만들어야 함
    // -> 중간 테이블(조인 테이블) : board_image_set

    // board_image : BoardImage 자체를 저장하는 테이블
    // board_image_set : "어떤 Board가 어떤 BoardImage를 갖고 있는가?" - 양방향 참조

    // mappedBy = "board" : "Board와 BoardImage의 관계는 BoardImage의 board 필드가 관리하고 있어."
    // -> DB에서는 BoardImage 쪽에 FK가 생기는 방식으로 처리 (Board.bno=BoardImage.bno)
    // -> 이러면 board_image_set(조인테이블) 필요x, 삭제


    /* 게시글에 첨부 이미지 하나 추가 */
    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder() // 이미지 객체 만들기
                .uuid(uuid) // 파일 정보 넣기
                .fileName(fileName) // 파일 정보 넣기
                .board(this) // this: 현재 Board 객체. "이 이미지는 지금 이 게시글에 속해 있어."
                .ord(imageSet.size()) // 이미지 순서 정하기 -> 광안리.jpg : ord=0, 해운대.jpg : ord=1
                .build();
        imageSet.add(boardImage); // 만들어진 이미지를 현재의 Board 이미지 목록에 추가
    }

    /* 게시글에 연결된 기존 이미지 전부 제거 */
    public void clearImages() {

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        // 현재 이미지를 하나씩 꺼내서, "이 이미지는 더 이상 이 Board에 속하지 않아." 관계 끊어주기

        this.imageSet.clear(); // Board가 가지고 있던 이미지 목록 자체를 비움.
        // 이것만 쓰면, Board가 가지고 있는 컬렉션에서는 빠지지만, BoardImage 입장에서는 여전히 board = 기존 board 로 생각함
    }
}
