package cadastro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class ClienteFormulario extends JDialog {

    private final JTextField campoNome = new JTextField(30);
    private final JTextField campoEmail = new JTextField(30);
    private final JTextField campoTelefone = new JTextField(20);

    private final JButton botaoSalvar = new JButton("Salvar");
    private final JButton botaoCancelar = new JButton("Cancelar");

    private final ClienteDAO dao = new ClienteDAO();

    private Cliente clienteEdicao;

    private boolean confirmado = false;

    private final Font FONTE_ROTULO = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONTE_CAMPO = new Font("SansSerif", Font.PLAIN, 16);
    private final Font FONTE_BOTAO = new Font("SansSerif", Font.BOLD, 16);
    private final Font FONTE_TITULO = new Font("SansSerif", Font.BOLD, 18);

    public ClienteFormulario(Frame dono, Cliente clienteEdicao) {
        super(dono, true);
        this.clienteEdicao = clienteEdicao;
        setTitle(clienteEdicao == null ? "Novo Cliente" : "Editar Cliente");
        setMinimumSize(new Dimension(720, 320));
        setLocationRelativeTo(dono);
        setLayout(new BorderLayout(0, 0));
        setResizable(false);
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(new TitledBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(12, 12, 0, 12),
                        BorderFactory.createLineBorder(new Color(220, 220, 220))
                ),
                "Dados do Cliente",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_TITULO
        ));

        painelCampos.setBackground(Color.white);
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(12, 12, 6, 12);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0;
        JLabel lblNome = new JLabel("Nome: ");
        JLabel lblEmail = new JLabel("E-mail: ");
        JLabel lblTelefone = new JLabel("Telefone (opcional): ");
        lblNome.setFont(FONTE_ROTULO);
        lblEmail.setFont(FONTE_ROTULO);
        lblTelefone.setFont(FONTE_ROTULO);
        campoNome.setFont(FONTE_CAMPO);
        campoEmail.setFont(FONTE_CAMPO);
        campoTelefone.setFont(FONTE_CAMPO);
        campoNome.setPreferredSize(new Dimension(420, 36));
        campoEmail.setPreferredSize(new Dimension(420, 36));
        campoTelefone.setPreferredSize(new Dimension(260, 36));
        campoNome.setToolTipText("Informe o nome completo (obrigatório).");
        campoEmail.setToolTipText("Informe um email válido.");
        campoTelefone.setToolTipText("Opcional ex: (11) 99999-9999");

        gc.anchor = GridBagConstraints.EAST;
        gc.gridx = 0;
        gc.gridy = 0;
        painelCampos.add(lblNome, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        painelCampos.add(lblEmail, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        painelCampos.add(lblTelefone, gc);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 1;
        painelCampos.add(campoNome, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        gc.weightx = 1;
        painelCampos.add(campoEmail, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.weightx = 1;
        painelCampos.add(campoTelefone, gc);

        add(painelCampos, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.setBorder(new EmptyBorder(10, 12, 12, 12));
        painelRodape.setBackground(new Color(245, 245, 245));
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        botoes.setOpaque(false);
        botaoSalvar.setFont(FONTE_BOTAO);
        botaoCancelar.setFont(FONTE_BOTAO);
        botaoSalvar.setMnemonic('S');
        botaoCancelar.setMnemonic('C');
        botaoSalvar.setToolTipText("Salvar (Enter).");
        botaoCancelar.setToolTipText("Cancelar (Esc).");
        botoes.add(botaoSalvar);
        botoes.add(botaoCancelar);

        painelRodape.add(botoes, BorderLayout.EAST);
        add(painelRodape, BorderLayout.SOUTH);
        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(this::salvarAcao);

        getRootPane().setDefaultButton(botaoSalvar);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "Fechar");


        getRootPane().getActionMap().put("Fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();


            }
        });

        if (clienteEdicao != null) preencherCampos();

        SwingUtilities.invokeLater(() -> campoNome.requestFocusInWindow());


    }

    private void preencherCampos() {
        campoNome.setText(clienteEdicao.getNome());
        campoEmail.setText(clienteEdicao.getEmail());
        campoTelefone.setText(clienteEdicao.getTelefone());
    }

    private void salvarAcao(ActionEvent ev) {
        salvar();
    }

    private void salvar() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String telefone = campoTelefone.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.", "Validação", JOptionPane.WARNING_MESSAGE);

            campoNome.requestFocus();
            return;
        }

        if (!ValidadorEmail.ehValido(email)) {
            JOptionPane.showMessageDialog(this, "E-mail inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
            campoEmail.requestFocus();
            return;
        }
        try {

            if (clienteEdicao == null) {
                Cliente novo = new Cliente(nome, email, telefone.isEmpty() ? null : telefone);
                dao.inserir(novo);
            } else {
                clienteEdicao.setNome(nome);
                clienteEdicao.setEmail(email);
                clienteEdicao.setTelefone(telefone.isEmpty() ? null : telefone);

                dao.atualizar(clienteEdicao);
            }

            confirmado = true;
            dispose();

        } catch (SQLException e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("duplicate")) {
                JOptionPane.showMessageDialog(this, "Já existe um cliente com este -e-mail.",
                        "Erro de Banco", JOptionPane.ERROR_MESSAGE);

                campoEmail.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Erro de Banco: " + msg,
                        "Erro de Banco", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public boolean foiConfirmado() {
        return confirmado;
    }


}
