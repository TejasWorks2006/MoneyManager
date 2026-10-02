package com.TraksComp.project.service;

import com.TraksComp.project.model.Budget;
import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;
import com.TraksComp.project.repository.TransactionRepository;

import java.util.HashMap;
import java.util.Map;

public class BudgetService {
    private Map<Category, Budget> Budgets = new HashMap<>();
    private final TransactionRepository transactionRepository;

    public BudgetService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setBudgetLimit(Category category, double limit){
        if (limit < 0) {
            throw new IllegalArgumentException("Budget limit cannot be negative.");
        }
        Budget budget = new Budget(category, limit);
        Budgets.put(category, budget);
    }
    public boolean isOverBudget(Category category){
        if(!Budgets.containsKey(category)) {
            return false;
        }

        Budget currBudget = Budgets.get(category);
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
