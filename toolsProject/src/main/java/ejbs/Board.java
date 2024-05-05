package ejbs;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.util.List;
import java.util.Set;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private User teamLeader;

    @JsonIgnore
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CardList> lists;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> collaborators;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<CardList> getLists() {
        return lists;
    }

    public void setLists(List<CardList> lists) {
        this.lists = lists;
    }
    
    public Set<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<User> collaborators) {
        this.collaborators = collaborators;
    }
}
