function listaCursos() {
    axios.get('/curso')
    .then(response => {
        const dataDisplay = document.getElementById('dropdown-content');
        dataDisplay.innerHTML = ''; // Clear previous content
        response.data.forEach(item => {
            const listItem = document.createElement('li');
            listItem.id = 'curso' + item.id;
            listItem.classList.add('curso');
            listItem.textContent = item.nome; // Assuming each item has a 'nome' property
            // Adicionando um ouvinte de eventos de clique ao listItem
            listItem.addEventListener('click', function() {
                // Chame aqui a função que você deseja executar ao clicar
                apresentaDisciplinas(item); // Exemplo de chamada de função

                const cursos = document.getElementsByClassName('curso');
                Array.from(cursos).forEach(curso => {
                    curso.style.color = '#F5F5F5';
                    curso.style.borderBottom = '1px solid #F5F5F5';
                });

                const curso = document.getElementById('curso' + item.id);
                curso.style.color = '#A1E887';
                curso.style.borderBottom = '1px solid #A1E887';
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
        ulItem.id = 'periodo' + i;
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
                liItem.classList.add('disciplina');
                liItem.style.zIndex = '1';
                liItem.id = 'disciplina' + disciplina.id;
                liItem.textContent = disciplina.nome; // Assuming each item has a 'nome' property
                // Adicionando um ouvinte de eventos de clique ao liItem
                liItem.addEventListener('click', function() {
                    preRequisitos(disciplina.id, item.id); // Código para executar quando o liItem é clicado
                });
                ulItem.appendChild(liItem); // Adiciona o liItem à ulItem correta
            });
        })
        .catch(error => {
            console.error(error);
        });
    }
}

function preRequisitos(disciplinaId, cursoId){
    dataDisplay = document.getElementById('paths');

    if(document.getElementById('buttonClose')){
        document.getElementById('buttonClose').remove();
    }

    // debugar
    const buttonClose = document.createElement('button');
    buttonClose.textContent = 'Voltar';
    buttonClose.style.position = 'absolute';
    buttonClose.style.width = '180px';
    buttonClose.style.height = '90px';
    buttonClose.style.top = 'calc(100vh - 180px)';
    buttonClose.style.right = 'calc(50vw - 90px)';
    buttonClose.style.backgroundColor = '#537A5A';
    buttonClose.style.color = '#F5F5F5';
    buttonClose.style.border = 'none';
    buttonClose.style.borderRadius = '5px';
    buttonClose.style.padding = '5px';
    buttonClose.style.cursor = 'pointer';
    buttonClose.style.fontSize = '20px';
    buttonClose.id = 'buttonClose';
    buttonClose.addEventListener('click', function(){
        const disciplinas = document.getElementsByClassName('disciplina');
        Array.from(disciplinas).forEach(disciplina => {
            disciplina.style.opacity = '1';
            disciplina.style.border = 'none';
            disciplina.style.color = '#F5F5F5';
            disciplina.style.backgroundColor = '#537A5A';
        });

        if(document.getElementById('svg')){
            document.getElementById('svg').remove();
        }
        buttonClose.remove();
    });

    dataDisplay.appendChild(buttonClose);

    const disciplinas = document.getElementsByClassName('disciplina');
    Array.from(disciplinas).forEach(disciplina => {
        disciplina.style.opacity = '0.4';
        disciplina.style.border = 'none';
        disciplina.style.color = '#F5F5F5';
        disciplina.style.backgroundColor = '#537A5A';
    });

    axios.get(`curso/${cursoId}/path/${disciplinaId}`)
    .then(response => {
        console.log(response.data);

        const disciplinas = document.getElementsByClassName('disciplina');
        Array.from(disciplinas).forEach(disciplina => {
            disciplina.style.opacity = '0.4';
        });

        response.data.forEach(item => {
            const disciplinaA = document.getElementById('disciplina' + item.a.id);
            const disciplinaB = document.getElementById('disciplina' + item.b.id);

            disciplinaA.style.opacity = '1';
            disciplinaA.style.backgroundColor = '#A1E887';
            disciplinaA.style.border = '4px solid #537A5A';
            disciplinaA.style.color = '#537A5A';

            disciplinaB.style.opacity = '1';
            disciplinaB.style.backgroundColor = '#A1E887';
            disciplinaB.style.border = '4px solid #537A5A';
            disciplinaB.style.color = '#537A5A';
        });

        const disciplina = document.getElementById('disciplina' + disciplinaId);
        disciplina.style.backgroundColor = '#F5F5F5';
    })

    if(document.getElementById('svg')){
        document.getElementById('svg').remove();
    }

    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('width', '100%');
    svg.setAttribute('height', '100%');
    svg.style.position = 'absolute';
    svg.style.pointerEvents = 'none';
    svg.style.top = '0';
    svg.style.left = '0';
    svg.id = 'svg';

    svg.addEventListener('scroll', function() {
        var scrollPos = dataDisplay.scrollLeft;
        var divAcompanha = document.getElementById('divAcompanha');
        divAcompanha.style.top = scrollPos + 'px';
    });

    dataDisplay.appendChild(svg);

    axios.get(`curso/${cursoId}/path/${disciplinaId}`)
    .then(response => {
        console.log(response.data);

        response.data.forEach(item => {
            const connection = document.createElementNS('http://www.w3.org/2000/svg', 'line');

            connection.id = 'connection' + item.a.id + '-' + item.b.id;

            const disciplinaA = document.getElementById('disciplina' + item.a.id);
            const disciplinaB = document.getElementById('disciplina' + item.b.id);

            var posA = disciplinaA.getBoundingClientRect();
            var posB = disciplinaB.getBoundingClientRect();
            var svgPos = svg.getBoundingClientRect();

            // Ajuste para coordenadas relativas ao SVG
            connection.setAttribute('x1', posA.left + posA.width - svgPos.left);
            connection.setAttribute('y1', posA.top + posA.height / 2 - svgPos.top);
            connection.setAttribute('x2', posB.left - svgPos.left);
            connection.setAttribute('y2', posB.top + posB.height / 2 - svgPos.top);

            connection.setAttribute('stroke', '#537A5A');
            connection.setAttribute('stroke-width', '2');

            svg.appendChild(connection);
        });
    })
    .catch(error => {
        console.error(error);
    });
}

function trocaCSSPaths(dataDisplay){
    dataDisplay.style.flexDirection = 'row';
    dataDisplay.style.alignItems = 'stretch';
    dataDisplay.style.justifyContent = 'space-around';
}

function scrollPaths(){
    // Seleciona o elemento que tem overflow-x
    const sectionPaths = document.getElementById('paths');
    // Adiciona um ouvinte de eventos para o evento 'wheel'
    sectionPaths.addEventListener('wheel', function(event) {
        // Previne o comportamento padrão de rolagem vertical
        event.preventDefault();

        // Ajusta a rolagem horizontal com base no movimento vertical da roda do mouse
        this.scrollLeft += event.deltaY * 2;
    }, { passive: false }); // O uso de { passive: false } permite que preventDefault funcione
}