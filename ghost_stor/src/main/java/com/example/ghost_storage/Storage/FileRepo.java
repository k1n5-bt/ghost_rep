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
    List<Data> findByStateIdAndFileDescLike(int stateId, String fileDesc);
    @Query(value = "select * from data where (file_desc LIKE ?1 or file_desc_first_redaction LIKE ?1)" +
            " and (name LIKE ?2 or name_first_redaction LIKE ?2)" +
            "and (okccode LIKE ?3 or okccode_first_redaction LIKE ?3)" +
            "and (okpdcode LIKE ?4 or okpdcode_first_redaction LIKE ?4)" +
            "and (adoption_date LIKE ?5 or adoption_date_first_redaction LIKE ?5)" +
            "and (introduction_date LIKE ?6 or introduction_date_first_redaction LIKE ?6)" +
            "and (developer LIKE ?7 or developer_first_redaction LIKE ?7)" +
            "and (predecessor LIKE ?8 or predecessor_first_redaction LIKE ?8)" +
            "and (head_content LIKE ?9 or head_content_first_redaction LIKE ?9)" +
            "and (keywords LIKE ?10 or keywords_first_redaction LIKE ?10)" +
            "and (key_phrases LIKE ?11 or key_phrases_first_redaction LIKE ?11)" +
            "and (level_of_acceptance LIKE ?12 or level_of_acceptance_first_redaction LIKE ?12)" +
            "and (contents LIKE ?13 or contents_first_redaction LIKE ?13)" +
            "and (modifications LIKE ?14 or modifications_first_redaction LIKE ?14)" +
            "and (status LIKE ?15 or status_first_redaction LIKE ?15)" +
            "and (state_id = 100)",
            nativeQuery = true)
    List<Data> search(
            String fileDesc,
            String name,
            String OKCcode,
            String OKPDcode,
            String adoptionDate,
            String introductionDate,
            String developer,
            String predecessor,
            String headContent,
            String keywords,
            String keyPhrases,
            String levelOfAcceptance,
            String contents,
            String modifications,
            String status
    );
}
