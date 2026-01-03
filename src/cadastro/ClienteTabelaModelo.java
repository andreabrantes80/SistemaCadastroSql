package cadastro;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClienteTabelaModelo extends AbstractTableModel {

    private final String[] colunas = {"ID", "Nome", "E-mail", "Telefone", "Criado em"};

    private final List<Cliente> dados = new ArrayList<>();

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public int getRowCount(){
        return dados.size();
    }

    @Override
    public int getColumnCount(){
        return colunas.length;
    }

    @Override
    public String getColumnName(int column){
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        Cliente c =dados.get(rowIndex);
        return  switch (columnIndex){
            case 0 -> c.getId();

            case 1 -> c.getNome();

            case 2 -> c.getEmail();

            case 3 -> c.getTelefone();

            case 4 -> c.getCriadoEm() == null ? "-" : c.getCriadoEm().format(fmt);

            default -> "";


        };
    }

    public void substituirDados(List<Cliente> novos){
        dados.clear();

        if(novos != null) dados.addAll(novos);

        fireTableDataChanged();
    }

    public Cliente getClientEm(int linha){
        return dados.get(linha);
    }

    public void removerEm(int linha){
        dados.remove(linha);
        fireTableRowsDeleted(linha, linha);

    }

    public List<Cliente> getDados(){
        return new ArrayList<>(dados);
    }

    //prox aula 494 22-12-25


}
