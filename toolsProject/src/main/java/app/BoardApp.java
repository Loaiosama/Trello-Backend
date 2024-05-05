package app;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Board;
import ejbs.CardList;
import ejbs.User;
import services.BoardService;
import services.UserService;

import java.util.List;

@Stateless
@Path("/boards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardApp {

    @Inject
    private BoardService boardService;

    @Inject
    private UserService userService;

    @POST
    @Path("/createBoard/{teamLeaderId}")
    public Response createBoard(String name, @PathParam("teamLeaderId") Long teamLeaderId) {
    	
        User teamLeader = userService.findUserById(teamLeaderId);
        System.out.println("test 2");
        System.out.println(teamLeader + "inside path");
        Board newBoard = boardService.createBoard(name, teamLeader);
        return Response.ok(newBoard).build();
    }

    @GET
    @Path("/getBoards/{userId}")
    public Response getBoardsByUserId(@PathParam("userId") Long userId) {
        List<Board> boards = boardService.getBoardsByUserId(userId);
        return Response.ok(boards).build();
    }
    
    @GET
    @Path("/getLists/{boardId}")
    public Response getListsByBoardId(@PathParam("boardId") Long boardId) {
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
        }

        List<CardList> lists = board.getLists();
        return Response.ok(lists).build();
    }

    @POST
    @Path("/invite/{boardId}/{userId}/{leaderId}")
    public Response inviteUserToBoard(@PathParam("boardId") Long boardId, @PathParam("userId") Long userId, @PathParam("leaderId") Long leaderId)
    {
        boardService.inviteUser(boardId, userId, leaderId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deleteBoard/{boardId}")
    public Response deleteBoard(@PathParam("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return Response.ok().build();
    }
}
