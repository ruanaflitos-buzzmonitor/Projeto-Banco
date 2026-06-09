const API_BASE = './';

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });

    const selectedSection = document.getElementById(sectionId);
    if (!selectedSection) return;

    selectedSection.classList.add('active');

    if (sectionId === 'dashboard') loadDashboard();
    if (sectionId === 'agendamentos') loadAgendamentos();
    if (sectionId === 'pacientes') loadPacientes();
    if (sectionId === 'profissionais') loadProfissionais();
    if (sectionId === 'consultas') loadConsultas();
}

function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    setTimeout(() => alertDiv.remove(), 5000);
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, options);
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error((data && data.mensagem) || `HTTP ${response.status}`);
    }

    return data;
}

function formatCPF(cpf = '') {
    return String(cpf).replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
}

function formatDate(date) {
    if (!date) return '';
    const d = new Date(`${date}T00:00:00`);
    return d.toLocaleDateString('pt-BR');
}

function formatDateTime(datetime) {
    if (!datetime) return '';
    const d = new Date(datetime);
    return `${d.toLocaleDateString('pt-BR')} ${d.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit'
    })}`;
}

function setTableMessage(tbodyId, colspan, message) {
    const tbody = document.getElementById(tbodyId);
    if (tbody) tbody.innerHTML = `<tr><td colspan="${colspan}">${message}</td></tr>`;
}

async function loadDashboard() {
    try {
        const stats = await requestJson(API_BASE + 'dashboard');
        document.getElementById('agendamentosHoje').textContent = stats.agendamentosHoje ?? 0;
        document.getElementById('totalPacientes').textContent = stats.totalPacientes ?? 0;
        document.getElementById('totalProfissionais').textContent = stats.totalProfissionais ?? 0;
        document.getElementById('examesPendentes').textContent = stats.examesPendentes ?? 0;
    } catch (erro) {
        console.error('Erro ao carregar dashboard:', erro);
        showAlert('Erro ao carregar o dashboard', 'error');
    }

    loadProximosAgendamentos();
}

