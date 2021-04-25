package com.example.demo.feign;

import com.example.demo.core.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="huya", url = "https://huya.com")
public interface HuyaFeign {
    @GetMapping("/search")
    String s(@RequestParam String hsk);
    //String s(@RequestBody SearchReq req);


}
