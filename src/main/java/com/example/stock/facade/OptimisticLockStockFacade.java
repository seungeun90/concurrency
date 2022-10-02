package com.example.stock.facade;

import com.example.stock.application.OptimisticLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptimisticLockStockFacade {

    private final OptimisticLockService optimisticLockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(true){
            try{
                optimisticLockService.decrease(id,quantity);
                break;
            } catch (Exception e){
                Thread.sleep(50);
            }
        }


    }

}
