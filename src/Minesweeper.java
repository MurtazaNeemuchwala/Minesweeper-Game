import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Minesweeper extends JFrame implements ActionListener, MouseListener {
    JToggleButton[][] board;
    JPanel boardPanel;
    boolean firstClick = true;
    int numMines;
    ImageIcon image1, image2, image3, image4, image5, image6, image7, image8, image_mine, image_flag;
    GraphicsEnvironment ge;
    JTextField statLabel;
    Font mineFont;
    boolean gameOver = false;
    Timer timer;
    int timePassed = 0;

    class UpdateTimer extends TimerTask {
        @Override
        public void run() {
            if (!gameOver) {
                timePassed++;
                statLabel.setText("Time: " + timePassed);
                System.out.println(timePassed);
            }
        }
    }

    public Minesweeper() {
        numMines = 10;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {

                mineFont = Font.createFont(Font.TRUETYPE_FONT, new File(
                        "src\\mine-sweeper.ttf"));
            } catch (FontFormatException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        image1 = new ImageIcon("src\\1.png");
        image1 = new ImageIcon(image1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image2 = new ImageIcon("src\\2.png");
        image2 = new ImageIcon(image2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image3 = new ImageIcon("src\\3.png");
        image3 = new ImageIcon(image3.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image4 = new ImageIcon("src\\4.png");
        image4 = new ImageIcon(image4.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image5 = new ImageIcon("src\\5.png");
        image5 = new ImageIcon(image5.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image6 = new ImageIcon("src\\6.png");
        image6 = new ImageIcon(image6.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image7 = new ImageIcon("src\\7.png");
        image7 = new ImageIcon(image7.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image8 = new ImageIcon("src\\8.png");
        image8 = new ImageIcon(image8.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        image_mine = new ImageIcon("src\\mine.png");
        image_mine = new ImageIcon(image_mine.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        image_flag = new ImageIcon("src\\flag.png");
        image_flag = new ImageIcon(image_flag.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

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

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            timer.cancel();
            timePassed = 0;
            createBoard(9, 9);
            numMines = 10;
            gameOver = false;
        });

        JMenuBar menubar = new JMenuBar();
        statLabel = new JTextField("Time (in seconds): " + timePassed, 16);
        statLabel.setEnabled(false);
        menubar.add(statLabel);
        menubar.add(resetButton);
        JMenu menu1 = new JMenu("Difficulty");
        menubar.add(menu1);

        JMenuItem easy = new JMenuItem("Easy");
        menu1.add(easy);
        easy.addActionListener(e -> {
            numMines = 10;
            createBoard(9, 9);
        });

        JMenuItem medium = new JMenuItem("Medium");
        menu1.add(medium);
        medium.addActionListener(e -> {
            numMines = 40;
            createBoard(16, 16);
        });

        JMenuItem hard = new JMenuItem("Hard");
        menu1.add(hard);
        hard.addActionListener(e -> {
            numMines = 99;
            createBoard(16, 40);
        });

        this.add(boardPanel);
        this.setJMenuBar(menubar);
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
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }
                    }
                    board[r][c].putClientProperty("state", mineCount);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void write(int row, int col, int state) {
        switch (state) {
            case 1 -> board[row][col].setIcon(image1);
            case 2 -> board[row][col].setIcon(image2);
            case 3 -> board[row][col].setIcon(image3);
            case 4 -> board[row][col].setIcon(image4);
            case 5 -> board[row][col].setIcon(image5);
            case 6 -> board[row][col].setIcon(image6);
            case 7 -> board[row][col].setIcon(image7);
            case 8 -> board[row][col].setIcon(image8);
            case 9 -> board[row][col].setIcon(image_mine);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            int row = (int) (((JToggleButton) e.getComponent()).getClientProperty("row"));
            int col = (int) (((JToggleButton) e.getComponent()).getClientProperty("col"));

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (firstClick) {
                    timer = new Timer();
                    timer.schedule(new UpdateTimer(), 0, 1000);
                    setMinesAndCounts(row, col);
                    firstClick = false;
                }
                int state = (int) (((JToggleButton) e.getComponent()).getClientProperty("state"));
                if (state == 9) {
                    JOptionPane.showMessageDialog(null, "You are a loser!!");
                    board[row][col].setIcon(image_mine);
                    gameOver = true;
                    timer.cancel();

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
        for (JToggleButton[] jToggleButtons : board) {
            for (int c = 0; c < dimC; c++) {
                int state = (int) jToggleButtons[c].getClientProperty("state");
                if (state != 9 && jToggleButtons[c].isSelected())
                    count++;
            }
        }
        if (numMines == totalSpaces - count) {
            JOptionPane.showMessageDialog(null, "You Win!!");
            timer.cancel();
        }

    }

    public void expand(int row, int col) {
        if (!board[row][col].isSelected() && !gameOver)
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
                    } catch (ArrayIndexOutOfBoundsException ignored) {
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
        new Minesweeper();
    }
}