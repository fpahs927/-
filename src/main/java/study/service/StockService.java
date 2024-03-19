package study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.domain.Stock;
import study.repository.StockRepository;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity){
        // Stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();

        // 재고를 감소시킨 뒤
        stock.decrease(quantity);

        // 갱신된 값을 저장하도록 한다
        stockRepository.saveAndFlush(stock);
    }
}
