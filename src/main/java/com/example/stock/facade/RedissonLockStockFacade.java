package com.example.stock.facade;

import com.example.stock.application.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decrease(Long key, Long quantity){
        RLock lock = redissonClient.getLock(key.toString());
        log.info("lock redisson {}" , lock);
        //몇 초간 락 획득 시도, 락을 점유할 것인지 설정
        try{
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!available){
                log.info("락 획득 실패");
                return ;
            }
            stockService.decrease(key,quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
