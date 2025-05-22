package kz.example.vccollections.repository;

import kz.example.entity.CollectionLike;
import kz.example.entity.CollectionLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionLikeRepository extends JpaRepository<CollectionLike, CollectionLikeId> {

    Integer countCollectionLikeByCollectionId(Long collectionId);
}
