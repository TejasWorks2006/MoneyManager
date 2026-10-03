package com.TraksComp.project.repository;

import com.TraksComp.project.config.DatabaseManager;
import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionRepository {
    public TransactionRepository(){
        DatabaseManager.initialiseDatabase();
    }
    public void save(Transactions t) {
        String sql = "INSERT INTO transactions (id, amount, category, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getId().toString());
            pstmt.setDouble(2, t.getAmount());
            pstmt.setString(3, t.getCategory().name());
            pstmt.setString(4, t.getDescription());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    public List<Transactions> findALL() {
        List<Transactions> transactionsList = new ArrayList<>();
        String sql = "SELECT id, amount, category, description FROM transactions";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                double amount = rs.getDouble("amount");
                Category category = Category.valueOf(rs.getString("category"));
                String description = rs.getString("description");

                Transactions transaction = new Transactions(id, amount, category, description);
                transactionsList.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
        }

        return transactionsList;
    }
}