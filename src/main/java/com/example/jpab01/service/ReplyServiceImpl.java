package com.example.jpab01.service;
/* 실제 댓글 업무 수행 */
import com.example.jpab01.domain.Reply;
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.dto.ReplyDTO;
import com.example.jpab01.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service // "Spring아, 얘는 Service 역할을 하는 클래스야."
@RequiredArgsConstructor // final 필드들을 생성자로 자동 주입
@Log4j2
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    /* 댓글 등록 */
    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = modelMapper.map(replyDTO, Reply.class);
        // DTO -> Entity로 바꿈
        Long rno = replyRepository.save(reply).getRno();
        // save(DB에 저장) -> getRno(DB가 만들어준 댓글 번호 가져옴)
        return rno;
        // Controller로 댓글 번호 돌려줌
    }

    /* 댓글 하나 조회 */
    @Override
    public ReplyDTO read(Long rno) {

        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();
        return modelMapper.map(reply, ReplyDTO.class);
        // Entity -> DTO로 Controller에 넘겨줌
    }

    /* 댓글 수정 */
    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow();
        // rno=999 댓글이 없다면 orElseThrow() -> NoSuchElementException 발생
        // 이 예외가 Controller까지 올라감 -> 500 Internal Server Error 같은 서버 오류 응답
        // -> CustomRestAdvice가 받아서 우리가 원하는 JSON으로 바꿔줌
        reply.changeText(replyDTO.getReplyText());
        replyRepository.save(reply);
        // DB에 반영

    }

    /* 댓글 삭제 */
    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);

    }

    /* 특정 게시글의 댓글 목록 */
    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(
            Long bno, PageRequestDTO pageRequestDTO) {
            // "bno 게시글의 댓글을 페이지 단위로 가져와라."

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() <=0
                        ? 0
                        : pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending()
                // 댓글 번호 오름차순
        );

        Page<Reply> result =
                replyRepository.listOfBoard(bno, pageable);
        // 실제로 "이 게시글의 댓글을 가져와."가 실행됨

        List<ReplyDTO> dtoList =
                result.getContent().stream()
                        .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());
        // DB에서 가져온 Entity -> DTO 로 변환


        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
        // 댓글 목록만 던지는 게 아니라 페이징 정보까지 묶어서 반환
    }
}
