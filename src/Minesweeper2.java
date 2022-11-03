import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.util.TimerTask;

public class Minesweeper2 extends JFrame implements ActionListener, MouseListener {
    JToggleButton[][] board;
    JPanel boardPanel;
    boolean firstClick = true;
    int numMines;
    boolean endGame = false;
    int timePassed;
    Timer timer;
    ImageIcon image1, image2, image3, image4, image5, image6, image7, image8, image_mine, image_flag;
    GraphicsEnvironment ge;
    Font mineFont;
    public String timeField;

    public Minesweeper2() {
        numMines = 10;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {

                mineFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\mine-sweeper.ttf"));
            } catch (FontFormatException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        image1 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\1.png");
        image1 = new ImageIcon(image1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image2 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\2.png");
        image2 = new ImageIcon(image2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image3 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\3.png");
        image3 = new ImageIcon(image3.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image4 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\4.png");
        image4 = new ImageIcon(image4.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image5 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\5.png");
        image5 = new ImageIcon(image5.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image6 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\6.png");
        image6 = new ImageIcon(image6.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image7 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\7.png");
        image7 = new ImageIcon(image7.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image8 = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\8.png");
        image8 = new ImageIcon(image8.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image_mine = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\mine.png");
        image_mine = new ImageIcon(image_mine.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image_flag = new ImageIcon("C:\\Users\\mneemuchwala\\Documents\\GitHub\\Data-Structures\\Minesweeper\\src\\flag.png");
        image_flag = new ImageIcon(image_flag.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        createBoard(10, 10);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void createBoard(int row, int col) {
        if (boardPanel != null)
            this.remove(boardPanel);
        boardPanel = new JPanel();
        board = new JToggleButton[row][col];
        boardPanel.setLayout(new GridLayout(row, col));
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                board[r][c] = new JToggleButton();
                board[r][c].putClientProperty("row", r);
                board[r][c].putClientProperty("col", c);
                board[r][c].putClientProperty("state", 0);
                board[r][c].setBorder(BorderFactory.createBevelBorder(0));
                board[r][c].setFocusPainted(false);
                board[r][c].setFont(mineFont.deriveFont(16f));
                board[r][c].addMouseListener(this);
                boardPanel.add(board[r][c]);
            }
        }
        this.add(boardPanel);
        this.setSize(col * 40, row * 40);
        this.revalidate();
    }

    public void setMinesAndCounts(int row, int col) {
        int count = numMines;
        int dimR = board.length;
        int dimC = board[0].length;
        while (count > 0) {
            int randR = (int) (Math.random() * dimR);
            int randC = (int) (Math.random() * dimC);
            int state = (int) (board[randR][randC].getClientProperty("state"));
            if (state == 0 && Math.abs(row - randR) > 1 || Math.abs(col - randC) > 1) {
                board[randR][randC].putClientProperty("state", 9);
                count--;
            }
        }

        for (int r = 0; r < dimR; r++) {
            for (int c = 0; c < dimC; c++) {
                int mineCount = 0;
                int state = (int) (board[r][c].getClientProperty("state"));
                if (state != 9) {
                    for (int rSmall = r - 1; rSmall <= r + 1; rSmall++) {
                        for (int cSmall = c - 1; cSmall <= c + 1; cSmall++) {
                            try {
                                state = (int) (board[rSmall][cSmall].getClientProperty("state"));
                                if (state == 9 && !(rSmall == r && cSmall == c)) {
                                    mineCount++;
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        }
                    }
                    board[r][c].putClientProperty("state", mineCount);
                }
            }
        }

        /*
         * for (int i = 0, boardLength = board.length; i < boardLength; i++) {
         * JToggleButton[] jToggleButtons = board[i]; for (int c = 0; c < dimC; c++) {
         * int state = (int) (jToggleButtons[c]).getClientProperty("state");
         * jToggleButtons[c].setText("" + state);
         *
         * } }
         */
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void write(int row, int col, int state) {
        switch (state) {
            case 1:
                board[row][col].setIcon(image1);
                break;
            case 2:
                board[row][col].setIcon(image2);
                break;
            case 3:
                board[row][col].setIcon(image3);
                break;
            case 4:
                board[row][col].setIcon(image4);
                break;
            case 5:
                board[row][col].setIcon(image5);
                break;
            case 6:
                board[row][col].setIcon(image6);
                break;
            case 7:
                board[row][col].setIcon(image7);
                break;
            case 8:
                board[row][col].setIcon(image8);
                break;
            case 9:
                board[row][col].setIcon(image_mine);
                break;
        }
        /*
         * if (state != 0) board[row][col].setText("" + state);
         */
    }

    public class UpdateTimer extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            timePassed++;
            timeField = String.valueOf(timePassed);
            System.out.println(timePassed);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (!endGame) {
            int row = (int) (((JToggleButton) e.getComponent()).getClientProperty("row"));
            int col = (int) (((JToggleButton) e.getComponent()).getClientProperty("col"));

            if (e.getButton() == MouseEvent.BUTTON1) {
                /*board[row][col].setBackground(Color.LIGHT_GRAY);*/
                board[row][col].setOpaque(true);
                if (firstClick) {
                    timer = new Timer();
                    timer.schedule(new UpdateTimer(), 0, 1000);
                    setMinesAndCounts(row, col);
                    firstClick = false;
                }
                int state = (int) (((JToggleButton) e.getComponent()).getClientProperty("state"));
                if (state == 9) {
                    for (int r = 0; r < 10; r++) {
                        for (int c = 0; c < 10; c++) {
                            board[r][c].setEnabled(false);
                        }
                    }
                    board[row][col].setIcon(image_mine);
                    endGame = true;
                    timer.cancel();
                    JOptionPane.showMessageDialog(null, "You are a loser!!");
                } else {
                    expand(row, col);
                    checkWin();
                }
            }

            if (e.getButton() == MouseEvent.BUTTON3) {
                if (board[row][col].getIcon() == image_flag) {
                    board[row][col].setIcon(null);
                } else {
                    board[row][col].setIcon(image_flag);
                }
            }
        }
    }

    public void checkWin() {
        int dimR = board.length;
        int dimC = board[0].length;
        int totalSpaces = dimR * dimC;
        int count = 0;
        for (int r = 0; r < dimR; r++) {
            for (int c = 0; c < dimC; c++) {
                int state = (int) board[r][c].getClientProperty("state");
                if (state != 9 && board[r][c].isSelected())
                    count++;
            }
        }
        if (numMines == totalSpaces - count) {
            timer.cancel();
            JOptionPane.showMessageDialog(null, "You Win!!");
        }
    }

    public void expand(int row, int col) {
        if (!board[row][col].isSelected() && !endGame)
            board[row][col].setSelected(true);
        int state = (int) board[row][col].getClientProperty("state");
        if (state > 0) {
            write(row, col, state);
        } else {
            for (int rSmall = row - 1; rSmall <= row + 1; rSmall++) {
                for (int cSmall = col - 1; cSmall <= col + 1; cSmall++) {
                    try {
                        if (!board[rSmall][cSmall].isSelected()) {
                            expand(rSmall, cSmall);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        Minesweeper2 app = new Minesweeper2();
    }
}