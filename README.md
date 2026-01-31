# 🐘 JDBC PostgreSQL

Projeto Java para demonstração de conexão com banco de dados PostgreSQL usando JDBC com Maven.

## 📋 Pré-requisitos

- ☕ Java JDK 25
- 🐘 PostgreSQL instalado e em execução
- 📦 Maven 3.6 ou superior

## ⚙️ Configuração

### 1. Instale as dependências

```bash
mvn clean install
```

Isso irá baixar automaticamente o driver PostgreSQL JDBC 42.7.9 e outras dependências.

### 2. Configure o banco de dados

Crie um arquivo `database.properties` na raiz do projeto com suas credenciais:

```bash
cp database.properties.example database.properties
```

### 3. Edite o arquivo `database.properties`

Abra o arquivo e configure com suas credenciais locais:

```properties
user=seu_usuario_postgres
password=sua_senha_postgres
dburl=jdbc:postgresql://localhost:5432/seu_banco_de_dados
```


---

Desenvolvido com ☕ e 🐘
