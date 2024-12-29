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

    // Reseta a sequencia caso vazia
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

    // Sequências ANSI
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";

    // Função Principal
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Pessoa> pessoas;


        String url = "jdbc:postgresql://localhost:5432/KanbanDatabase"; // Troque o nome do banco de dados de acordo com o seu
        String user = "postgres";
        String password = "*****"; //utilize a senha de seu PostgreSQL

        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Conexão bem-sucedida\nIniciando o programa...\n\n");

        pessoas = getPerson(conn); // Recupera os dados
        carregamento();

        boolean terminate = false;
        while (!terminate) {



            // Ordena a lista pelo nome
            pessoas.sort(Comparator.comparing(Pessoa::getName));


            // Exibe a logo
            //logo();
            // Menu de escolhas
            exibirMenu();
            int userInput = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            // Logica do programa
            switch (userInput) {
                // Logica para adicionar 'Crud'
                case 1 -> {
                    System.out.print("Digite o nome do novo registro: ");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) {
                        System.out.println(RED + "Erro: Nome não pode ser vazio!" + RESET);
                        System.out.println();
                        break; // Encerra a execução do case 1
                    }

                    String userChoice = "";

                    while (!userChoice.equals("y") && !userChoice.equals("n")) {
                        System.out.print("Deseja confirmar o registro? ('y'/'n'): ");
                        userChoice = scanner.nextLine().trim(); // Use trim() para evitar espaços extras
                    }

                    // Se o usuário confirmar
                    if (userChoice.equals("y")) {
                        Pessoa currentPerson = new Pessoa(name, 0);
                        // Adiciona o registro temporário
                        pessoas.add(currentPerson);
                        // Insere no banco e atualiza o ID
                        newPerson(conn, currentPerson);
                        System.out.println(GREEN + "Registro adicionado com sucesso!" + RESET);
                        System.out.println("ID gerado: " + currentPerson.getId());  // Exibindo o ID gerado
                    } else {
                        System.out.println("Registro cancelado. Retornando ao menu principal.");
                    }
                }


                // Logica para Vizualidar 'cRud'
                case 2 -> {
                    if (pessoas.isEmpty()) {
                        System.out.println("Sem registros no banco de dados");
                        System.out.println();
                    }
                    else {
                        Methods.showAll(pessoas);
                        System.out.println();
                    }
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
                            System.out.println(RED + "Erro: Nome não encontrado!" + RESET);
                            break;
                        }

                        Pessoa currentPerson = pessoas.get(index);
                        int choice = 0;
                        while (choice != 3) {
                            System.out.printf("%-8s | %-25s | %-15s\n", "Registro", "Nome", "Status");
                            System.out.println("----------------------------------------------");
                            System.out.printf("%-8d | %-25s | %-15s\n", currentPerson.getId(), currentPerson.getName(), currentPerson.getLevel());

                            System.out.println();
                            System.out.println(BLUE+ "1. Atualizar Nome." +RESET);
                            System.out.println(BLUE+"2. Atualizar Status." +RESET);
                            System.out.println(RED+"3. Voltar ao menu" +RESET);
                            System.out.print("O que deseja atualizar? ");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println();

                            if (choice == 1) {
                                System.out.print("Qual nome deseja alterar? ");
                                String newName = scanner.nextLine();
                                currentPerson.setName(newName);
                                updatePerson(conn, currentPerson);
                                carregamento();
                                System.out.println(GREEN + "Registro atualizado com sucesso!" + RESET);
                            } else if (choice == 2) {
                                System.out.printf("Qual Status deseja definir %s? ('junior'/'pleno'/'senior') ", currentPerson.getName());
                                String newStatus = scanner.nextLine();
                                if (!newStatus.equalsIgnoreCase("junior") && !newStatus.equalsIgnoreCase("pleno") && !newStatus.equalsIgnoreCase("senior")) {
                                    System.out.println(RED +"Opção inválida!" +RESET);
                                } else {
                                    switch (newStatus.toLowerCase()) {
                                        case "junior" -> currentPerson.setLevel(Pessoa.LevelType.JUNIOR.getLevel());
                                        case "pleno" -> currentPerson.setLevel(Pessoa.LevelType.PLENO.getLevel());
                                        case "senior" -> currentPerson.setLevel(Pessoa.LevelType.SENIOR.getLevel());
                                    }
                                    updatePerson(conn, currentPerson);
                                    carregamento();
                                    System.out.println(GREEN + "Status atualizado com sucesso!" + RESET);
                                }
                            } else if (choice != 3) {
                                System.out.println(RED +"Opção inválida!" +RESET);
                                break;
                            }
                        }
                    }
                }
                    // Logica para Deletar 'cruD'
                case 4 -> {
                    if (pessoas.isEmpty()) {
                        System.out.println(RED +"Erro: Nenhum registro no banco de dados" +RESET);
                    } else {
                        // Exibe a lista de registros e pergunta qual deseja excluir
                        Methods.showAll(pessoas);
                        System.out.println("Digite o nome do registro a ser excluído: ");
                        String name = scanner.nextLine();
                        int index = Methods.search(pessoas, name, 0, pessoas.size() - 1);

                        if (index == -1) {
                            System.out.println(RED + "Erro: Nome não encontrado!" + RESET);
                        } else if (confirmAction(scanner, "Deseja mesmo excluir o registro de " + name)) {
                            // Exclui o registro selecionado
                            deletePerson(conn, name);
                            carregamento();
                            pessoas.remove(index);  // Remove da lista local
                            System.out.println(GREEN + "Registro excluido com sucesso!" + RESET);
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
                    System.out.println(RED+ "Opção inválida!" +RESET);
            }
        }
    }

    private static void carregamento() throws InterruptedException {
        System.out.print("Carregando: ");
        for (int i = 0; i <= 100; i += 10) {
            System.out.print("\rCarregando: [" + "=".repeat(i / 10) + " ".repeat(10 - i / 10) + "] " + i + "%");
            Thread.sleep(200); // Simula o progresso
        }
        System.out.println("\nCarregamento concluído!");

    }

    private static void exibirMenu() {
        System.out.println(GREEN + "========== MENU ==========" + RESET);
        System.out.println("1. " + BLUE + "Adicionar Novo Registro" + RESET);
        System.out.println("2. " + YELLOW + "Visualizar Registros" + RESET);
        System.out.println("3. " + BLUE + "Atualizar Registro" + RESET);
        System.out.println("4. " + RED + "Deletar Registro" + RESET);
        System.out.println("5. " + RED + "Encerrar" + RESET);
        System.out.println(GREEN + "==========================" + RESET);

    }


    private static boolean confirmAction (Scanner scanner, String message) {
        char choice;
        do {
            System.out.printf("%s ('y'/'n'): ", message);
            choice = scanner.next().charAt(0);
            scanner.nextLine();
            if (choice == 'y') return true;
            if (choice == 'n') return false;
            System.out.println(RED+" ERRO: Opção invalida. Tente novamente!" +RESET);
        } while (true);
    }





    private static void logo() {
        System.out.println(YELLOW + """
           _   __               _                 \s
          | | / /              | |                \s
          | |/ /   __ _  _ __  | |__    __ _  _ __\s
          |    \\  / _` || '_ \\ | '_ \\  / _` || '_ \\
          | |\\  \\| (_| || | | || |_) || (_| || | | |
          \\_| \\_/ \\__,_||_| |_||_.__/  \\__,_||_| |_|
       \s""" + RESET);
    }


    private static void showAuthorMessage() {
        System.out.println("\n=== Software made by Mateus D Barros ===");
        logo();
        System.out.println("109 97 116 101 117 115 68 98 97 114 114 111 115");
    }
}
