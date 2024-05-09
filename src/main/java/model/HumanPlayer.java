package model;

// Класс HumanPlayer - наследник класса Player, который реализует логику хода игрока-человека
public class HumanPlayer extends Player {
    // Конструктор с параметрами
    public HumanPlayer(String name, char symbol) {
        super(name, symbol);
    }
    // Метод совершения хода игроком-человеком
    @Override
    public boolean makeTurn(Board board, int row, int col) {
        return board.makeTurn(row, col, this.symbol);
    }
}