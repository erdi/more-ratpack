package lggug

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
class BootApp {

    @RequestMapping("/")
    String root() {
        "Hello World!"
    }

    static void main(String[] args) {
        SpringApplication.run(BootApp, args)
    }
}
