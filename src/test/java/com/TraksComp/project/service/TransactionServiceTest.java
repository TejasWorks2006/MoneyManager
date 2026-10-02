package com.TraksComp.project.service;

import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;
import com.TraksComp.project.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionServiceTest {

    @Test
    void testAddTransactionSuccessfullySavesValidData(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);

        transactionService.addTransaction(500.0, Category.FOOD, "Expensive Food");

        List<Transactions> savedTransactions = transactionRepository.findALL();
        assertEquals(1, savedTransactions.size(), "Repository should contain exactly one entry.");

        Transactions saved = savedTransactions.getFirst();
        assertEquals(500, saved.getAmount(), "Amount saved should match input.");
        assertEquals(Category.FOOD, saved.getCategory(), "Category saved should match input.");
        assertEquals("Expensive Food", saved.getDescription(), "Description saved should match input.");
        assertNotNull(saved.getId(), "A uuid should have been generated and assigned.");
    }

    @Test
    void testAddTransactionSuccessfullyThrowsExceptionForZeroOrNegativeAmount(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);

        assertThrows(TransactionService.InvalidTransaction.class, ()-> {
            transactionService.addTransaction(0.00, Category.TRANSPORTATION, "Zero Expense Test");
        },"A zero amount should return an error.");
        assertThrows(TransactionService.InvalidTransaction.class, ()-> {
            transactionService.addTransaction(-750.00, Category.TRANSPORTATION, "Negative Expense Test");
        },"A negative amount should return an error.");
    }

    @Test
    void testAddTransactionSuccessfullyThrowsExceptionForNullCategory(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);

        assertThrows(TransactionService.InvalidTransaction.class, ()-> {
            transactionService.addTransaction(150.00, null, "Null Category Test");
        },"An invalid category should return an error.");
    }

    @Test
    void testValidBalance(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(transactionRepository);

        transactionService.addTransaction(2000, Category.SALARY, "Monthly salary");
        transactionService.addTransaction(500, Category.SAVINGS, "Monthly savings");
        transactionService.addTransaction(150, Category.FOOD, "Solo date");

        assertEquals(1850, transactionService.calculateBalance(), "Should add salary, subtract food and ignore savings.");
    }

    @Test
    void testZeroBalanceBeforeAnyTransactionsHaveBeenMade(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService service = new TransactionService(transactionRepository);

        assertEquals(0, service.calculateBalance(), "Balance before any transaction is made should be zero.");
    }

    @Test
    void testGetTransactionByCategoryReturnsMatchingItems(){
        TransactionRepository transactionRepository = new TransactionRepository();
        TransactionService service = new TransactionService(transactionRepository);

        // 1. Adding multiple transactions across different categories
        service.addTransaction(100.00, Category.FOOD, "Groceries");
        service.addTransaction(50.00, Category.FOOD, "Snacks");
        service.addTransaction(1500.00, Category.RENT, "Monthly Rent");
        service.addTransaction(200.00, Category.UTILITIES, "Electricity");

        // 2. Fetch transactions from a single category (FOOD)
        List<Transactions> foodTransactions = service.getTransactionsByCategory(Category.FOOD);
        List<Transactions> entertainmentTransactions = service.getTransactionsByCategory(Category.ENTERTAINMENT);

        // 3. Verifying filtered list
        assertEquals(2, foodTransactions.size(), "Category FOOD should return only 2 transactions.");
        assertEquals(Category.FOOD, foodTransactions.getFirst().getCategory(), "First Transaction should be FOOD.");
        assertEquals(Category.FOOD, foodTransactions.get(1).getCategory(), "Second Transaction should be FOOD.");

        // 4. Verifying empty list
        assertEquals(0, entertainmentTransactions.size(), "Category ENTERTAINMENT should return only 0 transactions.");
    }

}
