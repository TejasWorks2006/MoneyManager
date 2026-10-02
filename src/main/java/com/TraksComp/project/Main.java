package com.TraksComp.project;

import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;
import com.TraksComp.project.repository.TransactionRepository;
import com.TraksComp.project.service.BudgetService;
import com.TraksComp.project.service.TransactionService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TransactionRepository sharedRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(sharedRepository);
        BudgetService budgetService = new BudgetService(sharedRepository);

        System.out.println("--- Initializing MoneyManager Sandbox ---");

        transactionService.addTransaction(5000.00, Category.SALARY, "October Salary");
        System.out.println("Salary added. Current Balance: $" + transactionService.calculateBalance());

        budgetService.setBudgetLimit(Category.FOOD, 500.00);
        budgetService.setBudgetLimit(Category.RENT, 1500.00);

        transactionService.addTransaction(1500.00, Category.RENT, "Monthly Rent Payment");
        transactionService.addTransaction(120.50, Category.FOOD, "Grocery Store");
        transactionService.addTransaction(400.00, Category.FOOD, "Bulk Meal Prep Supplies");

        System.out.println("\n--- Displaying All Transactions ---");
        // Calling your newly added showTransactions method
        transactionService.showTransactions();

        System.out.println("\n--- Status Report ---");
        System.out.println("Final Balance: $" + transactionService.calculateBalance());
        System.out.println("Is RENT over budget? " + budgetService.isOverBudget(Category.RENT));
        System.out.println("Is FOOD over budget? " + budgetService.isOverBudget(Category.FOOD));

        System.out.println("\n--- Testing Validation Rules (Bad Data) ---");

        // 1. Testing negative amount validation
        try {
            System.out.println("Attempting to add a transaction with a negative amount (-50.00)...");
            transactionService.addTransaction(-50.00, Category.ENTERTAINMENT, "Negative Expense Test");
            System.out.println("WARNING: Negative amount was accepted. Check your amount validation logic.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Validation caught negative amount -> " + e.getMessage());
        }

        // 2. Testing zero amount validation
        try {
            System.out.println("Attempting to add a transaction with a zero amount (0.00)...");
            transactionService.addTransaction(0.00, Category.TRANSPORTATION, "Zero Expense Test");
            System.out.println("WARNING: Zero amount was accepted. Check if this is intended behavior.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Validation caught zero amount -> " + e.getMessage());
        }

        // 3. Testing null category validation
        try {
            System.out.println("Attempting to add a transaction with a null category...");
            transactionService.addTransaction(100.00, null, "Null Category Test");
            System.out.println("WARNING: Null category was accepted. Check your category validation logic.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Validation caught null category -> " + e.getMessage());
        }

        // 4. Testing budget limit with negative value
        try {
            System.out.println("Attempting to set a negative budget limit (-100.00) for UTILITIES...");
            budgetService.setBudgetLimit(Category.UTILITIES, -100.00);
            System.out.println("WARNING: Negative budget limit was accepted.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Validation caught negative budget limit -> " + e.getMessage());
        }

        // 5. Testing unconfigured budget behavior
        System.out.println("Checking budget status for unconfigured category (TRANSPORTATION)...");
        boolean isTransportOver = budgetService.isOverBudget(Category.TRANSPORTATION);
        System.out.println("Is TRANSPORTATION over budget? " + isTransportOver + " (Expected: false, as it shouldn't exist)");
    }
}