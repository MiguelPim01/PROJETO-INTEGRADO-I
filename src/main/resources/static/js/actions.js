function listaCursos() {
    axios.get('/curso')
    .then(response => {
        const dataDisplay = document.getElementById('dropdown-content');
        dataDisplay.innerHTML = ''; // Clear previous content
        response.data.forEach(item => {
            const listItem = document.createElement('li');
            listItem.textContent = item.nome; // Assuming each item has a 'nome' property
            // Adicionando um ouvinte de eventos de clique ao listItem
            listItem.addEventListener('click', function() {
                // Chame aqui a função que você deseja executar ao clicar
                apresentaDisciplinas(item); // Exemplo de chamada de função
            });
            dataDisplay.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error(error);
    });
}

// Exemplo de função que pode ser chamada quando um item é clicado
function apresentaDisciplinas(item) {
    const dataDisplay = document.getElementById('paths');
    dataDisplay.innerHTML = ''; // Clear previous content

    trocaCSSPaths(dataDisplay);

    for (let i = 1; i <= item.qtdPeriodos; i++) { // Ajuste para incluir o último período
        const ulItem = document.createElement('ul');
        dataDisplay.appendChild(ulItem);

        const liItemPeriodo = document.createElement('li');
        liItemPeriodo.textContent = "Período " + i;
        liItemPeriodo.classList.add('periodo');

        ulItem.appendChild(liItemPeriodo);

        axios.get(`/curso/${item.id}/disciplina`)
        .then(response => {
            response.data.forEach(disciplina => {
                if (disciplina.periodo != i) {
                    return;
                }
                const liItem = document.createElement('li');
                liItem.textContent = disciplina.nome; // Assuming each item has a 'nome' property
                // Adicionando um ouvinte de eventos de clique ao liItem
                liItem.addEventListener('click', function() {
                    // Código para executar quando o liItem é clicado
                });
                ulItem.appendChild(liItem); // Adiciona o liItem à ulItem correta
            });
        })
        .catch(error => {
            console.error(error);
        });
    }
}

function trocaCSSPaths(dataDisplay){
    dataDisplay.style.flexDirection = 'row';
    dataDisplay.style.alignItems = 'stretch';
    dataDisplay.style.justifyContent = 'space-around';
}

function scrollPaths(){
    // Seleciona o elemento que tem overflow-x
    const sectionPaths = document.getElementById('paths');
    console.log(sectionPaths);
    // Adiciona um ouvinte de eventos para o evento 'wheel'
    sectionPaths.addEventListener('wheel', function(event) {
        // Previne o comportamento padrão de rolagem vertical
        event.preventDefault();

        // Ajusta a rolagem horizontal com base no movimento vertical da roda do mouse
        this.scrollLeft += event.deltaY * 2;
    }, { passive: false }); // O uso de { passive: false } permite que preventDefault funcione
}
 