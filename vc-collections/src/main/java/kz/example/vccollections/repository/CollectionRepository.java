package kz.example.vccollections.repository;

import kz.example.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select c from Collection c where c.id = ?1")
    Collection getCollectionById(Long id);

    @Query("select c from Collection c where c.isPublic = true")
    Page<Collection> findPublicCollections(Pageable pageable);

    Page<Collection> findByUserId(Long userId, Pageable pageable);
}