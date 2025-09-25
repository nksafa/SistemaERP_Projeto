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
    private List<Usuario> listaUsuariosFiltrada;


    @PostConstruct
    public void init() {
        usuarioRepository = new UsuarioRepository();
        usuario = new Usuario();
        carregarUsuarios();
    }

    private void carregarUsuarios() {
        listaUsuarios = usuarioRepository.buscarTodos();
    }

    // Em UsuarioBean.java

    public void salvar() {
        if (usuario.getCodigo() != 0 && (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())) {

            Usuario usuarioDoBanco = usuarioRepository.buscarPorId(usuario.getCodigo());
            usuario.setSenha(usuarioDoBanco.getSenha());
        } else if (usuario.getCodigo() == 0 && (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A senha é obrigatória para novos usuários."));
            return;
        }


        usuarioRepository.salvar(usuario.getCodigo(), usuario);
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
        Usuario usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");

        if (usuarioLogado != null && usuarioLogado.getCodigo() == usuarioParaRemover.getCodigo()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Você não pode remover seu próprio usuário."));
            return;
        }

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
    public List<Usuario> getListaUsuariosFiltrada() {
        return listaUsuariosFiltrada;
    }
    public void setListaUsuariosFiltrada(List<Usuario> listaUsuariosFiltrada) {
        this.listaUsuariosFiltrada = listaUsuariosFiltrada;
    }
}