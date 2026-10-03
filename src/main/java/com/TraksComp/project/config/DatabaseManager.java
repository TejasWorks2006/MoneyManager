package com.TraksComp.project.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static Connection getConnection(){
        String projectRoot = System.getProperty("user.dir");

        String dbPath = projectRoot + File.separator +
                "src" + File.separator + "main" + File.separator +
                "java" + File.separator + "com" + File.separator +
                "TraksComp" + File.separator + "project" + File.separator + "moneymanager.db";

        String url = "jdbc:sqlite:" + dbPath;
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.err.println("Database connection error: " + e.getMessage());
        }
        return conn;
    }

    public static void initialiseDatabase(){
        // 1. Setting up table schema
        String createTransactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id TEXT PRIMARY KEY,
                    amount REAL NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT
                );
                """;

        String createBudgetsTable = """
                CREATE TABLE IF NOT EXISTS budgets (
                    category TEXT PRIMARY KEY,
                    max_spending REAL NOT NULL
                );
                """;

        // 2. Query Execution
        try(Connection conn = getConnection(); Statement stm = conn.createStatement()){
            stm.execute(createTransactionsTable);
            stm.execute(createBudgetsTable);
        }catch (SQLException e){
            System.err.println("Database initialisation failed: " + e.getMessage());
        }

    }
}
