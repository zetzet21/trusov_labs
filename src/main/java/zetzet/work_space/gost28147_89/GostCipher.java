package zetzet.work_space.gost28147_89;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GostCipher {

    // S-блоки (таблицы замены) для алгоритма ГОСТ 28147-89
    private static final int[][] S_BLOCKS = {
            {4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3},
            {14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9},
            {5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11},
            {7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3},
            {6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2},
            {4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14},
            {13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12},
            {1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 14, 3, 11, 6, 8, 12}
    };

    // Функция замены (S-блоки) - замена каждого 4-битного сегмента входного числа
    public static int substitute(int input) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            // Получаем 4 бита из input и заменяем их через S-блок
            int sBlockValue = S_BLOCKS[i][(input >> (4 * i)) & 0xF];
            // Собираем результат, сдвигая заменённые значения
            result |= sBlockValue << (4 * i);
        }
        return result;
    }

    // Функция циклического сдвига влево
    public static int rotateLeft(int value, int shift) {
        return ((value << shift) & 0xFFFFFFFF) | (value >> (32 - shift));
    }

    // Одна раундовая функция ГОСТ - возвращает массив с новым значением right и left
    public static int[] gostRound(int left, int right, int key) {
        int temp = (left + key) % (1 << 32); // Сложение с ключом по модулю 2^32
        temp = substitute(temp);            // Применение функции замены (S-блоки)
        temp = rotateLeft(temp, 11);        // Циклический сдвиг на 11 бит
        int newRight = right ^ temp;        // XOR для обновления правой части
        return new int[]{newRight, left};   // Возвращаем новый right и старый left
    }

    // Дополнение данных до длины, кратной 8 байтам
    public static byte[] pad(byte[] data) {
        int blockSize = 8;
        int padding = (blockSize - (data.length % blockSize)) % blockSize; // Рассчитываем количество байтов для дополнения
        byte[] paddedData = new byte[data.length + padding];               // Новый массив с padding
        System.arraycopy(data, 0, paddedData, 0, data.length);            // Копируем исходные данные
        for (int i = data.length; i < paddedData.length; i++) {           // Заполняем padding
            paddedData[i] = (byte) padding;
        }
        return paddedData;
    }

    // Удаление дополнения (padding) из расшифрованного сообщения
    public static byte[] unpad(byte[] input) {
        int paddingLength = input[input.length - 1]; // Количество байтов padding
        byte[] unpaddedData = new byte[input.length - paddingLength];
        System.arraycopy(input, 0, unpaddedData, 0, unpaddedData.length);
        return unpaddedData;
    }

    // Шифрование одного блока (8 байт)
    public static byte[] encryptBlock(byte[] block, byte[] key) {
        int left = byteArrayToInt(block, 0); // Левая половина блока
        int right = byteArrayToInt(block, 4); // Правая половина блока
        int[] keyParts = new int[8];          // Разбиение ключа на 8 частей

        for (int i = 0; i < 8; i++) {
            keyParts[i] = byteArrayToInt(key, i * 4);
        }

        // 24 "прямых" раунда
        for (int i = 0; i < 24; i++) {
            int[] result = gostRound(left, right, keyParts[i % 8]);
            right = result[0];
            left = result[1];
        }

        // 8 "обратных" раундов
        for (int i = 0; i < 8; i++) {
            int[] result = gostRound(left, right, keyParts[7 - i]);
            right = result[0];
            left = result[1];
        }

        // Собираем зашифрованный блок
        byte[] encryptedBlock = new byte[8];
        intToByteArray(left, encryptedBlock, 0);
        intToByteArray(right, encryptedBlock, 4);
        return encryptedBlock;
    }

    // Расшифрование одного блока
    public static byte[] decryptBlock(byte[] block, byte[] key) {
        int left = byteArrayToInt(block, 0);
        int right = byteArrayToInt(block, 4);
        int[] keyParts = new int[8];

        for (int i = 0; i < 8; i++) {
            keyParts[i] = byteArrayToInt(key, i * 4);
        }

        // 8 "прямых" раундов
        for (int i = 0; i < 8; i++) {
            int[] result = gostRound(left, right, keyParts[i]);
            right = result[0];
            left = result[1];
        }

        // 24 "обратных" раунда
        for (int i = 0; i < 24; i++) {
            int[] result = gostRound(left, right, keyParts[7 - (i % 8)]);
            right = result[0];
            left = result[1];
        }

        byte[] decryptedBlock = new byte[8];
        intToByteArray(left, decryptedBlock, 0);
        intToByteArray(right, decryptedBlock, 4);
        return decryptedBlock;
    }

    // Шифрование сообщения, состоящего из нескольких блоков
    public static byte[] encrypt(byte[] message, byte[] key) {
        message = pad(message); // Дополнение сообщения до кратности 8
        byte[] encryptedMessage = new byte[message.length];

        for (int i = 0; i < message.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(message, i, block, 0, 8);
            byte[] encryptedBlock = encryptBlock(block, key);
            System.arraycopy(encryptedBlock, 0, encryptedMessage, i, 8);
        }
        return encryptedMessage;
    }

    // Расшифрование сообщения
    public static byte[] decrypt(byte[] encryptedMessage, byte[] key) {
        byte[] decryptedMessage = new byte[encryptedMessage.length];

        for (int i = 0; i < encryptedMessage.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(encryptedMessage, i, block, 0, 8);
            byte[] decryptedBlock = decryptBlock(block, key);
            System.arraycopy(decryptedBlock, 0, decryptedMessage, i, 8);
        }
        return unpad(decryptedMessage);
    }

    // Преобразование массива байтов в целое число
    private static int byteArrayToInt(byte[] data, int offset) {
        if (data.length < offset + 4) {
            // Выводим информацию о длине данных и смещении для отладки
            System.out.println("Data length: " + data.length + ", Offset: " + offset);
            throw new IllegalArgumentException("Invalid data length");
        }
        return (data[offset] << 24) | ((data[offset + 1] & 0xFF) << 16) |
                ((data[offset + 2] & 0xFF) << 8) | (data[offset + 3] & 0xFF);
    }


    // Запись целого числа в массив байтов
    private static void intToByteArray(int value, byte[] array, int offset) {
        array[offset] = (byte) (value & 0xFF);
        array[offset + 1] = (byte) ((value >> 8) & 0xFF);
        array[offset + 2] = (byte) ((value >> 16) & 0xFF);
        array[offset + 3] = (byte) ((value >> 24) & 0xFF);
    }
}
