
package org.csanchez.crochunterjava;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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

    private String hostname;
    private String region;
    private String release;
    private String commit;
    private String powered;

    public MainController() {
        Class<MainController> clazz = MainController.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            System.out.println("Class is not in jar");
            return;
        }
        String manifestPath = classPath.substring(0, classPath.indexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        System.out.println("Parsing manifest in " + manifestPath);
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            hostname = InetAddress.getLocalHost().getHostName();
            region = getRegion();
            release = attr.getValue("Implementation-Version");
            commit = attr.getValue("Implementation-SCM-Revision");
            powered = attr.getValue("Implementation-Powered-By");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public String crocHunter(Model model) throws UnknownHostException {
        model.addAttribute("hostname", hostname);
        model.addAttribute("region", region);
        model.addAttribute("release", release);
        model.addAttribute("commit", commit);
        model.addAttribute("powered", powered);
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
        Thread.sleep(wait * 1000);
        return String.format("{\"delay\": %d}", wait);
    }

    @GetMapping(path = "/status", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> status(@RequestParam(value = "code", defaultValue = "500") int code) {
        return new ResponseEntity<String>(String.format("{\"code\": %d}", code), HttpStatus.valueOf(code));
    }
}