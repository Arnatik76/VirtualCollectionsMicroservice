package kz.example.vccollections.repository;

import kz.example.entity.CollectionItem;
import kz.example.entity.CollectionItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionItemRepository extends JpaRepository<CollectionItem, CollectionItemId> {

    Page<CollectionItem> findById_CollectionId(Long collectionId, Pageable pageable);

    Integer countById_CollectionIdAndAddedByUserIdIsNotNull(Long collectionId);
}