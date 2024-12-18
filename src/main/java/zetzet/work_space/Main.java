package zetzet.work_space;

import zetzet.work_space.caesarCipher.CaesarCipher;
import zetzet.work_space.adfgvxCipher.ADFGVXCipher;
import zetzet.work_space.gost28147_89.GostCipher;
import zetzet.work_space.gostHash.HashCipher;
import zetzet.work_space.graphStreamCipher.StreamCipher;
import zetzet.work_space.rsa.RSACipher;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Создание объектов шифров и хэширования
        CaesarCipher caesarCipher = new CaesarCipher();
        ADFGVXCipher adfgvxCipher = new ADFGVXCipher();
        GostCipher gostCipher = new GostCipher();
        StreamCipher streamCipher = new StreamCipher();
        RSACipher rsa = new RSACipher();
        HashCipher hashCipher = new HashCipher();
        Scanner scanner = new Scanner(System.in);

        // Основной цикл программы для выбора типа шифра
        while (true) {
            System.out.println("Choose cipher:");
            System.out.println("1. Caesar Cipher");
            System.out.println("2. ADFGVX Cipher");
            System.out.println("3. GOST 28147-89 Cipher");
            System.out.println("4. Stream Cipher (XOR)");
            System.out.println("5. RSA Cipher");
            System.out.println("6. Hashing");
            System.out.println("7. Exit");
            System.out.print("Your choice: ");
            int cipherChoice = scanner.nextInt();
            scanner.nextLine();

            // Завершение программы при выборе "Exit"
            if (cipherChoice == 7) {
                System.out.println("Goodbye!");
                break;
            }

            // Вызов соответствующего подменю в зависимости от выбора пользователя
            switch (cipherChoice) {
                case 1 -> caesarCipherMenu(caesarCipher, scanner);
                case 2 -> adfgvxCipherMenu(adfgvxCipher, scanner);
                case 3 -> gostCipherMenu(gostCipher, scanner);
                case 4 -> streamCipherMenu(streamCipher, scanner);
                case 5 -> rsaCipherMenu(rsa, scanner);
                case 6 -> hashingMenu(hashCipher, scanner);
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Меню для работы с шифром Цезаря
    private static void caesarCipherMenu(CaesarCipher cipher, Scanner scanner) {
        while (true) {
            System.out.println("\nCaesar Cipher Options:");
            System.out.println("1. Encrypt text");
            System.out.println("2. Decrypt text");
            System.out.println("3. Break cipher");
            System.out.println("4. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Возврат в главное меню
            if (choice == 4) break;

            // Выполнение операций шифрования, дешифрования и взлома
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter text to encrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter shift: ");
                    int shift = scanner.nextInt();
                    System.out.println("Encrypted text: " + cipher.encode(text, shift));
                }
                case 2 -> {
                    System.out.print("Enter text to decrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter shift: ");
                    int shift = scanner.nextInt();
                    System.out.println("Decrypted text: " + cipher.decode(text, shift));
                }
                case 3 -> {
                    System.out.print("Enter text to break: ");
                    String text = scanner.nextLine();
                    System.out.println(cipher.crackCipher(text));
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Меню для работы с шифром ADFGVX
    private static void adfgvxCipherMenu(ADFGVXCipher cipher, Scanner scanner) {
        while (true) {
            System.out.println("\nADFGVX Cipher Options:");
            System.out.println("1. Encrypt text");
            System.out.println("2. Decrypt text");
            System.out.println("3. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Возврат в главное меню
            if (choice == 3) break;

            // Выполнение операций шифрования и дешифрования
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter text to encrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    System.out.println("Encrypted text: " + cipher.encrypt(text, key));
                }
                case 2 -> {
                    System.out.print("Enter text to decrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    System.out.println("Decrypted text: " + cipher.finalDecrypt(text, key));
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Меню для работы с шифром GOST 28147-89
    private static void gostCipherMenu(GostCipher cipher, Scanner scanner) {
        while (true) {
            System.out.println("\nGOST 28147-89 Cipher Options:");
            System.out.println("1. Encrypt message");
            System.out.println("2. Decrypt message");
            System.out.println("3. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Возврат в главное меню
            if (choice == 3) break;

            // Шифрование и дешифрование с ключами
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter message to encrypt: ");
                    String message = scanner.nextLine();
                    System.out.print("Enter 32-byte key: ");
                    String keyHex = scanner.nextLine();
                    byte[] key = hexStringToByteArray(keyHex);
                    byte[] encryptedMessage = cipher.encrypt(message.getBytes(), key);
                    System.out.println("Encrypted message: " + byteArrayToHexString(encryptedMessage));
                }
                case 2 -> {
                    System.out.print("Enter message to decrypt: ");
                    String message = scanner.nextLine();
                    System.out.print("Enter 32-byte key: ");
                    String keyHex = scanner.nextLine();
                    byte[] key = hexStringToByteArray(keyHex);
                    byte[] encryptedMessage = hexStringToByteArray(message);
                    byte[] decryptedMessage = cipher.decrypt(encryptedMessage, key);
                    System.out.println("Decrypted message: " + new String(decryptedMessage));
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Меню для работы с потоковым шифром (XOR)
    private static void streamCipherMenu(StreamCipher cipher, Scanner scanner) {
        while (true) {
            System.out.println("\nStream Cipher Options:");
            System.out.println("1. Encrypt text");
            System.out.println("2. Decrypt text");
            System.out.println("3. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Возврат в главное меню
            if (choice == 3) break;

            // Выполнение шифрования и дешифрования
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter text to encrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    byte[] encrypted = cipher.processData(text.getBytes(), key.getBytes());
                    System.out.println("Encrypted text: " + bytesToHex(encrypted));
                }
                case 2 -> {
                    System.out.print("Enter text to decrypt: ");
                    String text = scanner.nextLine();
                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();
                    byte[] encryptedBytes = hexToBytes(text);
                    byte[] decrypted = cipher.processData(encryptedBytes, key.getBytes());
                    System.out.println("Decrypted text: " + new String(decrypted));
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Меню для работы с RSA-шифром
    private static void rsaCipherMenu(RSACipher rsa, Scanner scanner) {
        while (true) {
            System.out.println("\nRSA Cipher Options:");
            System.out.println("1. Encrypt text");
            System.out.println("2. Decrypt text");
            System.out.println("3. View public key");
            System.out.println("4. View private key");
            System.out.println("5. Generate new keys");
            System.out.println("6. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 6) break;

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter text to encrypt: ");
                    String text = scanner.nextLine();
                    System.out.println("Encrypted text: " + rsa.encrypt(text));
                }
                case 2 -> {
                    System.out.print("Enter text to decrypt: ");
                    String text = scanner.nextLine();
                    System.out.println("Decrypted text: " + rsa.decrypt(text));
                }
                case 3 -> {
                    System.out.println("Public Key: " + rsa.getPublicKey());
                    System.out.println("Modulus: " + rsa.getModulus());
                }
                case 4 -> {
                    System.out.println("Private Key: " + rsa.getPrivateKey());
                    System.out.println("Modulus: " + rsa.getModulus());
                }
                case 5 -> {
                    rsa = new RSACipher();
                    System.out.println("New RSA keys generated.");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // Меню для хэширования
    private static void hashingMenu(HashCipher hashCipher, Scanner scanner) {
        while (true) {
            System.out.println("\nHashing Options:");
            System.out.println("1. Hash message");
            System.out.println("2. Back");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 2) break;

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter message to hash: ");
                    String message = scanner.nextLine();
                    System.out.print("Enter 32-byte key: ");
                    String keyHex = scanner.nextLine();
                    byte[] key = hexStringToByteArray(keyHex);
                    byte[] hashedMessage = hashCipher.hashMessageHash(message.getBytes(), key);
                    System.out.println("Hashed message: " + byteArrayToHexString(hashedMessage));
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Вспомогательные методы для преобразования байтов в HEX и обратно
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    private static byte[] hexStringToByteArray(String hex) {
        return hexToBytes(hex);
    }

    private static String byteArrayToHexString(byte[] bytes) {
        return bytesToHex(bytes);
    }
}
