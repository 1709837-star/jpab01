package com.example.jpab01.domain;
/* Board  : 실제 게시판 데이터를 표현하는 Entity (내가 만듦) */
/* QBoard : Board를 대상으로 SQL 조건을 만들 때 사용하는 도구 */
/* QueryDSL : 복잡한 DB 검색 조건(select * from ...)을 Java 코드로 편하게 작성 */
/* QueryDSL이 Board.java의 @Entity를 보고 QBoard(검색용 Java 클래스)를 자동으로 생성 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}
