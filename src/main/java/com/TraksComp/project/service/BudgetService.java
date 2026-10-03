package com.TraksComp.project.service;

import com.TraksComp.project.model.Budget;
import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;
import com.TraksComp.project.repository.BudgetRepository;
import com.TraksComp.project.repository.TransactionRepository;

import java.util.HashMap;
import java.util.Map;

public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    public void setBudgetLimit(Category category, double limit){
        if (limit < 0) {
            throw new IllegalArgumentException("Budget limit cannot be negative.");
        }
        Budget budget = new Budget(category, limit);
        budgetRepository.save(budget);
    }
    public boolean isOverBudget(Category category){
        Budget currBudget = budgetRepository.findByCategory(category);

        if(currBudget == null) {
            return false;
        }

        double currLimit = currBudget.getMax_spending();
        double currSpent = 0;

        for(Transactions t: transactionRepository.findALL()){
            if(t.getCategory() == category){
                currSpent += t.getAmount();
            }
        }
        return currLimit < currSpent;
    }
}
