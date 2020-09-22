package lexico;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        
        Dicionario dici = new Dicionario();

        LerArq arq = new LerArq();

        List<String> dados = arq.ler("C:\\Users\\danil\\OneDrive\\Área de Trabalho\\test.txt");

        Analisador al = new Analisador(dados);

        List<Simbolo> tabela = al.Lexico();

        //al.Lexico();
        
        for (Simbolo simbolo : tabela) {
            System.out.println(simbolo.token + " " + simbolo.classificacao + " " + simbolo.linha);
           
        }

        
        /*String s = "{";

        if(s.equals("{")){
            System.out.printf("Chaves");
        }*/
        
        
    }
    
}
