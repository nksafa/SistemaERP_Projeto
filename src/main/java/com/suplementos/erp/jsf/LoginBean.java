package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Produto; // <-- NOVO IMPORT
import com.suplementos.erp.model.TipoUsuario;
import com.suplementos.erp.model.Usuario;
import com.suplementos.erp.repository.ProdutoRepository; // <-- NOVO IMPORT
import com.suplementos.erp.repository.UsuarioRepository;
import com.suplementos.erp.service.EstoqueService; // <-- NOVO IMPORT
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String errorMessage;
    private Usuario usuarioLogado;

    // <-- MUDANÇA: Novo atributo para guardar os alertas na sessão
    private List<Produto> alertasDeEstoque;

    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    @PostConstruct
    public void init() {
        // Crie e salve os usuários de teste apenas se a tabela estiver vazia
        List<Usuario> usuarios = usuarioRepository.buscarTodos();
        if (usuarios.isEmpty()) {
            usuarioRepository.salvar(0, new Usuario(0, "admin", "senhaadmin", TipoUsuario.ADMINISTRADOR));
            usuarioRepository.salvar(0, new Usuario(0, "gerente", "senhagerente", TipoUsuario.GERENTE));
            usuarioRepository.salvar(0, new Usuario(0, "funcionario", "senhafunc", TipoUsuario.FUNCIONARIO));
        }
    }

    // <-- MUDANÇA: Novo método para carregar/atualizar os alertas
    public void carregarAlertasDeEstoque() {
        // Instancia o serviço e busca os produtos com estoque baixo
        EstoqueService estoqueService = new EstoqueService(new ProdutoRepository());
        this.alertasDeEstoque = estoqueService.getProdutosComEstoqueBaixo();
    }

    public String login() {
        Usuario usuario = usuarioRepository.buscarPorNome(username);

        if (usuario != null && usuario.getSenha().equals(password)) {
            this.usuarioLogado = usuario;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogado", usuarioLogado);

            // <-- MUDANÇA: A lógica do Flash Scope foi removida מכאן
            // Agora, apenas chamamos o método para atualizar a lista de alertas
            carregarAlertasDeEstoque();

            return "dashboard?faces-redirect=true";
        } else {
            errorMessage = "Usuário ou senha incorretos.";
            FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage(javax.faces.application.FacesMessage.SEVERITY_ERROR, "Erro", "Usuário ou senha incorretos."));
            return "login?faces-redirect=true";
        }
    }

    // <-- MUDANÇA: Novo getter para a lista de alertas
    public List<Produto> getAlertasDeEstoque() {
        return alertasDeEstoque;
    }

    // Getters e Setters existentes
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getErrorMessage() { return errorMessage; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
}