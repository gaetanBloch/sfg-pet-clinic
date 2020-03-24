package guru.springframework.sfgpetclinic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
final class IndexController {

    @GetMapping({"", "/", "index", "index.html"})
    public String index() {
        return "index";
    }

    @GetMapping("/oups")
    public String oupsHandler() {
        return "notimplemented";
    }
}
