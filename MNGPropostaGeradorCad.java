package br.com.mm.jfrm.ord.viewjfs.bean;

import br.com.aph.estg.bus.BusClienteLocal;
import br.com.aph.estg.persist.*;
import br.com.aph.estg.persist.enumeratiom.StatusDocumento;
import br.com.aph.estg.persist.ws.CepTransporter;
import br.com.aph.ord.persist.*;
import br.com.mm.jfrm.bus.JFRMBusLocal;
import br.com.mm.jfrm.estg.viewjsf.util.UtilMsg;
import br.com.mm.jfrm.ger.bus.BusUploadLocal;
import br.com.mm.jfrm.ger.persist.*;
import br.com.mm.jfrm.ger.persist.Upload;
import br.com.mm.jfrm.ger.viewjsf.bean.MNGTelefone;
import br.com.mm.jfrm.jsfcomp.ActionComponent;
import br.com.mm.jfrm.jsfcomp.annotation.ActionJFRM;
import br.com.mm.jfrm.jsfcomp.annotation.InjectComponent;
import br.com.mm.jfrm.jsfcomp.annotation.InjectManagedBean;
import br.com.mm.jfrm.jsfcomp.bean.DefaultMNGCad;
import br.com.mm.jfrm.jsfcomp.nonvisual.ActionEvent;
import br.com.mm.jfrm.jsfcomp.nonvisual.ExtActions;
import br.com.mm.jfrm.jsfcomp.nonvisual.Param;
import br.com.mm.jfrm.jsfcomp.ui.*;
import br.com.mm.jfrm.jsfcomp.ui.combo.ComboDocument;
import br.com.mm.jfrm.jsfcomp.ui.combo.ComboElement;
import br.com.mm.jfrm.jsfcomp.ui.dialog.ActionDialog;
import br.com.mm.jfrm.jsfcomp.ui.grid.GridCol;
import br.com.mm.jfrm.jsfcomp.ui.grid.GridDocument;
import br.com.mm.jfrm.jsfcomp.ui.grid.GridRow;
import br.com.mm.jfrm.jsfcomp.ui.upload.ActionUpload;
import br.com.mm.jfrm.jsfcomp.util.FileTempJSF;
import br.com.mm.jfrm.kernel.bus.IBusJFRM;
import br.com.mm.jfrm.kernel.jsf.bean.JFRMSession;
import br.com.mm.jfrm.kernel.log.JFRMLogger;
import br.com.mm.jfrm.kernel.log.Log;
import br.com.mm.jfrm.kernel.persist.QueryParameters;
import br.com.mm.jfrm.kernel.persist.QueryType;
import br.com.mm.jfrm.kernel.report.DefaultFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Pierre Iost
 */

@ManagedBean(name = "MNGPropostaGeradorCad")
@RequestScoped
public class MNGPropostaGeradorCad extends DefaultMNGCad {
    /*BUS*/
    @EJB
    private BusUploadLocal<Upload> facadeUpload;
    @EJB
    private JFRMBusLocal facadeGeneric;
    @EJB
    private JFRMBusLocal<PropostaGerador> facade;
    @EJB
    private BusClienteLocal<Cliente> facadeCliente;
    /*Hidden Fields*/
    @InjectComponent
    private HiddenField hdIdMunicipio;
    @InjectComponent
    private HiddenField hfOptLock;
    @InjectComponent
    private HiddenField hdTipoEndereco;
    @InjectComponent
    private HiddenField hdCodEnd;
    @InjectComponent
    private HiddenField hdEnderecoOptLock;
    /* Input Text */
    @InjectComponent
    private Grid gridDocProp;
    @InjectComponent
    private InputText txtCod;
    @InjectComponent
    private InputText txtNomeDocProp;
    @InjectComponent
    private InputText txtDescArquivoProp;
    @InjectComponent
    private InputText txtHorEqpEntrega;
    @InjectComponent
    private InputText txtHorEqpRetirada;
    @InjectComponent
    private InputText txtHorProposta;
    @InjectComponent
    private InputText txtCodCliente;
    @InjectComponent
    private InputText txtDescCliente;
    @InjectComponent
    private InputText txtEndereco;
    @InjectComponent
    private InputText txtNumeroEnd;
    @InjectComponent
    private InputText txtBairro;
    @InjectComponent
    private InputText txtNumCep;
    @InjectComponent
    private InputText txtMunicipio;
    @InjectComponent
    private InputText txtUf;
    @InjectComponent
    private InputText txtNumTel;
    @InjectComponent
    private InputText txtDDD;
    @InjectComponent
    private InputText txtCodEquipamento;
    @InjectComponent
    private InputText txtDescEquipamento;
    @InjectComponent
    private InputText txtDataProposta;
    @InjectComponent
    private InputText txtDataEntrega;
    @InjectComponent
    private InputText txtDataRetirada;
    @InjectComponent
    private InputText txtDataAssinatura;
    @InjectComponent
    private InputText txtHoras;
    @InjectComponent
    private InputText txtEquipamentos;
    @InjectComponent
    private InputText txtPotencia;
    @InjectComponent
    private InputText txtCodItens;
    @InjectComponent
    private InputText txtDataEqpEntrega;
    @InjectComponent
    private InputText txtDataEqpRetirada;
    @InjectComponent
    private InputText txtHorRetirada;
    @InjectComponent
    private InputText txtHorEntrega;
    @InjectComponent
    private InputText txtValorTotal;
    @InjectComponent
    private InputText txtCodOper;
    /* Combos */
    @InjectComponent
    private Combo cmbStatus;
    @InjectComponent
    private Combo cmbColaboradorEntregou;
    @InjectComponent
    private Combo cmbColaboradorRetirou;
    @InjectComponent
    private Combo cmbResponsavel;
    @InjectComponent
    private Combo cmbElaborou;
    @InjectComponent
    private Combo cmbNivelCombustivelEqpEntrega;
    @InjectComponent
    private Combo cmbNivelCombustivelEqpRetirada;
    @InjectComponent
    private Combo cmbColabEntreg;
    @InjectComponent
    private Combo cmbColabRetirou;
    /* Button Hits */
    @InjectComponent
    private ButtonHit bthCliente;
    @InjectComponent
    private ButtonHit bthCep;
    @InjectComponent
    private ButtonHit bthTelCliente;
    @InjectComponent
    private ButtonHit bthEquipamento;
    @InjectComponent
    private Button btEmitir;
    @InjectComponent
    private Button btSaveItens;
    @InjectComponent
    private Button btClearItens;
    @InjectComponent
    private Button btRemoveItens;
    @InjectComponent
    private Button btExcluiDoc;
    /* Outros */
    @InjectManagedBean(name = "MNGTelefone")
    private MNGTelefone mngTelefone;
    @InjectComponent
    private TextArea txtObs;
    @InjectComponent
    private TextArea txtObsEntrega;
    @InjectComponent
    private Check ckAprovado;
    @InjectComponent
    private Check ckGeradoWs;
    @InjectComponent
    private Grid gridItens;
    @InjectComponent
    private ProgressBar pgbDocProp;
    @InjectComponent
    private br.com.mm.jfrm.jsfcomp.ui.Upload upDocProp;

    public MNGPropostaGeradorCad() {
    }

    @Override
    public void cadOnLoad(ActionComponent ac) {
        cadNew(ac);
        Usuario usuario = (Usuario) JFRMSession.get().getUsuario();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm");

        Param p = ac.getManaged().getParameter("key");
        QueryParameters qp = new QueryParameters("select o from Colaborador o where o.usuario.id=:id", QueryType.DYNAMIC);
        qp.add("id", usuario.getId());
        txtDataProposta.setValueWithMask(sdf.format(new Date()));
        txtHorProposta.setValueWithMask(sdfFull.format(new Date()));
        cmbElaborou.setKeySel(usuario.getId().toString());
        txtMunicipio.setDisabled(true);
        txtUf.setDisabled(true);
        ckGeradoWs.setDisabled(true);
        txtCodItens.setDisabled(true);
        if (p != null) {
            PropostaGerador proposta = facade.find(Long.valueOf(p.getValue()), PropostaGerador.class);
            fillCad(ac, proposta);
            if (cmbStatus.getKeySel() != null) {
                if (cmbStatus.getKeySel().equalsIgnoreCase("emitido")) {
                    disableAll(ac);
                } else if (cmbStatus.getKeySel().equalsIgnoreCase("cancelado")) {
                    disableAll(ac);
                } else if (cmbStatus.getKeySel().equalsIgnoreCase("pendente")) {
                    enableAll(ac);
                }
            }
        }
    }

