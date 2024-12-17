package zetzet.work_space.graphStreamCipher;

public class StreamCipher {
    // Используем ГОСТ для генерации потока ключа
    private byte[] substitute(byte[] block) {
        block = rol(block, 4); // Пример использования вращения на 4 бита
        // Шифрование блока с использованием S-блоков ГОСТ
        int[] sBox = {
                4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3,
                14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9,
                5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11,
                7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3,
                6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2,
                4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14,
                13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12,
                1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 14, 3, 11, 6, 8, 12
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
