package com.example.stock.application;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockServiceCaller {
    private final StockService stockService;

    public synchronized void decrease(Long id, Long quantity){
        stockService.decrease(id,quantity);
    }
}
