# CatServer (1.16.5)
![](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-36.2.39-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.16.5-brightgreen.svg?colorB=469C00)

<b>The translation is not necessarily correct, it is recommended to view the English version.</b>
<b>Перевод может содержать ошибки и другие неточности. Мы рекомендуем осмотрить версию на английском языке.</b>

### CatServer — высокопроизводительное серверное ядро, поддерживающее моды и плагины на базе Forge+Bukkit+Spigot.
Ранняя версия 1.16.5 была разработана в коллаборации с командой проекта LoliServer. В настоящее время стабильно поддерживает и запускает обширное количество модов и плагинов.
Теперь это ядро будет поддерживаться как ответвление LoliServer, обеспечивая улчушенную оптимизацию и совместимость.
Вы можете скачать последнюю версию с [Jenkins](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.16.5/lastSuccessfulBuild/)

Все доступные версии CatServer:
|    Версия    |    Статус     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Активная разработка      |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  Долгосрочная поддержка/Стабильная версия  |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  Долгосрочная поддержка/Стабильная версия  |

QQ Группа: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Телеграм: [@CatServer](https://t.me/CatServer) | Дискорд канал: [Нажмите, чтобы присоединиться
](https://discord.gg/wvBJN4d)

### Особенности
Используется самописная оптимизация + некоторая оптимизация Paper, чтобы значительно повысить производительность.
Имеется мощная Remap-система для обеспечения хорошей совместимости плагинов.
Хорошая совместимость с FakePlayer.
Исправлено большинство ванильных ошибок безопасности.
Удобная конфигурация для любого человека.
API позволяющий плагинам взаимодействовать с модами.

### Установка
Загрузите или соберите последний сервер
Создайте скрипт запуска и запустите `(пример скрипта: java -Xmx2G -
jar CatServer-1.16.5-xxxxxxx-server.jar)`.
Дождитесь завершения загрузки файлов библиотек при первом запуске.
Рекомендуется запускать на Java 8 или 11, хотя поддерживаются Java 12-17 и более поздние версии, **могут быть проблемы совместимости с модами и плагинами**.

### Сборка
1. Клонируйте ветку 1.16.5: `git clone -b 1.16.5 https://github.com/Luohuayu/CatServer.git`
2. Перейдите в директорию: `cd CatServer`
3. Запустите настройку проекта: `gradlew setup`
4. Сгенерируйте патчи (если вы изменяете код Minecraft): `gradlew genPatches`
5. Собирите проект: `gradlew buildCatServer`


### Сгенерируйте зависимость
1. Запустите сервер, подождите пока скачаются библиотеки и пока сгенерируется SRG
2. Проимпортиуйте .jar в порядке с зависимостями (Добавьте больше библиотек при желании):
```
libraries/net/minecraftforge/forge/{MC_VERSION}-{FORGE_VERSION}/forge-{MC_VERSION}-{FORGE_VERSION}-universal.jar
libraries/net/minecraftforge/forge/{MC_VERSION}-{FORGE_VERSION}/forge-{MC_VERSION}-{FORGE_VERSION}-server.jar
libraries/net/minecraft/server/{MC_VERSION}-{MCP_VERSION}/server-{MC_VERSION}-{MCP_VERSION}-srg.jar
libraries/net/minecraftforge/eventbus/{EVENTBUS_VERSION}/eventbus-{EVENTBUS_VERSION}.jar
libraries/net/minecraftforge/forgespi/{SPI_VERSION}/forgespi-{SPI_VERSION}.jar
```
