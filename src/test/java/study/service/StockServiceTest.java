package study.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.domain.Stock;
import study.repository.StockRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockRepository stockRepository;

//    @Autowired
//    private StockService stockService;  //일반케이스

    @Autowired
    private PessimisticLockStockService stockService;


    @BeforeEach
    public void before(){
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }
    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }
    @Test
    public void 재고감소(){  //만약 여러곳에서 재고감소를 한다면?
        stockService.decrease(1L, 1L);
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount =100;
        //멀티쓰레드를 이용해야하기 때문에 Executeservice를 이용한다
        //비동기로 실행하는 작업을 단순하게 사용할 수 있게하는 자바 api
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //100개 요청 기다려야하기 때문에 countDownMatching을 한다
        //다른 스레드의 수행중인 작업이 완료될때까지 대기하게 하는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try{
                    stockService.decrease(1L, 1L);
                }finally {
                    latch.countDown();
                }
            });

        }
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }

}
