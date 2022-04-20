# CatServer
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2%20latest-brightgreen.svg?colorB=469C00)

<b>The translation is not necessarily correct, it is recommended to view the English version.</b>
<b>Перевод может содержать ошибки и другие неточности для большей ясности смотрите Английскую версию.</b>

CatServer на версии 1.12.2 | Forge+Bukkit+Spigot сервер<br>
Самое продолжительное время непрерывной работы, поддержка большинства модов и плагинов для стабильной работы сервера!<br>
Вы можете загрузить последнюю версию с [зеркального сайта](https://catserver.moe/download/universal) или [релизов GitHub](https://github.com/Luohuayu/CatServer/releases)<br>

Все версии CatServer:
[1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2) | [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)

Другая версия:
[LoliServer (1.16.5)](https://github.com/Loli-Server/LoliServer) | [FoxServer (1.18.2)](https://github.com/Luohuayu/FoxServer)

### Особенности
Использует самописную оптимизацию и оптимизацию Paper, чтобы повысить производительность<br>
Имеет мощную Remap систему для обеспечения хорошей совместимости плагинов.<br>
Хорошая совместимость с FakePlayer<br>
Исправлена большая часть ванильных ошибок для обеспечения безопасности<br>
Конфигурация обеспечивает удобные для пользователя опции<br>
Предоставлено API, чтобы плагины могли легко взаимодействовать с модами<br>

### Установка
1. Загрузите или соберите последнюю версию сервера
2. Создайте скрипт запуска и запустите (Пример скрипта: java -Xmx2G -jar CatServer-xxxxxxx-universal.jar)
3. Дождитесь завершения загрузки файлов библиотек при первом запуске<br>

### Сборка
- 1.Установить проект: `gradlew setup`
- Чтобы импортировать IDEA или Eclipse, откройте: `projects/Catserver/.project`
- 2.Сгенерировать патч(Если вы измените код Minecraft): `gradlew genPatches`
- 3.Собрать: `gradlew build`

### Создать зависимость 
Используйте [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) для создания зависимости, которую можно использовать для написания плагинов для работы NMS или изменения MOD 
