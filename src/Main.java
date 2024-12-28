import java.sql.*;
import java.util.*;

public class Main {

    // Adicionar novo registro no banco de dados
    public static void newPerson(Connection conn, Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO kanbantable (name, level) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pessoa.getName());
            stmt.setInt(2, pessoa.getLevel().getLevel());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    pessoa.setId(generatedId);
                }
            }
        }
    }
    // Deletar registro
    public static void deletePerson(Connection conn, String name) throws SQLException {
        String sql = "DELETE FROM kanbantable WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    // Metodo para resetar a sequência quando não houver registros
    public static void resetSequenceIfEmpty(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM kanbantable";
        String resetSeqSql = "ALTER SEQUENCE kanban_userid_seq RESTART WITH 1";  // Reseta a sequência

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkSql);
            if (rs.next() && rs.getInt(1) == 0) {
                // Se não houver registros, reseta a sequência
                stmt.executeUpdate(resetSeqSql);
            }
        }
    }

    //Recuperar registros
    public static ArrayList<Pessoa> getPerson(Connection conn) throws SQLException {
        String sql = "SELECT name, level, userid FROM kanbantable";
        ArrayList<Pessoa> pessoas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                int level = rs.getInt("level");
                int id = rs.getInt("userid");
                pessoas.add(new Pessoa(name, level, id));
            }
        }
        return pessoas;
    }

    // Atualiza o banco de dados
    public static void updatePerson(Connection conn, Pessoa pessoa) throws SQLException {
        String sql = "UPDATE kanbantable SET name = ?, level = ? WHERE userid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getName());
            stmt.setInt(2, pessoa.getLevel().getLevel());
            stmt.setInt(3, pessoa.getId());
            stmt.executeUpdate();
        }
    }
    // Função Principal
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Pessoa> pessoas = new ArrayList<>();


        String url = "jdbc:postgresql://localhost:5432/KanbanDatabase";
        String user = "postgres";
        String password = "lilY@";

        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Conexão bem-sucedida\nIniciando o programa...\n\n");

        pessoas.clear(); // Limpando a lista antes da recuperação dos dados
        pessoas = getPerson(conn); // Recupera os dados

        boolean terminate = false;
        while (!terminate) {



            // Ordena a lista pelo nome
            pessoas.sort(Comparator.comparing(Pessoa::getName));


            // Exibe a logo
            //logo();
            // Menu de escolhas
            System.out.println("1. Adicionar Novo Registro.");
            System.out.println("2. Vizualizar.");
            System.out.println("3. Atualizar Registro.");
            System.out.println("4. Deletar registro");
            System.out.println("5. Encerrar...");
            System.out.printf("O que faremos hoje? ");
            int userInput = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            // Logica do programa
            switch (userInput) {
                // Logica para adicionar 'Crud'
                case 1 -> {
                    System.out.print("Digite o nome do novo registro: ");
                    String name = scanner.nextLine();
                    char userChoice = 'x';

                    while (userChoice != 'y' && userChoice != 'n') {
                        System.out.printf("Deseja confirmar o registro? ('y'/'n') ");
                        userChoice = scanner.next().charAt(0);
                        scanner.nextLine();

                        // Cria novo registro caso usuario confirme
                        if (Methods.confirm(userChoice) == 1) {
                            Pessoa currentPersonn = new Pessoa(name, 0);
                            // Adiciona o registro temporário com ID = 0
                            pessoas.add(currentPersonn);
                            // Insere no banco e atualiza o ID
                            newPerson(conn, currentPersonn);
                            System.out.println("Novo registro adicionado.");
                            System.out.println("ID gerado: " + currentPersonn.getId());  // Exibindo o ID gerado
                            System.out.println();
                            break;
                        } else if (Methods.confirm(userChoice) == 0) {
                            System.out.println("Retornando ao menu principal.");
                            System.out.println();
                            break;
                        } else System.out.println("Opção invalida");
                    }
                }

                // Logica para Vizualidar 'cRud'
                case 2 -> {
                    if (pessoas.isEmpty()) {
                        System.out.println("Sem registros no banco de dados");
                        System.out.println();
                    }
                    else
                        Methods.showAll(pessoas);
                }

                // Logica para Atualizar 'crUd'
                case 3 -> {
                    if (pessoas.isEmpty()) {
                        System.out.println("Sem registros no banco de dados");
                    } else {
                        Methods.showAll(pessoas);
                        System.out.println("Quem deseja atualizar? ");
                        String name = scanner.nextLine();
                        int index = Methods.search(pessoas, name, 0, pessoas.size() - 1);

                        if (index == -1) {
                            System.out.println(name + " não encontrada(o)!");
                            break;
                        }

                        Pessoa currentPerson = pessoas.get(index);
                        int choice = 0;
                        while (choice != 3) {
                            System.out.printf("%-8s | %-25s | %-15s\n", "Registro", "Nome", "Status");
                            System.out.println("----------------------------------------------");
                            System.out.printf("%-8d | %-25s | %-15s\n", currentPerson.getId(), currentPerson.getName(), currentPerson.getLevel());

                            System.out.println();
                            System.out.println("1. Atualizar Nome.");
                            System.out.println("2. Atualizar Status.");
                            System.out.println("3. Voltar ao menu");
                            System.out.printf("O que deseja atualizar? ");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (choice == 1) {
                                System.out.print("Qual nome deseja alterar? ");
                                String newName = scanner.nextLine();
                                currentPerson.setName(newName);
                                updatePerson(conn, currentPerson);
                                System.out.println("Nome atualizado!");
                            } else if (choice == 2) {
                                System.out.printf("Qual Status deseja definir %s? ('junior'/'pleno'/'senior') ", currentPerson.getName());
                                String newStatus = scanner.nextLine();
                                if (!newStatus.equalsIgnoreCase("junior") && !newStatus.equalsIgnoreCase("pleno") && !newStatus.equalsIgnoreCase("senior")) {
                                    System.out.println("Opção inválida, insira novamente:");
                                } else {
                                    switch (newStatus.toLowerCase()) {
                                        case "junior" -> currentPerson.setLevel(Pessoa.LevelType.JUNIOR.getLevel());
                                        case "pleno" -> currentPerson.setLevel(Pessoa.LevelType.PLENO.getLevel());
                                        case "senior" -> currentPerson.setLevel(Pessoa.LevelType.SENIOR.getLevel());
                                    }
                                    updatePerson(conn, currentPerson);
                                    System.out.println("Status atualizado!");
                                }
                            } else if (choice == 3) {
                                break;
                            } else {
                                System.out.println("Opção inválida!");
                            }
                        }
                    }
                }
                    // Logica para Deletar 'cruD'
                case 4 -> {
                    if (pessoas.isEmpty()) {
                        System.out.println("Nenhum registro no banco de dados");
                    } else {
                        // Exibe a lista de registros e pergunta qual deseja excluir
                        Methods.showAll(pessoas);
                        System.out.println("Digite o nome do registro a ser excluído: ");
                        String name = scanner.nextLine();
                        int index = Methods.search(pessoas, name, 0, pessoas.size() - 1);

                        if (index == -1) {
                            System.out.println(name + " não encontrada(o)!");
                        } else if (confirmAction(scanner, "Deseja mesmo excluir o registro de " + name)) {
                            // Exclui o registro selecionado
                            deletePerson(conn, name);
                            pessoas.remove(index);  // Remove da lista local
                            System.out.println("Registro excluído.");

                            // Verifica se a tabela está vazia e reseta a sequência
                            resetSequenceIfEmpty(conn);
                        } else {
                            System.out.println("Ação cancelada.");
                        }
                    }
                    System.out.println();
                }
                // Encerrar programa e exibir logo
                case 5 -> {
                    System.out.println("Encerrando o programa...");
                    conn.close(); // Fechar conexão com banco de dados
                    showAuthorMessage();
                    terminate = true;
                }
                default ->
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static boolean confirmAction (Scanner scanner, String message) {
        char choice;
        do {
            System.out.printf("%s ('y'/'n'): ", message);
            choice = scanner.next().charAt(0);
            scanner.nextLine();
            if (choice == 'y') return true;
            if (choice == 'n') return false;
            System.out.println("Opção invalida. Tente novamente!");
        } while (true);
    }





    private static void logo(){
        System.out.println(" _   __               _                   \n" +
                "| | / /              | |                  \n" +
                "| |/ /   __ _  _ __  | |__    __ _  _ __  \n" +
                "|    \\  / _` || '_ \\ | '_ \\  / _` || '_ \\ \n" +
                "| |\\  \\| (_| || | | || |_) || (_| || | | |\n" +
                "\\_| \\_/ \\__,_||_| |_||_.__/  \\__,_||_| |_|\n" +
                "                                          \n" +
                "                                          \n");
    }

    private static void showAuthorMessage() {
        System.out.println("\n=== Software made by Mateus D Barros ===");
        logo();
        System.out.println("109 97 116 101 117 115 68 98 97 114 114 111 115");
    }
}