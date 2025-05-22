package kz.example.vccontent.repository;

import kz.example.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {

    ContentType getContentTypeById(Integer id);
}
