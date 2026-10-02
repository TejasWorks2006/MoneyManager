package com.TraksComp.project.repository;

import com.TraksComp.project.model.Transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private final List<Transactions> transactionsList = new ArrayList<>();

    public void save(Transactions t){
        transactionsList.add(t);
    }
    public List<Transactions> findALL(){
        return transactionsList;
    }
}