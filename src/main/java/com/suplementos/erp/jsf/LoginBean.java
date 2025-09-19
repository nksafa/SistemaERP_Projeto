package com.suplementos.erp.jsf;

import com.suplementos.erp.model.Produto;
import com.suplementos.erp.model.TipoUsuario;
import com.suplementos.erp.model.Usuario;
import com.suplementos.erp.repository.ProdutoRepository;
import com.suplementos.erp.repository.UsuarioRepository;
import com.suplementos.erp.service.EstoqueService;
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

    // Método que busca os dados
    public void carregarAlertasDeEstoque() {
        EstoqueService estoqueService = new EstoqueService(new ProdutoRepository());
        // A busca e atribuição agora é a PRIMEIRA E ÚNICA coisa que este método faz.
        this.alertasDeEstoque = estoqueService.getProdutosComEstoqueBaixo();
    }

    // O Getter "inteligente" que garante que os dados sejam carregados
    public List<Produto> getAlertasDeEstoque() {
        carregarAlertasDeEstoque();
        return alertasDeEstoque;
    }

    public String login() {
        Usuario usuario = usuarioRepository.buscarPorNome(username);

        if (usuario != null && usuario.getSenha().equals(password)) {
            this.usuarioLogado = usuario;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogado", usuarioLogado);

            // A chamada foi removida daqui, pois o getter agora é responsável por isso.
            return "dashboard?faces-redirect=true";
        } else {
            errorMessage = "Usuário ou senha incorretos.";
            FacesContext.getCurrentInstance().addMessage(null, new javax.faces.application.FacesMessage(javax.faces.application.FacesMessage.SEVERITY_ERROR, "Erro", "Usuário ou senha incorretos."));
            return "login?faces-redirect=true";
        }
    }

    // Getters e Setters existentes
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getErrorMessage() { return errorMessage; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
}