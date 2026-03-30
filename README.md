# QwenCode NetBeans Plugin

Плагин для интеграции QwenCode CLI с NetBeans IDE 29.

## Требования

- NetBeans 29
- Java 17+
- QwenCode CLI (`npm install -g @qwen-code/qwen-code`)

## Установка

1. Сборка: `mvn clean nbm:nbm`
2. Tools → Plugins → Downloaded → Add Plugins... → выбрать target/*.nbm
3. Install и перезапустить NetBeans

## Использование

- **Кнопка QwenCode** на тулбаре или Tools → QwenCode
- **Настройки**: Tools → Options → QwenCode
- **Output**: Window → QwenCode Output

## Горячие клавиши

| Команда | Shortcut |
|---------|----------|
| Code Review | Ctrl+Alt+R |
| Generate | Ctrl+Alt+G |
| Refactor | Ctrl+Alt+T |
| Generate Tests | Ctrl+Alt+Y |

## Лицензия

Apache 2.0

Что загружено:
     - ✅ Полный исходный код плагина (17 Java файлов)
     - ✅ Maven конфигурация (pom.xml)
     - ✅ NetBeans конфигурация (layer.xml, Bundle.properties)
     - ✅ Тесты (12 тестов, все проходят)
     - ✅ Иконка (qwen.png 16x16)
     - ✅ Документация (README.md)
     - ✅ NBM файл для установки (45 KB)

    Структура коммитов (13 коммитов):

      1 d0523a8 chore: clean up Icons.java comment
      2 1c31d61 feat: add qwen.png icon
      3 089a7ae fix: add fallback icon, fix openSettings, add XML error handling
      4 5159cb7 docs: add README and finalize plugin
      5 4019b46 feat: add Output TopComponent with tabs and chat
      6 b87c561 feat: add Options panel for command management
      7 51a72c5 feat: add toolbar action with command popup menu
      8 e68a5c7 feat: add CommandManager and ShortcutManager
      9 1aba483 feat: add QwenCodeCLI executor with async support
     10 058dcf1 feat: add ProjectContext for project/file/selection access
     11 9062f26 feat: add model layer
     12 3e2f027 feat: initial Maven project structure
     13 e5a2341 Initial commit: .gitignore

    Установка плагина в NetBeans 29:
     1. Скачать NBM файл из репозитория или собрать: mvn clean package
     2. В NetBeans: Tools → Plugins → Downloaded → Add Plugins...
     3. Выбрать target/nbm/qwencode-netbeans-plugin-1.0.0-SNAPSHOT.nbm
     4. Install и перезапустить NetBeans

    Использование:
     - Кнопка QwenCode на тулбаре
     - Меню Tools → QwenCode
     - Настройки: Tools → Options → QwenCode
     - Output: Window → QwenCode Output
