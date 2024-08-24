package org.phinxt.navigator.controller;

import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.HooverResponse;
import org.phinxt.navigator.service.HooverService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hoover/")
public class HooverController {

    private final HooverService hooverService;

    public HooverController(HooverService hooverService) {
        this.hooverService = hooverService;
    }

    @PostMapping("clean")
    public HooverResponse cleanRoom(@RequestBody HooverRequest request) {
        return hooverService.cleanRoom(request);
    }
}
