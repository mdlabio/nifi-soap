/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.processors.soap;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;

public class PostSOAPTest {

    private TestRunner testRunner;
    // @Rule
    // public MockServerRule mockServerRule = new MockServerRule(1080,this);

    private Verify _verify = new Verify();

    private Given _given = new Given();

    private static ClientAndServer mockServer;
    // private MockServerClient mockServerClient;

    private static String wsdl = "<?xml version=\"1.0\"?>\n" + "<definitions name=\"TestService\"\n"
            + "             targetNamespace=\"http://localhost.com/stockquote.wsdl\"\n"
            + "             xmlns:tns=\"http://localhost.com/stockquote.wsdl\"\n"
            + "             xmlns:xsd1=\"http://localhost.com/stockquote.xsd\"\n"
            + "             xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"\n" + "             xmlns=\"http://schemas.xmlsoap.org/wsdl/\">\n"
            + "\n" + "  <types>\n" + "    <schema targetNamespace=\"http://localhost.com/stockquote.xsd\"\n"
            + "            xmlns=\"http://www.w3.org/2000/10/XMLSchema\">\n" + "      <element name=\"TradePriceRequest\">\n"
            + "        <complexType>\n" + "          <all>\n" + "            <element name=\"tickerSymbol\" type=\"string\"/>\n"
            + "          </all>\n" + "        </complexType>\n" + "      </element>\n" + "      <element name=\"TradePrice\">\n"
            + "         <complexType>\n" + "           <all>\n" + "             <element name=\"price\" type=\"float\"/>\n" + "           </all>\n"
            + "         </complexType>\n" + "      </element>\n" + "    </schema>\n" + "  </types>\n" + "\n"
            + "  <message name=\"GetLastTradePriceInput\">\n" + "    <part name=\"body\" element=\"xsd1:TradePriceRequest\"/>\n" + "  </message>\n"
            + "\n" + "  <message name=\"GetLastTradePriceOutput\">\n" + "    <part name=\"body\" element=\"xsd1:TradePrice\"/>\n" + "  </message>\n"
            + "\n" + "  <portType name=\"StockQuotePortType\">\n" + "    <operation name=\"GetLastTradePrice\">\n"
            + "      <input message=\"tns:GetLastTradePriceInput\"/>\n" + "      <output message=\"tns:GetLastTradePriceOutput\"/>\n"
            + "    </operation>\n" + "  </portType>\n" + "\n" + "  <binding name=\"StockQuoteSoapBinding\" type=\"tns:StockQuotePortType\">\n"
            + "    <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>\n"
            + "    <operation name=\"GetLastTradePrice\">\n" + "      <soap:operation soapAction=\"http://localhost.com/GetLastTradePrice\"/>\n"
            + "      <input>\n" + "        <soap:body use=\"literal\"/>\n" + "      </input>\n" + "      <output>\n"
            + "        <soap:body use=\"literal\"/>\n" + "      </output>\n" + "    </operation>\n" + "  </binding>\n" + "\n"
            + "  <service name=\"StockQuoteService\">\n" + "    <documentation>My first service</documentation>\n"
            + "    <port name=\"StockQuotePort\" binding=\"tns:StockQuoteSoapBinding\">\n"
            + "      <soap:address location=\"http://localhost.com/stockquote\"/>\n" + "    </port>\n" + "  </service>\n" + "\n" + "</definitions>";

    @BeforeClass
    public static void setup() {
        mockServer = startClientAndServer(1080);

        // mockServerClient = new MockServerClient("127.0.0.1", 1080);

    }

    @AfterClass
    public static void tearDown() {
        mockServer.stop();
    }

    @Before
    public void init() {
        testRunner = TestRunners.newTestRunner(PostSOAP.class);

        // mockServerClient = new MockServerClient("127.0.0.1", 1080);
    }

    @After
    public void after() {
        testRunner.shutdown();
    }

