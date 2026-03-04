package dev.trend;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface QuarterlyTrendDao {
    List<QuarterlyTrend> findAllQuarterlyTrends();
    List<QuarterlyTrend> findRecentTwoQuarters();  
}