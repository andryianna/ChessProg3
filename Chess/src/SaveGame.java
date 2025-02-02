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
            if (!(new File(savedir, fileName).exists())) {
                new File(savedir, fileName).createNewFile();
            }
            System.out.println(new File(savedir, fileName).exists());
            new File(savedir, fileName).setReadable(true,false);
            new File(savedir, fileName).setWritable(true,false);
            System.out.println("Saved to " + savedir + "\\"+fileName);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedir + fileName))) {
                bw.append("[Event \"").append(fileName).append("\"]\n");
                bw.append("[Site \"").append("localhost").append("\"]\n");
                bw.append("[Date \"").append(date.toString()).append("\"]\n");
                bw.append("[Round \"").append("?").append("\"]\n");
                bw.append("[White \"").append("Player").append("\"]\n");
                bw.append("[Black \"").append("Player").append("\"]\n");
                bw.append("[Result \"").append("*").append("\"]\n\n");
                System.out.println(moves.size());
                for (int i = 0; i < moves.size(); i++) {
                    System.out.println("Sono qui");
                    if (i % 2 == 0) {
                        bw.append(((i / 2) + 1) + ". ");
                    }
                    bw.append(moves.get(i) + " ");
                }
                bw.write("*");
                System.out.println(new File(fileName).canWrite());
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
