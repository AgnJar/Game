package game;

import javax.swing.*;
import java.util.*;

public class Game {
    public static final int DIM = 3; //-- Žaidimo lentos kraštinės dydis
    public static final int SIZE = DIM*DIM; //-- Žaidimo lentos langelių skaičius
    public char turn; // -- Kieno ėjimas (x - žmogus, o - kompiuteris)
    public char[] board; // -- žaidimo lenta
    private Map<Integer, Integer> cache = new HashMap<Integer, Integer>();

    public Game(){ //--- Sukuriam žaidimo lentą
        turn = 'x';
        board = new char[SIZE];
        for (int i=0;i<SIZE;i++){
            board [i] = ' ';
        }
    }
    public Game(String string, char c){
        this.board = string.toCharArray();
        this.turn = turn;
    }

    public String toString(){
        return new String(board);
    }
    public Game move(int idx){ //-- Jei ėjimas galimas, padarom jį ir perleidžiam ėjimą kitam žaidėjui
        if (!possibleMoves().contains(idx)) {
            JOptionPane.showMessageDialog(null, "KLAIDA");
            return this;
        }
        board[idx] = turn;
        turn = turn == 'x' ? 'o' : 'x';
        return this;

    }
    public Game unmove(int idx){ //-- Nuimam padarytą ėjimą
        board[idx] = ' ';
        turn = turn == 'x' ? 'o' : 'x';
        return this;
    }
    public List<Integer> possibleMoves(){ //-- Gaunam visus galimus ėjimus (neužimti laukai)
        List<Integer> list = new ArrayList<>();
        for (int i =0; i < board.length; i++){
            if (board[i] == ' ')
                list.add(i);
        }
        return list;
    }
    public boolean isWinFor(char turn){ //-- Patinkrinam ar yra laimėtojas
        boolean isWin = false;
        for (int i = 0; i < SIZE; i += DIM){ //-- Patikrinam horizontalias eilutes
            isWin = isWin || lineMatch(turn, i , i + DIM, 1);
        }
        for (int i = 0; i < DIM; i ++){ //-- Patikrinam vertikalias eilutes
            isWin = isWin || lineMatch(turn, i ,SIZE, DIM);
        }
        isWin = isWin || lineMatch(turn, 0, SIZE,DIM+1); //-- Patikrinam istrižainę (nuo viršaus kairės)
        isWin = isWin || lineMatch(turn, DIM-1, SIZE-1,DIM-1); //-- Patikrinam istrižainę (nuo viršaus dešinės)
        return isWin;
    }
    private boolean lineMatch(char turn, int start, int end, int step){ //-- Patikrinam ar yra trys vienodi simboliai  eilutėje
        for (int i = start; i < end; i += step){
            if (board[i] != turn)
                return false;
        }
        return true;
    }
    public int blanks(){ //-- Suskaičiuojam laisvų vietų skaičių
        int total = 0;
        for (int i = 0; i <SIZE; i++) {
            if (board[i] == ' ')
                total++;
        }
        return total;
    }
    public int code(){ //-- Suteikiame galimam ėjimui reikšmę
        int value = 0;
        for (int i = 0; i < SIZE; i++){
            value = value * 3;
            if (board[i] == 'x')
                value+=1;
            else if (board[i] == 'o')
                value -=1;
        }
        return value;
    }
    public int minimax(){ //-- Pasirenkame minimaliausią grėsmę sukeliantį ėjimą arba
        Integer key = code();
        Integer value = cache.get(key);
        if (value != null) return value;
        if (isWinFor('x')) return blanks()+10;
        if (isWinFor('o')) return -blanks()-10;
        if (blanks() == 0) return 0;
        List<Integer> list = new ArrayList<>();
        for (Integer idx : possibleMoves()) {
            list.add(move(idx).minimax());
            unmove(idx);
        }
        value = turn == 'x' ? Collections.max(list) : Collections.min(list); //-- Kompiuteris ('o') renkasi mažiausią grėsmę keliantį variantą
        cache.put(key, value);
        return value;
    }
    public int bestMove(){ //-- Palyginami galimi likę ėjimai
        Comparator<Integer> cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                int a = move(first).minimax();
                unmove(first);
                int b = move(second).minimax();
                unmove(second);
                return a - b;
            }
        };
        List<Integer> list = possibleMoves();
        return turn == 'x' ? Collections.max(list,cmp) : Collections.min(list, cmp); //-- Kompiuteris pasirenka iš galimų variantų mažiausią gręsmę keliantį ėjimą
    }
    public boolean isGameEnd(){ //-- Tikrinam ar žaidimas baigtas
        return isWinFor('x') || isWinFor('o') || blanks() == 0;
    }

}
