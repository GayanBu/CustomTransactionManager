package com.gayansoft.customtransactionmanager.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.*;

public class CustomTransactionManager {
    private DataSourceTransactionManager transactionManager;
    private TransactionStatus transactionStatus;
    private Connection connection;

    public CustomTransactionManager(DataSource dataSource) {
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.transactionStatus = null;
        this.connection = null;
    }

    public void beginTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        this.transactionStatus = transactionManager.getTransaction(def);
        try {
            this.connection = transactionManager.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        transactionManager.commit(transactionStatus);
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        transactionManager.rollback(transactionStatus);
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePoint(String savePointName) {
        Savepoint savePoint = null;
        try {
            savePoint = connection.setSavepoint(savePointName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdate(String query) {
        int result = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
