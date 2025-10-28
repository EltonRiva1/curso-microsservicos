# 🧩 Curso Microsserviços com Spring Boot e RabbitMQ

![Badge](https://img.shields.io/badge/Status-%20Concluído-green) ![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen) ![Docker](https://img.shields.io/badge/Docker-4.49.0-blue)

Projeto desenvolvido com base no curso [**Criando Microsserviços com Spring Boot e RabbitMQ (Udemy)**](https://www.udemy.com/course/criando-microsservicos-com-springboot-e-rabbitmq/?srsltid=AfmBOooOf4g1cBNk481JCTTOBUHJecZboRd9HJUuU4MNzZDTQ7muZL0T&couponCode=MT251028G19), com foco em comunicação assíncrona entre microsserviços utilizando **RabbitMQ** como broker de mensagens e **Spring Boot** como base da aplicação.

---

## 🚀 Propósito do Projeto

O objetivo é demonstrar, de forma prática, como construir uma arquitetura de microsserviços desacoplados que se comunicam por eventos, sem dependências diretas entre si.  
O fluxo simula um sistema de **pedidos** que passa pelas seguintes etapas:

1. **Pedidos API (`pedidos-api`)** — recebe o pedido via REST e publica o evento no RabbitMQ.  
2. **Processador (`processador`)** — consome o evento e persiste os dados no banco PostgreSQL.  
3. **Notificação (`notificacao`)** — consome o mesmo evento e envia um e-mail de confirmação via SMTP (Mailhog).

Essa arquitetura é baseada no padrão **event-driven** e demonstra:
- Uso de **Fanout Exchanges** e **Dead Letter Queues (DLQ)** no RabbitMQ.
- Comunicação totalmente **assíncrona e resiliente** entre serviços.
- Integração com **Docker Compose**, **PostgreSQL** e **Mailhog**.

---

## 🧱 Estrutura do Projeto
```
curso-microsservicos/
│
├── .env
├── docker-compose.yml
├── rabbitmq-diagrama.drawio
│
├── notificacao/ # Microsserviço de envio de e-mails
│ └── src/main/java/br/com/eltonriva/pedidos/notificacao
│
├── pedidos-api/ # Microsserviço principal de criação de pedidos
│ └── src/main/java/br/com/eltonriva/pedidos/api
│
└── processador/ # Microsserviço responsável por processar e salvar pedidos
└── src/main/java/br/com/eltonriva/pedidos/processador
```

---

## ⚙️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3+**
  - Spring Web
  - Spring AMQP
  - Spring Data JPA
  - Spring Mail
- **RabbitMQ**
- **PostgreSQL**
- **Mailhog** (SMTP fake para testes)
- **Docker Compose**
- **OpenAPI/Swagger** para documentação da API

---

## 🐳 Configuração e Execução

### 1️⃣ Pré-requisitos

Certifique-se de ter instalado:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)

---

### 2️⃣ Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/curso-microsservicos.git
cd curso-microsservicos

3️⃣ Configurar o Ambiente
O arquivo .env na raiz define as variáveis utilizadas pelo Docker Compose:

# RabbitMQ
RABBITMQ_DEFAULT_USER=rabbitmq
RABBITMQ_DEFAULT_PASS=rabbitmq
RABBITMQ_MANAGEMENT_PORT=15672
RABBITMQ_AMQP_PORT=5672

# Mailhog
MAILHOG_SMTP_PORT=1025
MAILHOG_UI_PORT=8025

# Postgres
POSTGRES_USER=root
POSTGRES_PASSWORD=root
POSTGRES_DB=pedidos-api
POSTGRES_PORT=5432

# Hostnames internos
RABBITMQ_HOST=rabbitmq
POSTGRES_HOST=postgres
SMTP_HOST=mailhog

4️⃣ Subir os Containers

docker-compose up -d

Isso iniciará:

🐇 RabbitMQ (UI em http://localhost:15672)
🐘 PostgreSQL (porta 5432)
📬 Mailhog (UI em http://localhost:8025)

5️⃣ Executar os Microsserviços
Cada microsserviço pode ser iniciado individualmente via IDE ou terminal:

📨 Pedidos API
cd pedidos-api
mvn spring-boot:run

⚙️ Processador
cd processador
mvn spring-boot:run

✉️ Notificação
cd notificacao
mvn spring-boot:run

🔄 Fluxo de Funcionamento
1.  Envie um POST para criar um pedido:

POST http://localhost:8080/api/v1/pedidos
Content-Type: application/json

{
    "cliente": "{{cliente}}",
    "itemPedidos": [
        {
            "quantidade": "{{quantidade}}",
            "produto": {
                "nome": "{{nomeProduto}}",
                "valor": "{{valorProduto}}"
            }
        }
    ],
    "valorTotal": {{valorTotal}},
    "emailNotificacao": "{{emailNotificacao}}"
}

2.  O Pedidos API publica o evento pedido-criado no RabbitMQ.
3.  O Processador consome, salva no banco e atualiza o status.
4.  O Notificação consome e envia e-mail via Mailhog.

📬 Acessos Rápidos
| Serviço     | URL Local                                                                    | Porta | Observações                               |
| ----------- | ---------------------------------------------------------------------------- | ----- | ----------------------------------------- |
| Pedidos API | [http://localhost:8080/api/v1/pedidos](http://localhost:8080/api/v1/pedidos) | 8080  | Swagger disponível em `/api-docs.html`    |
| Processador | —                                                                            | 8081* | Executa como consumer (sem endpoint REST) |
| Notificação | —                                                                            | 8082* | Executa como consumer (sem endpoint REST) |
| RabbitMQ UI | [http://localhost:15672](http://localhost:15672)                             | 15672 | Login: `rabbitmq` / `rabbitmq`            |
| Mailhog UI  | [http://localhost:8025](http://localhost:8025)                               | 8025  | Visualiza os e-mails simulados            |
| PostgreSQL  | localhost:5432                                                               | 5432  | Banco de dados `pedidos-api`              |

* As portas de processador e notificacao podem variar conforme configuração local ou IDE.

🧠 Conceitos Demonstrados

Comunicação assíncrona entre microsserviços.
Padrão Fanout Exchange (publicação de eventos para múltiplos consumidores).
Tratamento de falhas com Dead Letter Exchange (DLX) e DLQ.
Idempotência no envio de e-mails.
Persistência e consistência eventual.
Boas práticas de isolamento e configuração via .env.

🧹 Limpeza do Ambiente
Para parar e remover todos os containers:

docker-compose down -v

👨‍💻 Autor

Elton Riva
- [GitHub] (https://github.com/EltonRiva1)
- [LinkedIn] (https://www.linkedin.com/in/elton-riva-53206739)

Baseado no curso do instrutor @Udemy - Criando Microsserviços com Spring Boot e RabbitMQ

🪶 Licença

Este projeto é apenas para fins educacionais e de estudo, sem fins comerciais.
