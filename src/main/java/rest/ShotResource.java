package rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/shot")
@Produces(MediaType.APPLICATION_JSON)
public class ShotResource {

    public ShotResource() {
    }

    @POST
    public ShotModel shot(@QueryParam("letter") String letter, @QueryParam("number") Integer number) {
        // TODO handle shot here
        // TODO return result of shot, plus opponent information
        // TODO handle exceptions like already used locations etc...
        return new ShotModel(number, letter);
    }

}
