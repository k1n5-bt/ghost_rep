package com.example.ghost_storage.Storage;

import com.example.ghost_storage.Model.ActionStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionStatRepo extends JpaRepository<ActionStat, Long> {
}
