package com.risky.tictactoeapp.repository.jpa;

import com.risky.tictactoeapp.entity.GameDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<GameDataEntity, Long> {
}
