# PathToGrade

Este é um aplicativo desenvolvido para Desktop Web destinado aos estudantes universitários. Ele foi desenvolvido para a disciplina de Projeto Integrado I da Universidade Federal do Espírito Santo e tem como objetivo facilitar o planejamento dos estudantes na realização das matérias.  

Neste aplicativo o estudante poderá visualizar todos os pré-requisitos de matérias do curso de forma simples e intuitiva e, com isso, não gastará mais um tempo exorbitante olhando os pré-requisitos de cada matéria manualmente.

## Instalação

Para a instalação basta apenas clonar o repositório e ter uma versão Java 21 ou superior instalada em sua máquina:
```bash
git clone git@github.com:MiguelPim01/PROJETO-INTEGRADO-I.git
```

## Utilização

### Opção 1: Via terminal

Entre na pasta raiz do projeto após clonar ele:
```bash
cd PROJETO-INTEGRADO-I
```

Rode o seguinte comando na pasta raiz pelo terminal:
```bash
./mvnw spring-boot:run
```

### Opção 2: Via Docker

Entre na pasta raiz do projeto após clonar ele:
```bash
cd PROJETO-INTEGRADO-I
```

Crie uma imagem a partir do projeto:
```bash
docker build -t pathtograde-app .
```

Rode a imagem:
```bash
docker run -p 8080:8080 pathtograde-app
```

Após o servidor ter subido corretamente, clique no link [pathtograde_project](http://localhost:8080/) para acessar a aplicação em seu navegador.

## Funcionalidades

É um aplicativo simples e intuitivo, ao abri-lo você irá se deparar com a seguinte tela:
  
![alt text](readme-images/image.png)

Na parte esquerda da tela você pode ver todos os cursos cadastrados no sistema e clicar no qual deseja navegar. Ao selecionar um curso, todas as matérias serão listadas por período da seguinte forma:

![alt text](readme-images/image-1.png)

É possível utilizar o scroll do mouse ou clicar na barra na parte inferior da tela, para navegar para a direita e ver os restante dos períodos.

Digamos que o curso de Ciência da Computação tenha sido escolhido e o usuário queira saber todas as displinas relacionadas (que fazem parte do mesmo "caminho") com a disciplina de Técnicas de Busca e Ordenação. Basta clicar na caixa da disciplina, que será apresentado as relações da seguinte maneira:

![alt text](readme-images/image-2.png)

Caso queira retornar ao padrão, há um botão na inferior esquerda escrito "Voltar". Ou então, basta clicar em outra disciplina que gostaria de navegar.

## Técnologias Utilizadas

### Ferramenta de Automação de Compilação

Foi utilizado o **Maven** que é uma ferramenta de automação de compilação usada principalmente para projetos **Java**. Ela é projetada para simplificar o processo de construção do projeto (build), incluindo compilação, distribuição, documentação, entre outros, através do uso de um arquivo de configuração chamado **pom.xml** (Project Object Model). 

### Back-End

O back-end foi desenvolvido em **Java**, com o framework **Spring** junto com sua extensão **Spring Boot** que simplifica o processo de configuração e publicação. Além de utilizar o framework **JUnit5** para aplicação de testes unitários e o **Mockito** para os testes utilizando mocks, que foram utilizados para a controladora.

### Front-End

Para o front-end, como é uma aplicação simples de apenas uma tela, foi utilizado **HTML** e **CSS**, juntamente com **Javascript** puro e a biblioteca **Axios** para fazer requisições HTTP.

## Desenvolvedores

1. [Miguel Vieira Machado Pim](https://github.com/MiguelPim01)  
2. [Pedro Henrique Bravim Duarte](https://github.com/PhenBD)
