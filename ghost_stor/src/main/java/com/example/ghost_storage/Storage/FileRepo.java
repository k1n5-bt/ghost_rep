package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepo extends JpaRepository<Data, Long> {
    List<Data> findByNameLike(String name);
    List<Data> findById(int id);
//    List<Data> findByArchived(boolean archived);
    List<Data> findByStateId(int stateId);
    List<Data> findByFileDescLikeAndNameLike(String fileDesc, String name);
}
