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
