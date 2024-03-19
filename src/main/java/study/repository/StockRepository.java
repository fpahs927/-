package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
