package com.powerlogic.consumer.controller.jsf;

import javax.inject.Inject;

//import com.empresa.cargosalario.facade.ICargoService;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.rhavancado.ws.BuscaFuncionarioWS;
import com.powerlogic.rhavancado.ws.BuscaFuncionarioWSService;
import com.powerlogic.rhavancado.ws.Funcionario;


@PlcConfigForm (
formPattern=FormPattern.Ctl,
formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/buscafuncionario")
)
@SPlcMB
@PlcUriIoC("buscafuncionario")
@PlcHandleException
public class BuscafuncionarioMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	//@Inject
	//protected ICargoService cargoService;
	
	private String cpf;
	
	private Funcionario funcionario;
	     		
	public void buscar() {
		BuscaFuncionarioWSService servico = new BuscaFuncionarioWSService();
		BuscaFuncionarioWS port = servico.getBuscaFuncionarioWSPort();
		Funcionario f = port.busca(cpf);
		setFuncionario(f);
	}
	
	/*public void buscar() {
		setFuncionario(cargoService.buscaFuncionario(cpf));	
	}*/

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}