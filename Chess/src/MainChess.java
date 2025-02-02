import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainChess extends JFrame {

    private JButton newgame;
    private JButton loadgame;
    private SaveGame saveGame;

    public MainChess() {
        saveGame = new SaveGame("partita.pgn");
        setTitle("Scacchi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);

        showMainMenu();
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainChess();
    }

    private void showMainMenu(){
        JPanel mainmenu = new JPanel();
        mainmenu.setLayout(new BoxLayout(mainmenu, BoxLayout.Y_AXIS));

        newgame = new JButton("Nuova Partita");
        loadgame = new JButton("Carica Partita");

        newgame.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadgame.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainmenu.add(newgame);
        mainmenu.add(Box.createVerticalStrut(20));
        mainmenu.add(loadgame);

        newgame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showNewGame();
            }
        });

        loadgame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                showLoadGame();
                saveGame.load();
            }
        });

        setContentPane(mainmenu);
        revalidate();
        repaint();
    }

    private void showNewGame(){
        //Classe Game per creare la scacchiera
        JPanel newgameP = new Game();

        JMenuBar menuBar = new JMenuBar();
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JMenu filemenu = new JMenu("Partita");

        JMenuItem save = new JMenuItem("Salva ed esci");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveGame.save();
                showMainMenu();
            }
        });

        JMenuItem restartGame = new JMenuItem("Ricomincia Partita");
        restartGame.addActionListener(e -> showNewGame());

        filemenu.add(save);
        filemenu.add(restartGame);

        menuBar.add(filemenu);
        setJMenuBar(menuBar);

        setContentPane(newgameP);
        revalidate();
        repaint();
    }

    public void showLoadGame(){
        JPanel loadGameP = new JPanel();
        loadGameP.setLayout(new BorderLayout());

        JButton back = new JButton("Salva ed esci");

        back.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               showMainMenu();
           }
        });

        loadGameP.add(back,BorderLayout.SOUTH);

        setContentPane(loadGameP);
        revalidate();
        repaint();
    }
}

