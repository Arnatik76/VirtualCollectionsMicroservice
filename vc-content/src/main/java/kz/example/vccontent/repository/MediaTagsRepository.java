package kz.example.vccontent.repository;

import kz.example.entity.MediaTag;
import kz.example.entity.MediaTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaTagsRepository extends JpaRepository<MediaTag, MediaTagId> {

    @Query("SELECT mt FROM MediaTag mt JOIN FETCH mt.tag WHERE mt.media.id = :mediaId")
    List<MediaTag> findByMedia_Id(@Param("mediaId") Integer mediaId);
}
