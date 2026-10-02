package com.TraksComp.project.service;

import com.TraksComp.project.model.Category;
import com.TraksComp.project.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BudgetServiceTest {

    @Test
    void testIsOverBudgetReturnsTrueWhenLimitExceeded(){
        // 1. Setup
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);
        BudgetService budgetService = new BudgetService(transactionRepository);

        // 2. Entering data
        budgetService.setBudgetLimit(Category.FOOD, 100.0);
        transactionService.addTransaction(150, Category.FOOD, "Expensive Groceries");

        // 3. Verifying
        boolean isOver = budgetService.isOverBudget(Category.FOOD);
        assertTrue(isOver, "FOOD Budget should be exceeded.");
    }

    @Test
    void testNegativeBudgetLimitThrowsException(){
        // 1. Setup
        TransactionRepository transactionRepository = new TransactionRepository();
        BudgetService budgetService = new BudgetService(transactionRepository);

        // 2. Verifying
        assertThrows(IllegalArgumentException.class, ()-> {
            budgetService.setBudgetLimit(Category.RENT, -500.0);
        });
    }

    @Test
    void testIsOverBudgetReturnsFalseWhenWithinLimit(){
        // 1. Setup
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);
        BudgetService budgetService = new BudgetService(transactionRepository);

        // 2. Entering data
        budgetService.setBudgetLimit(Category.FOOD, 500.0);
        transactionService.addTransaction(150, Category.FOOD, "Expensive Groceries");

        // 3. Verifying
        boolean isOver = budgetService.isOverBudget(Category.FOOD);
        assertFalse(isOver, "FOOD expense should be within budget.");
    }

    @Test
    void testIsOverBudgetReturnsFalseWhenZero(){
        // 1. Setup
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);
        BudgetService budgetService = new BudgetService(transactionRepository);

        // 2. Entering data
        budgetService.setBudgetLimit(Category.FOOD, 500.0);
        transactionService.addTransaction(500, Category.FOOD, "Expensive Groceries");

        // 3. Verifying
        boolean isOver = budgetService.isOverBudget(Category.FOOD);
        assertFalse(isOver, "FOOD expense is equal to budget.");
    }
}
