package com.suplementos.erp.jsf;

import com.suplementos.erp.model.AuditLog; // IMPORT NOVO
import com.suplementos.erp.model.TipoUsuario;
import com.suplementos.erp.model.Usuario;
import com.suplementos.erp.repository.AuditLogRepository; // IMPORT NOVO
import com.suplementos.erp.repository.UsuarioRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private List<Usuario> listaUsuarios;
    private List<Usuario> listaUsuariosFiltrada;

    private UsuarioRepository usuarioRepository;
    private AuditLogRepository auditLogRepository; // Repositório de Logs

    @PostConstruct
    public void init() {
        usuarioRepository = new UsuarioRepository();
        auditLogRepository = new AuditLogRepository(); // Inicializa o repositório
        usuario = new Usuario();
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        listaUsuarios = usuarioRepository.buscarTodos();
    }

    // Método auxiliar para pegar o usuário atual da sessão
    private Usuario getUsuarioLogado() {
        return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    }

    public void salvar() {
        // Validações de senha...
        if (usuario.getCodigo() != 0 && (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())) {
            Usuario usuarioDoBanco = usuarioRepository.buscarPorId(usuario.getCodigo());
            usuario.setSenha(usuarioDoBanco.getSenha());
        } else if (usuario.getCodigo() == 0 && (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A senha é obrigatória para novos usuários."));
            return;
        }

        // --- LOGICA DE AUDITORIA ---
        String acao = (usuario.getCodigo() == 0) ? "CRIAR" : "EDITAR";
        String detalhes = "Usuário: " + usuario.getNome() + " (" + usuario.getTipo() + ")";
        // ---------------------------

        usuarioRepository.salvar(usuario.getCodigo(), usuario);

        // --- SALVA O LOG APÓS O SUCESSO DA OPERAÇÃO ---
        auditLogRepository.salvar(new AuditLog(getUsuarioLogado().getNome(), acao, "Usuario", detalhes));
        // ----------------------------------------------

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário salvo."));

        usuario = new Usuario();
        carregarUsuarios();
    }

    public void editar(Usuario usuarioSelecionado) {
        this.usuario = usuarioSelecionado;
        this.usuario.setSenha(null);
    }

    public void remover(Usuario usuarioParaRemover) {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado != null && usuarioLogado.getCodigo() == usuarioParaRemover.getCodigo()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Você não pode remover seu próprio usuário."));
            return;
        }

        usuarioRepository.remover(usuarioParaRemover.getCodigo());

        // --- LOG DE REMOÇÃO ---
        auditLogRepository.salvar(new AuditLog(usuarioLogado.getNome(), "REMOVER", "Usuario",
                "Removeu o usuário: " + usuarioParaRemover.getNome()));
        // ----------------------

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário removido."));
        carregarUsuarios();
    }

    public TipoUsuario[] getTiposDeUsuario() { return TipoUsuario.values(); }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Usuario> getListaUsuarios() { return listaUsuarios; }
    public List<Usuario> getListaUsuariosFiltrada() { return listaUsuariosFiltrada; }
    public void setListaUsuariosFiltrada(List<Usuario> listaUsuariosFiltrada) { this.listaUsuariosFiltrada = listaUsuariosFiltrada; }
}