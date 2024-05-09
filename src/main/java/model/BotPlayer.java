package model;

// Класс HumanPlayer - наследник класса Player, который реализует логику хода игрока-компьютера
public class BotPlayer extends Player {
    // Глубина просчёта ходов
    private int depth;
    private char other = '.';
    // Конструктор
    public BotPlayer(String name, int depth, char symbol) {
        super(name, symbol);
        this.depth = depth;
    }
    // Метод совершения хода игроком-компьютером
    @Override
    public boolean makeTurn(Board board, int row, int col) {
        // Определяем символ другого игрока
        if (symbol == 'X') {
            other = 'O';
        } else {
            other = 'X';
        }
        // Запускаем алгоритм поиска наилучшего хода
        int[] bestMove = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        // Делаем ход
        return board.makeTurn(bestMove[0], bestMove[1], symbol);
    }

    private int[] minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing) {
        // Если дальше не считаем или уже случился конец игры, то выходим из рекурсии
        if (depth == 0 || board.isGameOver(symbol)) {
            return new int[] {-1, -1, evaluateBoard(board)};
        }
        // Вычисляем все доступные ходы
        java.util.List<int[]> allPossibleMoves = board.getValidMoves();
        int bestRow = -1;
        int bestCol = -1;
        int score;
        // Если сейчас ход максимизирующего игрока
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Перебираем всех потомков
            for (int[] move : allPossibleMoves) {
                // Делаем ход
                board.makeTurn(move[0], move[1], symbol);
                // Делаем оценку состояния игры с этим ходом
                score = minimax(board, depth - 1, alpha, beta, false)[2];
                // Отменяем ход
                board.undoMove(move[0], move[1]);
                // Если для текущего потомка ход успешнее предыдущего,
                // то обновляем лучший ход
                if (score > bestScore) {
                    bestScore = score;
                    bestRow = move[0];
                    bestCol = move[1];
                }
                // Обновляем альфа при необходимости
                alpha = Math.max(alpha, score);
                // Проверяем возможность отсечения
                if (alpha >= beta) {
                    break;
                }
            }
            // Возвращаем лучший ход
            return new int[] {bestRow, bestCol, bestScore};
        } else {
            // Если игрок минимизирующий
            int bestScore = Integer.MAX_VALUE;
            // Перебираем всех потомков
            for (int[] move : allPossibleMoves) {
                // Делаем ход
                board.makeTurn(move[0], move[1], other);
                // Делаем оценку состояния игры с этим ходом
                score = minimax(board, depth - 1, alpha, beta, true)[2];
                // Отменяем ход
                board.undoMove(move[0], move[1]);
                // Если для текущего потомка ход успешнее предыдущего,
                // то обновляем лучший ход
                if (score < bestScore) {
                    bestScore = score;
                    bestRow = move[0];
                    bestCol = move[1];
                }
                // Обновляем бета при необходимости
                beta = Math.min(beta, score);
                // Проверяем возможность отсечения
                if (alpha >= beta) {
                    break;
                }
            }
            // Возвращаем лучший ход
            return new int[] {bestRow, bestCol, bestScore};
        }
    }


    private int evaluateBoard(Board board) {
        int score = 0;
        // Пятёрки оцениваем в 100000
        score += board.checkFives(symbol) * 100000;
        score -= board.checkFives(other) * 100000;
        // Открытые четвёрки оцениваем в 10000
        score += board.checkOpenFours(symbol) * 10000;
        score -= board.checkOpenFours(other) * 10000;
        // Закрытые четвёрки и открытые тройки оцениваем в 1000 очков
        score += board.checkClosedFours(symbol, other) * 1000;
        score -= board.checkClosedFours(other, symbol) * 1000;
        score += board.checkOpenThrees(symbol) * 1000;
        score -= board.checkOpenThrees(other) * 1000;
        // Закрытые тройки и открытые двойки оцениваем в 100 очков
        score += board.checkClosedThrees(symbol, other) * 100;
        score -= board.checkClosedThrees(other, symbol) * 100;
        score += board.checkOpenTwos(symbol) * 100;
        score -= board.checkOpenTwos(other) * 100;
        // Закрытые двойки и открытые единички оцениваем в 10 очков
        score += board.checkClosedTwos(symbol, other) * 10;
        score -= board.checkClosedTwos(other, symbol) * 10;
        score += board.checkOpenOnes(symbol) * 10;
        score -= board.checkOpenOnes(other) * 10;
        // Закрытые единички оцениваем в 1 очко
        score += board.checkClosedOnes(symbol, other);
        score -= board.checkClosedOnes(other, symbol);
        // Возвращаем количество набранных очков
        return score;
    }
}