package dev.sample.tx;

import java.sql.Connection;
/*
 * tx 패키지
 * Connection을 ThreadLocal로 잡고
 * DAO 템플릿이 Connection을 재사용하도록 하는 패키지 
 * */
public final class TxContext {
    private TxContext() {}

    private static final ThreadLocal<Connection> TL = new ThreadLocal<>();

    public static void bind(Connection con) {
        TL.set(con);
    }

    public static Connection currentConnectionOrNull() {
        return TL.get();
    }

    public static void clear() {
        TL.remove();
    }
}