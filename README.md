# Проект со всеми лабораторными работами по дисциплине "Методы шифрования". 

## Описание

Данный проект представляет собой консольное приложение для работы с различными криптографическими алгоритмами и хэш-функциями.
Программа позволяет шифровать, дешифровать и хэшировать данные с использованием следующих методов:

### Реализованные шифры и их функции:
1. **Caesar Cipher (Шифр Цезаря)**
    - **Функции:**
        - Шифрование текста с использованием сдвига символов.
        - Дешифрование текста.
        - Взлом шифра методом перебора.

2. **ADFGVX Cipher (Шифр ADFGVX)**
    - **Функции:**
        - Шифрование текста с использованием ключа.
        - Дешифрование текста с использованием ключа.

3. **GOST 28147-89 Cipher (Шифр ГОСТ 28147-89)**
    - **Функции:**
        - Шифрование и дешифрование сообщений с использованием 32-байтового ключа.
        - Поддержка работы с бинарными данными.

4. **Stream Cipher (Потоковый шифр с XOR)**
    - **Функции:**
        - Шифрование и дешифрование текста с использованием ключа методом XOR.

5. **RSA Cipher (RSA-шифр)**
    - **Функции:**
        - Шифрование текста с использованием алгоритма RSA.
        - Дешифрование текста.

6. **Hash Cipher (Хэширование ГОСТ)**
    - **Функции:**
        - Хэширование сообщений с использованием 32-байтового ключа.

---

## Использование
1. Запустите приложение из консоли.
2. Выберите необходимый алгоритм шифрования/хэширования, следуя инструкциям в меню.
3. Введите исходные данные (текст, ключ и т.д.).
4. Получите зашифрованный, расшифрованный или хэшированный результат на экране.

### Пример запуска программы:
```plaintext
Choose cipher:
1. Caesar Cipher
2. ADFGVX Cipher
3. GOST 28147-89 Cipher
4. Stream Cipher (XOR)
5. RSA Cipher
6. Hashing
7. Exit
Your choice: 1

Caesar Cipher Options:
1. Encrypt text
2. Decrypt text
3. Break cipher
4. Back
Your choice: 1
Enter text to encrypt: HelloWorld
Enter shift: 3
Encrypted text: KhoorZruog
```

---

## Предлагаемые данные для тестов

### 1. **Caesar Cipher**
- **Текст:** `HELLO`
- **Сдвиг:** `3`
- **Результат (шифрование):** `KHOOR`

### 2. **ADFGVX Cipher**
- **Текст:** `HELLO`
- **Ключ:** `KEY`
- **Результат (шифрование):** Зависит от реализации.

### 3. **GOST 28147-89 Cipher**
- **Текст:** `HELLO_WORLD`
- **Ключ:** `f29ab866ffc37ef8455c8718924002de5dadb51dcc46c9d2dc6d17ab8be3afca` (32-байтовый ключ в HEX)

### 4. **Stream Cipher**
- **Текст:** `HELLO`
- **Ключ:** `MYKEY`
- **Результат (шифрование):** HEX-представление XOR-результата.

### 5. **RSA Cipher**
- **Текст:** `HELLO`
- **Результат:** Зашифрованный текст зависит от открытого ключа RSA.

### 6. **Hashing (GOST)**
- **Текст:** `HELLO`
- **Ключ:** `0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF`
- **Результат:** Хэш-значение в HEX.

---

## Требования
- **JDK**: Версия 21

---
## Установка необходимого ПО

1. **Установите JDK 21**:
    - Скачать можно с официального сайта Oracle [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) или использовать OpenJDK.
    - После установки, проверьте что Java установилась на ваш компьютер консольной командой (нее забудьте про переменные среды):
      ```sh
      java -version
      ```

2. **Установите Maven**:
    - Скачать можете с официального сайта [Maven](https://maven.apache.org/download.cgi).
    - После установки, проверьте что Maven установился на ваш компьютер консольной командой (нее забудьте про переменные среды):
      ```sh
      mvn -v
      ```
---

## Компиляция и запуск
1. Склонируйте репозиторий:
   ```bash
   git clone <URL вашего репозитория>
   ```
2. Перейдите в директорию проекта и скомпилируйте:
   ```bash
   cd project-folder
   javac -d out src/zetzet/work_space/Main.java
   ```
3. Запустите приложение:
   ```bash
   java -cp out zetzet.work_space.Main
   ```

---

## Контакты
Разработчик: **BE|ГК ИННОТЕХ Бескровный Илья Александрович**  

Эл. почта: **zetzet16@yandex.ru**

Telegram: **[zetzet21](https://t.me/zetzet21)**