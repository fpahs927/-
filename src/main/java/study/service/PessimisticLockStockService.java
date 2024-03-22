package study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.domain.Stock;
import study.repository.StockRepository;

@Service
public class PessimisticLockStockService {
    private final StockRepository stockRepository;
    public PessimisticLockStockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }
    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.save(stock);
    }

}
