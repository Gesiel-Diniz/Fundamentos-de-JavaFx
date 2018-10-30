package org.agenda;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.agenda.entidades.Contato;
import org.agenda.repositorios.impl.ContatoRepositorio;
import org.agenda.repositorios.interfaces.AgendaRepositorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController implements Initializable{
	
	@FXML
	private TableView<Contato> tabelaContatos;
	
	@FXML
	private Button botaoInserir;
	
	@FXML
	private Button botaoAlterar;
	
	@FXML
	private Button botaoExcluir;
	
	@FXML
	private TextField txfNome;
	
	@FXML
	private TextField txfIdeda;
	
	@FXML
	private TextField txfTelefone;
	
	@FXML
	private Button botaoSalvar;
	
	@FXML
	private Button botaoCancelar;
	
	private Boolean ehInserir;
	
	private Contato contatoSelecionado;
	
	
	public void botaoCancelar_Action() {
	
		habilitarEdicaoAgenda(false);
		
		this.tabelaContatos.getSelectionModel().selectFirst();
		
	}
	
	public void botaoAlterar_Action() {
		
		this.txfNome.setText(contatoSelecionado.getNome());
		this.txfIdeda.setText(String.valueOf(contatoSelecionado.getIdade()));
		this.txfTelefone.setText(contatoSelecionado.getTelefone());
		
		this.ehInserir = false;
		habilitarEdicaoAgenda(true);
		
	}
	
	
	public void botaoInserir_Action() {
		
		this.ehInserir = true;
		
		this.txfNome.setText("");
		this.txfIdeda.setText("");
		this.txfTelefone.setText("");
		
		habilitarEdicaoAgenda(true);
		
	}
	
	public void botaoExcluir_Action() {

		Alert confirm = new Alert(AlertType.CONFIRMATION);

		confirm.setTitle("Excluir");
		confirm.setHeaderText("Confirma��o da exclus�o");
		confirm.setContentText("Deseja realmente excluir esse contato?");

		Optional<ButtonType> resultadoConfirm = confirm.showAndWait();
		if (resultadoConfirm.isPresent() && resultadoConfirm.get() == ButtonType.OK) {

			AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
			repositorioContato.excluir(contatoSelecionado);
			carregarTabelaContatos();

		}

	}
	
	public void botaoSalvar_Action() {
		
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		Contato contato = new Contato();
		
		contato.setNome(txfNome.getText());
		contato.setIdade(Integer.parseInt(txfIdeda.getText()));
		contato.setTelefone(txfTelefone.getText());
		
		if(this.ehInserir) {
			repositorioContato.inserir(contato);
		}else {
			repositorioContato.atualizar(contato);
		}

		habilitarEdicaoAgenda(false);
		carregarTabelaContatos();
		
		this.tabelaContatos.getSelectionModel().selectFirst();
		
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		habilitarEdicaoAgenda(false);
		
//		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contato>() {
//			@Override
//			public void changed(ObservableValue<? extends Contato> observable, Contato oldValue, Contato newValue) {
//				if(newValue != null) {
//					txfNome.setText(newValue.getNome());
//					txfIdeda.setText(String.valueOf(newValue.getIdade()));
//					txfTelefone.setText(newValue.getTelefone());
//				}
//			}
//		});
		
		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener((observable, contatoAntigo, contatoNovo)->{
			if(contatoNovo != null) {
				this.txfNome.setText(contatoNovo.getNome());
				this.txfIdeda.setText(String.valueOf(contatoNovo.getIdade()));
				this.txfTelefone.setText(contatoNovo.getTelefone());
				this.contatoSelecionado = contatoNovo;
			}
		});
		
		carregarTabelaContatos();
		
	}
	
	
	private void carregarTabelaContatos() {
		
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		List<Contato> contatos = repositorioContato.selecionar();
		
		if(contatos.isEmpty()) {
			Contato contato = new Contato();
			
			contato.setNome("TreinaWeb");
			contato.setIdade(12);
			contato.setTelefone("123456");
			
			contatos.add(contato);
			
		}
		
		ObservableList<Contato> contatosObservableList = FXCollections.observableList(contatos);
		
		this.tabelaContatos.getItems().setAll(contatosObservableList);
 		
	}
	
	private void habilitarEdicaoAgenda(Boolean edicaoEstaHabilitada) {
		
		this.txfNome.setDisable(!edicaoEstaHabilitada);
		this.txfIdeda.setDisable(!edicaoEstaHabilitada);
		this.txfTelefone.setDisable(!edicaoEstaHabilitada);
		this.botaoSalvar.setDisable(!edicaoEstaHabilitada);
		this.botaoCancelar.setDisable(!edicaoEstaHabilitada);
		
		this.botaoInserir.setDisable(edicaoEstaHabilitada);
		this.botaoExcluir.setDisable(edicaoEstaHabilitada);
		this.botaoAlterar.setDisable(edicaoEstaHabilitada);
		this.tabelaContatos.setDisable(edicaoEstaHabilitada);
		
	}
	
}