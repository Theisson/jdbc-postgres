package app;

import database.SchemaManager;

public class Program {
    public static void main(String[] args) {
        try {
            SchemaManager.createTables();
            SchemaManager.seedData();
        }
        finally {
            database.Database.closeConnection();
        }
    }
}