<p align="center">
<img src="https://i.imgur.com/REaCITJ.png" width="512" height="512"/>
</p>
<p align="center">
<a href="#"><img title="CatServer (1.12.2)" src="https://img.shields.io/badge/CatServer-green?colorA=%23ff0000&colorB=%23017e40&style=for-the-badge"></a>
</p>
<p align="center">
<a href="https://github.com/Luohuayu/"><img title="Author" src="https://img.shields.io/badge/AUTHOR-Luohuayu-orange.svg?style=for-the-badge&logo=github"></a>
</p>
<p align="center">
<a href="https://github.com/Luohuayu/followers"><img title="Followers" src="https://img.shields.io/github/followers/Luohuayu?color=blue&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/stargazers/"><img title="Stars" src="https://img.shields.io/github/stars/Luohuayu/CatServer?color=red&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/network/members"><img title="Forks" src="https://img.shields.io/github/forks/Luohuayu/CatServer?color=red&style=flat-square"></a>
<a href="https://github.com/Luohuayu/CatServer/watchers"><img title="Watching" src="https://img.shields.io/github/watchers/Luohuayu/CatServer?label=Watchers&color=blue&style=flat-square"></a>
</p>

# CatServer (1.16.5)
![](https://img.shields.io/badge/Minecraft-1.16.5-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-36.2.35-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.16.5-brightgreen.svg?colorB=469C00)

<b>The translation is not necessarily correct, it is recommended to view the English version.</b>
<b>Перевод может содержать ошибки и другие неточности для большей ясности смотрите Английскую версию.</b>

### CatServer — высокопроизводительный сервер для модов и плагинов на базе Forge+Bukkit+Spigot.
Ранняя версия 1.16.5 была разработана совместно с командой проекта LoliServer. В настоящее время поддерживает обширное количество модов и плагинов для стабильной работы
Теперь он будет по-прежнему поддерживаться как ответвление LoliServer, обеспечивая лучшую оптимизацию и совместимость.
Вы можете скачать последнюю версию с Jenkins

Все доступные версии CatServer:
|    Версия    |    Статус     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Активная разработка      |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  Долгосрочная поддержка         |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  Долгосрочная поддержка/Стабильная версия  |

QQ Группа: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Телеграм: [@CatServer](https://t.me/CatServer) | Дискорд канал: [Нажмите, чтобы присоединиться
](https://discord.gg/wvBJN4d)

### Функции
Используйте оптимизацию самозаписи и некоторую оптимизацию бумаги, чтобы значительно повысить производительность.
Иметь мощную систему переназначения для обеспечения хорошей совместимости плагинов.
Хорошая совместимость с FakePlayer
Исправьте большую часть ванильных ошибок для безопасности
Конфигурация обеспечивает удобные для пользователя параметры
Отличные API, чтобы плагины могли легко взаимодействовать с модами.

### Установка
Загрузите или соберите последний сервер
Создайте сценарий запуска и запустите `(пример сценария: java -Xmx2G -
jar CatServer-1.16.5-xxxxxxx-server.jar)`
Дождитесь завершения загрузки файла библиотек при первом запуске.
Рекомендуется запускать на Java8 или 11, хотя поддерживаются Java12-17 и более поздние версии, могут быть проблемы совместимости с модами и плагинами.

### Сборка
1. Ветка Clone 1.16.5: `git clone -b 1.16.5 https://github.com/Luohuayu/CatServer.git`
2. Изменить каталог: `cd CatServer`
3. Проект установки: установка градиента
4. Создайте патч (если вы изменяете код Minecraft): `gradlew genPatches`
5. Собрать всё: `gradlew buildCatServer`
