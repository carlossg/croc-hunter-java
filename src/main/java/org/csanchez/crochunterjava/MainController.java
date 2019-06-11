
package org.csanchez.crochunterjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            region = getRegion();
            System.out.println("Region: " + region);
        } catch (IOException e) {
            System.out.println("Could not get region due to " + e.getClass().getName() + " : " + e.getMessage());
        }
    }

    private String getRegion() throws IOException {
        URL url = new URL(GOOGLE_API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Metadata-Flavor", "Google");
        con.setRequestMethod("GET");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        } finally {
            con.disconnect();
        }
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