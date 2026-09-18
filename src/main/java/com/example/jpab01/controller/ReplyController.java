package com.example.jpab01.controller;
/* 브라우저와 백엔드가 만나는 입구 */
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.dto.ReplyDTO;
//import com.example.jpab01.service.ReplyService;

//import io.swagger.annotations.ApiOperation;
import com.example.jpab01.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController // REST API 만드는 Controller
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /* 댓글 등록 API */
    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register
                (@Valid @RequestBody ReplyDTO replyDTO,
                BindingResult bindingResult) throws BindException {
        // @Valid : DTO의 @NotNull @NotEmpty 실행
        // @RequestBody : JSON을 받아서 ReplyDTO로 변환해주는 역할

        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
            // replyText 비워버리면 직접 예외 발생 -> bindingResult 다루는 CustomRestAdvice로 감
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);
        resultMap.put("rno", rno);

        return resultMap; // Service -> ... -> DB에서 받아온 새 댓글 번호를 JSON으로 보냄
    }


    /* 댓글 목록 조회 API */
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList
                (@PathVariable("bno") Long bno,
                 PageRequestDTO pageRequestDTO){
        // ex) Get /replies/list/5?page=1&size=10 -> "5번 게시글의 댓글 1페이지를 가져와라."

        PageResponseDTO<ReplyDTO> responseDTO
                = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }


    /* 단건 조회 API */
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO
                (@PathVariable("rno") Long rno){

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }


    /* 삭제 API */
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove
                (@PathVariable("rno") Long rno){

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap; // 다시 JSON에 "rno":10 돌려줌 -> 알림창에 몇 번이 삭제되었는지 표기 가능
    }


    /* 수정 API */
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> remove
                    (@PathVariable("rno") Long rno,
                     // -> url에서 10가져옴
                     @RequestBody ReplyDTO replyDTO ){
                     // JSON에서 "replyText" 가져옴

        replyDTO.setRno(rno); // 번호를 일치시킴
        // 둘을 합침 -> rno=10, replyText="수정된 댓글"

        replyService.modify(replyDTO); // 여기서 수정

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }

}