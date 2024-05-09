package delegate;

import model.BotPlayer;
import model.Game;
import model.HumanPlayer;
import model.Player;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class RenjuGUI {
    private Game game;
    private JFrame gameFrame;
    private JPanel btPanel;
    private JButton[][] board;
    private JPanel ctrlPanel;
    private JLabel name1Label;
    private JLabel name2Label;
    private JButton giveUpBt;
    private JButton restartBt;
    private StartFrame startFrame;
    private int size = 15;

    private Clip ambientClip;
    private Clip endGameClip;
    private Clip hitBtClip;

    private Font mainFont = new Font("Comic Sans MS", Font.BOLD, 22);
    private Color greenColor = new Color(50, 205, 50);
    private Color redColor = new Color(200, 34, 34);


    public RenjuGUI() {
        gameFrame = new JFrame();
        gameFrame.setSize(1250,950);
        gameFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btPanel = new JPanel();
        btPanel.setPreferredSize(new Dimension(900,900));
        btPanel.setLayout(new GridLayout(size, size));
        initBoard();
        initClips();

        ctrlPanel = new JPanel();
        ctrlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        ctrlPanel.setPreferredSize(new Dimension(300,900));

        name1Label = new JLabel("X: ");
        name1Label.setPreferredSize(new Dimension(250,100));
        name1Label.setFont(mainFont);
        name1Label.setOpaque(true);
        name1Label.setBorder(BorderFactory.createLineBorder(Color.black,5,false));
        ctrlPanel.add(name1Label);

        name2Label = new JLabel("O: ");
        name2Label.setPreferredSize(new Dimension(250,100));
        name2Label.setFont(mainFont);
        name2Label.setOpaque(true);
        name2Label.setBorder(BorderFactory.createLineBorder(Color.black,5,false));
        ctrlPanel.add(name2Label);

        giveUpBt = new JButton("Сдаться");
        giveUpBt.setPreferredSize(new Dimension(250, 100));
        giveUpBt.setFont(mainFont);
        giveUpBt.setFocusable(false);
        giveUpBt.setBackground(Color.white);
        giveUpBt.addActionListener(e -> {
            if (game.getCurrentPlayer().getSymbol() == 'X') {
                name1Label.setBackground(redColor);
            } else {
                name2Label.setBackground(redColor);
            }
            endGame(game.showResult());
        });
        ctrlPanel.add(giveUpBt);

        restartBt = new JButton("Начать новую игру");
        restartBt.setPreferredSize(new Dimension(250, 100));
        restartBt.setFont(mainFont);
        restartBt.setFocusable(false);
        restartBt.setBackground(Color.white);
        restartBt.addActionListener(e -> restartGame());
        ctrlPanel.add(restartBt);

        gameFrame.add(btPanel);
        gameFrame.add(ctrlPanel);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gameFrame.setEnabled(false);
        startFrame = new StartFrame();
        if (ambientClip != null) {
            ambientClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void initClips() {
        try {
            AudioInputStream ambientStream = AudioSystem
                    .getAudioInputStream(new File("ambient_music.wav"));
            AudioInputStream endGameStream = AudioSystem
                    .getAudioInputStream(new File("end_game.wav"));
            AudioInputStream hitBtStream = AudioSystem
                    .getAudioInputStream(new File("hit_bt.wav"));

            ambientClip = AudioSystem.getClip();
            endGameClip = AudioSystem.getClip();
            hitBtClip = AudioSystem.getClip();

            ambientClip.open(ambientStream);
            endGameClip.open(endGameStream);
            hitBtClip.open(hitBtStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            ambientClip = null;
            endGameClip = null;
            hitBtClip = null;
        }
    }

    private void forEachBoardBt(Consumer<JButton> action) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                action.accept(board[i][j]);
            }
        }
    }

    private void initBoard() {
        board = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton curBt = board[i][j] = new JButton("");
                curBt.setFocusable(false);
                curBt.setBackground(Color.white);
                curBt.setFont(mainFont);
                curBt.addActionListener(new MakeTurnListener());
                btPanel.add(curBt);
            }
        }
    }

    private class MakeTurnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (e.getSource() == board[i][j]) {
                        // Ждём ход компьютера
                        if (calcTurn != null) {
                            try {
                                calcTurn.join();
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        // Делаем ход текущим игроком
                        boolean result = game.getCurrentPlayer().makeTurn(game.getBoard(), i, j);
                        if (result) {
                            if (hitBtClip != null) {
                                hitBtClip.stop();
                                hitBtClip.setFramePosition(0);
                                hitBtClip.start();
                            }
                            printBoard();
                            checkTurn();
                        }
                    }
                }
            }
        }
    }

    private void endGame(String result) {
        if (endGameClip != null) {
            endGameClip.stop();
            endGameClip.setFramePosition(0);
            endGameClip.start();
        }
        for (Point p : game.getBoard().getWinCombination()) {
            int i = p.x;
            int j = p.y;
            board[i][j].setBackground(greenColor);
        }
        giveUpBt.setEnabled(false);
        forEachBoardBt(bt -> {
            bt.setEnabled(false);
        });
        JOptionPane.showMessageDialog(gameFrame, result,
                "Конец игры!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGame() {
        // Сбрасываем значения новых полей
        botGame = false;
        botThread = null;
        calcTurn = null;
        forEachBoardBt(bt -> {
            bt.setText("");
            bt.setBackground(Color.white);
            bt.setEnabled(true);
        });
        giveUpBt.setEnabled(true);
        name1Label.setBackground(null);
        name1Label.setText("X: ");
        name2Label.setBackground(null);
        name2Label.setText("O: ");
        gameFrame.setEnabled(false);

        startFrame = new StartFrame();
    }

    private void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].setText(String.valueOf(game.getBoard().getBoard()[i][j]));
            }
        }
    }

    private boolean botGame = false;
    // Поток для запуска игры двух ботов
    private Thread botThread = null;
    // Поток для расчёта хода компьютера
    private Thread calcTurn = null;

    private class StartFrame extends JFrame {
        private JLabel welcomeLabel;
        private JLabel player1Label;
        private JLabel player2Label;
        // Добавляем выпадающие списки для выбора типа игрока
        private JComboBox<String> player1Type;
        private JComboBox<String> player2Type;
        private JTextField player1Name;
        private JTextField player2Name;
        // Добавляем выпадающие списки для выбора уровня сложности
        private JComboBox<String> player1Lvl;
        private JComboBox<String> player2Lvl;
        private JButton startBt;

        public StartFrame() {
            this.setTitle("Добро пожаловать!");
            this.setSize(800, 600);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
            this.setResizable(false);
            this.setAlwaysOnTop(true);

            welcomeLabel = new JLabel();
            welcomeLabel.setFont(mainFont);
            welcomeLabel.setText("<html><div style='text-align: center;'>Добро пожаловать в игру Рэндзю!" +
                    "<br>Цель игры - собрать пять своих символов в ряд" +
                    "<br> по горизонтали, вертикали или диагонали." +
                    "<br>Игроки ходят по очереди, ставя крестики и " +
                    "<br>нолики на свободные клетки поля." +
                    "<br>Игра заканчивается ничьей, когда поле полностью заполнено," +
                    "<br>а выигрышная комбинация так и не была получена." +
                    "<br>" +
                    "<br>Желаем вам удачи и приятной игры!<br><br></div></html>");

            player1Label = new JLabel("Игрок за 'X': ");
            player1Label.setFont(mainFont);
            player1Label.setPreferredSize(new Dimension(170,50));

            // Создаём массивы вариантов для выпадающего списка
            String[] playerTypes = {"Человек", "Компьютер"};
            String[] lvls = {"Уровень сложности 1", "Уровень сложности 2", "Уровень сложности 3"};
            // Инициализируем выпадающий список
            player1Type = new JComboBox<>(playerTypes);
            player1Type.setFont(mainFont);
            player1Type.setPreferredSize(new Dimension(200,50));
            // При выборе элемента списка переключаем компоненты
            player1Type.addItemListener(e -> {
                String type = (String) player1Type.getSelectedItem();
                if (type.equals("Человек")) {
                    player1Name.setVisible(true);
                    player1Lvl.setVisible(false);
                } else {
                    player1Name.setVisible(false);
                    player1Lvl.setVisible(true);
                }
            });

            player1Name = new JTextField();
            player1Name.setFont(mainFont);
            player1Name.setPreferredSize(new Dimension(350, 50));

            // Инициализируем выпадающий список
            player1Lvl = new JComboBox<>(lvls);
            player1Lvl.setFont(mainFont);
            player1Lvl.setPreferredSize(new Dimension(350,50));
            player1Lvl.setVisible(false);

            player2Label = new JLabel("Игрок за 'O': ");
            player2Label.setFont(mainFont);
            player2Label.setPreferredSize(new Dimension(170,50));

            // Инициализируем выпадающий список
            player2Type = new JComboBox<>(playerTypes);
            player2Type.setFont(mainFont);
            player2Type.setPreferredSize(new Dimension(200,50));
            // При выборе элемента списка переключаем компоненты
            player2Type.addItemListener(e -> {
                String type = (String) player2Type.getSelectedItem();
                if (type.equals("Человек")) {
                    player2Name.setVisible(true);
                    player2Lvl.setVisible(false);
                } else {
                    player2Name.setVisible(false);
                    player2Lvl.setVisible(true);
                }
            });

            player2Name = new JTextField();
            player2Name.setFont(mainFont);
            player2Name.setPreferredSize(new Dimension(350, 50));

            // Инициализируем выпадающий список
            player2Lvl = new JComboBox<>(lvls);
            player2Lvl.setFont(mainFont);
            player2Lvl.setPreferredSize(new Dimension(350,50));
            player2Lvl.setVisible(false);

            startBt = new JButton("Начать игру");
            startBt.setPreferredSize(new Dimension(250, 50));
            startBt.setFont(mainFont);
            startBt.setFocusable(false);
            startBt.setBackground(greenColor);
            startBt.addActionListener(e -> {
                // Проверяем имена в случае выбора "Человек"
                if (player1Name.isVisible() && player1Name.getText().isEmpty()
                        || player2Name.isVisible() && player2Name.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Имя игрока не может быть пустым!",
                            "Ошибка!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String name1;
                    String name2;
                    Player player1;
                    Player player2;
                    // Инициализируем игроков в зависимости от выбора
                    if (player1Type.getSelectedItem().equals("Человек")) {
                        name1 = player1Name.getText();
                        player1 = new HumanPlayer(name1, 'X');
                    } else {
                        int lvl = player1Lvl.getSelectedIndex() + 1;
                        name1 = "Компьютер ур. " + lvl;
                        player1 = new BotPlayer(name1, lvl, 'X');
                    }
                    if (player2Type.getSelectedItem().equals("Человек")) {
                        name2 = player2Name.getText();
                        player2 = new HumanPlayer(name2, 'O');
                    } else {
                        int lvl = player2Lvl.getSelectedIndex() + 1;
                        name2 = "Компьютер ур. " + lvl;
                        player2 = new BotPlayer(name2, lvl, 'O');
                    }
                    // Создаём новую игру, используя конструктор от игроков
                    game = new Game(player1, player2);
                    game.getBoard().setEmpty(' ');
                    printBoard();
                    name1Label.setText("X: " + name1);
                    name1Label.setBackground(greenColor);
                    name2Label.setText("O: " + name2);
                    name2Label.setBackground(null);
                    this.dispose();
                    gameFrame.setEnabled(true);
                    gameFrame.setVisible(true);
                    // Если первый игрок Компьютер, то он сразу делает ход
                    if (player1Type.getSelectedItem().equals("Компьютер")
                            && player2Type.getSelectedItem().equals("Человек")) {
                        game.getCurrentPlayer().makeTurn(game.getBoard(), 0, 0);
                        printBoard();
                        checkTurn();
                    }
                    // Если были выбраны 2 компьютера, то отключаем слушатели и запускаем игру ботов
                    if (player1Type.getSelectedItem().equals("Компьютер")
                            && player2Type.getSelectedItem().equals("Компьютер")) {
                        forEachBoardBt(bt -> {
                            ActionListener[] listeners = bt.getActionListeners();
                            for (ActionListener l : listeners) {
                                bt.removeActionListener(l);
                            }
                        });
                        // Инициализируем отдельный поток
                        botThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startBotGame();
                            }
                        });
                        // Запускаем игру ботов в отдельном потоке
                        botThread.start();
                    } else {
                        // Иначе включаем слушатели
                        forEachBoardBt(bt -> {
                            if (bt.getActionListeners().length == 0) {
                                bt.addActionListener(new MakeTurnListener());
                            }
                        });
                    }
                }
            });

            // Добавляем ВСЕ компоненты на фрейм
            this.add(welcomeLabel);
            this.add(player1Label);
            this.add(player1Type);
            this.add(player1Name);
            this.add(player1Lvl);
            this.add(player2Label);
            this.add(player2Type);
            this.add(player2Name);
            this.add(player2Lvl);
            this.add(startBt);
            this.setLocationRelativeTo(gameFrame);
            this.setVisible(true);
        }
    }

    private void checkTurn() {
        if (game.isGameOver()) {
            endGame(game.showResult());
            return;
        }
        game.switchPlayer();
        Color temp = name1Label.getBackground();
        name1Label.setBackground(name2Label.getBackground());
        name2Label.setBackground(temp);
        // Если нет игры двух ботов и теперь ход компьютера,
        if (!botGame && game.getCurrentPlayer() instanceof BotPlayer) {
            // то совершаем ход в другом потоке
            calcTurn = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Пока в целях тестирования имитируем сложные расчёты
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    game.getCurrentPlayer().makeTurn(game.getBoard(), 0, 0);
                    if (hitBtClip != null) {
                        hitBtClip.stop();
                        hitBtClip.setFramePosition(0);
                        hitBtClip.start();
                    }
                    printBoard();
                    checkTurn();
                }
            });
            calcTurn.start();
        }
    }

    // Метод запуска игры двух ботов
    public void startBotGame() {
        botGame = true;
        while (!game.isGameOver()) {
            game.getCurrentPlayer().makeTurn(game.getBoard(), 0,0);
            printBoard();
            checkTurn();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
