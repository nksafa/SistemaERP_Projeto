package com.suplementos.erp.jsf;

import com.suplementos.erp.model.TipoUsuario;
import com.suplementos.erp.model.Usuario;
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
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init() {
        usuarioRepository = new UsuarioRepository();
        usuario = new Usuario();
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        listaUsuarios = usuarioRepository.buscarTodos();
    }

    public void salvar() {
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A senha não pode estar em branco."));
            return;
        }

        // CORRIGIDO: de getId() para getCodigo()
        usuarioRepository.salvar(usuario.getCodigo(), usuario);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário salvo."));

        usuario = new Usuario();
        carregarUsuarios();
    }

    public void editar(Usuario usuarioSelecionado) {
        this.usuario = usuarioSelecionado;
    }

    public void remover(Usuario usuarioParaRemover) {
        Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");

        if (usuarioLogado != null && usuarioLogado.getCodigo() == usuarioParaRemover.getCodigo()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Você não pode remover seu próprio usuário."));
            return;
        }

        // CORRIGIDO: de getId() para getCodigo()
        usuarioRepository.remover(usuarioParaRemover.getCodigo());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Usuário removido."));
        carregarUsuarios();
    }

    public TipoUsuario[] getTiposDeUsuario() {
        return TipoUsuario.values();
    }

    // Getters e Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}