package model;

// Класс Player - абстрактный класс, который определяет общие свойства и поведение игрока
public abstract class Player {
    // Строка, которая хранит имя игрока
    protected String name;
    // Символ, которым игрок ходит
    protected char symbol;

    // Конструктор с параметрами
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    // Метод, который определяет логику хода игрока в зависимости от типа игрока (человек или компьютер)
    // Возвращает false, если игрок хочет сдаться досрочно
    public abstract boolean makeTurn(Board board, int row, int col);

    // Метод получения имени игрока
    public String getName() {
        return name;
    }

    // Метод получения символа игрока
    public char getSymbol() {
        return symbol;
    }
}