package rest;

import battleship.helper.Helper;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/newstart")
@Produces(MediaType.APPLICATION_JSON)

public class NewStartResource {

    public NewStartResource() {
    }

    @POST
    public void newstart() {
        Helper.getActiveGame().resetBoard();
        Helper.getActiveGame().setUpGame();
    }
}
