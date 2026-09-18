package com.example.jpab01.service;
/* 게시판에서 필요한 기능을 실제로 구현하는 곳 */
import com.example.jpab01.dto.BoardListReplyCountDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.jpab01.domain.Board;
import com.example.jpab01.dto.BoardDTO;
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.repository.BoardRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // "Spring아, 얘는 Service 역할을 하는 클래스야."
@Log4j2
@RequiredArgsConstructor // final 필드들을 생성자로 자동 주입
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    // boardRepository.save() findById() deleteByID() 등 메서드는
    // JpaRepository<Board, Long>으로 자동으로 제공받음

    /* register */
    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = modelMapper.map(boardDTO, Board.class);
        // DTO는 전달용, JPA가 DB에 저장하려면 Entity인 Board가 필요

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    /* readOne() */
    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);
        // DB에서 bno에 해당하는 게시글 찾기

        Board board = result.orElseThrow();
        // 찾으면 꺼내서 board에 저장

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // board -> BoardDTO 변환, Controller로

        return boardDTO;
    }

    /* modify() */
    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        // DB에서 bno에 해당하는 게시글 찾기

        Board board = result.orElseThrow();
        // 찾으면 꺼내서 board에 저장

        board.change(boardDTO.getTitle(), boardDTO.getContent());
        // Board Entity의 change() 메서드로 내용 변경

        boardRepository.save(board);
        // 저장
    }

    /* remove() */
    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }

//    @Override
//    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
//
//        String[] types = pageRequestDTO.getTypes();
//        String keyword = pageRequestDTO.getKeyword();
//        Pageable pageable = pageRequestDTO.getPageable("bno");
//
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//
//        return null;
//    }


    /* list */
    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        // 사용자의 검색 요청 꺼내기

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        // BoardRepository -> BoardSearch -> BoardSearchImpl -> QueryDSL 작동 -> DB
        // searchAll() 호출할 수 있는 이유 : BoardRepository가 extends JpaRepository<Board, Long>, BoardSearch

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());
        // DB에서 가져온 결과 List<Board> -> List<BoardDTO>로 변환


        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
        // PageResponseDTO : PageRequestDTO + 게시글 DTO 목록 + 전체 게시글 수
        // -> Controller에서 PageResponseDTO<BoardDTO> responseDTO 사용 가능
    }

    /* 댓글 */
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
}
