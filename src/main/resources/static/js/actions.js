/**
 * Função responsável por listar os cursos na barra lateral do site.
 * 
 * @function listaCursos
 */
function listaCursos() {
    axios.get('/curso')
    .then(response => {
        const dataDisplay = document.getElementById('dropdown-content');
        dataDisplay.innerHTML = ''; // Limpa conteudo anterior

        response.data.forEach(item => {
            const listItem = document.createElement('li');
            listItem.id = item.id;
            listItem.classList.add('curso');

            listItem.textContent = item.nome; 
            listItem.addEventListener('click', function() {
                apresentaDisciplinas(item);
                trocaCSSCursosPadrao();

                if(document.getElementsByClassName('cursoClicado')[0]){
                    var cursoClicado = document.getElementsByClassName('cursoClicado')[0];
                    cursoClicado.classList.remove('cursoClicado');
                }

                listItem.classList.add('cursoClicado');
                trocaCSSCursoClicado(item.id);
            });

            dataDisplay.appendChild(listItem);
        });
    })
    .catch(error => {
        console.error(error);
    });
}

/**
 * Coloca o CSS padrão nos cursos.
 * 
 * @function apresentaDisciplinas
 */
function trocaCSSCursosPadrao(){
    const cursos = document.getElementsByClassName('curso');
    Array.from(cursos).forEach(curso => {
        curso.style.color = '#F5F5F5';
        curso.style.borderBottom = '1px solid #F5F5F5';
    });
}

/**
 * Coloca o CSS no curso clicado.
 * 
 * @function apresentaDisciplinas
 */
function trocaCSSCursoClicado(){
    const curso = document.getElementsByClassName('cursoClicado')[0];
    curso.style.color = '#A1E887';
    curso.style.borderBottom = '1px solid #A1E887';
}

/**
 * Função responsável por apresentar na tela as disciplinas de um curso.
 * 
 * @function apresentaDisciplinas
 * @param {Object} curso - Objeto que contém as informações do curso.
 */
function apresentaDisciplinas(curso) {
    const dataDisplay = document.getElementById('paths');
    dataDisplay.innerHTML = ''; // Limpa conteudo anterior

    trocaCSSPaths();

    for (let i = 1; i <= curso.qtdPeriodos; i++) {
        const ulItem = document.createElement('ul');
        ulItem.id = 'periodo' + i;
        dataDisplay.appendChild(ulItem);

        const liItemPeriodo = document.createElement('li');
        liItemPeriodo.textContent = "Período " + i;
        liItemPeriodo.classList.add('periodo');

        ulItem.appendChild(liItemPeriodo);

        axios.get(`/curso/${curso.id}/disciplina`)
        .then(response => {
            response.data.forEach(disciplina => {
                if (disciplina.periodo != i) {
                    return;
                }
                const liItem = document.createElement('li');
                liItem.classList.add('disciplina');
                liItem.style.zIndex = '1';
                liItem.id = disciplina.id;
                liItem.textContent = disciplina.nome; 

                liItem.addEventListener('click', function() {
                    pathing(disciplina.id, curso.id);

                    if(document.getElementsByClassName('disciplinaClicada')[0]){
                        var disciplinaClicada = document.getElementsByClassName('disciplinaClicada')[0];
                        disciplinaClicada.classList.remove('disciplinaClicada');
                    }

                    liItem.classList.add('disciplinaClicada');
                });

                ulItem.appendChild(liItem);
            });
        })
        .catch(error => {
            console.error(error);
        });
    }
}

/**
 * Função responsável por criar o botão de voltar as disciplinas para o normal.
 * 
 * @function criaButtonClose
 */
function criaButtonClose(){
    const buttonClose = document.createElement('button');

    buttonClose.textContent = 'Voltar';
    buttonClose.style.position = 'absolute';
    buttonClose.style.width = '180px';
    buttonClose.style.height = '90px';
    buttonClose.style.top = 'calc(100vh - 180px)';
    buttonClose.style.right = '90px';
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

    const dataDisplay = document.getElementsByTagName('body')[0];
    dataDisplay.appendChild(buttonClose);
}

/**
 * Coloca o CSS padrão em todas as disciplinas.
 * 
 * @function trocaCSSDisciplinasPadrao
 */
function trocaCSSDisciplinasPadrao(){
    const disciplinas = document.getElementsByClassName('disciplina');
    Array.from(disciplinas).forEach(disciplina => {
        disciplina.style.opacity = '1';
        disciplina.style.border = 'none';
        disciplina.style.color = '#F5F5F5';
        disciplina.style.backgroundColor = '#537A5A';
    });
}

