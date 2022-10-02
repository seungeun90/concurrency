package com.example.stock.facade;

import com.example.stock.application.StockService;
import com.example.stock.domain.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class NamedLockStockFacade {
    private final LockRepository lockRepository;
    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity){
        try{
            lockRepository.lock(id.toString());
            stockService.decrease(id,quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
