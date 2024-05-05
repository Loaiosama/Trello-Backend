package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ForbiddenException;

import ejbs.Board;
import ejbs.User;

import java.util.List;

@Stateless
public class BoardService {

    @PersistenceContext
    private EntityManager em;

    public Board createBoard(String name, User teamLeader) {
    	
    	System.out.println("test 2");
    	System.out.println(teamLeader + "inside service");
    	System.out.println(teamLeader.getRole());
        if (!teamLeader.getRole()) {
            throw new ForbiddenException("Only team leaders can create boards.");
        }
        Board board = new Board();
        board.setName(name);
        board.setTeamLeader(teamLeader);
        em.persist(board);
        return board;
    }

    public List<Board> getBoardsByUserId(Long userId) {
        List<Board> boards = em.createQuery("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.collaborators WHERE b.teamLeader.id = :userId", Board.class)
                               .setParameter("userId", userId)
                               .getResultList();
        // Ensure that the lists collection is eagerly initialized
        boards.forEach(board -> board.getLists().size());
        
        return boards;
    }



    public void inviteUser(Long boardId, Long userId, Long leaderId) {
        Board board = em.find(Board.class, boardId);
        User user = em.find(User.class, userId);
        User leader = em.find(User.class, leaderId);
        
        
        if (board == null || user == null || leader == null) {
            throw new IllegalArgumentException("Board, user, or invoker not found.");
        }
        
        if (!board.getTeamLeader().getId().equals(leader.getId())) {
            throw new ForbiddenException("Only team leaders can add users to boards.");
        }
        
        board.getCollaborators().add(user);
    }


    public void deleteBoard(Long boardId) {
        Board board = em.find(Board.class, boardId);
        if (!board.getTeamLeader().getRole()) {
            throw new ForbiddenException("Only team leaders can delete boards.");
        }
        em.remove(board);
    }
    
    public Board findBoardById(Long boardId) {
		return em.find(Board.class, boardId);
	}
}