/**
 * Coloca o CSS diminuindo a opacidade nas disciplinas que não foram clicadas ou que não fazem parte do caminho de dependências.
 * 
 * @function trocaCSSDisciplinasOpacidade
 */
function trocaCSSDisciplinasOpacidade(){
    const disciplinas = document.getElementsByClassName('disciplina');
    Array.from(disciplinas).forEach(disciplina => {
        if(!(disciplina.classList.contains('disciplinaClicada') || disciplina.classList.contains('disciplinaPathing'))){
            disciplina.style.opacity = '0.4';
            disciplina.style.border = 'none';
            disciplina.style.color = '#F5F5F5';
            disciplina.style.backgroundColor = '#537A5A';
        }
    });
}

/**
 * Coloca o CSS nas disciplinas que fazem parte do caminho de dependências.
 * 
 * @function trocaCSSDisciplinaPathing
 */
function trocaCSSDisciplinaPathing(){
    const disciplina = document.getElementsByClassName('disciplinaPathing');
    Array.from(disciplina).forEach(disciplina => {
        disciplina.style.opacity = '1';
        disciplina.style.backgroundColor = '#A1E887';
        disciplina.style.border = '4px solid #537A5A';
        disciplina.style.color = '#537A5A';
    });
}

/**
 * Coloca o CSS na disciplina clicada.
 * 
 * @function trocaCSSDisciplinaClicada
 */
function trocaCSSDisciplinaClicada(){
    const disciplina = document.getElementsByClassName('disciplinaClicada')[0];
    disciplina.style.opacity = '1';
    disciplina.style.backgroundColor = '#F5F5F5';
    disciplina.style.border = '4px solid #537A5A';
    disciplina.style.color = '#537A5A';
}

/**
 * Função responsável por criar o SVG onde as linhas que representam as dependências entre as disciplinas irão ser incluídas.
 * 
 * @function criaSVG
 */
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

/**
 * Função responsável por criar as linhas que representam as dependências entre as disciplinas.
 * 
 * @function criaLinha
 * @param {String} idA - Id da disciplina A.
 * @param {String} idB - Id da disciplina B.
 */
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

/**
 * Função chamada ao clicar em uma disciplina, responsável por criar as linhas que representam as dependências entre as disciplinas.
 * 
 * @function pathing
 * @param {String} disciplinaId - Id da disciplina.
 * @param {String} cursoId - Id do curso.
 */
function pathing(disciplinaId, cursoId){
    dataDisplay = document.getElementById('paths');

    if(!(document.getElementById('buttonClose'))){
        criaButtonClose();
    }

    if(document.getElementById('svg')){
        document.getElementById('svg').remove();
    }

    criaSVG();

    axios.get(`curso/${cursoId}/path/${disciplinaId}`)
    .then(response => {
        response.data.forEach(item => {
            const itemA = document.getElementById(item.a.id);
            const itemB = document.getElementById(item.b.id);

            itemA.classList.add('disciplinaPathing');
            itemB.classList.add('disciplinaPathing');

            criaLinha(item.a.id, item.b.id);
        });

        trocaCSSDisciplinaPathing();
        trocaCSSDisciplinasOpacidade();

        response.data.forEach(item => {
            const itemA = document.getElementById(item.a.id);
            const itemB = document.getElementById(item.b.id);

            itemA.classList.remove('disciplinaPathing');
            itemB.classList.remove('disciplinaPathing');
        });

        trocaCSSDisciplinaClicada();
    })
}

/**
 * Muda o CSS da section que contém as disciplinas.
 * 
 * @function trocaCSSPaths
 */
function trocaCSSPaths(){
    const dataDisplay = document.getElementById('paths');
    dataDisplay.style.flexDirection = 'row';
    dataDisplay.style.alignItems = 'stretch';
    dataDisplay.style.justifyContent = 'space-around';
}

/**
 * Função responsável por permitir a rolagem horizontal na section que contém as disciplinas.
 * 
 * @function scrollPaths
 */
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

/**
 * Função responsável por recalcular as posições das linhas que representam as dependências entre as disciplinas ao redimensionar a tela.
 * 
 * @function criaDivAcompanha
 */
function onResize(){
    console.log('resize');

    var cursoClicado = document.getElementsByClassName('cursoClicado')[0];
    var disciplinaClicada = document.getElementsByClassName('disciplinaClicada')[0];

    if(cursoClicado && disciplinaClicada){
        pathing(disciplinaClicada.id, cursoClicado.id);
    }
}