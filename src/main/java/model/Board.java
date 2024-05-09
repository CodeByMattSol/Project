package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

// Класс Board - отвечает за логику работы игрового поля, а также за проверку наличия победной комбинации или ничьей
public class Board {
    // Двумерный массив символов, который хранит состояние игрового поля
    private char[][] board;
    // Размер поля
    private final int size;
    // Символ, который обозначает пустую клетку поля
    private char empty = '.';
    // Длина победной комбинации
    private final int winLength;
    // Счётчик свободных полей
    private int emptyCounter;
    // Массив для хранения точек с победной комбинацией
    private ArrayList<Point> winCombination = new ArrayList<>();

    // Конструктор по умолчанию
    public Board() {
        size = 15;
        winLength = 5;
        // Вызываем метод инициализации
        initBoard();
    }
    // Конструктор с параметрами
    public Board(int size, int winLength) {
        this.size = size;
        this.winLength = winLength;
        // Вызываем метод инициализации
        initBoard();
    }
    // Метод инициализации игрового поля
    public void initBoard() {
        // Создаем новый двумерный массив символов размером size на size
        board = new char[size][size];
        // Заполняем массив пустыми символами
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = empty;
            }
        }
        // Обновляем значение счётчика пустых клеток
        emptyCounter = size * size;
    }
    // Метод для смены символа, обозначающего пустую клетку
    public void setEmpty(char empty) {
        this.empty = empty;
        // Не забываем сбросить всё поле
        initBoard();
    }
    // Метод проверки корректности координат
    public boolean isTurnValid(int row, int col) {
        // Возвращаем false, если row и col выходят за границы массива
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        // Возвращаем false, если клетка с координатами row и col не пуста
        if (board[row][col] != empty) {
            return false;
        }
        // В других случаях возвращаем true
        return true;
    }
    // Если координаты корректны, то делаем ход - записываем символ в поле
    public boolean makeTurn(int row, int col, char player) {
        // Проверяем, что ход возможен
        if (isTurnValid(row, col)) {
            // Записываем символ в клетку
            board[row][col] = player;
            // Уменьшаем счётчик пустых клеток
            emptyCounter--;
            // Возвращаем true
            return true;
        }
        // Если ход невозможен, возвращаем false
        return false;
    }

    // Метод проверки наличия победной комбинации для игрока
    public boolean isWin(char player) {
        // Проверяем все возможные направления победной комбинации
        // Горизонталь
        for (int i = 0; i < size; i++) {
            // Считаем количество подряд идущих символов player в строке i
            int count = 0;
            winCombination.clear();
            for (int j = 0; j < size; j++) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
        }
        // Вертикаль
        for (int j = 0; j < size; j++) {
            // Считаем количество подряд идущих символов player в столбце j
            int count = 0;
            winCombination.clear();
            for (int i = 0; i < size; i++) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
        }
        // Главная диагональ
        for (int k = 0; k <= size - winLength; k++) {
            // Считаем количество подряд идущих символов player на главной диагонали, начиная с клетки (k, 0)
            int count = 0;
            winCombination.clear();
            for (int i = k, j = 0; i < size && j < size; i++, j++) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
            // Считаем количество подряд идущих символов player на главной диагонали, начиная с клетки (0, k)
            count = 0;
            winCombination.clear();
            for (int i = 0, j = k; i < size && j < size; i++, j++) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
        }
        // Побочная диагональ
        for (int k = 0; k <= size - winLength; k++) {
            // Считаем количество подряд идущих символов player на побочной диагонали, начиная с клетки (k, size - 1)
            int count = 0;
            winCombination.clear();
            for (int i = k, j = size - 1; i < size && j >= 0; i++, j--) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
            // Считаем количество подряд идущих символов player на побочной диагонали, начиная с клетки (0, size - 1 - k)
            count = 0;
            winCombination.clear();
            for (int i = 0, j = size - 1 - k; i < size && j >= 0; i++, j--) {
                if (board[i][j] == player) {
                    count++;
                    winCombination.add(new Point(i,j));
                } else {
                    count = 0;
                    winCombination.clear();
                }
                // Если количество достигло winLength, возвращаем true
                if (count == winLength) {
                    return true;
                }
            }
        }
        // Если ни одна из проверок не вернула true, возвращаем false
        winCombination.clear();
        return false;
    }

    public int checkOpenOnes(char player) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида _X_, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 3; col++) {
                if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == empty) {
                    count++;
                    col++;
                }
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size - 3; row++) {
                if (board[row][col] == empty
                        && board[row + 1][col] == player
                        && board[row + 2][col] == empty) {
                    count++;
                    row++;
                }
            }
        }
        // Проверяем диагонали на наличие комбинации
        for (int row = 0; row < size - 3; row++) {
            for (int col = 0; col < size - 3; col++) {
                if (board[row][col] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == empty) {
                    count++;
                } else if (board[row][col + 2] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col] == empty) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки закрытых с одной стороны единичек
    public int checkClosedOnes(char player, char other) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида OX_ или _XO, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            // Не забываем отдельно проверить у левого края поля
            if (board[row][0] == player && board[row][1] == empty) count++;
            for (int col = 0; col < size - 3; col++) {
                if (board[row][col] == other
                        && board[row][col+1] == player
                        && board[row][col+2] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == other) {
                    count++;
                }
                col++;
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            // Не забываем отдельно проверить у верхнего края поля
            if (board[0][col] == player && board[1][col] == empty) count++;
            for (int row = 0; row < size - 3; row++) {
                if (board[row][col] == other
                        && board[row+1][col] == player
                        && board[row+2][col] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col] == player
                        && board[row+2][col] == other) {
                    count++;
                }
                row++;
            }
        }
        // Проверяем обе диагонали на наличие комбинации
        for (int row = 0; row < size - 3; row++) {
            // Не забываем проверить левый и верхний края
            if (board[row][0] == player && board[row + 1][1] == empty) count++;
            if (board[0][row + 1] == player && board[1][row + 2] == empty) count++;
            for (int col = 0; col < size - 3; col++) {
                if (board[row][col] == other
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == other) {
                    count++;
                } else if (board[row][col + 2] == other
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col] == empty) {
                    count++;
                } else if (board[row][col + 2] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col] == other) {
                    count++;
                }
            }

        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки открытых двоек
    public int checkOpenTwos(char player) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида _XX_, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 4; col++) {
                if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == empty) {
                    count++;
                    col += 2;
                }
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size - 4; row++) {
                if (board[row][col] == empty
                        && board[row + 1][col] == player
                        && board[row + 2][col] == player
                        && board[row + 3][col] == empty) {
                    count++;
                    row += 2;
                }
            }
        }
        // Проверяем диагонали на наличие комбинации
        for (int row = 0; row < size - 4; row++) {
            for (int col = 0; col < size - 4; col++) {
                if (board[row][col] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == player
                        && board[row + 3][col + 3] == empty) {
                    count++;
                } else if (board[row][col + 3] == empty
                        && board[row + 1][col + 2] == player
                        && board[row + 2][col + 1] == player
                        && board[row + 3][col] == empty) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки закрытых с одной стороны двоек
    public int checkClosedTwos(char player, char other) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида OXX_ или _XXO, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            // Не забываем отдельно проверить у левого края поля
            if (board[row][0] == player
                    && board[row][1] == player
                    && board[row][2] == empty) {
                count++;
            }
            for (int col = 0; col < size - 4; col++) {
                if (board[row][col] == other
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == other) {
                    count++;
                }
                col += 2;
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            // Не забываем отдельно проверить у верхнего края поля
            if (board[0][col] == player
                    && board[1][col] == player
                    && board[2][col] == empty) {
                count++;
            }
            for (int row = 0; row < size - 4; row++) {
                if (board[row][col] == other
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == other) {
                    count++;
                }
                row += 2;
            }
        }
        // Проверяем обе диагонали на наличие комбинации
        for (int row = 0; row < size - 4; row++) {
            // Не забываем проверить левый и верхний края
            if (board[row][0] == player
                    && board[row + 1][1] == player
                    && board[row + 2][2] == empty) {
                count++;
            }
            if (board[0][row + 1] == player
                    && board[1][row + 2] == player
                    && board[2][row + 3] == empty) {
                count++;
            }
            for (int col = 0; col < size - 4; col++) {
                if (board[row][col] == other
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == other) {
                    count++;
                } else if (board[row][col+3] == other
                        && board[row+1][col+2] == player
                        && board[row+2][col+1] == player
                        && board[row+3][col] == empty) {
                    count++;
                } else if (board[row][col+3] == empty
                        && board[row+1][col+2] == player
                        && board[row+2][col+1] == player
                        && board[row+3][col] == other) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки открытых троек
    public int checkOpenThrees(char player) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида _XXX_, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 5; col++) {
                if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == empty) {
                    count++;
                    col += 3;
                }
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size - 5; row++) {
                if (board[row][col] == empty
                        && board[row + 1][col] == player
                        && board[row + 2][col] == player
                        && board[row + 3][col] == player
                        && board[row + 4][col] == empty) {
                    count++;
                    row += 3;
                }
            }
        }
        // Проверяем диагонали на наличие комбинации
        for (int row = 0; row < size - 5; row++) {
            for (int col = 0; col < size - 5; col++) {
                if (board[row][col] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == player
                        && board[row + 3][col + 3] == player
                        && board[row + 4][col + 4] == empty) {
                    count++;
                } else if (board[row][col + 4] == empty
                        && board[row + 1][col + 3] == player
                        && board[row + 2][col + 2] == player
                        && board[row + 3][col + 1] == player
                        && board[row + 4][col] == empty) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки закрытых с одной стороны троек
    public int checkClosedThrees(char player, char other) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида OXXX_ или _XXXO, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            // Не забываем отдельно проверить у левого края поля
            if (board[row][0] == player
                    && board[row][1] == player
                    && board[row][2] == player
                    && board[row][3] == empty) {
                count++;
            }
            for (int col = 0; col < size - 5; col++) {
                if (board[row][col] == other
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == other) {
                    count++;
                }
                col += 3;
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            // Не забываем отдельно проверить у верхнего края поля
            if (board[0][col] == player
                    && board[1][col] == player
                    && board[2][col] == player
                    && board[3][col] == empty) {
                count++;
            }
            for (int row = 0; row < size - 5; row++) {
                if (board[row][col] == other
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == player
                        && board[row+4][col] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == player
                        && board[row+4][col] == other) {
                    count++;
                }
                row += 3;
            }
        }
        // Проверяем обе диагонали на наличие комбинации
        for (int row = 0; row < size - 5; row++) {
            // Не забываем проверить левый и верхний края
            if (board[row][0] == player
                    && board[row + 1][1] == player
                    && board[row + 2][2] == player
                    && board[row + 3][3] == empty) {
                count++;
            }
            if (board[0][row + 1] == player
                    && board[1][row + 2] == player
                    && board[2][row + 3] == player
                    && board[3][row + 4] == empty) {
                count++;
            }
            for (int col = 0; col < size - 5; col++) {
                if (board[row][col] == other
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == player
                        && board[row+4][col+4] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == player
                        && board[row+4][col+4] == other) {
                    count++;
                } else if (board[row][col+4] == other
                        && board[row+1][col+3] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+1] == player
                        && board[row+4][col] == empty) {
                    count++;
                } else if (board[row][col+4] == empty
                        && board[row+1][col+3] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+1] == player
                        && board[row+4][col] == other) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки открытых четвёрок
    public int checkOpenFours(char player) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида _XXXX_, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 6; col++) {
                if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == player
                        && board[row][col+5] == empty) {
                    count++;
                    col += 4;
                }
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size - 6; row++) {
                if (board[row][col] == empty
                        && board[row + 1][col] == player
                        && board[row + 2][col] == player
                        && board[row + 3][col] == player
                        && board[row + 4][col] == player
                        && board[row + 5][col] == empty) {
                    count++;
                    row += 4;
                }
            }
        }
        // Проверяем диагонали на наличие комбинации
        for (int row = 0; row < size - 6; row++) {
            for (int col = 0; col < size - 6; col++) {
                if (board[row][col] == empty
                        && board[row + 1][col + 1] == player
                        && board[row + 2][col + 2] == player
                        && board[row + 3][col + 3] == player
                        && board[row + 4][col + 4] == player
                        && board[row + 5][col + 5] == empty) {
                    count++;
                } else if (board[row][col+5] == empty
                        && board[row + 1][col + 4] == player
                        && board[row + 2][col + 3] == player
                        && board[row + 3][col + 2] == player
                        && board[row + 4][col + 1] == player
                        && board[row + 5][col] == empty) {
                    count++;
                }
            }
        }
        // Возвращаем полученное значение
        return count;
    }
    // Метод проверки закрытых с одной стороны четвёрок
    public int checkClosedFours(char player, char other) {
        // Создаём счётчик
        int count = 0;
        // Если встречаем комбинацию вида OXXXX_ или _XXXXO, то увеличиваем счётчик.
        // Проверяем горизонтали на наличие комбинации
        for (int row = 0; row < size; row++) {
            // Не забываем отдельно проверить у левого края поля
            if (board[row][0] == player
                    && board[row][1] == player
                    && board[row][2] == player
                    && board[row][3] == player
                    && board[row][4] == empty) {
                count++;
            }
            for (int col = 0; col < size - 6; col++) {
                if (board[row][col] == other
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == player
                        && board[row][col+5] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row][col+1] == player
                        && board[row][col+2] == player
                        && board[row][col+3] == player
                        && board[row][col+4] == player
                        && board[row][col+5] == other) {
                    count++;
                }
                col += 4;
            }
        }
        // Проверяем вертикали на наличие комбинации
        for (int col = 0; col < size; col++) {
            // Не забываем отдельно проверить у верхнего края поля
            if (board[0][col] == player
                    && board[1][col] == player
                    && board[2][col] == player
                    && board[3][col] == player
                    && board[4][col] == empty) {
                count++;
            }
            for (int row = 0; row < size - 6; row++) {
                if (board[row][col] == other
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == player
                        && board[row+4][col] == player
                        && board[row+5][col] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col] == player
                        && board[row+2][col] == player
                        && board[row+3][col] == player
                        && board[row+4][col] == player
                        && board[row+5][col] == other) {
                    count++;
                }
                row += 4;
            }
        }
        // Проверяем обе диагонали на наличие комбинации
        for (int row = 0; row < size - 6; row++) {
            // Не забываем проверить левый и верхний края
            if (board[row][0] == player
                    && board[row + 1][1] == player
                    && board[row + 2][2] == player
                    && board[row + 3][3] == player
                    && board[row + 4][4] == empty) {
                count++;
            }
            if (board[0][row + 1] == player
                    && board[1][row + 2] == player
                    && board[2][row + 3] == player
                    && board[3][row + 4] == player
                    && board[4][row + 5] == empty) {
                count++;
            }
            for (int col = 0; col < size - 6; col++) {
                if (board[row][col] == other
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == player
                        && board[row+4][col+4] == player
                        && board[row+5][col+5] == empty) {
                    count++;
                } else if (board[row][col] == empty
                        && board[row+1][col+1] == player
                        && board[row+2][col+2] == player
                        && board[row+3][col+3] == player
                        && board[row+4][col+4] == player
                        && board[row+5][col+5] == other) {
                    count++;
                } else if (board[row][col+5] == other
                        && board[row+1][col+4] == player
                        && board[row+2][col+3] == player
                        && board[row+3][col+2] == player
                        && board[row+4][col+1] == player
                        && board[row+5][col] == empty) {
                    count++;
                } else if (board[row][col+5] == empty
                        && board[row+1][col+4] == player
                        && board[row+2][col+3] == player
                        && board[row+3][col+2] == player
                        && board[row+4][col+1] == player
                        && board[row+5][col] == other) {
                    count++;
                }
            }
        }
        // Возвращаем полученное количество
        return count;
    }
    // Метод проверки пятёрок
    public int checkFives(char player) {
        // Создаём счётчик
        int countAll = 0;

        // Проверяем все возможные направления победной комбинации
        // Горизонталь
        for (int i = 0; i < size; i++) {
            // Считаем количество подряд идущих символов player в строке i
            int count = 0;
            for (int j = 0; j < size; j++) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
        }
        // Вертикаль
        for (int j = 0; j < size; j++) {
            // Считаем количество подряд идущих символов player в столбце j
            int count = 0;
            for (int i = 0; i < size; i++) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
        }
        // Главная диагональ
        for (int k = 0; k <= size - winLength; k++) {
            // Считаем количество подряд идущих символов player на главной диагонали, начиная с клетки (k, 0)
            int count = 0;
            for (int i = k, j = 0; i < size && j < size; i++, j++) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
            // Считаем количество подряд идущих символов player на главной диагонали, начиная с клетки (0, k)
            count = 0;
            for (int i = 0, j = k; i < size && j < size; i++, j++) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
        }
        // Побочная диагональ
        for (int k = 0; k <= size - winLength; k++) {
            // Считаем количество подряд идущих символов player на побочной диагонали, начиная с клетки (k, size - 1)
            int count = 0;
            for (int i = k, j = size - 1; i < size && j >= 0; i++, j--) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
            // Считаем количество подряд идущих символов player на побочной диагонали, начиная с клетки (0, size - 1 - k)
            count = 0;
            for (int i = 0, j = size - 1 - k; i < size && j >= 0; i++, j--) {
                if (board[i][j] == player) {
                    count++;
                } else {
                    count = 0;
                }
                // Если количество достигло winLength, увеличиваем счётчик
                if (count == winLength) {
                    countAll++;
                }
            }
        }
        // Возвращаем полученное количество
        return countAll;
    }


    // Метод проверки на ничью
    public boolean isDraw() {
        // Если пустых клеток не осталось, то ничья
        return emptyCounter == 0;
    }
    // Геттер для получения победной комбинации
    public ArrayList<Point> getWinCombination() {
        return winCombination;
    }
    // Метод получения размера игрового поля
    public int getSize() {
        return size;
    }
    // Метод получения самого игрового поля
    public char[][] getBoard() {
        return board;
    }

    public boolean isGameOver(char player) {
        // Проверяем, есть ли победа или ничья у текущего игрока, используя его символ
        if (isWin(player) || isDraw()) {
            // Если да, возвращаем true
            return true;
        }
        // Если нет, возвращаем false
        return false;
    }

    public java.util.List<int[]> getValidMoves() {
        // Создаём список
        java.util.List<int[]> possibleMoves = new ArrayList<>();
        // Заносим туда координаты всех свободных клеток
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == empty) {
                    possibleMoves.add(new int[]{i, j});
                }
            }
        }
        // Возвращаем список
        return possibleMoves;
    }

    public void undoMove(int row, int col) {
        // Заполняем клетку символом пустой клетки
        board[row][col] = empty;
        // Увеличиваем счётчик пустых клеток
        emptyCounter++;
    }



}
