package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<Data, Long> {
    List<Data> findByNameLike(String name);
    List<Data> findByFileDescLike(String fileDesc);
    List<Data> findByAuthorAndNameLike(User user, String name);
    List<Data> findByAuthorAndFileDescLike(User user, String fileDesc);
    List<Data> findByAuthor(User user);
    List<Data> findById(int id);
    List<Data> findByStateId(int stateId);

    List<Data> findByFileDescLikeAndNameLike(String fileDesc, String name);
}
