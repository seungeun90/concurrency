package com.example.stock.application;

import com.example.stock.domain.Stock;
import com.example.stock.domain.StockRepository;
import com.example.stock.facade.OptimisticLockStockFacade;
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
class StockServiceTest {

    @Autowired
    private  StockService stockService;


    @Autowired
    private  PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private OptimisticLockStockFacade OptimisticLockStockService;
    @Autowired
    private StockServiceCaller stockServiceCaller;

    @Autowired
    private StockRepository stockRepository;

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
    public void decreaseQuantity(){
        stockService.decrease(1L,5L);
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(stock.getQuantity(),95L);
    }

    @Test
    public void multiThreadTestWithSeparateCaller() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개의 요청이 끝날때까지 기다려야해
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0;i<threadCount;i++) {
            executorService.submit(()->{
                try{
                    stockServiceCaller.decrease(1L,1L);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); //다른 스레드 수행 완료까지 기다려줌

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0L,stock.getQuantity());
    }

    @Test
    public void multiThreadTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개의 요청이 끝날때까지 기다려야해
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0;i<threadCount;i++) {
            executorService.submit(()->{
                try{
                    stockService.decrease(1L,1L);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); //다른 스레드 수행 완료까지 기다려줌

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0L,stock.getQuantity());
    }

    @Test
    public void multiThreadTestWithPessimisticLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개의 요청이 끝날때까지 기다려야해
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0;i<threadCount;i++) {
            executorService.submit(()->{
                try{
                    pessimisticLockStockService.decrease(1L,1L);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(); //다른 스레드 수행 완료까지 기다려줌

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0L,stock.getQuantity());
    }

    @Test
    public void multiThreadTestWithOptimisticLock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개의 요청이 끝날때까지 기다려야해
        CountDownLatch countDownLatch = new CountDownLatch(100);

        for(int i=0;i<threadCount;i++) {
            executorService.submit(()->{
                try{
                    OptimisticLockStockService.decrease(1L,1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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