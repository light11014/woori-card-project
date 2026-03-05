package dev.trend;

import org.springframework.stereotype.Service; // 추가
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service // 비즈니스 로직 빈으로 등록
public class QuarterlyTrendService {

    private final QuarterlyTrendDao dao;

    // DAO 인터페이스를 주입받음
    public QuarterlyTrendService(QuarterlyTrendDao dao) {
        this.dao = dao;
    }

    public List<QuarterlyTrend> getTrends(int range) {
        List<QuarterlyTrend> all = dao.findAllQuarterlyTrends();
        return sliceLast(all, range);
    }

    // 기존 서블릿에서 서비스 계층으로 이동된 비즈니스 로직
    private List<QuarterlyTrend> sliceLast(List<QuarterlyTrend> all, int n) {
        if (all == null || all.isEmpty()) return Collections.emptyList();
        if (all.size() <= n) return all;
        return new ArrayList<>(all.subList(all.size() - n, all.size()));
    }
}