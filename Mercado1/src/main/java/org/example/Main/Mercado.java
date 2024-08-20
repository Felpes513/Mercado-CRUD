package org.example.Main;

import org.example.DAO.ProdutoDAO;
import org.example.Modelo.Produto;
import org.example.utils.Utils;

import java.util.*;

public class Mercado {
    private static Scanner input = new Scanner(System.in);
    private static ArrayList<Produto> produtos;
    private static Map<Produto, Integer> carrinho;

    public static void main(String[] args) {
        produtos = new ArrayList<>();
        carrinho = new HashMap<>();
        menu();

    }

    private static void menu() {
        System.out.println("---------------------------------------------------");
        System.out.println("------------------Bem vindo ao Mercado-------------");
        System.out.println("---------------------------------------------------");
        System.out.println("**** Selecione uma operação que deseja realizar ***");
        System.out.println("|   Opção 1 -- Cadastrar  |");
        System.out.println("|   Opção 2 -- Listar   |");
        System.out.println("|   Opção 3 -- Comprar  |");
        System.out.println("|   Opção 4 -- Carrinho |");
        System.out.println("|   Opçao 5 -- Excluir |");
        System.out.println("|   Opçao 6 -- Atualizar |");
        System.out.println("|   Opçao 7 -- Sair |");

        int option = input.nextInt();

        switch (option){
            case 1:
                cadastrarProdutos();
                break;
            case 2:
                listarProdutos();
                break;
            case 3:
                comprarProdutos();
                break;
            case 4:
                verCarrinho();
                break;
            case 5:
                deletarProduto();
            case 6:
                atualizarProduto();
            case 7:
                System.out.println("Volte Sempre :)");
                System.exit(0);
            default:
                System.out.println("Opções invalidas");
                menu();
                break;
        }
    }

    private static void cadastrarProdutos(){
        System.out.println("Nome do produto");
        String nome = input.next();

        System.out.println("Preço do produto");
        Double preco = input.nextDouble();

        System.out.println("Quantidade do produto");
        int quantidade = input.nextInt();

        Produto produto = new Produto(nome, preco, quantidade);
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.cadastrarProduto(produto);// salvar no banco de dados

        System.out.println(produto.getNome() + " cadastrado com sucesso");
        menu();

    }

    private static void listarProdutos(){
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarProduto();

        if (produtos.size() > 0){
            System.out.println("Listar Produtos!\n");

            for (Produto p : produtos){
                System.out.println(p);
            }
        }else{
            System.out.println("Nenhum produto cadastrado");
        }
        menu();
    }

    private static void comprarProdutos(){
        if (produtos.size() > 0){
            System.out.println("Código do produto\n");

            System.out.println("-----------Produtos Disponiveis-----------");
            for (Produto p : produtos){
                System.out.println(p + "\n");
            }
            int id = Integer.parseInt(input.next());
            boolean isPresent = false;

            for(Produto p : produtos){
                if (p.getId() == id){
                    int qtd = 0;
                    try {
                        qtd = carrinho.get(p);
                        carrinho.put(p, qtd +1);
                    }catch (NullPointerException e){
                        carrinho.put(p, 1);
                    }
                    System.out.println(p.getNome() + "adicionando ao carrinho\n");
                    isPresent = true;

                    if (isPresent){
                        System.out.println("Deseja adicionar ao carrinho?\n");
                        System.out.println("Digite 1 para sim, ou 0 para finalizar a compra.\n");
                        int option = Integer.parseInt(input.next());

                        if (option == 1){
                            comprarProdutos();
                        }else{
                            finalizarCompras();
                        }
                        return;
                    }
                }else {
                    System.out.println("Produto não encontrado");
                    menu();
                }
            }
        }else {
            System.out.println("Não existem produtos cadastrado");
            menu();
        }
    }
    private static void verCarrinho() {
        System.out.println("Produtos no carrinho");
        if (carrinho.size() > 0) {
            for (Produto p : carrinho.keySet()) {
                System.out.println("Produto:" + p + "\nQuantidade" + carrinho.get(p));
            }
        } else {
            System.out.println("Carrinho vazio!");
        }
        menu();
    }

    private static void finalizarCompras(){
        Double valorDaCompra = 0.0;
        System.out.println("Seus produtos");

        for (Produto p : carrinho.keySet()){
            int qtd = carrinho.get(p);
            valorDaCompra += p.getPreco() + qtd;
            System.out.println(p);
            System.out.println("Quantidade" + qtd);
            System.out.println("-----------------");
        }
        System.out.println("O valor da sua compra é:" + Utils.doubleToString(valorDaCompra));
        carrinho.clear();
        System.out.println("Obrigado pela preferencia :)");
        menu();
    }
    private static void atualizarProduto(){
        System.out.println("Digite o ID do produto que deseja atualizar\n");
        int id = input.nextInt();

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.buscarProdutoPeloId(id);

        if (produto != null){
            System.out.println("Nome atual:" + produto.getNome());
            System.out.println("Digite o novo nome (ou pressione Enter para manter o mesmo nome)");
            input.nextLine();
            String nome = input.nextLine();
            if (!nome.isEmpty()){
                produto.setNome(nome);
            }

            System.out.println("Preço atual do produto:" + produto.getPreco());
            System.out.println("Digite o novo preço do produto (ou pressione Enter para continuar o mesmo preço)");
            double preco = input.nextDouble();
            if (preco > 0){
                produto.setPreco(preco);
            }

            System.out.println("Quantidade do produto atual:" + produto.getQuantidade());
            System.out.println("Digite a nova quantidade do produto (ou pressione Enter para manter a mesma quantidade");
            int quantidade = input.nextInt();
            if (quantidade > 0){
                produto.setQuantidade(quantidade);
            }
            produtoDAO.atualizarProduto(produto); //chama o DAO para atualizar o programa
        }else{
            System.out.println("Produto não encontrado.");

        }
    }


    //função excluir
    private static void deletarProduto(){
        System.out.println("Digite o ID do produto que deseja deletar");
        int id = input.nextInt();

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.deletarProduto(id);

        menu();
    }
}