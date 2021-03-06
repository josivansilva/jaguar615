
package com.powerlogic.rhavancado.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "BuscaFuncionarioWS", targetNamespace = "http://ws.rhavancado.powerlogic.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BuscaFuncionarioWS {


    /**
     * 
     * @param cpf
     * @return
     *     returns com.powerlogic.rhavancado.ws.Funcionario
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "busca", targetNamespace = "http://ws.rhavancado.powerlogic.com/", className = "com.powerlogic.rhavancado.ws.Busca")
    @ResponseWrapper(localName = "buscaResponse", targetNamespace = "http://ws.rhavancado.powerlogic.com/", className = "com.powerlogic.rhavancado.ws.BuscaResponse")
    public Funcionario busca(
        @WebParam(name = "cpf", targetNamespace = "")
        String cpf);

}
