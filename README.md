# Sistema ERP - Gestão de Loja de Suplementos

![Java](https://img.shields.io/badge/-Java-DE252C?style=flat-square&logo=java&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-square&logo=linux&logoColor=black)
![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-square&logo=windows&logoColor=white)
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/nksafa/SistemaERP_Projeto/blob/main/LICENSE)

# Sobre o projeto

O **ERP Suplementos** é uma aplicação web completa de gestão empresarial (ERP) construída para gerenciar as operações diárias de uma loja de varejo.

O sistema controla o fluxo completo de vendas (PDV), gerenciamento de estoque com alertas automáticos, cadastro de clientes e fornecedores, e possui um sistema robusto de controle de acesso baseado em níveis de usuário (RBAC) e logs de auditoria para segurança.

A aplicação foi desenvolvida utilizando a arquitetura MVC, separando claramente as responsabilidades entre a interface rica (JSF/PrimeFaces) e a lógica de negócios.

## Layout Web
https://sistemaerpprojeto-production.up.railway.app/

login: admin

senha: senhaadmin

<img width="1913" height="729" alt="image" src="https://github.com/user-attachments/assets/e6972d11-ee51-4f76-90c8-68757148c91c" />
<img width="1896" height="990" alt="image" src="https://github.com/user-attachments/assets/ce639c46-8e2a-4947-b004-724c6339ab47" />
<img width="1905" height="970" alt="image" src="https://github.com/user-attachments/assets/9fcddc4c-7aa1-42b7-8c03-50dd853487a5" />
<img width="1912" height="892" alt="image" src="https://github.com/user-attachments/assets/32fc6c99-9904-41b4-ac5e-e286d32965be" />





---

# Funcionalidades

### 🛒 Ponto de Venda (PDV)
- Realização de vendas com cálculo automático.
- Seleção de produtos com filtro dinâmico.
- Integração com controle de estoque (baixa automática).
- Opção de selecionar cliente ou venda anônima.
- Parcelamento para vendas no cartão de crédito.

### 📦 Gestão de Estoque
- Cadastro completo de produtos.
- **Alerta Visual:** Produtos abaixo do estoque mínimo ficam vermelhos na tabela.
- **Alerta no Dashboard:** Aviso persistente ao logar se houver itens com estoque crítico.

### 👥 Gerenciamento
- **Clientes:** CRUD completo e histórico de compras por cliente.
- **Fornecedores:** Gestão de parceiros e histórico de compras (entrada de nota).
- **Usuários:** Controle de acesso com perfis (Funcionário, Gerente, Administrador).

### 🛡️ Segurança e Auditoria
- Sistema de Login e Logout.
- Controle de visibilidade de botões/menus baseado no cargo do usuário.
- **Audit Logs:** Registro automático no banco de dados de ações críticas (quem criou, editou ou excluiu registros).

---

# Arquitetura

O projeto foi estruturado seguindo as melhores práticas de separação de responsabilidades em camadas:

* **com.suplementos.erp.model**: Camada de Entidades (JPA) que espelham o banco de dados.
* **com.suplementos.erp.repository**: Camada de Acesso a Dados (DAO) responsável pela comunicação com o MySQL via Hibernate.
* **com.suplementos.erp.service**: Camada de Regra de Negócio (ex: validação de estoque baixo).
* **com.suplementos.erp.jsf**: Camada de Controle (Managed Beans) que conecta o Front-end ao Back-end.

---

# Tecnologias utilizadas

### Back end
- Java 11
- Hibernate 5.6
- Maven
- Apache Tomcat 9
- MySQL 8

### Front end
- JavaServer Faces (JSF) 2.2
- PrimeFaces 10.0.0
- HTML5 / CSS3

### Deploy
- Docker
- Railway

---

# Como executar o projeto

## Pré-requisitos
- Java 11 JDK
- Maven
- MySQL Server rodando na porta 3306
- IDE (IntelliJ IDEA recomendado)

## Passo a passo

```bash
# Clone o repositório
git clone [https://github.com/seu-usuario/nome-do-projeto.git](https://github.com/nksafa/SistemaERP_Projeto.git)

# Entre na pasta do projeto
cd SistemaERP_Projeto

# Configuração do Banco de Dados
# 1. Crie um banco de dados no MySQL chamado 'db_suplementos'
# 2. Verifique o arquivo src/main/resources/hibernate.cfg.xml e ajuste usuário/senha se necessário.

# Executar o projeto (via IDE)
# 1. Abra o projeto no IntelliJ como projeto Maven.
# 2. Configure o servidor Tomcat Local apontando para o artefato :war exploded.
# 3. Execute.

# Acesso
O sistema estará disponível em: http://localhost:8080/ProjetoPOO3/
