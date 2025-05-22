package kz.example.vccollections.repository;

import kz.example.entity.CollectionCollaborator;
import kz.example.entity.CollectionCollaboratorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionCollaboratorRepository extends JpaRepository<CollectionCollaborator, CollectionCollaboratorId> {

    List<CollectionCollaborator> findCollectionCollaboratorByCollectionId(Integer collectionId);

    List<CollectionCollaborator> findCollectionCollaboratorsByCollectionId(Long collectionId);
}
