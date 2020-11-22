
import java.util.LinkedList;
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

        // LinkedList<Integer> values = new LinkedList<>();

        // values.push(1);
        // values.push(2);
        // values.push(3);
        // values.push(4);
        // values.push(5);

        // values.pop();

        // for (Integer value : values) {
        //     System.out.println(value);
        // }

    }

}
