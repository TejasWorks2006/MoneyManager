package com.TraksComp.project.service;

import com.TraksComp.project.model.Category;
import com.TraksComp.project.model.Transactions;
import com.TraksComp.project.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    public class InvalidTransaction extends IllegalArgumentException{
        public InvalidTransaction(){
            super();
        }

        public InvalidTransaction(String message){
            super(message);
        }
    }

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(double amount, Category category, String description){

        if(amount <= 0){
            throw new InvalidTransaction("Transaction amount must be greater than zero. Provided: " + amount);
        }else if(category == null){
            throw new InvalidTransaction("Transaction category must not be null.");
        }
        UUID id = UUID.randomUUID();
        Transactions t = new Transactions(id, amount,category,description);

        transactionRepository.save(t);
    }
    public double calculateBalance(){
        double bal = 0;

        List<Transactions> transactions = transactionRepository.findALL();

        if(transactions.isEmpty()){
            return 0;
        }

        for(Transactions transaction: transactions){
            if(transaction.getCategory() == Category.SALARY){
                bal += transaction.getAmount();
            }else if (transaction.getCategory() != Category.SAVINGS){
                bal -= transaction.getAmount();
            }
        }
        return bal;
    }
    public List<Transactions> getTransactionsByCategory(Category category){
        List<Transactions> collection = new ArrayList<Transactions>();
        List<Transactions> transactions = transactionRepository.findALL();
        for(Transactions transaction: transactions){
            if(transaction.getCategory() == category){
                collection.add(transaction);
            }
        }
        return collection;
    }
    public void showTransactions(){
        List<Transactions> transactions = transactionRepository.findALL();
        System.out.println("<------------- Transactions Made ------------->");
        for(Transactions transactions1 : transactions){
            System.out.println("\tAmount :" + transactions1.getAmount());
            System.out.println("\tCategory :" + transactions1.getCategory());
            System.out.println("\tDescription :" + transactions1.getDescription());
            System.out.println("<--------------------------------------------->");
        }
    }
}
