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
                    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                );
                
                CREATE TABLE IF NOT EXISTS tb_products (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) UNIQUE NOT NULL,
                    price DECIMAL(6, 2),
                    description TEXT,
                    image_url VARCHAR(255)
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
            conn.setAutoCommit(false);
            st = conn.createStatement();

            var rs = st.executeQuery("SELECT COUNT(*) FROM tb_products");
            rs.next();
            if(rs.getInt(1) > 0) {
                System.out.println("Dados já existem. Pulando inserção.");
                conn.rollback();
                return;
            }

            String sqlProducts = """
                INSERT INTO tb_products (name, price, description, image_url) VALUES
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
            st.executeUpdate(sqlProducts);

            String sqlOrders = """
                INSERT INTO tb_orders (latitude, longitude, moment, status) VALUES
                    (-23.5505, -46.6333, '2026-01-15 14:30:00', 'DELIVERED'),
                    (-23.5489, -46.6388, '2026-01-16 18:45:00', 'DELIVERED'),
                    (-23.5520, -46.6350, '2026-01-17 12:15:00', 'CANCELED'),
                    (-23.5530, -46.6310, '2026-01-18 19:20:00', 'DELIVERED'),
                    (-23.5495, -46.6375, '2026-01-19 20:10:00', 'PROCESSING'),
                    (-23.5510, -46.6340, '2026-01-20 13:00:00', 'SHIPPED'),
                    (-23.5515, -46.6355, '2025-12-24 11:30:00', 'PENDING'),
                    (-23.5500, -46.6320, '2026-02-03 15:45:00', 'PENDING');
            """;    
            st.executeUpdate(sqlOrders);

            String sqlOrderProducts = """
                INSERT INTO tb_order_products (order_id, product_id) VALUES
                    (1, 1), (1, 6),
                    (2, 2), (2, 3), (2, 8),
                    (3, 4), (3, 7),
                    (4, 5), (4, 6),
                    (5, 1), (5, 6),
                    (6, 2), (6, 1), (6, 8),
                    (7, 2), (7, 3), (7, 4), (7, 5), (7, 6), (7, 7),
                    (8, 1), (8, 2), (8, 3), (8, 4), (8, 5), (8, 6);
            """;
            st.executeUpdate(sqlOrderProducts);

            conn.commit();
            System.out.println("Dados iniciais inseridos com sucesso!");
        }
        catch(SQLException e) {
            try {
                if(conn != null) {
                    conn.rollback();
                    System.out.println("Rollback realizado!");
                }
            }
            catch(SQLException ex) {
                throw new DatabaseException("Erro ao fazer rollback: " + ex.getMessage());
            }
            throw new DatabaseException("Erro ao inserir dados iniciais: " + e.getMessage());
        }
        finally {
            try {
                if(conn != null) {
                    conn.setAutoCommit(true);
                }
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
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
