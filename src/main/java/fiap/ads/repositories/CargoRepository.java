package fiap.ads.repositories;

import fiap.ads.models.Cargo;
import fiap.ads.models.ConexaoBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fiap.ads.Main.LOGGER;

public class CargoRepository {
    public static final String URL_CONNECTION = ConexaoBD.URL_CONNECTION;
    public static final String USER = ConexaoBD.USER;
    public static final String PASSWORD = ConexaoBD.PASSWORD;
    private static final String TB_NAME = "CARGO";
    private static final Map<String, String> TB_COLUMNS = Map.of(
            "ID", "id",
            "NOME", "nome",
            "DESCRICAO", "descricao"
    );

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_CONNECTION, USER, PASSWORD);
    }

    public CargoRepository(){
    }

    public Cargo findById(int id){
        var cargo = new Cargo();
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
                var descricao = rs.getString(TB_COLUMNS.get("DESCRICAO"));

                cargo = new Cargo(_id,nome,descricao);
                LOGGER.info("Cargo retornado com sucesso");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar cargo: {0}", e.getMessage()));
        }

        return cargo;
    }

    public void create(Cargo cargo){

        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "INSERT INTO %s (%s, %s) VALUES (?,?)"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("NOME"),
                                        TB_COLUMNS.get("DESCRICAO")
                                )
                )
        )
        {
            stmt.setString(1, cargo.getNome());
            stmt.setString(2, cargo.getDescricao());

            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Cargo criado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao criar cargo: {0}", e.getMessage()));
        }
    }

    public List<Cargo> readAll(){
        var lista = new ArrayList<Cargo>();
        try (
                var conn = getConnection();
                var stmt = conn.prepareStatement("SELECT * FROM "+TB_NAME)
        )
        {
            var rs = stmt.executeQuery();
            while (rs.next()){
                lista.add(
                        new Cargo(
                                rs.getInt(TB_COLUMNS.get("ID")),
                                rs.getString(TB_COLUMNS.get("NOME")),
                                rs.getString(TB_COLUMNS.get("DESCRICAO"))
                        )
                );
            }
            LOGGER.info("Cargos retornados com sucesso");
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao buscar cargos: {0}", e.getMessage()));
        }
        return lista;
    }

    public int update(Cargo cargo){
        try(
                var conn = getConnection();
                var stmt = conn.prepareStatement(
                        "UPDATE %s SET %s = ?, %s = ? WHERE %s = ?"
                                .formatted(
                                        TB_NAME,
                                        TB_COLUMNS.get("NOME"),
                                        TB_COLUMNS.get("DESCRICAO"),
                                        TB_COLUMNS.get("ID")
                                )
                )
        )
        {
            stmt.setString(1, cargo.getNome());
            stmt.setString(2, cargo.getDescricao());
            stmt.setInt(3, cargo.getId());
            var rs = stmt.executeUpdate();
            if (rs == 1){
                LOGGER.info("Cargo atualizado com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao atualizar cargo: {0}", e.getMessage()));
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
                LOGGER.info("Cargo removido com sucesso!");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            LOGGER.error(MessageFormat.format("Erro ao deletar cargo: {0}", e.getMessage()));
        }
    }
}
