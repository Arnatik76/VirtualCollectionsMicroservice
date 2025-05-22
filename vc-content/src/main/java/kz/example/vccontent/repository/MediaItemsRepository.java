package kz.example.vccontent.repository;

import kz.example.entity.MediaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaItemsRepository extends JpaRepository<MediaItem, Long>, JpaSpecificationExecutor<MediaItem> {

    Page<MediaItem> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<MediaItem> findByTitleContainingIgnoreCaseAndType_TypeNameIgnoreCase(String title, String typeName, Pageable pageable);

}