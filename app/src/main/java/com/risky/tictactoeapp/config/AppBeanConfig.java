package com.risky.tictactoeapp.config;

import com.risky.tictactoeapp.repository.core.CoreGameRepository;
import com.risky.tictactoecore.service.GameService;
import com.risky.tictactoecore.service.GameServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeanConfig {

    @Bean
    GameService gameService(CoreGameRepository coreGameRepository) {
        return new GameServiceImpl(coreGameRepository);
    }

}
