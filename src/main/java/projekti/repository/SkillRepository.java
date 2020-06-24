package projekti.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.Account;
import projekti.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    List<Skill> findByAccount(Account account);

    Page<Skill> findByAccount(Account account, Pageable pageable);
}