async function loadProximosAgendamentos() {
    const tbody = document.getElementById('proximosAgendamentos');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="5">Carregando...</td></tr>';

    try {
        const agendamentos = await requestJson(API_BASE + 'agendamentos');
        tbody.innerHTML = '';

        if (!agendamentos || agendamentos.length === 0) {
            setTableMessage('proximosAgendamentos', 5, 'Nenhum agendamento cadastrado.');
            return;
        }

        agendamentos.slice(0, 5).forEach(agendamento => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${formatDate(agendamento.data)}</td>
                <td>${agendamento.hora || ''}</td>
                <td>${agendamento.paciente || ''}</td>
                <td>${agendamento.profissional || ''}</td>
                <td><span class="badge badge-${agendamento.status === 'Realizado' ? 'success' : 'info'}">${agendamento.status || ''}</span></td>
            `;
            tbody.appendChild(row);
        });
    } catch (erro) {
        console.error('Erro ao carregar proximos agendamentos:', erro);
        setTableMessage('proximosAgendamentos', 5, 'Erro ao carregar agendamentos.');
    }
}

function loadAgendamentos() {
    loadAgendamentosTable();
    loadPacientesSelect('pacienteSelect');
    loadProfissionaisSelect('profissionalSelect');
}

async function loadAgendamentosTable() {
    const tbody = document.getElementById('tabelaAgendamentos');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="7">Carregando...</td></tr>';

    try {
        const agendamentos = await requestJson(API_BASE + 'agendamentos');
        tbody.innerHTML = '';

        if (!agendamentos || agendamentos.length === 0) {
            setTableMessage('tabelaAgendamentos', 7, 'Nenhum agendamento cadastrado.');
            return;
        }

        agendamentos.forEach(agendamento => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${agendamento.id}</td>
                <td>${formatDate(agendamento.data)}</td>
                <td>${agendamento.hora || ''}</td>
                <td>${agendamento.paciente || ''}</td>
                <td>${agendamento.profissional || ''}</td>
                <td>${agendamento.status || ''}</td>
                <td>
                    <button class="btn btn-small btn-warning" onclick="editarAgendamento(${agendamento.id})">Editar</button>
                    <button class="btn btn-small btn-danger" onclick="cancelarAgendamento(${agendamento.id})">Cancelar</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (erro) {
        console.error('Erro ao carregar agendamentos:', erro);
        setTableMessage('tabelaAgendamentos', 7, 'Erro ao carregar agendamentos.');
    }
}

async function loadPacientesSelect(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = '<option value="">Selecione um paciente</option>';

    try {
        const pacientes = await requestJson(API_BASE + 'pacientes');
        pacientes.forEach(paciente => {
            const option = document.createElement('option');
            option.value = paciente.id;
            option.textContent = paciente.nome;
            select.appendChild(option);
        });
    } catch (erro) {
        console.error('Erro ao carregar pacientes:', erro);
    }
}

async function loadProfissionaisSelect(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = '<option value="">Selecione um profissional</option>';

    try {
        const profissionais = await requestJson(API_BASE + 'profissionais');
        profissionais.forEach(profissional => {
            const option = document.createElement('option');
            option.value = profissional.id;
            option.textContent = profissional.nome;
            select.appendChild(option);
        });
    } catch (erro) {
        console.error('Erro ao carregar profissionais:', erro);
    }
}

function editarAgendamento(id) {
    showAlert(`Editar agendamento ${id} - funcionalidade em desenvolvimento`, 'info');
}

function cancelarAgendamento(id) {
    showAlert(`Cancelamento do agendamento ${id} ainda nao foi implementado no backend`, 'info');
}

function loadPacientes() {
    loadPacientesTable();
}

async function loadPacientesTable() {
    const tbody = document.getElementById('tabelaPacientes');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="7">Carregando...</td></tr>';

    try {
        const pacientes = await requestJson(API_BASE + 'pacientes');
        tbody.innerHTML = '';

        if (!pacientes || pacientes.length === 0) {
            setTableMessage('tabelaPacientes', 7, 'Nenhum paciente cadastrado.');
            return;
        }

        pacientes.forEach(paciente => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${paciente.id}</td>
                <td>${paciente.nome || ''}</td>
                <td>${formatCPF(paciente.cpf || '')}</td>
                <td>${formatDate(paciente.dataNascimento)}</td>
                <td>${paciente.telefone || '-'}</td>
                <td>${paciente.email || ''}</td>
                <td>
                    <button class="btn btn-small btn-primary" onclick="visualizarPaciente(${paciente.id})">Ver</button>
                    <button class="btn btn-small btn-warning" onclick="editarPaciente(${paciente.id})">Editar</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (erro) {
        console.error('Erro ao carregar pacientes:', erro);
        setTableMessage('tabelaPacientes', 7, 'Erro ao carregar pacientes.');
        showAlert('Erro ao carregar pacientes', 'error');
    }
}

function visualizarPaciente(id) {
    showAlert(`Visualizar paciente ${id} - funcionalidade em desenvolvimento`, 'info');
}

function editarPaciente(id) {
    showAlert(`Editar paciente ${id} - funcionalidade em desenvolvimento`, 'info');
}

function loadProfissionais() {
    loadProfissionaisTable();
}

async function loadProfissionaisTable() {
    const tbody = document.getElementById('tabelaProfissionais');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="7">Carregando...</td></tr>';

    try {
        const profissionais = await requestJson(API_BASE + 'profissionais');
        tbody.innerHTML = '';

        if (!profissionais || profissionais.length === 0) {
            setTableMessage('tabelaProfissionais', 7, 'Nenhum profissional encontrado.');
            return;
        }

        profissionais.forEach(profissional => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${profissional.id}</td>
                <td>${profissional.nome || ''}</td>
                <td>${profissional.registro || ''}</td>
                <td>${profissional.tipo || ''}</td>
                <td>${profissional.especialidade || ''}</td>
                <td>${profissional.status || ''}</td>
                <td><button class="btn btn-small btn-warning" onclick="editarProfissional(${profissional.id})">Editar</button></td>
            `;
            tbody.appendChild(row);
        });
    } catch (erro) {
        console.error('Erro ao carregar profissionais:', erro);
        setTableMessage('tabelaProfissionais', 7, 'Erro ao carregar profissionais.');
    }
}

function editarProfissional(id) {
    showAlert(`Editar profissional ${id} - funcionalidade em desenvolvimento`, 'info');
}

function loadConsultas() {
    loadConsultasTable();
    loadAgendamentosSelect('agendamentoSelect');
}

