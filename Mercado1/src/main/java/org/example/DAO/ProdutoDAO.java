package org.example.DAO;

import com.mysql.cj.MysqlConnection;
import org.example.conexao.conexaoMySQL;
import org.example.Modelo.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    //CRUD

    //metodo de cadastro de objetos

    public void cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = conexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.executeUpdate();

            //Obtem o ID gerado pelo banco de dados
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()){
                produto.setId(generatedKeys.getInt(1));
            }
            System.out.println("Produto cadastrado com sucesso! ID: " + produto.getId());

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto:" + e.getMessage());
        }
    }

    public List<Produto> listarProduto() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = conexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()){
                Produto produto = new Produto(rs.getString("nome"), rs.getDouble("preco"), rs.getInt("quantidade"));
                produto.setId(rs.getInt("id"));
                produtos.add(produto);
            }
        }catch (SQLException e){
            System.out.println("Erro ao listar produtos:" + e.getMessage());
        }
        return produtos;
    }
    public void atualizarProduto(Produto produto){
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (Connection conn = conexaoMySQL.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Produto atualizado com sucesso\n");
            }else {
                System.out.println("Produto não econtrado\n");
            }
        }catch (SQLException e){
            System.out.println("Erro ao atualizar o produto:\n" + e.getMessage());
        }
    }

    //Quando fazia um cadastro, os ID do banco e do código eram diferentes
    //Então utilizei o TRUNCATE TABLE para corrigir isso, pois ele exclui e reinicia os registros
    public void truncarTable(){
        String sql = "TRUNCATE TABLE produtos";

        try (Connection conn = conexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
            System.out.println("Tabela 'produtos' truncada e IDs reiniciados.");
        }catch (SQLException e){
        System.out.println("Erro ao truncar a tabela:" + e.getMessage());
        }
    }
    public Produto buscarProdutoPeloId(int id){
        String sql = "SELECT * FROM produtos where id = ?";
        Produto produto = null;

        try(Connection conn = conexaoMySQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                produto = new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
                produto.setId(rs.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println("Erro ao buscar o produto:" + e.getMessage());
        }
        return produto;
    }

    public void deletarProduto(int id){
        String sql = "DELETE FROM produtos WHERE id =?";

        try(Connection conn = conexaoMySQL.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produto deletado com sucesso!");
            }else {
                System.out.println("Produto não encontado");
            }
            }catch (SQLException e) {
            System.out.println("Erro ao deletar produto:" + e.getMessage());
        }
    }
}