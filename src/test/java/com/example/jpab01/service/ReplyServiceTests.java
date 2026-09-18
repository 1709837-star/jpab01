package com.example.jpab01.service;
/* Service가 DTO를 받아 Repository까지 연결해서 저장하는지 테스트 */
import com.example.jpab01.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService; // 스프링이 ReplyService 주입

    @Test
    public void testRegister() {

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("22")
                .replyer("정조")
                .bno(200L)
                .build();

        log.info(replyService.register(replyDTO));
        // -> ReplyServiceImpl의 register(ReplyDTO replyDTO) 실행
    }

}
