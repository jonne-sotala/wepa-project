package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Account;
import projekti.model.Praise;
import projekti.model.Skill;

public interface PraiseRepository extends JpaRepository<Praise, Long> {
    
    Praise findBySkillAndAccount(Skill skill, Account account);
}