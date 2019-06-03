
package org.csanchez.crochunterjava;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class MainController {

    private static final String GOOGLE_API_URL = "http://metadata.google.internal/computeMetadata/v1/instance/attributes/cluster-location";

    private String hostname = "";
    private String region = "";
    private String release = "";
    private String commit = "";
    private String powered = "";

    public MainController() {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            region = getRegion();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getRegion() {
        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();
        // headers.set("Metadata-Flavor", "Google");
        // headers.set("Other-Header", "othervalue");
        // HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        // ResponseEntity<String> response = restTemplate.exchange(GOOGLE_API_URL, HttpMethod.GET, entity,
        // String.class);
        // String region = response.getBody();
        // System.out.println("Region: " + region);
        // return region;
        return "";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/data")
    public Map<String, String> crocHunter() {
        Map<String, String> data = new HashMap<>();
        data.put("hostname", hostname);
        data.put("region", region);
        data.put("release", release);
        data.put("commit", commit);
        data.put("powered", powered);
        return data;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/healthz")
    public String healthz() {
        return "ok";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delay")
    public String delay(@QueryParam("wait") int wait) throws InterruptedException {
        wait = wait == 0 ? 10 : wait;
        Thread.sleep(wait * 1000);
        return String.format("{\"delay\": %d}", wait);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response status(@QueryParam(value = "code") int code) {
        code = code == 0 ? 500 : code;
        return Response.status(code).entity(String.format("{\"code\": %d}", code)).build();
    }
}