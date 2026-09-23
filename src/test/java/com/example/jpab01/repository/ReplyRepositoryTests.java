package com.example.jpab01.repository;
/* “Repository에서 댓글이 실제 DB에 들어가는지” */
/* 전체 흐름 : ReplyRepositoryTests -> ReplyRepository -> JPA -> DB */
import com.example.jpab01.domain.Board;
import com.example.jpab01.domain.Reply;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    /* 댓글 100개 넣기 */
    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            // for(int i=1; i<=100; i++ { 와 동일

            Long bno = 206L; //실제 DB에 있는 bno

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .board(board) // bno=100인 Board
                    .replyText("댓글.......")
                    .replyer("정약용")
                    .build(); // rno는 DB가 자동으로 번호 만들어줌 (Entity에서 설정)

            replyRepository.save(reply); // 실제 DB 저장
        });
    }

    /* DB에 들어간 댓글을 조회 */
    @Transactional
    @Test
    public void testBoardReplies() {

        Long bno = 100L; // "100번 게시글의 댓글을 가져와!"

        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());
        // 0: 첫 번째 페이지, 10: 한 페이지에 댓글 10개, rno기준으로 내림차순(최신순)
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        // ex) listOfBoard(1L, pageable) : 1번 게시글의 댓글들만 가져와라.
        result.getContent().forEach(reply -> {
            log.info(reply);
            // result는 Page<Reply>, 댓글뿐만 아니라 페이지 정보도 같이 들어있음
            // -> 실제 댓글 목록만 꺼내는 게 result.getContent()
        });
    }

}

