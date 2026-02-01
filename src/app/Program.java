package app;

import database.SchemaManager;
import database.Database;

public class Program {
    public static void main(String[] args) {
        try {
            SchemaManager.createTables();
            SchemaManager.seedData();
        }
        finally {
            Database.closeConnection();
        }
    }
}