async function loadConsultasTable() {
    const tbody = document.getElementById('tabelaConsultas');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="6">Carregando...</td></tr>';

    try {
        const consultas = await requestJson(API_BASE + 'consultas');
        tbody.innerHTML = '';

        if (!consultas || consultas.length === 0) {
            setTableMessage('tabelaConsultas', 6, 'Nenhuma consulta registrada.');
            return;
        }

        consultas.forEach(consulta => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${consulta.id}</td>
                <td>${formatDateTime(consulta.data)}</td>
                <td>${consulta.paciente || ''}</td>
                <td>${consulta.profissional || ''}</td>
                <td>${consulta.queixa || ''}</td>
                <td><button class="btn btn-small btn-primary" onclick="visualizarConsulta(${consulta.id})">Ver</button></td>
            `;
            tbody.appendChild(row);
        });
    } catch (erro) {
        console.error('Erro ao carregar consultas:', erro);
        setTableMessage('tabelaConsultas', 6, 'Erro ao carregar consultas.');
    }
}

async function loadAgendamentosSelect(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = '<option value="">Selecione um agendamento</option>';

    try {
        const agendamentos = await requestJson(API_BASE + 'agendamentos');
        agendamentos
            .filter(agendamento => agendamento.status !== 'Realizado')
            .forEach(agendamento => {
                const option = document.createElement('option');
                option.value = agendamento.id;
                option.textContent = `${agendamento.paciente} - ${formatDate(agendamento.data)} ${agendamento.hora}`;
                select.appendChild(option);
            });
    } catch (erro) {
        console.error('Erro ao carregar agendamentos:', erro);
    }
}

function visualizarConsulta(id) {
    showAlert(`Visualizar consulta ${id} - funcionalidade em desenvolvimento`, 'info');
}

document.addEventListener('DOMContentLoaded', function() {
    showSection('dashboard');

    const formAgendamento = document.getElementById('formAgendamento');
    if (formAgendamento) {
        formAgendamento.addEventListener('submit', async function(e) {
            e.preventDefault();

            const dados = {
                idPaciente: Number(document.getElementById('pacienteSelect').value),
                idProfissional: Number(document.getElementById('profissionalSelect').value),
                dataConsulta: document.getElementById('dataConsulta').value,
                horaConsulta: document.getElementById('horaConsulta').value,
                motivoConsulta: document.getElementById('motivoConsulta').value,
                observacoes: ''
            };

            try {
                const resultado = await requestJson(API_BASE + 'agendamentos', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dados)
                });

                showAlert(resultado.mensagem || 'Agendamento realizado com sucesso!', 'success');
                this.reset();
                loadAgendamentos();
                loadDashboard();
            } catch (erro) {
                console.error('Erro ao cadastrar agendamento:', erro);
                showAlert(`Erro ao cadastrar agendamento: ${erro.message}`, 'error');
            }
        });
    }

    const formPaciente = document.getElementById('formPaciente');
    if (formPaciente) {
        formPaciente.addEventListener('submit', async function(e) {
            e.preventDefault();

            const dados = {
                nome: document.getElementById('nomePaciente').value,
                cpf: document.getElementById('cpfPaciente').value,
                dataNascimento: document.getElementById('dataNascimento').value,
                sexo: document.getElementById('sexoPaciente').value,
                telefone: document.getElementById('telefonePaciente').value,
                email: document.getElementById('emailPaciente').value
            };

            try {
                const resultado = await requestJson(API_BASE + 'pacientes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dados)
                });

                showAlert(resultado.mensagem || 'Paciente cadastrado com sucesso!', 'success');
                this.reset();
                loadPacientes();
                loadDashboard();
            } catch (erro) {
                console.error('Erro ao cadastrar paciente:', erro);
                showAlert(`Erro ao cadastrar paciente: ${erro.message}`, 'error');
            }
        });
    }

    const formProfissional = document.getElementById('formProfissional');
    if (formProfissional) {
        formProfissional.addEventListener('submit', async function(e) {
            e.preventDefault();

            const dados = {
                nome: document.getElementById('nomeProfissional').value,
                cpf: document.getElementById('cpfProfissional').value,
                registro: document.getElementById('numeroRegistro').value,
                tipo: document.getElementById('tipoProfissional').value,
                especialidade: document.getElementById('especialidade').value,
                email: document.getElementById('emailProfissional').value
            };

            try {
                const resultado = await requestJson(API_BASE + 'profissionais', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dados)
                });

                showAlert(resultado.mensagem || 'Profissional cadastrado com sucesso!', 'success');
                this.reset();
                loadProfissionais();
                loadDashboard();
            } catch (erro) {
                console.error('Erro ao cadastrar profissional:', erro);
                showAlert(`Erro ao cadastrar profissional: ${erro.message}`, 'error');
            }
        });
    }

    const formConsulta = document.getElementById('formConsulta');
    if (formConsulta) {
        formConsulta.addEventListener('submit', async function(e) {
            e.preventDefault();

            const dados = {
                idAgendamento: Number(document.getElementById('agendamentoSelect').value),
                queixaPrincipal: document.getElementById('queixaPrincipal').value,
                anamnese: document.getElementById('anamnese').value,
                achadosExameFisico: document.getElementById('achadosExame').value,
                hipoteseDiagnostica: document.getElementById('diagnostico').value,
                notasClinicas: '',
                peso: document.getElementById('peso').value,
                altura: document.getElementById('altura').value,
                pressaoArterial: document.getElementById('pressao').value,
                frequenciaCardiaca: document.getElementById('frequencia').value,
                temperatura: ''
            };

            try {
                const resultado = await requestJson(API_BASE + 'consultas', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(dados)
                });

                showAlert(resultado.mensagem || 'Consulta registrada com sucesso!', 'success');
                this.reset();
                loadConsultas();
                loadAgendamentosTable();
                loadDashboard();
            } catch (erro) {
                console.error('Erro ao registrar consulta:', erro);
                showAlert(`Erro ao registrar consulta: ${erro.message}`, 'error');
            }
        });
    }

    const logoutLink = document.querySelector('.logout');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(e) {
            e.preventDefault();
            if (confirm('Deseja sair do sistema?')) {
                showAlert('Desconectado com sucesso', 'success');
            }
        });
    }
});
