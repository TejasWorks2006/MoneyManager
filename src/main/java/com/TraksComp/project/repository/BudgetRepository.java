package com.TraksComp.project.repository;

import com.TraksComp.project.config.DatabaseManager;
import com.TraksComp.project.model.Budget;
import com.TraksComp.project.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetRepository {
    public void save(Budget budget){
        String sql = "INSERT OR REPLACE INTO budgets (category, max_spending) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, budget.getCategory().name());
            pstmt.setDouble(2, budget.getMax_spending());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving budget: " + e.getMessage());
        }
    }
    public Budget findByCategory(Category category) {
        String sql = "SELECT max_spending FROM budgets WHERE category = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double limit = rs.getDouble("max_spending");
                    return new Budget(category, limit);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving budget: " + e.getMessage());
        }

        return null;
    }
}
