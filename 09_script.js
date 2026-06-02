// ============================================================
// ARQUIVO: web/js/script.js
// Funcionalidades principais da aplicação
// ============================================================

// ============================================================
// FUNÇÕES GLOBAIS
// ============================================================

// Alternar entre sections
function showSection(sectionId) {
    // Ocultar todas as sections
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => {
        section.classList.remove('active');
    });

    // Mostrar a section selecionada
    const selectedSection = document.getElementById(sectionId);
    if (selectedSection) {
        selectedSection.classList.add('active');
        
        // Carregar dados específicos da section
        if (sectionId === 'dashboard') {
            loadDashboard();
        } else if (sectionId === 'agendamentos') {
            loadAgendamentos();
        } else if (sectionId === 'pacientes') {
            loadPacientes();
        } else if (sectionId === 'profissionais') {
            loadProfissionais();
        } else if (sectionId === 'consultas') {
            loadConsultas();
        }
    }
}

// Mostrar alert
function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Formatar CPF
function formatCPF(cpf) {
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
}

// Remover formatação de CPF
function unformatCPF(cpf) {
    return cpf.replace(/[^\d]/g, '');
}

// Formatar telefone
function formatPhone(phone) {
    const cleaned = phone.replace(/\D/g, '');
    if (cleaned.length === 11) {
        return cleaned.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }
    return phone;
}

// Formatar data
function formatDate(date) {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('pt-BR');
}

// Formatar data e hora
function formatDateTime(datetime) {
    if (!datetime) return '';
    const d = new Date(datetime);
    return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { 
        hour: '2-digit', 
        minute: '2-digit' 
    });
}

// ============================================================
// DASHBOARD
// ============================================================

function loadDashboard() {
    console.log('Carregando dashboard...');
    
    // Carregar estatísticas (simulado)
    document.getElementById('agendamentosHoje').textContent = '5';
    document.getElementById('totalPacientes').textContent = '145';
    document.getElementById('totalProfissionais').textContent = '12';
    document.getElementById('examesPendentes').textContent = '8';
    
    // Carregar próximos agendamentos
    loadProximosAgendamentos();
}

