package com.example.jpab01.service;
/* 게시판에서 필요한 기능을 실제로 구현하는 곳 */
import com.example.jpab01.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.example.jpab01.domain.Board;
import com.example.jpab01.repository.BoardRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // "Spring아, 얘는 Service 역할을 하는 클래스야."
@Log4j2
@RequiredArgsConstructor // final 필드들을 생성자로 자동 주입
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper; // DTO <-> Entity 변환 담당
    private final BoardRepository boardRepository; // Service -> DB 연결 담당

    // boardRepository.save() findById() deleteByID() 등 메서드는
    // JpaRepository<Board, Long>으로 자동으로 제공받음

    /* 게시글 등록 */
    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = modelMapper.map(boardDTO, Board.class);
        // DTO는 전달용, JPA가 DB에 저장하려면 Entity인 Board가 필요, 형태 변환

        Long bno = boardRepository.save(board).getBno();
        // JpaRepository가 제공하는 save() 메서드 : Repository에 저장 -> getBno()로 게시글 번호 가져옴

        return bno; // -> 최종적으로 Controller에게 "저장됐어. 게시글 번호는 101이야." 라고 돌려주는 것
    }

    /* 게시글 하나 조회 */
    @Override
    public BoardDTO readOne(Long bno) {
        // 사용자가 /board/read?bno=100 으로 요청 -> Controller가 boardServce.readOne(100L) 호출

        Optional<Board> result = boardRepository.findById(bno);
        // DB에서 bno에 해당하는 100번 게시글 찾기

        Board board = result.orElseThrow();
        // 찾으면 꺼내서 board에 저장

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // board -> BoardDTO 변환

        return boardDTO; // 최종적으로 Controller로 반환
    }

    /* 게시글 수정 */
    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // DB에서 bno에 해당하는 게시글 찾기

        Board board = result.orElseThrow();
        // 찾으면 꺼내서 board에 저장

        board.change(boardDTO.getTitle(), boardDTO.getContent());
        // ★ Board Entity의 change() 메서드로 내용 변경

        boardRepository.save(board);
        // JpaRepository가 제공하는 save() 메서드 : Repository에 저장
    }

    /* 게시글 삭제 */
    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);
        // JpaRepository가 제공하는 deleteByID 메서드
    }

    /* 게시글 전체 목록 */
    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword(); // @Data가 keyword 객체로 자동으로 메서드 생성
        Pageable pageable = pageRequestDTO.getPageable("bno");
        // 사용자가 요청한 검색 조건 꺼내기
        // ex) 제목+내용, 검색어: "스프링", 2페이지, 10개씩 요청
        // -> types=["t", "c"] keyword="스프링" pageable= 2페이지, 10개 이런 정보가 만들어짐

        /* 검색 조건 넣는 부분 */
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        // BoardRepository -> BoardSearch -> BoardSearchImpl -> QueryDSL 작동 -> DB
        // searchAll() 호출할 수 있는 이유 : BoardRepository가 extends JpaRepository<Board, Long>, BoardSearch

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());
        // 위 메서드를 통해 DB에서 가져온 결과(Page<Board>)가 Entity 목록으로 돌아옴
        // List<Board> -> 각 Board를 BoardDTO로 변환 -> List<BoardDTO> (Controller 전달 위해서)

        /* 최종적으로 데이터 포장 */
        return PageResponseDTO.<BoardDTO>withAll()
                // PageResponseDTO를 builder 방식으로 만들기 시작하는 메서드
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
        // PageResponseDTO : PageRequestDTO + 게시글 DTO 목록 + 전체 게시글 수
        // -> Controller에서 PageResponseDTO<BoardDTO> responseDTO 사용 가능
    }

    /* 게시글 전체 + 댓글 개수 목록 */
    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        return null;
    }
}
