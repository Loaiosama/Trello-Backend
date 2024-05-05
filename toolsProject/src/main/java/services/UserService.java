package services;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ejbs.User;

@Stateless
public class UserService {
	
	@PersistenceContext
	private EntityManager em;
	
	public User register(String email, String name, String password, boolean teamLeader) {
		User user = new User();
		
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setTeamLead(teamLeader);
		
		em.persist(user);
		
		return user;
	}
	
	public User login(String email, String password) {
		
		try {
			
            Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            
            return (User) query.getSingleResult();
            
        } catch (NoResultException e) {
        	
        	System.out.println("No user found with the provided credentials.");
            return null;
        }
	}
	
	public User updateProfile(Long userId, String email, String name, String password)
	{
		User user = em.find(User.class, userId);
		
		System.out.println("hena");
		System.out.println(userId + " el user id");
		System.out.println(user);
		
		
		if(user != null)
		{
			user.setEmail(email);
			user.setName(name);
			user.setPassword(password);
			
			em.merge(user);
			
			return user;
			
		}
		else {
			System.out.println("User no found");
			return null;
		}
		
	}
	
	public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }
	

}
