package models;

public enum OrderStatus {
    PENDING("Pendente", "Pedido aguardando confirmação"),
    PROCESSING("Em Processamento", "Pedido está sendo preparado"),
    SHIPPED("Enviado", "Pedido a caminho"),
    DELIVERED("Entregue", "Pedido entregue ao cliente"),
    CANCELED("Cancelado", "Pedido foi cancelado");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}