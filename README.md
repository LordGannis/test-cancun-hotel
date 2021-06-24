# Cancun Hotel

# 1. Introduction
Cancun Hotel is a back-end aggregation of microservices with the goal to attend to a book system for the Cancun Hotel.
Currently the hotel has only one room available!

# 2. Acceptance Criterias
We expect from the system:
* Allow user to see his bookings;
* Allow user to see available days for bookings;
* Allow user to book an available range of days from one to up to three days;
* Allow user to modify his current bookings; and
* Allow user to cancel his current bookings.

# 3. Fluxo
<!BDD.INICIO>

## Tag
@desenvolvimento

## Funcionalidade: Testar Recursos do Sistema

## Cenario: Verificar situa��o Banco de Dados Dispon�vel (http://endereco-sfpge-servicos/api/v1/monitoramento/banco-dados.xml)
  **Dado** que o sistema tenha executado a consulta ao banco de dados (([RN01](#RN01))   
  **E** o haja um retorno de sucesso  
  **Ent�o** o sistema dever� gerar o arquivo xml com o status de sucesso (([RN02](#RN02))  
  
## Cenario: Verificar situa��o WebService Banco do Brasil Dispon�vel (http://endereco-sfpge-servicos/api/v1/monitoramento/banco-brasil.xml)
  **Dado** que o sistema tenha executado a consulta ao WS do Banco do Brasil (([RN03](#RN03))   
  **E** o haja um retorno de sucesso  
  **Ent�o** o sistema dever� gerar o arquivo xml com o status de sucesso  
  
## Cenario: Verificar situa��o Front End Dispon�vel (http://endereco-sfpge-front-end/monitoramento.xml)
  **Dado** que o sistema tenha executado a consulta ao Fron-end (([RN04](#RN04))   
  **E** o haja um retorno de sucesso   
  **Ent�o** o sistema dever� gerar o arquivo xml com o status de sucesso  
  
<!BDD.FIM>

## Regras de Neg�cio
* <a name="RN01"></a>**RN01**  Consultar Banco de Dados:
*1. Executar consulta na tabela tb_parametro obtendo �nico registro e sem condi��o;
*2. Caso encontre o registro (indicando sucesso) ou uma falha venha a ocorrer, preparar XML de resultado do teste por recurso, aplicando [RN02](#RN02) informando (tratar outras tags conforme definido na regra):
*	- nome: Teste de conex�o com o banco de dados do sistema (PostgreSQL);
*	- descricao: Realiza a sele��o do par�metro �nico (tb_parametro).
*   - Status: OK ou ERRO;

*<a name="RN02" />**RN2**. Padr�o XML para cada recurso
*	<teste>
*		<nome>Informar um nome para o recurso</nome>
*		<descricao>Informar descritivo sobre o teste realizado</descricao>
*		<status>Informar *OK* quando teste executado com sucesso, caso contr�rio *ERRO*.</status>
*		<msgerro>Informar detalhes sobre o erro (caso exista), normalmente o erro pode ser obtido atrav�s da falha ao executar o teste do recurso ou mesmo uma mensagem espec�fica obtida pelo servi�o.</msgerro>
*	</teste>*

*<a name="RN03"></a>RN03.  Verificar situa��o WebService Banco do Brasil
*1. Efetuar chamada ao servi�o dispon�vel no webservice do Banco do Brasil (ver API, se necess�rio);
*2. Caso o resultado seja igual ao c�digo 200 - Sucesso, preparar XML de resultado do teste por recurso, aplicando [RN02](#RN02) informando:
*	- nome: Teste de comunica��o com o WebService do Banco do Brasil;
*	- descricao: Teste de comunica��o com o WebService do Banco do Brasil;
*    - Status: OK;

*3. Caso contr�rio, preparar XML de resultado do teste por recurso, aplicando [RN02](#RN02) informando:
*	- nome: Teste de comunica��o com o WebService do Banco do Brasil;
*	- descricao: Teste de comunica��o com o WebService do Banco do Brasil;
*	- msgerro: N�o foi poss�vel efetuar a chamada ao webservice. Caso tenha ocorrido uma falha de execu��o, adicionar qual foi a mensagem de erro obtida atrav�s da exce��o, exemplo "Detalhe Exception.Message". 
*   - Status: ERRO;

*<a name="RN04"></a>RN04.  Verificar situa��o front-end
*1. Retornar arquivo est�tico com o conte�do abaixo:
*<gopmp>
*	<titulo>GOPMP - Monitoramento do sistema SFPGE</titulo>
*	<teste>
*		<nome>Teste de comunica��o com o SFPGE Front-End</nome>
*		<descricao>Teste de comunica��o com o servi�o do SFPGE Front-End</descricao>
*		<status>OK</status>		
*	</teste>	
*</gopmp>

# 4. Casos de Uso Relacionados

# 5. Prot�tipo de Interface

# 6. Mensagens ([ApplicationMessages](src/main/resources/ApplicationMessages.properties))

## 7. Projeto

### 7.1. Interface

### 7.2. Servico

### 7.3 Demais Artefatos
