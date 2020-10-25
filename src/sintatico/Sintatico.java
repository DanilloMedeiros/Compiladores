package sintatico;

import java.util.List;
import lexico.*;

public class Sintatico {

    List<Simbolo> tabela;

    public Sintatico(List<Simbolo> tabela) {
        this.tabela = tabela;
    }

    public void analisar() {
        for (Simbolo simbolo : tabela) {
            System.out.println(simbolo.toString());
        }
    }

}