    @Override
    public void cadOpen(ActionComponent ac) {
        try {
            PropostaGerador proposta = facade.find(Long.valueOf(txtCod.getValue()), PropostaGerador.class);
            if (proposta != null) {
                fillCad(ac, proposta);
            } else {
                txtCod.setFocus();
                ac.showDialog(Dialog.getMessage("Código não existe!"));
            }
        } catch (NumberFormatException ex) {
            txtCod.setFocus();
            ac.showDialog(Dialog.getMessage("Código inválido"));
        }
    }

    @Override
    public void cadRemove(ActionComponent ac) {
        ac.showDialog(Dialog.getMessage("Não é possivel excluir!"));
    }

    @Override
    public void cadSave(ActionComponent ac) {
        if (cadValidate(ac)) {
            PropostaGerador proposta = null;
            if (this.getModo() == Modo.EDIT) {
                proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
                proposta = fillProposta(ac, proposta);
                proposta = facade.merge(proposta);
            } else {
                proposta = new PropostaGerador();
                proposta = fillProposta(ac, proposta);
                proposta = facade.persist(proposta);
            }
            fillCad(ac, proposta);
            ac.showDialog(Dialog.getMessage(UtilMsg.SALVO));
        }
    }

    @Override
    public void cadNew(ActionComponent ac) {
        super.cadNew(ac);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm");
        JFRMSession jfrmSession = JFRMSession.get();

        clearCadEnderecoCli();
        clearCadCliente();
        Usuario usuario = (Usuario) jfrmSession.getUsuario();
        QueryParameters qp = new QueryParameters("select o from Colaborador o where o.usuario.id=:id", QueryType.DYNAMIC);
        qp.add("id", usuario.getId());
        cmbElaborou.setKeySel(usuario.getId().toString());
        cmbStatus.setKeySel(StatusDocumento.PENDENTE.name());
        txtDataProposta.setValueWithMask(sdf.format(new Date()));
        txtHorProposta.setValueWithMask(sdfFull.format(new Date()));
        txtMunicipio.setDisabled(true);
        txtUf.setDisabled(true);
        ckGeradoWs.setDisabled(true);
        txtCodItens.setDisabled(true);
        enableAll(ac);
    }

