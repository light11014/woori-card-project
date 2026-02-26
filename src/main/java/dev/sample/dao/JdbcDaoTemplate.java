package dev.sample.dao;

// JDBC 공통 템플릿
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import dev.sample.tx.TxContext;

public class JdbcDaoTemplate {

    private final DataSource dataSource;

    public JdbcDaoTemplate(DataSource dataSource) { // 커넥션 관리, SQL실행, 예외처리 클래스
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... params) {
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                bind(ps, params);
                return ps.executeUpdate();
            }
        });
    }

    public <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = queryList(sql, mapper, params); // queryList() 재사용 => JDBC 로직 중복 제거 
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params) {
        return withConnection(con -> {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                bind(ps, params);
                try (ResultSet rs = ps.executeQuery()) {
                    List<T> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapper.map(rs));
                    }
                    return result;
                }
            }
        });
    }

    private void bind(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    /**
     * 트랜잭션이 있으면(현재 쓰레드에 Connection이 있으면) 그걸 재사용.
     * 없으면 DataSource에서 새 Connection을 열고 자동 close.
     */
    private <T> T withConnection(SqlFunction<Connection, T> block) {
        Connection txCon = TxContext.currentConnectionOrNull();
        if (txCon != null) {
            try {
                return block.apply(txCon);
            } catch (Exception e) {
                throw wrap(e);
            }
        }

        try (Connection con = dataSource.getConnection()) {
            return block.apply(con);
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    private RuntimeException wrap(Exception e) {
        return (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
    }

    @FunctionalInterface
    private interface SqlFunction<A, B> {
        B apply(A a) throws Exception;
    }
}