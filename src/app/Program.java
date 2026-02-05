package app;

import database.SchemaManager;
import models.Order;
import models.OrderStatus;
import models.Price;
import models.Product;
import services.DiscountService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import database.Database;

public class Program {
    public static void main(String[] args) throws SQLException {
        try {
            SchemaManager.createTables();
            SchemaManager.seedData();

            Connection conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("""
                SELECT * FROM tb_orders
                INNER JOIN tb_order_products ON tb_orders.id = tb_order_products.order_id
                INNER JOIN tb_products ON tb_products.id = tb_order_products.product_id
                    """);

            Map<Long, Order> orderMap = new HashMap<>();
            Map<Long, Product> productMap = new HashMap<>();

            while(rs.next()) {
                Long orderId = rs.getLong("order_id");
                Long productId = rs.getLong("product_id");

                if(orderMap.get(orderId) == null) {
                    Order order = instantiateOrder(rs);
                    orderMap.put(orderId, order);
                }

                if(productMap.get(productId) == null) {
                    Product product = instantiateProduct(rs);
                    productMap.put(productId, product);
                }

                orderMap.get(orderId).getProducts().add(productMap.get(productId));
            }

            // EXIBINDO PEDIDOS ANTES DO DESCONTO
            System.out.println("=== PEDIDOS ANTES DO DESCONTO ===\n");
            for(Order order : orderMap.values()) {
                System.out.println(order);
                System.out.println("Pode desconto? " + (order.canApplyDiscount() ? "Sim" : "Não"));

                for(Product product : order.getProducts()) {
                    System.out.println("  " + product);
                }
                System.out.println();
            }

            // APLICANDO DESCONTOS
            System.out.println("=== APLICANDO DESCONTOS ===");
            DiscountService discountService = new DiscountService();

            for(Order order : orderMap.values()) {
                if(order.canApplyDiscount()) {
                    try {
                        discountService.apply(order);
                    }
                    catch(IllegalStateException | IllegalArgumentException e) {
                        System.out.println("Não foi possível aplicar desconto: " + e.getMessage());
                    }
                }
            }

            Database.closeStatement(st);
        }
        finally {
            Database.closeConnection();
        }
    }

    private static Product instantiateProduct(ResultSet rs) throws SQLException {
        Long id = rs.getLong("product_id");
        String name = rs.getString("name");
        Price price = new Price(rs.getBigDecimal("price"));
        String description = rs.getString("description");
        String imageUrl = rs.getString("image_url");
        
        return new Product(id, name, price, description, imageUrl);
    }

    private static Order instantiateOrder(ResultSet rs) throws SQLException {
        Long id = rs.getLong("order_id");
        Double latitude = rs.getDouble("latitude");
        Double longitude = rs.getDouble("longitude");
        Instant moment = rs.getTimestamp("moment").toInstant();
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        return new Order(id, latitude, longitude, moment, status);
    }
}