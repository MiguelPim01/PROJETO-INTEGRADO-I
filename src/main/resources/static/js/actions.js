function listaCursos() {
    axios.get('/curso')
    .then(response => {
        const dataDisplay = document.getElementById('dropdown-content');
        dataDisplay.innerHTML = ''; // Clear previous content
        response.data.forEach(item => {
            const listItem = document.createElement('li');
            listItem.id = item.id;
            listItem.classList.add('curso');

            listItem.textContent = item.nome; // Assuming each item has a 'nome' property
            // Adicionando um ouvinte de eventos de clique ao listItem
            listItem.addEventListener('click', function() {
                // Chame aqui a função que você deseja executar ao clicar
                apresentaDisciplinas(item); // Exemplo de chamada de função

                trocaCSSCursosPadrao();

                trocaCSSCursoClicado(item.id);

                if(document.getElementsByClassName('cursoClicado')[0]){
                    var cursoClicado = document.getElementsByClassName('cursoClicado')[0];
                    cursoClicado.classList.remove('cursoClicado');
                }

                listItem.classList.add('cursoClicado');
            });

            dataDisplay.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error(error);
    });
}

function trocaCSSCursosPadrao(){
    const cursos = document.getElementsByClassName('curso');
    Array.from(cursos).forEach(curso => {
        curso.style.color = '#F5F5F5';
        curso.style.borderBottom = '1px solid #F5F5F5';
    });
}

function trocaCSSCursoClicado(cursoId){
    const curso = document.getElementById(cursoId);
    curso.style.color = '#A1E887';
    curso.style.borderBottom = '1px solid #A1E887';
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
                liItem.id = disciplina.id;
                liItem.textContent = disciplina.nome; // Assuming each item has a 'nome' property
                // Adicionando um ouvinte de eventos de clique ao liItem
                liItem.addEventListener('click', function() {
                    pathing(disciplina.id, item.id); // Código para executar quando o liItem é clicado

                    if(document.getElementsByClassName('disciplinaClicada')[0]){
                        var disciplinaClicada = document.getElementsByClassName('disciplinaClicada')[0];
                        disciplinaClicada.classList.remove('disciplinaClicada');
                    }

                    liItem.classList.add('disciplinaClicada');
                });
                ulItem.appendChild(liItem); // Adiciona o liItem à ulItem correta
            });
        })
        .catch(error => {
            console.error(error);
        });
    }
}

function criaButtonClose(){
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
        trocaCSSDisciplinasPadrao();

        if(document.getElementById('svg')){
            document.getElementById('svg').remove();
        }
        buttonClose.remove();
    });

    dataDisplay = document.getElementById('paths');
    dataDisplay.appendChild(buttonClose);
}

function trocaCSSDisciplinasPadrao(){
    const disciplinas = document.getElementsByClassName('disciplina');
    Array.from(disciplinas).forEach(disciplina => {
        disciplina.style.opacity = '1';
        disciplina.style.border = 'none';
        disciplina.style.color = '#F5F5F5';
        disciplina.style.backgroundColor = '#537A5A';
    });
}

function trocaCSSDisciplinasOpacidade(){
    const disciplinas = document.getElementsByClassName('disciplina');
    Array.from(disciplinas).forEach(disciplina => {
        if(!(disciplina.classList.contains('disciplinaClicada') || window.getComputedStyle(disciplina).getPropertyValue('background-color') == 'rgb(161, 232, 135)')){
            disciplina.style.opacity = '0.4';
            disciplina.style.border = 'none';
            disciplina.style.color = '#F5F5F5';
            disciplina.style.backgroundColor = '#537A5A';
        }
    });
}

function trocaCSSDisciplinaPathing(disciplinaId){
    const disciplina = document.getElementById(disciplinaId);
    disciplina.style.opacity = '1';
    disciplina.style.backgroundColor = '#A1E887';
    disciplina.style.border = '4px solid #537A5A';
    disciplina.style.color = '#537A5A';
}

function trocaCSSDisciplinaClicada(disciplinaId){
    const disciplina = document.getElementById(disciplinaId);
    disciplina.style.opacity = '1';
    disciplina.style.backgroundColor = '#F5F5F5';
    disciplina.style.border = '4px solid #537A5A';
    disciplina.style.color = '#537A5A';
}

function criaSVG(){
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    const dataDisplay = document.getElementById('paths');

    svg.setAttribute('width', dataDisplay.scrollWidth + 'px');
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

    return svg;
}

function criaLinha(idA, idB){
    const connection = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    const svg = document.getElementById('svg');

    connection.id = 'connection' + idA + '-' + idB;

    const disciplinaA = document.getElementById(idA);
    const disciplinaB = document.getElementById(idB);

    var posA = disciplinaA.getBoundingClientRect();
    var posB = disciplinaB.getBoundingClientRect();
    var svgPos = svg.getBoundingClientRect();

    // Ajuste para coordenadas relativas ao SVG
    connection.setAttribute('x1', posA.left + posA.width - svgPos.left);
    connection.setAttribute('y1', posA.top + posA.height / 2 - svgPos.top);
    connection.setAttribute('x2', posB.left - svgPos.left);
    connection.setAttribute('y2', posB.top + posB.height / 2 - svgPos.top);

    connection.setAttribute('stroke', '#537A5A');
    connection.setAttribute('stroke-width', '3');

    svg.appendChild(connection);
}

function pathing(disciplinaId, cursoId){
    dataDisplay = document.getElementById('paths');

    if(!(document.getElementById('buttonClose'))){
        criaButtonClose();
    }

    axios.get(`curso/${cursoId}/path/${disciplinaId}`)
    .then(response => {
        response.data.forEach(item => {
            trocaCSSDisciplinaPathing(item.a.id);
            trocaCSSDisciplinaPathing(item.b.id);
        });

        trocaCSSDisciplinaClicada(disciplinaId);
    })

    trocaCSSDisciplinasOpacidade();

    if(document.getElementById('svg')){
        document.getElementById('svg').remove();
    }

    criaSVG();

    axios.get(`curso/${cursoId}/path/${disciplinaId}`)
    .then(response => {
        response.data.forEach(item => {
            criaLinha(item.a.id, item.b.id);
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

function onResize(){
    console.log('resize');

    var cursoClicado = document.getElementsByClassName('cursoClicado')[0];
    var disciplinaClicada = document.getElementsByClassName('disciplinaClicada')[0];

    console.log(cursoClicado);
    console.log(disciplinaClicada);

    if(cursoClicado && disciplinaClicada){
        
        console.log(cursoClicado.id);
        console.log(disciplinaClicada.id);

        pathing(disciplinaClicada.id, cursoClicado.id);
    }
}