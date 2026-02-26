package dev.sample.tx;

import java.sql.Connection;

import javax.sql.DataSource;

public class TransactionTemplate {

    private final DataSource writeDataSource;

    public TransactionTemplate(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public <T> T execute(TxCallback<T> callback) {
        try (Connection con = writeDataSource.getConnection()) {
            con.setAutoCommit(false);
            TxContext.bind(con);

            try {
                T result = callback.doInTx();
                con.commit();
                return result;
            } catch (Exception e) {
                try { con.rollback(); } catch (Exception ignore) {}
                throw wrap(e);
            } finally {
                TxContext.clear();
                try { con.setAutoCommit(true); } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    private RuntimeException wrap(Exception e) {
        return (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
    }

    @FunctionalInterface
    public interface TxCallback<T> {
        T doInTx() throws Exception;
    }
}