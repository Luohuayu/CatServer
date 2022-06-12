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

# CatServer (1.18.2)

##### Selecione o README de outro idioma (Select Other Language README): [English](README_EN.md) | [中文](README.md) | [Русский](README_RU.md) | [Português](README_BR.md)

CatServer é um servidor Forge+Bukkit+Spigot de alto desempenho<br>
Você pode baixar a versão mais recente em [Jenkins](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.16.5/lastSuccessfulBuild/)<br>

Todas as versões do CatServer:
|    Versão    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Ativo        |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS          |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/ESTÁVEL  |

Grupo QQ: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [点击加入](https://discord.gg/wvBJN4d)

**Aviso: A versão atual não foi testada formalmente e não deve ser usada em ambientes de produção**

### Não fique inquieto, CatServer(1.18.2) está em desenvolvimento..
Você pode contribuir com o projeto em desenvolvimento ou teste.

#### Progresso do desenvolvimento
- CraftBukkit Patches (Quase)
- Spigot Patches (Feito)
- Patches Compatibility (Em andamento)
- FoxLaunch (Feito)
- BukkitInjector (Não foi implementado)
- Plugin Remapper (Não foi implementado)
- Plugin ASM Patcher (Não foi implementado)
- MOD ASM Patcher (Não foi implementado)
- CatServer API (Não foi implementado)

### Como construir e desenvolver?
Tutorial:

1. Baixe o código-fonte: `git clone -b 1.18.2 https://github.com/Luohuayu/CatServer.git`,
2. Entre no diretório: `cd CatServer`
3. Inicialize o projeto: `gradlew setup`, Então você poderá abrir o projeto no IDEA
4. Crie um caminho(Se você modificar o código do Minecraft): `gradlew genPatches`
5. Compile o projeto: `gradlew buildCatServer`
6. O arquivo será gerado em: `(project directory)/projects/forge/build/libs/`

### Como depurar no IDEA?
Executar tarefa gradle: `CatServer -> forge -> Tasks -> forgegradle runs -> forge_server`

Em seguida, use IDEA para depurar ou executar:  `CatServer:projects:forge [forge_server]` (Lembre-se de modificar `eula.txt` para concordar com a eula)

### Por que não há introdução ao Eclipse?
Como eu não uso o Eclipse, você precisa fazer sua própria pesquisa. (Você pode consultar [Forge Development Documentation](https://mcforge.readthedocs.io/en/latest/forgedev/))

### Como obter a última compilação?
**Dica: A versão atual não é recomendada**

Você pode baixá-lo em nosso Jenkins: [https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/](https://jenkins.rbqcloud.cn:30011/job/CatServer-1.18.2/lastSuccessfulBuild/)
