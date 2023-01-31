package be.vdab.firma.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seven")
class SevenController {
    @GetMapping
    public String seven() {
        return "seven";
    }
}