function loadProximosAgendamentos() {
    const tbody = document.getElementById('proximosAgendamentos');
    
    // Dados simulados - substituir por requisição AJAX
    const agendamentos = [
        {
            id: 1,
            data: '2024-01-15',
            hora: '09:00',
            paciente: 'João Silva',
            profissional: 'Dr. Carlos',
            status: 'Agendado'
        },
        {
            id: 2,
            data: '2024-01-15',
            hora: '10:30',
            paciente: 'Maria Santos',
            profissional: 'Dra. Ana',
            status: 'Agendado'
        },
        {
            id: 3,
            data: '2024-01-15',
            hora: '14:00',
            paciente: 'Pedro Costa',
            profissional: 'Dr. Paulo',
            status: 'Realizado'
        }
    ];
    
    tbody.innerHTML = '';
    agendamentos.forEach(agendamento => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${agendamento.data}</td>
            <td>${agendamento.hora}</td>
            <td>${agendamento.paciente}</td>
            <td>${agendamento.profissional}</td>
            <td>
                <span class="badge badge-${agendamento.status === 'Realizado' ? 'success' : 'info'}">
                    ${agendamento.status}
                </span>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// ============================================================
// AGENDAMENTOS
// ============================================================

function loadAgendamentos() {
    console.log('Carregando agendamentos...');
    loadAgendamentosTable();
    loadPacientesSelect('pacienteSelect');
    loadProfissionaisSelect('profissionalSelect');
}

function loadAgendamentosTable() {
    const tbody = document.getElementById('tabelaAgendamentos');
    
    // Dados simulados
    const agendamentos = [
        {
            id: 1,
            data: '2024-01-15',
            hora: '09:00',
            paciente: 'João Silva',
            profissional: 'Dr. Carlos',
            status: 'Agendado'
        },
        {
            id: 2,
            data: '2024-01-15',
            hora: '10:30',
            paciente: 'Maria Santos',
            profissional: 'Dra. Ana',
            status: 'Agendado'
        }
    ];
    
    tbody.innerHTML = '';
    agendamentos.forEach(agendamento => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${agendamento.id}</td>
            <td>${agendamento.data}</td>
            <td>${agendamento.hora}</td>
            <td>${agendamento.paciente}</td>
            <td>${agendamento.profissional}</td>
            <td>${agendamento.status}</td>
            <td>
                <button class="btn btn-small btn-warning" onclick="editarAgendamento(${agendamento.id})">Editar</button>
                <button class="btn btn-small btn-danger" onclick="cancelarAgendamento(${agendamento.id})">Cancelar</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function loadPacientesSelect(selectId) {
    const select = document.getElementById(selectId);
    
    // Dados simulados
    const pacientes = [
        { id: 1, nome: 'João Silva' },
        { id: 2, nome: 'Maria Santos' },
        { id: 3, nome: 'Pedro Costa' }
    ];
    
    pacientes.forEach(paciente => {
        const option = document.createElement('option');
        option.value = paciente.id;
        option.textContent = paciente.nome;
        select.appendChild(option);
    });
}

function loadProfissionaisSelect(selectId) {
    const select = document.getElementById(selectId);
    
    // Dados simulados
    const profissionais = [
        { id: 1, nome: 'Dr. Carlos' },
        { id: 2, nome: 'Dra. Ana' },
        { id: 3, nome: 'Dr. Paulo' }
    ];
    
    profissionais.forEach(profissional => {
        const option = document.createElement('option');
        option.value = profissional.id;
        option.textContent = profissional.nome;
        select.appendChild(option);
    });
}

function editarAgendamento(id) {
    showAlert(`Editar agendamento ${id} - Funcionalidade em desenvolvimento`, 'info');
}

function cancelarAgendamento(id) {
    if (confirm('Tem certeza que deseja cancelar este agendamento?')) {
        showAlert(`Agendamento ${id} cancelado com sucesso`, 'success');
    }
}

// ============================================================
// PACIENTES
// ============================================================

function loadPacientes() {
    console.log('Carregando pacientes...');
    loadPacientesTable();
}

function loadPacientesTable() {
    const tbody = document.getElementById('tabelaPacientes');
    
    // Dados simulados
    const pacientes = [
        {
            id: 1,
            nome: 'João Silva',
            cpf: '123.456.789-00',
            dataNascimento: '1990-05-15',
            telefone: '(71) 98765-4321',
            email: 'joao@example.com'
        },
        {
            id: 2,
            nome: 'Maria Santos',
            cpf: '987.654.321-00',
            dataNascimento: '1985-10-20',
            telefone: '(71) 99876-5432',
            email: 'maria@example.com'
        }
    ];
    
    tbody.innerHTML = '';
    pacientes.forEach(paciente => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${paciente.id}</td>
            <td>${paciente.nome}</td>
            <td>${paciente.cpf}</td>
            <td>${formatDate(paciente.dataNascimento)}</td>
            <td>${paciente.telefone}</td>
            <td>${paciente.email}</td>
            <td>
                <button class="btn btn-small btn-primary" onclick="visualizarPaciente(${paciente.id})">Ver</button>
                <button class="btn btn-small btn-warning" onclick="editarPaciente(${paciente.id})">Editar</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function visualizarPaciente(id) {
    showAlert(`Visualizar paciente ${id} - Funcionalidade em desenvolvimento`, 'info');
}

function editarPaciente(id) {
    showAlert(`Editar paciente ${id} - Funcionalidade em desenvolvimento`, 'info');
}

// ============================================================
// PROFISSIONAIS
// ============================================================

function loadProfissionais() {
    console.log('Carregando profissionais...');
    loadProfissionaisTable();
}

function loadProfissionaisTable() {
    const tbody = document.getElementById('tabelaProfissionais');
    
    // Dados simulados
    const profissionais = [
        {
            id: 1,
            nome: 'Dr. Carlos Silva',
            registro: '123456',
            tipo: 'Médico',
            especialidade: 'Cardiologia',
            status: 'Ativo'
        },
        {
            id: 2,
            nome: 'Dra. Ana Santos',
            registro: '654321',
            tipo: 'Médico',
            especialidade: 'Pediatria',
            status: 'Ativo'
        }
    ];
    
    tbody.innerHTML = '';
    profissionais.forEach(profissional => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${profissional.id}</td>
            <td>${profissional.nome}</td>
            <td>${profissional.registro}</td>
            <td>${profissional.tipo}</td>
            <td>${profissional.especialidade}</td>
            <td>${profissional.status}</td>
            <td>
                <button class="btn btn-small btn-warning" onclick="editarProfissional(${profissional.id})">Editar</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function editarProfissional(id) {
    showAlert(`Editar profissional ${id} - Funcionalidade em desenvolvimento`, 'info');
}

// ============================================================
// CONSULTAS
// ============================================================

function loadConsultas() {
    console.log('Carregando consultas...');
    loadConsultasTable();
    loadAgendamentosSelect('agendamentoSelect');
}

function loadConsultasTable() {
    const tbody = document.getElementById('tabelaConsultas');
    
    // Dados simulados
    const consultas = [
        {
            id: 1,
            data: '2024-01-15 09:00',
            paciente: 'João Silva',
            profissional: 'Dr. Carlos',
            queixa: 'Dor no peito'
        },
        {
            id: 2,
            data: '2024-01-14 14:30',
            paciente: 'Maria Santos',
            profissional: 'Dra. Ana',
            queixa: 'Febre alta'
        }
    ];
    
    tbody.innerHTML = '';
    consultas.forEach(consulta => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${consulta.id}</td>
            <td>${consulta.data}</td>
            <td>${consulta.paciente}</td>
            <td>${consulta.profissional}</td>
            <td>${consulta.queixa}</td>
            <td>
                <button class="btn btn-small btn-primary" onclick="visualizarConsulta(${consulta.id})">Ver</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function loadAgendamentosSelect(selectId) {
    const select = document.getElementById(selectId);
    
    // Dados simulados
    const agendamentos = [
        { id: 1, paciente: 'João Silva', data: '2024-01-15 09:00' },
        { id: 2, paciente: 'Maria Santos', data: '2024-01-15 10:30' }
    ];
    
    agendamentos.forEach(agendamento => {
        const option = document.createElement('option');
        option.value = agendamento.id;
        option.textContent = `${agendamento.paciente} - ${agendamento.data}`;
        select.appendChild(option);
    });
}

function visualizarConsulta(id) {
    showAlert(`Visualizar consulta ${id} - Funcionalidade em desenvolvimento`, 'info');
}

// ============================================================
// EVENT LISTENERS
// ============================================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('Página carregada');
    
    // Mostrar dashboard por padrão
    showSection('dashboard');
    
    // Form Agendamento
    const formAgendamento = document.getElementById('formAgendamento');
    if (formAgendamento) {
        formAgendamento.addEventListener('submit', function(e) {
            e.preventDefault();
            showAlert('Agendamento realizado com sucesso!', 'success');
            this.reset();
            loadAgendamentos();
        });
    }
    
    // Form Paciente
    const formPaciente = document.getElementById('formPaciente');
    if (formPaciente) {
        formPaciente.addEventListener('submit', function(e) {
            e.preventDefault();
            showAlert('Paciente cadastrado com sucesso!', 'success');
            this.reset();
            loadPacientes();
        });
    }
    
    // Form Profissional
    const formProfissional = document.getElementById('formProfissional');
    if (formProfissional) {
        formProfissional.addEventListener('submit', function(e) {
            e.preventDefault();
            showAlert('Profissional cadastrado com sucesso!', 'success');
            this.reset();
            loadProfissionais();
        });
    }
    
    // Form Consulta
    const formConsulta = document.getElementById('formConsulta');
    if (formConsulta) {
        formConsulta.addEventListener('submit', function(e) {
            e.preventDefault();
            showAlert('Consulta registrada com sucesso!', 'success');
            this.reset();
            loadConsultas();
        });
    }
    
    // Logout
    const logoutLink = document.querySelector('.logout');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(e) {
            e.preventDefault();
            if (confirm('Deseja sair do sistema?')) {
                showAlert('Desconectado com sucesso', 'success');
                // Redirecionar para login: window.location.href = 'login.html';
            }
        });
    }
});

// ============================================================
// FUNÇÕES DE REQUISIÇÃO AJAX (Quando integrar com backend)
// ============================================================

/*
function fetchAgendamentos() {
    fetch('/api/agendamentos')
        .then(response => response.json())
        .then(data => {
            // Processar dados
        })
        .catch(error => console.error('Erro:', error));
}

function saveAgendamento(agendamento) {
    fetch('/api/agendamentos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(agendamento)
    })
    .then(response => response.json())
    .then(data => {
        showAlert('Agendamento salvo com sucesso', 'success');
    })
    .catch(error => console.error('Erro:', error));
}
*/