    @Override
    public boolean cadValidate(ActionComponent ac) {
        if (this.getModo() == Modo.NEW) {
            if (txtCod.getValueWithNull() != null) {
                txtCod.setValue(null);
                txtCod.setFocus();
                ac.showDialog(Dialog.getMessage("Código gerado automaticamente, portanto não pode ser preenchido!"));
                return false;
            }
        }
        if (txtNumeroEnd.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Número do Endereço deve ser informado"));
            txtNumeroEnd.setFocus();
            return false;
        }
        if (txtDataProposta.getValueWithNull() == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm");
            txtDataProposta.setValueWithMask(sdf.format(new Date()));
            txtHorProposta.setValueWithMask(sdfFull.format(new Date()));
        }
        if (txtCodCliente.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Cliente deve ser informado!"));
            txtCodCliente.setFocus();
            return false;
        }
        if (txtBairro.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Bairro do Cliente deve ser informado!"));
            return false;
        }
        if (txtEndereco.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Logradouro do Cliente deve ser informado!"));
            return false;
        }
        if (txtBairro.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Bairro do Cliente deve ser informado!"));
            return false;
        }
        if (txtNumCep.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("CEP do Cliente deve ser informado!"));
            return false;
        }
        if (txtUf.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("A Uf do Cliente deve ser informado!"));
            return false;
        }
        if (txtMunicipio.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Cidade do Cliente deve ser informado!"));
            return false;
        }
        if (cmbResponsavel.getKeySel() == null) {
            ac.showDialog(Dialog.getMessage("É preciso cadastrar um responsável!"));
            return false;
        }
        return true;
    }

    @Override
    public void cadPrint(ActionComponent ac) {
        super.cadPrint(ac);
        if (txtCod.getValueWithNull() != null) {
            PropostaGerador pg = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            if (pg != null) {
                ac.print(geraArqPdf(ac, pg), "relPropostaGerador_" + txtCod.getValue() + ".pdf");
            } else {
                ac.showDialog(Dialog.getMessage(UtilMsg.NAO_ENCONTRADO));
            }
        } else {
            ac.showDialog(Dialog.getMessage(UtilMsg.NAO_ENCONTRADO));
        }
    }

    @ActionJFRM("cancel")
    public void cadCancel(ActionComponent ac) {
        btCancela(ac);
    }

    public byte[] geraArqPdf(ActionComponent ac, PropostaGerador pg) {
        Empresa emp = (Empresa) JFRMSession.get().getEmpresa();
        QueryParameters qp;
        qp = new QueryParameters("select o from PropostaGerador o where o.id=:codPed", QueryType.DYNAMIC);
        qp.add("codPed", pg.getId());
        PropostaGerador p = (PropostaGerador) facade.findSingleResult(qp);
        String telEmpresa = "";
        if (!emp.getPessoa().getTelefones().isEmpty()) {
            int i = 0;
            for (Telefone telefone : emp.getPessoa().getTelefones()) {
                if (i < 3) {
                    telEmpresa += validaTelefone(telefone.getNumeroTelefone().getDdd(), telefone.getNumeroTelefone().getNumero()) + "  ";
                }
                i++;
            }
        }
        String tel = "";
        if (!p.getTelefoneDestinatario().isEmpty() && !p.getDddDestinatario().isEmpty()) {
            tel = "(" + p.getDddDestinatario() + ") " + p.getTelefoneDestinatario().substring(0, 4) + "-" + p.getTelefoneDestinatario().substring(4);
        }
        Map map = new HashMap();
        map.put("Empresa", (Empresa) JFRMSession.get().getEmpresa());
        map.put("Usuario", (Usuario) JFRMSession.get().getUsuario());
        map.put("TelefoneEmpresa", telEmpresa);
        map.put("Telefone", tel);
        map.put("Arquivo", "relPropostaGeradorCad");
        map.put("SUBREPORT_DIR", "br/com/mm/jfrm/ord/report/");
        if (emp.getLogo() != null) {
            byte[] logo = facadeUpload.findContent(emp.getLogo()).getContent();
            InputStream in = new ByteArrayInputStream(logo);
            map.put("JFRMLOGO", in);
        }
        List<PropostaGerador> lista = new ArrayList<>();
        lista.add(pg);
        DefaultFactory.setBeanCollection(lista);
        byte[] rel = facade.print("/br/com/mm/jfrm/ord/report/relPropostaGeradorCad.jasper",
                DefaultFactory.getBeanCollection(), map, IBusJFRM.ReportType.PDF, IBusJFRM.ConnectionType.JAVABEAN);
        return rel;
    }

    public static String validaTelefone(String ddd, String tel) {
        String dddFone;
        if (tel != null && !tel.isEmpty()) {
            tel = tel.replace(".", " "); //tira o ponto
            dddFone = ddd != null && !ddd.isEmpty() ? "(" + ddd + ") " : "(00) ";
            switch (tel.length()) {
                case 8:
                    dddFone = dddFone + (tel.substring(0, 4) + "-" + tel.substring(4, 8));
                    break;
                case 6:
                    dddFone = dddFone + (tel.substring(0, 2) + "." + tel.substring(2, 4) + "." + tel.substring(4, 6));
                    break;
                case 9:
                    dddFone = dddFone + (tel.substring(0, 4) + "-" + tel.substring(4, 9));
                    break;
                default:
                    break;
            }
            return dddFone;
        }
        return "";
    }

    private void fillCad(ActionComponent ac, PropostaGerador elem) {
        setModo(Modo.EDIT);
        Usuario usuario = (Usuario) JFRMSession.get().getUsuario();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm");

        txtCod.setValueLong(elem.getId());
        cmbElaborou.setKeySel(usuario.getId().toString());
        if (elem.getColaboradorResponsavel() != null) {
            cmbResponsavel.setKeySel(elem.getColaboradorResponsavel().getId().toString());
        } else {
            cmbResponsavel.reset();
        }
        cmbStatus.setKeySel(elem.getStatus() == null ? null : elem.getStatus().name());
        txtDataProposta.setValueWithMask(elem.getDtProposta() != null ? sdf.format(elem.getDtProposta()) : null);
        txtHorProposta.setValueWithMask(elem.getDtProposta() != null ? sdfFull.format(elem.getDtProposta()) : null);
        txtDataAssinatura.setValueWithMask(elem.getDataAssContrato() != null ? sdf.format(elem.getDataAssContrato()) : null);
        txtDataEntrega.setValueWithMask(elem.getDataPrevistaEntrega() != null ? sdf.format(elem.getDataPrevistaEntrega()) : null);
        txtDataRetirada.setValueWithMask(elem.getDataPrevistaRetirada() != null ? sdf.format(elem.getDataPrevistaRetirada()) : null);
        txtHoras.setValueLong(elem.getQtdHorasProposta());
        txtEquipamentos.setValueLong(elem.getQtdEquipamentos());
        txtPotencia.setValueDouble(elem.getPotenciaProposta());
        txtValorTotal.setValueDouble(elem.getValorTotal());
        txtObs.setValue(elem.getObservacoesProposta() == null ? "" : elem.getObservacoesProposta().trim());
        txtObsEntrega.setValue(elem.getObservacoesEntrega() == null ? "" : elem.getObservacoesEntrega().trim());
        if (elem.getCliente() != null) {
            txtCodCliente.setValueLong(elem.getCliente().getId());
            txtDescCliente.setValue(elem.getCliente().getPessoa().toString());
            onLostFocusCliente(ac);
        } else {
            txtCodCliente.setValue(null);
            txtDescCliente.setValue(null);
        }
        mngTelefone.converteMaskDynamic(elem.getTelefoneDestinatario(), txtNumTel);
        txtNumTel.setValue(elem.getTelefoneDestinatario());
        txtDDD.setValue(elem.getDddDestinatario());
        txtMunicipio.setValue(elem.getEnderecoProposta().getMunicipio().getNome().toUpperCase());
        txtEndereco.setValue(elem.getEnderecoProposta().getLogradouro());
        txtNumCep.setValue(elem.getEnderecoProposta().getNumCEP().toString());
        txtNumeroEnd.setValue(elem.getEnderecoProposta().getNumero().toString());
        txtBairro.setValue(elem.getEnderecoProposta().getBairro());
        txtUf.setValue(elem.getEnderecoProposta().getMunicipio().getUf().getSigla());
        ckAprovado.setSelected(elem.getAprovou());
        fillGridItens(elem);
        fillGridDocumentos(ac);
        saveBind(ac);
        String telaOrigem = (String) JFRMSession.get().getPropriedade("telaOrigem");
        if (telaOrigem == null) {
            ac.getExtActions().execAction(ExtActions.Scope.PARENT, "MNGPropostaGeradorFilter.filterFind");
        }
    }

    private PropostaGerador fillProposta(ActionComponent ac, PropostaGerador proposta) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (cmbResponsavel.getKeySel() != null) {
            proposta.setColaboradorResponsavel((Colaborador) facadeGeneric.find(Long.parseLong(cmbResponsavel.getKeySel()), Colaborador.class));
        }
        if (cmbElaborou.getKeySel() != null) {
            proposta.setElaborouProposta((Usuario) facadeGeneric.find(Long.parseLong(cmbElaborou.getKeySel()), Usuario.class));
        } else {
            proposta.setColaboradorResponsavel(null);
        }
        try {
            proposta.setDtProposta(txtDataProposta.getValueWithNull() != null ? sdfFull.parse((txtDataProposta.getValueWithMask() + " " + txtHorProposta.getValueWithMask())) : null);
        } catch (java.text.ParseException ex) {
            JFRMLogger.get(MNGPropostaGeradorCad.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            proposta.setDataAssContrato(txtDataAssinatura.getValueWithNull() != null ? sdf.parse(txtDataAssinatura.getValueWithMask()) : null);
        } catch (ParseException e) {
            txtDataAssinatura.setValue(null);
        }
        try {
            proposta.setDataPrevistaEntrega(txtDataEntrega.getValueWithNull() != null ? sdf.parse(txtDataEntrega.getValueWithMask()) : null);
        } catch (ParseException e) {
            txtDataEntrega.setValue(null);
        }
        try {
            proposta.setDataPrevistaRetirada(txtDataRetirada.getValueWithNull() != null ? sdf.parse(txtDataRetirada.getValueWithMask()) : null);
        } catch (ParseException e) {
            txtDataRetirada.setValue(null);
        }
        if (cmbStatus.getKeySel().equalsIgnoreCase("pendente")) {
            proposta.setStatus(StatusDocumento.PENDENTE);
        } else if (cmbStatus.getKeySel().equalsIgnoreCase("emitido")) {
            proposta.setStatus(StatusDocumento.EMITIDO);
        } else if (cmbStatus.getKeySel().equalsIgnoreCase("cancelado")) {
            proposta.setStatus(StatusDocumento.CANCELADO);
        }
        proposta.setQtdHorasProposta(txtHoras.getValueLong());
        proposta.setQtdEquipamentos(txtEquipamentos.getValueLong());
        proposta.setPotenciaProposta(txtPotencia.getValueDouble());
        proposta.setObservacoesProposta(txtObs.getValue().trim());
        proposta.setObservacoesEntrega(txtObsEntrega.getValue().trim());
        proposta.setValorTotal(txtValorTotal.getValueDouble());
        if (txtCodCliente.getValueWithNull() != null) {
            proposta.setCliente((Cliente) facadeGeneric.find(txtCodCliente.getValueLong(), Cliente.class));
        } else {
            proposta.setCliente(null);
        }
        proposta.setDddDestinatario(txtDDD.getValue());
        proposta.setTelefoneDestinatario(txtNumTel.getValue());
        Endereco endereco = fillEnderecoProposta();
        proposta.setEnderecoProposta(endereco);
        proposta.setAprovou(ckAprovado.isSelected());
        return proposta;
    }

    public void dialogReturn(ActionComponent ac) {
        ActionDialog ad = (ActionDialog) ac;
        if (ad.getOpc() == Dialog.OPTION.OK) {
            String value = ad.getParameter("key").getValue();
            PropostaGerador proposta = facade.find(Long.valueOf(value), PropostaGerador.class);
            if (proposta != null) {
                facade.remove(proposta);
                cadNew(ac);
                ac.getExtActions().execAction(ExtActions.Scope.PARENT, "MNGPropostaGeradorFilter.filterFind");
                ac.showDialog(Dialog.getMessage(UtilMsg.EXCLUIDO));
            } else {
                ac.showDialog(Dialog.getMessage(UtilMsg.NAO_ENCONTRADO));
            }
        }
    }

    public ComboDocument<String> loadStartupCmbStatus() {
        ComboDocument cd = new ComboDocument(false);
        cd.addComboElement(new ComboElement<>(StatusDocumento.PENDENTE.name(), StatusDocumento.PENDENTE.name()));
        cd.addComboElement(new ComboElement<>(StatusDocumento.EMITIDO.name(), StatusDocumento.EMITIDO.name()));
        cd.addComboElement(new ComboElement<>(StatusDocumento.CANCELADO.name(), StatusDocumento.CANCELADO.name()));
        return cd;
    }

    public ComboDocument<String> loadStartupCmbUsuarioElaborou() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        List<Usuario> lista = facadeGeneric.find(new QueryParameters("select o from Usuario o "
                + "where o.ativo=true order by o.id", QueryType.DYNAMIC)).getResultList();
        for (Usuario elem : lista) {
            cd.addComboElement(new ComboElement<>(elem.getId().toString(), elem.getPessoa().toString().toUpperCase()));
        }
        return cd;
    }

    private Boolean validaCliente(ActionComponent ac, Cliente cli) {
        if (!cli.getPessoa().isAtivo() || !cli.isAtivo()) {
            ac.showDialog(Dialog.getMessage("Cliente deve estar Ativo!!"));
            return false;
        }
        if (!cli.getUtilizar()) {
            ac.showDialog(Dialog.getMessage("Cliente Inutilizado!"));
            return false;
        }
        return true;
    }

    public void onLostFocusCliente(ActionComponent ac) {
        if (txtCodCliente.getValueWithNull() != null) {
            Cliente cli = facadeCliente.find(txtCodCliente.getValueLong(), Cliente.class);
            if (cli != null) {
                if (validaCliente(ac, cli)) {
                    fillCadCliente(cli);
                    fillEnderecoCliente(cli);
                    fillTelefoneCliente(cli.getPessoa());
                }
            } else {
                ac.showDialog(Dialog.getMessage("Este código não esta associado a um Cliente!"));
                clearCadCliente();
                clearCadEnderecoCli();
                txtCodCliente.setFocus();
            }
        }
    }

    public void onClickBthCliente(ActionComponent ac) {
        Window w = ac.getManaged().getById("winBhCliente", Window.class);
        bthCliente.open(w);
    }

    public void actJFRMBthCliente(ActionComponent ac) {
        if (ac instanceof ActionEvent) {
            ActionEvent ea = (ActionEvent) ac;
            Cliente cli = (Cliente) facadeGeneric.find(Long.valueOf(ea.getParametros().get(0).getValue()), Cliente.class);
            txtCodCliente.setValue(ea.getParametros().get(0).getValue());
            txtDescCliente.setValue(cli.getPessoa().toString());
            onLostFocusCliente(ac);
        }
    }

    public void actJFRMBthTelefoneCliente(ActionComponent ac) {
        if (ac instanceof ActionEvent) {
            ActionEvent ea = (ActionEvent) ac;
            Telefone tel = (Telefone) facadeGeneric.find(Long.valueOf(ea.getParametros().get(1).getValue()), Telefone.class);
            mngTelefone.converteMaskDynamic(tel.getNumeroTelefone().getNumero(), txtNumTel);
            txtNumTel.setValue(tel.getNumeroTelefone().getNumero());
            txtDDD.setValue(tel.getNumeroTelefone().getDdd());
        }
    }

    public void onClickBthTelefoneCliente(ActionComponent ac) {
        List<Param> l = new ArrayList<>();
        l.add(new Param("idComponent", ac.getId()));
        Window w = ac.getManaged().getById("winBhTelefone", Window.class);
        if (bthTelCliente.getId().equals(ac.getId())) {
            if (txtCodCliente.getValueWithNull() != null) {
                Cliente cli = (Cliente) facadeGeneric.find(txtCodCliente.getValueLong(), Cliente.class);
                l.add(new Param("idPessoa", cli.getPessoa().getId().toString()));
                bthTelCliente.open(w, l);
            } else {
                ac.showDialog(Dialog.getMessage("Deve ser selecionado um cliente!"));
            }
        }
    }

    public ComboDocument<String> loadStartupCmbColaboradorEntregou() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        List<Colaborador> lista = facadeGeneric.find(new QueryParameters("select o from Colaborador o "
                + "where o.ativo=true order by o.apelido", QueryType.DYNAMIC)).getResultList();
        for (Colaborador elem : lista) {
            cd.addComboElement(new ComboElement<>(elem.getId().toString(), elem.getApelido()));
        }
        return cd;
    }

    public ComboDocument<String> loadStartupCmbColaboradorRetirou() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        List<Colaborador> lista = facadeGeneric.find(new QueryParameters("select o from Colaborador o "
                + "where o.ativo=true order by o.apelido", QueryType.DYNAMIC)).getResultList();
        for (Colaborador elem : lista) {
            cd.addComboElement(new ComboElement<>(elem.getId().toString(), elem.getApelido()));
        }
        return cd;
    }

    public void onLostFocusEquipamento(ActionComponent ac) {
        if (txtCodEquipamento.getValueWithNull() != null) {
            QueryParameters qp = new QueryParameters("SELECT O FROM Equipamento o where o.id=:id and o.cliente.id=:cliente", QueryType.DYNAMIC);
            qp.add("id", txtCodEquipamento.getValueLong());
            qp.add("cliente", txtCodCliente.getValueLong());
            Equipamento equipamento = (Equipamento) facadeGeneric.find(txtCodEquipamento.getValueLong(), Equipamento.class);
            if (equipamento != null) {
                txtCodEquipamento.setValue(equipamento.getId().toString());
                txtDescEquipamento.setValue(equipamento.getDescricao());
            } else {
                txtCodEquipamento.setValue(null);
                txtDescEquipamento.setValue(null);
            }
        } else {
            txtCodEquipamento.setValue(null);
            txtDescEquipamento.setValue(null);
        }
    }

    public void actJFRMBthEquipamento(ActionComponent ac) {
        if (ac instanceof ActionEvent) {
            ActionEvent ea = (ActionEvent) ac;
            Equipamento eqp = (Equipamento) facadeGeneric.find(Long.valueOf(ea.getParametros().get(0).getValue()), Equipamento.class);
            txtCodEquipamento.setValue(ea.getParametros().get(0).getValue());
            txtDescEquipamento.setValue(eqp.getDescricao().toString());
            onLostFocusEquipamento(ac);
        }
    }

    public void onClickBthEquipamento(ActionComponent ac) {
        Window w = ac.getManaged().getById("winBhEquipamento", Window.class);
        bthEquipamento.open(w);
    }

    public ComboDocument<String> loadStartupCmbResponsavel() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        List<Colaborador> lista = facadeGeneric.find(new QueryParameters("select o from Colaborador o "
                + "where o.ativo=true order by o.apelido", QueryType.DYNAMIC)).getResultList();
        for (Colaborador elem : lista) {
            cd.addComboElement(new ComboElement<>(elem.getId().toString(), elem.getApelido()));
        }
        return cd;
    }

    public void onClickBthCep(ActionComponent ac) {
        Window w = ac.getManaged().getById("winBhCep", Window.class);
        bthCep.open(w);
    }

    public void actJFRMBthCep(ActionComponent ac) {
        if (ac instanceof ActionEvent) {
            ActionEvent ea = (ActionEvent) ac;
            if (ea.getParametros().get(0).getValue().length() < 8) {
                txtNumCep.setValue("0" + ea.getParametros().get(0).getValue());
            } else {
                txtNumCep.setValue(ea.getParametros().get(0).getValue());
            }
            onLostFocusNumeroCepCad(ac);
        }
    }

    public void onLostFocusNumeroCepCad(ActionComponent ac) {
        if (txtNumCep.getValueWithNull() != null) {
            try {
                Long num = Long.valueOf(txtNumCep.getValue());
                QueryParameters qp = new QueryParameters("select c from CEP c where c.numero=:numero", QueryType.DYNAMIC);
                qp.add("numero", num);
                List<CEP> cep = facadeGeneric.find(qp).getResultList();
                if (!cep.isEmpty()) {
                    qp = new QueryParameters("select o from TipoEndereco o where o.descricao=:descricao", QueryType.DYNAMIC);
                    qp.add("descricao", "Entrega");
                    TipoEndereco tEnd = (TipoEndereco) facadeGeneric.findSingleResult(qp);
                    fillCadCEPEndereco(ac, cep.get(0), tEnd);
                    txtDDD.setFocus();
                }
            } catch (NumberFormatException ex) {
                ac.showDialog(Dialog.getMessage(String.format("Número de CEP Inválido[%s]", txtNumCep.getValue())));
            }
            if (UtilMsg.testaInternet()) {
                QueryParameters qp = new QueryParameters("select o from TipoEndereco o where o.descricao=:descricao", QueryType.DYNAMIC);
                qp.add("descricao", "Entrega");
                TipoEndereco tEnd = (TipoEndereco) facadeGeneric.findSingleResult(qp);
                CepTransporter cepT = UtilMsg.getCEP(txtNumCep.getValue());
                if (cepT != null) {
                    if (txtNumCep.getId().equals(txtNumCep.getId())) {
                        fillCadCEPEndereco(cepT, tEnd);
                    }
                } else {
                    ac.showDialog(Dialog.getMessage("Cep não encontrado no WS!"));
                    txtNumCep.setValue(null);
                }
            } else {
                ac.showDialog(Dialog.getMessage("Indisponibilidade de internet!"));
            }
            } else {
            txtNumCep.setValue(null);
            txtEndereco.setValue(null);
            txtNumeroEnd.setValue(null);
            txtDDD.setValue(null);
            txtNumTel.setValue(null);
            txtBairro.setValue(null);
            hdIdMunicipio.setValue(null);
            txtMunicipio.setValue(null);
            txtUf.setValue(null);
            hdTipoEndereco.setValue(null);
        }
    }

    public void fillCadCEPEndereco(CepTransporter elem, TipoEndereco tEnd) {
        txtNumCep.setValue(elem.getCep().replace("-", ""));
        txtEndereco.setValue(elem.getLogradouro());
        txtBairro.setValue(elem.getBairro());
        hdTipoEndereco.setValue(tEnd.getId() == null ? null : tEnd.getId().toString());
        txtNumeroEnd.setValue(null);
        txtDDD.setValue(elem.getDdd());
        QueryParameters qp = new QueryParameters("select o from Municipio o where upper(o.codigoIBGE) = :ibge", QueryType.DYNAMIC);
        qp.add("ibge", elem.getIbge());
        Municipio mun = ((Municipio) facadeGeneric.findSingleResult(qp));
        if (mun != null) {
            hdIdMunicipio.setValue(mun.getId().toString());
            txtMunicipio.setValue(mun.getNome());
            txtUf.setValue(mun.getUf().getSigla());
        }
    }

    public void fillCadCEPEndereco(ActionComponent ac, CEP elem, TipoEndereco tEnd) {
        if (elem != null) {
            if (elem.toString().length() < 8) {
                txtNumCep.setValue("0" + elem.toString());
            } else {
                txtNumCep.setValue(elem.toString());
            }
            txtEndereco.setValue(elem.getLogradouro());
            txtBairro.setValue(elem.getBairro());
            hdIdMunicipio.setValue(elem.getMunicipio().getId().toString());
            txtMunicipio.setValue(elem.getMunicipio().getNome());
            txtUf.setValue(elem.getMunicipio().getUf().getSigla());
            hdTipoEndereco.setValue(tEnd.getId() == null ? null : tEnd.getId().toString());
        }
    }

    public void onLostFocusId(ActionComponent ac) {
        if (txtCod.getValueWithNull() != null) {
            QueryParameters qp = new QueryParameters("select o from PropostaGerador o where o.id=:id", QueryType.DYNAMIC);
            qp.add("id", Long.valueOf(txtCod.getValue()));
            PropostaGerador proposta = facade.findSingleResult(qp);
            if (proposta != null) {
                fillCad(ac, proposta);
            } else {
                ac.showDialog(Dialog.getMessage("Este código não existe!"));
                txtCod.setFocus();
            }
        }
    }

    public void clearCadCliente() {
        txtCodCliente.setValue(null);
        txtDescCliente.setValue(null);
    }

    public void clearCadEnderecoCli() {
        txtNumCep.setValue(null);
        txtBairro.setValue(null);
        txtMunicipio.setValue(null);
        txtUf.setValue(null);
        txtNumTel.setValue(null);
        txtDDD.setValue(null);
        hdIdMunicipio.setValue(null);
    }

    public void fillCadCliente(Cliente cli) {
        txtCodCliente.setValueLong(cli.getId());
        txtDescCliente.setValue(cli.getPessoa().toString());
    }

    private void fillEnderecoCliente(Cliente cli) {
        QueryParameters qp;
        Endereco endereco;
        qp = new QueryParameters("select o from Pessoa p inner join p.enderecos o where p.id=:idPessoa and o.tipoEndereco.id=:tipo", QueryType.DYNAMIC);
        qp.add("idPessoa", cli.getPessoa().getId());
        qp.add("tipo", 2);
        List<Endereco> lista = facadeGeneric.find(qp).getResultList();
        endereco = (lista != null && !lista.isEmpty()) ? lista.get(0) : null;
        if (endereco != null) {
            fillCadEnderecoProposta(endereco);
        } else {
            qp = new QueryParameters("select o from Pessoa p inner join p.enderecos o where p.id=:idPessoa", QueryType.DYNAMIC);
            qp.add("idPessoa", cli.getPessoa().getId());
            lista = facadeGeneric.find(qp).getResultList();
            if (!lista.isEmpty()) {
                fillCadEnderecoProposta(lista.get(0));
            }
        }
    }

    public void fillCadEnderecoProposta(Endereco end) {
        if (end.getNumCEP() != null) {
            hdCodEnd.setValue(end.getId().toString());
            if (end.getNumCEP().toString().length() < 8) {
                txtNumCep.setValue("0" + end.getNumCEP().toString());
            } else {
                txtNumCep.setValue(end.getNumCEP().toString());
            }
            txtEndereco.setValue(end.getLogradouro());
            txtBairro.setValue(end.getBairro());
            txtNumeroEnd.setValue(end.getNumero());
            hdIdMunicipio.setValue(end.getMunicipio().getId().toString());
            txtMunicipio.setValue(end.getMunicipio().getNome());
            txtUf.setValue(end.getMunicipio().getUf().getSigla());
            hdTipoEndereco.setValue(end.getTipoEndereco().getId().toString());
        }
    }

    private void fillTelefoneCliente(Pessoa pessoa) {
        Telefone tel;
        QueryParameters qp = new QueryParameters("SELECT O FROM Pessoa p inner join p.telefones as o where p.id=:idPessoa and o.tipoContato.descricao=:tipo", QueryType.DYNAMIC);
        qp.add("tipo", "Entrega");
        qp.add("idPessoa", pessoa.getId());
        tel = (Telefone) facadeGeneric.findSingleResult(qp);
        if (tel != null) {
            mngTelefone.converteMaskDynamic(tel.getNumeroTelefone().getNumero(), txtNumTel);
            txtNumTel.setValue(tel.getNumeroTelefone().getNumero());
            txtDDD.setValue(tel.getNumeroTelefone().getDdd());
        } else {
            qp = new QueryParameters("SELECT O FROM Pessoa p inner join p.telefones as o where p.id=:idPessoa", QueryType.DYNAMIC);
            qp.add("idPessoa", pessoa.getId());
            List<Telefone> lista = facadeGeneric.find(qp).getResultList();
            if (!lista.isEmpty()) {
                mngTelefone.converteMaskDynamic(lista.get(0).getNumeroTelefone().getNumero(), txtNumTel);
                txtNumTel.setValue(lista.get(0).getNumeroTelefone().getNumero());
                txtDDD.setValue(lista.get(0).getNumeroTelefone().getDdd());
            } else {
                txtNumTel.setValue(null);
            }
        }
    }

    private Endereco fillEnderecoProposta() {
        Endereco endereco = new Endereco();
        endereco.setNumCEP(Long.valueOf(txtNumCep.getValue()));
        endereco.setLogradouro(txtEndereco.getValueWithNull());
        endereco.setBairro(txtBairro.getValueWithNull());
        endereco.setNumero(txtNumeroEnd.getValueWithNull());
        Municipio municipio = (Municipio) facadeGeneric.find(Long.valueOf(hdIdMunicipio.getValue()), Municipio.class);
        endereco.setMunicipio(municipio);
        endereco.setTipoEndereco((TipoEndereco) facadeGeneric.find(Long.valueOf(hdTipoEndereco.getValue()), TipoEndereco.class));
        return endereco;
    }

    public void onLostFocusMunicipio(String mc) {
        if (mc != null) {
            QueryParameters qp = new QueryParameters(
                    "select o from Municipio o "
                            + "where o.codigoIBGE=:codigoIBGE "
                            + "order by o.nome", QueryType.DYNAMIC);
            qp.add("codigoIBGE", mc);
            Municipio municipio = (Municipio) facadeGeneric.findSingleResult(qp);
            if (municipio != null) {
                txtMunicipio.setValue(municipio.getNome());
                txtUf.setValue(municipio.getUf().getSigla());
                hdIdMunicipio.setValue(String.valueOf(municipio.getId()));
                hdTipoEndereco.setValue("1");
            } else {
                txtMunicipio.reset();
                txtUf.reset();
            }
        } else {
            txtMunicipio.reset();
            txtUf.reset();
        }
    }

    public ComboDocument<String> loadStartupCmbNivelCombustivelEqpEntrega() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        for (ItemPropostaGerador.NivelCombustivel n : ItemPropostaGerador.NivelCombustivel.values()) {
            cd.addComboElement(new ComboElement<>(n.name(), n.getAbrev()));
        }
        return cd;
    }

    public ComboDocument<String> loadStartupCmbNivelCombustivelEqpRetirada() {
        ComboDocument<String> cd = new ComboDocument<>(true);
        for (ItemPropostaGerador.NivelCombustivel n : ItemPropostaGerador.NivelCombustivel.values()) {
            cd.addComboElement(new ComboElement<>(n.name(), n.getAbrev()));
        }
        return cd;
    }

    private boolean validaItens(ActionComponent ac) {
        if (txtCod.getValueWithNull() == null) {
            ac.showDialog((Dialog.getMessage("Salve a Proposta Primeiro")));
            return false;
        }
        if (txtCodEquipamento.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage("Obrigatório selecionar um Equipamento"));
            return false;
        }
        if (txtDataEqpEntrega.getValueWithNull() != null) {
            if (txtHorEqpEntrega.getValueWithNull() != null) {
                ac.showDialog(Dialog.getMessage("Obrigatório Preencher o Horário de Entrega"));
            }
        }
        if (txtDataEqpRetirada.getValueWithNull() != null) {
            if (txtHorEqpRetirada.getValueWithNull() != null) {
                ac.showDialog(Dialog.getMessage("Obrigatório Preencher o Horário de Retirada"));
            }
        }
        return true;
    }

    public void btRemoveItens(ActionComponent ac) {
        if (txtCodItens.getValueWithNull() != null) {
            PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            ItemPropostaGerador item = (ItemPropostaGerador) facadeGeneric.find(Long.valueOf(txtCodItens.getValue()), ItemPropostaGerador.class);
            proposta.getListaEquipamentosGerador().remove(item);
            proposta = facade.merge(proposta);
            facadeGeneric.remove(item);
            btClearItens(ac);
            fillCad(ac, proposta);
        } else {
            ac.showDialog(Dialog.getMessage("Registro deve ser carregado antes de excluir!"));
        }
    }

    public void btClearItens(ActionComponent ac) {
        txtCodItens.setValue(null);
        txtHorEntrega.setValue(null);
        txtHorRetirada.setValue(null);
        txtDataEqpRetirada.setValue(null);
        txtDataEqpEntrega.setValue(null);
        txtCodEquipamento.setValue(null);
        txtDescEquipamento.setValue(null);
        cmbNivelCombustivelEqpEntrega.setKeySel(null);
        cmbNivelCombustivelEqpRetirada.setKeySel(null);
        cmbColabEntreg.setKeySel(null);
        cmbColabRetirou.setKeySel(null);
        txtHorRetirada.setValue(null);
        txtHorEqpRetirada.setValue(null);
        txtHorEqpEntrega.setValue(null);
        ckGeradoWs.setSelected(false);
    }

    public void btSaveItens(ActionComponent ac) {
        if (validaItens(ac)) {
            PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            if (txtCodItens.getValueWithNull() == null) {
                try {
                    ItemPropostaGerador item = new ItemPropostaGerador();
                    item.setHorimetroEqpEntrega(txtHorEntrega.getValueDouble());
                    item.setHorimetroEqpRetirada(txtHorRetirada.getValueDouble());
                    item.setDataEntregaEquipamento(txtDataEqpEntrega.getValueWithNull() != null ? sdfFull.parse((txtDataEqpEntrega.getValueWithMask() + " " + txtHorEqpEntrega.getValueWithMask())) : null);
                    item.setDataRetiradaEquipamento(txtDataEqpRetirada.getValueWithNull() != null ? sdfFull.parse((txtDataEqpRetirada.getValueWithMask()) + " " + txtHorEqpRetirada.getValueWithMask()) : null);
                    if (cmbNivelCombustivelEqpEntrega.getKeySel() != null) {
                        item.setNivelCombustivelEqpEntrega(ItemPropostaGerador.NivelCombustivel.valueOf(cmbNivelCombustivelEqpEntrega.getKeySel()));
                    }
                    if (cmbNivelCombustivelEqpRetirada.getKeySel() != null) {
                        item.setNivelCombustivelEqpRetirada(ItemPropostaGerador.NivelCombustivel.valueOf(cmbNivelCombustivelEqpRetirada.getKeySel()));
                    }
                    if (cmbColabEntreg.getKeySel() != null) {
                        item.setColaboradorEntregou((Colaborador) facadeGeneric.find(Long.parseLong(cmbColabEntreg.getKeySel()), Colaborador.class));
                    }
                    if (cmbColabRetirou.getKeySel() != null) {
                        item.setColaboradorRetirou((Colaborador) facadeGeneric.find(Long.parseLong(cmbColabRetirou.getKeySel()), Colaborador.class));
                    }
                    if (txtCodEquipamento.getValueWithNull() != null) {
                        item.setEquipamento((Equipamento) facadeGeneric.find(txtCodEquipamento.getValueLong(), Equipamento.class));
                    } else {
                        item.setEquipamento(null);
                    }
                    item = (ItemPropostaGerador) facadeGeneric.persist(item);
                    proposta.getListaEquipamentosGerador().add(item);
                } catch (java.text.ParseException ex) {
                    JFRMLogger.get(MNGPropostaGeradorCad.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ItemPropostaGerador item = (ItemPropostaGerador) facadeGeneric.find(Long.valueOf(txtCodItens.getValue()), ItemPropostaGerador.class);
                try {
                    item.setHorimetroEqpEntrega(txtHorEntrega.getValueDouble());
                    item.setHorimetroEqpRetirada(txtHorRetirada.getValueDouble());
                    item.setDataEntregaEquipamento(txtDataEqpEntrega.getValueWithNull() != null ? sdfFull.parse((txtDataEqpEntrega.getValueWithMask() + " " + txtHorEqpEntrega.getValueWithMask())) : null);
                    item.setDataRetiradaEquipamento(txtDataEqpRetirada.getValueWithNull() != null ? sdfFull.parse((txtDataEqpRetirada.getValueWithMask() + " " + txtHorEqpRetirada.getValueWithMask())) : null);
                    if (cmbNivelCombustivelEqpEntrega.getKeySel() != null) {
                        item.setNivelCombustivelEqpEntrega(ItemPropostaGerador.NivelCombustivel.valueOf(cmbNivelCombustivelEqpEntrega.getKeySel()));
                    }
                    if (cmbNivelCombustivelEqpRetirada.getKeySel() != null) {
                        item.setNivelCombustivelEqpRetirada(ItemPropostaGerador.NivelCombustivel.valueOf(cmbNivelCombustivelEqpRetirada.getKeySel()));
                    }
                    if (cmbColabEntreg.getKeySel() != null) {
                        item.setColaboradorEntregou((Colaborador) facadeGeneric.find(Long.parseLong(cmbColabEntreg.getKeySel()), Colaborador.class));
                    }
                    if (cmbColabRetirou.getKeySel() != null) {
                        item.setColaboradorRetirou((Colaborador) facadeGeneric.find(Long.parseLong(cmbColabRetirou.getKeySel()), Colaborador.class));
                    }
                    if (txtCodEquipamento.getValueWithNull() != null) {
                        item.setEquipamento((Equipamento) facadeGeneric.find(txtCodEquipamento.getValueLong(), Equipamento.class));
                    } else {
                        item.setEquipamento(null);
                    }
                    item = (ItemPropostaGerador) facadeGeneric.merge(item);
                    proposta.getListaEquipamentosGerador().set(proposta.getListaEquipamentosGerador().indexOf(item), item);
                } catch (java.text.ParseException ex) {
                    JFRMLogger.get(MNGPropostaGeradorCad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            proposta = facade.merge(proposta);
            ac.showDialog(Dialog.getMessage(UtilMsg.ALTERADO));
            btClearItens(ac);
            fillGridItens(proposta);
        }
    }

    private void fillCadItens(ItemPropostaGerador elem, ActionComponent ac) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm");
        txtCodItens.setValueLong(elem.getId());
        txtHorEntrega.setValueDouble(elem.getHorimetroEqpEntrega());
        txtHorRetirada.setValueDouble(elem.getHorimetroEqpRetirada());
        txtDataEqpEntrega.setValueWithMask(elem.getDataEntregaEquipamento() != null ? sdf.format(elem.getDataEntregaEquipamento()) : null);
        txtDataEqpRetirada.setValueWithMask(elem.getDataRetiradaEquipamento() != null ? sdf.format(elem.getDataRetiradaEquipamento()) : null);
        txtHorEqpEntrega.setValueWithMask(elem.getDataEntregaEquipamento() != null ? sdfFull.format(elem.getDataEntregaEquipamento()) : null);
        txtHorEqpRetirada.setValueWithMask(elem.getDataRetiradaEquipamento() != null ? sdfFull.format(elem.getDataRetiradaEquipamento()) : null);
        cmbNivelCombustivelEqpEntrega.setKeySel(elem.getNivelCombustivelEqpEntrega() == null ? "" : elem.getNivelCombustivelEqpEntrega().name());
        cmbNivelCombustivelEqpRetirada.setKeySel(elem.getNivelCombustivelEqpRetirada() == null ? "" : elem.getNivelCombustivelEqpRetirada().name());
        if (elem.getColaboradorEntregou() != null) {
            cmbColabEntreg.setKeySel(elem.getColaboradorEntregou().getId().toString());
        } else {
            cmbColabEntreg.reset();
        }
        if (elem.getColaboradorRetirou() != null) {
            cmbColabRetirou.setKeySel(elem.getColaboradorRetirou().getId().toString());
        } else {
            cmbColabRetirou.reset();
        }
        if (elem.getEquipamento() != null) {
            txtCodEquipamento.setValueLong(elem.getEquipamento().getId());
            txtDescEquipamento.setValue(elem.getEquipamento().getDescricao());
            onLostFocusCliente(ac);
        } else {
            txtCodEquipamento.setValue(null);
            txtDescEquipamento.setValue(null);
        }
        ckGeradoWs.setSelected(elem.getGeradoWs());
    }

    public void selectFiltroItens(ActionComponent ac) {
        ItemPropostaGerador elem = (ItemPropostaGerador) facadeGeneric.find(Long.valueOf(gridItens.getKeySel()), ItemPropostaGerador.class);
        if (elem != null) {
            fillCadItens(elem, ac);
        }
    }

    private void fillGridItens(PropostaGerador proposta) {
        gridItens.reset();
        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        GridDocument gd = gridItens.getDocument();

        for (ItemPropostaGerador item : proposta.getListaEquipamentosGerador()) {
            GridRow<String> gr = new GridRow<>();
            gr.setKey(item.getId().toString());
            GridCol<String> gc = new GridCol(item.getId().toString());
            gr.addColumn(gc);
            gc = new GridCol(item.getEquipamento() != null ? item.getEquipamento().getId() + " - " + item.getEquipamento().getDescricao() : "");
            gr.addColumn(gc);
            gc = new GridCol<>(item.getDataEntregaEquipamento() != null ? sdfFull.format(item.getDataEntregaEquipamento()) : "");
            gr.addColumn(gc);
            gc = new GridCol<>(item.getDataRetiradaEquipamento() != null ? sdfFull.format(item.getDataRetiradaEquipamento()) : "");
            gr.addColumn(gc);
            gd.addRow(gr);
        }
    }

    public void removeDocProp(ActionComponent ac) {
        if (gridDocProp.getKeySel() != null) {
            Param p = new Param("key", gridDocProp.getKeySel());
            PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            Upload elem = facadeUpload.find(Long.valueOf(p.getValue()), Upload.class);
            if (elem != null) {
                proposta = removeDoc(elem, proposta);
                gridDocProp.setKeySel(null);
                txtNomeDocProp.reset();
                txtDescArquivoProp.reset();
                fillCad(ac, proposta);
                ac.showDialog(Dialog.getMessage(UtilMsg.EXCLUIDO));
            }
        } else {
            ac.showDialog(Dialog.getMessage(UtilMsg.SELECT_ITEM));
        }
    }

    private PropostaGerador removeDoc(Upload u, PropostaGerador pg) {
        Upload upload = facadeUpload.find(u.getId(), Upload.class);
        PropostaGerador proposta = facade.find(pg.getId(), PropostaGerador.class);
        proposta.getListaDoc().remove(upload);
        proposta = facade.merge(proposta);
        facadeUpload.remove(upload);
        return proposta;
    }

    public void uploadActionDoc(ActionComponent ac) {
        if (ac instanceof ActionUpload) {
            try {
                if (txtCod.getValueWithNull() != null) {
                    PropostaGerador pg = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
                    if (pg != null) {
                        ActionUpload acUp = (ActionUpload) ac;
                        if (acUp.getNumError() == null) {
                            try {
                                Upload upload = new Upload();
                                int size = acUp.getSize().intValue();
                                upload.setContentSize(size);
                                upload.setContentType(acUp.getMime());
                                upload.setDateCad(new Date());
                                upload.setName(acUp.getFilename());
                                FileTempJSF fileTemp = new FileTempJSF(acUp.getFilename());
                                fileTemp.write(acUp.getInputStream());
                                upload.setLocal(Upload.ARMAZENAMENTO.SBGD);
                                UploadContent content = new UploadContent(fileTemp.getBytes());
                                upload.setContent(content);
                                upload.setNameTemp(txtDescArquivoProp.getValue());
                                upload = (Upload) facadeGeneric.persist(upload);
                                pg = saveDoc(upload, pg);
                                hfOptLock.setValue(pg.getOptimisticLock().toString());
                                novoDoc(ac);
                                fillCad(ac, pg);
                            } catch (IOException ex) {
                                Log.getLog().getLogger(MNGPropostaGeradorCad.class).log(Level.SEVERE, String.format("Erro de Leitura do Upload [%s]", ex.getMessage()), ex);
                                ac.showDialog(Dialog.getMessage(String.format("Erro de Leitura do Upload [%s]", ex.getMessage())));
                            }
                        } else {
                            ac.showDialog(Dialog.getMessage(String.format("Erro num[%d] msg[%s] Filename [%s] mime[%s] size[%d] sizeRead[%d]", acUp.getNumError(), acUp.getMsgError(), acUp.getFilename(), acUp.getMime(), acUp.getSize(), acUp.getSizeRead())));
                        }
                        pgbDocProp.setVisible(false);
                        pgbDocProp.setValue(0);
                        fillGridDocumentos(ac);
                    } else {
                        txtCod.setFocus();
                        pgbDocProp.setVisible(false);
                        ac.showDialog(Dialog.getMessage(("Código inválido, Salve a Proposta antes de realizar o upload!")));
                    }
                } else {
                    txtCod.setFocus();
                    pgbDocProp.setVisible(false);
                    ac.showDialog(Dialog.getMessage(("Código inválido, Salve a Proposta antes de realizar o upload!")));
                }
            } catch (NumberFormatException ex) {
                JFRMLogger.get(MNGPropostaGeradorCad.class).log(Level.SEVERE, String.format("Erro de Leitura do Upload [%s]", ex.getMessage(), ex));
                ac.showDialog(Dialog.getMessage(String.format("Erro de Leitura do Upload [%s]", ex.getMessage())));
            }
        }
    }

    public void novoDoc(ActionComponent ac) {
        gridDocProp.setKeySel(null);
        txtNomeDocProp.reset();
        txtDescArquivoProp.reset();
    }

    public PropostaGerador saveDoc(Upload u, PropostaGerador pg) {
        Upload upload = (Upload) facadeGeneric.find(u.getId(), Upload.class);
        PropostaGerador proposta = (PropostaGerador) facadeGeneric.find(pg.getId(), PropostaGerador.class);
        proposta.getListaDoc().add(upload);
        return (PropostaGerador) facadeGeneric.merge(proposta);
    }

    private void fillGridDocumentos(ActionComponent ac) {
        PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
        gridDocProp.reset();
        int count = 1;
        GridDocument gd = gridDocProp.getDocument();
        List<Upload> lista = proposta.getListaDoc();
        for (br.com.mm.jfrm.ger.persist.Upload up : lista) {
            GridRow<String> tr = new GridRow<>();
            tr.setKey(up.getId().toString());
            GridCol<String> tc = new GridCol<>(String.valueOf(count));
            tr.addColumn(tc);
            tc = new GridCol<>(up.getName());
            tr.addColumn(tc);
            tc = new GridCol<>(up.getNameTemp());
            tr.addColumn(tc);
            gd.addRow(tr);
            count++;
        }
    }

    public void downloadDocOs(ActionComponent ac) {
        if (gridDocProp.getKeySel() != null) {
            Param p = new Param("key", gridDocProp.getKeySel());
            Upload upload = facadeUpload.find(Long.valueOf(p.getValue()), Upload.class);
            if (upload.getLocal() == Upload.ARMAZENAMENTO.SBGD) {
                UploadContent content = facadeUpload.findContent(upload);
                ac.download(content.getContent(), upload.getName(), upload.getContentType());
            }
        }
    }

    public void btCancela(ActionComponent ac) {
        if (txtCod.getValueWithNull() != null) {
            PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            validaCancela(ac);
            proposta.setStatus(StatusDocumento.CANCELADO);
            proposta = (PropostaGerador) facadeGeneric.merge(proposta);
            cmbStatus.setKeySel("CANCELADO");
            ac.showDialog(Dialog.getMessage("Registro Cancelado com Sucesso"));
            disableAll(ac);
        } else {
            ac.showDialog(Dialog.getMessage("Cancelamento Não Realizado, Salve Proposta antes!"));
        }
    }

    public void btEmitir(ActionComponent ac) {
        if (txtCod.getValueWithNull() != null) {
            PropostaGerador proposta = facade.find(Long.parseLong(txtCod.getValue()), PropostaGerador.class);
            if (validaBtEmitir(ac)) {
                proposta.setStatus(StatusDocumento.EMITIDO);
                proposta = (PropostaGerador) facadeGeneric.merge(proposta);
                cmbStatus.setKeySel("EMITIDO");
                ac.showDialog(Dialog.getMessage("Emissão realizada com Sucesso!"));
                disableAll(ac);
            } else {
                ac.showDialog(Dialog.getMessage("Emissão Não Realizada, Salve Proposta antes!"));
            }
        }
    }

    private boolean validaBtEmitir(ActionComponent ac) {
        if (txtCod.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage(("Proposta precisa ser salva antes")));
            return false;
        }
        return true;
    }

    private boolean validaCancela(ActionComponent ac) {
        if (txtCod.getValueWithNull() == null) {
            ac.showDialog(Dialog.getMessage(("Proposta precisa ser salva antes")));
            return false;
        }
        return true;
    }

    private void disableAll(ActionComponent ac) {
        txtCod.setDisabled(true);
        txtDataProposta.setDisabled(true);
        txtHorProposta.setDisabled(true);
        cmbElaborou.setDisabled(true);
        cmbStatus.setDisabled(true);
        cmbResponsavel.setDisabled(true);
        txtCodCliente.setDisabled(true);
        txtHorProposta.setDisabled(true);
        txtHorEqpRetirada.setDisabled(true);
        txtHorEqpEntrega.setDisabled(true);
        txtNomeDocProp.setDisabled(true);
        txtDescArquivoProp.setDisabled(true);
        txtCodEquipamento.setDisabled(true);
        txtCodItens.setDisabled(true);
        txtMunicipio.setDisabled(true);
        txtEndereco.setDisabled(true);
        txtBairro.setDisabled(true);
        txtDescArquivoProp.setDisabled(true);
        txtNomeDocProp.setDisabled(true);
        btEmitir.setDisabled(true);
        txtNumTel.setDisabled(true);
        txtValorTotal.setDisabled(true);
        txtObs.setDisabled(true);
        txtUf.setDisabled(true);
        txtNumeroEnd.setDisabled(true);
        txtNumCep.setDisabled(true);
        txtDataAssinatura.setDisabled(true);
        txtDataEntrega.setDisabled(true);
        txtDataRetirada.setDisabled(true);
        txtHoras.setDisabled(true);
        txtEquipamentos.setDisabled(true);
        txtPotencia.setDisabled(true);
        bthEquipamento.setDisabled(true);
        bthTelCliente.setDisabled(true);
        bthCliente.setDisabled(true);
        bthCep.setDisabled(true);
        cmbNivelCombustivelEqpRetirada.setDisabled(true);
        cmbNivelCombustivelEqpEntrega.setDisabled(true);
        cmbColabRetirou.setDisabled(true);
        cmbColabEntreg.setDisabled(true);
        btSaveItens.setDisabled(true);
        btRemoveItens.setDisabled(true);
        btClearItens.setDisabled(true);
        btExcluiDoc.setDisabled(true);
        txtObsEntrega.setDisabled(true);
        txtDDD.setDisabled(true);
        txtHorEntrega.setDisabled(true);
        txtHorRetirada.setDisabled(true);
        txtDataEqpEntrega.setDisabled(true);
        txtDataEqpRetirada.setDisabled(true);
        upDocProp.setDisabled(true);
        ckAprovado.setDisabled(true);
    }

    private void enableAll(ActionComponent ac) {
        txtCod.setDisabled(false);
        txtDataProposta.setDisabled(false);
        txtHorProposta.setDisabled(false);
        cmbResponsavel.setDisabled(false);
        txtCodCliente.setDisabled(false);
        txtHorProposta.setDisabled(false);
        txtHorEqpRetirada.setDisabled(false);
        txtHorEqpEntrega.setDisabled(false);
        txtNomeDocProp.setDisabled(false);
        txtDescArquivoProp.setDisabled(false);
        txtCodEquipamento.setDisabled(false);
        txtCodItens.setDisabled(false);
        txtMunicipio.setDisabled(false);
        txtEndereco.setDisabled(false);
        txtBairro.setDisabled(false);
        txtDescArquivoProp.setDisabled(false);
        txtNomeDocProp.setDisabled(false);
        btEmitir.setDisabled(false);
        txtNumTel.setDisabled(false);
        txtValorTotal.setDisabled(false);
        txtObs.setDisabled(false);
        txtUf.setDisabled(false);
        txtNumeroEnd.setDisabled(false);
        txtNumCep.setDisabled(false);
        txtDataAssinatura.setDisabled(false);
        txtDataEntrega.setDisabled(false);
        txtDataRetirada.setDisabled(false);
        txtHoras.setDisabled(false);
        txtEquipamentos.setDisabled(false);
        txtPotencia.setDisabled(false);
        bthEquipamento.setDisabled(false);
        bthTelCliente.setDisabled(false);
        bthCliente.setDisabled(false);
        bthCep.setDisabled(false);
        cmbNivelCombustivelEqpRetirada.setDisabled(false);
        cmbNivelCombustivelEqpEntrega.setDisabled(false);
        cmbColabRetirou.setDisabled(false);
        cmbColabEntreg.setDisabled(false);
        btSaveItens.setDisabled(false);
        btRemoveItens.setDisabled(false);
        btClearItens.setDisabled(false);
        btExcluiDoc.setDisabled(false);
        txtObsEntrega.setDisabled(false);
        txtDDD.setDisabled(false);
        txtHorEntrega.setDisabled(false);
        txtHorRetirada.setDisabled(false);
        txtDataEqpEntrega.setDisabled(false);
        txtDataEqpRetirada.setDisabled(false);
        upDocProp.setDisabled(false);
        ckAprovado.setDisabled(false);
    }
}