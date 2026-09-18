package com.example.jpab01.controller;

import com.example.jpab01.dto.SampleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello........");
        model.addAttribute("msg", "HELLO WORLD");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {

        List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD");
        model.addAttribute("list", list);
        // "html에서 list라는 이름으로 이 데이터를 사용할 수 있게 해줘."

        List<String> strList = IntStream.range(1, 10)
                .mapToObj(i -> "Data" + i)
                .collect(Collectors.toList());
        model.addAttribute("strList", strList);


            Map<String, Integer> maps = new HashMap<>();
            maps.put("홍길동", 80);
            maps.put("박경미", 75);
            maps.put("윤요섭", 85);
            model.addAttribute("maps", maps);
            // html에서 maps라는 이름으로 사용 가능 -> ${maps}

            SampleDTO sampleDTO = SampleDTO.builder()
                    .name("hong")
                    .age(20)
                    .gender("남자")
                    .build();
            model.addAttribute("sampleDTO", sampleDTO);
            // html에서 SampleDTO라는 이름으로 사용 가능 -> ${sampleDTO}
        }

    }