    @Test
    public void test_ObterChaveAcessoLC_Integracao() throws Exception {
        // testRunner.setProperty(PostSOAP.CONNECTION_TIMEOUT, "");
        testRunner.setProperty(PostSOAP.ENDPOINT_URL, "http://cloudservices.bematech.com.br/Live40/LiveConnector/FacadeIntegracao.svc");
        testRunner.setProperty(PostSOAP.METHOD_NAME, "http://LiveConnector/IFacadeIntegracao/ObterChaveAcessoLC_Integracao");
        testRunner.setProperty(PostSOAP.PAYLOAD_XML, _given.getObterChaveAcessoLC_IntegracaoPayloadXml());
        // testRunner.setProperty(PostSOAP.PASSWORD, "");
        // testRunner.setProperty(PostSOAP.SO_TIMEOUT, "");
        // testRunner.setProperty(PostSOAP.USER_AGENT, "");
        // testRunner.setProperty(PostSOAP.USER_NAME, "");
        testRunner.setProperty(PostSOAP.WSDL_URL, "http://cloudservices.bematech.com.br/Live40/LiveConnector/FacadeIntegracao.svc?wsdl");

        testRunner.run();

        final List<MockFlowFile> flowFiles = testRunner.getFlowFilesForRelationship(PostSOAP.REL_SUCCESS);
        Assert.assertNotNull(flowFiles);

        _verify.printArgumentsAndContent(flowFiles.get(0));

    }

    @Test
    public void test_RecuperarCupomFiscalLC_Integracao() throws Exception {
        // testRunner.setProperty(PostSOAP.CONNECTION_TIMEOUT, "");
        testRunner.setProperty(PostSOAP.ENDPOINT_URL, "http://cloudservices.bematech.com.br/Live40/LiveConnector/FacadeIntegracao.svc");
        testRunner.setProperty(PostSOAP.METHOD_NAME, "http://LiveConnector/IFacadeIntegracao/RecuperarCupomFiscalLC_Integracao");
        // testRunner.setProperty(PostSOAP.PASSWORD, "");
        // testRunner.setProperty(PostSOAP.SO_TIMEOUT, "");
        // testRunner.setProperty(PostSOAP.USER_AGENT, "");
        // testRunner.setProperty(PostSOAP.USER_NAME, "");
        testRunner.setProperty(PostSOAP.WSDL_URL, "http://cloudservices.bematech.com.br/Live40/LiveConnector/FacadeIntegracao.svc?wsdl");

        // testRunner.setProperty(PostSOAP.PARAMETERS_ORDER, "protectedDomain, publicDomain, CodigoSistemaSatelite, Usuario, Senha");

        // ATT RAIZ
        testRunner.setProperty("protectedDomain", "00000000-0000-0000-0000-000000000000");
        testRunner.setProperty("publicDomain", "00000000-0000-0000-0000-000000000000");
        // ATT REN (http://schemas.datacontract.org/2004/07/Rentech.Framework.Data)
        testRunner.setProperty("ClassID", "00000000-0000-0000-0000-000000000000");
        testRunner.setProperty("Creation", "0001-01-01T00:00:00");
        testRunner.setProperty("LastUpdate", "0001-01-01T00:00:00");
        testRunner.setProperty("ObjectID", "00000000-0000-0000-0000-000000000000");
        testRunner.setProperty("OwnerID", "00000000-0000-0000-0000-000000000000");
        testRunner.setProperty("UserID", "00000000-0000-0000-0000-000000000000");
        // ATT REN1 (http://schemas.datacontract.org/2004/07/Rentech.PracticoLive.Connector.Objects)
        testRunner.setProperty("Chave", "b3c02efe-9e54-49a9-95b4-3af8f9475392");
        testRunner.setProperty("CodigoSistemaSatelite", "220000251");
        testRunner.setProperty("Data", "0001-01-01T00:00:00");
        testRunner.setProperty("Hora", "0001-01-01T00:00:00");

        testRunner.run();

        final List<MockFlowFile> flowFiles = testRunner.getFlowFilesForRelationship(PostSOAP.REL_SUCCESS);
        Assert.assertNotNull(flowFiles);

        final MockFlowFile mockFlowFile = flowFiles.get(0);
        _verify.printArgumentsAndContent(flowFiles.get(0));

    }

