package com.example.jpab01.controller;
/* 사용자의 요청을 처음 받는 곳 */
import com.example.jpab01.dto.BoardDTO;
import com.example.jpab01.dto.BoardListReplyCountDTO;
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /* list */
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListReplyCountDTO> responseDTO =
                boardService.listWithReplyCount(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
        // -> html에 전달

    }

    /* register */
        /* 등록 버튼 누르기 전 */
    @GetMapping("/register")
    public void registerGET(){

    }

        /* 등록 버튼 누른 후 */
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        // BoardDTO의 검사 결과를 확인
        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);
        // flash - redirect 하면서 데이터를 잠깐 전달할 때 사용

        return "redirect:/board/list";
    }


//    @GetMapping("/read")
//    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
//
//        BoardDTO boardDTO = boardService.readOne(bno);
//
//        log.info(boardDTO);
//
//        model.addAttribute("dto", boardDTO);
//
//    }

    /* read, modify - 상세 목록 하나 */
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        // -> html로 전달

    }

        /* 수정 버튼 누르고 난 후 */
    @PostMapping("/modify")
    public String modify( PageRequestDTO pageRequestDTO,
                          @Valid BoardDTO boardDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes){

        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();
            // ex) link = page=3&size=10&keyword=스프링&type=t가 만들어짐

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            // -> bno를 직접 url에 안 붙여도 Spring이 알아서 붙여줌

            return "redirect:/board/modify?"+link;
            // -> /board/modify?bno=15&page=3&size=10&keyword=스프링&type=t로 다시 감 + bno
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        // -> bno를 직접 url에 안 붙여도 Spring이 알아서 붙여줌

        return "redirect:/board/read"; // -> /board/read?bno=15
    }

    /* 삭제 버튼 누르고 난 후 */
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }


}
