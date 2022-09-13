package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.GhostRelation;
import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepo extends JpaRepository<Data, Long> {
    List<Data> findById(int id);
    List<Data> findByStateId(int stateId);
    List<Data> findByFileDescLikeAndNameLike(String fileDesc, String name);
    @Query(value = "select * from data where (file_desc LIKE ?1 or file_desc_first_redaction LIKE ?1)", nativeQuery = true)
    List<Data> search(String file_desc);
}
