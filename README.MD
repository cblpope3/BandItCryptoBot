____
# Тестовый проект команды BAND_IT.
____
Приложение, взаимодействовать с которым можно через Телеграм-бота.
Присылает котировки криптовалют по запросу.
Пока функционал не до конца понятен, описание будет дополнено по ходу разработки.

Для успешной сборки проекта требуется поместить файл `application-deploy.yaml` в папку `./BotApp/src/main/resources`.
Структура файла:
```yaml
bot:
  username: TELEGRAM_BOT_NAME
  token: TELEGRAM_API_TOKEN
spring:
  datasource:
    url: DATABASE_URI
    username: DATABASE_USERNAME
    password: DATABASE_PASSWORD
```