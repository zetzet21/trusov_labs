package zetzet.work_space.caesarCipher;

import java.util.HashMap;
import java.util.Map;

public class CaesarCipher {
    // Частотный словарь символов на основе анализа английского языка.
    // Эти значения используются для расшифровки путем сопоставления частот символов.
    private static final Map<Character, Double> LETTER_FREQUENCY_MAP = new HashMap<>();

    // Инициализация частотного словаря
    static {
        LETTER_FREQUENCY_MAP.put('E', 12.70);
        LETTER_FREQUENCY_MAP.put('T', 9.06);
        LETTER_FREQUENCY_MAP.put('A', 8.17);
        LETTER_FREQUENCY_MAP.put('O', 7.51);
        LETTER_FREQUENCY_MAP.put('I', 6.97);
        LETTER_FREQUENCY_MAP.put('N', 6.75);
        LETTER_FREQUENCY_MAP.put('S', 6.33);
        LETTER_FREQUENCY_MAP.put('H', 6.09);
        LETTER_FREQUENCY_MAP.put('R', 5.99);
        LETTER_FREQUENCY_MAP.put('D', 4.25);
        LETTER_FREQUENCY_MAP.put('L', 4.03);
        LETTER_FREQUENCY_MAP.put('C', 2.78);
        LETTER_FREQUENCY_MAP.put('U', 2.76);
        LETTER_FREQUENCY_MAP.put('M', 2.41);
        LETTER_FREQUENCY_MAP.put('W', 2.36);
        LETTER_FREQUENCY_MAP.put('F', 2.23);
        LETTER_FREQUENCY_MAP.put('G', 2.02);
        LETTER_FREQUENCY_MAP.put('Y', 1.97);
        LETTER_FREQUENCY_MAP.put('P', 1.93);
        LETTER_FREQUENCY_MAP.put('B', 1.49);
        LETTER_FREQUENCY_MAP.put('V', 0.98);
        LETTER_FREQUENCY_MAP.put('K', 0.77);
        LETTER_FREQUENCY_MAP.put('X', 0.15);
        LETTER_FREQUENCY_MAP.put('J', 0.15);
        LETTER_FREQUENCY_MAP.put('Q', 0.10);
        LETTER_FREQUENCY_MAP.put('Z', 0.07);
    }

    /**
     * Метод для шифрования текста с использованием шифра Цезаря.
     * @param input  исходный текст
     * @param offset величина сдвига
     * @return зашифрованный текст
     */
    public String encode(String input, int offset) {
        StringBuilder encodedText = new StringBuilder();

        // Обходим каждый символ входного текста
        for (char symbol : input.toCharArray()) {
            // Обработка заглавных букв
            if (Character.isUpperCase(symbol)) {
                encodedText.append((char) ((symbol + offset - 'A') % 26 + 'A'));
            }
            // Обработка строчных букв
            else if (Character.isLowerCase(symbol)) {
                encodedText.append((char) ((symbol + offset - 'a') % 26 + 'a'));
            }
            // Символы, которые не являются буквами, добавляются без изменений
            else {
                encodedText.append(symbol);
            }
        }
        return encodedText.toString();
    }

    /**
     * Метод для расшифровки текста с использованием шифра Цезаря.
     * @param input  зашифрованный текст
     * @param offset величина сдвига
     * @return расшифрованный текст
     */
    public String decode(String input, int offset) {
        // Расшифровка выполняется шифрованием с отрицательным сдвигом
        return encode(input, -offset);
    }

    /**
     * Метод для взлома шифра Цезаря на основе анализа частоты символов.
     * @param input зашифрованный текст
     * @return сообщение с оптимальным сдвигом и расшифрованным текстом
     */
    public String crackCipher(String input) {
        int optimalOffset = 0; // Оптимальный сдвиг
        double lowestScore = Double.MAX_VALUE; // Наименьшая разница частот
        String decodedText = ""; // Лучший вариант расшифрованного текста

        // Перебираем все возможные сдвиги от 1 до 25
        for (int shiftAmount = 1; shiftAmount < 26; shiftAmount++) {
            String currentDecryption = decode(input, shiftAmount); // Дешифруем текст
            double currentScore = evaluateDecryption(currentDecryption); // Оцениваем текст по частотам

            // Обновляем оптимальный сдвиг, если текущий результат лучше
            if (currentScore < lowestScore) {
                lowestScore = currentScore;
                optimalOffset = shiftAmount;
                decodedText = currentDecryption;
            }
        }
        return "Optimal Shift: " + optimalOffset + "\nDecoded Message: " + decodedText;
    }

    /**
     * Метод для оценки качества расшифрованного текста.
     * Сравнивает частоту символов в тексте с частотой символов в английском языке.
     * @param input расшифрованный текст
     * @return суммарная разница частот
     */
    private double evaluateDecryption(String input) {
        Map<Character, Integer> frequencyCounter = new HashMap<>(); // Счетчик частот символов
        int totalSymbols = 0; // Общее количество символов

        // Считаем частоту появления символов в тексте
        for (char symbol : input.toUpperCase().toCharArray()) {
            if (Character.isLetter(symbol)) {
                frequencyCounter.put(symbol, frequencyCounter.getOrDefault(symbol, 0) + 1);
                totalSymbols++;
            }
        }

        // Если символов нет, возвращаем максимальное значение
        if (totalSymbols == 0) return Double.MAX_VALUE;

        double totalDifference = 0; // Суммарная разница частот

        // Сравниваем частоты символов с эталонными значениями
        for (Map.Entry<Character, Integer> entry : frequencyCounter.entrySet()) {
            char character = entry.getKey();
            double calculatedFrequency = (entry.getValue() / (double) totalSymbols) * 100;
            // Добавляем разницу между расчетной и эталонной частотой
            totalDifference += Math.abs(LETTER_FREQUENCY_MAP.getOrDefault(character, 0.0) - calculatedFrequency);
        }
        return totalDifference;
    }
}