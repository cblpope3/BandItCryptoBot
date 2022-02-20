____
# Тестовый проект команды BAND_IT.
____
Приложение, взаимодействовать с которым можно через Телеграм-бота.
Присылает котировки криптовалют по запросу.

###### Ссылка на бот: 
@BanditTestBot
<br />
![BandIt_Bot](img/BanditTestBot.png)

Дашборды [тут](http://ec2-16-171-43-227.eu-north-1.compute.amazonaws.com:3000/d/twqdYjziz/micrometer-spring-throughput?orgId=1)

логин: `admin` Пароль: `BAND_IT`

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