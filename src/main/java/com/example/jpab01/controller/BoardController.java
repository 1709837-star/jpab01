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
@RequiredArgsConstructor // BoardService 생성자 생성
public class BoardController {

    private final BoardService boardService;

    /* 게시글 전체 목록 */
    @GetMapping("/list") // /board/list 로 들어오면 실행되는 메서드
    public void list(PageRequestDTO pageRequestDTO, Model model){
    // PageRequestDTO : 사용자가 URl로 보낸 페이지/검색 관련 정보를 받아옴 ex) page=2&size=10&type=t&keyword=스프링
    // Model : HTML에 데이터를 전달하는 통로

        PageResponseDTO<BoardListReplyCountDTO> responseDTO =
                boardService.listWithReplyCount(pageRequestDTO);
        // "Service야, 게시글 전체 목록 + 댓글 개수가 필요해."

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
        // "responseDTO"라는 이름으로 html에 전달 -> ${responseDTO}로 사용 가능

    }

    /* 게시글 등록 */
        /* 등록 버튼 누르기 전 */
    @GetMapping("/register")
    public void registerGET(){ // 그냥 등록 화면 보여주기만 하면 되니까 코드 x

    }

        /* 등록 버튼 누른 후 */
    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult, // 검사결과 담고 있는 객체
                               RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        } // BoardDTO의 검사 결과를 확인 -> 오류 있으면 다시 등록 화면으로 보냄

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);
        // Service에게 저장 요청 -> 새로 만들어진 게시글 번호 bno 받음

        redirectAttributes.addFlashAttribute("result", bno);
        // flash - redirect 하면서 데이터를 잠깐 전달할 때 사용

        return "redirect:/board/list";
        // 게시글 저장 -> redirect -> GET /board/list -> list() -> 게시글 목록 출력
        // 이 패턴을 PRG(Post -> Redirect -> Get) 방식이라고 함.
    }


    /* 게시글 상세 조회 */
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        // 사용자가 /board/read?bno=100 요청하면 Spring이 Long bno에 100 넣어줌

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        // "dto"라는 이름으로 html에 전달 -> ${dto}로 사용 가능

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
        } // valid 유효성에 오류 확인 -> /board/modify?bno=15&page=3&size=10&keyword=스프링&type=t로 다시 감

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        // url 파라미터로 붙는 값. bno를 직접 url에 안 붙여도 Spring이 알아서 붙여줌

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
