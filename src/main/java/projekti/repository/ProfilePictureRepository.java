package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projekti.model.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {

    ProfilePicture findByName(String name);
    
}

