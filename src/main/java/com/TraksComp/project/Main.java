package com.TraksComp.project;

import com.TraksComp.project.config.DatabaseManager;
import com.TraksComp.project.model.Category;
import com.TraksComp.project.repository.BudgetRepository;
import com.TraksComp.project.repository.TransactionRepository;
import com.TraksComp.project.service.BudgetService;
import com.TraksComp.project.service.TransactionService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        // 1. Initialize Repositories (Creates tables if they don't exist)
        TransactionRepository transactionRepository = new TransactionRepository();
        BudgetRepository budgetRepository = new BudgetRepository();

        // 2. Initialize Services
        TransactionService transactionService = new TransactionService(transactionRepository);
        BudgetService budgetService = new BudgetService(transactionRepository, budgetRepository);

        System.out.println("--- Starting Automated Backend Test ---\n");

        // 3. Execute Core Logic
        System.out.println("[+] Adding Transactions...");
        transactionService.addTransaction(5000.00, Category.SALARY, "Base Salary");
        transactionService.addTransaction(150.00, Category.FOOD, "Groceries");
        transactionService.addTransaction(200.00, Category.FOOD, "Dining Out");
        transactionService.addTransaction(1200.00, Category.RENT, "Monthly Rent");

        System.out.println("[+] Setting Budgets...");
        budgetService.setBudgetLimit(Category.FOOD, 300.00); // Spent 350, should be over
        budgetService.setBudgetLimit(Category.RENT, 1500.00); // Spent 1200, should be under

        // 4. Verify Calculations & Logic
        System.out.println("\n--- Verification Results ---");
        System.out.println("Calculated Balance: $" + transactionService.calculateBalance() + " (Expected: 3650.0)");
        System.out.println("Is FOOD over budget? " + budgetService.isOverBudget(Category.FOOD) + " (Expected: true)");
        System.out.println("Is RENT over budget? " + budgetService.isOverBudget(Category.RENT) + " (Expected: false)");
        System.out.println("Is UTILITIES over budget? " + budgetService.isOverBudget(Category.UTILITIES) + " (Expected: false)");

        // 5. Display Saved Data
        System.out.println("\n--- Database Records Before Cleanup ---");
        transactionService.showTransactions();

        // 6. Tear Down
        System.out.println("\n--- Initiating Cleanup ---");
        cleanUpDatabase();
    }

    /**
     * Wipes the SQLite tables cleanly before shutting down the app,
     * leaving the schema intact for the next test run.
     */
    private static void cleanUpDatabase() {
        String wipeTransactions = "DELETE FROM transactions;";
        String wipeBudgets = "DELETE FROM budgets;";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(wipeTransactions);
            stmt.execute(wipeBudgets);
            System.out.println("[System] Successfully wiped 'transactions' and 'budgets' tables.");

        } catch (SQLException e) {
            System.err.println("[Error] Database cleanup failed: " + e.getMessage());
        }
    }
}