    private class Given {

        public String getObterChaveAcessoLC_IntegracaoPayloadXml() {
            final StringBuilder sb = new StringBuilder();

            sb.append("<nifi:ObterChaveAcessoLC_Integracao xmlns:nifi='http://LiveConnector/'>");
            sb.append("    <nifi:protectedDomain>00000000-0000-0000-0000-000000000000</nifi:protectedDomain>");
            sb.append("    <nifi:publicDomain>00000000-0000-0000-0000-000000000000</nifi:publicDomain>");
            sb.append("    <nifi:CodigoSistemaSatelite>220000251</nifi:CodigoSistemaSatelite>");
            sb.append("    <nifi:Usuario>Integracao@natura_iris</nifi:Usuario>");
            sb.append("    <nifi:Senha>123456</nifi:Senha>");
            sb.append("</nifi:ObterChaveAcessoLC_Integracao>");

            return sb.toString();
        }

        public String getRecuperarCupomFiscalLC_IntegracaoPayloadXml() {
            final StringBuilder sb = new StringBuilder();
            // sb.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:liv='http://LiveConnector/'
            // xmlns:ren='http://schemas.datacontract.org/2004/07/Rentech.Framework.Data'
            // xmlns:ren1='http://schemas.datacontract.org/2004/07/Rentech.PracticoLive.Connector.Objects'>");
            // sb.append(" <soapenv:Header/>");
            // sb.append(" <soapenv:Body>");
            sb.append(
                    "      <liv:RecuperarCupomFiscalLC_Integracao xmlns:liv='http://LiveConnector/' xmlns:ren='http://schemas.datacontract.org/2004/07/Rentech.Framework.Data' xmlns:ren1='http://schemas.datacontract.org/2004/07/Rentech.PracticoLive.Connector.Objects'>");
            sb.append("         <!--Optional:-->");
            sb.append("         <liv:publicDomain>00000000-0000-0000-0000-000000000000</liv:publicDomain>");
            sb.append("         <!--Optional:-->");
            sb.append("         <liv:protectedDomain>00000000-0000-0000-0000-000000000000</liv:protectedDomain>");
            sb.append("         <!--Optional:-->");
            sb.append("         <liv:identificacao>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:ClassID>00000000-0000-0000-0000-000000000000</ren:ClassID>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:Creation>0001-01-01T00:00:00</ren:Creation>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:LastUpdate>0001-01-01T00:00:00</ren:LastUpdate>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:ObjectID>00000000-0000-0000-0000-000000000000</ren:ObjectID>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:OwnerID>00000000-0000-0000-0000-000000000000</ren:OwnerID>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren:UserID>00000000-0000-0000-0000-000000000000</ren:UserID>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren1:Chave>b3c02efe-9e54-49a9-95b4-3af8f9475392</ren1:Chave>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren1:CodigoSistemaSatelite>220000251</ren1:CodigoSistemaSatelite>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren1:Data>0001-01-01T00:00:00</ren1:Data>");
            sb.append("            <!--Optional:-->");
            sb.append("            <ren1:Hora>0001-01-01T00:00:00</ren1:Hora>");
            sb.append("         </liv:identificacao>");
            sb.append("      </liv:RecuperarCupomFiscalLC_Integracao>");
            // sb.append(" </soapenv:Body>");
            // sb.append("</soapenv:Envelope>");
            return sb.toString();
        }

    }

    private class Verify {
        private Verify printArgumentsAndContent(MockFlowFile mockFlowFile) {
            final Map<String, String> attributes = mockFlowFile.getAttributes();
            System.out.println("============== FLOW FILE ============");
            System.out.println("========================== ATTRIBUTES");
            attributes.entrySet().stream().forEach(new Consumer<Entry<String, String>>() {
                @Override
                public void accept(Entry<String, String> t) {
                    System.out.println(t.getKey() + "=" + t.getValue());
                }
            });
            System.out.println("========================== CONTENT");
            System.out.println(new String(mockFlowFile.toByteArray()));
            System.out.println("=====================================");

            return this;
        }

    }

}
