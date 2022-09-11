package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.GhostRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RelationRepo extends JpaRepository<GhostRelation, Long> {
    List<GhostRelation> findById(int id);
    List<GhostRelation> findByDataId(int dataId);
    List<GhostRelation> findByReferralDataId(int referralDataId);
    List<GhostRelation> findByDataIdAndReferralDataId(int dataId, int referralDataId);

}