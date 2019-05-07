
package org.csanchez.crochunterjava;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MainController {

    @GetMapping("/")
    public String crocHunter(Model model) throws UnknownHostException {
        model.addAttribute("hostname", InetAddress.getLocalHost().getHostName());
        model.addAttribute("region", getRegion());
        model.addAttribute("release", System.getenv("WORKFLOW_RELEASE"));
        model.addAttribute("commit", System.getenv("GIT_SHA"));
        model.addAttribute("powered", System.getenv("POWERED_BY"));
        return "index";
    }

    private String getRegion() {
        // TODO
        return "TODO";
    }

    @GetMapping("/healthz")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String healthz() {
        return "ok";
    }

    @GetMapping(path = "/delay", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String delay(@RequestParam(value = "wait", defaultValue = "10") int wait) throws InterruptedException {
        Thread.sleep(wait*1000);
        return String.format("{\"delay\": %d}", wait);
    }

    @GetMapping(path = "/status", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> status(@RequestParam(value = "code", defaultValue = "500") int code) {
        return new ResponseEntity<String>(String.format("{\"code\": %d}", code), HttpStatus.valueOf(code));
    }
}