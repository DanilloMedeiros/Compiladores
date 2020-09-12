package lexico;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        
        LerArq arq = new LerArq();

        List<String> dados = arq.ler("C:\\Users\\danil\\OneDrive\\Área de Trabalho\\test.txt");

        Analisador al = new Analisador(dados);
    }
    
}
