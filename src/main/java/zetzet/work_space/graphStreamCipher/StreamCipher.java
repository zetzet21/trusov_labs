package zetzet.work_space.graphStreamCipher;

public class StreamCipher {
    // Используем ГОСТ для генерации потока ключа
    private byte[] substitute(byte[] block) {
        block = rol(block, 4); // Пример использования вращения на 4 бита
        // Шифрование блока с использованием S-блоков ГОСТ
        int[] sBox = {
                10, 4, 14, 2, 13, 6, 1, 15, 8, 9, 0, 11, 7, 12, 3, 5,
                11, 2, 12, 4, 14, 13, 8, 15, 5, 3, 1, 6, 10, 7, 9, 0,
                15, 9, 1, 12, 5, 3, 10, 14, 2, 6, 8, 4, 7, 0, 11, 13,
                13, 7, 11, 1, 0, 10, 6, 15, 9, 4, 5, 12, 14, 2, 3, 8,
                6, 12, 9, 3, 15, 7, 13, 5, 1, 10, 8, 14, 0, 4, 11, 2,
                4, 9, 8, 0, 7, 1, 3, 13, 2, 6, 10, 5, 15, 12, 11, 14,
                13, 14, 4, 1, 3, 15, 5, 9, 8, 10, 7, 6, 2, 11, 0, 12,
                1, 12, 13, 0, 5, 7, 14, 4, 9, 3, 10, 2, 11, 6, 15, 8
        };

        byte[] result = new byte[block.length];
        for (int i = 0; i < block.length; i++) {
            result[i] = (byte) sBox[block[i] & 0x0F];
        }
        return result;
    }

    // Операция вращения влево
    private byte[] rol(byte[] block, int shift) {
        byte[] result = new byte[block.length];
        for (int i = 0; i < block.length; i++) {
            result[i] = (byte) ((block[i] << shift) | (block[i] >> (8 - shift)));
        }
        return result;
    }

    // Генерация потока ключа с использованием вращения влево
    public byte[] generateKeyStream(byte[] key, int length) {
        byte[] keyStream = new byte[length];
        int counter = 0;
        while (keyStream.length < length) {
            byte[] counterBytes = Integer.toHexString(counter).getBytes();

            // Применяем вращение влево к блоку счетчика перед подстановкой
            counterBytes = rol(counterBytes, 4); // Пример вращения на 4 бита

            keyStream = xorBlocks(keyStream, substitute(counterBytes)); // Применяем XOR
            counter++;
        }
        return keyStream;
    }


    // Функция для XOR двух блоков
    private byte[] xorBlocks(byte[] block1, byte[] block2) {
        byte[] result = new byte[Math.min(block1.length, block2.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (block1[i] ^ block2[i]);
        }
        return result;
    }

    // Шифрование/дешифрование текста
    public byte[] processData(byte[] data, byte[] key) {
        byte[] keyStream = generateKeyStream(key, data.length);
        return xorBlocks(data, keyStream);
    }

}
