import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class SaveGame {
    private String fileName;
    private StringBuilder content;
    private String result = "*";

    public SaveGame(String name) {
        fileName = name;
        content = new StringBuilder();
        content.append("[Event \"").append(name).append("\"]\n");
        content.append("[Site \"").append("localhost").append("\"]\n");
        content.append("[Date \"").append(LocalDate.now()).append("\"]\n");
        content.append("[Round \"").append("?").append("\"]\n");
        content.append("[White \"").append("Player").append("\"]\n");
        content.append("[Black \"").append("Player").append("\"]\n");
        content.append("[Result \"").append(result).append("\"]\n\n");
        content.append("1. e4 e5 2. Nf3 Nf6 3. Bc4 Nxe4 4. Bxe5+ Kxf7 5. Nxe5+ Kf6 ").append(result);
    }

    public void save(){
        String appdataPath = System.getenv("LOCALAPPDATA");
        if(appdataPath == null){
            System.err.println("Impossibile trovare cartella");
            return;
        }

        String foldername = "Chess";
        File dir = new File(appdataPath + File.separator + foldername);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                System.err.println("Impossibile trovare la cartella di salvataggio");
                return;
            }
        }

        String filePath = appdataPath + File.separator + foldername + File.separator + fileName;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content.toString());
            System.out.println("Partita salvata in " + filePath);
        }
        catch (IOException e){
            System.err.println("Errore nel salvataggio" + e.getMessage());
        }

    }
}
