import java.util.ArrayList;

public class Methods{


    // Sequências ANSI
    static final String RESET = "\u001B[0m";
    static final String BLUE = "\u001B[34m";

    public static void showAll(ArrayList<Pessoa> pessoas) {
        if (pessoas == null || pessoas.isEmpty()) {
            System.out.println("Nenhum registro encontrado");
            return;
        }
        System.out.println(BLUE + "+----------+-------------------------+----------------+" + RESET);
        System.out.println(BLUE + "| Registro | Nome                    | Status         |" + RESET);
        System.out.println(BLUE + "+----------+-------------------------+----------------+" + RESET);
        for (Pessoa pessoa : pessoas) {
            System.out.printf("| %-8s | %-23s | %-14s |\n", pessoa.getId(), pessoa.getName(), pessoa.getLevel().getDescription());
        }
        System.out.println(BLUE + "+----------+-------------------------+----------------+" + RESET);
    }

    public static int search(ArrayList<Pessoa> pessoas, String name, int l, int r) {

        if (pessoas == null || pessoas.isEmpty() || l < 0 || r >= pessoas.size()) {
            return -1;
        }

        if (l > r) {
            return -1;
        }

        int mid = l + (r - l) / 2;
        if (pessoas.get(mid).getName().equals(name)) return mid;

        if (pessoas.get(mid).getName().compareTo(name) > 0)
            return search(pessoas, name, l, mid - 1);
        else
            return search(pessoas, name, mid + 1, r);
    }

    public static int confirm(String input) {
        if (input.equalsIgnoreCase("y")){
            return 1;
        } else if (input.equalsIgnoreCase("n")) {
            return 0;
        }
        else return -1;
    }


}
