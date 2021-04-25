package com.example.demo.feign;

import com.example.demo.annotation.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="baidu", url = "127.0.0.1:8090")
public interface TestFeign {
    @GetMapping("/s")
    //String s(@RequestParam String wd);
    String s(@RequestBody SearchReq req);

    @PostMapping
    //String r(@RequestParam String r);
    @ResponseEntity RealLevelRolesResp r(@RequestParam String r);

    @GetMapping("/getJson")
    DemoVo getJson();

}
