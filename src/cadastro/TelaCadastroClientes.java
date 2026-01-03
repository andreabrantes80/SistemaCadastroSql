package cadastro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaCadastroClientes extends JFrame {

    private final JTextField campoFiltroNome = new JTextField(20);
    private final JTextField campoFiltroEmail = new JTextField(20);

    private final JButton botaoPesquisar = new JButton("Pesquisar");

    private final JButton botaoLimparFiltro = new JButton("Limpar Filtro");

    private final ClienteTabelaModelo modeloTabela = new ClienteTabelaModelo();

    private final JTable tabela = new JTable(modeloTabela);

    private final TableRowSorter<ClienteTabelaModelo> ordenador = new TableRowSorter<>(modeloTabela);

    private final JButton botaoNovo = new JButton("Novo");
    private final JButton botaoEditar = new JButton("Editar");
    private final JButton botaoExcluir = new JButton("Excluir");
    private final JButton botaoAtualizar = new JButton("Atualizar");
    private final JButton botaoExportar = new JButton("Exportar");

    private final JLabel rotuloStatus = new JLabel("Pronto");

    private final ClienteDAO dao = new ClienteDAO();

    public TelaCadastroClientes() {
        super("Cadastro de Clientes");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(980, 620);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(8, 8));

        JPanel panielFiltros = new JPanel(new GridBagLayout());

        panielFiltros.setBorder(new EmptyBorder(10, 10, 0, 10));

        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(4, 6, 4, 6);

        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        panielFiltros.add(new JLabel("Filtro por Nome: "), gc);
        gc.gridx = 1;
        gc.gridy = 0;
        panielFiltros.add(campoFiltroNome, gc);
        gc.gridx = 2;
        gc.gridy = 0;
        panielFiltros.add(new JLabel("Filtro por E-mail: "));
        gc.gridx = 3;
        gc.gridy = 0;
        panielFiltros.add(campoFiltroEmail, gc);
        gc.gridx = 4;
        gc.gridy = 0;
        panielFiltros.add(botaoPesquisar, gc);
        gc.gridx = 5;
        gc.gridy = 0;
        panielFiltros.add(botaoLimparFiltro, gc);

        add(panielFiltros, BorderLayout.NORTH);

        tabela.setRowSorter(ordenador);

        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() > -0) {
                    editarSelecionado();
                }
            }
        });

        JScrollPane rolagem = new JScrollPane(tabela);

        rolagem.setBorder(new EmptyBorder(0, 10, 0, 10));

        add(rolagem, BorderLayout.CENTER);

        JPanel panielInferior = new JPanel(new BorderLayout());

        panielInferior.setBorder(new EmptyBorder(0, 10, 10, 10));

        JPanel painelBoteoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));

        painelBoteoes.add(botaoNovo);

        painelBoteoes.add(botaoEditar);

        painelBoteoes.add(botaoExcluir);

        painelBoteoes.add(botaoAtualizar);

        painelBoteoes.add(botaoExportar);

        panielInferior.add(painelBoteoes, BorderLayout.NORTH);

        JPanel barraStatus = new JPanel(new BorderLayout());

        barraStatus.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        rotuloStatus.setBorder(new EmptyBorder(6, 6, 6, 6));

        barraStatus.add(rotuloStatus, BorderLayout.CENTER);

        panielInferior.add(barraStatus, BorderLayout.SOUTH);

        add(panielInferior, BorderLayout.SOUTH);

        botaoNovo.setToolTipText("Cadastrar novo cliente (Inserir)");
        botaoEditar.setToolTipText("Editar cliente selecionado (Enter ou duplo clique)");
        botaoExcluir.setToolTipText("Excluir cliente selecionado (Delete)");
        botaoAtualizar.setToolTipText("Recarregar a lista do banco)");
        botaoExportar.setToolTipText("Exportar dados para excel (.csv) (ctrl + E)");
        botaoPesquisar.setToolTipText("Aplicar filtros de nome/ e-mail)");
        botaoLimparFiltro.setToolTipText("Limpar filtros e mostrar todos");

        JPopupMenu menu = new JPopupMenu();

        JMenuItem miEditar = new JMenuItem("Editar");

        JMenuItem miExcluir = new JMenuItem("Excluir");

        menu.add(miEditar);
        menu.add(miExcluir);

        miEditar.addActionListener(e -> botaoEditar.doClick());
        miExcluir.addActionListener(e -> botaoExcluir.doClick());

        tabela.setComponentPopupMenu(menu);

        InputMap im = tabela.getInputMap(JComponent.WHEN_FOCUSED);

        ActionMap am = tabela.getActionMap();

        im.put(KeyStroke.getKeyStroke("INSERT"), "novo");

        am.put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botaoNovo.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke("ENTER"), "editar");

        am.put("editar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botaoEditar.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke("DELETE"), "excluir");

        am.put("excluir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botaoExcluir.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke("control E"), "exportar");

        am.put("exportar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botaoExportar.doClick();
            }
        });

         botaoPesquisar.addActionListener(this:: acaoPesquisar);

        botaoLimparFiltro.addActionListener(e -> {
            campoFiltroNome.setText("");
            campoFiltroEmail.setText("");

            ordenador.setRowFilter(null);

            atualizarListaCompleta();
        });

        botaoNovo.addActionListener(e -> abrirFormulario(null));
        botaoEditar.addActionListener(e -> editarSelecionado());
        botaoExcluir.addActionListener(e -> excluirSelecionado());
        botaoAtualizar.addActionListener(e -> atualizarListaCompleta());
         botaoExportar.addActionListener(e-> exportarClientes());

        DocumentListener listener = new DocumentListener() {
            private void onChange() {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }
        };

        campoFiltroNome.getDocument().addDocumentListener(listener);

        campoFiltroEmail.getDocument().addDocumentListener(listener);

        atualizarListaCompleta();


    }

    private void acaoPesquisar(ActionEvent e){
        String nome = campoFiltroNome.getText().trim();

        String email = campoFiltroEmail.getText().trim();

        try{
            if(!nome.isEmpty() && email.isEmpty()){
                List<Cliente> lista = dao.buscarPorNome(nome);
                modeloTabela.substituirDados(lista);
                rotuloStatus.setText("Encontrados "+lista.size()+ " resgistros por nome. ");
                return;
            }
            if(nome.isEmpty() && !email.isEmpty()){
                List<Cliente> lista = dao.listarTodos();
                lista.removeIf(c-> c.getEmail() == null || !c.getEmail().toLowerCase().contains(email.toLowerCase()));
                modeloTabela.substituirDados(lista);
                rotuloStatus.setText("Encongtrados "+lista.size()+ " registros(s) por e-mail. ");
                return;
            }

            if(!nome.isEmpty() && !email.isEmpty()){
                List<Cliente> lista = dao.buscarPorNome(nome);
                lista.removeIf(c-> c.getEmail() == null || !c.getEmail().toLowerCase().contains(email.toLowerCase()));
                modeloTabela.substituirDados(lista);
                rotuloStatus.setText("Encontrados "+lista.size()+ " resgistros por nome + e-mail");
                return;
            }

            atualizarListaCompleta();

        }catch (SQLException ex){
            mostrarErroBanco(ex);

        }
    }


    private void atualizarListaCompleta() {
        try {
            List<Cliente> todos = dao.listarTodos();
            modeloTabela.substituirDados(todos);
            rotuloStatus.setText("Total: " + todos.size() + "registro(s).");
        } catch (SQLException ex) {
            mostrarErroBanco(ex);
        }


    }

    private void abrirFormulario(Cliente clienteEdicao) {
        ClienteFormulario form = new ClienteFormulario(this, clienteEdicao);

        form.setVisible(true);
        if (form.foiConfirmado()) {
            atualizarListaCompleta();
        }
    }

    private void editarSelecionado() {
        int linhaView = tabela.getSelectedRow();
        if (linhaView < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela. ", "Atenção",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }
        int linhaModelo = tabela.convertColumnIndexToModel(linhaView);
        Cliente cliente = modeloTabela.getClientEm(linhaModelo);

        abrirFormulario(cliente);
    }

    private void excluirSelecionado() {

        int linhaView = tabela.getSelectedRow();
        if(linhaView < 0 ){
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.",
                    "Atenção",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opc = JOptionPane.showConfirmDialog(this, "Confirmar excluir o cliente selecionado?","Confirmação",
                JOptionPane.YES_NO_OPTION);
        if(opc != JOptionPane.YES_NO_OPTION) return;

        int linhaModelo = tabela.convertRowIndexToModel(linhaView);
        Cliente cliente = modeloTabela.getClientEm(linhaModelo);
        try {
            boolean ok = dao.deletarPorId(cliente.getId());
            if(ok ){
                modeloTabela.removerEm(linhaModelo);
                rotuloStatus.setText("Cliente removido.");
            }else{
                JOptionPane.showMessageDialog(this, "Cliente não encontrado(pode ter sido removido por outro usuário",
                        "info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }catch (SQLException ex){
            mostrarErroBanco(ex);
        }

    }

    private void exportarClientes() {
        if(tabela.getRowCount() == 0){
            JOptionPane.showMessageDialog(this,
                    "Não há dados para exportar!",
                    "Atenção ",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar como excel .csv");
        chooser.setSelectedFile(new File("clientes.csv"));
        if(chooser.showSaveDialog(this)!= JFileChooser.APPROVE_OPTION) return;

        File destino = chooser.getSelectedFile();
        if(!destino.getAbsolutePath().toLowerCase().endsWith(".csv")){
            destino = new File(destino.getAbsolutePath()+ ".csv");

        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try(
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(destino),
                    Charset.forName("windows-1252"));

            BufferedWriter w = new BufferedWriter(osw);

        ){
            w.write("sep=;");
            w.newLine();
            w.write("ID;Nome;E-mail;Telefone;Criado em");
            w.newLine();
            for(int i=0;i< tabela.getRowCount();i++){
                int linhaModelo = tabela.convertRowIndexToModel(i);
                Cliente c = modeloTabela.getClientEm(linhaModelo);
                String linha = s(c.getId()) + ";" +
                        s(c.getNome()) + ";" +
                        s(c.getEmail()) + ";" +
                        s(c.getTelefone()) + ";" +
                        s(c.getCriadoEm() == null ? "" : s(c.getCriadoEm().format(fmt)));
                w.write(linha);
                w.newLine();
            }
        }catch (Exception ex ){
            JOptionPane.showMessageDialog(this, "Falha ao exportar: "+ ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Exportafo com acentuação correta em:\n" + destino.getAbsolutePath(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

    }

private static String s(Object o) {
        if(o ==null) return "";
        String s = String.valueOf(o);
        s =s.replace("\r\n", "\n").replace("\r", "\n");
        boolean precisaAspas = s.indexOf(';') >=0 || s.indexOf('"') >=0 || s.indexOf('\n') >=0;
        if(precisaAspas){
            s = "\""+ s.replace("\"", "\"\"") + "\"";

        }
        return s;
}

    private void mostrarErroBanco(SQLException e) {

        String msg = e.getMessage();
        if (msg != null && msg.toLowerCase().contains("duplicate")) {
            JOptionPane.showMessageDialog(this,
                    "Erro já existe um cliente com este e-mail",
                    "Erro de Banco",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro de Banco: " + msg,
                    "Erro de Banco",
                    JOptionPane.ERROR_MESSAGE);
        }

    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new TelaCadastroClientes().setVisible(true));

    }
}

