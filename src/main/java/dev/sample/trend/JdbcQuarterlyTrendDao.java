package dev.sample.trend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcQuarterlyTrendDao implements QuarterlyTrendDao {

    private final DataSource ds;

    public JdbcQuarterlyTrendDao(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<QuarterlyTrend> findAllQuarterlyTrends() {
    	String sql =
    	        "SELECT " +
    	        "  BAS_YH AS quarter, " +
    	        "  SUM(FSBZ_AM) AS food, " +
    	        "  SUM(AUTO_AM) AS car, " +
    	        "  SUM(TRVLEC_AM) AS travel_culture, " +
    	        "  SUM(INSUHOS_AM + PLSANIT_AM) AS insurance_health, " +
    	        "  SUM(OFFEDU_AM) AS education_office, " +
    	        "  SUM(CLOTHGDS_AM) AS shopping, " +
    	        "  SUM(DIST_AM) AS living, " +
    	        "  SUM(INTERIOR_AM) AS home " +
    	        "FROM CARD_TRANSACTION " +
    	        "GROUP BY BAS_YH " +
    	        "ORDER BY BAS_YH";

        List<QuarterlyTrend> result = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                QuarterlyTrend row = new QuarterlyTrend(
                    rs.getString("quarter"),
                    rs.getLong("food"),
                    rs.getLong("car"),
                    rs.getLong("travel_culture"),
                    rs.getLong("insurance_health"),
                    rs.getLong("education_office"),
                    rs.getLong("shopping"),
                    rs.getLong("living"),
                    rs.getLong("home")
                );
                result.add(row);
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load quarterly trends", e);
        }
    }
}