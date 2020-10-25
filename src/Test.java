
import java.util.List;

import lexico.*;

public class Test {
    public static void main(String[] args) {

        LerArq arq = new LerArq();

        List<String> dados = arq.ler("E:\\benchmark-arquivos_testes\\Test1.pas");

        Analisador al = new Analisador(dados);

        List<Simbolo> tabela = al.Lexico();

        for (Simbolo simbolo : tabela) {
            System.out.println(simbolo.token + "\t\t" + simbolo.classificacao + "\t\t" + simbolo.linha);

        }
    }

}
