package dev.sample.dao;

// 공통 RowMapper
import java.sql.ResultSet;
import java.sql.SQLException;

// 함수형 인터페이스
@FunctionalInterface
public interface RowMapper<T> { // 결과가 여러 개일 경우 RowMapper 리스트<T>로 반환
    T map(ResultSet rs) throws SQLException;
}