package cadastro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMYSQL {

    private static final String HOST = "localhost";

    private static final int PORTA = 3306;

    private static final String BANCO = "cadastro_db";

    private static final String USUARIO = "root";

    private static final String SENHA = "$Aluno123BD";

    private static final String URL = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&characterEncoding=utf8",
            HOST,
            PORTA,
            BANCO
    );

    public static Connection obterConexao() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }


}
