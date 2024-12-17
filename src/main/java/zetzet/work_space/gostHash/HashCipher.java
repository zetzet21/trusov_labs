package zetzet.work_space.gostHash;

public class HashCipher {
    private static final int BLOCK_SIZE = 8; // Размер блока (8 байт)

    // Паддинг для дополнения сообщения
    public static byte[] pad(byte[] data) {
        int paddingLen = BLOCK_SIZE - (data.length % BLOCK_SIZE);
        byte[] paddedData = new byte[data.length + paddingLen];
        System.arraycopy(data, 0, paddedData, 0, data.length);
        for (int i = data.length; i < paddedData.length; i++) {
            paddedData[i] = (byte) paddingLen;
        }
        return paddedData;
    }

    // Функция хэширования с использованием ГОСТ
    public static byte[] hashMessageHash(byte[] message, byte[] key) {
        byte[] paddedMessage = pad(message); // Паддинг
        byte[][] blocks = new byte[paddedMessage.length / BLOCK_SIZE][BLOCK_SIZE]; // Разбиение сообщения на блоки

        // Разбиение на блоки
        for (int i = 0; i < paddedMessage.length; i += BLOCK_SIZE) {
            System.arraycopy(paddedMessage, i, blocks[i / BLOCK_SIZE], 0, BLOCK_SIZE);
        }

        byte[] hPrev = new byte[BLOCK_SIZE]; // Начальный хеш, все байты равны 0

        // Алгоритм хэширования с измененной формулой
        for (byte[] block : blocks) {
            byte[] encrypted = hashEncryptBlock(hPrev, key); // Шифруем предыдущий хеш с использованием ГОСТ
            byte[] intermediate = complexXor(encrypted, block); // Применяем сложное XOR (сдвиг и маскирование)
            hPrev = intermediate; // Обновляем предыдущий хеш
        }

        return hPrev; // Возвращаем итоговый хеш
    }

    // Операция XOR (сдвиг и маскирование)
    private static byte[] complexXor(byte[] encrypted, byte[] block) {
        byte[] result = new byte[BLOCK_SIZE];

        // Применяем побитовый сдвиг и маскировку
        for (int i = 0; i < encrypted.length; i++) {
            result[i] = (byte) ((encrypted[i] << 3) & 0xFF); // Сдвиг влево на 3 бита
            result[i] ^= block[i]; // XOR с текущим блоком
            result[i] &= 0x7F; // Маскируем старшие биты
        }

        return result;
    }

    private static byte[] hashEncryptBlock(byte[] block, byte[] key) {
        if (key.length != 32) {
            throw new IllegalArgumentException("Key must be 32 bytes long.");
        }

        int left = byteArrayToInt(block, 0);
        int right = byteArrayToInt(block, 4);
        int[] keyParts = new int[8];

        // Разбиение ключа на 8 частей по 4 байта
        for (int i = 0; i < 8; i++) {
            keyParts[i] = byteArrayToInt(key, i * 4);
        }

        for (int i = 0; i < 24; i++) {
            int temp = (left + keyParts[i % 8]) & 0xFFFFFFFF;
            right = right ^ substitute(temp);
            left = right;
        }

        byte[] result = new byte[BLOCK_SIZE];
        intToByteArray(left, result, 0);
        intToByteArray(right, result, 4);
        return result;
    }

    // Преобразование массива байтов в целое число (4 байта)
    private static int byteArrayToInt(byte[] byteArray, int offset) {
        return (byteArray[offset] & 0xFF) | ((byteArray[offset + 1] & 0xFF) << 8) |
                ((byteArray[offset + 2] & 0xFF) << 16) | ((byteArray[offset + 3] & 0xFF) << 24);
    }

    // Преобразование целого числа в массив байтов
    private static void intToByteArray(int value, byte[] byteArray, int offset) {
        byteArray[offset] = (byte) (value & 0xFF);
        byteArray[offset + 1] = (byte) ((value >> 8) & 0xFF);
        byteArray[offset + 2] = (byte) ((value >> 16) & 0xFF);
        byteArray[offset + 3] = (byte) ((value >> 24) & 0xFF);
    }

    // Функция замещения для ГОСТ
    private static int substitute(int value) {
        return value; // Можно добавить замену по таблице, если требуется
    }
}
