package app;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import ejbs.Board;
import ejbs.CardList;
import ejbs.User;
import services.ListService;
import services.BoardService;
import services.UserService;

@Path("/lists")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardListApp {

    @Inject
    private ListService listService;

    @Inject
    private BoardService boardService;

    @Inject
    private UserService userService;

    @POST
    @Path("/create/{boardId}/{leaderId}")
    public Response createCardList(@PathParam("boardId") Long boardId, @PathParam("leaderId") Long leaderId, String name) {
    	
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
        }

        User user = userService.findUserById(leaderId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        if (!user.getRole()) {
            throw new ForbiddenException("Only team leaders can create lists.");
        }

        CardList newCardList = listService.createList(board, user, name);

        return Response.ok(newCardList).build();
    }

    @DELETE
    @Path("/delete/{listId}/{leaderId}")
    public Response deleteCardList(@PathParam("listId") Long listId, @PathParam("leaderId") Long leaderId) {
    	
    	User user = userService.findUserById(leaderId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        
        if (!user.getRole()) {
            throw new ForbiddenException("Only team leaders can create lists.");
        }

    	
        CardList cardList = listService.findCardListById(listId);
        if (cardList == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card list not found").build();
        }

        listService.deleteCardList(cardList);

        return Response.ok("Card list deleted successfully").build();
    }
}
