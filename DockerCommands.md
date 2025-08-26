

```bash
docker-compose -f bot-docker-compose.yml up -d
```

```bash
docker stop db 
```
```bash
docker stop todo_tb
```
```bash
docker rm db 
```

```bash
 docker rm todo_tb
```
```bash
docker volume rm telegrambotconstructionhelper_pgdata
```
```bash
docker volume rm $(docker volume ls -q)
```

```bash
docker volume ls
```