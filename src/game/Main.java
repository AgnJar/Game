package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Main extends JFrame {

    Game game = new Game();
    JButton[] buttons = new JButton[Game.SIZE];

    public Main(){
        setLayout(new GridLayout(Game.DIM, Game.DIM));
        for (int i = 0; i < Game.SIZE; i++){
           final JButton button = createButton();
           buttons[i] = button;
           final int idx = i;
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (button.getText().isEmpty()) { //--- Patiktinam ar paspaustas mygtukas yra laisvas
                        button.setText(Character.toString(game.turn));
                        game.move(idx);
                        if (!game.isGameEnd()) { //--- Jei žaidimas nebaigtas, kompiuteris daro savo ėjimą
                            int best = game.bestMove();
                            buttons[best].setText(Character.toString(game.turn));
                            game.move(best);
                        }
                        if (game.isGameEnd()) { //--- Jei žaidimas baigtas, parodomas pranešimas, kas laimėjo
                            String message = game.isWinFor('x') ? "You won!" :
                                    game.isWinFor('o') ? "Computer Won!" : "Draw";
                            JOptionPane.showMessageDialog(null, message);
                        }

                    }
                }
            });
        }
        pack();
        setVisible(true);
    }

    private JButton createButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(100,100));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFont(new Font(null, Font.PLAIN, 50));
        add(button);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
//--- Paleidžiam žaidimą
            public void run() {
                new Main();
            }
        });
    }

}
