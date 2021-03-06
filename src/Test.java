
import java.util.List;

import lexico.*;
import sintatico.Sintatico;

public class Test {
    public static void main(String[] args) {

        LerArq arq = new LerArq();

        List<String> dados = arq.ler("E:\\benchmark-arquivos_testes\\Test4.pas");

        Analisador al = new Analisador(dados);
        List<Simbolo> tabela = al.Lexico();

        Sintatico sintatico = new Sintatico(tabela);

        sintatico.analisar();


    }

}
