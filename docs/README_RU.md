# CatServer (1.12.2)
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2%20latest-brightgreen.svg?colorB=469C00)

<b>The translation is not necessarily correct, it is recommended to view the English version.</b>
<b>Перевод может содержать ошибки и другие неточности. Мы рекомендуем осмотрить версию на английском языке.</b>

Серверное ядро CatServer для версии 1.12.2 | Forge+Bukkit+Spigot<br>
Версия с самым продолжительным временем непрерывной работы, поддержки большинства модов и плагинов для стабильной работы сервера!<br>
Вы можете загрузить последнюю версию с [зеркального сайта](https://catserver.moe/download/universal) или [релизов GitHub](https://github.com/Luohuayu/CatServer/releases).<br>

Все версии CatServer:
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
Загрузите или соберите последний сервер.
Создайте скрипт запуска и запустите `(пример скрипта: java -Xmx2G -jar CatServer-xxxxxxx-universal.jar)`.
Дождитесь завершения загрузки файлов библиотек при первом запуске.


### Сборка
- 1.Запустите настройку проекта: `gradlew setup`.
- 2.Сгенерируйте патч(Если вы измените код Minecraft): `gradlew genPatches`.
- 3.Соберите: `gradlew build`.

### Создание зависимости 
Используйте [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) для создания зависимости, которую можно использовать для написания плагинов для работы NMS или изменения MOD.
