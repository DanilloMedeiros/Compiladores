package semantico;

import java.util.LinkedList;

import lexico.Dicionario;

public class Semantico {

    int escopo = 0;

    public LinkedList<Identificador> pilhaIdentificadores = new LinkedList<>();
    public String MARCA_CONTEXTO = "$";

    public Semantico() {
        this.pilhaIdentificadores = new LinkedList<>();
    }

    public boolean pilhaVazia() {
        return pilhaIdentificadores.isEmpty();
    }

    public boolean entrada() {
        return true;
    }

    public void pushId(Identificador id) {
        if (!declaracao(id))
            pilhaIdentificadores.push(id);
    }

    public void addTipoNasVariaveis(String tipo) {
        int j = 0;

        while ((j < (pilhaIdentificadores.size()) && (pilhaIdentificadores.get(j).tipo.equals("")))) {
            pilhaIdentificadores.get(j++).tipo = tipo;
        }
    }

    public boolean declaracao(Identificador id) {
        try {

            int index = 0;

            String nome = pilhaIdentificadores.get(index).nome;
            String tipo = pilhaIdentificadores.get(index).tipo;

            while (!nome.equals(MARCA_CONTEXTO)) {

                if (nome.equals(id.nome)) {

                    System.out.println("Variavel " + id.nome + "já foi declarada");
                    return true;

                }

                index++;

                nome = pilhaIdentificadores.get(index).nome;
                tipo = pilhaIdentificadores.get(index).tipo;
            }
        } catch (Exception e) {
            System.out.println("Erro em declarações");
            System.out.println(e);
        }

        return false;
    }

    public void escopoAberto() {
        escopo++;

    }

    public void escopoFechado() {
        escopo--;
        if (escopo == 0) {
            desempilha();
        }
    }

    public boolean procura(Identificador id) {

        int index = 0;
        int len = pilhaIdentificadores.size();
        String nome, tipo;

        while (index < len) {
            // nome do identificado atual verificado dinamicamente na pilha

            nome = pilhaIdentificadores.get(index).nome;
            tipo = pilhaIdentificadores.get(index).tipo;

            // o identificador foi declado em algum local
            if (nome.equals(id.nome)) {
                System.out.println("Variável " + id.nome + "é usada, mas não foi declarada");
                return true;
            }
            index++;
        }

        return false;
    }

    public void desempilha() {

        // desempilha até encontrar a delimitacao de contexto
        while (!isEquals(pilhaIdentificadores.get(0).nome, MARCA_CONTEXTO)) {
            pilhaIdentificadores.pop();
        }

        // desempilha a delimitacao de contexto
        pilhaIdentificadores.pop();
    }

    public boolean addProcedimentoPilha(Identificador id) {

        switch (id.tipo) {
            case "program":
                addContexto(); // ADD MARK MA PILHA
                return true;

            case "procedure":

                return true;

            default:
                System.out.println("erro no Adiciona procedimento na pilha: " + id.nome + " " + id.tipo);
                return true;
        }

    }

    public void addContexto() {
        pilhaIdentificadores.push(new Identificador(MARCA_CONTEXTO, MARCA_CONTEXTO));
    }

    public boolean isEquals(String str1, String str2) {
        try {
            System.out.println(str1);
            System.out.println(str2);
            return str1.equals(str2);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return str1.equals(str2);
    }

}
