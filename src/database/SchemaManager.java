package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManager {
    public static void createTables() {
        Connection conn = null;
        Statement st = null;

        try {
            conn = Database.getConnection();
            st = conn.createStatement();

            String sql = """
                CREATE TABLE IF NOT EXISTS tb_orders (
                    id BIGSERIAL PRIMARY KEY,
                    latitude DOUBLE PRECISION,
                    longitude DOUBLE PRECISION,
                    moment TIMESTAMP WITHOUT TIME ZONE,
                    status SMALLINT
                );
                
                CREATE TABLE IF NOT EXISTS tb_products (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255),
                    price DECIMAL(6, 2),
                    description TEXT,
                    img_url VARCHAR(255)
                );

                CREATE TABLE IF NOT EXISTS tb_order_products (
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    PRIMARY KEY (order_id, product_id),
                    CONSTRAINT fk_tb_order_products_order FOREIGN KEY (order_id) REFERENCES tb_orders(id),
                    CONSTRAINT fk_tb_order_products_product FOREIGN KEY (product_id) REFERENCES tb_products(id)
                );
            """;

            st.executeUpdate(sql);
            System.out.println("Tabelas criadas com sucesso!");
        }
        catch(SQLException e) {
            throw new DatabaseException("Erro ao criar tabelas: " + e.getMessage());
        }
        finally {
            Database.closeStatement(st);
            Database.closeConnection();
        }
    }
}
