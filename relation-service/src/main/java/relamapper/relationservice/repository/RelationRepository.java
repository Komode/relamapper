package relamapper.relationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import relamapper.relationservice.model.Relation;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Integer> {

    @Query("SELECT rel FROM Relation rel WHERE (uuid1 = :uuid) OR (uuid2 = :uuid)")
    List<Relation> getUUIDRelations(@Param("uuid") String uuid);

    @Query("SELECT rel FROM Relation rel WHERE (uuid1 = :uuid1 AND uuid2 = :uuid2) OR (uuid1 = :uuid2 AND uuid2 = :uuid1)")
    Relation getRelationBetween(@Param("uuid1") String uuid1, @Param("uuid2") String uuid2);
}
