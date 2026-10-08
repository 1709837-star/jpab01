package com.example.jpab01.service;

import com.example.jpab01.dto.*;
import com.example.jpab01.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("스위스에서 보물찾기")
                .content("스위스")
                .writer("유관순")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify() {

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated....101")
                .content("Updated content 101...")
                .build();

        // 첨부파일 : 새로운 파일명 정보를 DTO에 담기
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

        boardService.modify(boardDTO);

    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

    }

    /* 첨부파일이 있는 게시글을 등록 테스트 */
    @Test
    public void testRegisterWithImages() {

        log.info(boardService.getClass().getName());

        // 게시글 DTO 하나 만들기
        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        // fileNames에 3개의 파일 정보 넣기
        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_bbb.jpg"
                ));

        Long bno = boardService.register(boardDTO);
        // BoardDTO -> dtoEntity() -> Board에 title, content, writer, imageSet(...) -> repository.save(board) -> DB

        log.info("bno: " + bno);
    }

    /* 첨부파일이 있는 게시글 조회 테스트 */
    @Test
    public void testReadAll() {

        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }//end for

    }

    /* 게시글 목록 처리 (검색 + 댓글 수 + 이미지)가 제대로 이루어지는지 */
    @Test
    public void testListWithAll() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build(); // 1페이지 한 페이지에 10개로 조회해보겠다

        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList(); // 위에서 받아온 실제 목록 꺼내기

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }

            log.info("----------------------------------");
        });
    }


}
