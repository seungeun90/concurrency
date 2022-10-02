package com.example.stock.facade;

import com.example.stock.application.StockService;
import com.example.stock.application.StockServiceCaller;
import com.example.stock.domain.LockRepository;
import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class NamedLockStockFacadeTest {
    @Autowired
    private NamedLockStockFacade namedLockStockFacade;

    @Autowired
    private LockRepository stockRepository;

    @BeforeEach
    public void before(){
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }
    @AfterEach
    public void delete(){
        stockRepository.deleteAll();
    }

    @Test
    public void multiThreadTestWithNamedLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개의 요청이 끝날때까지 기다려야해
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0;i<threadCount;i++) {
            executorService.submit(()->{
                try{
                    namedLockStockFacade.decrease(1L,1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); //다른 스레드 수행 완료까지 기다려줌

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0L,stock.getQuantity());
    }

}