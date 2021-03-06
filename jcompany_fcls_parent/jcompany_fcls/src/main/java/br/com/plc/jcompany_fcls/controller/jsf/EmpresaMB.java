/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller.jsf;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.plc.jcompany_fcls.entity.empresa.EmpresaEntity;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
		entity = br.com.plc.jcompany_fcls.entity.empresa.EmpresaEntity.class,
		details = {
			@PlcConfigDetail(
					clazz 			= br.com.plc.jcompany_fcls.entity.empresa.EnderecoEntity.class,
					collectionName 	= "endereco", 
					numNew 			= 1,
					 
					onDemand 		= true,
					subDetail 		= @PlcConfigSubDetail(
										clazz 			= br.com.plc.jcompany_fcls.entity.empresa.ComplementoEntity.class,
										collectionName 	= "complemento", 
										numNew 			= 1
										)
			),
			@PlcConfigDetail(
					clazz 			= br.com.plc.jcompany_fcls.entity.empresa.ParceirosEntity.class,
					collectionName 	= "parceiros", 
					numNew 			= 1,
					 
					onDemand 		= true
			)
		}
	)


@SPlcMB
@PlcUriIoC("empresa")	
@PlcHandleException
@ConversationScoped
public class EmpresaMB extends AppMB {

	@Inject
	protected transient Logger log;
 

	/**
	 * Entidade da aÃ§Ã£o injetado pela CDI
	 */
	@Produces  @Named("empresa") 
	public EmpresaEntity criaentityPlc() {
		if (this.entityPlc==null) {
			this.entityPlc = new EmpresaEntity();
			this.newEntity();
		}
		return (EmpresaEntity)this.entityPlc;
	}
	

}
