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

CatServer é um servidor Forge+Bukkit+Spigot de alto desempenho<br>
A versão 1.16.5 inicial foi co-desenvolvida com a equipe do projeto LoliServer. Atualmente suporta a maioria dos mods e plugins para rodar de forma estável<br>
Agora continuará sendo mantido como um fork do LoliServer, proporcionando melhor otimização e compatibilidade<br>
Você pode baixar a versão mais recente em [Jenkins](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.16.5/lastSuccessfulBuild/)<br>

Todas as versões do CatServer:
|    Versões    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Ativo        |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS          |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/ESTÁVEL  |

Grupo QQ: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

### Características
Use a otimização de escrita automática e alguma otimização de papel para melhorar muito o desempenho<br>
Tenha um poderoso sistema Remap para garantir uma boa compatibilidade de plugins<br>
Boa compatibilidade com FakePlayer<br>
Corrija a maioria dos bugs do vanilla com segurança<br>
A configuração fornece opções fáceis de usar<br>
Fornecer API para permitir que plugins interajam facilmente com mods<br>

### Instalação
1. Baixe ou faça um build do servidor mais recente
2. Crie o script de inicialização e execute (Script de exemplo: java -Xmx2G -jar CatServer-xxxxxxx-universal.jar)
3. Aguarde a conclusão do download do arquivo de bibliotecas na primeira inicialização

##### Recomendado para execução em Java 8 ou 11, embora Java 12-17 e versões posteriores sejam suportadas, pode haver problemas de compatibilidade com mods e plugins

### Building
- 1.Baixe o branch 1.16.5 : `git clone -b 1.16.5 https://github.com/Luohuayu/CatServer.git`
- 2.Alterar diretório: `cd CatServer`
- 3.Iniciar projeto: `gradlew setup`
- 4.Criar um caminho(Se você modificar o código do Minecraft): `gradlew genPatches`
- 5.Build: `gradlew buildCatServer`

### Gerar dependência
##### A FAZER
