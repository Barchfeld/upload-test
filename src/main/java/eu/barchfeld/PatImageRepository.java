package eu.barchfeld;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatImageRepository  extends JpaRepository<PatImage, Long>  {
    @EntityGraph(attributePaths={"patPicture"})
    PatImage findWithPropertyPictureAttachedById(Long id);
}
