package com.example.stock.application;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticLockService {

    private final StockRepository stockRepository;

    @Transactional
    public Long decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
        return stock.getQuantity();
    }
}
