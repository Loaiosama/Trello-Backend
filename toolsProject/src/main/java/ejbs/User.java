package ejbs;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String email;
	private String password;
	private boolean teamLeader;
	
	@OneToMany(mappedBy = "teamLeader")
    private List<Board> leadingBoards;
	
	@ManyToMany(mappedBy = "collaborators", fetch = FetchType.EAGER)
    private Set<Board> boards;
	
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Card> ownedCards;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "assignedUsers", fetch = FetchType.EAGER)
    private Set<Card> assignedCards;
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getRole() {
		return teamLeader;
	}
	
	public void setName(String name) {
        this.name = name;
    }
	
	public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setTeamLead(boolean teamLead) {
        this.teamLeader = teamLead;
    }
    
    public Set<Card> getOwnedCards() {
        return ownedCards;
    }

    public void setOwnedCards(Set<Card> ownedCards) {
        this.ownedCards = ownedCards;
    }
    
    public Set<Card> getAssignedCards() {
        return assignedCards;
    }
    
    public void setAssignedCards(Set<Card> ownedCards) {
        this.assignedCards = ownedCards;
    }
}
