package org.agenda.fabricas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FabricaConexaoJdbc {

	public static Connection criarConexao() throws SQLException, IOException {
		
		Connection conexao = null;
		
		//conexao = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tw_jdbc?serverTimezone=UTC", "root", "");
		
		Properties props = new Properties();
		InputStream is = FabricaConexaoJdbc.class.getClassLoader().getResourceAsStream("application.properties");
		
		if(is == null) {
			throw new FileNotFoundException("O arquivo 'application.properties' n�o foi encontrado");
		}

		props.load(is);
		conexao = DriverManager.getConnection(props.getProperty("urlConexao"), props.getProperty("usuarioConexao"), props.getProperty("senhaConexao"));
		
		return conexao;
		
	}
	
}
