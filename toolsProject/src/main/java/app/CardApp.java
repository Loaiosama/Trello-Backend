package app;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Card;
import ejbs.User;
import services.CardService;

@Stateless
@Path("/cards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardApp {

    @Inject
    private CardService cardService;

    @POST
    @Path("/createCard/{listId}/{ownerId}")
    public Response createCard(String name, @PathParam("listId") Long listId, @PathParam("ownerId") Long ownerId) {
        Card newCard = cardService.createCard(name, listId, ownerId);
        if (newCard == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Could not create card. Please check list and owner IDs.").build();
        }
        return Response.ok(newCard).build();
    }

    @PUT
    @Path("/moveCard/{cardId}/{newListId}")
    public Response moveCard(@PathParam("cardId") Long cardId, @PathParam("newListId") Long newListId) {
        cardService.moveCard(cardId, newListId);
        return Response.ok().build();
    }

    @PUT
    @Path("/assignCard/{cardId}/{ownerId}")
    public Response assignCard(@PathParam("cardId") Long cardId, @PathParam("ownerId") Long ownerId) {
        Set<User> assignedUsers = cardService.assignCard(cardId, ownerId);
        if (assignedUsers == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card or owner not found").build();
        }
        return Response.ok(assignedUsers).build();
    }

    @PUT
    @Path("/addDescription/{cardId}")
    public Response addDescription(@PathParam("cardId") Long cardId, String description) {
        cardService.addDescription(cardId, description);
        return Response.ok().build();
    }

    @PUT
    @Path("/addComment/{cardId}")
    public Response addComment(@PathParam("cardId") Long cardId, String comment) {
        cardService.addComment(cardId, comment);
        return Response.ok().build();
    }
    
    @GET
    @Path("/getCardsInList/{listId}")
    public Response getCardsByListId(@PathParam("listId") Long listId) {
    	System.out.println(listId + "inside path cardApp");
        List<Card> cards = cardService.getCardsByListId(listId);
        if (cards == null || cards.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No cards found for list ID: " + listId).build();
        }
        return Response.ok(cards).build();
    }
}
