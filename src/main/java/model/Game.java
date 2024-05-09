package model;

// Класс-модель игры
public class Game {
    // Объект класса Board, на котором происходит игра
    private Board board;
    // Первый игрок
    private Player player1;
    // Второй игрок
    private Player player2;
    // Текущий игрок
    private Player currentPlayer;

    // Конструктор
    public Game(String name1, String name2) {
        // Создаем новый объект класса Board
        board = new Board();
        // Создаем новые объекты класса HumanPlayer с заданными именами и символами
        player1 = new HumanPlayer(name1, 'X');
        player2 = new HumanPlayer(name2, 'O');
        // Устанавливаем currentPlayer равным player1
        currentPlayer = player1;
    }

    public Game(Player player1, Player player2) {
        // Инициализируем поля
        this.player1 = player1;
        this.player2 = player2;
        // Создаем новый объект класса Board
        board = new Board();
        // Устанавливаем currentPlayer равным player1
        currentPlayer = player1;
    }


    // Метод смены игрока на противоположного
    public void switchPlayer() {
        // Если currentPlayer равен player1, то меняем его на player2, иначе на player1
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    // Метод проверки на победу и ничью
    public boolean isGameOver() {
        // Проверяем, есть ли победа или ничья у текущего игрока, используя его символ
        if (board.isWin(currentPlayer.getSymbol()) || board.isDraw()) {
            // Если да, возвращаем true
            return true;
        }
        // Если нет, возвращаем false
        return false;
    }

    // Метод получения результата игры
    public String showResult() {
        // Проверяем, есть ли победа у текущего игрока, используя его символ
        if (board.isWin(currentPlayer.getSymbol())) {
            // Если есть победа, то возвращаем поздравление с победой, используя его имя и символ
            return "Поздравляем! " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ") выиграл!";
        } else if (board.isDraw()) {
            // Если ничья, то возвращаем сообщение о ничьей
            return "Ничья! Никто не выиграл!";
        } else {
            // Иначе игрок сдался
            return "Игрок " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ") сдался!";
        }
    }
    // Геттеры для полей
    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    // Сеттер для текущего игрока
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}