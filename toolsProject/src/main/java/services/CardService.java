package services;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ejbs.Card;
import ejbs.CardList;
import ejbs.User;

@Stateless
public class CardService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ListService cardListService;

    @Inject
    private UserService userService;

    public Card createCard(String name, Long listId, Long ownerId) {
        Card card = new Card();
        card.setName(name);

        CardList cardList = cardListService.findCardListById(listId);
        if (cardList == null) {
            return null;
        }

        card.setCardList(cardList);

        User owner = userService.findUserById(ownerId);
        if (owner == null) {
            return null;
        }

        card.setOwner(owner);

        em.persist(card);

        cardList.getCards().add(card);

        em.merge(cardList);

        return card;
    }

    public void moveCard(Long cardId, Long newListId) {
        Card card = em.find(Card.class, cardId);
        if (card == null) {
            return;
        }

        // Find the new card list by its ID
        CardList newList = cardListService.findCardListById(newListId);
        if (newList == null) {
            // Handle case where the new card list is not found
            return;
        }

        // Remove the card from its current card list
        card.getCardList().getCards().remove(card);

        // Update the card list in the database
        em.merge(card.getCardList());

        // Update the card's card list to the new card list
        card.setCardList(newList);

        // Add the card to the new card list's collection of cards
        newList.getCards().add(card);

        // Update the new card list in the database
        em.merge(newList);
    }

    public Set<User> assignCard(Long cardId, Long userId) {

        Card card = em.find(Card.class, cardId);
        if (card == null) {
            return null;
        }

        // Find the user by ID
        User user = userService.findUserById(userId);
        if (user == null) {
            return null;
        }

        card.getAssignedUsers().add(user);
        System.out.println(card.getAssignedUsers() + "Assigned users.");

        em.merge(card);
        
        return card.getAssignedUsers();
    }


    public void addDescription(Long cardId, String description) {
        // Find the card by its ID
        Card card = em.find(Card.class, cardId);
        if (card == null) {
            // Handle case where the card is not found
            return;
        }

        // Set the description for the card
        card.setDescription(description);

        // Update the card in the database
        em.merge(card);
    }

    public void addComment(Long cardId, String comment) {
        // Find the card by its ID
        Card card = em.find(Card.class, cardId);
        if (card == null) {
            // Handle case where the card is not found
            return;
        }

        // Append the comment to the existing comments for the card
        String existingComments = card.getComments();
        if (existingComments != null && !existingComments.isEmpty()) {
            existingComments += "\n"; // Add newline for separation
            existingComments += comment;
        } else {
            existingComments = comment;
        }

        // Set the updated comments for the card
        card.setComments(existingComments);

        // Update the card in the database
        em.merge(card);
    }
    
    
    public List<Card> getCardsByListId(Long listId) {
        TypedQuery<Card> query = em.createQuery(
                "SELECT c FROM Card c WHERE c.cardList.id = :listId", Card.class);
        query.setParameter("listId", listId);
        return query.getResultList();
    }
}
