package zetzet.work_space.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSACipher {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger modulus;

    private static final int BIT_LENGTH = 1024; // Длина ключа

    public RSACipher() {
        generateKeys();
    }

    // Генерация ключей
    private void generateKeys() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(BIT_LENGTH / 2, random);
        BigInteger q = BigInteger.probablePrime(BIT_LENGTH / 2, random);

        modulus = p.multiply(q); // n = p * q
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)); // phi(n) = (p - 1) * (q - 1)

        publicKey = BigInteger.valueOf(65537); // Часто используется значение 65537 для e
        privateKey = publicKey.modInverse(phi); // d = e^-1 mod phi(n)
    }

    // Шифрование
    public String encrypt(String message) {
        BigInteger messageInt = new BigInteger(message.getBytes());
        BigInteger encrypted = messageInt.modPow(publicKey, modulus); // ciphertext = plaintext^e mod n
        return encrypted.toString();
    }

    // Дешифрование
    public String decrypt(String encryptedMessage) {
        BigInteger encryptedInt = new BigInteger(encryptedMessage);
        BigInteger decrypted = encryptedInt.modPow(privateKey, modulus); // plaintext = ciphertext^d mod n
        return new String(decrypted.toByteArray());
    }

    // Получение публичного ключа
    public BigInteger getPublicKey() {
        return publicKey;
    }

    // Получение приватного ключа
    public BigInteger getPrivateKey() {
        return privateKey;
    }

    // Получение модуля
    public BigInteger getModulus() {
        return modulus;
    }
}
