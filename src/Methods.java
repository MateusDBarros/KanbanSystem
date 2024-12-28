import java.util.ArrayList;

public class Methods{


    public static void showAll(ArrayList<Pessoa> pessoas) {
        System.out.printf("%-8s | %-25s | %-15s\n", "Registro", "Nome", "Status");
        System.out.println("----------------------------------------------");
        for (Pessoa pessoa : pessoas)
            System.out.printf("%-8s | %-25s | %-15s\n", pessoa.getId(), pessoa.getName(), pessoa.getLevel().getDescription());
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

    public static int confirm(char x) {
        if (x == 'y'){
            return 1;
        } else if (x == 'n') {
            return 0;
        }
        else return -1;
    }


}
