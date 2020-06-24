package projekti.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> 
                   implements Comparable<Skill> {
    
    @ManyToOne
    private Account account;

    private String content;
    private Integer praiseCount;
    
    @OneToMany(mappedBy = "skill")
    private List<Praise> praises;
    
    public void praise() {
        this.praiseCount++;
    }

    public int getPraises() {
        return praises.stream().mapToInt(p -> p.getCount()).sum();
    }

    public int compareTo(Skill skill) {
        return skill.getPraises() - this.getPraises();
    }

}