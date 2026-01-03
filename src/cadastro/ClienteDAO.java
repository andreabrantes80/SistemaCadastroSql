package cadastro;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String SQL_INSERIR = "INSERT INTO clientes (nome, email, telefone) VALUES (?, ?, ?)";

    private static final String SQL_ATUALIZAR = "UPDATE clientes SET nome = ?, email = ? , telefone = ? WHERE id = ? ";

    private static final String SQL_DELETAR = "DELETE FROM clientes WHERE id = ?";

    private static final String SQL_BUSCAR_POR_ID = "SELECT id, nome, email, telefone, criado_em FROM clientes WHERE id = ? ";

    private static final String SQL_BUSCAR_POR_NOME_LIKE = "SELECT id, nome, email, telefone, criado_em FROM clientes WHERE nome LIKE ? ORDER BY nome";

    private static final String SQL_LISTAR_TODOS = "SELECT id, nome, email, telefone, criado_em FROM clientes ORDER BY id";

    public Integer inserir(Cliente c) throws SQLException {

        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    int ideGerado = rs.getInt(1);
                    c.setId(ideGerado);
                    return ideGerado;
                }
            }
        }
        return null;

    }

    public boolean atualizar(Cliente c) throws SQLException {
        if (c.getId() == null)
            throw new IllegalArgumentException("ID não pode ser nulo para atualizar.");
        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_ATUALIZAR)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setInt(4, c.getId());

            return ps.executeUpdate() > 0;

        }

    }

    public boolean deletarPorId(int id) throws SQLException {
        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_DELETAR) ){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }

    }

    public Cliente buscarPorId(int id) throws SQLException {
        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapear(rs);
            }
        }
        return null;

    }

    public List<Cliente> buscarPorNome(String trecho) throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOME_LIKE)) {
            ps.setString(1, "%" + (trecho == null ? "" : trecho.trim()) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    lista.add(mapear(rs));
            }
        }
        return lista;

    }

    public List<Cliente> listarTodos() throws SQLException{

        List<Cliente> lista = new ArrayList<>();

        try (Connection con = ConexaoMYSQL.obterConexao();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS)) {
            ResultSet rs = ps.executeQuery();{

                while (rs.next())
                    lista.add(mapear(rs));

            }

        }
        return lista;

    }

    private Cliente mapear(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String telefone = rs.getString("telefone");
        Timestamp ts = rs.getTimestamp("criado_em");

        LocalDateTime criadoEm = ts == null ? null : LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());

        return new Cliente(id, nome, email, telefone, criadoEm);

    }


}
