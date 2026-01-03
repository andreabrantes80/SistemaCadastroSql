# Sistema de Cadastro de Clientes (DAO)

Este sistema implementa um **Data Access Object (DAO)** para gerenciar operações de persistência de clientes em um banco de dados MySQL.  
O DAO encapsula toda a lógica de acesso ao banco, fornecendo métodos para **CRUD** (Create, Read, Update, Delete).

---

## 📦 Estrutura do Pacote
- **Pacote:** `cadastro`
- **Classe principal:** `ClienteDAO`
- **Dependências:**
  - `java.sql.*`
  - `java.time.*`
  - `java.util.List`

---

## 🗂️ Funcionalidades

### 1. Inserir Cliente
- **Método:** `inserir(Cliente c)`
- **SQL:**  
  ```sql
  INSERT INTO clientes (nome, email, telefone) VALUES (?, ?, ?)

  UPDATE clientes SET nome = ?, email = ?, telefone = ? WHERE id = ?
  
  DELETE FROM clientes WHERE id = ?

  SELECT id, nome, email, telefone, criado_em FROM clientes WHERE id = ?

  SELECT id, nome, email, telefone, criado_em 
FROM clientes 
WHERE nome LIKE ? 
ORDER BY nome

SELECT id, nome, email, telefone, criado_em FROM clientes ORDER BY id

🔄 Mapeamento de ResultSet
• 	Método: 
• 	Converte os dados do banco em um objeto .
• 	Campos mapeados:
• 	
• 	
• 	
• 	
• 	 → convertido para 
