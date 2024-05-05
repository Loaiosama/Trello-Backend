package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ejbs.Board;
import ejbs.CardList;
import ejbs.User;

@Stateless
public class ListService {
		
	@PersistenceContext
	private EntityManager em;
	
	public CardList createList(Board board, User teamLeader, String name) {
		
		CardList cardList = new CardList();
		cardList.setBoard(board);
		cardList.setName(name);
		board.getLists().add(cardList);
		em.persist(cardList);
		return cardList;
		
	}
	
	public void deleteCardList(CardList cardList) {
	
        if (cardList != null) {
            em.remove(cardList);
        }
    }
	
	public CardList findCardListById(Long listId) {
        return em.find(CardList.class, listId);
    }

}
