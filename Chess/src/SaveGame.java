import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaveGame {
    private final String fileName;
    private List<String> moves;

    public SaveGame(String name) {
        fileName = name;
        moves = new ArrayList<>();
    }

    public void addMove(String move) {
        moves.add(move);
    }

    public void save() {
        LocalDate date = LocalDate.now();
        try {
            File savedir = new File(System.getenv("LOCALAPPDATA") + "\\Chess\\");
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedir + fileName,false))) {
                bw.append("[Event \"").append(fileName).append("\"]\n");
                bw.append("[Site \"").append("localhost").append("\"]\n");
                bw.append("[Date \"").append(date.toString()).append("\"]\n");
                bw.append("[Round \"").append("?").append("\"]\n");
                bw.append("[White \"").append("Player").append("\"]\n");
                bw.append("[Black \"").append("Player").append("\"]\n");
                bw.append("[Result \"").append("*").append("\"]\n\n");
                for (int i = 0; i < moves.size(); i++) {
                    if (i % 2 == 0) {
                        bw.write(((i / 2) + 1) + " ");
                    }
                    bw.write(moves.get(i) + " ");
                }
                bw.write("*");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public List<String> load() {
        List<String> moves = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null){
                if (!line.startsWith("[")) {
                    String[] split = line.split(" ");
                    for (String move : split)
                        if (!move.matches("\\d+\\."))
                            moves.add(move);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Errore nel caricamento: " + e);
        }
        return moves;
    }
}
