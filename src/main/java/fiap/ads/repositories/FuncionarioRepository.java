package fiap.ads.repositories;

import fiap.ads.models.ConexaoBD;
import fiap.ads.models.Funcionario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fiap.ads.Main.LOGGER;

public class FuncionarioRepository {
    public static final String URL_CONNECTION = ConexaoBD.URL_CONNECTION;
    public static final String USER = ConexaoBD.USER;
    public static final String PASSWORD = ConexaoBD.PASSWORD;
    private static final String TB_NAME = "FUNCIONARIO";
    private static final Map<String, String> TB_COLUMNS = Map.of(
            "ID", "id",
            "NOME", "nome",
            "CARGO", "cargo",
            "EMAIL", "email"
    );

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_CONNECTION, USER, PASSWORD);
    }

    public FuncionarioRepository(){
    }

    public Funcionario findById(int id){
        var funcionario = new Funcionario();
        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "SELECT * FROM %s WHERE %s = ?"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("ID")
                                )
                )
        )
        {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if(rs.next()){
                var _id = rs.getInt(TB_COLUMNS.get("ID"));
                var nome = rs.getString(TB_COLUMNS.get("NOME"));
                var cargo = rs.getString(TB_COLUMNS.get("CARGO"));
                var email = rs.getString(TB_COLUMNS.get("EMAIL"));

                funcionario = new Funcionario(_id,nome,cargo,email);
                LOGGER.info("Funcionario retornado com sucesso");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar funcionario numero: {0}", e.getMessage()));
        }

        return funcionario;
    }

    public void create(Funcionario funcionario){

        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "INSERT INTO %s (%s, %s, %s) VALUES (?,?,?)"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("NOME"),
                                        TB_COLUMNS.get("CARGO"),
                                        TB_COLUMNS.get("EMAIL")
                                )
                )
        )
        {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getEmail());

            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Funcionario criado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao criar funcionario: {0}", e.getMessage()));
        }
    }

    public List<Funcionario> readAll(){
        var lista = new ArrayList<Funcionario>();
        try (
                var conn = getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM "+TB_NAME)
        )
        {
            var rs = stmt.executeQuery();
            while (rs.next()){
                lista.add(
                        new Funcionario(
                                rs.getInt(TB_COLUMNS.get("ID")),
                                rs.getString(TB_COLUMNS.get("NOME")),
                                rs.getString(TB_COLUMNS.get("CARGO")),
                                rs.getString(TB_COLUMNS.get("EMAIL"))
                        )
                );
            }
            LOGGER.info("Funcionarios retornados com sucesso");
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar funcionarios: {0}", e.getMessage()));
        }
        return lista;
    }

    public int update(Funcionario funcionario){
        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("NOME"),
                                        TB_COLUMNS.get("CARGO"),
                                        TB_COLUMNS.get("EMAIL"),
                                        TB_COLUMNS.get("ID")
                                )
                )
        )
        {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setString(3, funcionario.getEmail());
            stmt.setInt(4, funcionario.getId());
            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Funcionario atualizado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao atualizar funcionario: {0}", e.getMessage()));
        }
        return 0;
    }

    public void delete(int id){
        try (
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "DELETE FROM %s WHERE %s = ?"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("ID")
                                )
                )
        )
        {
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Funcionario removido com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao deletar funcionario: {0}", e.getMessage()));
        }
    }
}
