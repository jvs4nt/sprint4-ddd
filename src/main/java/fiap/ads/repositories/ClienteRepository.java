package fiap.ads.repositories;

import fiap.ads.models.Cliente;
import fiap.ads.models.ConexaoBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fiap.ads.Main.LOGGER;
public class ClienteRepository {
    public static final String URL_CONNECTION = ConexaoBD.URL_CONNECTION;
    public static final String USER = ConexaoBD.USER;
    public static final String PASSWORD = ConexaoBD.PASSWORD;
    private static final String TB_NAME = "CLIENTE";
    private static final Map<String, String> TB_COLUMNS = Map.of(
            "ID", "id",
            "NOME", "nome",
            "EMAIL", "email",
            "CPF", "cpf"
    );

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_CONNECTION, USER, PASSWORD);
    }

    public ClienteRepository(){
    }

    public Cliente findById(int id){
        var cliente = new Cliente();
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
                var email = rs.getString(TB_COLUMNS.get("EMAIL"));
                var cpf = rs.getString(TB_COLUMNS.get("CPF"));
                cliente = new Cliente(_id,nome,email,cpf);
                LOGGER.info("Cliente retornado com sucesso");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar cliente: {0}", e.getMessage()));
        }

        return cliente;
    }

    public void create(Cliente cliente){

        try(
            var conn = getConnection();
            var stmt = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s) VALUES (?,?,?)"
                            .formatted(
                                    TB_NAME,
                                    TB_COLUMNS.get("NOME"),
                                    TB_COLUMNS.get("EMAIL"),
                                    TB_COLUMNS.get("CPF")
                            )
            )
        )
        {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());

            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Cliente criado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao criar cliente: {0}", e.getMessage()));
        }
    }

    public List<Cliente> readAll(){
        var lista = new ArrayList<Cliente>();
        try (
                var conn = getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM "+TB_NAME)
        )
        {
            var rs = stmt.executeQuery();
            while (rs.next()){
                lista.add(
                        new Cliente(
                                rs.getInt(TB_COLUMNS.get("ID")),
                                rs.getString(TB_COLUMNS.get("NOME")),
                                rs.getString(TB_COLUMNS.get("EMAIL")),
                                rs.getString(TB_COLUMNS.get("CPF"))
                        )
                );
            }
            LOGGER.info("Clientes retornados com sucesso");
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar clientes: {0}", e.getMessage()));
        }
        return lista;
    }

    public int update(Cliente cliente){
        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("NOME"),
                                        TB_COLUMNS.get("EMAIL"),
                                        TB_COLUMNS.get("CPF"),
                                        TB_COLUMNS.get("ID")
                                )
                )
        )
        {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setInt(4, cliente.getId());
            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Cliente atualizado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao atualizar cliente: {0}", e.getMessage()));
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
                LOGGER.info("Cliente removido com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao deletar cliente: {0}", e.getMessage()));
        }
    }
}
