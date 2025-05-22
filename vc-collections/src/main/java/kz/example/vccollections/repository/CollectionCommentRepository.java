package kz.example.vccollections.repository;

import kz.example.entity.CollectionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionCommentRepository extends JpaRepository<CollectionComment, Integer> {

    Integer countCollectionCommentByCollectionId(Long collectionId);

    @Query("SELECT cc FROM CollectionComment cc WHERE cc.collection.id = :collectionId")
    Page<CollectionComment> findByCollection_Id(@Param("collectionId") Long collectionId, Pageable pageable);
}