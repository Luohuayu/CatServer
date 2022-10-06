# CatServer (1.12.2)
![](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Forge-14.23.5.2860-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Spigot-1.12.2-brightgreen.svg?colorB=469C00)

CatServer é um servidor Forge+Bukkit+Spigot de alto desempenho e alta compatibilidade<br>
O tempo de atualização contínua mais longo da versão 1.12.2, suportando a maioria dos mods e plugins para rodar de forma estável<br>
Você pode baixar a versão mais recente em [nosso site](https://catserver.moe/download/universal) ou [lançamentos no GitHub](https://github.com/Luohuayu/CatServer/releases)<br>

Todas as versões do CatServer:
|    Versões    |    Status     |
| ------------- | ------------- |
| [1.18.2](https://github.com/Luohuayu/CatServer/tree/1.18.2)  |  Ativo       |
| [1.16.5](https://github.com/Luohuayu/CatServer/tree/1.16.5)  |  LTS         |
| [1.12.2](https://github.com/Luohuayu/CatServer/tree/1.12.2)  |  LTS/ESTÁVEL |

Grupo QQ: [591257](https://jq.qq.com/?_wv=1027&k=5B5aKkW) | Telegram: [@CatServer](https://t.me/CatServer) | Discord: [Click to join](https://discord.gg/wvBJN4d)<br>

#### Como nos apoiar
Não há necessidade de patrocinar, basta clicar em "Star" e "Fork" para o repositório, e clicar em "Watch" se quiser ser notificado

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

##### Recomendado para execução em Java 8, embora Java 9-17 e versões posteriores sejam suportadas, pode haver problemas de compatibilidade com mods e plugins

### Building
- 1.Iniciar projeto: `gradlew setup`
- Para importar para o IDEA ou Eclipse abra: projects/Catserver/.project
- 2.Criar um caminho(Se você modificar o código do Minecraft): `gradlew genPatches`
- 3.Build: `gradlew build`

### Gerar dependência
Use [CatServerSRG-Generator](https://github.com/Luohuayu/CatServerSRG-Generator) para gerar dependência, que pode ser usada para escrever plugins para operar NMS ou modificar MOD
