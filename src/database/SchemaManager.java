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
                    name VARCHAR(255) UNIQUE NOT NULL,
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
        }
    }

    public static void seedData() {
        Connection conn = null;
        Statement st = null;

        try {
            conn = Database.getConnection();
            st = conn.createStatement();

            String sql = """
                INSERT INTO tb_products (name, price, description, img_url) VALUES
                    ('Pizza Calabresa', 42.50, 'Deliciosa pizza de calabresa com cebola e azeitonas.', 'https://example.com/pizza_calabresa.jpg'),
                    ('Pizza Margherita', 38.00, 'Clássica pizza Margherita com molho de tomate, mussarela e manjericão.', 'https://example.com/pizza_margherita.jpg'),
                    ('Pizza Portuguesa', 45.00, 'Pizza portuguesa com presunto, ovos, cebola e azeitonas.', 'https://example.com/pizza_portuguesa.jpg'),
                    ('Pizza Frango com Catupiry', 44.50, 'Saborosa pizza de frango desfiado com catupiry.', 'https://example.com/pizza_frango_catupiry.jpg'),
                    ('Pizza Quatro Queijos', 48.00, 'Combinação de quatro queijos: mussarela, provolone, gorgonzola e parmesão.', 'https://example.com/pizza_quatro_queijos.jpg'),
                    ('Coca-Cola Lata 350ml', 6.00, 'Refrigerante Coca-Cola em lata de 350ml.', 'https://example.com/coca_cola_lata.jpg'),
                    ('Suco de Laranja 300ml', 6.50, 'Suco natural de laranja em embalagem de 300ml.', 'https://example.com/suco_laranja.jpg'),
                    ('Água Mineral 500ml', 3.00, 'Água mineral sem gás em garrafa de 500ml.', 'https://example.com/agua_mineral.jpg')
                ON CONFLICT (name) DO NOTHING;
            """;

            st.executeUpdate(sql);
            System.out.println("Dados iniciais inseridos com sucesso!");
        }
        catch(SQLException e) {
            throw new DatabaseException("Erro ao inserir dados iniciais: " + e.getMessage());
        }
        finally {
            Database.closeStatement(st);
        }
    }

    public static void resetDatabase() {
        Connection conn = null;
        Statement st = null;

        try {
            conn = Database.getConnection();
            st = conn.createStatement();

            String sql = """
                DROP TABLE IF EXISTS tb_order_products CASCADE;
                DROP TABLE IF EXISTS tb_orders CASCADE;
                DROP TABLE IF EXISTS tb_products CASCADE;
            """;

            st.executeUpdate(sql);
            System.out.println("Banco de dados resetado com sucesso!");
        }
        catch(SQLException e) {
            throw new DatabaseException("Erro ao resetar banco: " + e.getMessage());
        }
        finally {
            Database.closeStatement(st);
        }
    }
}
