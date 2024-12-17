package zetzet.work_space.adfgvxCipher;

import java.util.*;

public class ADFGVXCipher {

    // Метод для генерации квадрата Полибия
    public static Map<String, Character> generatePolybiusSquare() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Алфавит [A-Z, 0-9]
        Map<String, Character> square = new HashMap<>();
        String adfgvx = "ADFGVX";
        int index = 0;

        for (char row : adfgvx.toCharArray()) {
            for (char col : adfgvx.toCharArray()) {
                square.put(String.valueOf(row) + col, alphabet.charAt(index));
                index++;
            }
        }
        return square;
    }

    // Метод для шифрования текста с использованием квадрата Полибия и ключа
    public static String encrypt(String message, String key) {
        StringBuilder encryptedMessage = new StringBuilder();
        Map<String, Character> square = generatePolybiusSquare();

        // Шифрование с использованием квадрата Полибия
        for (char c : message.toUpperCase().toCharArray()) {
            for (Map.Entry<String, Character> entry : square.entrySet()) {
                if (entry.getValue() == c) {
                    encryptedMessage.append(entry.getKey());
                    break;
                }
            }
        }

        // Завершающее шифрование с использованием ключа
        return finalEncrypt(encryptedMessage.toString(), key);
    }

    // Метод для окончательного шифрования с использованием ключа
    public static String finalEncrypt(String message, String key) {
        int paddingLen = 0;
        StringBuilder paddedMessage = new StringBuilder(message);

        // Добавление выравнивающих символов (если необходимо)
        if (message.length() % key.length() != 0) {
            paddingLen = key.length() - message.length() % key.length();
            for (int i = 0; i < paddingLen; i++) {
                paddedMessage.append('X');
            }
        }

        StringBuilder encryptedMessage = new StringBuilder();
        List<Integer> keyIndices = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            keyIndices.add(i);
        }

        // Разбиваем сообщение на колонки в зависимости от ключа
        Map<Integer, List<Character>> columns = new HashMap<>();
        for (int i = 0; i < key.length(); i++) {
            columns.put(i, new ArrayList<>());
        }

        for (int i = 0; i < paddedMessage.length(); i++) {
            columns.get(i % key.length()).add(paddedMessage.charAt(i));
        }

        // Сортируем индексы ключа
        keyIndices.sort(Comparator.comparingInt(i -> key.charAt(i)));

        // Строим зашифрованное сообщение
        for (int index : keyIndices) {
            for (Character c : columns.get(index)) {
                encryptedMessage.append(c);
            }
        }

        return encryptedMessage.toString() + paddingLen;  // Возвращаем зашифрованное сообщение и длину добавленных символов
    }

    // Метод для дешифровки с использованием квадрата Полибия
    public static String decryptSquare(String message, Map<String, Character> square) {
        StringBuilder decryptedMessage = new StringBuilder();

        for (int i = 0; i < message.length(); i += 2) {
            String key = message.substring(i, i + 2);
            if (square.containsKey(key)) {
                decryptedMessage.append(square.get(key));
            }
        }
        return decryptedMessage.toString();
    }

    // Метод для окончательной дешифровки с использованием ключа
    public static String finalDecrypt(String message, String key) {
        // Извлекаем количество добавленных символов
        int paddingLen = Character.getNumericValue(message.charAt(message.length() - 1));
        message = message.substring(0, message.length() - 1);  // Удаляем символ с количеством добавленных символов

        StringBuilder decryptedMessage = new StringBuilder();
        int colSize = message.length() / key.length();  // Длина каждой колонки

        // Получаем индексы символов ключа
        List<Integer> keyIndices = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            keyIndices.add(i);
        }

        // Сортируем индексы ключа
        List<Integer> sortedKeyIndices = new ArrayList<>(keyIndices);
        sortedKeyIndices.sort(Comparator.comparingInt(i -> key.charAt(i)));

        Map<Integer, List<Character>> columns = new HashMap<>();
        for (int index : sortedKeyIndices) {
            columns.put(index, new ArrayList<>());
        }

        // Заполняем колонки в соответствии с порядком ключа
        String tempMessage = message;
        for (int i : sortedKeyIndices) {
            for (int j = 0; j < colSize; j++) {
                columns.get(i).add(tempMessage.charAt(j));
            }
            tempMessage = tempMessage.substring(colSize);
        }

        // Восстанавливаем колонки в исходный порядок
        Map<Integer, List<Character>> unsortedKeyColumns = new HashMap<>();
        for (int i : keyIndices) {
            unsortedKeyColumns.put(i, columns.get(i));
        }

        // Восстановление исходного сообщения
        for (int i = 0; i < colSize; i++) {
            for (int j : keyIndices) {
                decryptedMessage.append(unsortedKeyColumns.get(j).get(i));
            }
        }

        // Удаляем добавленные символы
        if (paddingLen > 0) {
            decryptedMessage.delete(decryptedMessage.length() - paddingLen, decryptedMessage.length());
        }

        // Дешифруем сообщение с использованием квадрата Полибия
        return decryptSquare(decryptedMessage.toString(), generatePolybiusSquare());
    }
}
